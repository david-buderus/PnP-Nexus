import {ActionIcon, Group, NumberInput, Popover, Stack, Switch, Table, Text} from '@mantine/core';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext, useMemo, useState} from 'react';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {diceFormatter} from '../../../../utils/Formatters';
import {ItemStackServiceApi, ShieldEquipment, Weapon, WeaponEquipment} from '../../../../../api';
import {useUniverseContext} from '../../../../PageBase';
import {PageElementSettings} from '../PageElementSettings';
import {IconCircleMinus, IconCirclePlus} from '@tabler/icons-react';
import {fetchAllWeapons} from '../../../../Database';
import {ItemSearchCard} from '../../../../items/ItemSearchCard';
import {ItemStackCardModal} from '../../../../items/ItemStackCard';
import {API_CONFIGURATION, SomeEquipment} from '../../../../Constants';
import {ShieldAdditionPopover} from './ArmorSlots';
import {GiMagicAxe} from 'react-icons/gi';
import {UpgradeControl} from '../../../../items/UpgradeControl';

const STACK_SERVICE = new ItemStackServiceApi(API_CONFIGURATION);

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
    const {characterForm, allowEdit} = useContext(PnPCharacterContext);
    const character = characterForm.getValues();

    const weapons = useMemo(() => {
        const w = [...character.equipment.weaponEquipments];
        w.length = numberOfHandheld;
        w.fill(null, character.equipment.weaponEquipments.length);
        return w;
    }, [character.equipment, numberOfHandheld]);
    const shield = character?.equipment.shieldEquipment;

    const [lastClicked, setLastClicked] = useState<WeaponEquipment | ShieldEquipment>(null);

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
                                <Table.Td style={TABLE_STYLE}>
                                    <Group justify="flex-end">
                                        <WeaponAdditionPopover/>
                                    </Group>
                                </Table.Td>
                            </Table.Tr>,
                            <Table.Tr key={index + '-upgrades'} h={TABLE_ROW_HEIGHT}>
                                <Table.Td style={TABLE_STYLE} colSpan={7}/>
                            </Table.Tr>
                        ];
                    }

                    return [
                        <Table.Tr
                            key={index + '-stats'}
                            h={TABLE_ROW_HEIGHT}
                            onClick={allowEdit ? () => setLastClicked(weapon) : null}
                        >
                            <Table.Td style={TABLE_STYLE}>{weapon.item.name}</Table.Td>
                            <Table.Td style={TABLE_STYLE}>{weapon.item.tags.join(', ')}</Table.Td>
                            <Table.Td style={TABLE_STYLE}>
                                {formatStat(weapon.initiative, weapon.item.initiative)}
                            </Table.Td>
                            <Table.Td style={TABLE_STYLE}>{formatStat(weapon.hit, weapon.item.hit)}</Table.Td>
                            <Table.Td style={TABLE_STYLE}>{diceFormatter(weapon.item.dice)}</Table.Td>
                            <Table.Td style={TABLE_STYLE}>{formatStat(weapon.damage, weapon.item.damage)}</Table.Td>
                            <Table.Td style={TABLE_STYLE}>
                                <Group justify="space-between" wrap="nowrap" style={{width: '100%'}}>
                                    <Text
                                        size={TABLE_STYLE.fontSize}
                                        truncate="end"
                                        style={{flex: 1, minWidth: 0}}
                                    >
                                        {weapon.item.effects.map(e => e.description).join(',')}
                                    </Text>
                                    <Group
                                        wrap="nowrap"
                                        gap={1}
                                        style={{flexShrink: 0}}
                                        onClick={e => e.stopPropagation()}
                                    >
                                        <UpgradePopover
                                            equipment={weapon}
                                            onChange={w => characterForm.replaceListItem('equipment.weaponEquipments', index, w)}
                                        />
                                        <ActionIcon
                                            variant="subtle"
                                            size={TABLE_ROW_HEIGHT - 8}
                                            className="no-drag"
                                            style={{flexShrink: 0}}
                                            onClick={e => {
                                                characterForm.removeListItem('equipment.weaponEquipments', index);
                                                e.stopPropagation();
                                            }}
                                        >
                                            <IconCircleMinus color="red" size={14}/>
                                        </ActionIcon>
                                    </Group>
                                </Group>
                            </Table.Td>
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
                            <Group justify="space-between" wrap="nowrap" style={{width: '100%'}}>
                                <Text
                                    size={TABLE_STYLE.fontSize}
                                    truncate="end"
                                    style={{flex: 1, minWidth: 0}}
                                >
                                    {shield?.item.effects.map(e => e.description).join(',') ?? ''}
                                </Text>
                                {shield ?
                                    <ActionIcon
                                        variant="subtle"
                                        size={TABLE_ROW_HEIGHT - 8}
                                        className="no-drag"
                                        style={{flexShrink: 0}}
                                        onClick={e => {
                                            characterForm.setFieldValue('equipment.shieldEquipment', null);
                                            e.stopPropagation();
                                        }}
                                    >
                                        <IconCircleMinus color="red" size={14}/>
                                    </ActionIcon> :
                                    <ShieldAdditionPopover/>
                                }
                            </Group>
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
        <ItemStackCardModal stack={lastClicked} onClose={() => setLastClicked(null)}/>
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

function WeaponAdditionPopover() {
    const {allowEdit, characterForm} = useContext(PnPCharacterContext);
    const [items] = fetchAllWeapons();

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
                        STACK_SERVICE.createWeapon({item: item.item as Weapon, stackSize: 1})
                            .then(response => characterForm.insertListItem('equipment.weaponEquipments', response.data))
                    }
                />
            </Popover.Dropdown>
        </Popover>
    );
}

function UpgradePopover<E extends SomeEquipment>({
    equipment, onChange
}: {
    equipment: E;
    onChange: (equipment: E) => void;
}) {
    const {allowEdit} = useContext(PnPCharacterContext);

    if (!allowEdit) {
        return null;
    }

    return (
        <Popover
            position="bottom"
            withArrow
            shadow="md"
        >
            <Popover.Target>
                <ActionIcon
                    variant="subtle"
                    size={TABLE_ROW_HEIGHT - 8}
                    className="no-drag"
                >
                    <GiMagicAxe size={14}/>
                </ActionIcon>
            </Popover.Target>
            <Popover.Dropdown>
                <UpgradeControl equipment={equipment} onChange={onChange}/>
            </Popover.Dropdown>
        </Popover>
    );
}