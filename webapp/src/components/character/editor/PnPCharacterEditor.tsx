import {useTranslation} from "react-i18next";
import {useUniverseContext} from "../../PageBase";
import {PnPCharacter, PnPCharacterServiceApi} from "../../../api";
import {API_CONFIGURATION} from "../../Constants";
import React, {useEffect, useState} from "react";
import {AspectRatio, Badge, Button, Group, Paper, Stack, Table, Text, Title} from "@mantine/core";
import {Editor, Element, Frame, useEditor} from "@craftjs/core";
import {StackPart} from "./parts/StackPart";
import {FreeTextPart} from "./parts/FreeTextPart";
import {GroupPart} from "./parts/GroupPart";
import {GridPart} from "./parts/GridPart";

const CHARACTER_API = new PnPCharacterServiceApi(API_CONFIGURATION);

export function PnPCharacterEditor() {
    const {t} = useTranslation();
    const {activeUniverse} = useUniverseContext();

    const [character, setCharacter] = useState<PnPCharacter>(null);

    useEffect(() => {
        if (!activeUniverse) {
            return;
        }
        CHARACTER_API.getAllCharacters(activeUniverse.name).then(response => setCharacter(response.data[0]));
    }, [activeUniverse]);

    if (!character) {
        return <></>;
    }

    return <Group wrap="nowrap">
        <Editor resolver={{StackPart, FreeTextPart, GroupPart, GridPart}}>
            <AspectRatio ratio={1 / 1.4142} w="800px">
                <Paper
                    shadow="sm"
                    p="md"
                    withBorder
                >
                    <Frame>
                        <Element is={StackPart} canvas>
                            <FreeTextPart text="Test A"/>
                            <FreeTextPart text="Test B"/>
                        </Element>
                    </Frame>
                </Paper>

            </AspectRatio>
            <Stack maw={200} align="flex-end">
                <Toolbox/>
                <SettingsPanel/>
            </Stack>
        </Editor>
    </Group>;
}

function Toolbox() {
    const {connectors, query} = useEditor();

    return <Stack>
        <Title order={3}>
            Drag top add
        </Title>
        <Button ref={ref => connectors.create(ref, <Element is={StackPart} canvas/>)}>
            Stack
        </Button>
        <Button ref={ref => connectors.create(ref, <Element is={GroupPart} canvas/>)}>
            Group
        </Button>
        <Button ref={ref => connectors.create(ref, <FreeTextPart text="Test"/>)}>
            Text
        </Button>
        <Button ref={ref => connectors.create(ref, <GridPart columns={2} rows={2}/>)}>
            Grid
        </Button>
    </Stack>;
}

function DescriptionHeader({character}: { character: PnPCharacter }) {
    const {t} = useTranslation();

    return <Group wrap="nowrap" justify="space-between" align="flex-start">
        <Table w="2/5">
            <Table.Tbody>
                <Table.Tr>
                    <Table.Td>
                        <Text>{t("name")}</Text>
                    </Table.Td>
                    <Table.Td>
                        <Text>{character.description.name}</Text>
                    </Table.Td>
                </Table.Tr>
                <Table.Tr>
                    <Table.Td>
                        <Text>{t("race")}</Text>
                    </Table.Td>
                    <Table.Td>
                        <Text>
                            {character.race.name + (character.nation ? " / " + character.nation.name : "")}
                        </Text>
                    </Table.Td>
                </Table.Tr>
                <Table.Tr>
                    <Table.Td>
                        <Text>{t("profession")}</Text>
                    </Table.Td>
                    <Table.Td><Text>{character.description.profession}</Text></Table.Td>
                </Table.Tr>
            </Table.Tbody>
        </Table>
        <Table w="2/5">
            <Table.Tbody>
                <Table.Tr>
                    <Table.Td>
                        <Text>{t("level")}</Text>
                    </Table.Td>
                    <Table.Td>
                        <Text>{character.level}</Text>
                    </Table.Td>
                </Table.Tr>
                <Table.Tr>
                    <Table.Td>
                        <Text>{t("experiencePoints")}</Text>
                    </Table.Td>
                    <Table.Td>
                        <Text>
                            {character.experience}
                        </Text>
                    </Table.Td>
                </Table.Tr>
            </Table.Tbody>
        </Table>
    </Group>;
}

function SettingsPanel() {
    const {actions, selected} = useEditor((state, query) => {
        const [currentNodeId] = Array.from(state.events.selected);
        let selected;

        if (currentNodeId) {
            selected = {
                id: currentNodeId,
                name: state.nodes[currentNodeId].data.name,
                settings: state.nodes[currentNodeId].related && state.nodes[currentNodeId].related.settings,
                isDeletable: query.node(currentNodeId).isDeletable()
            };
        }

        return {
            selected
        };
    });

    return <Stack>
        <Group wrap="nowrap">
            <Text>
                Selected
            </Text>
            <Badge>
                {selected?.name}
            </Badge>
        </Group>
        {
            selected?.settings && React.createElement(selected.settings)
        }
        <Button disabled={!selected?.isDeletable} onClick={() => actions.delete(selected.id)}>
            Delete
        </Button>
    </Stack>;
}