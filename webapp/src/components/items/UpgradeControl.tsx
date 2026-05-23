import {ActionIcon, Box, Card, Group, Popover, ScrollArea, Stack, Text, TextInput} from '@mantine/core';
import {useTranslation} from 'react-i18next';
import React, {useContext, useMemo, useState} from 'react';
import {fetchAllUpgrades} from '../Database';
import {SomeItemStack} from '../Constants';
import {UpgradeCard} from './UpgradeCard';
import {Upgrade} from '../../api';
import {IconCheck} from '@tabler/icons-react';
import {PnPCharacterContext} from '../character/PnPCharacterContext';
import {TABLE_ROW_HEIGHT} from '../character/editor/parts/Constants';
import {GiMagicAxe} from 'react-icons/gi';
import {getPossibleUpgradeRestriction} from '../utils/UpgradeUtils';
import {removeUpgradeFromItem, upgradeItem} from '../utils/InventoryUtils';


/**
 * Popover to control upgrades on items.
 */
export function UpgradePopover<I extends SomeItemStack>({
    item, onChange
}: {
    item: I;
    onChange: (item: I) => void;
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
                <UpgradeControl item={item} onChange={onChange}/>
            </Popover.Dropdown>
        </Popover>
    );
}

function UpgradeControl<I extends SomeItemStack>({
    item, onChange
}: {
    item: I;
    onChange: (item: I) => void;
}) {
    const [upgrades] = fetchAllUpgrades();
    const {t} = useTranslation();

    const [filterValue, setFilterValue] = useState('');

    const sortedUpgrades = useMemo(() =>
            item.upgrades.map(u => ({used: true, upgrade: u})).concat(upgrades
                .filter(upgrade => upgrade.name.toLowerCase().includes(filterValue.toLowerCase()))
                .filter(upgrade => !item.upgrades.map(u => u.id).includes(upgrade.id))
                .filter(upgrade => getPossibleUpgradeRestriction(item.item['@type']).includes(upgrade.restriction))
                .map(u => ({used: false, upgrade: u}))),
        [filterValue, upgrades, item]);

    function onClick(upgrade: Upgrade, used: boolean) {
        if (used) {
            removeUpgradeFromItem(item, upgrade).then(onChange);
        } else {
            upgradeItem(item, upgrade).then(onChange);
        }
    }

    return <Card miw={1264}>
        <Stack>
            <Group justify="space-between">
                <TextInput
                    label={t('search')}
                    value={filterValue}
                    onChange={(e) => setFilterValue(e.target.value)}
                />
                <Text>
                    {t('upgradeSlots') + ': ' + item.remainingUpgradeSlots + '/' + item.upgradeSlots}
                </Text>
            </Group>
            <ScrollArea.Autosize type="auto" mah={500}>
                <Stack gap={1}>
                    <Group mih={300} align="flex-start">
                        {sortedUpgrades.slice(0, 3).map(u => <EquippedUpgradeCard
                            key={u.upgrade.id}
                            upgrade={u.upgrade}
                            used={u.used}
                            onClick={() => onClick(u.upgrade, u.used)}
                            item={item}
                        />)}
                    </Group>
                    {sortedUpgrades.length > 3 ?
                        <Group mih={300} align="flex-start">
                            {sortedUpgrades.slice(3, 6).map(u => <EquippedUpgradeCard
                                key={u.upgrade.id}
                                upgrade={u.upgrade}
                                used={u.used}
                                onClick={() => onClick(u.upgrade, u.used)}
                                item={item}
                            />)}
                        </Group>
                        : null
                    }
                    {sortedUpgrades.length > 6 ?
                        <Group mih={300} align="flex-start">
                            {sortedUpgrades.slice(6, 9).map(u => <EquippedUpgradeCard
                                key={u.upgrade.id}
                                upgrade={u.upgrade}
                                used={u.used}
                                onClick={() => onClick(u.upgrade, u.used)}
                                item={item}
                            />)}
                        </Group>
                        : null
                    }
                </Stack>
            </ScrollArea.Autosize>
        </Stack>
    </Card>;
}

function EquippedUpgradeCard({
    used, upgrade, onClick, item
}: {
    used: boolean;
    upgrade: Upgrade;
    onClick: () => void;
    item: SomeItemStack;
}) {
    return <Box pos="relative">
        <UpgradeCard
            upgrade={upgrade}
            onClick={onClick}
            enoughSlots={used || upgrade.slots <= item.remainingUpgradeSlots}
        />
        {used ? <IconCheck
            style={{
                position: 'absolute',
                bottom: 4,
                right: 8,
                zIndex: 1,
            }}
            color="green"
        /> : null}
    </Box>;
}