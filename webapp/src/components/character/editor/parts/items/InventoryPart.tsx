import {useNode} from '@craftjs/core';
import {NumberInput, Stack, Table} from '@mantine/core';
import {getPartStyle, TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext} from 'react';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';


/** Shows the inventory of the character */
export const InventoryPart = ({rows, columns}: {
    rows: number;
    columns: number;
}) => {
    const {t} = useTranslation();
    const {character} = useContext(PnPCharacterContext);
    const {connectors: {connect, drag}, selected} = useNode((state => ({
        selected: state.events.selected
    })));

    return <Table
        withTableBorder
        withColumnBorders
        striped
        ref={ref => connect(drag(ref))}
        style={getPartStyle(selected)}
    >
        <Table.Tbody>
            <Table.Tr h={TABLE_ROW_HEIGHT}>
                <Table.Th style={TABLE_STYLE} colSpan={columns}>{t('inventory')}</Table.Th>
            </Table.Tr>
            {Array.from({length: rows}, (_, rowIndex) =>
                <Table.Tr h={TABLE_ROW_HEIGHT} key={rowIndex}>
                    {Array.from({length: columns}, (_, colIndex) => {
                        const itemStack = character?.inventory.inventory.items[rowIndex + colIndex * rows];

                        return <Table.Td style={{width: `${100 / columns}%`, ...TABLE_STYLE}} key={colIndex}>
                            {itemStack ? `${itemStack.stackSize}x ${itemStack.item.name}` : ''}
                        </Table.Td>;
                    })}
                </Table.Tr>
            )}
        </Table.Tbody>
    </Table>;
};

const InventoryPartSettings = () => {
    const {t} = useTranslation();
    const {actions: {setProp}, rows, columns} = useNode(node => ({
        rows: node.data.props.rows,
        columns: node.data.props.columns
    }));

    return <Stack>
        <NumberInput
            label={t('sheetEditor:numberOfRows')}
            value={rows}
            onChange={e => setProp(props => {
                props.rows = Number(e);
            })}
        />
        <NumberInput
            label={t('sheetEditor:numberOfColumns')}
            value={columns}
            onChange={e => setProp(props => {
                props.columns = Number(e);
            })}
        />
    </Stack>;
};

InventoryPart.craft = {
    name: 'inventory',
    related: {
        settings: InventoryPartSettings
    }
};