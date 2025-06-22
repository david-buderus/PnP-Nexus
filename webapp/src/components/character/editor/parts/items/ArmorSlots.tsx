import {useNode} from "@craftjs/core";
import {Stack, Switch, Table} from "@mantine/core";
import {EMPTY_TABLE_ROW_HEIGHT, getPartStyle, TABLE_STYLE} from "../Constants";
import React, {useContext} from "react";
import {useTranslation} from "react-i18next";
import {PnPCharacterContext} from "../../PnPCharacterContext";
import {EArmorSlot, ShieldEquipment} from "../../../../../api";
import {useUniverseContext} from "../../../../PageBase";
import {diceFormatter} from "../../../../utils/Formatters";


/** Shows armor of the character */
export const ArmorSlots = ({withShield}: { withShield: boolean }) => {
    const {t} = useTranslation();
    const {character} = useContext(PnPCharacterContext);
    const {itemSettings} = useUniverseContext();
    const {connectors: {connect, drag}, selected} = useNode((state => ({
        selected: state.events.selected
    })));

    const shield = character?.equipment.shieldEquipment;

    return <Table
        withTableBorder
        withColumnBorders
        striped
        ref={ref => connect(drag(ref))}
        style={getPartStyle(selected)}
    >
        <Table.Tbody>
            <Table.Tr>
                <Table.Th style={{width: "10%", ...TABLE_STYLE}}></Table.Th>
                <Table.Th style={{width: "15%", ...TABLE_STYLE}}>{t("name")}</Table.Th>
                <Table.Th style={{width: "10%", ...TABLE_STYLE}}>{t("armor")}</Table.Th>
                {itemSettings?.usingProtection ?
                    <Table.Th style={{width: "10%", ...TABLE_STYLE}}>{t("protection")}</Table.Th> : null
                }
                <Table.Th style={{width: "10%", ...TABLE_STYLE}}>{t("weight")}</Table.Th>
                <Table.Th style={{width: "30%", ...TABLE_STYLE}}>{t("effect")}</Table.Th>
            </Table.Tr>
            <ArmorSlot slot={EArmorSlot.Head}/>
            <ArmorSlot slot={EArmorSlot.Body}/>
            <ArmorSlot slot={EArmorSlot.Arms}/>
            <ArmorSlot slot={EArmorSlot.Legs}/>
            {withShield ?
                <>
                    <Table.Tr>
                        <Table.Td style={TABLE_STYLE}>{t("shield")}</Table.Td>
                        <Table.Td style={TABLE_STYLE}>{shield?.item.name ?? ''}</Table.Td>
                        <Table.Td style={TABLE_STYLE}>{formatStat(shield?.armor, shield?.item.armor)}</Table.Td>
                        {itemSettings?.usingProtection ?
                            <Table.Td
                                style={TABLE_STYLE}>{formatStat(shield?.protection, shield?.item.protection)}</Table.Td> : null
                        }
                        <Table.Td style={TABLE_STYLE}>{formatStat(shield?.weight, shield?.item.weight)}</Table.Td>
                        <Table.Td style={TABLE_STYLE}>{shield?.item.effect ?? ''}</Table.Td>
                    </Table.Tr>
                    <ShieldExtraLine shield={shield}/>
                </>
                : null}
        </Table.Tbody>
    </Table>;
};

function ArmorSlot({slot}: { slot: EArmorSlot }) {
    const {t} = useTranslation();
    const {character} = useContext(PnPCharacterContext);
    const {itemSettings} = useUniverseContext();

    const armor = character?.equipment.armor[slot];

    return <>
        <Table.Tr h={EMPTY_TABLE_ROW_HEIGHT}>
            <Table.Td style={TABLE_STYLE}>{t("enum:" + slot.toLowerCase())}</Table.Td>
            <Table.Td style={TABLE_STYLE}>{armor?.item.name ?? ''}</Table.Td>
            <Table.Td style={TABLE_STYLE}>{formatStat(armor?.armor, armor?.item.armor)}</Table.Td>
            {itemSettings?.usingProtection ?
                <Table.Td style={TABLE_STYLE}>{formatStat(armor?.protection, armor?.item.protection)}</Table.Td> : null
            }
            <Table.Td style={TABLE_STYLE}>{formatStat(armor?.weight, armor?.item.weight)}</Table.Td>
            <Table.Td style={TABLE_STYLE}>{armor?.item.effect ?? ''}</Table.Td>
        </Table.Tr>
        <Table.Tr h={EMPTY_TABLE_ROW_HEIGHT}>
            <Table.Td style={TABLE_STYLE} colSpan={itemSettings?.usingProtection ? 6 : 5}>
                {armor ? `${armor.remainingUpgradeSlots}/${armor.upgradeSlots} ${armor.upgrades.map(u => u.name).join(", ")}` : ''}
            </Table.Td>
        </Table.Tr>
    </>;
}

function ShieldExtraLine({shield}: { shield: ShieldEquipment }) {
    const {t} = useTranslation();
    const {itemSettings} = useUniverseContext();

    if (!shield) {
        return <Table.Tr h={EMPTY_TABLE_ROW_HEIGHT}>
            <Table.Td style={TABLE_STYLE} colSpan={itemSettings?.usingProtection ? 6 : 5}/>
        </Table.Tr>;
    }

    let description = `${shield.remainingUpgradeSlots}/${shield.upgradeSlots}`;
    if (shield.hit !== 0 && shield.item.hit !== 0) {
        description += ` ${t("hit")}: ` + formatStat(shield.hit, shield.item.hit);
    }
    if (shield.initiative !== 0 && shield.item.initiative !== 0) {
        description += ` ${t("initiative")}: ` + formatStat(shield.initiative, shield.item.initiative);
    }
    if (shield.item.dice.dices) {
        description += ` ${t("dice")}: ` + diceFormatter(shield.item.dice);
    }
    description += " " + shield.upgrades.map(u => u.name).join(", ");

    return <Table.Tr h={EMPTY_TABLE_ROW_HEIGHT}>
        <Table.Td style={TABLE_STYLE} colSpan={itemSettings?.usingProtection ? 6 : 5}>
            {description}
        </Table.Td>
    </Table.Tr>;
}

function formatStat(current: number, base: number) {
    if (current === undefined) {
        return '';
    }
    if (current === base) {
        return current;
    }
    return `${current} (${base})`;
}

const ArmorSlotsSettings = () => {
    const {t} = useTranslation();
    const {actions: {setProp}, withShield} = useNode(node => ({
        withShield: node.data.props.withShield
    }));

    return <Stack>
        <Switch
            label={t("sheetEditor:withShield")}
            value={withShield}
            onChange={e => setProp(props => {
                props.withShield = Number(e.target.checked);
            })}
        />
    </Stack>;
};

ArmorSlots.craft = {
    name: "sheetEditor:armorSlots",
    related: {
        settings: ArmorSlotsSettings
    }
};