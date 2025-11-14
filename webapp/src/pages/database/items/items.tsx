import {useEffect, useMemo} from 'react';
import {
    fetchAllArmor,
    fetchAllItems,
    fetchAllJewllery,
    fetchAllMaterials,
    fetchAllShields,
    fetchAllTags,
    fetchAllWeapons
} from '../../../components/Database';
import OverviewPage, {ExtendedColumnDef} from '../../../components/OverviewPage';
import {useTranslation} from 'react-i18next';
import TagCell, {filterTagCell} from '../../../components/table/TagCell';
import {Armor, EArmorSlot, ERarity, Item, ItemServiceApi, Jewellery, Material, Shield, Weapon} from '../../../api';
import CurrencyCell from '../../../components/table/CurrencyCell';
import {API_CONFIGURATION} from '../../../components/Constants';
import {useDisclosure} from '@mantine/hooks';
import {Button, Group, Modal, NumberInput, Select, TagsInput, Textarea, TextInput} from '@mantine/core';
import {useForm} from '@mantine/form';
import {useUniverseContext} from '../../../components/PageBase';
import {handleDatabaseInsertErrors, handleValidationErrors} from '../../../components/utils/ErrorUtils';
import {ObjectSelect} from '../../../components/input/ObjectSelect';
import {ArmorSlotSelect, RaritySelect} from '../../../components/input/EnumSelect';
import DiceInput from '../../../components/input/DiceInput';
import {currencyFormatter} from '../../../components/utils/Formatters';
import DiceCell from '../../../components/table/DiceCell';
import {filterNamedCell, NamedCell} from '../../../components/table/NamedCell';

const ITEM_API = new ItemServiceApi(API_CONFIGURATION);
type ItemCombination = Item & Partial<Weapon> & Partial<Shield> & Partial<Armor> & Partial<Jewellery>;

/** Overview over all items */
export function Items() {
    const {t} = useTranslation();

    const columns = useMemo<ExtendedColumnDef<Item, any>[]>(
        () => [
            {
                accessorKey: 'name',
                header: t('name'),
            },
            {
                accessorKey: 'tags',
                header: t('tags'),
                Cell: TagCell,
                filterFn: filterTagCell
            },
            {
                accessorKey: 'requirement',
                header: t('requirement'),
            },
            {
                accessorKey: 'effect',
                header: t('effect'),
            },
            {
                accessorKey: 'rarity',
                header: t('rarity'),
                filterVariant: 'select',
                mantineFilterMultiSelectProps: {
                    data: Object.values(ERarity).map(rarity => {
                        return {
                            label: t('enum:' + rarity.toLowerCase()),
                            value: rarity
                        };
                    }),
                },
                Cell: cell => t('enum:' + cell.cell.getValue().toLowerCase())
            },
            {
                accessorKey: 'vendorPrice',
                header: t('price'),
                Cell: CurrencyCell
            },
            {
                accessorKey: 'tier',
                header: t('tier'),
            },
            {
                accessorKey: 'description',
                header: t('description'),
            },
            {
                accessorKey: 'note',
                header: t('note'),
                defaultHidden: true
            },
            {
                accessorKey: 'maximumStackSize',
                header: t('item:maxStackSize'),
                defaultHidden: true
            },
            {
                accessorKey: 'minimumStackSize',
                header: t('item:minStackSize'),
                defaultHidden: true
            }
        ], []);

    return <OverviewPage
        fetchData={fetchAllItems()}
        columns={columns}
        identifier="items"
        manipulationDialog={(editMode, refresh, disabled, getInitial) => <CreationDialog
            initialType="Item"
            editMode={editMode}
            refresh={refresh}
            disabled={disabled}
            getInitial={getInitial}
        />}
        deletionDialogTitle={t('item:confirmDeletionTitle')}
        onDelete={(universe, items) => ITEM_API.deleteAllItems(universe, items.map(item => item.id))}
        idKey="id"
    />;
}

