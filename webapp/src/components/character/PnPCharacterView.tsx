import {PnPCharacterDTO, PnPCharacterSheet} from '../../api/model';
import {PnPCharacterContext} from './PnPCharacterContext';
import {PnPCharacterSheetContext} from './PnPCharacterSheetContext';
import React, {useEffect, useState} from 'react';
import {ActionIcon, AspectRatio, Box, Group, Paper, Skeleton, Stack} from '@mantine/core';
import {CharacterSheetPaper, PnPCharacterSheetPage} from './editor/parts/CharacterSheetPaper';
import {FaChevronLeft} from 'react-icons/fa';
import {FaChevronRight} from 'react-icons/fa6';
import {UseFormReturnType} from '@mantine/form';
import {useListState, UseListStateHandlers} from '@mantine/hooks';

/** Shows the character with the help of the given sheet */
export function PnPCharacterView({
    characterForm, allowEdit, isLoading, sheet
}: {
    characterForm: UseFormReturnType<PnPCharacterDTO>;
    allowEdit: boolean;
    isLoading?: boolean;
    sheet: PnPCharacterSheet;
}) {
    const [selectedPage, setSelectedPage] = useState(0);
    const [pages, setPages] = useListState<PnPCharacterSheetPage>([{data: [], layout: []}]);

    if (!sheet) {
        return <AspectRatio
            ratio={1 / 1.4142}
            w="800px"
        >
            <Paper
                shadow="sm"
                p="md"
                withBorder
                style={{overflow: 'hidden'}}
            />
        </AspectRatio>;
    }

    if (isLoading) {
        return <AspectRatio
            ratio={1 / 1.4142}
            w="800px"
        >
            <Paper
                shadow="sm"
                p="md"
                withBorder
                style={{overflow: 'hidden'}}
            >
                <Stack>
                    <Skeleton height={8} radius="xl"/>
                    <Skeleton height={8} mt={6} radius="xl"/>
                    <Skeleton height={8} mt={6} radius="xl"/>
                    <Skeleton height={8} mt={6} radius="xl"/>
                    <Skeleton height={8} mt={6} radius="xl"/>
                    <Skeleton height={8} mt={6} width="70%" radius="xl"/>
                </Stack>
            </Paper>
        </AspectRatio>;
    }

    return <PnPCharacterContext.Provider value={{characterForm, allowEdit}}>
        <PnPCharacterSheetContext.Provider value={{allowEdit: false}}>
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
                                    updatePage={() => {
                                        // Empty
                                    }}
                                    pageNumber={i}
                                />
                            </Box>
                        );
                    })}
                </Box>
                <Group justify="space-around">
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
                </Group>
                <Controls sheet={sheet} setPages={setPages}/>
            </Stack>
        </PnPCharacterSheetContext.Provider>
    </PnPCharacterContext.Provider>;
}

function Controls({sheet, setPages}: {
    sheet?: PnPCharacterSheet;
    setPages: UseListStateHandlers<PnPCharacterSheetPage>;
}) {
    useEffect(() => {
        if (!sheet || !sheet.sheet) {
            return;
        }
        setPages.setState(loadCharacterSheet(sheet.sheet));
    }, [sheet]);

    return <></>;
}

/** Loads a character sheet from a stored string */
export function loadCharacterSheet(sheet: string): PnPCharacterSheetPage[] {
    const json = atob(sheet);
    const pages = JSON.parse(json);
    if (!isCharacterSheet(pages)) {
        console.error('The given character sheet is malformed.');
        return [{
            data: [],
            layout: []
        }];
    }
    return pages;
}

function isCharacterSheet(sheet: any): sheet is PnPCharacterSheetPage[] {
    return Array.isArray(sheet) &&
        sheet.every(page => Array.isArray(page.data) && Array.isArray(page.layout));
}