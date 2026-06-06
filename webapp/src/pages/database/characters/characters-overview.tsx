import {useTranslation} from 'react-i18next';
import React, {useMemo, useState} from 'react';
import OverviewPage from '../../../components/OverviewPage';
import {CharacterDescription, Nation, PnPCharacterDTO, Species} from '../../../api/model';
import {fetchAllCharacters} from '../../../components/Database';
import {ExtendedColumnDef} from '../../../components/table/SortableTable';
import {CharacterEdit} from '../../../components/character/CharacterEdit';
import {useNavigate} from 'react-router-dom';
import {useUniverseContext, useUserContext} from '../../../components/PageBase';
import TruncatedCell from '../../../components/table/TruncatedCell';
import {handleNetworkErrors, handleValidationErrors} from '../../../components/utils/ErrorUtils';
import {
    getGetAllCharactersQueryKey,
    getGetCharacterPermissionsQueryKey,
    useAddCharacterPermission,
    useDeleteAllCharacters,
    useGetCharacterPermissions,
    useRemoveCharacterPermission
} from '../../../api/pn-p-character-service/pn-p-character-service';
import {useQueryClient} from '@tanstack/react-query';
import {IconLicense} from '@tabler/icons-react';
import {ActionIcon, Autocomplete, Button, Divider, Group, Modal, Select, Stack, Table} from '@mantine/core';
import ConfirmationDialog from '../../../components/modal/ConfirmationDialog';
import {FaRegTrashCan} from 'react-icons/fa6';
import {useForm} from '@mantine/form';
import {useGetDisplayNames} from '../../../api/user-service/user-service';


/** An overview over all characters */
export function CharactersOverview() {
    const {t} = useTranslation();
    const queryClient = useQueryClient();
    const {activeUniverse} = useUniverseContext();
    const {userPermissions} = useUserContext();
    const [allCharacters, refreshCharacters, loading] = fetchAllCharacters();
    const [editMode, setEditMode] = useState<boolean>(false);
    const navigate = useNavigate();
    const [permissionCharacter, setPermissionCharacter] = useState<string>(null);

    const columns = useMemo<ExtendedColumnDef<PnPCharacterDTO, any>[]>(
        () => [
            {
                accessorKey: 'description.name',
                header: t('name'),
            },
            {
                accessorKey: 'level.level',
                header: t('character:level'),
                defaultHidden: true
            },
            {
                accessorKey: 'origin.species',
                header: t('species'),
                cell: cell => cell.getValue<Species>()?.name ?? '',
                filterFn: (row, id, filterValue) => {
                    return row.getValue<Species>(id)?.name.includes(filterValue);
                }
            },
            {
                accessorKey: 'origin.nation',
                header: t('nation'),
                cell: cell => cell.getValue<CharacterDescription>()?.name ?? '',
                filterFn: (row, id, filterValue) => {
                    return row.getValue<Nation>(id)?.name.includes(filterValue);
                }
            },
            {
                accessorKey: 'description.backstory',
                header: t('character:backstory'),
                defaultHidden: true,
                cell: TruncatedCell,
                filterFn: () => false
            }
        ], []);

    const {mutateAsync: deleteCharacters} = useDeleteAllCharacters({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({queryKey: getGetAllCharactersQueryKey(activeUniverse.id)}),
            onError: handleNetworkErrors
        }
    });

    if (editMode) {
        return <CharacterEdit
            onCancel={() => {
                setEditMode(false);
                refreshCharacters();
            }}
            onDelete={() => {
                setEditMode(false);
                refreshCharacters();
            }}
        />;
    }

    return <Stack>
        <OverviewPage
            fetchData={[allCharacters, refreshCharacters, loading]}
            columns={columns}
            idKey="id"
            identifier="characters"
            onAdd={() => setEditMode(true)}
            onEdit={c => navigate('/characters/' + c.id + '?universe=' + activeUniverse.id)}
            deletionDialogTitle={t('spell:editTitle')}
            onDelete={(universe, characters) => deleteCharacters({
                universe: universe,
                params: {
                    ids: characters.map(c => c.id)
                }
            })}
            manipulationWithObjectsRights={true}
            additionalContextMenuEntries={data => [
                {
                    key: 'permissions',
                    icon: <IconLicense size={16}/>,
                    title: t('universe:permissions'),
                    onClick: () => setPermissionCharacter(data.id),
                    disabled: !userPermissions.isAdmin && !userPermissions.isActiveUniverseOwner
                        && userPermissions.objectPermissions[data.id] !== 'OWNER'
                }
            ]}
        />
        <PermissionDialog
            characterId={permissionCharacter}
            opened={permissionCharacter !== null}
            close={() => setPermissionCharacter(null)}
        />
    </Stack>;
}

