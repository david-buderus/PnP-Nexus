import {fetchAllNations, fetchAllSpecies} from '../../../components/Database';
import {useMemo} from 'react';
import {Anchor, Button, Center, Group, List, Paper, Stack, Text, Title} from '@mantine/core';
import {useTranslation} from 'react-i18next';
import {useUniverseContext, useUserContext} from '../../../components/PageBase';
import {Link, useNavigate} from 'react-router-dom';
import {SpeciesForm} from '../../../components/character/SpeciesForm';
import {BooleanParam, useQueryParam, withDefault} from 'use-query-params';
import {Nation, Species} from '../../../api';

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
                        <Button
                            variant="outline"
                            data-testid="add"
                            onClick={() => setEditMode(true)}
                        >
                            {t('add')}
                        </Button> : null
                    }
                </Group>
                <Text>
                    {t('species:overviewDescription')}
                </Text>
                {playableSpecies.length > 0 ?
                    <SpeciesList
                        title={t('species:playableSpecies')}
                        species={playableSpecies}
                    /> : null
                }
                {nonplayableSpecies.length > 0 ?
                    <SpeciesList
                        title={t('species:nonplayableSpecies')}
                        species={nonplayableSpecies}
                    /> : null
                }
                {unboundNations.length > 0 ?
                    <>
                        <Title order={4}>
                            {t('species:unboundNations')}
                        </Title>
                        <NationList
                            nations={unboundNations}
                        />
                    </> : null
                }
            </Stack>
        </Paper>
    </Center>;
}

function SpeciesList({
    title,
    species,
}: {
    title: string;
    species: Species[];
}) {
    const {activeUniverse} = useUniverseContext();

    return <>
        <Title order={4}>
            {title}
        </Title>
        <List>
            {species.map(s =>
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
                        <NationList
                            species={s}
                            nations={s.nations}
                        />
                        : null}
                </List.Item>
            )}
        </List>
    </>;
}

function NationList({
    species,
    nations
}: {
    species?: Species;
    nations: Nation[];
}) {
    const {activeUniverse} = useUniverseContext();

    return <List>
        {nations.map(n =>
            <List.Item key={n.id}>
                <Anchor
                    component={Link}
                    to={{
                        pathname: `/nations/${n.id}`,
                        search: `universe=${activeUniverse.name}` + (species ? `&species=${species.id}` : '')
                    }}
                    data-testid={(species ? species.id + '/' : '') + n.id}
                >
                    {n.name}
                </Anchor>
            </List.Item>)
        }
    </List>;
}