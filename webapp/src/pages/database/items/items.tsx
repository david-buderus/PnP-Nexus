import {useEffect, useMemo, useState} from 'react';
import {
    fetchAllArmor,
    fetchAllItems,
    fetchAllJewllery,
    fetchAllMaterials,
    fetchAllShields,
    fetchAllTags,
    fetchAllWeapons
} from '../../../components/Database';
import OverviewPage from '../../../components/OverviewPage';
import {useTranslation} from 'react-i18next';
import TagCell, {filterTagCell} from '../../../components/table/TagCell';
import {Armor, ERarity, Item, Jewellery, Material, Shield, Universe, Weapon} from '../../../api/model';
import CurrencyCell from '../../../components/table/CurrencyCell';
import {ItemClass} from '../../../components/Constants';
import {useDisclosure} from '@mantine/hooks';
import {Button, Group, Modal, NumberInput, Select, Stack, TagsInput, Textarea, TextInput} from '@mantine/core';
import {useForm} from '@mantine/form';
import {useUniverseContext} from '../../../components/PageBase';
import {
    handleDatabaseInsertErrors,
    handleNetworkErrors,
    handleValidationErrors
} from '../../../components/utils/ErrorUtils';
import {ObjectSelect} from '../../../components/input/ObjectSelect';
import {ArmorSlotSelect, RaritySelect} from '../../../components/input/EnumSelect';
import DiceInput from '../../../components/input/DiceInput';
import {currencyFormatter} from '../../../components/utils/Formatters';
import DiceCell from '../../../components/table/DiceCell';
import {filterNamedCell, NamedCell} from '../../../components/table/NamedCell';
import {ExtendedColumnDef} from '../../../components/table/SortableTable';
import {ItemCardModal} from '../../../components/items/ItemCard';
import {filterItemEffectsCell, ItemEffectsCell} from '../../../components/table/ItemEffectsCell';
import {ItemEffectForm} from '../../../components/input/ItemEffectForm';
import {getPossibleUpgradeRestriction} from '../../../components/utils/UpgradeUtils';
import {QueryClient, useQueryClient} from '@tanstack/react-query';
import {
    getGetAllArmorQueryKey,
    getGetAllItemsQueryKey,
    getGetAllJewelleryQueryKey,
    getGetAllShieldsQueryKey,
    getGetAllWeaponsQueryKey,
    useDeleteAllItems,
    useInsertAllItems,
    useUpdateItem
} from '../../../api/item-service/item-service';

/** Combination of all items */
export type ItemCombination = Item & Partial<Weapon> & Partial<Shield> & Partial<Armor> & Partial<Jewellery>;

