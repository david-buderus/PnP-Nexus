import {useTranslation} from 'react-i18next';
import {useUniverseContext} from '../../PageBase';
import {PnPCharacterDto, PnPCharacterServiceApi} from '../../../api';
import {API_CONFIGURATION} from '../../Constants';
import React, {useEffect, useMemo, useState} from 'react';
import {Accordion, ActionIcon, Badge, Button, Group, Modal, Stack, Text, Textarea, Title} from '@mantine/core';
import {Editor, Element, Frame, useEditor} from '@craftjs/core';
import {StackPart} from './parts/layout/StackPart';
import {FreeTextPart} from './parts/other/FreeTextPart';
import {GroupPart} from './parts/layout/GroupPart';
import {GridPart} from './parts/layout/GridPart';
import {PnPCharacterContext} from './PnPCharacterContext';
import {CharacterInfo} from './parts/character/CharacterInfo';
import {LevelInfo} from './parts/character/LevelInfo';
import {PrimaryAttributeInfo} from './parts/stats/PrimaryAttributeInfo';
import {SecondaryAttributeInfo} from './parts/stats/SecondaryAttributeInfo';
import {fetchAllPrimaryAttributes, fetchAllSecondaryAttributes} from '../../Database';
import {WeaponList} from './parts/items/WeaponList';
import {CharacterSheetPaper} from './parts/CharacterSheetPaper';
import {FaChevronRight} from 'react-icons/fa6';
import {FaChevronLeft} from 'react-icons/fa';
import {ArmorSlots} from './parts/items/ArmorSlots';
import {JewelleryList} from './parts/items/JewelleryList';
import {InventoryPart} from './parts/items/InventoryPart';
import {TextFieldPart} from './parts/other/TextFieldPart';
import {PrimaryAttributeRow} from './parts/stats/PrimaryAttributeRow';
import {useDisclosure} from '@mantine/hooks';
import {TitlePart} from './parts/other/TitlePart';
import {TalentGroup} from './parts/talent/TalentGroup';
import {CharacterDescriptionInfo} from './parts/character/CharacterDesciptionInfo';
import {AdvantagesInfo} from './parts/character/AdvantagesInfo';

const CHARACTER_API = new PnPCharacterServiceApi(API_CONFIGURATION);

/** Interface for the context */
interface SheetEditor {
    selectedPage: number;
}

/** Context in sheet editor */
export const SheetEditorContext = React.createContext<SheetEditor>(null);

/** Editor to create character sheets */
export function PnPCharacterSheetEditor() {
    const {activeUniverse} = useUniverseContext();

    const [character, setCharacter] = useState<PnPCharacterDto>(null);
    const [pages, setPages] = useState(1);
    const [selectedPage, setSelectedPage] = useState(0);
    console.log(pages, selectedPage);

    useEffect(() => {
        if (!activeUniverse) {
            return;
        }
        CHARACTER_API.getAllCharacters(activeUniverse.name).then(response => setCharacter(response.data[0]));
    }, [activeUniverse]);

    if (!character) {
        return <></>;
    }

    return <Group wrap="nowrap" align="flex-start">
        <PnPCharacterContext.Provider value={{character}}>
            <SheetEditorContext.Provider value={{selectedPage}}>
                <Editor resolver={{
                    StackPart,
                    FreeTextPart,
                    TextFieldPart,
                    TitlePart,
                    GroupPart,
                    GridPart,
                    CharacterInfo,
                    LevelInfo,
                    CharacterDescriptionInfo,
                    PrimaryAttributeInfo,
                    SecondaryAttributeInfo,
                    PrimaryAttributeRow,
                    WeaponList,
                    ArmorSlots,
                    JewelleryList,
                    InventoryPart,
                    TalentGroup,
                    AdvantagesInfo,
                    CharacterSheetPaper
                }}>
                    <Stack>
                        <Stack id="print-section">
                            <Frame>
                                <Element is={StackPart}>
                                    <Element is={CharacterSheetPaper} pageNumber={0} id="page-0" canvas/>
                                </Element>
                            </Frame>
                        </Stack>
                        <Group justify="space-around">
                            <RemovePageButton
                                pages={pages}
                                setPages={setPages}
                                selectedPage={selectedPage}
                                setSelectedPage={setSelectedPage}
                            />
                            <Group gap={0}>
                                <ActionIcon
                                    style={{
                                        borderTopRightRadius: 0,
                                        borderBottomRightRadius: 0,
                                    }}
                                    disabled={selectedPage === 0}
                                    onClick={() => setSelectedPage(prev => prev - 1)}
                                >
                                    <FaChevronLeft/>
                                </ActionIcon>
                                <ActionIcon
                                    style={{
                                        borderTopLeftRadius: 0,
                                        borderBottomLeftRadius: 0,
                                        borderLeft: 0
                                    }}
                                    disabled={selectedPage === pages - 1}
                                    onClick={() => setSelectedPage(prev => prev + 1)}
                                >
                                    <FaChevronRight/>
                                </ActionIcon>
                            </Group>
                            <AddPageButton pages={pages} setPages={setPages}/>
                        </Group>
                    </Stack>
                    <Stack w={300}>
                        <Toolbox/>
                        <SettingsPanel setPages={setPages}/>
                    </Stack>
                </Editor>
            </SheetEditorContext.Provider>
        </PnPCharacterContext.Provider>
    </Group>;
}

