import {useNode} from "@craftjs/core";
import {Table} from "@mantine/core";
import {getPartStyle, TABLE_STYLE} from "../Constants";
import React, {useContext} from "react";
import {useTranslation} from "react-i18next";
import {PnPCharacterContext} from "../../PnPCharacterContext";

/** Shows level and co of the character */
export const LevelInfo = () => {
    const {t} = useTranslation();
    const {character} = useContext(PnPCharacterContext);
    const {connectors: {connect, drag}, selected} = useNode((state => ({
        selected: state.events.selected
    })));

    return <Table
        variant="vertical"
        layout="fixed"
        withTableBorder
        ref={ref => connect(drag(ref))}
        style={getPartStyle(selected)}
    >
        <Table.Tbody>
            <Table.Tr>
                <Table.Th style={TABLE_STYLE}>{t("level")}</Table.Th>
                <Table.Td style={TABLE_STYLE}>{character?.level.level}</Table.Td>
            </Table.Tr>

            <Table.Tr>
                <Table.Th style={TABLE_STYLE}>{t("experiencePoints")}</Table.Th>
                <Table.Td style={TABLE_STYLE}>{character?.level.experience}</Table.Td>
            </Table.Tr>

            <Table.Tr>
                <Table.Th style={TABLE_STYLE}>{t("skillPoints")}</Table.Th>
                <Table.Td style={TABLE_STYLE}>{character?.level.skillPoints}</Table.Td>
            </Table.Tr>
        </Table.Tbody>
    </Table>;
};

LevelInfo.craft = {
    name: "sheetEditor:levelInfo"
};