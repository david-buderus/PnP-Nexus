import {ActionIcon, Button, Group, Input, Modal, NumberInput, Stack, Text, TextInput, Tooltip} from '@mantine/core';
import {useForm} from '@mantine/form';
import {randomId, useDisclosure} from '@mantine/hooks';
import {useEffect, useMemo, useState} from 'react';
import {useTranslation} from 'react-i18next';
import {Material, MaterialItem} from '../../../api/model';
import {fetchAllMaterials} from '../../../components/Database';
import OverviewPage from '../../../components/OverviewPage';
import {useUniverseContext} from '../../../components/PageBase';
import {
    handleDatabaseInsertErrors,
    handleNetworkErrors,
    handleValidationErrors
} from '../../../components/utils/ErrorUtils';
import {FaRegTrashCan} from 'react-icons/fa6';
import {ItemSelect} from '../../../components/input/ObjectSelect';
import {ExtendedColumnDef} from '../../../components/table/SortableTable';
import {
    getGetAllMaterialsQueryKey,
    useDeleteAllMaterials,
    useInsertAllMaterials,
    useUpdateMaterial
} from '../../../api/material-service/material-service';
import {useQueryClient} from '@tanstack/react-query';

/** Overview over all materials */
export function MaterialOverview() {
    const {t} = useTranslation();
    const {activeUniverse} = useUniverseContext();
    const queryClient = useQueryClient();

    const [toEdit, setToEdit] = useState<Material>(null);
    const [openedAdd, {open: openAdd, close: closeAdd}] = useDisclosure(false);

    const {mutateAsync: deleteMaterial} = useDeleteAllMaterials({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({queryKey: getGetAllMaterialsQueryKey(activeUniverse.id)}),
            onError: handleNetworkErrors
        }
    });

    const columns = useMemo<ExtendedColumnDef<Material, any>[]>(
        () => [
            {
                accessorKey: 'name',
                header: t('name'),
            },
            {
                accessorKey: 'items',
                header: t('items'),
                cell: cell => {
                    const items = cell.getValue<MaterialItem[]>();
                    return items.map(item => item.amount + ' ' + item.item?.name).join(', ');
                },
                filterFn: (row, id, filterValue) => {
                    return row.getValue<MaterialItem[]>(id).some(item => item.item?.name.includes(filterValue));
                }
            }
        ], []);

    return <Stack>
        <OverviewPage
            fetchData={fetchAllMaterials()}
            columns={columns}
            idKey="id"
            identifier="materials"
            deletionDialogTitle={t('item:materialDeletionTitle')}
            onDelete={(universe, materials) => deleteMaterial({
                universe: universe,
                params: {ids: materials.map(material => material.id)}
            })}
            onAdd={openAdd}
            onEdit={s => setToEdit(s)}
        />
        <CreationDialog editMode={false} opened={openedAdd} close={closeAdd} material={null}/>
        <CreationDialog editMode={true} opened={toEdit !== null} close={() => setToEdit(null)} material={toEdit}/>
    </Stack>;
}

function CreationDialog({
    editMode,
    opened,
    close,
    material
}: {
    editMode: boolean,
    opened: boolean,
    close: () => void
    material: Material
}) {
    const {t} = useTranslation();
    const queryClient = useQueryClient();
    const {activeUniverse} = useUniverseContext();

    const form = useForm<Material>({
        mode: 'controlled',
        initialValues: {
            name: '',
            items: []
        }
    });

    useEffect(() => {
        if (!material) {
            return;
        }
        form.setValues(material);
        form.setInitialValues(material);
    }, [material]);

    const {mutateAsync: updateMaterial} = useUpdateMaterial({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({queryKey: getGetAllMaterialsQueryKey(activeUniverse.id)}).then(close),
            onError: handleValidationErrors(form.setErrors)
        }
    });
    const {mutateAsync: insertMaterials} = useInsertAllMaterials({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({queryKey: getGetAllMaterialsQueryKey(activeUniverse.id)}).then(close),
            onError: handleValidationErrors(handleDatabaseInsertErrors(form.setErrors))
        }
    });

    function onSubmit(material: Material) {
        if (editMode) {
            return updateMaterial({
                universe: activeUniverse.id,
                id: material.id,
                data: material
            });
        } else {
            return insertMaterials({
                universe: activeUniverse.id,
                data: [material]
            });
        }
    }

    return <Modal
        opened={opened}
        onClose={close}
        title={editMode ? t('item:materialEditTitle') : t('item:materialCreationTitle')}
        maw={300}
    >
        <form onSubmit={form.onSubmit(onSubmit)}>
            <TextInput
                label={t('name')}
                key={form.key('name')}
                {...form.getInputProps('name')}
            />
            <Input.Label>
                {t('items')}
            </Input.Label>
            {form.getValues().items.length > 0 ? (
                <Group>
                    <Text fw={500} size="sm" style={{flex: 1}} pr={50}>
                        {t('amount')}
                    </Text>
                    <Text fw={500} size="sm" pr={195}>
                        {t('item')}
                    </Text>
                </Group>
            ) : (
                <Text c="dimmed" ta="center">
                    {t('nothing-here')}
                </Text>
            )}
            <Stack gap="xs">
                {form.getValues().items.map((item, index) => {
                    if (!item['key']) {
                        item['key'] = randomId();
                    }

                    return <Group key={'item-' + item['key']} wrap="nowrap">
                        <NumberInput
                            key={form.key(`items.${index}.amount`)}
                            {...form.getInputProps(`items.${index}.amount`)}
                        />
                        <ItemSelect
                            key={form.key(`items.${index}.item`)}
                            {...form.getInputProps(`items.${index}.item`)}
                        />
                        <ActionIcon
                            variant="outline"
                            color="red"
                            size="input-sm"
                            data-testid={'items-sub-' + index}
                            onClick={() => form.removeListItem('items', index)}>
                            <FaRegTrashCan/>
                        </ActionIcon>
                    </Group>;
                })}
            </Stack>
            <Tooltip label={form.errors['items']} disabled={!form.errors['items']}>
                <Button
                    data-testid={'items-add'}
                    onClick={() =>
                        form.insertListItem('items', {
                            amount: 0,
                            item: null,
                            key: randomId()
                        } as MaterialItem)
                    }
                    mt="md"
                    color={form.errors['items'] ? 'red' : undefined}
                >
                    {t('item:addItem')}
                </Button>
            </Tooltip>
            <Group justify="flex-end" mt="md">
                <Button autoFocus variant="outline" onClick={close}>
                    {t('cancel')}
                </Button>
                <Button type="submit">
                    {editMode ? t('edit') : t('add')}
                </Button>
            </Group>
        </form>
    </Modal>;
}
