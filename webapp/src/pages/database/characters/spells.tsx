import {
    ActionIcon,
    Button,
    Flex,
    Group,
    Input,
    Modal,
    NumberInput,
    Paper,
    Select,
    Stack,
    TagsInput,
    Text,
    Textarea,
    TextInput,
    Tooltip
} from '@mantine/core';
import {useForm} from '@mantine/form';
import {randomId, useDisclosure} from '@mantine/hooks';
import {ReactNode, useEffect, useMemo} from 'react';
import {useTranslation} from 'react-i18next';
import {
    EAction,
    ECastingType,
    Spell,
    SpellCast,
    type SpellCostItem,
    TagCast,
    Talent,
    TalentCast
} from '../../../api/model';
import {fetchAllSpells, fetchAllTags, fetchAllTalents, IResourceUsage} from '../../../components/Database';
import OverviewPage from '../../../components/OverviewPage';
import {useUniverseContext} from '../../../components/PageBase';
import {
    handleDatabaseInsertErrors,
    handleNetworkErrors,
    handleValidationErrors
} from '../../../components/utils/ErrorUtils';
import {FaRegTrashCan} from 'react-icons/fa6';
import {ObjectMultiSelect, ResourceSelect} from '../../../components/input/ObjectSelect';
import {addTypeAnnotationToUsage} from '../crafting/crafting-recipes';
import TagCell from '../../../components/table/TagCell';
import {resourceFormatter, spellCastFormatter} from '../../../components/utils/Formatters';
import {ActionSelect, CastingTypeMultiSelect} from '../../../components/input/EnumSelect';
import TagRequirementsInput from '../../../components/input/TagRequirementsInput';
import {ExtendedColumnDef} from '../../../components/table/SortableTable';
import {useQueryClient} from '@tanstack/react-query';
import {getGetAllSpeciessQueryKey} from '../../../api/species-service/species-service';
import {
    getGetAllSpellsQueryKey,
    useDeleteAllSpells,
    useInsertAllSpells,
    useUpdateSpell
} from '../../../api/spell-service/spell-service';


/** Overview over all spells */
export function SpellOverview() {
    const {t} = useTranslation();
    const {activeUniverse} = useUniverseContext();
    const queryClient = useQueryClient();

    const {mutateAsync: deleteSpells} = useDeleteAllSpells({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({queryKey: getGetAllSpellsQueryKey(activeUniverse.id)}),
            onError: handleNetworkErrors
        }
    });

    const columns = useMemo<ExtendedColumnDef<Spell, any>[]>(
        () => [
            {
                accessorKey: 'name',
                header: t('name'),
            },
            {
                accessorKey: 'effect',
                header: t('effect'),
            },
            {
                accessorKey: 'cost',
                header: t('spell:cost'),
                cell: cell => {
                    const items = cell.getValue<IResourceUsage[]>();
                    return items.map(resourceFormatter).join(', ');
                },
                filterFn: (row, id, filterValue) => {
                    return row.getValue<IResourceUsage[]>(id).some(item => item.resource?.name.includes(filterValue));
                }
            },
            {
                accessorKey: 'additionalCost',
                header: t('spell:additionalCost'),
            },
            {
                accessorKey: 'castTime',
                header: t('spell:castTime'),
            },
            {
                accessorKey: 'cooldown',
                header: t('spell:cooldown'),
            },
            {
                accessorKey: 'action',
                header: t('enum:action'),
                cell: cell => t('enum:' + cell.getValue().toLowerCase())
            },
            {
                accessorKey: 'tags',
                header: t('tags'),
                cell: TagCell,
                filterFn: (row, id, filterValue) =>
                    row.getValue<string[]>(id).some(tag => tag.includes(filterValue))
            },
            {
                accessorKey: 'cast',
                header: t('spell:cast'),
                cell: (cell): ReactNode => spellCastFormatter(cell.cell.getValue(), t),
                filterFn: (row, id, filterValue) => {
                    const cast = row.getValue<SpellCast>(id);
                    if (!cast) {
                        return Boolean(filterValue);
                    }
                    if (cast['@type'] === 'TalentCast') {
                        return (cast as TalentCast).talents.some(o => o?.name?.includes(filterValue));
                    } else {
                        return (cast as TagCast).tagRequirement.tagRequirements.some(tags => tags.some(tag => tag.includes(filterValue)));
                    }
                }
            },
            {
                accessorKey: 'castingTypes',
                header: t('spell:castingTypes'),
                cell: cell => cell.getValue<ECastingType[]>()?.map(type => t('enum:' + type.toLowerCase())).join(', '),
                filterFn: (row, id, filterValue: ECastingType[]) =>
                    filterValue.every(val => row.getValue<ECastingType[]>(id).includes(val)),
                defaultHidden: true
            },
            {
                accessorKey: 'tier',
                header: t('tier'),
            },
            {
                accessorKey: 'countermeasures',
                header: t('spell:countermeasures'),
                defaultHidden: true
            }
        ], []);

    return <Stack>
        <OverviewPage
            fetchData={fetchAllSpells()}
            columns={columns}
            identifier="spells"
            manipulationDialog={(editMode, refresh, disabled, getInitial) => <CreationDialog
                editMode={editMode}
                disabled={disabled}
                getInitial={getInitial}
            />}
            deletionDialogTitle={t('spell:editTitle')}
            onDelete={(universe, spells) => deleteSpells({
                universe: universe,
                params: {ids: spells.map(spell => spell.id)}
            })}
            idKey="id"
        />
    </Stack>;
}

