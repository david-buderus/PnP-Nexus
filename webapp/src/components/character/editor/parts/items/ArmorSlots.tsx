import {useNode} from '@craftjs/core';
import {Stack, Switch, Table} from '@mantine/core';
import {getPartStyle, TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext} from 'react';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {EArmorSlot, ShieldEquipment} from '../../../../../api';
import {useUniverseContext} from '../../../../PageBase';
import {diceFormatter} from '../../../../utils/Formatters';
import {OverflowSwitch} from '../../../../utils/OverflowSwitch';
import {FaWeightHanging} from 'react-icons/fa6';
import {GiCrackedShield, GiShield} from 'react-icons/gi';


/** Shows armor of the character */
export const ArmorSlots = ({withShield}: { withShield: boolean }) => {
    const {t} = useTranslation();
    const {characterForm} = useContext(PnPCharacterContext);
    const character = characterForm.getValues();
    const {itemSettings} = useUniverseContext();
    const {connectors: {connect, drag}, selected} = useNode((state => ({
        selected: state.events.selected
    })));

    const shield = character?.equipment.shieldEquipment;

    return <Table
        withTableBorder
        withColumnBorders
        striped
        ref={ref => connect(drag(ref))}
        style={{...getPartStyle(selected), tableLayout: 'fixed'}}
    >
        <Table.Tbody>
            <Table.Tr h={TABLE_ROW_HEIGHT}>
                <Table.Th style={{width: 'max-content', ...TABLE_STYLE}}></Table.Th>
                <Table.Th style={{width: '20%', ...TABLE_STYLE}}>{t('name')}</Table.Th>
                <Table.Th style={{width: '10%', ...TABLE_STYLE}}>
                    <OverflowSwitch fallback={<GiShield/>}>{t('armor')}</OverflowSwitch>
                </Table.Th>
                {itemSettings?.usingProtection ?
                    <Table.Th style={{width: '10%', ...TABLE_STYLE}}>
                        <OverflowSwitch fallback={<GiCrackedShield/>}>{t('protection')}</OverflowSwitch>
                    </Table.Th> : null
                }
                <Table.Th style={{width: '10%', ...TABLE_STYLE}}>
                    <OverflowSwitch fallback={<FaWeightHanging/>}>{t('weight')}</OverflowSwitch>
                </Table.Th>
                <Table.Th
                    style={{
                        width: itemSettings?.usingProtection ? '32%' : '42%',
                        ...TABLE_STYLE
                    }}
                >
                    {t('effect')}
                </Table.Th>
            </Table.Tr>
            <ArmorSlot slot={EArmorSlot.Head}/>
            <ArmorSlot slot={EArmorSlot.Body}/>
            <ArmorSlot slot={EArmorSlot.Arms}/>
            <ArmorSlot slot={EArmorSlot.Legs}/>
            {withShield ?
                <>
                    <Table.Tr h={TABLE_ROW_HEIGHT}>
                        <Table.Td style={TABLE_STYLE}>{t('shield')}</Table.Td>
                        <Table.Td style={TABLE_STYLE}>{shield?.item.name ?? ''}</Table.Td>
                        <Table.Td style={TABLE_STYLE}>{formatStat(shield?.armor, shield?.item.armor)}</Table.Td>
                        {itemSettings?.usingProtection ?
                            <Table.Td
                                style={TABLE_STYLE}>{formatStat(shield?.protection, shield?.item.protection)}</Table.Td> : null
                        }
                        <Table.Td style={TABLE_STYLE}>{formatStat(shield?.weight, shield?.item.weight)}</Table.Td>
                        <Table.Td style={TABLE_STYLE}>{shield?.item.effect ?? ''}</Table.Td>
                    </Table.Tr>
                    <ShieldExtraLine shield={shield}/>
                </>
                : null}
        </Table.Tbody>
    </Table>;
};

function ArmorSlot({slot}: { slot: EArmorSlot }) {
    const {t} = useTranslation();
    const {characterForm} = useContext(PnPCharacterContext);
    const character = characterForm.getValues();
    const {itemSettings} = useUniverseContext();

    const armor = character?.equipment.armor[slot];

    return <>
        <Table.Tr h={TABLE_ROW_HEIGHT}>
            <Table.Td style={TABLE_STYLE}>{t('enum:' + slot.toLowerCase())}</Table.Td>
            <Table.Td style={TABLE_STYLE}>{armor?.item.name ?? ''}</Table.Td>
            <Table.Td style={TABLE_STYLE}>{formatStat(armor?.armor, armor?.item.armor)}</Table.Td>
            {itemSettings?.usingProtection ?
                <Table.Td style={TABLE_STYLE}>{formatStat(armor?.protection, armor?.item.protection)}</Table.Td> : null
            }
            <Table.Td style={TABLE_STYLE}>{formatStat(armor?.weight, armor?.item.weight)}</Table.Td>
            <Table.Td style={TABLE_STYLE}>{armor?.item.effect ?? ''}</Table.Td>
        </Table.Tr>
        <Table.Tr h={TABLE_ROW_HEIGHT}>
            <Table.Td style={TABLE_STYLE} colSpan={itemSettings?.usingProtection ? 6 : 5}>
                {armor ? `${armor.remainingUpgradeSlots}/${armor.upgradeSlots} ${armor.upgrades.map(u => u.name).join(', ')}` : ''}
            </Table.Td>
        </Table.Tr>
    </>;
}

function ShieldExtraLine({shield}: { shield: ShieldEquipment }) {
    const {t} = useTranslation();
    const {itemSettings} = useUniverseContext();

    if (!shield) {
        return <Table.Tr h={TABLE_ROW_HEIGHT}>
            <Table.Td style={TABLE_STYLE} colSpan={itemSettings?.usingProtection ? 6 : 5}/>
        </Table.Tr>;
    }

    let description = `${shield.remainingUpgradeSlots}/${shield.upgradeSlots}`;
    if (shield.hit !== 0 && shield.item.hit !== 0) {
        description += ` ${t('hit')}: ` + formatStat(shield.hit, shield.item.hit);
    }
    if (shield.initiative !== 0 && shield.item.initiative !== 0) {
        description += ` ${t('initiative')}: ` + formatStat(shield.initiative, shield.item.initiative);
    }
    if (shield.item.dice.dices) {
        description += ` ${t('dice')}: ` + diceFormatter(shield.item.dice);
    }
    description += ' ' + shield.upgrades.map(u => u.name).join(', ');

    return <Table.Tr h={TABLE_ROW_HEIGHT}>
        <Table.Td style={TABLE_STYLE} colSpan={itemSettings?.usingProtection ? 6 : 5}>
            {description}
        </Table.Td>
    </Table.Tr>;
}

function formatStat(current: number, base: number) {
    if (current === undefined) {
        return '';
    }
    if (current === base) {
        return current;
    }
    return `${current} (${base})`;
}

const ArmorSlotsSettings = () => {
    const {t} = useTranslation();
    const {actions: {setProp}, withShield} = useNode(node => ({
        withShield: node.data.props.withShield
    }));

    return <Stack>
        <Switch
            label={t('sheetEditor:withShield')}
            value={withShield}
            onChange={e => setProp(props => {
                props.withShield = Number(e.target.checked);
            })}
        />
    </Stack>;
};

ArmorSlots.craft = {
    name: 'sheetEditor:armorSlots',
    related: {
        settings: ArmorSlotsSettings
    }
};