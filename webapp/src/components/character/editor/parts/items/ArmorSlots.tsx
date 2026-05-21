import {ActionIcon, Group, NumberInput, Popover, Table, Text} from '@mantine/core';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext, useMemo, useState} from 'react';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {Armor, ArmorEquipment, EArmorSlot, ItemStackServiceApi, Shield, ShieldEquipment} from '../../../../../api';
import {useUniverseContext} from '../../../../PageBase';
import {diceFormatter} from '../../../../utils/Formatters';
import {OverflowSwitch} from '../../../../utils/OverflowSwitch';
import {FaWeightHanging} from 'react-icons/fa6';
import {GiCrackedShield, GiShield} from 'react-icons/gi';
import {PageElementSettings} from '../PageElementSettings';
import {fetchAllArmor, fetchAllShields} from '../../../../Database';
import {IconCircleMinus, IconCirclePlus} from '@tabler/icons-react';
import {ItemSearchCard} from '../../../../items/ItemSearchCard';
import {API_CONFIGURATION} from '../../../../Constants';
import {ItemStackCardModal} from '../../../../items/ItemStackCard';
import {UpgradePopover} from '../../../../items/UpgradeControl';

const STACK_SERVICE = new ItemStackServiceApi(API_CONFIGURATION);

/** Shows armor of the character */
export function ArmorSlots({numberOfShieldRows, setNumberOfShieldRows}: {
    numberOfShieldRows: number;
    setNumberOfShieldRows: (n: number) => void;
}) {
    const {t} = useTranslation();
    const {characterForm} = useContext(PnPCharacterContext);
    const character = characterForm.getValues();
    const {itemSettings} = useUniverseContext();

    const [lastClicked, setLastClicked] = useState<ArmorEquipment | ShieldEquipment>(null);

    const shields = useMemo(() => {
        const s = [...character.equipment.shields];
        s.length = numberOfShieldRows;
        s.fill(null, character.equipment.shields.length);
        return s;
    }, [character.equipment, numberOfShieldRows]);

    return <>
        <Table
            withTableBorder
            withColumnBorders
            striped
            layout="fixed"
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
                <ArmorSlot slot={EArmorSlot.Head} setLastClicked={setLastClicked}/>
                <ArmorSlot slot={EArmorSlot.Body} setLastClicked={setLastClicked}/>
                <ArmorSlot slot={EArmorSlot.Arms} setLastClicked={setLastClicked}/>
                <ArmorSlot slot={EArmorSlot.Legs} setLastClicked={setLastClicked}/>
                {shields.map((shield, index) => <ShieldRow
                    key={index}
                    shield={shield}
                    index={index}
                    setLastClicked={setLastClicked}
                    numberOfShieldRows={numberOfShieldRows}
                />)}
            </Table.Tbody>
        </Table>
        <PageElementSettings>
            <NumberInput
                label={t('sheetEditor:numberOfShields')}
                value={numberOfShieldRows}
                onChange={e => setNumberOfShieldRows(Number(e))}
                min={0}
                allowDecimal={false}
            />
        </PageElementSettings>
        <ItemStackCardModal stack={lastClicked} onClose={() => setLastClicked(null)}/>
    </>;
}

function ArmorSlot({slot, setLastClicked}: {
    slot: EArmorSlot,
    setLastClicked: (equipment: ArmorEquipment) => void
}) {
    const {t} = useTranslation();
    const {characterForm, allowEdit} = useContext(PnPCharacterContext);
    const character = characterForm.getValues();
    const {itemSettings} = useUniverseContext();

    const armor = character?.equipment.armor[slot];

    return <>
        <Table.Tr
            h={TABLE_ROW_HEIGHT}
            onClick={allowEdit ? () => setLastClicked(armor ?? null) : null}
        >
            <Table.Td style={TABLE_STYLE}>{t('enum:' + slot.toLowerCase())}</Table.Td>
            <Table.Td style={TABLE_STYLE}>{armor?.item.name ?? ''}</Table.Td>
            <Table.Td style={TABLE_STYLE}>{formatStat(armor?.armor, armor?.item.armor)}</Table.Td>
            {itemSettings?.usingProtection ?
                <Table.Td style={TABLE_STYLE}>{formatStat(armor?.protection, armor?.item.protection)}</Table.Td> : null
            }
            <Table.Td style={TABLE_STYLE}>{formatStat(armor?.weight, armor?.item.weight)}</Table.Td>
            <Table.Td style={TABLE_STYLE}>
                <Group justify="space-between" wrap="nowrap" style={{width: '100%'}}>
                    <Text
                        size={TABLE_STYLE.fontSize}
                        truncate="end"
                        style={{flex: 1, minWidth: 0}}
                    >
                        {armor?.item.effects.map(e => e.description).join(',') ?? ''}
                    </Text>
                    {armor ?
                        <Group
                            wrap="nowrap"
                            gap={1}
                            style={{flexShrink: 0}}
                            onClick={e => e.stopPropagation()}
                        >
                            <UpgradePopover
                                equipment={armor}
                                onChange={a => characterForm.setFieldValue(`equipment.armor.${slot}`, a)}
                            />
                            <ActionIcon
                                variant="subtle"
                                size={TABLE_ROW_HEIGHT - 8}
                                className="no-drag"
                                style={{flexShrink: 0}}
                                onClick={() => characterForm.setFieldValue(`equipment.armor.${slot}`, null)}
                            >
                                <IconCircleMinus color="red" size={14}/>
                            </ActionIcon>
                        </Group> :
                        <ArmorAdditionPopover slot={slot}/>
                    }
                </Group>
            </Table.Td>
        </Table.Tr>
        <Table.Tr h={TABLE_ROW_HEIGHT}>
            <Table.Td style={TABLE_STYLE} colSpan={itemSettings?.usingProtection ? 6 : 5}>
                {armor ? `${armor.remainingUpgradeSlots}/${armor.upgradeSlots} ${armor.upgrades.map(u => u.name).join(', ')}` : ''}
            </Table.Td>
        </Table.Tr>
    </>;
}

