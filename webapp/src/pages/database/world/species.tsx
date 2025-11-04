import {fetchAllNations} from '../../../components/Database';
import {Nation, Species, SpeciesServiceApi} from '../../../api';
import {API_CONFIGURATION} from '../../../components/Constants';
import {useEffect, useMemo, useState} from 'react';
import {
    Anchor,
    Breadcrumbs,
    Button,
    Center,
    Divider,
    Group,
    List,
    Modal,
    Paper,
    Stack,
    Text,
    Title
} from '@mantine/core';
import {useTranslation} from 'react-i18next';
import StarterKit from '@tiptap/starter-kit';
import {EditorContent, useEditor} from '@tiptap/react';
import {Link as TipTapLink} from '@mantine/tiptap';
import Underline from '@tiptap/extension-underline';
import TextAlign from '@tiptap/extension-text-align';
import {useUniverseContext, useUserContext} from '../../../components/PageBase';
import {DropdownButton} from '../../../components/button/DropdownButton';
import {useDisclosure} from '@mantine/hooks';
import {ObjectMultiSelect, ObjectSelect} from '../../../components/input/ObjectSelect';
import {Link, useNavigate, useParams} from 'react-router-dom';
import {BooleanParam, useQueryParam, withDefault} from 'use-query-params';
import {SpeciesForm} from '../../../components/character/SpeciesForm';

const SPECIES_API = new SpeciesServiceApi(API_CONFIGURATION);

/** Detail view for a single species */
export function SpeciesDetail() {
    const {species} = useParams();
    const {t} = useTranslation();
    const navigate = useNavigate();
    const {activeUniverse} = useUniverseContext();
    const {userPermissions} = useUserContext();
    const [editMode, setEditMode] = useQueryParam('edit', withDefault(BooleanParam, false));

    const [selected, setSelected] = useState<Species>(null);

    const editor = useEditor({
        extensions: [
            StarterKit,
            TipTapLink,
            Underline,
            TextAlign,
        ],
        content: selected?.description ?? '',
        editable: false
    });

    useEffect(() => {
        SPECIES_API.getSpecies(activeUniverse.name, species).then(response => {
            setSelected(response.data);
            editor.commands.setContent(response.data.description);
        });
    }, []);

    function refresh() {
        SPECIES_API.getSpecies(activeUniverse.name, species).then(response => {
            setSelected(response.data);
            editor.commands.setContent(response.data.description);
        });
    }

    const [openedAddition, {open: openAddition, close: closeAddition}] = useDisclosure(false);
    const [openedDeletion, {open: openDeletion, close: closeDeletion}] = useDisclosure(false);

    if (editMode) {
        return <SpeciesForm
            initial={selected}
            onSave={() => {
                setEditMode(false);
                refresh();
            }}
            onDelete={() => {
                navigate('/species?universe=' + activeUniverse.name);
            }}
            onCancel={() => setEditMode(false)}
        />;
    }

    if (!selected) {
        return <></>;
    }

    return <Center>
        <Stack gap="sm">
            <Breadcrumbs>
                <Anchor
                    component={Link}
                    to={{
                        pathname: `/species`,
                        search: `universe=${activeUniverse.name}`
                    }}
                >
                    {t('overview')}
                </Anchor>
                <Anchor>
                    {selected.name}
                </Anchor>
            </Breadcrumbs>
            <Paper shadow="sm" p="md">
                <Stack miw={900}>
                    <Group justify="space-between">
                        <Title data-testid="name">
                            {selected.name}
                        </Title>
                        {userPermissions?.canWriteActiveUniverse ?
                            <DropdownButton
                                label={t('edit')}
                                onClick={() => setEditMode(true)}
                                variant="outline"
                                dropdownItems={[
                                    {
                                        label: t('species:addNation'),
                                        link: {
                                            pathname: `/nations`,
                                            search: `universe=${activeUniverse.name}&species=${species}&edit=1`
                                        }
                                    },
                                    {
                                        label: t('species:addExistingNation'),
                                        onClick: openAddition
                                    },
                                    {
                                        label: t('species:removeNation'),
                                        onClick: openDeletion
                                    }
                                ]}
                            /> : null
                        }
                    </Group>
                    <EditorContent editor={editor}/>
                    <Text>
                        {selected.playable ?
                            t('species:playableDescription') :
                            t('species:nonplayableDescription')}
                    </Text>
                    <Divider/>
                    {selected.nations.length > 0 ?
                        <>
                            <Title order={2}>
                                {t('nations')}
                            </Title>
                            <List>
                                {selected.nations.map(n =>
                                    <List.Item key={n.id}>
                                        <Anchor
                                            component={Link}
                                            to={{
                                                pathname: `/nations/${n.id}`,
                                                search: `universe=${activeUniverse.name}&species=${species}`
                                            }}
                                            data-testid={n.id}
                                        >
                                            {n.name}
                                        </Anchor>
                                    </List.Item>
                                )}
                            </List>
                            <Divider/>
                        </> : null
                    }
                    <Group wrap="nowrap" justify="space-between" align="flex-start">
                        <Stack pl="xl">
                            <Title order={3}>
                                {t('advantages')}
                            </Title>
                            {selected.advantageTraits.length > 0 ?
                                <List>
                                    {selected.advantageTraits.map((trait, index) =>
                                        <List.Item key={index}>{trait.description}</List.Item>
                                    )}
                                </List>
                                : t('nothing-here')
                            }
                        </Stack>
                        <Stack pr="xl">
                            <Title order={3}>
                                {t('disadvantages')}
                            </Title>
                            {selected.disadvantageTraits.length > 0 ?
                                <List>
                                    {selected.disadvantageTraits.map((trait, index) =>
                                        <List.Item key={index}>{trait.description}</List.Item>
                                    )}
                                </List>
                                : t('nothing-here')
                            }
                        </Stack>
                    </Group>
                </Stack>
            </Paper>
        </Stack>
        <AddNationDialog species={selected} opened={openedAddition} close={() => {
            refresh();
            closeAddition();
        }}/>
        <DeletionNationDialog species={selected} opened={openedDeletion} close={() => {
            refresh();
            closeDeletion();
        }}/>
    </Center>;
}

