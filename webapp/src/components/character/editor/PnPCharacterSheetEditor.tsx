import {useTranslation} from 'react-i18next';
import {useUniverseContext} from '../../PageBase';
import {PnPCharacterDto, PnPCharacterServiceApi, PnPCharacterSheet, PnPCharacterSheetServiceApi} from '../../../api';
import {API_CONFIGURATION} from '../../Constants';
import React, {useEffect, useMemo, useState} from 'react';
import {
    Accordion,
    ActionIcon,
    Badge,
    Button,
    Center,
    Divider,
    Flex,
    Group,
    Modal,
    Stack,
    Text,
    Textarea,
    TextInput,
    Title
} from '@mantine/core';
import {Editor, Element, Frame, Resolver, useEditor} from '@craftjs/core';
import {StackPart} from './parts/layout/StackPart';
import {FreeTextPart} from './parts/other/FreeTextPart';
import {GroupPart} from './parts/layout/GroupPart';
import {GridPart} from './parts/layout/GridPart';
import {PnPCharacterContext} from '../PnPCharacterContext';
import {CharacterInfo} from './parts/character/CharacterInfo';
import {LevelInfo} from './parts/character/LevelInfo';
import {PrimaryAttributeInfo} from './parts/stats/PrimaryAttributeInfo';
import {SecondaryAttributeInfo} from './parts/stats/SecondaryAttributeInfo';
import {WeaponList} from './parts/items/WeaponList';
import {CharacterSheetPaper} from './parts/CharacterSheetPaper';
import {FaChevronRight} from 'react-icons/fa6';
import {FaChevronLeft} from 'react-icons/fa';
import {ArmorSlots} from './parts/items/ArmorSlots';
import {JewelleryList} from './parts/items/JewelleryList';
import {InventoryPart} from './parts/items/InventoryPart';
import {TextFieldPart} from './parts/custom/TextFieldPart';
import {PrimaryAttributeRow} from './parts/stats/PrimaryAttributeRow';
import {useDisclosure} from '@mantine/hooks';
import {TitlePart} from './parts/other/TitlePart';
import {TalentGroup} from './parts/talent/TalentGroup';
import {CharacterDescriptionInfo} from './parts/character/CharacterDesciptionInfo';
import {AdvantagesInfo} from './parts/character/AdvantagesInfo';
import {useForm} from '@mantine/form';
import {handleDatabaseInsertErrors, handleValidationErrors} from '../../utils/ErrorUtils';
import {DropdownButton} from '../../button/DropdownButton';
import {SpellList} from './parts/spells/SpellList';
import {CurrencyPart, SHOW_ALL_CURRENCIES} from './parts/items/CurrencyPart';
import {CustomTablePart, EMPTY_TABLE_DEFINITION} from './parts/custom/CustomTablePart';
import {PnPCharacterSheetContext} from '../PnPCharacterSheetContext';
import ConfirmationDialog from '../../modal/ConfirmationDialog';
import {EMPTY_CHARACTERS} from '../../../pages/database/characters/characters-overview';

const CHARACTER_API = new PnPCharacterServiceApi(API_CONFIGURATION);
const SHEET_API = new PnPCharacterSheetServiceApi(API_CONFIGURATION);

/** All resolver used by the character sheet editor */
export const RESOLVER: Resolver = {
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
    SpellList,
    AdvantagesInfo,
    CurrencyPart,
    CharacterSheetPaper,
    CustomTablePart
};

