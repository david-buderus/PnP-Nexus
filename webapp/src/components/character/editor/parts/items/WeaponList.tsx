import {ActionIcon, Group, NumberInput, Popover, Stack, Switch, Table, Text} from '@mantine/core';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext, useMemo, useState} from 'react';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {diceFormatter} from '../../../../utils/Formatters';
import {Shield, ShieldEquipment, Weapon, WeaponEquipment} from '../../../../../api/model';
import {useUniverseContext} from '../../../../PageBase';
import {PageElementSettings} from '../PageElementSettings';
import {IconCircleMinus, IconCirclePlus} from '@tabler/icons-react';
import {fetchAllShields, fetchAllWeapons} from '../../../../Database';
import {ItemSearchCard} from '../../../../items/ItemSearchCard';
import {ItemStackCardModal} from '../../../../items/ItemStackCard';
import {UpgradePopover} from '../../../../items/UpgradeControl';
import {useCreateShield, useCreateWeapon} from '../../../../../api/item-stack-service/item-stack-service';
import {handleNetworkErrors} from '../../../../utils/ErrorUtils';

/** Shows weapons of the character */
export function WeaponList({
    numberOfRows,
    numberOfFallbackRows,
    setNumberOfRows,
    setNumberOfFallbackRows,
    showShields,
    setShowShields
}: {
    numberOfRows: number;
    numberOfFallbackRows: number;
    setNumberOfRows: (n: number) => void;
    setNumberOfFallbackRows: (n: number) => void;
    showShields: boolean;
    setShowShields: (b: boolean) => void;
}) {
    const {t} = useTranslation();
    const {characterForm} = useContext(PnPCharacterContext);
    const character = characterForm.getValues();

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
                <HandHeldRows
                    weapons={character?.equipment?.weapons ?? []}
                    shields={showShields ? character?.equipment?.shields ?? [] : []}
                    totalRows={numberOfRows}
                    weaponPath="weapons"
                    shieldPath="shields"
                    setLastClicked={setLastClicked}
                    highlightLastRow={numberOfFallbackRows > 0}
                    showShields={showShields}
                />
                <HandHeldRows
                    weapons={character?.equipment?.fallbackWeapons ?? []}
                    shields={showShields ? character?.equipment?.fallbackShields ?? [] : []}
                    totalRows={numberOfFallbackRows}
                    weaponPath="fallbackWeapons"
                    shieldPath="fallbackShields"
                    setLastClicked={setLastClicked}
                    highlightLastRow={false}
                    showShields={showShields}
                />
            </Table.Tbody>
        </Table>
        <PageElementSettings>
            <Stack>
                <NumberInput
                    label={t('sheetEditor:numberOfRows')}
                    value={numberOfRows}
                    onChange={e => setNumberOfRows(Number(e))}
                    min={1}
                    allowDecimal={false}
                />
                <NumberInput
                    label={t('sheetEditor:numberOfFallbackRows')}
                    value={numberOfFallbackRows}
                    onChange={e => setNumberOfFallbackRows(Number(e))}
                    min={0}
                    allowDecimal={false}
                />
                <Switch
                    label={t('sheetEditor:showShields')}
                    checked={showShields}
                    onChange={e => setShowShields(e.target.checked)}
                />
            </Stack>
        </PageElementSettings>
        <ItemStackCardModal
            stack={lastClicked as (WeaponEquipment | ShieldEquipment)}
            onClose={() => setLastClicked(null)}
        />
    </>;
}