function PermissionDialog({
    opened,
    close,
    characterId
}: {
    opened: boolean;
    close: () => void;
    characterId: string;
}) {
    const {t} = useTranslation();
    const queryClient = useQueryClient();
    const {activeUniverse} = useUniverseContext();
    const permissions = useGetCharacterPermissions(activeUniverse.id, characterId, {
        query: {enabled: Boolean(characterId)}
    }).data?.data ?? [];
    const displayNames = useGetDisplayNames().data?.data ?? [];

    const form = useForm({
        initialValues: {
            displayName: '',
            permission: 'READ'
        }
    });

    const {mutate: addPermission} = useAddCharacterPermission({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({
                queryKey: getGetCharacterPermissionsQueryKey(activeUniverse.id, characterId),
            }),
            onError: err => {
                if (err.response.status !== 404) {
                    handleValidationErrors(form.setErrors)(err);
                    return;
                }
                form.setFieldError('displayName', t('user:unknownUser'));
            }
        }
    });
    const {mutate: removePermission} = useRemoveCharacterPermission({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({
                queryKey: getGetCharacterPermissionsQueryKey(activeUniverse.id, characterId),
            })
        }
    });

    return <Modal opened={opened} onClose={close} title={t('universe:addPermission')}>
        <Stack>
            <Table>
                <Table.Thead>
                    <Table.Tr>
                        <Table.Th>{t('displayName')}</Table.Th>
                        <Table.Th>{t('permission')}</Table.Th>
                        <Table.Th></Table.Th>
                    </Table.Tr>
                </Table.Thead>
                <Table.Tbody>
                    {permissions.map((permission, i) => {
                        return <Table.Tr key={i}>
                            <Table.Td>
                                {permission?.displayName || ''}
                            </Table.Td>
                            <Table.Td>
                                {t('permission:' + permission.dto.permission.toLocaleLowerCase())}
                            </Table.Td>
                            <Table.Td>
                                <ConfirmationDialog
                                    title={t('universe:confirmPermissionDeletionTitle')}
                                    onConfirmation={() => removePermission({
                                        universe: activeUniverse.id,
                                        id: characterId,
                                        params: {
                                            displayName: permission?.displayName,
                                        }
                                    })}
                                    openNode={open => <ActionIcon variant="outline" color="red" onClick={open}>
                                        <FaRegTrashCan/>
                                    </ActionIcon>}
                                />
                            </Table.Td>
                        </Table.Tr>;
                    })}
                </Table.Tbody>
            </Table>
            <Divider/>
            <form
                onSubmit={form.onSubmit((values) => addPermission({
                    universe: activeUniverse.id,
                    id: characterId,
                    params: {displayName: values.displayName, accessPermission: values.permission}
                }))}
            >
                <Stack>
                    <Autocomplete
                        label={t('name')}
                        data={displayNames}
                        key={form.key('displayName')}
                        {...form.getInputProps('displayName')}
                    />
                    <Select
                        label={t('permission')}
                        data={[
                            {
                                value: 'READ',
                                label: t('permission:read')
                            },
                            {
                                value: 'WRITE',
                                label: t('permission:write')
                            },
                            {
                                value: 'OWNER',
                                label: t('permission:owner')
                            }
                        ]}
                        key={form.key('permission')}
                        {...form.getInputProps('permission')}
                    />
                    <Group justify="flex-end">
                        <Button type="submit">
                            {t('add')}
                        </Button>
                    </Group>
                </Stack>
            </form>
        </Stack>
    </Modal>;
}