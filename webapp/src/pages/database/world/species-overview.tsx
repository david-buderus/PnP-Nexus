import {fetchAllNations, fetchAllSpecies} from '../../../components/Database';
import {useMemo} from 'react';
import {Anchor, Button, Center, Group, List, Paper, Stack, Text, Title} from '@mantine/core';
import {useTranslation} from 'react-i18next';
import {useUniverseContext, useUserContext} from '../../../components/PageBase';
import {Link, useNavigate} from 'react-router-dom';
import {SpeciesForm} from '../../../components/character/SpeciesForm';
import {BooleanParam, useQueryParam, withDefault} from 'use-query-params';

/** Overview over all species */
export function SpeciesOverview() {
    const {t} = useTranslation();
    const navigate = useNavigate();
    const {userPermissions} = useUserContext();
    const {activeUniverse} = useUniverseContext();
    const [editMode, setEditMode] = useQueryParam('edit', withDefault(BooleanParam, false));

    const [species, refresh] = fetchAllSpecies();
    const [nations] = fetchAllNations();

    const playableSpecies = useMemo(() => species.filter(s => s.playable), [species]);
    const nonplayableSpecies = useMemo(() => species.filter(s => !s.playable), [species]);
    const unboundNations = useMemo(() =>
            nations.filter(n => !species.some(s => s.nations.some(sn => sn.id === n.id))),
        [species, nations]
    );

    if (editMode) {
        return <SpeciesForm
            initial={{
                name: '',
                description: '',
                nations: [],
                advantageTraits: [],
                disadvantageTraits: []
            }}
            onSave={s => {
                setEditMode(false);
                navigate(`/species/${s.id}?universe=` + activeUniverse.name);
            }}
            onDelete={() => {
                setEditMode(false);
                refresh();
            }}
            onCancel={() => setEditMode(false)}
        />;
    }

    return <Center>
        <Paper shadow="sm" p="md">
            <Stack miw={800}>
                <Group justify="space-between">
                    <Title>
                        {t('species')}
                    </Title>
                    {userPermissions?.canWriteActiveUniverse ?
                        <Button variant="outline" onClick={() => setEditMode(true)}>
                            {t('add')}
                        </Button> : null
                    }
                </Group>
                <Text>
                    {t('species:overviewDescription')}
                </Text>
                {playableSpecies.length > 0 ?
                    <>
                        <Title order={4}>
                            {t('species:playableSpecies')}
                        </Title>
                        <List>
                            {playableSpecies.map(s =>
                                <List.Item key={s.id}>
                                    <Anchor
                                        component={Link}
                                        to={{
                                            pathname: `/species/${s.id}`,
                                            search: `universe=${activeUniverse.name}`
                                        }}
                                        data-testid={s.id}
                                    >
                                        {s.name}
                                    </Anchor>
                                    {s.nations.length > 0 ?
                                        <List>
                                            {s.nations.map(n =>
                                                <List.Item key={n.id}>
                                                    <Anchor
                                                        component={Link}
                                                        to={{
                                                            pathname: `/nations/${n.id}`,
                                                            search: `universe=${activeUniverse.name}&species=${s.id}`
                                                        }}
                                                        data-testid={s.id + '/' + n.id}
                                                    >
                                                        {n.name}
                                                    </Anchor>
                                                </List.Item>)
                                            }
                                        </List>
                                        : null}
                                </List.Item>
                            )}
                        </List>
                    </> : null
                }
                {nonplayableSpecies.length > 0 ?
                    <>
                        <Title order={4}>
                            {t('species:nonplayableSpecies')}
                        </Title>
                        <List>
                            {nonplayableSpecies.map(s =>
                                <List.Item key={s.id}>
                                    <Anchor
                                        component={Link}
                                        to={{
                                            pathname: `/species/${s.id}`,
                                            search: `universe=${activeUniverse.name}`
                                        }}
                                        data-testid={s.id}
                                    >
                                        {s.name}
                                    </Anchor>
                                    {s.nations.length > 0 ?
                                        <List>
                                            {s.nations.map(n =>
                                                <List.Item key={n.id}>
                                                    <Anchor
                                                        component={Link}
                                                        to={{
                                                            pathname: `/nations/${n.id}`,
                                                            search: `universe=${activeUniverse.name}&species=${species}`
                                                        }}
                                                        data-testid={s.id + '/' + n.id}
                                                    >
                                                        {n.name}
                                                    </Anchor>
                                                </List.Item>)
                                            }
                                        </List>
                                        : null}
                                </List.Item>
                            )}
                        </List>
                    </> : null
                }
                {unboundNations.length > 0 ?
                    <>
                        <Title order={4}>
                            {t('species:unboundNations')}
                        </Title>
                        <List>
                            {unboundNations.map(n =>
                                <List.Item key={n.id}>
                                    <Anchor
                                        component={Link}
                                        to={{
                                            pathname: `/nations/${n.id}`,
                                            search: `universe=${activeUniverse.name}`
                                        }}
                                        data-testid={n.id}
                                    >
                                        {n.name}
                                    </Anchor>
                                </List.Item>
                            )}
                        </List>
                    </> : null
                }
            </Stack>
        </Paper>
    </Center>;
}