import {NumberInput, Stack, Table} from '@mantine/core';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext, useMemo} from 'react';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {JewelleryDefinition} from '../../../../../api';
import {useUniverseContext} from '../../../../PageBase';
import {PageElementSettings} from '../PageElementSettings';


/** Shows jewellery of the character */
export function JewelleryList({numberOfJewellery, setNumberOfJewellery}: {
    numberOfJewellery: Record<string, number>;
    setNumberOfJewellery: (j: Record<string, number>) => void;
}) {
    const {t} = useTranslation();
    const {equipmentSettings} = useUniverseContext();

    return <>
        <Table
            withTableBorder
            withColumnBorders
            striped
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
        </Table>
        <PageElementSettings>
            <Stack>
                {equipmentSettings.jewelleryDefinitions.map(definition =>
                    <NumberInput
                        key={definition.name}
                        label={definition.name}
                        value={numberOfJewellery[definition.name]}
                        onChange={e => {
                            const changed = {...numberOfJewellery};
                            changed[definition.name] = Number(e);
                            setNumberOfJewellery(changed);
                        }}
                        min={1}
                        max={definition.amount}
                        allowDecimal={false}
                    />
                )}
            </Stack>
        </PageElementSettings>
    </>;
}

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