function RemovePageButton({selectedPage, setSelectedPage, pages, setPages}: {
    selectedPage: number,
    setSelectedPage: (value: (((prevState: number) => number) | number)) => void
    pages: number,
    setPages: (value: (((prevState: number) => number) | number)) => void
}) {
    const {t} = useTranslation();
    const {actions, query} = useEditor();

    const handleRemove = () => {
        if (pages < 2) {
            return;
        }

        const allNodes = query.getNodes();
        const pageId = `page-${selectedPage}`;

        const targetEntry = Object.entries(allNodes).find(([, node]) => {
            return node.data.props?.id === pageId;
        });

        if (!targetEntry) {
            console.warn(`Node with props.id=${pageId} not found.`);
            return;
        }

        const [nodeIdToDelete] = targetEntry;
        actions.delete(nodeIdToDelete);

        const remainingNodes = Object.entries(query.getNodes())
            .filter(([, node]) => node.data.type === CharacterSheetPaper);

        remainingNodes.forEach(([nodeId, node]) => {
            const {pageNumber} = node.data.props;

            if (pageNumber > selectedPage) {
                actions.setProp(nodeId, props => {
                    props.pageNumber = pageNumber - 1;
                    props.id = `page-${pageNumber - 1}`;
                });
            }
        });

        setPages(prev => prev - 1);

        if (selectedPage > 0) {
            setSelectedPage(selectedPage - 1);
        }
    };

    return <Button
        variant="outline"
        color="red"
        disabled={pages < 2}
        onClick={handleRemove}
    >
        {t('sheetEditor:removePage')}
    </Button>;
}

function AddPageButton({pages, setPages}: {
    pages: number,
    setPages: (value: (((prevState: number) => number) | number)) => void
}) {
    const {t} = useTranslation();
    const {actions, query} = useEditor();

    const handleAdd = () => {
        const newNode = query.parseReactElement(
            <Element
                is={CharacterSheetPaper}
                pageNumber={pages}
                id={'page-' + pages}
                canvas
            />
        ).toNodeTree();
        actions.addNodeTree(newNode, 'ROOT');
        setPages(prev => prev + 1);
    };

    return <Button onClick={handleAdd}>
        {t('sheetEditor:addPage')}
    </Button>;
}

