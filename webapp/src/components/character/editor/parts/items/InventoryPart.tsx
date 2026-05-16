import {ActionIcon, NumberInput, Popover, Stack, Table} from '@mantine/core';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext, useState} from 'react';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {PageElementSettings} from '../PageElementSettings';
import {IconMoneybagPlus} from '@tabler/icons-react';
import {ItemSearchCard} from '../../../../items/ItemSearchCard';
import {addItemToInventory} from '../../../../utils/InventoryUtils';
import {ItemStackCardModal} from '../../../../items/ItemStackCard';
import {ItemStack} from '../../../../Constants';


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

    const [lastClicked, setLastClicked] = useState<ItemStack>(null);

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
                            const itemStack = character?.inventory.inventory.items[rowIndex + colIndex * rows];

                            return <Table.Td
                                style={{width: `${100 / columns}%`, ...TABLE_STYLE}}
                                key={colIndex}
                                onClick={allowEdit ? () => setLastClicked(itemStack ?? null) : null}
                            >
                                {itemStack ? `${itemStack.stackSize}x ${itemStack.item.name}` : ''}
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
                    onSelect={item =>
                        addItemToInventory(characterForm.values.inventory.inventory, item.item, item.amount)
                            .then(inventory => characterForm.setFieldValue('inventory.inventory', inventory))}
                />
            </Popover.Dropdown>
        </Popover>
    );
}