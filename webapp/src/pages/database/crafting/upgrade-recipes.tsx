import {
    ActionIcon,
    Button,
    Group,
    Input,
    Modal,
    NumberInput,
    Paper,
    Stack,
    Text,
    TextInput,
    Tooltip
} from '@mantine/core';
import {useForm} from '@mantine/form';
import {randomId, useDisclosure} from '@mantine/hooks';
import {useEffect, useMemo, useState} from 'react';
import {useTranslation} from 'react-i18next';
import {Upgrade, UpgradeRecipe} from '../../../api/model';
import {fetchAllUpgradeRecipes, fetchAllUpgrades, IResourceUsage} from '../../../components/Database';
import OverviewPage from '../../../components/OverviewPage';
import {useUniverseContext} from '../../../components/PageBase';
import {
    handleDatabaseInsertErrors,
    handleNetworkErrors,
    handleValidationErrors
} from '../../../components/utils/ErrorUtils';
import {FaRegTrashCan} from 'react-icons/fa6';
import {ObjectMultiSelect, ObjectSelect, ResourceSelect} from '../../../components/input/ObjectSelect';
import {resourceFormatter} from '../../../components/utils/Formatters';
import {addTypeAnnotationToUsage} from './crafting-recipes';
import {ExtendedColumnDef} from '../../../components/table/SortableTable';
import {filterMultiNamedCell, filterNamedCell, MultiNamedCell, NamedCell} from '../../../components/table/NamedCell';
import {useQueryClient} from '@tanstack/react-query';
import {
    getGetAllUpgradeRecipesQueryKey,
    useDeleteAllUpgradeRecipes,
    useInsertAllUpgradeRecipes,
    useUpdateUpgradeRecipe
} from '../../../api/upgrade-recipe-service/upgrade-recipe-service';

/** Overview over all upgrade recipes */
export function UpgradeRecipeOverview() {
    const {t} = useTranslation();
    const {activeUniverse} = useUniverseContext();
    const queryClient = useQueryClient();

    const [toEdit, setToEdit] = useState<UpgradeRecipe>(null);
    const [openedAdd, {open: openAdd, close: closeAdd}] = useDisclosure(false);

    const {mutateAsync: deleteRecipes} = useDeleteAllUpgradeRecipes({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({queryKey: getGetAllUpgradeRecipesQueryKey(activeUniverse.id)}),
            onError: handleNetworkErrors
        }
    });

    const columns = useMemo<ExtendedColumnDef<UpgradeRecipe, any>[]>(
        () => [
            {
                accessorKey: 'upgrade',
                header: t('upgrade'),
                cell: NamedCell,
                filterFn: filterNamedCell
            },
            {
                accessorKey: 'requirement',
                header: t('crafting:requirement')
            },
            {
                accessorKey: 'requiredUpgrades',
                header: t('crafting:requiredUpgrades'),
                cell: MultiNamedCell,
                filterFn: filterMultiNamedCell
            },
            {
                accessorKey: 'materials',
                header: t('materials'),
                cell: cell => {
                    const items = cell.getValue<IResourceUsage[]>();
                    return items.map(resourceFormatter).join(', ');
                },
                filterFn: (row, id, filterValue) => {
                    return row.getValue<IResourceUsage[]>(id).some(item => item.resource?.name.includes(filterValue));
                }
            }
        ], []);

    return <Stack>
        <OverviewPage
            fetchData={fetchAllUpgradeRecipes()}
            idKey="id"
            columns={columns}
            identifier="upgrade-recipes"
            deletionDialogTitle={t('crafting:upgradeRecipeDeletionTitle')}
            onDelete={(universe, recipes) => deleteRecipes({
                universe: universe,
                params: {ids: recipes.map(recipe => recipe.id)}
            })}
            onAdd={openAdd}
            onEdit={s => setToEdit(s)}
        />
        <CreationDialog editMode={false} opened={openedAdd} close={closeAdd} recipe={null}/>
        <CreationDialog editMode={true} opened={toEdit !== null} close={() => setToEdit(null)} recipe={toEdit}/>
    </Stack>;
}

