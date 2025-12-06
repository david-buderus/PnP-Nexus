import {useNode} from '@craftjs/core';
import {NumberInput, Stack, Table} from '@mantine/core';
import {getPartStyle, TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext, useMemo} from 'react';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {JewelleryDefinition} from '../../../../../api';
import {useUniverseContext} from '../../../../PageBase';


/** Shows jewellery of the character */
export const JewelleryList = ({numberOfJewellery}: {
    numberOfJewellery: Record<string, number>;
}) => {
    const {t} = useTranslation();
    const {equipmentSettings} = useUniverseContext();
    const {connectors: {connect, drag}, selected} = useNode((state => ({
        selected: state.events.selected
    })));

    return <Table
        withTableBorder
        withColumnBorders
        striped
        ref={ref => connect(drag(ref))}
        style={{...getPartStyle(selected), tableLayout: 'fixed'}}
    >
        <Table.Tbody>
            <Table.Tr h={TABLE_ROW_HEIGHT}>
                <Table.Th style={{width: '20%', ...TABLE_STYLE}}>{t('jewellery')}</Table.Th>
                <Table.Th style={{width: '25%', ...TABLE_STYLE}}>{t('name')}</Table.Th>
                <Table.Th style={{width: '55%', ...TABLE_STYLE}}>{t('effect')}</Table.Th>
            </Table.Tr>
            {equipmentSettings.jewelleryDefinitions.map(definition => <JewelleryLines
                key={definition.name}
                definition={definition}
                numberOfJewellery={numberOfJewellery}
            />)}
        </Table.Tbody>
    </Table>;
};

function JewelleryLines({
    definition,
    numberOfJewellery
}: {
    definition: JewelleryDefinition,
    numberOfJewellery: Record<string, number>
}) {
    const {characterForm} = useContext(PnPCharacterContext);
    const character = characterForm.getValues();

    const number = numberOfJewellery[definition.name];
    const jewellery = useMemo(() => {
        const list = character.equipment.jewellery[definition.name] ?? [];
        const w = [...list];
        w.length = number ?? 1;
        w.fill(null, list.length);
        return w;
    }, [character.equipment, number]);

    return <>
        {jewellery.map((j, index) => {

            let effect = j?.item.effect ?? '';
            if (j?.upgradeSlots > 0) {
                effect += ` ${j.remainingUpgradeSlots}/${j.upgradeSlots} ${j.upgrades.map(u => u.name).join(', ')}`;
            }

            return <Table.Tr key={index} h={TABLE_ROW_HEIGHT}>
                <Table.Td style={TABLE_STYLE}>{definition.name + (number > 1 ? ` ${index + 1}` : '')}</Table.Td>
                <Table.Td style={TABLE_STYLE}>{j?.item.name ?? ''}</Table.Td>
                <Table.Td style={TABLE_STYLE}>{effect}</Table.Td>
            </Table.Tr>;
        })}
    </>;
}

const JewelleryListSettings = () => {
    const {equipmentSettings} = useUniverseContext();
    const {actions: {setProp}, numberOfJewellery} = useNode(node => ({
        numberOfJewellery: node.data.props.numberOfJewellery as Record<string, number>
    }));

    return <Stack>
        {equipmentSettings.jewelleryDefinitions.map(definition =>
            <NumberInput
                key={definition.name}
                label={definition.name}
                value={numberOfJewellery[definition.name]}
                onChange={e => setProp(props => {
                    const changed = {...numberOfJewellery};
                    changed[definition.name] = Number(e);
                    props.numberOfJewellery = changed;
                })}
                min={1}
                max={definition.amount}
                allowDecimal={false}
            />
        )}
    </Stack>;
};

JewelleryList.craft = {
    name: 'sheetEditor:jewelleryList',
    related: {
        settings: JewelleryListSettings
    }
};