/** Overview over all items */
export function Items() {
    const {t} = useTranslation();

    const [toEdit, setToEdit] = useState<Item>(null);
    const [openedAdd, {open: openAdd, close: closeAdd}] = useDisclosure(false);

    const columns = useMemo<ExtendedColumnDef<Item, any>[]>(
        () => [
            {
                accessorKey: 'name',
                header: t('name'),
            },
            {
                accessorKey: 'tags',
                header: t('tags'),
                cell: TagCell,
                filterFn: filterTagCell
            },
            {
                accessorKey: 'requirement',
                header: t('requirement'),
            },
            {
                accessorKey: 'effects',
                header: t('upgrade:effects'),
                cell: ItemEffectsCell,
                filterFn: filterItemEffectsCell
            },
            {
                accessorKey: 'rarity',
                header: t('rarity'),
                cell: cell => t('enum:' + cell.cell.getValue()?.toLowerCase())
            },
            {
                accessorKey: 'vendorPrice',
                header: t('price'),
                cell: CurrencyCell
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

    const deleteItems = useDeleteItems();

    return <Stack>
        <OverviewPage
            fetchData={fetchAllItems()}
            columns={columns}
            idKey="id"
            identifier="items"
            deletionDialogTitle={t('item:confirmDeletionTitle')}
            onDelete={(universe, items) => deleteItems({
                universe: universe,
                params: {ids: items.map(item => item.id)}
            })}
            viewModal={(item, onClose) => <ItemCardModal item={item} onClose={onClose}/>}
            onAdd={openAdd}
            onEdit={s => setToEdit(s)}
        />
        <CreationDialog initialType="Item" editMode={false} opened={openedAdd} close={closeAdd} item={null}/>
        <CreationDialog initialType="Item" editMode={true} opened={toEdit !== null} close={() => setToEdit(null)}
                        item={toEdit}/>
    </Stack>;
}

/** Overview over all weapons */
export function Weapons() {
    const {t} = useTranslation();

    const [toEdit, setToEdit] = useState<Weapon>(null);
    const [openedAdd, {open: openAdd, close: closeAdd}] = useDisclosure(false);

    const columns = useMemo<ExtendedColumnDef<Weapon, any>[]>(
        () => [
            {
                accessorKey: 'name',
                header: t('name'),
            },
            {
                accessorKey: 'tags',
                header: t('tags'),
                cell: TagCell,
                filterFn: filterTagCell
            },
            {
                accessorKey: 'material',
                header: t('material'),
                cell: NamedCell,
                filterFn: filterNamedCell
            },
            {
                accessorKey: 'damage',
                header: t('damage'),
            },
            {
                accessorKey: 'dice',
                header: t('dice'),
                cell: DiceCell
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
                cell: cell => t('enum:' + cell.cell.getValue().toLowerCase())
            },
            {
                accessorKey: 'vendorPrice',
                header: t('price'),
                cell: CurrencyCell
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

    const deleteItems = useDeleteItems();

    return <Stack>
        <OverviewPage
            fetchData={fetchAllWeapons()}
            columns={columns}
            idKey="id"
            identifier="weapons"
            deletionDialogTitle={t('item:confirmDeletionTitle')}
            onDelete={(universe, items) => deleteItems({
                universe: universe,
                params: {ids: items.map(item => item.id)}
            })}
            viewModal={(item, onClose) => <ItemCardModal item={item} onClose={onClose}/>}
            onAdd={openAdd}
            onEdit={s => setToEdit(s)}
        />
        <CreationDialog initialType="Weapon" editMode={false} opened={openedAdd} close={closeAdd} item={null}/>
        <CreationDialog initialType="Weapon" editMode={true} opened={toEdit !== null} close={() => setToEdit(null)}
                        item={toEdit}/>
    </Stack>;
}

/** Overview over all shields */
export function Shields() {
    const {t} = useTranslation();
    const {itemSettings} = useUniverseContext();

    const [toEdit, setToEdit] = useState<Shield>(null);
    const [openedAdd, {open: openAdd, close: closeAdd}] = useDisclosure(false);

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
                    cell: TagCell,
                    filterFn: filterTagCell
                },
                {
                    accessorKey: 'material',
                    header: t('material'),
                    cell: NamedCell,
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
                    cell: cell => t('enum:' + cell.cell.getValue().toLowerCase())
                },
                {
                    accessorKey: 'vendorPrice',
                    header: t('price'),
                    cell: CurrencyCell
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
                    cell: DiceCell
                });
            }

            return c;
        }, []);

    const deleteItems = useDeleteItems();

    return <Stack>
        <OverviewPage
            fetchData={fetchAllShields()}
            columns={columns}
            idKey="id"
            identifier="shields"
            deletionDialogTitle={t('item:confirmDeletionTitle')}
            onDelete={(universe, items) => deleteItems({
                universe: universe,
                params: {ids: items.map(item => item.id)}
            })}
            viewModal={(item, onClose) => <ItemCardModal item={item} onClose={onClose}/>}
            onAdd={openAdd}
            onEdit={s => setToEdit(s)}
        />
        <CreationDialog initialType="Shield" editMode={false} opened={openedAdd} close={closeAdd} item={null}/>
        <CreationDialog initialType="Shield" editMode={true} opened={toEdit !== null} close={() => setToEdit(null)}
                        item={toEdit}/>
    </Stack>;
}

/** Overview over all shields */
export function ArmorOverview() {
    const {t} = useTranslation();
    const {itemSettings} = useUniverseContext();

    const [toEdit, setToEdit] = useState<Armor>(null);
    const [openedAdd, {open: openAdd, close: closeAdd}] = useDisclosure(false);

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
                    cell: TagCell,
                    filterFn: filterTagCell
                },
                {
                    accessorKey: 'material',
                    header: t('material'),
                    cell: NamedCell,
                    filterFn: filterNamedCell
                },
                {
                    accessorKey: 'armorSlot',
                    header: t('item:armorSlot'),
                    cell: cell => t('enum:' + cell.cell.getValue().toLowerCase())
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
                    cell: cell => t('enum:' + cell.cell.getValue().toLowerCase())
                },
                {
                    accessorKey: 'vendorPrice',
                    header: t('price'),
                    cell: CurrencyCell
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

    const deleteItems = useDeleteItems();

    return <Stack>
        <OverviewPage
            fetchData={fetchAllArmor()}
            columns={columns}
            idKey="id"
            identifier="armor"
            deletionDialogTitle={t('item:confirmDeletionTitle')}
            onDelete={(universe, items) => deleteItems({
                universe: universe,
                params: {ids: items.map(item => item.id)}
            })}
            viewModal={(item, onClose) => <ItemCardModal item={item} onClose={onClose}/>}
            onAdd={openAdd}
            onEdit={s => setToEdit(s)}
        />
        <CreationDialog initialType="Armor" editMode={false} opened={openedAdd} close={closeAdd} item={null}/>
        <CreationDialog initialType="Armor" editMode={true} opened={toEdit !== null} close={() => setToEdit(null)}
                        item={toEdit}/>
    </Stack>;
}

/** Overview over all jewellery */
export function JewelleryOverview() {
    const {t} = useTranslation();

    const [toEdit, setToEdit] = useState<Jewellery>(null);
    const [openedAdd, {open: openAdd, close: closeAdd}] = useDisclosure(false);

    const columns = useMemo<ExtendedColumnDef<Jewellery, any>[]>(
        () => [
            {
                accessorKey: 'name',
                header: t('name'),
            },
            {
                accessorKey: 'tags',
                header: t('tags'),
                cell: TagCell,
                filterFn: filterTagCell
            },
            {
                accessorKey: 'material',
                header: t('material'),
                cell: NamedCell,
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
                cell: cell => t('enum:' + cell.cell.getValue().toLowerCase())
            },
            {
                accessorKey: 'vendorPrice',
                header: t('price'),
                cell: CurrencyCell
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

    const deleteItems = useDeleteItems();

    return <Stack>
        <OverviewPage
            fetchData={fetchAllJewllery()}
            columns={columns}
            idKey="id"
            identifier="jewellery"
            deletionDialogTitle={t('item:confirmDeletionTitle')}
            onDelete={(universe, items) => deleteItems({
                universe: universe,
                params: {ids: items.map(item => item.id)}
            })}
            viewModal={(item, onClose) => <ItemCardModal item={item} onClose={onClose}/>}
            onAdd={openAdd}
            onEdit={s => setToEdit(s)}
        />
        <CreationDialog initialType="Jewellery" editMode={false} opened={openedAdd} close={closeAdd} item={null}/>
        <CreationDialog initialType="Jewellery" editMode={true} opened={toEdit !== null} close={() => setToEdit(null)}
                        item={toEdit}/>
    </Stack>;
}

function CreationDialog({
    initialType,
    editMode,
    opened,
    close,
    item
}: {
    initialType: ItemClass;
    editMode: boolean,
    opened: boolean,
    close: () => void
    item: ItemCombination
}) {
    const {t} = useTranslation();
    const queryClient = useQueryClient();
    const {activeUniverse, itemSettings, currencySettings} = useUniverseContext();
    const [tags] = fetchAllTags();
    const [materials] = fetchAllMaterials();

    const form = useForm<ItemCombination & {
        '@type': ItemClass;
    }>({
        mode: 'controlled',
        initialValues: {
            '@type': initialType,
            description: '',
            effects: [],
            maximumStackSize: initialType === 'Item' ? 100 : 1,
            minimumStackSize: initialType === 'Item' ? 0 : 1,
            name: '',
            note: '',
            rarity: ERarity.COMMON,
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
    const itemType = form.values['@type'];

    useEffect(() => {
        if (!item) {
            return;
        }
        form.setValues(item);
        form.setInitialValues({
            ...item,
            '@type': initialType,
        });
    }, [item, initialType]);

    const {mutateAsync: updateItem} = useUpdateItem({
        mutation: {
            onSuccess: () => createInvalidateQueries(queryClient, activeUniverse).then(close),
            onError: handleValidationErrors(form.setErrors)
        }
    });
    const {mutateAsync: insertItems} = useInsertAllItems({
        mutation: {
            onSuccess: () => createInvalidateQueries(queryClient, activeUniverse).then(close),
            onError: handleValidationErrors(handleDatabaseInsertErrors(form.setErrors))
        }
    });

    function onSubmit(i: ItemCombination) {
        if (editMode) {
            return updateItem({
                universe: activeUniverse.id,
                id: i.id,
                data: i
            });
        } else {
            return insertItems({
                universe: activeUniverse.id,
                data: [i]
            });
        }
    }

    return <Modal opened={opened} onClose={close} title={editMode ? t('item:editTitle') : t('item:creationTitle')}
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
            <ItemEffectForm
                form={form}
                path="effects"
                restrictions={getPossibleUpgradeRestriction(itemType)}
            />
            <Textarea
                label={t('description')}
                key={form.key('description')}
                {...form.getInputProps('description')}
            />
            <NumberInput
                label={t('upgradeSlots')}
                key={form.key('upgradeSlots')}
                {...form.getInputProps('upgradeSlots')}
                allowDecimal={false}
            />
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
    </Modal>;
}

function useDeleteItems() {
    const {activeUniverse} = useUniverseContext();
    const queryClient = useQueryClient();

    const {mutate} = useDeleteAllItems({
        mutation: {
            onSuccess: () => createInvalidateQueries(queryClient, activeUniverse),
            onError: handleNetworkErrors
        }
    });

    return mutate;
}

async function createInvalidateQueries(queryClient: QueryClient, activeUniverse: Universe): Promise<void> {
    await queryClient.invalidateQueries({queryKey: getGetAllItemsQueryKey(activeUniverse.id)});
    await queryClient.invalidateQueries({queryKey: getGetAllWeaponsQueryKey(activeUniverse.id)});
    await queryClient.invalidateQueries({queryKey: getGetAllShieldsQueryKey(activeUniverse.id)});
    await queryClient.invalidateQueries({queryKey: getGetAllArmorQueryKey(activeUniverse.id)});
    return queryClient.invalidateQueries({queryKey: getGetAllJewelleryQueryKey(activeUniverse.id)});
}

