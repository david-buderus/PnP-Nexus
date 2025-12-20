import {useParams} from 'react-router-dom';
import React, {useEffect, useState} from 'react';
import {PnPCharacterDTO, PnPCharacterSheet, PnPCharacterSheetServiceApi} from '../../../api';
import {API_CONFIGURATION} from '../../../components/Constants';
import {useUniverseContext, useUserContext} from '../../../components/PageBase';
import {PnPCharacterSheetEditor} from '../../../components/character/editor/PnPCharacterSheetEditor';
import {PnPCharacterView} from '../../../components/character/PnPCharacterView';
import {Button, Center, Group, Stack, Title} from '@mantine/core';
import {useTranslation} from 'react-i18next';
import {useForm} from '@mantine/form';
import {EMPTY_CHARACTERS} from './characters-overview';

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
        SHEET_API.getPnPCharacterSheet(activeUniverse.id, sheet).then(response => {
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
    const form = useForm<PnPCharacterDTO>({
        initialValues: EMPTY_CHARACTERS
    });


    useEffect(() => {
        if (!activeUniverse) {
            return;
        }
        SHEET_API.getExampleCharacter(activeUniverse.id).then(response => form.setValues(response.data));
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
                characterForm={form}
                allowEdit={false}
                sheet={sheet}
            />
        </Stack>
    </Center>;
}