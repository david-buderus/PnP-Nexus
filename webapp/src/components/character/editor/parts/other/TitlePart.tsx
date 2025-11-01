import {useNode} from '@craftjs/core';
import {Group, NumberInput, Stack, TextInput, Title} from '@mantine/core';
import React from 'react';
import {getPartStyle} from '../Constants';
import {useTranslation} from 'react-i18next';
import {TitleOrder} from '@mantine/core/lib/components/Title/Title';

/** Part to show text */
export const TitlePart = ({text, order = 1}: {
    text: string;
    order: TitleOrder;
}) => {
    const {t} = useTranslation();
    const {connectors: {connect, drag}, selected} = useNode(((state) => ({
        selected: state.events.selected
    })));

    return <Group
        ref={ref => connect(drag(ref))}
        justify="center"
        style={{backgroundColor: 'var(--mantine-color-gray-0)'}}
    >
        <Title
            style={{whiteSpace: 'pre-line', ...getPartStyle(selected)}}
            order={order}
        >
            {text ? text : t('nothing-here')}
        </Title>
    </Group>;
};

const TitleSettings = () => {
    const {t} = useTranslation();
    const {actions: {setProp}, order, text} = useNode(node => ({
        order: node.data.props.order,
        text: node.data.props.text
    }));

    return <Stack>
        <TextInput
            label={t('sheetEditor:content')}
            value={text}
            onChange={e => {
                setProp(props => props.text = e.currentTarget.value);
            }}
        />
        <NumberInput
            label={t('sheetEditor:order')}
            value={order ?? 1}
            onChange={e => setProp(props => {
                props.order = Number(e);
            })}
            min={1}
            max={6}
            allowDecimal={false}
        />
    </Stack>;
};

TitlePart.craft = {
    name: 'sheetEditor:title',
    related: {
        settings: TitleSettings
    }
};