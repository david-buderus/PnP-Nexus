import {Select, Stack, Text, Textarea} from '@mantine/core';
import React from 'react';
import {useTranslation} from 'react-i18next';
import {PageElementSettings} from '../PageElementSettings';

/** Part to show text */
export function FreeTextPart({
    text,
    fontSize = 'md',
    setText,
    setFontSize
}: {
    text: string;
    fontSize: string;
    setText: (text: string) => void;
    setFontSize: (fontSize: string) => void;
}) {
    const {t} = useTranslation();

    return <>
        <Stack justify="center" h="100%">
            <Text size={fontSize}>
                {text ? text : t('nothing-here')}
            </Text>
        </Stack>
        <PageElementSettings>
            <Stack>
                <Textarea
                    label={t('sheetEditor:content')}
                    value={text}
                    onChange={e => setText(e.currentTarget.value)}
                    rows={5}
                />
                <Select
                    label={t('sheetEditor:fontSize')}
                    value={fontSize ?? 'md'}
                    onChange={e => setFontSize(e)}
                    data={['xs', 'sm', 'md', 'lg', 'xl']}
                />
            </Stack>
        </PageElementSettings>
    </>;
}