/** Overview over all weapons */
export function Weapons() {
    const {t} = useTranslation();

    const columns = useMemo<ExtendedColumnDef<Weapon, any>[]>(
        () => [
            {
                accessorKey: 'name',
                header: t('name'),
            },
            {
                accessorKey: 'tags',
                header: t('tags'),
                Cell: TagCell,
                filterFn: (row, id, filterValue) => {
                    return row.getValue<string[]>(id).some(tag => tag.includes(filterValue));
                }
            },
            {
                accessorKey: 'material',
                header: t('material'),
                Cell: NamedCell,
                filterFn: filterNamedCell
            },
            {
                accessorKey: 'damage',
                header: t('damage'),
            },
            {
                accessorKey: 'dice',
                header: t('dice'),
                Cell: DiceCell
            },
            {
                accessorKey: 'hit',
                header: t('hit'),
            },
            {
                accessorKey: 'initiative',
                header: t('initiative'),
            },
            {
                accessorKey: 'requirement',
                header: t('requirement')
            },
            {
                accessorKey: 'effect',
                header: t('effect'),
            },
            {
                accessorKey: 'rarity',
                header: t('rarity'),
                filterVariant: 'select',
                mantineFilterMultiSelectProps: {
                    data: Object.values(ERarity).map(rarity => {
                        return {
                            label: t('enum:' + rarity.toLowerCase()),
                            value: rarity
                        };
                    }),
                },
                Cell: cell => t('enum:' + cell.cell.getValue().toLowerCase())
            },
            {
                accessorKey: 'vendorPrice',
                header: t('price'),
                Cell: CurrencyCell
            },
            {
                accessorKey: 'tier',
                header: t('tier'),
            },
            {
                accessorKey: 'description',
                header: t('description'),
            },
            {
                accessorKey: 'upgradeSlots',
                header: t('upgradeSlots'),
            },
            {
                accessorKey: 'note',
                header: t('note'),
                defaultHidden: true
            },
            {
                accessorKey: 'maximumStackSize',
                header: t('item:maxStackSize'),
                defaultHidden: true
            },
            {
                accessorKey: 'minimumStackSize',
                header: t('item:minStackSize'),
                defaultHidden: true
            }
        ], []);

    return <OverviewPage
        fetchData={fetchAllWeapons()}
        columns={columns}
        identifier="weapons"
        manipulationDialog={(editMode, refresh, disabled, getInitial) => <CreationDialog
            initialType="Weapon"
            editMode={editMode}
            refresh={refresh}
            disabled={disabled}
            getInitial={getInitial}
        />}
        deletionDialogTitle={t('item:confirmDeletionTitle')}
        onDelete={(universe, items) => ITEM_API.deleteAllItems(universe, items.map(item => item.id))}
        idKey="id"
    />;
}

