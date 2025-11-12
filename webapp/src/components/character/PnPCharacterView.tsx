import {PnPCharacterDto, PnPCharacterSheet} from '../../api';
import {PnPCharacterContext} from './PnPCharacterContext';
import {PnPCharacterSheetContext} from './PnPCharacterSheetContext';
import React, {useEffect, useState} from 'react';
import {ActionIcon, Group, Stack} from '@mantine/core';
import {Editor, Element, Frame, useEditor} from '@craftjs/core';
import {StackPart} from './editor/parts/layout/StackPart';
import {CharacterSheetPaper} from './editor/parts/CharacterSheetPaper';
import {FaChevronLeft} from 'react-icons/fa';
import {FaChevronRight} from 'react-icons/fa6';
import {countPagesOfImport, RESOLVER} from './editor/PnPCharacterSheetEditor';

/** Shows the character with the help of the given sheet */
export function PnPCharacterView({
    character, sheet
}: {
    character: PnPCharacterDto;
    sheet: PnPCharacterSheet
}) {
    const [selectedPage, setSelectedPage] = useState(0);
    const [pages, setPages] = useState(1);

    if (!character) {
        return <></>;
    }

    return <PnPCharacterContext.Provider value={{character}}>
        <PnPCharacterSheetContext.Provider value={{selectedPage}}>
            <Editor resolver={RESOLVER} enabled={false}>
                <Stack>
                    <Stack id="print-section">
                        <Frame>
                            <Element is={StackPart}>
                                <Element is={CharacterSheetPaper} pageNumber={0} id="page-0" canvas/>
                            </Element>
                        </Frame>
                    </Stack>
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
                                disabled={selectedPage === pages - 1}
                                onClick={() => setSelectedPage(prev => prev + 1)}
                            >
                                <FaChevronRight/>
                            </ActionIcon>
                        </Group>
                    </Group>
                    <Controls sheet={sheet} setPages={setPages}/>
                </Stack>
            </Editor>
        </PnPCharacterSheetContext.Provider>
    </PnPCharacterContext.Provider>;
}

function Controls({sheet, setPages}: {
    sheet?: PnPCharacterSheet;
    setPages: (p: number) => void;
}) {
    const {actions} = useEditor();

    useEffect(() => {
        if (!sheet || !sheet.sheet) {
            return;
        }
        const json = atob(sheet.sheet);
        actions.deserialize(json);
        setPages(countPagesOfImport(JSON.parse(json)));
    }, [sheet]);

    return <></>;
}