import {fetchAllNations, fetchAllSpecies} from "../../../components/Database";
import {Nation, Species, SpeciesServiceApi} from "../../../api";
import {API_CONFIGURATION} from "../../../components/Constants";
import {useSearchParams} from "react-router-dom";
import {useEffect, useMemo, useState} from "react";
import {
    Anchor,
    Breadcrumbs,
    Button,
    Center,
    Divider,
    Group,
    Input,
    List,
    Modal,
    Paper,
    Stack,
    Switch,
    Text,
    TextInput,
    Title
} from "@mantine/core";
import {useTranslation} from "react-i18next";
import {useForm} from "@mantine/form";
import StarterKit from '@tiptap/starter-kit';
import {BubbleMenu, EditorContent, useEditor} from '@tiptap/react';
import {Link, RichTextEditor} from "@mantine/tiptap";
import Underline from '@tiptap/extension-underline';
import TextAlign from '@tiptap/extension-text-align';
import {useUniverseContext, useUserContext} from "../../../components/PageBase";
import ConfirmationDialog from "../../../components/modal/ConfirmationDialog";
import {CharacterTraitInput} from "../../../components/input/CharacterTraitInput";
import {handleDatabaseInsertErrors, handleValidationErrors} from "../../../components/utils/ErrorUtils";
import {DropdownButton} from "../../../components/button/DropdownButton";
import {NationView} from "./nations";
import {useDisclosure} from "@mantine/hooks";
import {ObjectMultiSelect, ObjectSelect} from "../../../components/input/ObjectSelect";

const SPECIES_API = new SpeciesServiceApi(API_CONFIGURATION);