/** Overview over all shields */
export function Shields() {
    const {t} = useTranslation();
    const {itemSettings} = useUniverseContext();

    const columns = useMemo<ExtendedColumnDef<Shield, any>[]>(
        () => {
            const c: ExtendedColumnDef<Shield, any>[] = [
                {
                    accessorKey: 'name',
                    header: t('name'),
                },
                {
                    accessorKey: 'tags',
                    header: t('tags'),
                    Cell: TagCell,
                    filterFn: (row, id, filterValue) => {
                        return row.getValue<string[]>(id).some(tag => tag.includes(filterValue));
                    }
                },
                {
                    accessorKey: 'material',
                    header: t('material'),
                    Cell: NamedCell,
                    filterFn: filterNamedCell
                },
                {
                    accessorKey: 'armor',
                    header: t('armor'),
                },
                {
                    accessorKey: 'weight',
                    header: t('weight'),
                },
                {
                    accessorKey: 'hit',
                    header: t('hit'),
                },
                {
                    accessorKey: 'initiative',
                    header: t('initiative'),
                },
                {
                    accessorKey: 'requirement',
                    header: t('requirement')
                },
                {
                    accessorKey: 'effect',
                    header: t('effect'),
                },
                {
                    accessorKey: 'rarity',
                    header: t('rarity'),
                    filterVariant: 'select',
                    mantineFilterMultiSelectProps: {
                        data: Object.values(ERarity).map(rarity => {
                            return {
                                label: t('enum:' + rarity.toLowerCase()),
                                value: rarity
                            };
                        }),
                    },
                    Cell: cell => t('enum:' + cell.cell.getValue().toLowerCase())
                },
                {
                    accessorKey: 'vendorPrice',
                    header: t('price'),
                    Cell: CurrencyCell
                },
                {
                    accessorKey: 'tier',
                    header: t('tier'),
                },
                {
                    accessorKey: 'description',
                    header: t('description'),
                },
                {
                    accessorKey: 'upgradeSlots',
                    header: t('upgradeSlots'),
                },
                {
                    accessorKey: 'note',
                    header: t('note'),
                    defaultHidden: true
                },
                {
                    accessorKey: 'maximumStackSize',
                    header: t('item:maxStackSize'),
                    defaultHidden: true
                },
                {
                    accessorKey: 'minimumStackSize',
                    header: t('item:minStackSize'),
                    defaultHidden: true
                }
            ];

            if (itemSettings?.usingProtection) {
                c.splice(4, 0, {
                    accessorKey: 'protection',
                    header: t('protection'),
                });
            }
            if (itemSettings?.shieldUsingDice) {
                const offset = itemSettings.usingProtection ? 7 : 6;
                c.splice(offset, 0, {
                    accessorKey: 'dice',
                    header: t('dice'),
                    Cell: DiceCell
                });
            }

            return c;
        }, []);

    return <OverviewPage
        fetchData={fetchAllShields()}
        columns={columns}
        identifier="shields"
        manipulationDialog={(editMode, refresh, disabled, getInitial) => <CreationDialog
            initialType="Shield"
            editMode={editMode}
            refresh={refresh}
            disabled={disabled}
            getInitial={getInitial}
        />}
        deletionDialogTitle={t('item:confirmDeletionTitle')}
        onDelete={(universe, items) => ITEM_API.deleteAllItems(universe, items.map(item => item.id))}
        idKey="id"
    />;
}

