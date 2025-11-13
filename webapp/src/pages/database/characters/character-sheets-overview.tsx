import {useTranslation} from 'react-i18next';
import {fetchAllCharacterSheets} from '../../../components/Database';
import {Anchor, Button, Center, Group, List, Paper, Stack, Text, Title} from '@mantine/core';
import {Link} from 'react-router-dom';
import {useUniverseContext, useUserContext} from '../../../components/PageBase';
import {useState} from 'react';
import {PnPCharacterSheetEditor} from '../../../components/character/editor/PnPCharacterSheetEditor';

/** An overview over all characters */
export function CharacterSheetsOverview() {
    const {t} = useTranslation();
    const {activeUniverse} = useUniverseContext();
    const {userPermissions} = useUserContext();
    const [sheets] = fetchAllCharacterSheets();

    const [editMode, setEditMode] = useState<boolean>(false);

    if (editMode) {
        return <PnPCharacterSheetEditor onCancel={() => setEditMode(false)}/>;
    }

    return <Center>
        <Stack gap="sm">
            <Paper shadow="sm" p="md">
                <Stack miw={900}>
                    <Group justify="space-between">
                        <Title>
                            {t('sheetEditor:character-sheets')}
                        </Title>
                        {userPermissions?.canWriteActiveUniverse ?
                            <Button
                                data-testid="add"
                                onClick={() => setEditMode(true)}
                                variant="outline"
                            >
                                {t('add')}
                            </Button> : null
                        }
                    </Group>
                    {sheets.length > 0 ?
                        <List>
                            {sheets.map(s =>
                                <List.Item key={s.id}>
                                    <Anchor
                                        component={Link}
                                        to={{
                                            pathname: `/characters-editor/${s.id}`,
                                            search: `universe=${activeUniverse.name}`
                                        }}
                                        data-testid={s.id}
                                    >
                                        {s.name}
                                    </Anchor>
                                </List.Item>
                            )}
                        </List> :
                        <Text>
                            {t('nothing-here')}
                        </Text>
                    }
                </Stack>
            </Paper>
        </Stack>
    </Center>;
}