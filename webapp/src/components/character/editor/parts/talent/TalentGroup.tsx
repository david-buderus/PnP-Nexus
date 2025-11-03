import {useNode} from "@craftjs/core";
import {ActionIcon, Button, Card, Divider, Group, Modal, Stack, Table, TextInput} from "@mantine/core";
import {EMPTY_TABLE_ROW_HEIGHT, getPartStyle, TABLE_STYLE} from "../Constants";
import React, {useContext, useMemo, useState} from "react";
import {PnPCharacterContext} from "../../PnPCharacterContext";
import {PrimaryAttribute, Talent} from "../../../../../api";
import {FaChevronDown, FaChevronUp, FaRegTrashCan} from "react-icons/fa6";
import {useTranslation} from "react-i18next";
import {useDisclosure} from "@mantine/hooks";
import {ObjectMultiSelect, PrimaryAttributeSelect} from "../../../../input/ObjectSelect";
import {fetchAllTalents} from "../../../../Database";

/** Shows level and co of the character */
export const TalentGroup = ({
    groupName,
    talents,
    firstAttribute,
    secondAttribute,
    thirdAttribute
}: {
    groupName: string;
    talents: Talent[];
    firstAttribute: PrimaryAttribute;
    secondAttribute: PrimaryAttribute;
    thirdAttribute: PrimaryAttribute;
}) => {
    const {character} = useContext(PnPCharacterContext);
    const {connectors: {connect, drag}, selected} = useNode((state => ({
        selected: state.events.selected
    })));

    const attributeString = useMemo(() => {
        let result = "";

        if (firstAttribute) {
            result = firstAttribute.shortName;
        }
        if (secondAttribute) {
            if (firstAttribute) {
                result += " / "
            }
            result += secondAttribute.shortName;
        }
        if (thirdAttribute) {
            if (firstAttribute || secondAttribute) {
                result += " / "
            }
            result += thirdAttribute.shortName;
        }

        return result;

    }, [firstAttribute, secondAttribute, thirdAttribute]);

    return <Table
        withTableBorder
        withColumnBorders
        variant="vertical"
        ref={ref => connect(drag(ref))}
        style={getPartStyle(selected)}
    >
        <Table.Tbody>
            <Table.Tr>
                <Table.Th colSpan={2} style={{
                    width: "40%",
                    textAlign: 'center',
                    fontWeight: 'bold',
                    ...TABLE_STYLE
                }}>
                    {groupName ? groupName : '???'}
                </Table.Th>
                <Table.Th style={{width: "30%", fontWeight: 'bold', textAlign: 'center', ...TABLE_STYLE}}>
                    {attributeString}
                </Table.Th>
                <Table.Th style={{width: "30%", fontWeight: 'bold', textAlign: 'center', ...TABLE_STYLE}}/>
            </Table.Tr>

            {talents.map((talent, index) => {
                if (!talent) {
                    return <Table.Tr key={"empty-row-" + index} h={EMPTY_TABLE_ROW_HEIGHT}>
                        <Table.Th style={{width: "30%", textAlign: 'center', ...TABLE_STYLE}}/>
                        <Table.Th style={{width: "10%", textAlign: 'center', ...TABLE_STYLE}}/>
                        <Table.Td style={{width: "30%", textAlign: 'center', ...TABLE_STYLE}}/>
                        <Table.Td style={{width: "30%", textAlign: 'center', ...TABLE_STYLE}}/>
                    </Table.Tr>;
                }

                return <Table.Tr key={talent.id}>
                    <Table.Th style={{width: "30%", textAlign: 'center', ...TABLE_STYLE}}>{talent.name}</Table.Th>
                    <Table.Th style={{width: "10%", textAlign: 'center', ...TABLE_STYLE}}></Table.Th>
                    <Table.Td style={{width: "30%", textAlign: 'center', ...TABLE_STYLE}}>
                        {
                            (talent.firstAttribute?.shortName ?? '??') + " / " +
                            (talent.secondAttribute?.shortName ?? '??') + " / " +
                            (talent.thirdAttribute?.shortName ?? '??')
                        }
                    </Table.Td>
                    <Table.Td style={{width: "30%", textAlign: 'center', ...TABLE_STYLE}}>
                        {character?.talents[talent.id].totalValue ?? 0}
                    </Table.Td>
                </Table.Tr>
            })}
        </Table.Tbody>
    </Table>;
};

