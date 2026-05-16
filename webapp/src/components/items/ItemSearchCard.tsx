import {fetchAllItems} from '../Database';
import {Card, Group, NumberInput, Stack, TextInput} from '@mantine/core';
import {useMemo, useState} from 'react';
import {ItemCard} from './ItemCard';
import {useTranslation} from 'react-i18next';
import {SomeItem} from '../Constants';

export function ItemSearchCard({
    onSelect
}: {
    onSelect: (item: {
        amount: number;
        item: SomeItem;
    }) => void;
}) {
    const {t} = useTranslation();
    const [items] = fetchAllItems();

    const [filterValue, setFilterValue] = useState('');
    const [amount, setAmount] = useState(1);

    const filteredItems = useMemo(() =>
            items.filter(item => item.name.toLowerCase().includes(filterValue.toLowerCase())).slice(0, 3),
        [filterValue, items]);

    return <Card>
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
            <Group mih={300} align="flex-start">
                {filteredItems.map(item => <ItemCard
                    key={item.id}
                    item={item}
                    onClick={() => onSelect({amount: amount, item: item})}
                />)}
            </Group>
        </Stack>
    </Card>;
}