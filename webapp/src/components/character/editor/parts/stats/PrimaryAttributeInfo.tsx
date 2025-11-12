import {useNode} from '@craftjs/core';
import {Table} from '@mantine/core';
import {getPartStyle, TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext} from 'react';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../PnPCharacterContext';
import {useUniverseContext} from '../../../../PageBase';
import {fetchAllPrimaryAttributes} from '../../../../Database';
import {OrderModifier} from '../OrderModifier';
import {toIdMap} from '../../../../utils/Utils';

/** Shows level and co of the character */
export const PrimaryAttributeInfo = ({
    attributesOrder
}: {
    attributesOrder?: string[]
}) => {
    const {t} = useTranslation();
    const {character} = useContext(PnPCharacterContext);
    const {characterSettings} = useUniverseContext();
    const [primaryAttributes] = fetchAllPrimaryAttributes();
    const {connectors: {connect, drag}, selected} = useNode((state => ({
        selected: state.events.selected
    })));
    const attributeMap = toIdMap(primaryAttributes);
    if (!attributesOrder) {
        attributesOrder = primaryAttributes.map(attribute => attribute.id);
    }

    return <Table
        variant="vertical"
        layout="fixed"
        withTableBorder
        ref={ref => connect(drag(ref))}
        style={getPartStyle(selected)}
    >
        <Table.Tbody>
            <Table.Tr h={TABLE_ROW_HEIGHT}>
                <Table.Th colSpan={2} style={TABLE_STYLE}>{t('primary-attributes')}</Table.Th>
                <Table.Th
                    style={TABLE_STYLE}>{`Min: ${characterSettings.minPrimaryAttributeValue} Max: ${characterSettings.maxPrimaryAttributeValue}`}</Table.Th>
            </Table.Tr>

            {attributesOrder.map((id) => (
                <Table.Tr key={id} h={TABLE_ROW_HEIGHT}>
                    <Table.Th style={TABLE_STYLE}>{attributeMap[id]?.name ?? ''}</Table.Th>
                    <Table.Th style={TABLE_STYLE}>{attributeMap[id]?.shortName ?? ''}</Table.Th>
                    <Table.Td style={TABLE_STYLE}>
                        {character?.stats.primaryStats[id]?.rawValue ?? 0}
                    </Table.Td>
                </Table.Tr>
            ))}
        </Table.Tbody>
    </Table>;
};

const PrimaryAttributeSettings = () => {
    const {actions: {setProp}, attributesOrder} = useNode(node => ({
        attributesOrder: node.data.props.attributesOrder
    }));
    const [primaryAttributes] = fetchAllPrimaryAttributes();

    return <OrderModifier
        currentOrder={attributesOrder}
        setOrder={order => {
            setProp(props => {
                props.attributesOrder = order;
            });
        }}
        fullList={primaryAttributes}
    />;
};

PrimaryAttributeInfo.craft = {
    name: 'sheetEditor:primaryAttributeInfo',
    related: {
        settings: PrimaryAttributeSettings
    }
};