/** Overview over all shields */
export function ArmorOverview() {
    const {t} = useTranslation();
    const {itemSettings} = useUniverseContext();

    const columns = useMemo<ExtendedColumnDef<Armor, any>[]>(
        () => {
            const c: ExtendedColumnDef<Armor, any>[] = [
                {
                    accessorKey: 'name',
                    header: t('name'),
                },
                {
                    accessorKey: 'tags',
                    header: t('tags'),
                    Cell: TagCell,
                    filterFn: (row, id, filterValue) => {
                        return row.getValue<string[]>(id).some(tag => tag.includes(filterValue));
                    }
                },
                {
                    accessorKey: 'material',
                    header: t('material'),
                    Cell: NamedCell,
                    filterFn: filterNamedCell
                },
                {
                    accessorKey: 'armorSlot',
                    header: t('item:armorSlot'),
                    filterVariant: 'select',
                    mantineFilterMultiSelectProps: {
                        data: Object.values(EArmorSlot).map(rarity => {
                            return {
                                label: t('enum:' + rarity.toLowerCase()),
                                value: rarity
                            };
                        }),
                    },
                    Cell: cell => t('enum:' + cell.cell.getValue().toLowerCase())
                },
                {
                    accessorKey: 'armor',
                    header: t('armor'),
                },
                {
                    accessorKey: 'weight',
                    header: t('weight'),
                },
                {
                    accessorKey: 'requirement',
                    header: t('requirement')
                },
                {
                    accessorKey: 'effect',
                    header: t('effect'),
                },
                {
                    accessorKey: 'rarity',
                    header: t('rarity'),
                    filterVariant: 'select',
                    mantineFilterMultiSelectProps: {
                        data: Object.values(ERarity).map(rarity => {
                            return {
                                label: t('enum:' + rarity.toLowerCase()),
                                value: rarity
                            };
                        }),
                    },
                    Cell: cell => t('enum:' + cell.cell.getValue().toLowerCase())
                },
                {
                    accessorKey: 'vendorPrice',
                    header: t('price'),
                    Cell: CurrencyCell
                },
                {
                    accessorKey: 'tier',
                    header: t('tier'),
                },
                {
                    accessorKey: 'description',
                    header: t('description'),
                },
                {
                    accessorKey: 'upgradeSlots',
                    header: t('upgradeSlots'),
                },
                {
                    accessorKey: 'note',
                    header: t('note'),
                    defaultHidden: true
                },
                {
                    accessorKey: 'maximumStackSize',
                    header: t('item:maxStackSize'),
                    defaultHidden: true
                },
                {
                    accessorKey: 'minimumStackSize',
                    header: t('item:minStackSize'),
                    defaultHidden: true
                }
            ];

            if (itemSettings?.usingProtection) {
                c.splice(4, 0, {
                    accessorKey: 'protection',
                    header: t('protection'),
                });
            }

            return c;
        }, []);

    return <OverviewPage
        fetchData={fetchAllArmor()}
        columns={columns}
        identifier="armor"
        manipulationDialog={(editMode, refresh, disabled, getInitial) => <CreationDialog
            initialType="Armor"
            editMode={editMode}
            refresh={refresh}
            disabled={disabled}
            getInitial={getInitial}
        />}
        deletionDialogTitle={t('item:confirmDeletionTitle')}
        onDelete={(universe, items) => ITEM_API.deleteAllItems(universe, items.map(item => item.id))}
        idKey="id"
    />;
}

/** Overview over all jewellery */
export function JewelleryOverview() {
    const {t} = useTranslation();

    const columns = useMemo<ExtendedColumnDef<Jewellery, any>[]>(
        () => [
            {
                accessorKey: 'name',
                header: t('name'),
            },
            {
                accessorKey: 'tags',
                header: t('tags'),
                Cell: TagCell,
                filterFn: (row, id, filterValue) => {
                    return row.getValue<string[]>(id).some(tag => tag.includes(filterValue));
                }
            },
            {
                accessorKey: 'material',
                header: t('material'),
                Cell: NamedCell,
                filterFn: filterNamedCell
            },
            {
                accessorKey: 'requirement',
                header: t('requirement')
            },
            {
                accessorKey: 'effect',
                header: t('effect'),
            },
            {
                accessorKey: 'rarity',
                header: t('rarity'),
                filterVariant: 'select',
                mantineFilterMultiSelectProps: {
                    data: Object.values(ERarity).map(rarity => {
                        return {
                            label: t('enum:' + rarity.toLowerCase()),
                            value: rarity
                        };
                    }),
                },
                Cell: cell => t('enum:' + cell.cell.getValue().toLowerCase())
            },
            {
                accessorKey: 'vendorPrice',
                header: t('price'),
                Cell: CurrencyCell
            },
            {
                accessorKey: 'tier',
                header: t('tier'),
            },
            {
                accessorKey: 'description',
                header: t('description'),
            },
            {
                accessorKey: 'upgradeSlots',
                header: t('upgradeSlots'),
            },
            {
                accessorKey: 'note',
                header: t('note'),
                defaultHidden: true
            },
            {
                accessorKey: 'maximumStackSize',
                header: t('item:maxStackSize'),
                defaultHidden: true
            },
            {
                accessorKey: 'minimumStackSize',
                header: t('item:minStackSize'),
                defaultHidden: true
            }
        ], []);

    return <OverviewPage
        fetchData={fetchAllJewllery()}
        columns={columns}
        identifier="jewellery"
        manipulationDialog={(editMode, refresh, disabled, getInitial) => <CreationDialog
            initialType="Jewellery"
            editMode={editMode}
            refresh={refresh}
            disabled={disabled}
            getInitial={getInitial}
        />}
        deletionDialogTitle={t('item:confirmDeletionTitle')}
        onDelete={(universe, items) => ITEM_API.deleteAllItems(universe, items.map(item => item.id))}
        idKey="id"
    />;
}