function Toolbox() {
    const {t} = useTranslation();
    const {connectors} = useEditor();
    const [primaryAttributes] = fetchAllPrimaryAttributes();
    const [secondaryAttributes] = fetchAllSecondaryAttributes();
    const {equipmentSettings} = useUniverseContext();

    return <Stack>
        <Title order={3}>
            Drag top add
        </Title>
        <Accordion>
            <Accordion.Item value="layout">
                <Accordion.Control>{t('sheetEditor:layout')}</Accordion.Control>
                <Accordion.Panel>
                    <Stack gap="xs">
                        <Button ref={ref => connectors.create(ref, <Element is={StackPart} canvas/>)}>
                            {t('sheetEditor:vertical')}
                        </Button>
                        <Button ref={ref => connectors.create(ref, <Element is={GroupPart} canvas/>)}>
                            {t('sheetEditor:horizontal')}
                        </Button>
                    </Stack>
                </Accordion.Panel>
            </Accordion.Item>
            <Accordion.Item value="character">
                <Accordion.Control>{t('character')}</Accordion.Control>
                <Accordion.Panel>
                    <Stack gap="xs">
                        <Button ref={ref => connectors.create(ref, <CharacterInfo/>)}>
                            {t('sheetEditor:characterInfo')}
                        </Button>
                        <Button ref={ref => connectors.create(ref, <LevelInfo/>)}>
                            {t('sheetEditor:levelInfo')}
                        </Button>
                        <Button ref={ref => connectors.create(ref, <CharacterDescriptionInfo
                            description={null} numberOfRows={5}/>)}>
                            {t('sheetEditor:description')}
                        </Button>
                        <Button ref={ref => connectors.create(ref, <AdvantagesInfo
                            showsAdvantages={true} numberOfRows={5}/>)}>
                            {t('advantages')}
                        </Button>
                    </Stack>
                </Accordion.Panel>
            </Accordion.Item>
            <Accordion.Item value="stats">
                <Accordion.Control>{t('sheetEditor:stats')}</Accordion.Control>
                <Accordion.Panel>
                    <Stack gap="xs">
                        <Button
                            ref={ref => connectors.create(ref, <PrimaryAttributeInfo attributes={primaryAttributes}/>)}>
                            {t('sheetEditor:primaryAttributeInfo')}
                        </Button>
                        <Button ref={ref => connectors.create(ref, <SecondaryAttributeInfo
                            attributes={secondaryAttributes}/>)}>
                            {t('sheetEditor:secondaryAttributeInfo')}
                        </Button>
                        <Button ref={ref => connectors.create(ref, <PrimaryAttributeRow
                            attributes={primaryAttributes}/>)}>
                            {t('sheetEditor:primaryAttributeRow')}
                        </Button>
                    </Stack>
                </Accordion.Panel>
            </Accordion.Item>
            <Accordion.Item value="items">
                <Accordion.Control>{t('items')}</Accordion.Control>
                <Accordion.Panel>
                    <Stack gap="xs">
                        <Button ref={ref => connectors.create(ref, <WeaponList
                            numberOfHandheld={equipmentSettings?.numberOfHandheld} withShield={false}/>)}>
                            {t('sheetEditor:weaponList')}
                        </Button>
                        <Button ref={ref => connectors.create(ref, <ArmorSlots
                            withShield={false}/>)}>
                            {t('sheetEditor:armorSlots')}
                        </Button>
                        <Button ref={ref => connectors.create(ref, <JewelleryList
                            numberOfJewellery={equipmentSettings?.jewelleryDefinitions
                                .reduce((acc, item) => {
                                    acc[item.name] = item.amount;
                                    return acc;
                                }, {} as Record<string, number>)}/>)}>
                            {t('sheetEditor:jewelleryList')}
                        </Button>
                        <Button ref={ref => connectors.create(ref, <InventoryPart rows={8} columns={3}/>)}>
                            {t('inventory')}
                        </Button>
                    </Stack>
                </Accordion.Panel>
            </Accordion.Item>
            <Accordion.Item value="talents">
                <Accordion.Control>{t('talents')}</Accordion.Control>
                <Accordion.Panel>
                    <Stack gap="xs">
                        <Button ref={ref => connectors.create(ref, <TalentGroup
                            talents={[]}
                            groupName=""
                            firstAttribute={null}
                            secondAttribute={null}
                            thirdAttribute={null}
                        />)}>
                            {t('sheetEditor:talentGroup')}
                        </Button>
                    </Stack>
                </Accordion.Panel>
            </Accordion.Item>
            <Accordion.Item value="other">
                <Accordion.Control>{t('other')}</Accordion.Control>
                <Accordion.Panel>
                    <Stack gap="xs">
                        <Button ref={ref => connectors.create(ref, <FreeTextPart text="" fontSize="md"/>)}>
                            {t('sheetEditor:freeText')}
                        </Button>
                        <Button ref={ref => connectors.create(ref, <TextFieldPart text="" title="" numberOfRows={3}/>)}>
                            {t('sheetEditor:textField')}
                        </Button>
                        <Button ref={ref => connectors.create(ref, <TitlePart text="" order={1}/>)}>
                            {t('sheetEditor:title')}
                        </Button>
                    </Stack>
                </Accordion.Panel>
            </Accordion.Item>
        </Accordion>
    </Stack>;
}

