import {useTranslation} from 'react-i18next';
import {useUniverseContext} from '../../PageBase';
import {PnPCharacterDTO, PnPCharacterSheet, PnPCharacterSheetServiceApi} from '../../../api';
import {API_CONFIGURATION} from '../../Constants';
import React, {useEffect, useMemo, useState} from 'react';
import {
    ActionIcon,
    Box,
    Button,
    Center,
    Divider,
    Flex,
    Group,
    Modal,
    Stack,
    Textarea,
    TextInput,
    Title
} from '@mantine/core';
import {PnPCharacterContext} from '../PnPCharacterContext';
import {CharacterSheetPaper, PnPCharacterSheetPage} from './parts/CharacterSheetPaper';
import {FaChevronRight} from 'react-icons/fa6';
import {FaChevronLeft} from 'react-icons/fa';
import {useDisclosure, useListState, UseListStateHandlers} from '@mantine/hooks';
import {useForm} from '@mantine/form';
import {handleDatabaseInsertErrors, handleValidationErrors} from '../../utils/ErrorUtils';
import {DropdownButton} from '../../button/DropdownButton';
import {PnPCharacterSheetContext} from '../PnPCharacterSheetContext';
import ConfirmationDialog from '../../modal/ConfirmationDialog';
import {EMPTY_CHARACTERS} from '../../../pages/database/characters/characters-overview';

import '/node_modules/react-grid-layout/css/styles.css';
import '/node_modules/react-resizable/css/styles.css';
import {Toolbox} from './Toolbox';
import {PageElementLayout} from './parts/PageElement';


const SHEET_API = new PnPCharacterSheetServiceApi(API_CONFIGURATION);

/** Editor to create character sheets */
export function PnPCharacterSheetEditor({
    initialSheet, onCancel
}: {
    initialSheet?: PnPCharacterSheet;
    onCancel: () => void;
}) {
    const {activeUniverse} = useUniverseContext();

    const form = useForm<PnPCharacterDTO>({
        initialValues: EMPTY_CHARACTERS
    });
    const [isLoading, setIsLoading] = useState(false);
    const [selectedPage, setSelectedPage] = useState(0);
    const [pages, setPages] = useListState<PnPCharacterSheetPage>([{data: [], layout: []}]);
    const [activeDropSettings, setActiveDropSettings] = useState<Partial<PageElementLayout>>({
        w: 4,
        h: 2,
        minW: 3,
        minH: 2
    });

    function updatePage(index: number, updatedPage: Partial<PnPCharacterSheetPage>) {
        setPages.apply((item, i) =>
            i === index ? {...item, ...updatedPage} : item
        );
    }

    useEffect(() => {
        if (!activeUniverse) {
            return;
        }
        setIsLoading(true);
        SHEET_API.getExampleCharacter(activeUniverse.id).then(response => {
            setIsLoading(false);
            form.setValues(response.data);
        });
    }, [activeUniverse]);

    if (isLoading) {
        return <></>;
    }

    return <Center>
        <Group wrap="nowrap" align="flex-start">
            <PnPCharacterSheetContext.Provider value={{allowEdit: true}}>
                <PnPCharacterContext.Provider value={{characterForm: form, allowEdit: false}}>
                    <Stack>
                        {initialSheet ?
                            <Title data-testid="name">
                                {initialSheet?.name ?? ''}
                            </Title>
                            : null}
                        <Flex align="flex-start" wrap="nowrap" gap="md">
                            <Stack>
                                <Box
                                    id="print-section"
                                    style={{
                                        position: 'relative',
                                        width: '100%',
                                        minHeight: '1150px' // Height of A4 to prevent layout collapse
                                    }}
                                >
                                    {pages.map((page, i) => {
                                        const isSelected = i === selectedPage;
                                        // Logic: Only show the current page and 2-3 pages "peeking" behind it
                                        const distance = i - selectedPage;
                                        const isBehind = distance > 0 && distance <= 2;

                                        return (
                                            <Box
                                                key={i}
                                                className="print-page-wrapper"
                                                style={{
                                                    position: isSelected ? 'relative' : 'absolute',
                                                    top: isSelected ? 0 : (distance * 8), // 8px vertical offset
                                                    left: isSelected ? 0 : (distance * 4), // 4px horizontal offset
                                                    zIndex: 90 - i,

                                                    // Visual "Stack" feedback
                                                    opacity: isSelected ? 1 : (isBehind ? 0.7 : 0),
                                                    transition: 'all 0.3s ease-in-out',

                                                    // Interaction
                                                    pointerEvents: isSelected ? 'all' : 'none',
                                                }}
                                            >
                                                <CharacterSheetPaper
                                                    page={page}
                                                    updatePage={p => updatePage(i, p)}
                                                    pageNumber={i}
                                                    activeDropSettings={activeDropSettings}
                                                />
                                            </Box>
                                        );
                                    })}
                                </Box>
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
                                            disabled={selectedPage === pages.length - 1}
                                            onClick={() => setSelectedPage(prev => prev + 1)}
                                        >
                                            <FaChevronRight/>
                                        </ActionIcon>
                                    </Group>
                                    <AddPageButton setPages={setPages}/>
                                </Group>
                            </Stack>
                            <Stack w={300}>
                                <Toolbox
                                    setActiveDropSettings={setActiveDropSettings}
                                />
                                <Divider/>
                                <StorageModal
                                    initialSheet={initialSheet}
                                    pages={pages}
                                    setPages={setPages}
                                    onCancel={onCancel}
                                />
                            </Stack>
                        </Flex>
                    </Stack>
                </PnPCharacterContext.Provider>
            </PnPCharacterSheetContext.Provider>
        </Group>
    </Center>;
}