const TalentGroupSettings = () => {
    const {t} = useTranslation();
    const {
        actions: {setProp},
        talentsOrder,
        groupName,
        firstAttribute,
        secondAttribute,
        thirdAttribute
    } = useNode(node => ({
        talentsOrder: node.data.props.talents,
        groupName: node.data.props.groupName,
        firstAttribute: node.data.props.firstAttribute,
        secondAttribute: node.data.props.secondAttribute,
        thirdAttribute: node.data.props.thirdAttribute
    }));

    function moveUp(index: number) {
        const copy = [...talentsOrder];
        const item = copy.splice(index, 1)[0];
        copy.splice(index - 1, 0, item);
        setProp(props => {
            props.talents = copy;
        });
    }

    function moveDown(index: number) {
        const copy = [...talentsOrder];
        const item = copy.splice(index, 1)[0];
        copy.splice(index + 1, 0, item);
        setProp(props => {
            props.talents = copy;
        });
    }

    function add(talent: Talent | Talent[]) {
        const copy = [...talentsOrder];
        if (Array.isArray(talent)) {
            copy.push(...talent);
        } else {
            copy.push(talent);
        }
        setProp(props => {
            props.talents = copy;
        });
    }

    function remove(index: number) {
        const copy = [...talentsOrder];
        copy.splice(index, 1)
        setProp(props => {
            props.talents = copy;
        });
    }

    return <Stack>
        <TextInput
            label={t("name")}
            value={groupName}
            onChange={e => {
                setProp(props => props.groupName = e.currentTarget.value);
            }}
        />
        <PrimaryAttributeSelect
            label={t("character:firstAttribute")}
            value={firstAttribute}
            onChange={a => {
                console.log(a);
                setProp(props => props.firstAttribute = a);
            }}
            clearable
        />
        <PrimaryAttributeSelect
            label={t("character:secondAttribute")}
            value={secondAttribute}
            onChange={a => {
                setProp(props => props.secondAttribute = a);
            }}
            clearable
        />
        <PrimaryAttributeSelect
            label={t("character:thirdAttribute")}
            value={thirdAttribute}
            onChange={a => {
                setProp(props => props.thirdAttribute = a);
            }}
            clearable
        />
        <Divider/>
        <Stack gap={1}>
            {talentsOrder.map((attribute: PrimaryAttribute, index: number) => (
                <Card key={attribute ? attribute.id : ("empty-row-" + index)} shadow="sm">
                    <Group wrap="nowrap" justify="space-between">
                        {attribute?.name ?? t("sheetEditor:emptyRow")}
                        <Group wrap="nowrap" gap={0}>
                            <ActionIcon
                                variant="outline"
                                color="red"
                                onClick={() => remove(index)}
                            >
                                <FaRegTrashCan/>
                            </ActionIcon>
                            <ActionIcon
                                variant="outline"
                                disabled={index === 0}
                                onClick={() => moveUp(index)}
                            >
                                <FaChevronUp/>
                            </ActionIcon>
                            <ActionIcon
                                variant="outline"
                                disabled={index === talentsOrder.length - 1}
                                onClick={() => moveDown(index)}
                            >
                                <FaChevronDown/>
                            </ActionIcon>
                        </Group>
                    </Group>
                </Card>
            ))}
            <AddTalentDialog addTalent={add}/>
            <Card
                style={{cursor: "pointer"}}
                shadow="sm"
                onClick={() => add(null)}
            >
                {t("sheetEditor:addEmptyRow")}
            </Card>
        </Stack>
    </Stack>;
};

function AddTalentDialog({
    addTalent
}: {
    addTalent: (t: Talent | Talent[]) => void;
}) {
    const {t} = useTranslation();
    const [talents] = fetchAllTalents();
    const [opened, {open, close}] = useDisclosure(false);
    const [importTalents, setImportTalents] = useState<Talent[]>([]);

    return <>
        <Modal opened={opened} onClose={close} title={t("sheetEditor:addTalent")}>
            <Stack>
                <ObjectMultiSelect<Talent>
                    label={t("talents")}
                    value={importTalents}
                    onChange={setImportTalents}
                    data={talents}
                    idKey="id"
                    labelKey="name"
                />
                <Group justify="flex-end">
                    <Button autoFocus variant="outline" onClick={close}>
                        {t("cancel")}
                    </Button>
                    <Button type="submit" disabled={!importTalents} onClick={() => {
                        addTalent(importTalents);
                        close();
                    }}>
                        {t("confirm")}
                    </Button>
                </Group>
            </Stack>
        </Modal>
        <Card
            style={{cursor: "pointer"}}
            shadow="sm"
            onClick={open}
        >
            {t("sheetEditor:addTalent")}
        </Card>
    </>
}

TalentGroup.craft = {
    name: "sheetEditor:talentGroup",
    related: {
        settings: TalentGroupSettings
    }
};