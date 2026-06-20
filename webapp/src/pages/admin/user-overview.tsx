import {useTranslation} from 'react-i18next';
import {GrantedDatabaseObjectIdAuthorityDTO, PnPUser, PnPUserCreation, RoleAuthorityDTO,} from '../../api/model';
import {useEffect, useMemo, useState} from 'react';
import OverviewPage from '../../components/OverviewPage';
import {Button, Group, Input, Modal, MultiSelect, Paper, PasswordInput, Stack, Switch, TextInput} from '@mantine/core';
import {useForm} from '@mantine/form';
import {useDisclosure} from '@mantine/hooks';
import {handleNetworkErrors, handleValidationErrors} from '../../components/utils/ErrorUtils';
import {useUniverseContext} from '../../components/PageBase';
import {ExtendedColumnDef} from '../../components/table/SortableTable';
import {
    getGetAllUsersQueryKey,
    getGetPermissionsQueryKey,
    getGetUserQueryKey,
    useCreateUser,
    useGetAllUsers,
    useGetPermissions,
    useRemoveUsers,
    useUpdatePermissions,
    useUpdateUser
} from '../../api/user-service/user-service';
import {useQueryClient} from '@tanstack/react-query';


/** Admin overview over all users */
export function UserOverview() {
    const {t} = useTranslation();
    const queryClient = useQueryClient();

    const [toEdit, setToEdit] = useState<PnPUser>(null);
    const [openedAdd, {open: openAdd, close: closeAdd}] = useDisclosure(false);

    const {data, isLoading} = useGetAllUsers();
    const users = data?.data ?? [];

    const {mutate: deleteUsers} = useRemoveUsers({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({queryKey: getGetAllUsersQueryKey()}),
            onError: handleNetworkErrors
        }
    });

    const columns = useMemo<ExtendedColumnDef<PnPUser, any>[]>(
        () => [
            {
                accessorKey: 'username',
                header: t('username'),
            },
            {
                accessorKey: 'displayName',
                header: t('displayName'),
            },
            {
                accessorKey: 'email',
                header: t('email'),
            }
        ], []);

    return <Stack>
        <OverviewPage
            fetchData={[users, () => {
            }, isLoading]}
            columns={columns}
            idKey="username"
            identifier="users"
            deletionDialogTitle={t('user:confirmDeleteUser')}
            onDelete={(_, usersToDelete) => deleteUsers({
                params: {
                    usernames: usersToDelete.map(user => user.username)
                }
            })}
            onAdd={openAdd}
            onEdit={s => setToEdit(s)}
        />
        <CreationDialog opened={openedAdd} close={closeAdd}/>
        <EditDialog opened={toEdit !== null} close={() => setToEdit(null)} initial={toEdit}/>
    </Stack>;
}

function CreationDialog({
    opened, close
}: {
    opened: boolean,
    close: () => void
}) {
    const {t} = useTranslation();
    const queryClient = useQueryClient();

    const form = useForm<PnPUserCreation>({
        mode: 'controlled',
        initialValues: {
            authorities: [],
            displayName: '',
            username: ''
        }
    });

    const {mutate: createUser} = useCreateUser({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({queryKey: getGetAllUsersQueryKey()}).then(close),
            onError: handleValidationErrors(form.setErrors)
        }
    });

    return <Modal opened={opened} onClose={close} title={t('user:createUser')} maw={300}>
        <form onSubmit={form.onSubmit(user => createUser({data: user}))}
        >
            <TextInput
                label={t('username')}
                key={form.key('username')}
                {...form.getInputProps('username')}
            />
            <TextInput
                label={t('displayName')}
                key={form.key('displayName')}
                {...form.getInputProps('displayName')}
            />
            <TextInput
                label={t('email')}
                key={form.key('email')}
                {...form.getInputProps('email')}
            />
            <PasswordInput
                label={t('password')}
                key={form.key('password')}
                {...form.getInputProps('password')}

            />
            <PermissionManipulation
                authorities={form.getValues()?.authorities ?? []}
                setAuthorities={authorities => form.setFieldValue('authorities', authorities)}
            />
            <Group justify="flex-end" mt="md">
                <Button autoFocus variant="outline" onClick={close}>
                    {t('cancel')}
                </Button>
                <Button type="submit">
                    {t('add')}
                </Button>
            </Group>
        </form>
    </Modal>;
}