function RemovePageButton({selectedPage, setSelectedPage, pages, setPages}: {
    selectedPage: number,
    setSelectedPage: (value: (((prevState: number) => number) | number)) => void
    pages: PnPCharacterSheetPage[],
    setPages: UseListStateHandlers<PnPCharacterSheetPage>
}) {
    const {t} = useTranslation();

    const handleRemove = () => {
        if (pages.length < 2) {
            return;
        }

        setPages.remove(selectedPage);

        if (selectedPage > 0) {
            setSelectedPage(selectedPage - 1);
        }
    };

    return <Button
        variant="outline"
        color="red"
        disabled={pages.length < 2}
        onClick={handleRemove}
    >
        {t('sheetEditor:removePage')}
    </Button>;
}

function AddPageButton({setPages}: {
    setPages: UseListStateHandlers<PnPCharacterSheetPage>
}) {
    const {t} = useTranslation();

    const handleAdd = () => {
        setPages.append({data: [], layout: []});
    };

    return <Button onClick={handleAdd}>
        {t('sheetEditor:addPage')}
    </Button>;
}

function StorageModal({initialSheet, pages, setPages, onCancel}: {
    initialSheet?: PnPCharacterSheet;
    pages: PnPCharacterSheetPage[];
    setPages: UseListStateHandlers<PnPCharacterSheetPage>;
    onCancel: () => void;
}) {
    const {t} = useTranslation();
    const {activeUniverse} = useUniverseContext();

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
        setPages.setState(JSON.parse(json));
    }, [initialSheet]);

    return <Stack>
        <Modal opened={openedSave} onClose={closeSave} maw={300} title={t('saveAs')}>
            <form
                data-testid="species-form"
                onSubmit={form.onSubmit(sheet => {
                    sheet.sheet = btoa(JSON.stringify(pages));
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
                        if (btoa(JSON.stringify(pages)) !== latestSave) {
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
        <ImportModal pages={pages} setPages={setPages} opened={openedImport} close={closeImport}/>
        <ExportModal pages={pages} opened={openedExport} close={closeExport}/>
    </Stack>;
}

function ExportModal({
    opened, close, pages
}: {
    opened: boolean;
    close: () => void;
    pages: PnPCharacterSheetPage[];
}) {
    const {t} = useTranslation();

    const result = useMemo(() => btoa(JSON.stringify(pages)), [pages, opened]);

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
    pages: PnPCharacterSheetPage[];
    setPages: UseListStateHandlers<PnPCharacterSheetPage>;
    opened: boolean;
    close: () => void;
}) {
    const {t} = useTranslation();
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
                setPages.setState(JSON.parse(json));
            }}>
                {t('import')}
            </Button>
        </Group>
    </Modal>;
}