/** Editor to create character sheets */
export function PnPCharacterSheetEditor({
    initialSheet, onCancel
}: {
    initialSheet?: PnPCharacterSheet;
    onCancel: () => void;
}) {
    const {activeUniverse} = useUniverseContext();

    const form = useForm<PnPCharacterDto>({
        initialValues: EMPTY_CHARACTERS
    });
    const [pages, setPages] = useState(1);
    const [isLoading, setIsLoading] = useState(false);
    const [selectedPage, setSelectedPage] = useState(0);


    useEffect(() => {
        if (!activeUniverse) {
            return;
        }
        setIsLoading(true);
        CHARACTER_API.getExampleCharacter(activeUniverse.id).then(response => {
            setIsLoading(false);
            form.setValues(response.data);
        });
    }, [activeUniverse]);

    if (isLoading) {
        return <></>;
    }

    return <Center>
        <Group wrap="nowrap" align="flex-start">
            <PnPCharacterContext.Provider value={{characterForm: form, allowEdit: false}}>
                <PnPCharacterSheetContext.Provider value={{selectedPage}}>
                    <Editor resolver={RESOLVER}>
                        <Stack>
                            {initialSheet ?
                                <Title data-testid="name">
                                    {initialSheet?.name ?? ''}
                                </Title>
                                : null}
                            <Flex align="flex-start" wrap="nowrap" gap="md">
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
                                    <SettingsPanel/>
                                    <Divider/>
                                    <StorageModal initialSheet={initialSheet} setPages={setPages} onCancel={onCancel}/>
                                </Stack>
                            </Flex>
                        </Stack>
                    </Editor>
                </PnPCharacterSheetContext.Provider>
            </PnPCharacterContext.Provider>
        </Group>
    </Center>;
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
    const {equipmentSettings} = useUniverseContext();

    return <Stack>
        <Title order={3}>
            {t('sheetEditor:dragToAdd')}
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
                            {t('sheetEditor:characterDescription')}
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
                            ref={ref => connectors.create(ref, <PrimaryAttributeInfo/>)}>
                            {t('sheetEditor:primaryAttributeInfo')}
                        </Button>
                        <Button ref={ref => connectors.create(ref, <SecondaryAttributeInfo/>)}>
                            {t('sheetEditor:secondaryAttributeInfo')}
                        </Button>
                        <Button ref={ref => connectors.create(ref, <PrimaryAttributeRow/>)}>
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
                        <Button ref={ref => connectors.create(ref, <CurrencyPart
                            withoutLabel={false}
                            oneLine={false}
                            showCurrency={SHOW_ALL_CURRENCIES}
                        />)}>
                            {t('sheetEditor:currency')}
                        </Button>
                    </Stack>
                </Accordion.Panel>
            </Accordion.Item>
            <Accordion.Item value="talents">
                <Accordion.Control>{t('talents')}</Accordion.Control>
                <Accordion.Panel>
                    <Stack gap="xs">
                        <Button ref={ref => connectors.create(ref, <TalentGroup
                            groupName=""
                        />)}>
                            {t('sheetEditor:talentGroup')}
                        </Button>
                    </Stack>
                </Accordion.Panel>
            </Accordion.Item>
            <Accordion.Item value="spells">
                <Accordion.Control>{t('spells')}</Accordion.Control>
                <Accordion.Panel>
                    <Stack gap="xs">
                        <Button ref={ref => connectors.create(ref, <SpellList numberOfRows={6}/>)}>
                            {t('sheetEditor:spellList')}
                        </Button>
                    </Stack>
                </Accordion.Panel>
            </Accordion.Item>
            <Accordion.Item value="custom">
                <Accordion.Control>{t('sheetEditor:customFields')}</Accordion.Control>
                <Accordion.Panel>
                    <Stack gap="xs">
                        <Button
                            ref={ref => connectors.create(ref, <TextFieldPart customId="" title="" numberOfRows={3}/>)}>
                            {t('sheetEditor:textField')}
                        </Button>
                        <Button
                            ref={ref => connectors.create(ref, <CustomTablePart definition={EMPTY_TABLE_DEFINITION}/>)}>
                            {t('sheetEditor:table')}
                        </Button>
                    </Stack>
                </Accordion.Panel>
            </Accordion.Item>
            <Accordion.Item value="other">
                <Accordion.Control>{t('other')}</Accordion.Control>
                <Accordion.Panel>
                    <Stack gap="xs">
                        <Button ref={ref => connectors.create(ref, <TitlePart text="" order={1}/>)}>
                            {t('sheetEditor:title')}
                        </Button>
                        <Button ref={ref => connectors.create(ref, <FreeTextPart text="" fontSize="md"/>)}>
                            {t('sheetEditor:freeText')}
                        </Button>
                    </Stack>
                </Accordion.Panel>
            </Accordion.Item>
        </Accordion>
    </Stack>;
}

function SettingsPanel() {
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
    </Stack>;
}

