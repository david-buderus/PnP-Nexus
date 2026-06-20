import {Table} from '@mantine/core';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext} from 'react';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {fetchAllSecondaryAttributes} from '../../../../Database';
import {toIdMap} from '../../../../utils/Utils';
import {TableStatsInput} from '../inputs/TableStatsInput';
import {PageElementSettings} from '../PageElementSettings';
import {OrderModifier} from '../OrderModifier';
import {TableNumberInput} from '../inputs/TableNumberInput';

/** Shows level and co of the character */
export function SecondaryAttributeInfo({
    attributesOrder, setAttributesOrder
}: {
    attributesOrder?: string[],
    setAttributesOrder: (order: string[]) => void
}) {
    const {t} = useTranslation();
    const {characterForm, allowEdit} = useContext(PnPCharacterContext);
    const [secondaryAttributes] = fetchAllSecondaryAttributes();
    const attributeMap = toIdMap(secondaryAttributes);
    if (!attributesOrder) {
        attributesOrder = secondaryAttributes.map(attribute => attribute.id);
    }

    return <>
        <Table
            variant="vertical"
            withTableBorder
            layout="fixed"
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
                        })}>
                            <TableNumberInput
                                allowDecimal={false}
                                readOnly={!allowEdit}
                                key={characterForm.key(`stats.secondaryStats.${id}.flatModifier`)}
                                {...characterForm.getInputProps(`stats.secondaryStats.${id}.flatModifier`)}
                            />
                        </Table.Td>
                        <Table.Td style={{width: '20%', ...TABLE_STYLE}}>
                            <TableStatsInput
                                allowDecimal={false}
                                allowNegative={false}
                                readOnly={true}
                                key={characterForm.key(`stats.secondaryStats.${id}`)}
                                {...characterForm.getInputProps(`stats.secondaryStats.${id}`)}
                            />
                        </Table.Td>
                    </Table.Tr>
                ))}
            </Table.Tbody>
        </Table>
        <PageElementSettings>
            <OrderModifier
                currentOrder={attributesOrder}
                setOrder={setAttributesOrder}
                fullList={secondaryAttributes}
            />
        </PageElementSettings>
    </>;
}