function HandHeldRows({
    weapons,
    shields,
    totalRows,
    weaponPath,
    shieldPath,
    setLastClicked,
    highlightLastRow,
    showShields
}: {
    weapons: WeaponEquipment[];
    shields: ShieldEquipment[];
    totalRows: number;
    weaponPath: 'weapons' | 'fallbackWeapons';
    shieldPath: 'shields' | 'fallbackShields';
    setLastClicked: (w: WeaponEquipment | ShieldEquipment) => void;
    highlightLastRow: boolean;
    showShields: boolean;
}) {
    const data = useTableData(weapons, shields, totalRows);

    return data.map((row, index) => {
        const highlightRow = highlightLastRow && (index === data.length - 1);
        if (!row) {
            return <EmptyRow
                key={row.id}
                weaponPath={weaponPath}
                shieldPath={shieldPath}
                highlightRow={highlightRow}
                showShields={showShields}
            />;
        }
        switch (row.type) {
            case 'WEAPON':
                return <WeaponRow
                    key={row.id}
                    index={row.originalIndex}
                    weapon={row.data as WeaponEquipment}
                    setLastClicked={setLastClicked}
                    path={weaponPath}
                    highlightRow={highlightRow}
                />;
            case 'SHIELD':
                return <ShieldRow
                    key={row.id}
                    index={row.originalIndex}
                    shield={row.data as ShieldEquipment}
                    setLastClicked={setLastClicked}
                    path={shieldPath}
                    highlightRow={highlightRow}
                />;
            case 'EMPTY':
            default:
                return <EmptyRow
                    key={row.id}
                    weaponPath={weaponPath}
                    shieldPath={shieldPath}
                    highlightRow={highlightRow}
                    showShields={showShields}
                />;
        }
    });
}

function EmptyRow({
    weaponPath,
    shieldPath,
    highlightRow,
    showShields,
}: {
    weaponPath: 'weapons' | 'fallbackWeapons';
    shieldPath: 'shields' | 'fallbackShields';
    highlightRow: boolean;
    showShields: boolean;
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
                    <AdditionPopover weaponPath={weaponPath} shieldPath={shieldPath} showShields={showShields}/>
                </Group>
            </Table.Td>
        </Table.Tr>,
        <Table.Tr
            key="info-row"
            h={TABLE_ROW_HEIGHT}
            style={highlightRow ? {
                borderBottom: '3px solid var(--mantine-color-gray-4)',
            } : undefined}
        >
            <Table.Td style={TABLE_STYLE} colSpan={7}/>
        </Table.Tr>
    ];
}

function WeaponRow({
    index,
    weapon,
    setLastClicked,
    path,
    highlightRow,
}: {
    index: number;
    weapon: WeaponEquipment;
    setLastClicked: (w: WeaponEquipment) => void;
    path: 'weapons' | 'fallbackWeapons';
    highlightRow: boolean;
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
                    {allowEdit ?
                        <Group
                            wrap="nowrap"
                            gap={1}
                            style={{flexShrink: 0}}
                            onClick={e => e.stopPropagation()}
                        >
                            <UpgradePopover
                                item={weapon}
                                onChange={w => characterForm.replaceListItem('equipment.' + path, index, w)}
                            />
                            <ActionIcon
                                variant="subtle"
                                size={TABLE_ROW_HEIGHT - 8}
                                className="no-drag"
                                style={{flexShrink: 0}}
                                onClick={() => characterForm.removeListItem('equipment.' + path, index)}
                            >
                                <IconCircleMinus color="red" size={14}/>
                            </ActionIcon>
                        </Group> : null
                    }
                </Group>
            </Table.Td>
        </Table.Tr>,
        <Table.Tr
            key={index + '-upgrades'}
            h={TABLE_ROW_HEIGHT}
            style={highlightRow ? {
                borderBottom: '3px solid var(--mantine-color-gray-4)',
            } : undefined}
        >
            <Table.Td colSpan={7} style={TABLE_STYLE}>
                {`${weapon.remainingUpgradeSlots}/${weapon.upgradeSlots} ${weapon.upgrades.map(u => u.name).join(', ')}`}
            </Table.Td>
        </Table.Tr>
    ];
}

