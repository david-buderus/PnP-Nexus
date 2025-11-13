import {useNode} from '@craftjs/core';
import {Table} from '@mantine/core';
import {getPartStyle, TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext} from 'react';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {fetchAllPrimaryAttributes} from '../../../../Database';
import {OrderModifier} from '../OrderModifier';
import {toIdMap} from '../../../../utils/Utils';

/** Shows level and co of the character */
export const PrimaryAttributeRow = ({
    attributesOrder
}: {
    attributesOrder?: string[]
}) => {
    const {character} = useContext(PnPCharacterContext);
    const {connectors: {connect, drag}, selected} = useNode((state => ({
        selected: state.events.selected
    })));
    const [primaryAttributes] = fetchAllPrimaryAttributes();
    const attributeMap = toIdMap(primaryAttributes);
    if (!attributesOrder) {
        attributesOrder = primaryAttributes.map(attribute => attribute.id);
    }

    return <Table
        withTableBorder
        withColumnBorders
        ref={ref => connect(drag(ref))}
        style={getPartStyle(selected)}
    >
        <Table.Tbody>
            <Table.Tr h={TABLE_ROW_HEIGHT}>
                {attributesOrder.map((id) => (
                    <Table.Th key={id} style={TABLE_STYLE}>{attributeMap[id]?.shortName ?? ''}</Table.Th>
                ))}
            </Table.Tr>
            <Table.Tr h={TABLE_ROW_HEIGHT}>
                {attributesOrder.map((id) => (
                    <Table.Th key={id}
                              style={TABLE_STYLE}>{character?.stats.primaryStats[attributeMap[id].id]?.rawValue ?? 0}</Table.Th>
                ))}
            </Table.Tr>
        </Table.Tbody>
    </Table>;
};

const PrimaryAttributeRowSettings = () => {
    const {actions: {setProp}, attributesOrder} = useNode(node => ({
        attributesOrder: node.data.props.attributesOrder
    }));
    const [primaryAttributes] = fetchAllPrimaryAttributes();

    return <OrderModifier
        currentOrder={attributesOrder}
        setOrder={order => setProp(props => {
            props.attributesOrder = order;
        })}
        fullList={primaryAttributes}
    />;
};

PrimaryAttributeRow.craft = {
    name: 'sheetEditor:primaryAttributeRow',
    related: {
        settings: PrimaryAttributeRowSettings
    }
};