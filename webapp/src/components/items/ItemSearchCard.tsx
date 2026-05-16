import {Card, Group, NumberInput, ScrollArea, Stack, TextInput} from '@mantine/core';
import {useMemo, useState} from 'react';
import {ItemCard} from './ItemCard';
import {useTranslation} from 'react-i18next';
import {SomeItem} from '../Constants';

/** Search with amount for items */
export function ItemSearchCard({
    onSelect, items
}: {
    items: SomeItem[];
    onSelect: (item: {
        amount: number;
        item: SomeItem;
    }) => void;
}) {
    const {t} = useTranslation();

    const [filterValue, setFilterValue] = useState('');
    const [amount, setAmount] = useState(1);

    const filteredItems = useMemo(() =>
            items.filter(item => item.name.toLowerCase().includes(filterValue.toLowerCase())),
        [filterValue, items]);

    return <Card miw={1264}>
        <Stack>
            <Group wrap="nowrap">
                <TextInput
                    label={t('search')}
                    value={filterValue}
                    onChange={(e) => setFilterValue(e.target.value)}
                />
                <NumberInput
                    label={t('amount')}
                    value={amount}
                    onChange={(e) => setAmount(Number(e))}
                    allowNegative={false}
                />
            </Group>
            <ScrollArea.Autosize type="auto" mah={500}>
                <Group mih={300} align="flex-start">
                    {filteredItems.slice(0, 3).map(item => <ItemCard
                        key={item.id}
                        item={item}
                        onClick={() => onSelect({amount: amount, item: item})}
                    />)}
                </Group>
                {filteredItems.length > 3 ?
                    <Group mih={300} align="flex-start">
                        {filteredItems.slice(3, 6).map(item => <ItemCard
                            key={item.id}
                            item={item}
                            onClick={() => onSelect({amount: amount, item: item})}
                        />)}
                    </Group>
                    : null
                }
                {filteredItems.length > 6 ?
                    <Group mih={300} align="flex-start">
                        {filteredItems.slice(6, 9).map(item => <ItemCard
                            key={item.id}
                            item={item}
                            onClick={() => onSelect({amount: amount, item: item})}
                        />)}
                    </Group>
                    : null
                }
            </ScrollArea.Autosize>
        </Stack>
    </Card>;
}