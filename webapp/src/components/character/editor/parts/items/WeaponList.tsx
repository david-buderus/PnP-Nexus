import {useNode} from '@craftjs/core';
import {NumberInput, Stack, Switch, Table} from '@mantine/core';
import {getPartStyle, TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext, useMemo} from 'react';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../PnPCharacterContext';
import {diceFormatter} from '../../../../utils/Formatters';
import {ShieldEquipment} from '../../../../../api';
import {useUniverseContext} from '../../../../PageBase';


/** Shows weapons of the character */
export const WeaponList = ({numberOfHandheld, withShield}: {
    numberOfHandheld: number;
    withShield: boolean;
}) => {
    const {t} = useTranslation();
    const {character} = useContext(PnPCharacterContext);
    const {connectors: {connect, drag}, selected} = useNode((state => ({
        selected: state.events.selected
    })));

    const weapons = useMemo(() => {
        const w = [...character.equipment.weaponEquipments];
        w.length = numberOfHandheld;
        w.fill(null, character.equipment.weaponEquipments.length);
        return w;
    }, [character.equipment, numberOfHandheld]);
    const shield = character?.equipment.shieldEquipment;

    return <Table
        withTableBorder
        withColumnBorders
        striped
        ref={ref => connect(drag(ref))}
        style={getPartStyle(selected)}
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
                        <Table.Td style={TABLE_STYLE}>{formatStat(weapon.initiative, weapon.item.initiative)}</Table.Td>
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
    </Table>;
};

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

const WeaponListSettings = () => {
    const {t} = useTranslation();
    const {actions: {setProp}, numberOfHandheld, withShield} = useNode(node => ({
        numberOfHandheld: node.data.props.numberOfHandheld,
        withShield: node.data.props.withShield
    }));

    return <Stack>
        <NumberInput
            label={t('sheetEditor:numberOfRows')}
            value={numberOfHandheld}
            onChange={e => setProp(props => {
                props.numberOfHandheld = Number(e);
            })}
        />
        <Switch
            label={t('sheetEditor:withShield')}
            value={withShield}
            onChange={e => setProp(props => {
                props.withShield = Number(e.target.checked);
            })}
        />
    </Stack>;
};

WeaponList.craft = {
    name: 'sheetEditor:weaponList',
    related: {
        settings: WeaponListSettings
    }
};