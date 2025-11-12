import {useParams} from 'react-router-dom';
import React, {useEffect, useState} from 'react';
import {PnPCharacterDto, PnPCharacterServiceApi, PnPCharacterSheet, PnPCharacterSheetServiceApi} from '../../../api';
import {API_CONFIGURATION} from '../../../components/Constants';
import {useUniverseContext, useUserContext} from '../../../components/PageBase';
import {PnPCharacterSheetEditor} from '../../../components/character/editor/PnPCharacterSheetEditor';
import {PnPCharacterView} from '../../../components/character/PnPCharacterView';
import {Button, Center, Group, Stack, Title} from '@mantine/core';
import {useTranslation} from 'react-i18next';

const CHARACTER_API = new PnPCharacterServiceApi(API_CONFIGURATION);
const SHEET_API = new PnPCharacterSheetServiceApi(API_CONFIGURATION);

/** Page to show the character sheet editor */
export function CharacterSheetEditor() {
    const {sheet} = useParams();
    const {activeUniverse} = useUniverseContext();

    const [initialSheet, setInitialSheet] = useState<PnPCharacterSheet>(null);
    const [editMode, setEditMode] = useState<boolean>(false);

    useEffect(() => {
        if (!sheet) {
            return;
        }
        SHEET_API.getPnPCharacterSheet(activeUniverse.name, sheet).then(response => {
            setInitialSheet(response.data);
        });
    }, [sheet]);

    if (!editMode) {
        return <CharacterSheetView
            sheet={initialSheet}
            onEdit={() => setEditMode(true)}
        />;
    }

    return <Stack>
        <PnPCharacterSheetEditor
            initialSheet={initialSheet}
            onCancel={() => setEditMode(false)}
        />
    </Stack>;
}

function CharacterSheetView({
    sheet, onEdit
}: {
    sheet: PnPCharacterSheet;
    onEdit: () => void;
}) {
    const {t} = useTranslation();
    const {activeUniverse} = useUniverseContext();
    const {userPermissions} = useUserContext();
    const [character, setCharacter] = useState<PnPCharacterDto>(null);


    useEffect(() => {
        if (!activeUniverse) {
            return;
        }
        CHARACTER_API.getExampleCharacter(activeUniverse.name).then(response => setCharacter(response.data));
    }, [activeUniverse]);

    return <Center>
        <Stack>
            <Group justify="space-between">
                <Title data-testid="name">
                    {sheet?.name ?? ''}
                </Title>
                {userPermissions?.canWriteActiveUniverse ?
                    <Button
                        onClick={onEdit}
                        variant="outline"
                        data-testid="edit"
                    >
                        {t('edit')}
                    </Button> : null
                }
            </Group>
            <PnPCharacterView
                character={character}
                sheet={sheet}
            />
        </Stack>
    </Center>;
}