function SettingsPanel({setPages}: { setPages: (p: number) => void }) {
    const {t} = useTranslation();
    const {actions, selected} = useEditor((state, query) => {
        const [currentNodeId] = Array.from(state.events.selected);
        let s;

        if (currentNodeId) {
            s = {
                id: currentNodeId,
                name: state.nodes[currentNodeId].data.displayName,
                settings: state.nodes[currentNodeId].related && state.nodes[currentNodeId].related.settings,
                isDeletable: query.node(currentNodeId).isDeletable() && state.nodes[currentNodeId].data.name !== 'CharacterSheetPaper'
            };
        }

        return {
            selected: s
        };
    });

    return <Stack>
        <Group wrap="nowrap">
            <Text>
                {t('sheetEditor:selected')}
            </Text>
            <Badge>
                {selected?.name ? t(selected?.name) : '???'}
            </Badge>
        </Group>
        {
            selected?.settings && React.createElement(selected.settings)
        }
        <Button disabled={!selected?.isDeletable} onClick={() => actions.delete(selected.id)}>
            {t('delete')}
        </Button>
        <Button onClick={() => {
            actions.selectNode(null);
            window.print();
        }}>
            {t('print')}
        </Button>
        <ExportModal/>
        <ImportModal setPages={setPages}/>
    </Stack>;
}

function ExportModal() {
    const {t} = useTranslation();
    const {query} = useEditor();
    const [opened, {open, close}] = useDisclosure(false);

    const result = useMemo(() => btoa(query.serialize()), [query, opened]);

    return <>
        <Modal opened={opened} onClose={close} title={t('todo')} maw={300}>
            <Textarea readOnly value={result} rows={10}/>
            <Group justify="flex-end" pt="md">
                <Button type="submit" onClick={() => {
                    close();
                }}>
                    {t('close')}
                </Button>
            </Group>
        </Modal>
        <Button onClick={open}>
            {t('export')}
        </Button>
    </>;
}

function ImportModal({setPages}: { setPages: (p: number) => void }) {
    const {t} = useTranslation();
    const {actions} = useEditor();
    const [opened, {open, close}] = useDisclosure(false);

    const [value, setValue] = useState('');

    return <>
        <Modal opened={opened} onClose={close} title={t('todo')} maw={300}>
            <Textarea
                value={value}
                onChange={e => setValue(e.target.value)}
                rows={10}
            />
            <Group justify="flex-end" pt="md">
                <Button onClick={() => {
                    close();
                }}>
                    {t('cancel')}
                </Button>
                <Button type="submit" onClick={() => {
                    close();
                    const json = atob(value);
                    actions.deserialize(json);
                    setPages(countPagesOfImport(JSON.parse(json)));
                }}>
                    {t('import')}
                </Button>
            </Group>
        </Modal>
        <Button onClick={open}>
            {t('import')}
        </Button>
    </>;
}

function countPagesOfImport(nodes: any) {
    let count = 0;

    for (const nodeId in nodes) {
        const node = nodes[nodeId];

        if (node?.type?.resolvedName === 'CharacterSheetPaper') {
            count++;
        }
    }

    return count;
}