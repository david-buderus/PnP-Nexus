import {ActionIcon, Group, NumberInput, Popover, Stack, Table, Text} from '@mantine/core';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext, useState} from 'react';
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


/** Shows the inventory of the character */
export function InventoryPart({rows, columns, setRows, setColumns}: {
    rows: number;
    columns: number;
    setRows: (n: number) => void;
    setColumns: (n: number) => void;
}) {
    const {t} = useTranslation();
    const {characterForm, allowEdit} = useContext(PnPCharacterContext);
    const character = characterForm.getValues();

    const [lastClicked, setLastClicked] = useState<SomeItemStack>(null);

    const {mutate: addItemStackToInventory} = useAddItemStackToInventory({
        mutation: {
            onSuccess: response => characterForm.setFieldValue('inventory.inventory', response.data)
        }
    });
    const {mutate: removeItemFromInventory} = useRemoveItemFromInventory({
        mutation: {
            onSuccess: response => characterForm.setFieldValue('inventory.inventory', response.data)
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
                    <Table.Th style={TABLE_STYLE} colSpan={columns}>{t('inventory')}</Table.Th>
                </Table.Tr>
                {Array.from({length: rows}, (_, rowIndex) =>
                    <Table.Tr h={TABLE_ROW_HEIGHT} key={rowIndex}>
                        {Array.from({length: columns}, (__, colIndex) => {
                            const itemIndex = rowIndex + colIndex * rows;
                            const itemStack = character?.inventory.inventory.items[itemIndex];

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
                                                onChange={i => characterForm.replaceListItem('inventory.inventory.items', itemIndex, i)}
                                            />
                                            <ActionIcon
                                                variant="subtle"
                                                size={TABLE_ROW_HEIGHT - 8}
                                                className="no-drag"
                                                onClick={() => addItemStackToInventory({
                                                    data: {
                                                        inventory: characterForm.values.inventory.inventory,
                                                        itemStack: {
                                                            ...itemStack,
                                                            stackSize: 1
                                                        }
                                                    }
                                                })}
                                                onContextMenu={() => addItemStackToInventory({
                                                    data: {
                                                        inventory: characterForm.values.inventory.inventory,
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
                                                        inventory: characterForm.values.inventory.inventory,
                                                        itemStack: {
                                                            ...itemStack,
                                                            stackSize: 1
                                                        }
                                                    }
                                                })}
                                                onContextMenu={() => removeItemFromInventory({
                                                    data: {
                                                        inventory: characterForm.values.inventory.inventory,
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
        <ItemAdditionPopover/>
        <ItemStackCardModal stack={lastClicked} onClose={() => setLastClicked(null)}/>
    </>;
}

function ItemAdditionPopover() {
    const {allowEdit, characterForm} = useContext(PnPCharacterContext);
    const [items] = fetchAllItems();
    const {mutate: addItemToInventory} = useAddItemToInventory({
        mutation: {
            onSuccess: response => characterForm.setFieldValue('inventory.inventory', response.data)
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
                            inventory: characterForm.values.inventory.inventory,
                            item: item.item,
                            amount: item.amount,
                        }
                    })}
                />
            </Popover.Dropdown>
        </Popover>
    );
}