import {Nation, NationServiceApi, Species, SpeciesServiceApi} from "../../../api";
import {useTranslation} from "react-i18next";
import {useUniverseContext, useUserContext} from "../../../components/PageBase";
import {useForm} from "@mantine/form";
import {BubbleMenu, EditorContent, useEditor} from "@tiptap/react";
import StarterKit from "@tiptap/starter-kit";
import {Link, RichTextEditor} from "@mantine/tiptap";
import Underline from "@tiptap/extension-underline";
import TextAlign from "@tiptap/extension-text-align";
import {handleDatabaseInsertErrors, handleValidationErrors} from "../../../components/utils/ErrorUtils";
import {
    Anchor,
    Breadcrumbs,
    Button,
    Center,
    Divider,
    Group,
    Input,
    List,
    Paper,
    Stack,
    TextInput,
    Title
} from "@mantine/core";
import ConfirmationDialog from "../../../components/modal/ConfirmationDialog";
import {CharacterTraitInput} from "../../../components/input/CharacterTraitInput";
import {API_CONFIGURATION} from "../../../components/Constants";

const SPECIES_API = new SpeciesServiceApi(API_CONFIGURATION);
const NATION_API = new NationServiceApi(API_CONFIGURATION);

/** View for nations */
export function NationView({
    editMode,
    setEditMode,
    selectedSpecies,
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
    refreshSpecies: () => void,
    selectedNation: Nation,
    setSelectedNation: (value: Nation) => void,
    refreshNations: () => void,
    clearSelection: () => void,
    clearNationSelection: () => void,
}) {
    if (editMode) {
        return <NationEdit
            selectedSpecies={selectedSpecies}
            selectedNation={selectedNation}
            onSave={() => {
                setEditMode(false);
                refreshNations();
                refreshSpecies();
            }}
            onDelete={() => {
                setEditMode(false);
                setSelectedNation(null);
                refreshNations();
                refreshSpecies();
            }}
            onCancel={() => {
                setEditMode(false);
                if (!selectedNation?.id) {
                    setSelectedNation(null);
                }
            }}
        />;
    }
    return <NationDetail
        selectedSpecies={selectedSpecies}
        selectedNation={selectedNation}
        onClear={clearSelection}
        onClearNation={clearNationSelection}
        onEdit={() => setEditMode(true)}
    />;
}

function NationDetail({
    selectedSpecies,
    selectedNation,
    onClear,
    onClearNation,
    onEdit
}: {
    selectedSpecies: Species;
    selectedNation: Nation;
    onClear: () => void;
    onClearNation: () => void;
    onEdit: () => void;
}) {
    const {t} = useTranslation();
    const {userPermissions} = useUserContext();

    const editor = useEditor({
        extensions: [
            StarterKit,
            Link,
            Underline,
            TextAlign,
        ],
        content: selectedNation.description,
        editable: false
    });

    return <Center>
        <Stack gap="sm">
            <Breadcrumbs>
                <Anchor onClick={onClear}>
                    {t("overview")}
                </Anchor>
                {selectedSpecies ?
                    <Anchor onClick={onClearNation}>
                        {selectedSpecies.name}
                    </Anchor> : null
                }
                <Anchor>
                    {selectedNation.name}
                </Anchor>
            </Breadcrumbs>
            <Paper shadow="sm" p="md">
                <Stack miw={900}>
                    <Group justify="space-between">
                        <Title>
                            {selectedNation.name}
                        </Title>
                        {userPermissions?.canWriteActiveUniverse ?
                            <Button
                                onClick={onEdit}
                                variant="outline"
                            >
                                {t("edit")}
                            </Button> : null
                        }
                    </Group>
                    <EditorContent editor={editor}/>
                    <Divider/>
                    <Group wrap="nowrap" justify="space-between" align="flex-start">
                        <Stack pl="xl">
                            <Title order={3}>
                                {t("advantages")}
                            </Title>
                            {selectedNation.advantageTraits.length > 0 ?
                                <List>
                                    {selectedNation.advantageTraits.map((trait, index) =>
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
                            {selectedNation.disadvantageTraits.length > 0 ?
                                <List>
                                    {selectedNation.disadvantageTraits.map((trait, index) =>
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
    </Center>;
}

function NationEdit({
    selectedSpecies,
    selectedNation,
    onSave,
    onDelete,
    onCancel
}: {
    selectedSpecies: Species;
    selectedNation: Nation;
    onSave: () => void;
    onDelete: () => void;
    onCancel: () => void;
}) {
    const {t} = useTranslation();
    const {activeUniverse} = useUniverseContext();
    const {userPermissions} = useUserContext();

    const form = useForm<Nation>({
        mode: 'controlled',
        initialValues: selectedNation
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
        onSubmit={form.onSubmit(nation => {
            if (nation.id) {
                NATION_API.updateNation(activeUniverse.name, nation.id, nation)
                    .then(onSave).catch(handleValidationErrors(form.setErrors));
            } else {
                NATION_API.insertAllNations(activeUniverse.name, [nation])
                    .then(response => {
                        if (!selectedSpecies) {
                            return;
                        }
                        SPECIES_API.updateSpecies(activeUniverse.name, selectedSpecies.id, {
                            ...selectedSpecies,
                            nations: selectedSpecies.nations.concat(response.data),
                        });
                    })
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
                    {userPermissions?.canWriteActiveUniverse && selectedNation.id !== undefined ?
                        <ConfirmationDialog
                            title={t("species:deleteSpecies")}
                            onConfirmation={() => {
                                NATION_API.deleteNation(activeUniverse.name, selectedNation.id).then(onDelete);
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