function CreationDialog({
    editMode,
    opened,
    close,
    recipe
}: {
    editMode: boolean,
    opened: boolean,
    close: () => void
    recipe: UpgradeRecipe
}) {
    const {t} = useTranslation();
    const queryClient = useQueryClient();
    const {activeUniverse} = useUniverseContext();
    const [upgrades] = fetchAllUpgrades();

    const form = useForm<UpgradeRecipe>({
        mode: 'controlled',
        initialValues: {
            upgrade: null,
            materials: [{
                amount: 1,
                resource: null
            }],
            requirement: '',
            requiredUpgrades: []
        }
    });

    useEffect(() => {
        if (!recipe) {
            return;
        }
        form.setValues(recipe);
        form.setInitialValues(recipe);
    }, [recipe]);

    const {mutateAsync: updateRecipe} = useUpdateUpgradeRecipe({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({queryKey: getGetAllUpgradeRecipesQueryKey(activeUniverse.id)}).then(close),
            onError: handleValidationErrors(form.setErrors)
        }
    });
    const {mutateAsync: insertRecipes} = useInsertAllUpgradeRecipes({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({queryKey: getGetAllUpgradeRecipesQueryKey(activeUniverse.id)}).then(close),
            onError: handleValidationErrors(handleDatabaseInsertErrors(form.setErrors))
        }
    });

    function onSubmit(recipe: UpgradeRecipe) {
        if (editMode) {
            return updateRecipe({
                universe: activeUniverse.id,
                id: recipe.id,
                data: addTypeAnnotationToRecipe(recipe)
            });
        } else {
            return insertRecipes({
                universe: activeUniverse.id,
                data: [addTypeAnnotationToRecipe(recipe)]
            });
        }
    }

    return <Modal
        opened={opened}
        onClose={close}
        title={editMode ? t('crafting:upgradeRecipeEditTitle') : t('crafting:upgradeRecipeCreationTitle')}
        maw={300}
    >
        <form onSubmit={form.onSubmit(onSubmit)}>
            <ObjectSelect<Upgrade>
                label={t('upgrade')}
                key={form.key('upgrade')}
                {...form.getInputProps('upgrade')}
                data={upgrades}
                idKey="id"
                labelKey="name"
            />
            <TextInput
                label={t('crafting:requirement')}
                key={form.key('requirement')}
                {...form.getInputProps('requirement')}
            />
            <ObjectMultiSelect<Upgrade>
                label={t('crafting:requiredUpgrades')}
                key={form.key('requiredUpgrades')}
                {...form.getInputProps('requiredUpgrades')}
                data={upgrades}
                idKey="id"
                labelKey="name"
            />
            <Input.Label>
                {t('materials')}
            </Input.Label>
            <Paper shadow="md" p="sm">
                {form.getValues().materials.length > 0 ? (
                    <Group>
                        <Text fw={500} size="sm" style={{flex: 1}} pr={50}>
                            {t('amount')}
                        </Text>
                        <Text fw={500} size="sm" pr={160}>
                            {t('crafting:resource')}
                        </Text>
                    </Group>
                ) : (
                    <Text c="dimmed" ta="center">
                        {t('nothing-here')}
                    </Text>
                )}
                <Stack gap="xs">
                    {form.getValues().materials.map((resource, index) => {
                        if (!resource['key']) {
                            resource['key'] = randomId();
                        }

                        return <Group key={'material-' + resource['key']} wrap="nowrap" align="flex-start">
                            <NumberInput
                                key={form.key(`materials.${index}.amount`)}
                                {...form.getInputProps(`materials.${index}.amount`)}
                            />
                            <ResourceSelect
                                key={form.key(`materials.${index}.resource`)}
                                {...form.getInputProps(`materials.${index}.resource`)}
                            />
                            <ActionIcon
                                variant="outline"
                                color="red"
                                size="input-sm"
                                data-testid={'materials-sub-' + index}
                                onClick={() => form.removeListItem('materials', index)}>
                                <FaRegTrashCan/>
                            </ActionIcon>
                        </Group>;
                    })}
                </Stack>
                <Tooltip label={form.errors['materials']} disabled={!form.errors['materials']}>
                    <Button
                        onClick={() =>
                            form.insertListItem('materials', {
                                amount: 1,
                                resource: null,
                                key: randomId()
                            })
                        }
                        mt="md"
                        data-testid="materials-add"
                        color={form.errors['materials'] ? 'red' : undefined}
                    >
                        {t('crafting:addResource')}
                    </Button>
                </Tooltip>
            </Paper>
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

function addTypeAnnotationToRecipe(recipe: UpgradeRecipe): UpgradeRecipe {
    return {
        ...recipe,
        materials: recipe.materials.map(addTypeAnnotationToUsage)
    };
}