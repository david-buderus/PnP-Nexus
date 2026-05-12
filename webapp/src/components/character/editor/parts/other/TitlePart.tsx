import {Group, NumberInput, Stack, TextInput, Title} from '@mantine/core';
import React from 'react';
import {useTranslation} from 'react-i18next';
import {TitleOrder} from '@mantine/core/lib/components/Title/Title';
import {PageElementSettings} from '../PageElementSettings';

/** Part to show text */
export function TitlePart({text, order, setText, setOrder}: {
    text: string;
    order: TitleOrder;
    setText: (text: string) => void;
    setOrder: (order: TitleOrder) => void;
}) {
    const {t} = useTranslation();

    return <>
        <Group
            justify="center"
            style={{backgroundColor: 'var(--mantine-color-gray-0)'}}
        >
            <Title
                style={{whiteSpace: 'pre-line'}}
                order={order}
            >
                {text ? text : t('nothing-here')}
            </Title>
        </Group>
        <PageElementSettings>
            <Stack>
                <TextInput
                    label={t('sheetEditor:content')}
                    value={text}
                    onChange={e => setText(e.currentTarget.value)}
                />
                <NumberInput
                    label={t('sheetEditor:order')}
                    value={order ?? 1}
                    onChange={e => setOrder(Number(e) as TitleOrder)}
                    min={1}
                    max={6}
                    allowDecimal={false}
                />
            </Stack>
        </PageElementSettings>
    </>;
}