function ShieldRow({
    index,
    shield,
    setLastClicked,
    path,
    highlightRow
}: {
    index: number;
    shield: ShieldEquipment;
    setLastClicked: (s: ShieldEquipment) => void;
    path: 'shields' | 'fallbackShields';
    highlightRow: boolean;
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
                    {allowEdit ?
                        <Group
                            wrap="nowrap"
                            gap={1}
                            style={{flexShrink: 0}}
                            onClick={e => e.stopPropagation()}
                        >
                            <UpgradePopover
                                item={shield}
                                onChange={s => characterForm.replaceListItem('equipment.' + path, index, s)}
                            />
                            <ActionIcon
                                variant="subtle"
                                size={TABLE_ROW_HEIGHT - 8}
                                className="no-drag"
                                style={{flexShrink: 0}}
                                onClick={() => characterForm.removeListItem('equipment.' + path, index)}
                            >
                                <IconCircleMinus color="red" size={14}/>
                            </ActionIcon>
                        </Group> : null
                    }
                </Group>
            </Table.Td>
        </Table.Tr>,
        <ShieldExtraLine key={'info-row-' + index} shield={shield} highlightRow={highlightRow}/>
    ];
}

function ShieldExtraLine({
    shield, highlightRow
}: {
    shield: ShieldEquipment;
    highlightRow: boolean;
}) {
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

    return <Table.Tr
        h={TABLE_ROW_HEIGHT}
        style={highlightRow ? {
            borderBottom: '3px solid var(--mantine-color-gray-4)',
        } : undefined}
    >
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

function AdditionPopover({
    weaponPath,
    shieldPath,
    showShields
}: {
    weaponPath: 'weapons' | 'fallbackWeapons';
    shieldPath: 'shields' | 'fallbackShields';
    showShields: boolean;
}) {
    const {allowEdit, characterForm} = useContext(PnPCharacterContext);
    const [weapons] = fetchAllWeapons();
    const [shields] = fetchAllShields();
    const items = useMemo(() => {
        if (showShields) {
            return [...weapons, ...shields];
        }
        return weapons;
    }, [weapons, shields, showShields]);

    const {mutate: createWeapon} = useCreateWeapon({
        mutation: {
            onSuccess: response => characterForm.insertListItem('equipment.' + weaponPath, response.data),
            onError: handleNetworkErrors
        }
    });
    const {mutate: createShield} = useCreateShield({
        mutation: {
            onSuccess: response => characterForm.insertListItem('equipment.' + shieldPath, response.data),
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
                    onSelect={item => {
                        if (item.item['@type'] === 'Weapon') {
                            createWeapon({
                                data: {item: item.item as Weapon, stackSize: 1}
                            });
                        } else {
                            createShield({
                                data: {item: item.item as Shield, stackSize: 1}
                            });
                        }
                    }}
                />
            </Popover.Dropdown>
        </Popover>
    );
}

type CombinedRow = {
    id: string;
    type: 'WEAPON' | 'SHIELD' | 'EMPTY';
    originalIndex: number | null;
    data: WeaponEquipment | ShieldEquipment;
}

function useTableData(weapons: WeaponEquipment[], shields: ShieldEquipment[], totalRows: number): CombinedRow[] {
    return useMemo(() => {
        const combined: CombinedRow[] = [];

        weapons.forEach((weapon, index) => {
            combined.push({
                id: `W-${index}-${weapon?.item?.id || 'empty'}`,
                type: 'WEAPON',
                originalIndex: index,
                data: weapon,
            });
        });

        shields.forEach((shield, index) => {
            combined.push({
                id: `S-${index}-${shield?.item?.id || 'empty'}`,
                type: 'SHIELD',
                originalIndex: index,
                data: shield,
            });
        });

        if (combined.length > totalRows) {
            return combined.slice(0, totalRows);
        }

        while (combined.length < totalRows) {
            combined.push({
                id: `empty-${combined.length}`,
                type: 'EMPTY',
                originalIndex: null,
                data: null,
            });
        }

        return combined;
    }, [weapons, shields, totalRows]);
}