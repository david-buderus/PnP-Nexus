import {useTranslation} from 'react-i18next';
import {
    GrantedDatabaseObjectIdAuthorityDTO,
    PnPUser,
    PnPUserCreation,
    RoleAuthorityDTO,
    UserServiceApi
} from '../../api';
import {API_CONFIGURATION} from '../../components/Constants';
import {useEffect, useMemo, useState} from 'react';
import OverviewPage, {ExtendedColumnDef} from '../../components/OverviewPage';
import {Button, Group, Input, Modal, MultiSelect, Paper, PasswordInput, Stack, Switch, TextInput} from '@mantine/core';
import {useForm} from '@mantine/form';
import {useDisclosure} from '@mantine/hooks';
import {handleValidationErrors} from '../../components/utils/ErrorUtils';
import {useUniverseContext} from '../../components/PageBase';

const USER_API = new UserServiceApi(API_CONFIGURATION);

/** Admin overview over all users */
export function UserOverview() {
    const {t} = useTranslation();

    const [users, setUsers] = useState<PnPUser[]>([]);
    const [loading, setLoading] = useState<boolean>(false);

    const refresh = () => {
        setLoading(true);
        USER_API.getAllUsers().then(response => {
            setLoading(false);
            setUsers(response.data);
        });
    };

    useEffect(() => {
        refresh();
    }, []);

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

    return <OverviewPage
        fetchData={[users, refresh, loading]}
        columns={columns}
        identifier="users"
        manipulationDialog={(editMode, refreshCallback, disabled, getInitial) => {
            if (editMode) {
                return <EditDialog refresh={refreshCallback} disabled={disabled} getInitial={getInitial}/>;
            } else {
                return <CreationDialog refresh={refreshCallback} disabled={disabled}/>;
            }
        }}
        deletionDialogTitle={t('user:confirmDeleteUser')}
        onDelete={(_, usersToDelete) => USER_API.removeUsers(usersToDelete.map(user => user.username))}
        idKey="username"
    />;
}

function CreationDialog({
    refresh,
    disabled
}: {
    refresh: () => void;
    disabled: boolean;
}) {
    const {t} = useTranslation();

    const [opened, {open, close}] = useDisclosure(false);
    const form = useForm<PnPUserCreation>({
        mode: 'controlled',
        initialValues: {
            authorities: [],
            displayName: '',
            username: ''
        }
    });

    return <>
        <Modal opened={opened} onClose={close} title={t('user:createUser')} maw={300}>
            <form onSubmit={form.onSubmit(user => USER_API.createUser(user).then(refresh).then(close)
                .catch(handleValidationErrors(form.setErrors)))}
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
        </Modal>
        <Button data-testid={'add'} onClick={open} disabled={disabled}>
            {t('add')}
        </Button>
    </>;
}

function EditDialog({
    refresh,
    disabled,
    getInitial
}: {
    refresh: () => void;
    disabled: boolean;
    getInitial: () => PnPUser;
}) {
    const {t} = useTranslation();

    const [opened, {open, close}] = useDisclosure(false);
    const form = useForm<PnPUser>({
        mode: 'controlled',
        initialValues: {
            'displayName': '',
            'email': ''
        }
    });
    const [authorities, setAuthorities] = useState([]);

    useEffect(() => {
        if (!opened) {
            return;
        }
        const user = getInitial();
        form.setValues(user);
        USER_API.getPermissions(user.username).then(response => setAuthorities(response.data));
    }, [opened, getInitial]);

    return <>
        <Modal opened={opened} onClose={close} title={t('user:editUser')} maw={300}>
            <form onSubmit={form.onSubmit(user => USER_API.updateUser(user.username, user)
                .then(() => USER_API.updatePermissions(user.username, authorities))
                .then(() => {
                    refresh();
                    close();
                })
                .catch(handleValidationErrors(form.setErrors)))}
            >
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
        </Modal>
        <Button data-testid={'edit'} onClick={open} disabled={disabled}>
            {t('edit')}
        </Button>
    </>;
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