/** Overview over all species */
export function SpeciesOverview() {
    const {t} = useTranslation();
    const [searchParams, setSearchParams] = useSearchParams();
    const {userPermissions} = useUserContext();

    const [species, refreshSpecies] = fetchAllSpecies();
    const [nations, refreshNations] = fetchAllNations();
    const [selectedSpecies, setSelectedSpecies] = useState<Species>(null);
    const [selectedNation, setSelectedNation] = useState<Nation>(null);
    const [editMode, setEditMode] = useState<boolean>(false);

    const playableSpecies = useMemo(() => species.filter(s => s.playable), [species]);
    const nonplayableSpecies = useMemo(() => species.filter(s => !s.playable), [species]);
    const unboundNations = useMemo(() =>
            nations.filter(n => !species.some(s => s.nations.some(sn => sn.id === n.id))),
        [species, nations]
    );

    function clearSelection() {
        setSelectedSpecies(null);
        setSelectedNation(null);
        searchParams.delete("species");
        searchParams.delete("nation");
        setSearchParams(searchParams);
    }

    function clearNationSelection() {
        setSelectedNation(null);
        searchParams.delete("nation");
        setSearchParams(searchParams);
    }

    useEffect(() => {
        setSelectedSpecies(species.find(s => s.id === searchParams.get("species")));
    }, [species, searchParams]);

    useEffect(() => {
        setSelectedNation(nations.find(s => s.id === searchParams.get("nation")));
    }, [nations, searchParams]);

    if (selectedSpecies) {
        return <SpeciesView
            editMode={editMode}
            setEditMode={setEditMode}
            selectedSpecies={selectedSpecies}
            setSelectedSpecies={setSelectedSpecies}
            refreshSpecies={refreshSpecies}
            selectedNation={selectedNation}
            setSelectedNation={n => {
                if (n) {
                    searchParams.set("nation", n.id);
                    setSearchParams(searchParams);
                } else {
                    searchParams.delete("nation");
                    setSearchParams(searchParams);
                }
                setSelectedNation(n);
            }}
            refreshNations={refreshNations}
            clearSelection={clearSelection}
            clearNationSelection={clearNationSelection}
        />;
    }
    if (selectedNation) {
        return <NationView
            editMode={editMode}
            setEditMode={setEditMode}
            selectedSpecies={selectedSpecies}
            refreshSpecies={refreshSpecies}
            selectedNation={selectedNation}
            setSelectedNation={n => {
                if (n) {
                    searchParams.set("nation", n.id);
                    setSearchParams(searchParams);
                } else {
                    searchParams.delete("nation");
                    setSearchParams(searchParams);
                }
                setSelectedNation(n);
            }}
            refreshNations={refreshNations}
            clearSelection={clearSelection}
            clearNationSelection={clearNationSelection}
        />;
    }

    return <Center>
        <Paper shadow="sm" p="md">
            <Stack miw={800}>
                <Group justify="space-between">
                    <Title>
                        {t("species")}
                    </Title>
                    {userPermissions?.canWriteActiveUniverse ?
                        <Button variant="outline" onClick={() => {
                            setSelectedSpecies({
                                id: undefined,
                                name: "",
                                description: "",
                                playable: true,
                                advantageTraits: [],
                                disadvantageTraits: [],
                                nations: []
                            });
                            setEditMode(true);
                        }}>
                            {t("add")}
                        </Button> : null
                    }
                </Group>
                <Text>
                    {t("species:overviewDescription")}
                </Text>
                {playableSpecies.length > 0 ?
                    <>
                        <Title order={4}>
                            {t("species:playableSpecies")}
                        </Title>
                        <List>
                            {playableSpecies.map(s =>
                                <List.Item key={s.id}>
                                    <Anchor onClick={() => {
                                        setSelectedSpecies(s);
                                        searchParams.set("species", s.id);
                                        setSearchParams(searchParams);
                                    }}>
                                        {s.name}
                                    </Anchor>
                                    {s.nations.length > 0 ?
                                        <List>
                                            {s.nations.map(n =>
                                                <List.Item key={n.id}>
                                                    <Anchor onClick={() => {
                                                        setSelectedSpecies(s);
                                                        setSelectedNation(n);
                                                        searchParams.set("species", s.id);
                                                        searchParams.set("nation", n.id);
                                                        setSearchParams(searchParams);
                                                    }}>
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
                            {t("species:nonplayableSpecies")}
                        </Title>
                        <List>
                            {nonplayableSpecies.map(s =>
                                <List.Item key={s.id}>
                                    <Anchor onClick={() => {
                                        setSelectedSpecies(s);
                                        searchParams.set("species", s.id);
                                        setSearchParams(searchParams);
                                    }}>
                                        {s.name}
                                    </Anchor>
                                    {s.nations.length > 0 ?
                                        <List>
                                            {s.nations.map(n =>
                                                <List.Item key={n.id}>
                                                    <Anchor onClick={() => {
                                                        setSelectedSpecies(s);
                                                        setSelectedNation(n);
                                                        searchParams.set("species", s.id);
                                                        searchParams.set("nation", n.id);
                                                        setSearchParams(searchParams);
                                                    }}>
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
                            {t("species:unboundNations")}
                        </Title>
                        <List>
                            {unboundNations.map(n =>
                                <List.Item key={n.id}>
                                    <Anchor onClick={() => {
                                        setSelectedNation(n);
                                        searchParams.set("nation", n.id);
                                        setSearchParams(searchParams);
                                    }}>
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

function SpeciesView({
    editMode,
    setEditMode,
    selectedSpecies,
    setSelectedSpecies,
    selectedNation,
    refreshSpecies,
    setSelectedNation,
    refreshNations,
    clearSelection,
    clearNationSelection,
}: {
    editMode: boolean,
    setEditMode: (value: boolean) => void,
    selectedSpecies: Species,
    setSelectedSpecies: (value: Species) => void
    refreshSpecies: () => void,
    selectedNation: Nation,
    setSelectedNation: (value: Nation) => void,
    refreshNations: () => void,
    clearSelection: () => void,
    clearNationSelection: () => void,
}) {
    if (selectedNation) {
        return <NationView
            editMode={editMode}
            setEditMode={setEditMode}
            selectedSpecies={selectedSpecies}
            refreshSpecies={refreshSpecies}
            selectedNation={selectedNation}
            setSelectedNation={setSelectedNation}
            refreshNations={refreshNations}
            clearSelection={clearSelection}
            clearNationSelection={clearNationSelection}
        />;
    }

    if (editMode) {
        return <SpeciesEdit
            selected={selectedSpecies}
            onSave={() => {
                setEditMode(false);
                refreshSpecies();
            }}
            onDelete={() => {
                setEditMode(false);
                setSelectedSpecies(null);
                refreshSpecies();
            }}
            onCancel={() => {
                setEditMode(false);
                if (!selectedSpecies?.id) {
                    setSelectedSpecies(null);
                }
            }}
        />;
    }
    return <SpeciesDetail
        selected={selectedSpecies}
        onClear={clearSelection}
        onEdit={() => setEditMode(true)}
        onAddNation={() => {
            setSelectedNation({
                id: undefined,
                name: "",
                description: "",
                advantageTraits: [],
                disadvantageTraits: []
            });
            setEditMode(true);
        }}
        onSelectNation={n => setSelectedNation(n)}
        refresh={refreshSpecies}
    />;
}

function SpeciesDetail({
    selected,
    onClear,
    onEdit,
    onSelectNation,
    onAddNation,
    refresh
}: {
    selected: Species;
    onClear: () => void;
    onEdit: () => void;
    onSelectNation: (n: Nation) => void;
    onAddNation: () => void;
    refresh: () => void;
}) {
    const {t} = useTranslation();
    const {userPermissions} = useUserContext();
    const [openedAddition, {open: openAddition, close: closeAddition}] = useDisclosure(false);
    const [openedDeletion, {open: openDeletion, close: closeDeletion}] = useDisclosure(false);

    const editor = useEditor({
        extensions: [
            StarterKit,
            Link,
            Underline,
            TextAlign,
        ],
        content: selected.description,
        editable: false
    });

    return <Center>
        <Stack gap="sm">
            <Breadcrumbs>
                <Anchor onClick={onClear}>
                    {t("overview")}
                </Anchor>
                <Anchor>
                    {selected.name}
                </Anchor>
            </Breadcrumbs>
            <Paper shadow="sm" p="md">
                <Stack miw={900}>
                    <Group justify="space-between">
                        <Title>
                            {selected.name}
                        </Title>
                        {userPermissions?.canWriteActiveUniverse ?
                            <DropdownButton
                                label={t("edit")}
                                onClick={onEdit}
                                variant="outline"
                                dropdownItems={[
                                    {
                                        label: t("species:addNation"),
                                        onClick: onAddNation
                                    },
                                    {
                                        label: t("species:addExistingNation"),
                                        onClick: openAddition
                                    },
                                    {
                                        label: t("species:removeNation"),
                                        onClick: openDeletion
                                    }
                                ]}
                            /> : null
                        }
                    </Group>
                    <EditorContent editor={editor}/>
                    <Text>
                        {selected.playable ?
                            t("species:playableDescription") :
                            t("species:nonplayableDescription")}
                    </Text>
                    <Divider/>
                    {selected.nations.length > 0 ?
                        <>
                            <Title order={2}>
                                {t("nations")}
                            </Title>
                            <List>
                                {selected.nations.map(n =>
                                    <List.Item key={n.id}>
                                        <Anchor onClick={() => onSelectNation(n)}>
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
                                {t("advantages")}
                            </Title>
                            {selected.advantageTraits.length > 0 ?
                                <List>
                                    {selected.advantageTraits.map((trait, index) =>
                                        <List.Item key={index}>{trait.description}</List.Item>
                                    )}
                                </List>
                                : t("nothing-here")
                            }
                        </Stack>
                        <Stack pr="xl">
                            <Title order={3}>
                                {t("disadvantages")}
                            </Title>
                            {selected.disadvantageTraits.length > 0 ?
                                <List>
                                    {selected.disadvantageTraits.map((trait, index) =>
                                        <List.Item key={index}>{trait.description}</List.Item>
                                    )}
                                </List>
                                : t("nothing-here")
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

function SpeciesEdit({
    selected,
    onSave,
    onDelete,
    onCancel
}: {
    selected: Species;
    onSave: () => void;
    onDelete: () => void;
    onCancel: () => void;
}) {
    const {t} = useTranslation();
    const {activeUniverse} = useUniverseContext();
    const {userPermissions} = useUserContext();

    const form = useForm<Species>({
        mode: 'controlled',
        initialValues: selected
    });

    const editor = useEditor({
        extensions: [
            StarterKit,
            Link,
            Underline,
            TextAlign,
        ],
        content: form.getValues().description,
        onUpdate: ({editor: e}) => {
            form.setFieldValue('description', e.getHTML());
        }
    });

    return <form
        onSubmit={form.onSubmit(species => {
            if (species.id) {
                SPECIES_API.updateSpecies(activeUniverse.name, species.id, species)
                    .then(onSave).catch(handleValidationErrors(form.setErrors));
            } else {
                SPECIES_API.insertAllSpeciess(activeUniverse.name, [species])
                    .then(onSave).catch(handleValidationErrors(handleDatabaseInsertErrors(form.setErrors)));
            }
        })}
    >
        <Center>
            <Stack>
                <Group justify="space-between">
                    <TextInput
                        size="lg"
                        label={t("name")}
                        key={form.key('name')}
                        {...form.getInputProps('name')}
                    />
                    {userPermissions?.canWriteActiveUniverse && selected.id !== undefined ?
                        <ConfirmationDialog
                            title={t("species:deleteSpecies")}
                            onConfirmation={() => {
                                SPECIES_API.deleteSpecies(activeUniverse.name, selected.id).then(onDelete);
                            }}
                            openNode={open =>
                                <Button variant="outline" color="red" onClick={open}>
                                    {t("delete")}
                                </Button>
                            }
                        /> : null
                    }
                </Group>
                <RichTextEditor editor={editor} variant="subtle">
                    <RichTextEditor.Toolbar sticky stickyOffset={60}>
                        <RichTextEditor.ControlsGroup>
                            <RichTextEditor.Bold/>
                            <RichTextEditor.Italic/>
                            <RichTextEditor.Underline/>
                            <RichTextEditor.Strikethrough/>
                            <RichTextEditor.ClearFormatting/>
                        </RichTextEditor.ControlsGroup>

                        <RichTextEditor.ControlsGroup>
                            <RichTextEditor.H1/>
                            <RichTextEditor.H2/>
                            <RichTextEditor.H3/>
                            <RichTextEditor.H4/>
                        </RichTextEditor.ControlsGroup>

                        <RichTextEditor.ControlsGroup>
                            <RichTextEditor.Blockquote/>
                            <RichTextEditor.Hr/>
                            <RichTextEditor.BulletList/>
                            <RichTextEditor.OrderedList/>
                        </RichTextEditor.ControlsGroup>

                        <RichTextEditor.ControlsGroup>
                            <RichTextEditor.Link/>
                            <RichTextEditor.Unlink/>
                        </RichTextEditor.ControlsGroup>

                        <RichTextEditor.ControlsGroup>
                            <RichTextEditor.AlignLeft/>
                            <RichTextEditor.AlignCenter/>
                            <RichTextEditor.AlignJustify/>
                            <RichTextEditor.AlignRight/>
                        </RichTextEditor.ControlsGroup>

                        <RichTextEditor.ControlsGroup>
                            <RichTextEditor.Undo/>
                            <RichTextEditor.Redo/>
                        </RichTextEditor.ControlsGroup>
                    </RichTextEditor.Toolbar>
                    {editor && (
                        <BubbleMenu editor={editor}>
                            <RichTextEditor.ControlsGroup>
                                <RichTextEditor.Bold/>
                                <RichTextEditor.Italic/>
                                <RichTextEditor.Link/>
                            </RichTextEditor.ControlsGroup>
                        </BubbleMenu>
                    )}
                    <RichTextEditor.Content/>
                </RichTextEditor>
                <Input.Error>{form.errors.description}</Input.Error>
                <Switch
                    label={t("species:playable")}
                    key={form.key('playable')}
                    {...form.getInputProps('playable', {type: 'checkbox'})}
                />
                <Group wrap="nowrap" justify="space-between" align="flex-start">
                    <Stack>
                        <Title order={3}>
                            {t("advantages")}
                        </Title>
                        <CharacterTraitInput
                            form={form}
                            path="advantageTraits"
                        />
                    </Stack>
                    <Stack>
                        <Title order={3}>
                            {t("disadvantages")}
                        </Title>
                        <CharacterTraitInput
                            form={form}
                            path="disadvantageTraits"
                        />
                    </Stack>
                </Group>
                <Group justify="flex-end" pt="md">
                    <Button onClick={onCancel} variant="outline">
                        {t("cancel")}
                    </Button>
                    <Button type="submit">
                        {t("save")}
                    </Button>
                </Group>
            </Stack>
        </Center>
    </form>;
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

    return <Modal opened={opened} onClose={close} title={t("species:addExistingNation")} maw={300}>
        <ObjectSelect<Nation>
            data={selectableNation}
            idKey="id"
            labelKey="name"
            value={nation}
            onChange={setNation}
        />
        <Group justify="flex-end" pt="md">
            <Button autoFocus variant="outline" onClick={close}>
                {t("cancel")}
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
                {t("confirm")}
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

    return <Modal opened={opened} onClose={close} title={t("species:removeNation")} maw={300}>
        <ObjectMultiSelect<Nation>
            data={species.nations}
            idKey="id"
            labelKey="name"
            value={nations}
            onChange={setNations}
        />
        <Group justify="flex-end" pt="md">
            <Button autoFocus variant="outline" onClick={close}>
                {t("cancel")}
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
                {t("confirm")}
            </Button>
        </Group>
    </Modal>;
}