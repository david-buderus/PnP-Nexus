import {ActionIcon, Group, NumberInput, Popover, Select, Stack, Table, Text} from '@mantine/core';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext, useMemo, useState} from 'react';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {PageElementSettings} from '../PageElementSettings';
import {IconCircleMinus, IconCirclePlus, IconMoneybagPlus} from '@tabler/icons-react';
import {ItemSearchCard} from '../../../../items/ItemSearchCard';
import {ItemStackCardModal} from '../../../../items/ItemStackCard';
import {SomeItemStack} from '../../../../Constants';
import {fetchAllItems} from '../../../../Database';
import {UpgradePopover} from '../../../../items/UpgradeControl';
import {
    useAddItemStackToInventory,
    useAddItemToInventory,
    useRemoveItemFromInventory
} from '../../../../../api/inventory-service/inventory-service';
import {useUniverseContext} from '../../../../PageBase';

const CROSSED_CELL_BACKGROUND = `
  linear-gradient(to top right, transparent calc(50% - 1px), var(--mantine-color-gray-4), transparent calc(50% + 1px)),
  linear-gradient(to bottom right, transparent calc(50% - 1px), var(--mantine-color-gray-4), transparent calc(50% + 1px))
`;

/** Shows the inventory of the character */
export function InventoryPart({name, rows, columns, setName, setRows, setColumns}: {
    name: string;
    rows: number;
    columns: number;
    setName: (n: string) => void;
    setRows: (n: number) => void;
    setColumns: (n: number) => void;
}) {
    const {t} = useTranslation();
    const {characterForm, allowEdit} = useContext(PnPCharacterContext);
    const {characterSettings} = useUniverseContext();
    const character = characterForm.getValues();
    const inventorySize = useMemo(() => characterSettings.inventorySizes.filter(e => e.name === name)[0]?.size ?? 0, [characterSettings]);

    const [lastClicked, setLastClicked] = useState<SomeItemStack>(null);

    const {mutate: addItemStackToInventory} = useAddItemStackToInventory({
        mutation: {
            onSuccess: response => characterForm.setFieldValue(`inventory.inventories.${name}`, response.data)
        }
    });
    const {mutate: removeItemFromInventory} = useRemoveItemFromInventory({
        mutation: {
            onSuccess: response => characterForm.setFieldValue(`inventory.inventories.${name}`, response.data)
        }
    });

    return <>
        <Table
            withTableBorder
            withColumnBorders
            striped
            style={{tableLayout: 'fixed'}}
        >
            <Table.Tbody>
                <Table.Tr h={TABLE_ROW_HEIGHT}>
                    <Table.Th style={TABLE_STYLE} colSpan={columns}>
                        {name}
                    </Table.Th>
                </Table.Tr>
                {Array.from({length: rows}, (_, rowIndex) =>
                    <Table.Tr h={TABLE_ROW_HEIGHT} key={rowIndex}>
                        {Array.from({length: columns}, (__, colIndex) => {
                            const itemIndex = rowIndex + colIndex * rows;
                            const itemStack = character?.inventory.inventories[name]?.items[itemIndex];

                            if (itemIndex >= inventorySize) {
                                return <Table.Td
                                    style={{
                                        width: `${100 / columns}%`,
                                        backgroundImage: CROSSED_CELL_BACKGROUND,
                                        backgroundSize: '100% 100%',
                                        ...TABLE_STYLE
                                    }}
                                    key={colIndex}
                                />;
                            }

                            return <Table.Td
                                style={{width: `${100 / columns}%`, ...TABLE_STYLE}}
                                key={colIndex}
                                onClick={allowEdit ? () => setLastClicked(itemStack ?? null) : null}
                            >
                                <Group justify="space-between" wrap="nowrap" style={{width: '100%'}}>
                                    {itemStack ? (
                                        <Text
                                            size={TABLE_STYLE.fontSize}
                                            truncate="end"
                                            style={{flex: 1, minWidth: 0}}
                                        >
                                            {`${itemStack.stackSize}x ${itemStack.item.name}`}
                                        </Text>
                                    ) : null}
                                    {itemStack && allowEdit ?
                                        <Group
                                            wrap="nowrap"
                                            gap={1}
                                            style={{flexShrink: 0}}
                                            onClick={e => e.stopPropagation()}
                                        >
                                            <UpgradePopover
                                                item={itemStack}
                                                onChange={i => characterForm.replaceListItem(`inventory.inventories.${name}.items`, itemIndex, i)}
                                            />
                                            <ActionIcon
                                                variant="subtle"
                                                size={TABLE_ROW_HEIGHT - 8}
                                                className="no-drag"
                                                onClick={() => addItemStackToInventory({
                                                    data: {
                                                        inventory: characterForm.values.inventory.inventories[name],
                                                        itemStack: {
                                                            ...itemStack,
                                                            stackSize: 1
                                                        }
                                                    }
                                                })}
                                                onContextMenu={() => addItemStackToInventory({
                                                    data: {
                                                        inventory: characterForm.values.inventory.inventories[name],
                                                        itemStack: {
                                                            ...itemStack,
                                                            stackSize: 5
                                                        }
                                                    }
                                                })}
                                            >
                                                <IconCirclePlus size={14}/>
                                            </ActionIcon>
                                            <ActionIcon
                                                variant="subtle"
                                                size={TABLE_ROW_HEIGHT - 8}
                                                className="no-drag"
                                                onClick={() => removeItemFromInventory({
                                                    data: {
                                                        inventory: characterForm.values.inventory.inventories[name],
                                                        itemStack: {
                                                            ...itemStack,
                                                            stackSize: 1
                                                        }
                                                    }
                                                })}
                                                onContextMenu={() => removeItemFromInventory({
                                                    data: {
                                                        inventory: characterForm.values.inventory.inventories[name],
                                                        itemStack: {
                                                            ...itemStack,
                                                            stackSize: 5
                                                        }
                                                    }
                                                })}
                                            >
                                                <IconCircleMinus color="red" size={14}/>
                                            </ActionIcon>
                                        </Group>
                                        : null}
                                </Group>
                            </Table.Td>;
                        })}
                    </Table.Tr>
                )}
            </Table.Tbody>
        </Table>
        <PageElementSettings>
            <Stack>
                <Select
                    label={t('name')}
                    value={name}
                    onChange={setName}
                    data={characterSettings.inventorySizes.map(s => s.name)}
                />
                <NumberInput
                    label={t('sheetEditor:numberOfRows')}
                    value={rows}
                    onChange={e => setRows(Number(e))}
                />
                <NumberInput
                    label={t('sheetEditor:numberOfColumns')}
                    value={columns}
                    onChange={e => setColumns(Number(e))}
                />
            </Stack>
        </PageElementSettings>
        <ItemAdditionPopover name={name}/>
        <ItemStackCardModal stack={lastClicked} onClose={() => setLastClicked(null)}/>
    </>;
}

function ItemAdditionPopover({name}: { name: string }) {
    const {allowEdit, characterForm} = useContext(PnPCharacterContext);
    const [items] = fetchAllItems();
    const {mutate: addItemToInventory} = useAddItemToInventory({
        mutation: {
            onSuccess: response => characterForm.setFieldValue(`inventory.inventories.${name}`, response.data)
        }
    });

    if (!allowEdit) {
        return null;
    }

    return (
        <Popover position="bottom" withArrow shadow="md">
            <Popover.Target>
                <ActionIcon
                    variant="subtle"
                    size="sm"
                    className="no-drag"
                    disabled={!name}
                    style={{
                        position: 'absolute',
                        top: 2,
                        right: 2,
                        zIndex: 10, // Ensure it stays above everything
                    }}
                >
                    <IconMoneybagPlus size={14}/>
                </ActionIcon>
            </Popover.Target>
            <Popover.Dropdown>
                <ItemSearchCard
                    items={items}
                    onSelect={item => addItemToInventory({
                        data: {
                            inventory: characterForm.values.inventory.inventories[name],
                            item: item.item,
                            amount: item.amount,
                        }
                    })}
                />
            </Popover.Dropdown>
        </Popover>
    );
}