function CreationDialog({
    editMode,
    disabled,
    getInitial
}: {
    editMode: boolean,
    disabled: boolean;
    getInitial: () => Spell;
}) {
    const {t} = useTranslation();
    const queryClient = useQueryClient();
    const {activeUniverse} = useUniverseContext();
    const [talents] = fetchAllTalents();
    const [tags] = fetchAllTags();

    const [opened, {open, close}] = useDisclosure(false);
    const form = useForm<Spell>({
        mode: 'controlled',
        initialValues: {
            name: '',
            additionalCost: '',
            castTime: 0,
            cost: [],
            effect: '',
            tags: [],
            cast: {
                // @ts-ignore
                '@type': 'TalentCast',
                talents: [],
                tagRequirement: {
                    tagRequirements: []
                }
            } as TalentCast,
            tier: 1,
            action: EAction.ACTION,
            castingTypes: [],
            countermeasures: '',
            cooldown: 1
        }
    });
    const castType = form.getValues().cast?.['@type'] ?? 'TalentCast';

    useEffect(() => {
        if (!editMode || !opened) {
            return;
        }
        form.setValues({
            ...getInitial(),
            cast: getInitial().cast ?? ({
                '@type': 'TalentCast',
                talents: [],
                tagRequirement: {
                    tagRequirements: []
                }
            } as TalentCast),
        });
    }, [opened, getInitial, editMode]);

    const {mutateAsync: updateSpell} = useUpdateSpell({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({queryKey: getGetAllSpeciessQueryKey(activeUniverse.id)}).then(close),
            onError: handleValidationErrors(form.setErrors)
        }
    });
    const {mutateAsync: insertSpells} = useInsertAllSpells({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({queryKey: getGetAllSpeciessQueryKey(activeUniverse.id)}).then(close),
            onError: handleValidationErrors(handleDatabaseInsertErrors(form.setErrors))
        }
    });

    function onSubmit(spell: Spell) {
        if (editMode) {
            return updateSpell({
                universe: activeUniverse.id,
                id: spell.id,
                data: addTypeAnnotationToSpell(spell)
            });
        } else {
            return insertSpells({
                universe: activeUniverse.id,
                data: [addTypeAnnotationToSpell(spell)]
            });
        }
    }

    return <>
        <Modal opened={opened} onClose={close} title={editMode ? t('spell:editTitle') : t('spell:creationTitle')}
               size="xl">
            <form onSubmit={form.onSubmit(onSubmit)}>
                <TextInput
                    label={t('name')}
                    key={form.key('name')}
                    {...form.getInputProps('name')}
                />
                <Textarea
                    label={t('effect')}
                    key={form.key('effect')}
                    {...form.getInputProps('effect')}
                />
                <Flex justify="center" align="flex-start" wrap="nowrap" gap="md">
                    <Stack justify="flex-start" flex={1}>
                        <Stack gap={0}>
                            <Input.Label>
                                {t('spell:cast')}
                            </Input.Label>
                            <Paper shadow="xs" p="sm">
                                <Select
                                    data={[
                                        {value: 'TalentCast', label: t('talents')},
                                        {value: 'TagCast', label: t('tags')}
                                    ]}
                                    key={form.key('cast.@type')}
                                    {...form.getInputProps('cast.@type')}
                                />
                                {castType === 'TalentCast' ?
                                    <ObjectMultiSelect<Talent>
                                        label={t('talents')}
                                        key={form.key('cast.talents')}
                                        {...form.getInputProps('cast.talents')}
                                        data={talents}
                                        idKey="id"
                                        labelKey="name"
                                    /> :
                                    <TagRequirementsInput
                                        label={t('spell:tagRequirement')}
                                        tooltip={t('spell:tagRequirementTooltip')}
                                        noRequirementsText={t('spell:noRequirements')}
                                        addTagRequirementText={t('spell:addTagRequirement')}
                                        dataTestIdPrefix="cast.tagRequirement."
                                        key={form.key('cast.tagRequirement')}
                                        {...form.getInputProps('cast.tagRequirement')}
                                    />
                                }
                            </Paper>
                        </Stack>
                        <Group wrap="nowrap" grow>
                            <NumberInput
                                label={t('spell:castTime')}
                                key={form.key('castTime')}
                                {...form.getInputProps('castTime')}
                                allowDecimal={false}
                            />
                            <NumberInput
                                label={t('spell:cooldown')}
                                key={form.key('cooldown')}
                                {...form.getInputProps('cooldown')}
                                allowDecimal={false}
                            />
                        </Group>
                        <TextInput
                            label={t('spell:countermeasures')}
                            key={form.key('countermeasures')}
                            {...form.getInputProps('countermeasures')}
                        />
                        <NumberInput
                            label={t('tier')}
                            key={form.key('tier')}
                            {...form.getInputProps('tier')}
                            allowDecimal={false}
                        />
                        <TagsInput
                            label={t('tags')}
                            data={tags}
                            clearable
                            key={form.key('tags')}
                            {...form.getInputProps('tags')}
                        />
                    </Stack>
                    <Stack justify="flex-start" flex={1}>
                        <ActionSelect
                            label={t('enum:action')}
                            key={form.key('action')}
                            {...form.getInputProps('action')}
                        />
                        <CastingTypeMultiSelect
                            label={t('spell:castingTypes')}
                            key={form.key('castingTypes')}
                            {...form.getInputProps('castingTypes')}
                        />
                        <Stack gap={0}>
                            <Input.Label>
                                {t('spell:cost')}
                            </Input.Label>
                            <Paper shadow="md" p="xs">
                                {form.getValues().cost.length > 0 ? (
                                    <Group wrap="nowrap">
                                        <Text fw={500} size="sm" style={{flex: 1}} pr={50}>
                                            {t('amount')}
                                        </Text>
                                        <Text fw={500} size="sm" pr={140}>
                                            {t('crafting:resource')}
                                        </Text>
                                    </Group>
                                ) : (
                                    <Text c="dimmed" ta="center">
                                        {t('nothing-here')}
                                    </Text>
                                )}
                                <Stack gap="xs">
                                    {form.getValues().cost.map((usage, index) => {
                                        if (!usage['key']) {
                                            usage['key'] = randomId();
                                        }

                                        return <Group key={'item-' + usage['key']} wrap="nowrap">
                                            <NumberInput
                                                key={form.key(`cost.${index}.amount`)}
                                                {...form.getInputProps(`cost.${index}.amount`)}
                                            />
                                            <ResourceSelect
                                                key={form.key(`cost.${index}.resource`)}
                                                {...form.getInputProps(`cost.${index}.resource`)}
                                            />
                                            <ActionIcon
                                                variant="outline"
                                                color="red"
                                                size="input-sm"
                                                data-testid={'cost-sub-' + index}
                                                onClick={() => form.removeListItem('cost', index)}
                                            >
                                                <FaRegTrashCan/>
                                            </ActionIcon>
                                        </Group>;
                                    })}
                                </Stack>
                            </Paper>
                        </Stack>
                        <Tooltip label={form.errors.cost} disabled={!form.errors.cost}>
                            <Button
                                onClick={() =>
                                    form.insertListItem('cost', {
                                        amount: 0,
                                        resource: null,
                                        key: randomId()
                                    } as SpellCostItem)
                                }
                                mt="md"
                                data-testid="cost-add"
                                color={form.errors.cost ? 'red' : undefined}
                            >
                                {t('spell:addCost')}
                            </Button>
                        </Tooltip>
                        <TextInput
                            label={t('spell:additionalCost')}
                            key={form.key('additionalCost')}
                            {...form.getInputProps('additionalCost')}
                        />
                    </Stack>
                </Flex>
                <Group justify="flex-end" mt="md">
                    <Button autoFocus variant="outline" onClick={close}>
                        {t('cancel')}
                    </Button>
                    <Button type="submit">
                        {editMode ? t('edit') : t('add')}
                    </Button>
                </Group>
            </form>
        </Modal>
        <Button data-testid={editMode ? 'edit' : 'add'} onClick={open} disabled={disabled}>
            {editMode ? t('edit') : t('add')}
        </Button>
    </>;
}

function addTypeAnnotationToSpell(recipe: Spell): Spell {
    return {
        ...recipe,
        cost: recipe.cost.map(addTypeAnnotationToUsage)
    };
}