function StorageModal({initialSheet, setPages, onCancel}: {
    initialSheet?: PnPCharacterSheet;
    setPages: (p: number) => void;
    onCancel: () => void;
}) {
    const {t} = useTranslation();
    const {activeUniverse} = useUniverseContext();
    const {actions, query} = useEditor();

    const [latestSave, setLatestSave] = useState<string>('');

    const [openedSave, {open: openSave, close: closeSave}] = useDisclosure(false);
    const [openedImport, {open: openImport, close: closeImport}] = useDisclosure(false);
    const [openedExport, {open: openExport, close: closeExport}] = useDisclosure(false);

    const form = useForm<PnPCharacterSheet>({
        mode: 'controlled',
        initialValues: initialSheet ?? {
            name: '',
            sheet: ''
        }
    });

    useEffect(() => {
        if (!initialSheet || !initialSheet.sheet) {
            return;
        }
        setLatestSave(initialSheet.sheet);
        const json = atob(initialSheet.sheet);
        actions.deserialize(json);
        setPages(countPagesOfImport(JSON.parse(json)));
    }, [initialSheet]);

    return <Stack>
        <Modal opened={openedSave} onClose={closeSave} maw={300} title={t('saveAs')}>
            <form
                data-testid="species-form"
                onSubmit={form.onSubmit(sheet => {
                    sheet.sheet = btoa(query.serialize());
                    if (sheet.id) {
                        SHEET_API.updatePnPCharacterSheet(activeUniverse.id, sheet.id, sheet)
                            .then(closeSave)
                            .then(() => setLatestSave(sheet.sheet))
                            .catch(handleValidationErrors(form.setErrors));
                    } else {
                        SHEET_API.insertAllPnPCharacterSheets(activeUniverse.id, [sheet])
                            .then(closeSave)
                            .then(() => setLatestSave(sheet.sheet))
                            .catch(handleValidationErrors(handleDatabaseInsertErrors(form.setErrors)));
                    }
                })}
            >
                <TextInput
                    label={t('name')}
                    key={form.key('name')}
                    {...form.getInputProps('name')}
                />
                <Group justify="flex-end" pt="md">
                    <Button onClick={close} variant="outline">
                        {t('cancel')}
                    </Button>
                    <Button type="submit">
                        {t('save')}
                    </Button>
                </Group>
            </form>
        </Modal>
        <Button variant="outline" onClick={() => {
            actions.selectNode(null);
            window.print();
        }}>
            {t('print')}
        </Button>
        <ConfirmationDialog
            title={t('unsavedChangesTitle')}
            text={t('unsavedChangesDescription')}
            onConfirmation={onCancel}
            openNode={(open) =>
                <Button
                    onClick={() => {
                        if (btoa(query.serialize()) !== latestSave) {
                            open();
                        } else {
                            onCancel();
                        }
                    }}
                    variant="outline"
                >
                    {t('cancel')}
                </Button>}
        />
        <DropdownButton
            label={t('save')}
            onClick={openSave}
            dropdownItems={[
                {
                    label: t('import'),
                    onClick: openImport
                },
                {
                    label: t('export'),
                    onClick: openExport
                }
            ]}
        />
        <ImportModal setPages={setPages} opened={openedImport} close={closeImport}/>
        <ExportModal opened={openedExport} close={closeExport}/>
    </Stack>;
}

function ExportModal({
    opened, close,
}: {
    opened: boolean;
    close: () => void;
}) {
    const {t} = useTranslation();
    const {query} = useEditor();

    const result = useMemo(() => btoa(query.serialize()), [query, opened]);

    return <Modal opened={opened} onClose={close} title={t('todo')} maw={300}>
        <Textarea readOnly value={result} rows={10}/>
        <Group justify="flex-end" pt="md">
            <Button type="submit" onClick={() => {
                close();
            }}>
                {t('close')}
            </Button>
        </Group>
    </Modal>;
}

function ImportModal({setPages, opened, close}: {
    setPages: (p: number) => void;
    opened: boolean;
    close: () => void;
}) {
    const {t} = useTranslation();
    const {actions} = useEditor();

    const [value, setValue] = useState('');

    return <Modal opened={opened} onClose={close} title={t('todo')} maw={300}>
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
    </Modal>;
}

/** Counts the number of pages in a sheet import */
export function countPagesOfImport(nodes: any) {
    let count = 0;

    for (const nodeId in nodes) {
        const node = nodes[nodeId];

        if (node?.type?.resolvedName === 'CharacterSheetPaper') {
            count++;
        }
    }

    return count;
}