import {Card, Group, ScrollArea, Stack, TextInput} from '@mantine/core';
import {useMemo, useState} from 'react';
import {useTranslation} from 'react-i18next';
import {Upgrade} from '../../api/model';
import {UpgradeCard} from './UpgradeCard';

/** Search with amount for items */
export function UpgradeSearchCard({
    onSelect, upgrades
}: {
    upgrades: Upgrade[];
    onSelect: (upgrade: Upgrade) => void;
}) {
    const {t} = useTranslation();

    const [filterValue, setFilterValue] = useState('');

    const filteredUpgrades = useMemo(() =>
            upgrades.filter(upgrade => upgrade.name.toLowerCase().includes(filterValue.toLowerCase())),
        [filterValue, upgrades]);

    return <Card miw={1264}>
        <Stack>
            <Group align="space-between">
                <TextInput
                    label={t('search')}
                    value={filterValue}
                    onChange={(e) => setFilterValue(e.target.value)}
                />
            </Group>
            <ScrollArea.Autosize type="auto" mah={500}>
                <Group mih={300} align="flex-start">
                    {filteredUpgrades.slice(0, 3).map(upgrade => <UpgradeCard
                        key={upgrade.id}
                        upgrade={upgrade}
                        onClick={() => onSelect(upgrade)}
                    />)}
                </Group>
                {filteredUpgrades.length > 3 ?
                    <Group mih={300} align="flex-start">
                        {filteredUpgrades.slice(3, 6).map(upgrade => <UpgradeCard
                            key={upgrade.id}
                            upgrade={upgrade}
                            onClick={() => onSelect(upgrade)}
                        />)}
                    </Group>
                    : null
                }
                {filteredUpgrades.length > 6 ?
                    <Group mih={300} align="flex-start">
                        {filteredUpgrades.slice(6, 9).map(upgrade => <UpgradeCard
                            key={upgrade.id}
                            upgrade={upgrade}
                            onClick={() => onSelect(upgrade)}
                        />)}
                    </Group>
                    : null
                }
            </ScrollArea.Autosize>
        </Stack>
    </Card>;
}