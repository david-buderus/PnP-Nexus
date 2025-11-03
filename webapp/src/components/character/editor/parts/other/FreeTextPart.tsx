import {useNode} from "@craftjs/core";
import {Select, Stack, Text, Textarea} from "@mantine/core";
import React from "react";
import {getPartStyle} from "../Constants";
import {useTranslation} from "react-i18next";

/** Part to show text */
export const FreeTextPart = ({text, fontSize = "md"}: {
    text: string;
    fontSize: string;
}) => {
    const {t} = useTranslation();
    const {connectors: {connect, drag}, selected} = useNode(((state) => ({
        selected: state.events.selected
    })));

    return <Text
        ref={ref => connect(drag(ref))}
        style={{whiteSpace: 'pre-line', ...getPartStyle(selected)}}
        size={fontSize}
    >
        {text ? text : t("nothing-here")}
    </Text>;
};

const FreeTextSettings = () => {
    const {t} = useTranslation();
    const {actions: {setProp}, fontSize, text} = useNode(node => ({
        fontSize: node.data.props.fontSize,
        text: node.data.props.text
    }));

    return <Stack>
        <Textarea
            label={t("sheetEditor:content")}
            value={text}
            onChange={e => {
                setProp(props => props.text = e.currentTarget.value);
            }}
            rows={5}
        />
        <Select
            label={t("sheetEditor:fontSize")}
            value={fontSize ?? 'md'}
            onChange={e => setProp(props => {
                props.fontSize = e;
            })}
            data={['xs', 'sm', 'md', 'lg', 'xl']}
        />
    </Stack>;
};

FreeTextPart.craft = {
    name: "sheetEditor:freeText",
    related: {
        settings: FreeTextSettings
    }
};