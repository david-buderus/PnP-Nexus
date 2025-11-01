import {
    ActionIcon,
    Button,
    Group,
    Input,
    Modal,
    NumberInput,
    Paper,
    Select,
    Stack,
    TagsInput,
    Text,
    TextInput,
    Tooltip
} from '@mantine/core';
import {useForm} from '@mantine/form';
import {randomId, useDisclosure} from '@mantine/hooks';
import {ReactNode, useEffect, useMemo} from 'react';
import {useTranslation} from 'react-i18next';
import {EAction, ECastingType, Spell, SpellCast, SpellServiceApi, TagCast, Talent, TalentCast} from '../../../api';
import {fetchAllSpells, fetchAllTags, fetchAllTalents, IResourceUsage} from '../../../components/Database';
import OverviewPage, {ExtendedColumnDef} from '../../../components/OverviewPage';
import {useUniverseContext} from '../../../components/PageBase';
import {handleDatabaseInsertErrors, handleValidationErrors} from '../../../components/utils/ErrorUtils';
import {API_CONFIGURATION} from '../../../components/Constants';
import {FaRegTrashCan} from 'react-icons/fa6';
import {ObjectMultiSelect, ResourceSelect} from '../../../components/input/ObjectSelect';
import {addTypeAnnotationToUsage} from '../crafting/crafting-recipes';
import TagCell from '../../../components/table/TagCell';
import {resourceFormatter} from '../../../components/utils/Formatters';
import {ActionSelect, CastingTypeMultiSelect} from '../../../components/input/EnumSelect';
import TagRequirementsInput from '../../../components/input/TagRequirementsInput';

const SPELL_API = new SpellServiceApi(API_CONFIGURATION);

/** Overview over all spells */
export function SpellOverview() {
    const {t} = useTranslation();

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
                Cell: cell => {
                    const items = cell.cell.getValue<IResourceUsage[]>();
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
                filterVariant: 'select',
                mantineFilterMultiSelectProps: {
                    data: Object.values(EAction).map(action => {
                        return {
                            label: t('enum:' + action.toLowerCase()),
                            value: action
                        };
                    }),
                },
                Cell: cell => t('enum:' + cell.cell.getValue().toLowerCase())
            },
            {
                accessorKey: 'tags',
                header: t('tags'),
                Cell: TagCell,
                filterFn: (row, id, filterValue) =>
                    row.getValue<string[]>(id).some(tag => tag.includes(filterValue))
            },
            {
                accessorKey: 'cast',
                header: t('spell:cast'),
                Cell: (cell): ReactNode => {
                    const cast: SpellCast = cell.cell.getValue();
                    if (!cast) {
                        return '';
                    }
                    if (cast['@type'] === 'TalentCast') {
                        return (cast as TalentCast).talents.map(o => o?.name ?? '-').join(', ');
                    } else {
                        return (cast as TagCast).tagRequirement.tagRequirements.map(tags => tags.join(', ')).join(' ' + t('or') + ' ');
                    }
                },
                filterFn: (row, id, filterValue) => {
                    const cast = row.getValue<SpellCast>(id);
                    if (!cast) {
                        return !!filterValue;
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
                filterVariant: 'multi-select',
                mantineFilterMultiSelectProps: {
                    data: Object.values(ECastingType).map(type => {
                        return {
                            label: t('enum:' + type.toLowerCase()),
                            value: type
                        };
                    }),
                },
                Cell: cell => cell.cell.getValue<ECastingType[]>()?.map(type => t('enum:' + type.toLowerCase())).join(', '),
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

    return <OverviewPage
        fetchData={fetchAllSpells()}
        columns={columns}
        identifier="spells"
        manipulationDialog={(editMode, refresh, disabled, getInitial) => <CreationDialog editMode={editMode}
                                                                                         refresh={refresh}
                                                                                         disabled={disabled}
                                                                                         getInitial={getInitial}/>}
        deletionDialogTitle={t('spell:editTitle')}
        onDelete={(universe, spells) => SPELL_API.deleteAllSpells(universe, spells.map(spell => spell.id))}
        idKey="id"
    />;
}

function CreationDialog({
    editMode,
    refresh,
    disabled,
    getInitial
}: {
    editMode: boolean,
    refresh: () => void;
    disabled: boolean;
    getInitial: () => Spell;
}) {
    const {t} = useTranslation();
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
            action: EAction.Action,
            castingTypes: [],
            countermeasures: '',
            cooldown: 1
        }
    });
    const castType = form.getValues().cast['@type'];

    useEffect(() => {
        if (!editMode || !opened) {
            return;
        }
        form.setValues(getInitial());
    }, [opened, getInitial, editMode]);

    function onSubmit(spell: Spell) {
        if (editMode) {
            SPELL_API.updateSpell(activeUniverse.name, spell.id, addTypeAnnotationToSpell(spell)).then(refresh).then(close)
                .catch(handleValidationErrors(form.setErrors));
        } else {
            SPELL_API.insertAllSpells(activeUniverse.name, [addTypeAnnotationToSpell(spell)]).then(refresh).then(close)
                .catch(handleValidationErrors(handleDatabaseInsertErrors(form.setErrors)));
        }
    }

    return <>
        <Modal opened={opened} onClose={close} title={editMode ? t('spell:editTitle') : t('spell:creationTitle')}
               maw={300}>
            <form onSubmit={form.onSubmit(onSubmit)}>
                <TextInput
                    label={t('name')}
                    key={form.key('name')}
                    {...form.getInputProps('name')}
                />
                <TextInput
                    label={t('effect')}
                    key={form.key('effect')}
                    {...form.getInputProps('effect')}
                />
                <Input.Label>
                    {t('spell:cast')}
                </Input.Label>
                <Paper shadow="md" p="sm">
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
                            key={form.key('cast.tagRequirement')}
                            {...form.getInputProps('cast.tagRequirement')}
                        />
                    }
                </Paper>
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
                <NumberInput
                    label={t('tier')}
                    key={form.key('tier')}
                    {...form.getInputProps('tier')}
                    allowDecimal={false}
                />
                <Input.Label>
                    {t('spell:cost')}
                </Input.Label>
                <Paper shadow="md" p="xs">
                    {form.getValues().cost.length > 0 ? (
                        <Group>
                            <Text fw={500} size="sm" style={{flex: 1}} pr={50}>
                                {t('amount')}
                            </Text>
                            <Text fw={500} size="sm" pr={175}>
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
                                <ActionIcon variant="outline" color="red" size="input-sm"
                                            onClick={() => form.removeListItem('cost', index)}>
                                    <FaRegTrashCan/>
                                </ActionIcon>
                            </Group>;
                        })}
                    </Stack>
                </Paper>
                <Tooltip label={form.errors.cost} disabled={!form.errors.cost}>
                    <Button
                        onClick={() =>
                            form.insertListItem('cost', {
                                amount: 0,
                                resource: null,
                                key: randomId()
                            })
                        }
                        mt="md"
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
                <TextInput
                    label={t('spell:countermeasures')}
                    key={form.key('countermeasures')}
                    {...form.getInputProps('countermeasures')}
                />
                <TagsInput
                    label={t('tags')}
                    data={tags}
                    clearable
                    key={form.key('tags')}
                    {...form.getInputProps('tags')}
                />
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