function CreationDialog({
    initialType,
    editMode,
    refresh,
    disabled,
    getInitial
}: {
    initialType: string;
    editMode: boolean,
    refresh: () => void;
    disabled: boolean;
    getInitial: () => Item;
}) {
    const {t} = useTranslation();
    const {activeUniverse, itemSettings, currencySettings} = useUniverseContext();
    const [tags] = fetchAllTags();
    const [materials] = fetchAllMaterials();

    const [opened, {open, close}] = useDisclosure(false);
    const form = useForm<ItemCombination & {
        '@type': string;
    }>({
        mode: 'controlled',
        initialValues: {
            '@type': initialType,
            description: '',
            effect: '',
            maximumStackSize: initialType === 'Item' ? 100 : 1,
            minimumStackSize: initialType === 'Item' ? 0 : 1,
            name: '',
            note: '',
            rarity: ERarity.Common,
            requirement: '',
            tags: [],
            tier: 1,
            vendorPrice: 0,
            armor: 0,
            weight: 0,
            protection: 0,
            hit: 0,
            initiative: 0,
            upgradeSlots: 0,
            dice: {dices: []}
        }
    });
    const itemType = form.getValues()['@type'];

    useEffect(() => {
        if (!editMode || !opened) {
            return;
        }
        form.setValues(getInitial());
    }, [opened, getInitial, editMode]);

    function onSubmit(item: ItemCombination) {
        if (editMode) {
            ITEM_API.updateItem(activeUniverse.id, item.id, item).then(refresh).then(close)
                .catch(handleValidationErrors(form.setErrors));
        } else {
            ITEM_API.insertAllItems(activeUniverse.id, [item]).then(refresh).then(close)
                .catch(handleValidationErrors(handleDatabaseInsertErrors(form.setErrors)));
        }
    }

    return <>
        <Modal opened={opened} onClose={close} title={editMode ? t('item:editTitle') : t('item:creationTitle')}
               maw={300}>
            <form onSubmit={form.onSubmit(onSubmit)}>
                {!editMode ? <Select
                    data={[
                        {value: 'Item', label: t('item')},
                        {value: 'Weapon', label: t('weapon')},
                        {value: 'Shield', label: t('shield')},
                        {value: 'Armor', label: t('armor')},
                        {value: 'Jewellery', label: t('jewellery')}
                    ]}
                    key={form.key('@type')}
                    {...form.getInputProps('@type')}
                /> : null}
                <TextInput
                    label={t('name')}
                    key={form.key('name')}
                    {...form.getInputProps('name')}
                />
                <TagsInput
                    label={t('tags')}
                    data={tags}
                    clearable
                    key={form.key('tags')}
                    {...form.getInputProps('tags')}
                />
                {['Weapon', 'Shield', 'Armor', 'Jewellery'].includes(itemType) ?
                    <ObjectSelect<Material>
                        idKey="id"
                        labelKey="name"
                        label={t('material')}
                        data={materials}
                        searchable
                        key={form.key('material')}
                        {...form.getInputProps('material')} /> : null}
                {itemType === 'Armor' ?
                    <ArmorSlotSelect
                        key={form.key('armorSlot')}
                        {...form.getInputProps('armorSlot')}
                    /> : null}
                {['Shield', 'Armor'].includes(itemType) ?
                    <Group grow align="flex-start">
                        <NumberInput
                            label={t('armor')}
                            key={form.key('armor')}
                            {...form.getInputProps('armor')}
                            allowDecimal={false}
                        />
                        <NumberInput
                            label={t('weight')}
                            key={form.key('weight')}
                            {...form.getInputProps('weight')}
                        />
                    </Group> : null}
                {itemType === 'Armor' && itemSettings?.usingProtection ?
                    <NumberInput
                        label={t('protection')}
                        key={form.key('protection')}
                        {...form.getInputProps('protection')}
                        allowDecimal={false}
                    /> : null}
                {itemType === 'Shield' && (itemSettings?.shieldUsingDice || itemSettings?.usingProtection) ?
                    <Group grow align="flex-start">
                        {itemSettings?.shieldUsingDice ?
                            <DiceInput
                                label={t('dice')}
                                key={form.key('dice')}
                                {...form.getInputProps('dice')}
                            /> : null}
                        {itemSettings?.usingProtection ?
                            <NumberInput
                                label={t('protection')}
                                key={form.key('protection')}
                                {...form.getInputProps('protection')}
                                allowDecimal={false}
                            /> : null}
                    </Group> : null}
                {itemType === 'Weapon' ?
                    <Group grow align="flex-start">
                        <NumberInput
                            label={t('damage')}
                            key={form.key('damage')}
                            {...form.getInputProps('damage')}
                            allowDecimal={false}
                        />
                        <DiceInput
                            label={t('dice')}
                            key={form.key('dice')}
                            {...form.getInputProps('dice')}
                        />
                    </Group> : null}
                {['Shield', 'Weapon'].includes(itemType) ?
                    <Group grow align="flex-start">
                        <NumberInput
                            label={t('hit')}
                            key={form.key('hit')}
                            {...form.getInputProps('hit')}
                            allowDecimal={false}
                        />
                        <NumberInput
                            label={t('initiative')}
                            key={form.key('initiative')}
                            {...form.getInputProps('initiative')}
                        />
                    </Group> : null}
                <Textarea
                    label={t('effect')}
                    key={form.key('effect')}
                    {...form.getInputProps('effect')}
                />
                <Textarea
                    label={t('description')}
                    key={form.key('description')}
                    {...form.getInputProps('description')}
                />
                {['Weapon', 'Shield', 'Armor', 'Jewellery'].includes(itemType) ?
                    <NumberInput
                        label={t('upgradeSlots')}
                        key={form.key('upgradeSlots')}
                        {...form.getInputProps('upgradeSlots')}
                        allowDecimal={false}
                    /> : null}
                <Group grow align="flex-start">
                    <RaritySelect
                        label={t('rarity')}
                        key={form.key('rarity')}
                        {...form.getInputProps('rarity')}
                    />
                    <NumberInput
                        label={t('tier')}
                        key={form.key('tier')}
                        {...form.getInputProps('tier')}
                        allowDecimal={false}
                    />
                </Group>
                <TextInput
                    label={t('requirement')}
                    key={form.key('requirement')}
                    {...form.getInputProps('requirement')}
                />
                <Group grow align="flex-start">
                    <NumberInput
                        label={t('price')}
                        key={form.key('vendorPrice')}
                        {...form.getInputProps('vendorPrice')}
                        allowDecimal={false}
                    />
                    <TextInput
                        label={t('resultingPrice')}
                        readOnly
                        value={currencyFormatter(currencySettings, form.getValues().vendorPrice)}
                    />
                </Group>
                <Group grow align="flex-start">
                    <NumberInput
                        label={t('item:minStackSize')}
                        key={form.key('minimumStackSize')}
                        {...form.getInputProps('minimumStackSize')}
                        allowDecimal={false}
                    />
                    <NumberInput
                        label={t('item:maxStackSize')}
                        key={form.key('maximumStackSize')}
                        {...form.getInputProps('maximumStackSize')}
                        allowDecimal={false}
                    />
                </Group>
                <TextInput
                    label={t('note')}
                    key={form.key('note')}
                    {...form.getInputProps('note')}
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