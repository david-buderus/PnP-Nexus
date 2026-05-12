import {Table} from '@mantine/core';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext} from 'react';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {fetchAllPrimaryAttributes} from '../../../../Database';
import {toIdMap} from '../../../../utils/Utils';
import {OrderModifier} from '../OrderModifier';
import {PageElementSettings} from '../PageElementSettings';

/** Shows the primary attributes of the user in a read-only row */
export function PrimaryAttributeRow({
    attributesOrder, setAttributesOrder
}: {
    attributesOrder?: string[],
    setAttributesOrder: (order: string[]) => void
}) {
    const {characterForm} = useContext(PnPCharacterContext);
    const character = characterForm.getValues();
    const [primaryAttributes] = fetchAllPrimaryAttributes();
    const attributeMap = toIdMap(primaryAttributes);
    if (!attributesOrder) {
        attributesOrder = primaryAttributes.map(attribute => attribute.id);
    }

    return <>
        <Table
            variant="vertical"
            layout="fixed"
            withTableBorder
        >
            <Table.Tbody>
                <Table.Tr h={TABLE_ROW_HEIGHT}>
                    {attributesOrder.map((id) => (
                        <Table.Th key={id} style={TABLE_STYLE}>{attributeMap[id]?.shortName ?? ''}</Table.Th>
                    ))}
                </Table.Tr>
                <Table.Tr h={TABLE_ROW_HEIGHT}>
                    {attributesOrder.map((id) => (
                        <Table.Td key={id}
                                  style={TABLE_STYLE}>{character?.stats.primaryStats[attributeMap[id].id]?.rawValue ?? 0}</Table.Td>
                    ))}
                </Table.Tr>
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