function ShieldRow({
    shield, index, setLastClicked, numberOfShieldRows
}: {
    shield: ShieldEquipment;
    index: number;
    setLastClicked: (equipment: ShieldEquipment) => void;
    numberOfShieldRows: number;
}) {
    const {t} = useTranslation();
    const {characterForm, allowEdit} = useContext(PnPCharacterContext);
    const {itemSettings} = useUniverseContext();

    return [
        <Table.Tr
            h={TABLE_ROW_HEIGHT}
            onClick={allowEdit ? () => setLastClicked(shield ?? null) : null}
        >
            <Table.Td style={TABLE_STYLE}>
                {t('shield') + (numberOfShieldRows > 1 ? ` ${index + 1}` : '')}
            </Table.Td>
            <Table.Td style={TABLE_STYLE}>{shield?.item.name ?? ''}</Table.Td>
            <Table.Td style={TABLE_STYLE}>{formatStat(shield?.armor, shield?.item.armor)}</Table.Td>
            {itemSettings?.usingProtection ?
                <Table.Td
                    style={TABLE_STYLE}>{formatStat(shield?.protection, shield?.item.protection)}</Table.Td> : null
            }
            <Table.Td style={TABLE_STYLE}>{formatStat(shield?.weight, shield?.item.weight)}</Table.Td>
            <Table.Td style={TABLE_STYLE}>
                <Group justify="space-between" wrap="nowrap" style={{width: '100%'}}>
                    <Text
                        size={TABLE_STYLE.fontSize}
                        truncate="end"
                        style={{flex: 1, minWidth: 0}}
                    >
                        {shield?.item.effects.map(e => e.description).join(',') ?? ''}
                    </Text>
                    {shield ?
                        <Group
                            wrap="nowrap"
                            gap={1}
                            style={{flexShrink: 0}}
                            onClick={e => e.stopPropagation()}
                        >
                            <UpgradePopover
                                equipment={shield}
                                onChange={s => characterForm.replaceListItem('equipment.shields', index, s)}
                            />
                            <ActionIcon
                                variant="subtle"
                                size={TABLE_ROW_HEIGHT - 8}
                                className="no-drag"
                                style={{flexShrink: 0}}
                                onClick={() => characterForm.removeListItem('equipment.shields', index)}
                            >
                                <IconCircleMinus color="red" size={14}/>
                            </ActionIcon>
                        </Group> :
                        <ShieldAdditionPopover/>
                    }
                </Group>
            </Table.Td>
        </Table.Tr>,
        <ShieldExtraLine shield={shield}/>
    ];
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
    if (shield.item.dice.dices && shield.item.dice.dices.length > 0) {
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

function ArmorAdditionPopover({slot}: { slot: EArmorSlot }) {
    const {allowEdit, characterForm} = useContext(PnPCharacterContext);
    const [items] = fetchAllArmor();
    const filtered = useMemo(() => items.filter(i => i.armorSlot === slot), [items, slot]);

    if (!allowEdit) {
        return null;
    }

    return (
        <Popover position="bottom" withArrow shadow="md">
            <Popover.Target>
                <ActionIcon
                    variant="subtle"
                    size={TABLE_ROW_HEIGHT - 8}
                    className="no-drag"
                >
                    <IconCirclePlus size={14}/>
                </ActionIcon>
            </Popover.Target>
            <Popover.Dropdown>
                <ItemSearchCard
                    items={filtered}
                    onSelect={item =>
                        STACK_SERVICE.createArmor({item: item.item as Armor, stackSize: 1})
                            .then(response => characterForm.setFieldValue(`equipment.armor.${slot}`, response.data))
                    }
                />
            </Popover.Dropdown>
        </Popover>
    );
}

/** Popover to add a shield */
export function ShieldAdditionPopover() {
    const {allowEdit, characterForm} = useContext(PnPCharacterContext);
    const [items] = fetchAllShields();

    if (!allowEdit) {
        return null;
    }

    return (
        <Popover position="bottom" withArrow shadow="md">
            <Popover.Target>
                <ActionIcon
                    variant="subtle"
                    size={TABLE_ROW_HEIGHT - 8}
                    className="no-drag"
                >
                    <IconCirclePlus size={14}/>
                </ActionIcon>
            </Popover.Target>
            <Popover.Dropdown>
                <ItemSearchCard
                    items={items}
                    onSelect={item =>
                        STACK_SERVICE.createShield({item: item.item as Shield, stackSize: 1})
                            .then(response => characterForm.insertListItem('equipment.shields', response.data))
                    }
                />
            </Popover.Dropdown>
        </Popover>
    );
}