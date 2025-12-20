import {useNode} from '@craftjs/core';
import {Table} from '@mantine/core';
import {getPartStyle, TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext} from 'react';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {fetchAllSecondaryAttributes} from '../../../../Database';
import {OrderModifier} from '../OrderModifier';
import {toIdMap} from '../../../../utils/Utils';
import {TableStatsInput} from '../inputs/TableStatsInput';

/** Shows level and co of the character */
export const SecondaryAttributeInfo = ({
    attributesOrder
}: {
    attributesOrder?: string[]
}) => {
    const {t} = useTranslation();
    const {characterForm, allowEdit} = useContext(PnPCharacterContext);
    const {connectors: {connect, drag}, selected} = useNode((state => ({
        selected: state.events.selected
    })));
    const [secondaryAttributes] = fetchAllSecondaryAttributes();
    const attributeMap = toIdMap(secondaryAttributes);
    if (!attributesOrder) {
        attributesOrder = secondaryAttributes.map(attribute => attribute.id);
    }

    return <Table
        variant="vertical"
        withTableBorder
        ref={ref => connect(drag(ref))}
        style={getPartStyle(selected)}
    >
        <Table.Tbody>
            <Table.Tr h={TABLE_ROW_HEIGHT}>
                <Table.Th style={TABLE_STYLE} colSpan={4}>{t('secondary-attributes')}</Table.Th>
            </Table.Tr>

            {attributesOrder.map((id) => (
                <Table.Tr key={id} h={TABLE_ROW_HEIGHT}>
                    <Table.Th style={{width: '45%', ...TABLE_STYLE}}>{attributeMap[id]?.name}</Table.Th>
                    <Table.Th style={{width: '15%', ...TABLE_STYLE}}>{attributeMap[id]?.shortName}</Table.Th>
                    <Table.Td style={(theme) => ({
                        width: '20%',
                        borderRight: `1px solid ${theme.colors.gray[3]}`,
                        ...TABLE_STYLE
                    })}></Table.Td>
                    <Table.Td style={{width: '20%', ...TABLE_STYLE}}>
                        <TableStatsInput
                            allowDecimal={false}
                            allowNegative={false}
                            readOnly={!allowEdit}
                            key={characterForm.key(`stats.secondaryStats.${id}`)}
                            {...characterForm.getInputProps(`stats.secondaryStats.${id}`)}
                        />
                    </Table.Td>
                </Table.Tr>
            ))}
        </Table.Tbody>
    </Table>;
};

const SecondaryAttributeSettings = () => {
    const {actions: {setProp}, attributesOrder} = useNode(node => ({
        attributesOrder: node.data.props.attributesOrder
    }));
    const [secondaryAttributes] = fetchAllSecondaryAttributes();

    return <OrderModifier
        currentOrder={attributesOrder}
        setOrder={order => setProp(props => {
            props.attributesOrder = order;
        })}
        fullList={secondaryAttributes}
    />;
};

SecondaryAttributeInfo.craft = {
    name: 'sheetEditor:secondaryAttributeInfo',
    related: {
        settings: SecondaryAttributeSettings
    }
};