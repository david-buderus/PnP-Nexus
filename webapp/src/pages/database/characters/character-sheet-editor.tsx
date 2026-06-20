import {Link, useParams} from 'react-router-dom';
import React, {useEffect, useState} from 'react';
import {PnPCharacterDTO, PnPCharacterSheet} from '../../../api/model';
import {useUniverseContext, useUserContext} from '../../../components/PageBase';
import {PnPCharacterSheetEditor} from '../../../components/character/editor/PnPCharacterSheetEditor';
import {PnPCharacterView} from '../../../components/character/PnPCharacterView';
import {Anchor, Breadcrumbs, Button, Center, Group, Stack, Title} from '@mantine/core';
import {useTranslation} from 'react-i18next';
import {useForm} from '@mantine/form';
import {useEmptyCharacter} from '../../../components/character/PnPCharacterContext';
import {
    useGetExampleCharacter,
    useGetPnPCharacterSheet
} from '../../../api/pn-p-character-sheet-service/pn-p-character-sheet-service';

/** Page to show the character sheet editor */
export function CharacterSheetEditor() {
    const {sheet} = useParams();
    const {activeUniverse} = useUniverseContext();

    const [editMode, setEditMode] = useState<boolean>(false);
    const initialSheet = useGetPnPCharacterSheet(activeUniverse?.id, sheet, {
        query: {enabled: Boolean(activeUniverse?.id) && Boolean(sheet)}
    }).data?.data ?? null;

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
    const emptyCharacter = useEmptyCharacter();

    const form = useForm<PnPCharacterDTO>({
        initialValues: emptyCharacter
    });
    const {data: example} = useGetExampleCharacter(activeUniverse?.id, {
        query: {enabled: Boolean(activeUniverse?.id)},
    });

    useEffect(() => {
        if (!example) {
            return;
        }
        form.setValues(example.data);
    }, [example]);

    return <Center>
        <Stack>
            <Breadcrumbs>
                <Anchor
                    component={Link}
                    to={{
                        pathname: `/characters-editor`,
                        search: `universe=${activeUniverse.id}`
                    }}
                >
                    {t('overview')}
                </Anchor>
                <Anchor>
                    {sheet?.name ?? ''}
                </Anchor>
            </Breadcrumbs>
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