function AddNationDialog({
    species,
    opened,
    close
}: {
    species: Species;
    opened: boolean;
    close: () => void;
}) {
    const {t} = useTranslation();
    const {activeUniverse} = useUniverseContext();
    const [nations] = fetchAllNations();
    const [nation, setNation] = useState<Nation>(null);

    const selectableNation = useMemo(
        () => nations.filter(n => !species.nations.some(sn => sn.id === n.id)),
        [nations, species]
    );

    return <Modal opened={opened} onClose={close} title={t('species:addExistingNation')} maw={300}>
        <ObjectSelect<Nation>
            data={selectableNation}
            idKey="id"
            labelKey="name"
            value={nation}
            onChange={setNation}
        />
        <Group justify="flex-end" pt="md">
            <Button autoFocus variant="outline" onClick={close}>
                {t('cancel')}
            </Button>
            <Button
                onClick={() => {
                    SPECIES_API.updateSpecies(activeUniverse.name, species.id, {
                        ...species,
                        nations: species.nations.concat([nation]),
                    }).then(close);
                }}
                disabled={nation === null}
            >
                {t('confirm')}
            </Button>
        </Group>
    </Modal>;
}

function DeletionNationDialog({
    species,
    opened,
    close
}: {
    species: Species;
    opened: boolean;
    close: () => void;
}) {
    const {t} = useTranslation();
    const {activeUniverse} = useUniverseContext();
    const [nations, setNations] = useState<Nation[]>([]);

    return <Modal opened={opened} onClose={close} title={t('species:removeNation')} maw={300}>
        <ObjectMultiSelect<Nation>
            data={species.nations}
            idKey="id"
            labelKey="name"
            value={nations}
            onChange={setNations}
        />
        <Group justify="flex-end" pt="md">
            <Button autoFocus variant="outline" onClick={close}>
                {t('cancel')}
            </Button>
            <Button
                onClick={() => {
                    SPECIES_API.updateSpecies(activeUniverse.name, species.id, {
                        ...species,
                        nations: species.nations.filter(n => !nations.some(sn => sn.id === n.id)),
                    }).then(close).then(() => setNations([]));
                }}
                disabled={nations.length === 0}
            >
                {t('confirm')}
            </Button>
        </Group>
    </Modal>;
}