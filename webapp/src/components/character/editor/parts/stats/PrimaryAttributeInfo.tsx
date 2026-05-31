import {Table} from '@mantine/core';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext} from 'react';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {useUniverseContext} from '../../../../PageBase';
import {fetchAllPrimaryAttributes} from '../../../../Database';
import {toIdMap} from '../../../../utils/Utils';
import {TableStatsInput} from '../inputs/TableStatsInput';
import {OrderModifier} from '../OrderModifier';
import {PageElementSettings} from '../PageElementSettings';

/** Shows the primary attributes of the user */
export function PrimaryAttributeInfo({
    attributesOrder, setAttributesOrder
}: {
    attributesOrder?: string[],
    setAttributesOrder: (order: string[]) => void
}) {
    const {t} = useTranslation();
    const {characterForm, allowEdit} = useContext(PnPCharacterContext);
    const {characterSettings} = useUniverseContext();
    const [primaryAttributes] = fetchAllPrimaryAttributes();

    const attributeMap = toIdMap(primaryAttributes);
    if (!attributesOrder) {
        attributesOrder = primaryAttributes.map(attribute => attribute.id);
    }

    const sum = Object.values(characterForm.values.stats.primaryStats)
        .map(s => s.rawValue).reduce((a, b) => a + b, 0);

    return <>
        <Table
            variant="vertical"
            layout="fixed"
            withTableBorder
        >
            <Table.Tbody>
                <Table.Tr h={TABLE_ROW_HEIGHT}>
                    <Table.Th colSpan={2} style={TABLE_STYLE}>{t('primary-attributes')}</Table.Th>
                    <Table.Th style={TABLE_STYLE}>
                        {`Min: ${characterSettings?.minPrimaryAttributeValue ?? 0} Max: ${sum} / ${characterSettings?.maxPrimaryAttributeValue ?? 0}`}
                    </Table.Th>
                </Table.Tr>

                {attributesOrder.map((id) => (
                    <Table.Tr key={id} h={TABLE_ROW_HEIGHT}>
                        <Table.Th style={TABLE_STYLE}>{attributeMap[id]?.name ?? ''}</Table.Th>
                        <Table.Th style={TABLE_STYLE}>{attributeMap[id]?.shortName ?? ''}</Table.Th>
                        <Table.Td style={TABLE_STYLE}>
                            <TableStatsInput
                                allowDecimal={false}
                                allowNegative={false}
                                readOnly={!allowEdit}
                                min={characterSettings.minPrimaryAttributeValue}
                                max={characterSettings.maxPrimaryAttributeValue}
                                key={characterForm.key(`stats.primaryStats.${id}`)}
                                {...characterForm.getInputProps(`stats.primaryStats.${id}`)}
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
                fullList={primaryAttributes}
            />
        </PageElementSettings>
    </>;
}