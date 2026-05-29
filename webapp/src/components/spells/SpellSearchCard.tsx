import {Card, Group, ScrollArea, Stack, TextInput} from '@mantine/core';
import {useMemo, useState} from 'react';
import {useTranslation} from 'react-i18next';
import {Spell} from '../../api/model';
import {SpellCard} from './SpellCard';

/** Search for spells */
export function SpellSearchCard({
    onSelect, spells
}: {
    spells: Spell[];
    onSelect: (spell: Spell) => void;
}) {
    const {t} = useTranslation();

    const [filterValue, setFilterValue] = useState('');

    const filteredSpells = useMemo(() =>
            spells.filter(spell => spell.name.toLowerCase().includes(filterValue.toLowerCase())),
        [filterValue, spells]);

    return <Card miw={1264}>
        <Stack>
            <Group wrap="nowrap">
                <TextInput
                    label={t('search')}
                    value={filterValue}
                    onChange={(e) => setFilterValue(e.target.value)}
                />
            </Group>
            <ScrollArea.Autosize type="auto" mah={500}>
                <Stack>
                    <Group mih={300} align="flex-start">
                        {filteredSpells.slice(0, 3).map(spell => <SpellCard
                            key={spell.id}
                            spell={spell}
                            onClick={() => onSelect(spell)}
                        />)}
                    </Group>
                    {filteredSpells.length > 3 ?
                        <Group mih={300} align="flex-start">
                            {filteredSpells.slice(3, 6).map(spell => <SpellCard
                                key={spell.id}
                                spell={spell}
                                onClick={() => onSelect(spell)}
                            />)}
                        </Group>
                        : null
                    }
                    {filteredSpells.length > 6 ?
                        <Group mih={300} align="flex-start">
                            {filteredSpells.slice(6, 9).map(spell => <SpellCard
                                key={spell.id}
                                spell={spell}
                                onClick={() => onSelect(spell)}
                            />)}
                        </Group>
                        : null
                    }
                </Stack>
            </ScrollArea.Autosize>
        </Stack>
    </Card>;
}