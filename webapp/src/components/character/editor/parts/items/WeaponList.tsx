import {ActionIcon, Group, NumberInput, Popover, Stack, Table, Text} from '@mantine/core';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext, useMemo, useState} from 'react';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {diceFormatter} from '../../../../utils/Formatters';
import {ShieldEquipment, Weapon, WeaponEquipment} from '../../../../../api/model';
import {useUniverseContext} from '../../../../PageBase';
import {PageElementSettings} from '../PageElementSettings';
import {IconCircleMinus, IconCirclePlus} from '@tabler/icons-react';
import {fetchAllWeapons} from '../../../../Database';
import {ItemSearchCard} from '../../../../items/ItemSearchCard';
import {ItemStackCardModal} from '../../../../items/ItemStackCard';
import {UpgradePopover} from '../../../../items/UpgradeControl';
import {ShieldAdditionPopover} from './ArmorSlots';
import {useCreateWeapon} from '../../../../../api/item-stack-service/item-stack-service';
import {handleNetworkErrors} from '../../../../utils/ErrorUtils';

/** Shows weapons of the character */
export function WeaponList({
    numberOfWeapons,
    numberOfShields,
    setNumberOfWeapons,
    setNumberOfShields,
}: {
    numberOfWeapons: number;
    numberOfShields: number;
    setNumberOfWeapons: (n: number) => void;
    setNumberOfShields: (n: number) => void;
}) {
    const {t} = useTranslation();
    const {characterForm} = useContext(PnPCharacterContext);
    const character = characterForm.getValues();

    const weapons = useMemo(() => {
        const w = [...character.equipment.weapons];
        w.length = numberOfWeapons;
        w.fill(null, character.equipment.weapons.length);
        return w;
    }, [character.equipment, numberOfWeapons]);

    const shields = useMemo(() => {
        const s = [...character.equipment.shields];
        s.length = numberOfShields;
        s.fill(null, character.equipment.shields.length);
        return s;
    }, [character.equipment, numberOfShields]);

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
                        return <EmptyRow key={'weapon-' + index} isWeapon={true}/>;
                    }
                    return <WeaponRow
                        key={'weapon-' + index}
                        index={index}
                        weapon={weapon}
                        setLastClicked={setLastClicked}
                    />;
                })}
                {shields.map((shield, index) => {
                    if (!shield) {
                        return <EmptyRow key={'shield-' + index} isWeapon={false}/>;
                    }
                    return <ShieldRow
                        key={'shield-' + index}
                        index={index}
                        shield={shield}
                        setLastClicked={setLastClicked}
                    />;
                })}
            </Table.Tbody>
        </Table>
        <PageElementSettings>
            <Stack>
                <NumberInput
                    label={t('sheetEditor:numberOfWeapons')}
                    value={numberOfWeapons}
                    onChange={e => setNumberOfWeapons(Number(e))}
                    min={1}
                    allowDecimal={false}
                />
                <NumberInput
                    label={t('sheetEditor:numberOfShields')}
                    value={numberOfShields}
                    onChange={e => setNumberOfShields(Number(e))}
                    min={0}
                    allowDecimal={false}
                />
            </Stack>
        </PageElementSettings>
        <ItemStackCardModal
            stack={lastClicked as (WeaponEquipment | ShieldEquipment)}
            onClose={() => setLastClicked(null)}
        />
    </>;
}

function EmptyRow({
    isWeapon
}: {
    isWeapon: boolean;
}) {
    return [
        <Table.Tr key="details-row" h={TABLE_ROW_HEIGHT}>
            <Table.Td style={TABLE_STYLE}/>
            <Table.Td style={TABLE_STYLE}/>
            <Table.Td style={TABLE_STYLE}/>
            <Table.Td style={TABLE_STYLE}/>
            <Table.Td style={TABLE_STYLE}/>
            <Table.Td style={TABLE_STYLE}/>
            <Table.Td style={TABLE_STYLE}>
                <Group justify="flex-end">
                    {isWeapon ?
                        <WeaponAdditionPopover/> :
                        <ShieldAdditionPopover/>
                    }
                </Group>
            </Table.Td>
        </Table.Tr>,
        <Table.Tr key="info-row" h={TABLE_ROW_HEIGHT}>
            <Table.Td style={TABLE_STYLE} colSpan={7}/>
        </Table.Tr>
    ];
}

function WeaponRow({
    index,
    weapon,
    setLastClicked
}: {
    index: number;
    weapon: WeaponEquipment;
    setLastClicked: (w: WeaponEquipment) => void;
}) {
    const {characterForm, allowEdit} = useContext(PnPCharacterContext);

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
                            item={weapon}
                            onChange={w => characterForm.replaceListItem('equipment.weapons', index, w)}
                        />
                        <ActionIcon
                            variant="subtle"
                            size={TABLE_ROW_HEIGHT - 8}
                            className="no-drag"
                            style={{flexShrink: 0}}
                            onClick={() => characterForm.removeListItem('equipment.weapons', index)}
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
}

function ShieldRow({
    index,
    shield,
    setLastClicked
}: {
    index: number;
    shield: ShieldEquipment;
    setLastClicked: (s: ShieldEquipment) => void;
}) {
    const {characterForm, allowEdit} = useContext(PnPCharacterContext);

    return [
        <Table.Tr
            h={TABLE_ROW_HEIGHT}
            key={'details-row-' + index}
            onClick={allowEdit ? () => setLastClicked(shield) : null}
        >
            <Table.Td style={TABLE_STYLE}>{shield?.item.name ?? ''}</Table.Td>
            <Table.Td style={TABLE_STYLE}>{shield?.item.tags.join(', ') ?? ''}</Table.Td>
            <Table.Td
                style={TABLE_STYLE}>{formatStat(shield?.initiative, shield?.item.initiative)}</Table.Td>
            <Table.Td style={TABLE_STYLE}>{formatStat(shield?.hit, shield?.item.hit)}</Table.Td>
            <Table.Td style={TABLE_STYLE}>{diceFormatter(shield?.item.dice)}</Table.Td>
            <Table.Td style={TABLE_STYLE}>{formatStat(shield?.armor, shield?.item.armor)}</Table.Td>
            <Table.Td style={TABLE_STYLE}>
                <Group justify="space-between" wrap="nowrap" style={{width: '100%'}}>
                    <Text
                        size={TABLE_STYLE.fontSize}
                        truncate="end"
                        style={{flex: 1, minWidth: 0}}
                    >
                        {shield?.item.effects.map(e => e.description).join(',') ?? ''}
                    </Text>
                    <Group
                        wrap="nowrap"
                        gap={1}
                        style={{flexShrink: 0}}
                        onClick={e => e.stopPropagation()}
                    >
                        <UpgradePopover
                            item={shield}
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
                    </Group>
                </Group>
            </Table.Td>
        </Table.Tr>,
        <ShieldExtraLine key={'info-row-' + index} shield={shield}/>
    ];
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
    const {mutate: createWeapon} = useCreateWeapon({
        mutation: {
            onSuccess: response => characterForm.insertListItem('equipment.weapons', response.data),
            onError: handleNetworkErrors
        }
    });

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
                    onSelect={item => createWeapon({
                        data: {item: item.item as Weapon, stackSize: 1}
                    })}
                />
            </Popover.Dropdown>
        </Popover>
    );
}