function EditDialog({
    opened,
    close,
    initial
}: {
    opened: boolean,
    close: () => void
    initial: PnPUser
}) {
    const {t} = useTranslation();
    const queryClient = useQueryClient();

    const form = useForm<PnPUser>({
        mode: 'controlled',
        initialValues: {
            'displayName': '',
            'email': ''
        }
    });
    const [authorities, setAuthorities] = useState([]);
    const {data: permissionResponse} = useGetPermissions(initial?.username, {
        query: {enabled: Boolean(initial?.username)}
    });

    const {mutate: updatePermissions} = useUpdatePermissions({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({queryKey: getGetPermissionsQueryKey(initial.username)}),
            onError: handleValidationErrors(form.setErrors)
        }
    });

    const {mutate: updateUser} = useUpdateUser({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({queryKey: getGetAllUsersQueryKey()})
                .then(() => queryClient.invalidateQueries({queryKey: getGetUserQueryKey(initial.username)}))
                .then(() => updatePermissions({
                    username: initial.username,
                    data: authorities
                }))
                .then(close),
            onError: handleValidationErrors(form.setErrors)
        }
    });

    useEffect(() => {
        if (!initial) {
            return;
        }
        form.setValues(initial);
        setAuthorities(permissionResponse?.data ?? []);
    }, [initial, permissionResponse]);

    return <Modal opened={opened} onClose={close} title={t('user:editUser')} maw={300}>
        <form onSubmit={form.onSubmit(user => updateUser({
            username: user.username,
            data: user
        }))}>
            <TextInput
                label={t('displayName')}
                key={form.key('displayName')}
                {...form.getInputProps('displayName')}
            />
            <TextInput
                label={t('email')}
                key={form.key('email')}
                {...form.getInputProps('email')}
            />
            <PermissionManipulation authorities={authorities} setAuthorities={setAuthorities}/>
            <Group justify="flex-end" mt="md">
                <Button autoFocus variant="outline" onClick={close}>
                    {t('cancel')}
                </Button>
                <Button type="submit">
                    {t('edit')}
                </Button>
            </Group>
        </form>
    </Modal>;
}

function PermissionManipulation({authorities, setAuthorities}: {
    authorities: (GrantedDatabaseObjectIdAuthorityDTO | RoleAuthorityDTO)[];
    setAuthorities: (authorities: (GrantedDatabaseObjectIdAuthorityDTO | RoleAuthorityDTO)[]) => void;
}) {
    const {t} = useTranslation();
    const {universes} = useUniverseContext();
    const universeOptions = useMemo(() => {
        return universes.map(universe => {
            return {label: universe.displayName, value: universe.id};
        });
    }, [universes]);

    function getUniverseRights(right: 'READ' | 'WRITE' | 'OWNER') {
        return authorities.filter(auth => (auth as GrantedDatabaseObjectIdAuthorityDTO)?.permission === right)
            .map(auth => universes.find(opt => opt.id === (auth as GrantedDatabaseObjectIdAuthorityDTO).id))
            .filter(auth => auth !== undefined).map(universe => universe.id);
    }

    const {
        adminRights, universeCreationRights, readRights, writeRights, ownerRights
    } = useMemo(() => ({
        adminRights: authorities.find(auth => (auth as RoleAuthorityDTO)?.role === 'ADMIN') !== undefined,
        universeCreationRights: authorities.find(auth => (auth as RoleAuthorityDTO)?.role === 'UNIVERSE_CREATOR') !== undefined,
        readRights: getUniverseRights('READ'),
        writeRights: getUniverseRights('WRITE'),
        ownerRights: getUniverseRights('OWNER'),
    }), [authorities]);

    function setRole(hasRole: boolean, role: string) {
        const newAuthorities = authorities.filter(auth => (auth as RoleAuthorityDTO)?.role !== role);
        if (hasRole) {
            newAuthorities.push({
                '@type': 'Role',
                'role': role
            } as RoleAuthorityDTO);
        }
        setAuthorities(newAuthorities);
    }

    function setUniverseRights(universesWithPermission: string[], permission: string) {
        const newAuthorities = authorities.filter(auth =>
            !(
                // Remove all universe rights with this permission
                (auth as GrantedDatabaseObjectIdAuthorityDTO)?.permission === permission &&
                universes.map(u => u.id).includes((auth as GrantedDatabaseObjectIdAuthorityDTO).id)
            )
        );
        universesWithPermission.forEach(right => newAuthorities.push({
            '@type': 'DatabaseObjectAuthority',
            id: right,
            permission: permission

        } as GrantedDatabaseObjectIdAuthorityDTO));
        setAuthorities(newAuthorities);
    }

    return <>
        <Input.Label>
            {t('rights')}
        </Input.Label>
        <Paper shadow="sm" p="xs">
            <Stack gap="xs">
                <Switch
                    checked={adminRights}
                    onChange={event => setRole(event.target.checked, 'ADMIN')}
                    label={t('user:adminRights')}
                    data-testid="adminRights"
                />
                <Switch
                    checked={universeCreationRights || adminRights}
                    disabled={adminRights}
                    onChange={event => setRole(event.target.checked, 'UNIVERSE_CREATOR')}
                    label={t('user:universeCreationRights')}
                    data-testid="universeCreationRights"
                />
            </Stack>
            <MultiSelect
                label={t('user:universeReadRights')}
                data={universeOptions}
                value={readRights}
                onChange={universesWithPermission => setUniverseRights(universesWithPermission, 'READ')}
                data-testid="universe-read-rights"
            />
            <MultiSelect
                label={t('user:universeWriteRights')}
                data={universeOptions}
                value={writeRights}
                onChange={universesWithPermission => setUniverseRights(universesWithPermission, 'WRITE')}
                data-testid="universe-write-rights"
            />
            <MultiSelect
                label={t('user:universeOwnerRights')}
                data={universeOptions}
                value={ownerRights}
                onChange={universesWithPermission => setUniverseRights(universesWithPermission, 'OWNER')}
                data-testid="universe-owner-rights"
            />
        </Paper>
    </>;
}