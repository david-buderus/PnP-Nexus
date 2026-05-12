import {NumberInput, Stack, Switch, Table} from '@mantine/core';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext, useMemo} from 'react';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {diceFormatter} from '../../../../utils/Formatters';
import {ShieldEquipment} from '../../../../../api';
import {useUniverseContext} from '../../../../PageBase';
import {PageElementSettings} from '../PageElementSettings';


/** Shows weapons of the character */
export function WeaponList({
    numberOfHandheld,
    withShield,
    setNumberOfHandheld,
    setWithShield,
}: {
    numberOfHandheld: number;
    withShield: boolean;
    setNumberOfHandheld: (n: number) => void;
    setWithShield: (b: boolean) => void;
}) {
    const {t} = useTranslation();
    const {characterForm} = useContext(PnPCharacterContext);
    const character = characterForm.getValues();

    const weapons = useMemo(() => {
        const w = [...character.equipment.weaponEquipments];
        w.length = numberOfHandheld;
        w.fill(null, character.equipment.weaponEquipments.length);
        return w;
    }, [character.equipment, numberOfHandheld]);
    const shield = character?.equipment.shieldEquipment;

    return <>
        <Table
            withTableBorder
            withColumnBorders
            striped
        >
            <Table.Tbody>
                <Table.Tr h={TABLE_ROW_HEIGHT}>
                    <Table.Th style={{width: '15%', ...TABLE_STYLE}}>{t('weapon')}</Table.Th>
                    <Table.Th style={{width: '15%', ...TABLE_STYLE}}>{t('tag')}</Table.Th>
                    <Table.Th style={{width: '10%', ...TABLE_STYLE}}>{t('initiative')}</Table.Th>
                    <Table.Th style={{width: '10%', ...TABLE_STYLE}}>{t('hit')}</Table.Th>
                    <Table.Th style={{width: '10%', ...TABLE_STYLE}}>{t('dice')}</Table.Th>
                    <Table.Th style={{width: '10%', ...TABLE_STYLE}}>{t('damage')}</Table.Th>
                    <Table.Th style={{width: '30%', ...TABLE_STYLE}}>{t('effect')}</Table.Th>
                </Table.Tr>
                {weapons.map((weapon, index) => {
                    if (!weapon) {
                        return [
                            <Table.Tr key={index} h={TABLE_ROW_HEIGHT}>
                                <Table.Td style={TABLE_STYLE}></Table.Td>
                                <Table.Td style={TABLE_STYLE}></Table.Td>
                                <Table.Td style={TABLE_STYLE}></Table.Td>
                                <Table.Td style={TABLE_STYLE}></Table.Td>
                                <Table.Td style={TABLE_STYLE}></Table.Td>
                                <Table.Td style={TABLE_STYLE}></Table.Td>
                                <Table.Td style={TABLE_STYLE}></Table.Td>
                            </Table.Tr>,
                            <Table.Tr key={index + '-upgrades'} h={TABLE_ROW_HEIGHT}>
                                <Table.Td style={TABLE_STYLE} colSpan={7}/>
                            </Table.Tr>
                        ];
                    }

                    return [
                        <Table.Tr key={index + '-stats'} h={TABLE_ROW_HEIGHT}>
                            <Table.Td style={TABLE_STYLE}>{weapon.item.name}</Table.Td>
                            <Table.Td style={TABLE_STYLE}>{weapon.item.tags.join(', ')}</Table.Td>
                            <Table.Td
                                style={TABLE_STYLE}>{formatStat(weapon.initiative, weapon.item.initiative)}</Table.Td>
                            <Table.Td style={TABLE_STYLE}>{formatStat(weapon.hit, weapon.item.hit)}</Table.Td>
                            <Table.Td style={TABLE_STYLE}>{diceFormatter(weapon.item.dice)}</Table.Td>
                            <Table.Td style={TABLE_STYLE}>{formatStat(weapon.damage, weapon.item.damage)}</Table.Td>
                            <Table.Td style={TABLE_STYLE}>{weapon.item.effect}</Table.Td>
                        </Table.Tr>,
                        <Table.Tr key={index + '-upgrades'} h={TABLE_ROW_HEIGHT}>
                            <Table.Td colSpan={7} style={TABLE_STYLE}>
                                {`${weapon.remainingUpgradeSlots}/${weapon.upgradeSlots} ${weapon.upgrades.map(u => u.name).join(', ')}`}
                            </Table.Td>
                        </Table.Tr>
                    ];
                })}
                {withShield ?
                    <>
                        <Table.Tr h={TABLE_ROW_HEIGHT}>
                            <Table.Td style={TABLE_STYLE}>{shield?.item.name ?? ''}</Table.Td>
                            <Table.Td style={TABLE_STYLE}>{shield?.item.tags.join(', ') ?? ''}</Table.Td>
                            <Table.Td
                                style={TABLE_STYLE}>{formatStat(shield?.initiative, shield?.item.initiative)}</Table.Td>
                            <Table.Td style={TABLE_STYLE}>{formatStat(shield?.hit, shield?.item.hit)}</Table.Td>
                            <Table.Td style={TABLE_STYLE}>{diceFormatter(shield?.item.dice)}</Table.Td>
                            <Table.Td style={TABLE_STYLE}>{formatStat(shield?.armor, shield?.item.armor)}</Table.Td>
                            <Table.Td style={TABLE_STYLE}>{shield?.item.effect ?? ''}</Table.Td>
                        </Table.Tr>
                        <ShieldExtraLine shield={shield}/>
                    </>
                    : null}
            </Table.Tbody>
        </Table>
        <PageElementSettings>
            <Stack>
                <NumberInput
                    label={t('sheetEditor:numberOfRows')}
                    value={numberOfHandheld}
                    onChange={e => setNumberOfHandheld(Number(e))}
                    min={1}
                    allowDecimal={false}
                />
                <Switch
                    label={t('sheetEditor:withShield')}
                    checked={withShield}
                    onChange={e => setWithShield(e.target.checked)}
                />
            </Stack>
        </PageElementSettings>
    </>;
}

function ShieldExtraLine({shield}: { shield: ShieldEquipment }) {
    const {t} = useTranslation();
    const {itemSettings} = useUniverseContext();

    if (!shield) {
        return <Table.Tr h={TABLE_ROW_HEIGHT}>
            <Table.Td colSpan={7} style={TABLE_STYLE}/>
        </Table.Tr>;
    }

    let description = `${shield.remainingUpgradeSlots}/${shield.upgradeSlots}`;
    if (shield.weight !== 0 && shield.item.weight !== 0) {
        description += ` ${t('weight')}: ` + formatStat(shield.weight, shield.item.weight);
    }
    if (itemSettings.usingProtection && shield.protection !== 0 && shield.item.protection !== 0) {
        description += ` ${t('protection')}: ` + formatStat(shield.protection, shield.item.protection);
    }
    description += ' ' + shield.upgrades.map(u => u.name).join(', ');

    return <Table.Tr h={TABLE_ROW_HEIGHT}>
        <Table.Td colSpan={7} style={TABLE_STYLE}>
            {description}
        </Table.Td>
    </Table.Tr>;
}

function formatStat(current: number, base: number) {
    if (current === base) {
        return current;
    }
    return `${current} (${base})`;
}