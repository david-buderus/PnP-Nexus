import {Nation, Species} from '../../../api/model';
import {useTranslation} from 'react-i18next';
import {useUniverseContext, useUserContext} from '../../../components/PageBase';
import {useForm} from '@mantine/form';
import {BubbleMenu, EditorContent, useEditor} from '@tiptap/react';
import StarterKit from '@tiptap/starter-kit';
import {Link, RichTextEditor} from '@mantine/tiptap';
import Underline from '@tiptap/extension-underline';
import TextAlign from '@tiptap/extension-text-align';
import {handleDatabaseInsertErrors, handleValidationErrors} from '../../../components/utils/ErrorUtils';
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
} from '@mantine/core';
import ConfirmationDialog from '../../../components/modal/ConfirmationDialog';
import {CharacterTraitInput} from '../../../components/input/CharacterTraitInput';
import {BooleanParam, StringParam, useQueryParam, withDefault} from 'use-query-params';
import {useEffect} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import {
    getGetAllSpeciessQueryKey,
    getGetSpeciesQueryKey,
    useGetSpecies,
    useUpdateSpecies
} from '../../../api/species-service/species-service';
import {
    getGetAllNationsQueryKey,
    getGetNationQueryKey,
    useDeleteNation,
    useGetNation,
    useInsertAllNations,
    useUpdateNation
} from '../../../api/nation-service/nation-service';
import {useQueryClient} from '@tanstack/react-query';

/** View for nations */
export function NationView() {
    const {nation} = useParams();
    const {activeUniverse} = useUniverseContext();
    const [selectedSpeciesQuery] = useQueryParam('species', withDefault(StringParam, null));
    const [editMode, setEditMode] = useQueryParam('edit', withDefault(BooleanParam, false));
    const navigate = useNavigate();

    const species = useGetSpecies(activeUniverse.id, selectedSpeciesQuery, {query: {enabled: Boolean(selectedSpeciesQuery)}}).data?.data ?? null;
    const selected = useGetNation(activeUniverse.id, nation, {query: {enabled: Boolean(nation)}}).data?.data ?? null;

    function clearSelection() {
        navigate(`/species?universe=${activeUniverse.id}`);
    }

    function clearNationSelection() {
        if (species) {
            navigate(`/species/${selectedSpeciesQuery}?universe=${activeUniverse.id}`);
        } else {
            navigate(`/species?universe=${activeUniverse.id}`);
        }
    }

    if (editMode || !nation) {
        return <NationEdit
            selectedSpecies={species}
            initial={selected}
            onSave={n => {
                if (nation) {
                    setEditMode(false);
                    return;
                }
                if (species) {
                    navigate(`/nations/${n.id}?universe=${activeUniverse.id}&species=${selectedSpeciesQuery}`);
                } else {
                    navigate(`/nations/${n.id}?universe=${activeUniverse.id}`);
                }
            }}
            onDelete={clearNationSelection}
            onCancel={() => {
                if (selected) {
                    setEditMode(false);
                } else {
                    clearNationSelection();
                }
            }}
        />;
    }
    if (selected) {
        return <NationDetail
            selectedSpecies={species}
            selectedNation={selected}
            onClear={clearSelection}
            onClearNation={clearNationSelection}
            onEdit={() => setEditMode(true)}
        />;
    }

    return <></>;
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

    useEffect(() => {
        editor.commands.setContent(selectedNation.description);
    }, [selectedNation]);

    return <Center>
        <Stack gap="sm">
            <Breadcrumbs>
                <Anchor onClick={onClear}>
                    {t('overview')}
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
                        <Title data-testid="name">
                            {selectedNation.name}
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
                    <EditorContent editor={editor} data-testid="description"/>
                    <Divider/>
                    <Group wrap="nowrap" justify="space-between" align="flex-start">
                        <Stack pl="xl">
                            <Title order={3}>
                                {t('advantages')}
                            </Title>
                            {selectedNation.advantageTraits.length > 0 ?
                                <List data-testid="advantageTraits">
                                    {selectedNation.advantageTraits.map((trait, index) =>
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
                            {selectedNation.disadvantageTraits.length > 0 ?
                                <List data-testid="disadvantages">
                                    {selectedNation.disadvantageTraits.map((trait, index) =>
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
    </Center>;
}

function NationEdit({
    selectedSpecies,
    initial,
    onSave,
    onDelete,
    onCancel
}: {
    selectedSpecies: Species;
    initial: Nation;
    onSave: (n: Nation) => void;
    onDelete: () => void;
    onCancel: () => void;
}) {
    const {t} = useTranslation();
    const queryClient = useQueryClient();
    const {activeUniverse} = useUniverseContext();
    const {userPermissions} = useUserContext();

    const form = useForm<Nation>({
        mode: 'controlled',
        initialValues: initial ?? {
            name: '',
            description: '',
            advantageTraits: [],
            disadvantageTraits: []
        }
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

    const {mutate: updateSpecies} = useUpdateSpecies({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({
                queryKey: getGetAllSpeciessQueryKey(activeUniverse.id)
            }).then(() => queryClient.invalidateQueries({
                queryKey: getGetSpeciesQueryKey(activeUniverse.id, selectedSpecies.id)
            }))
        }
    });

    const {mutate: updateNation} = useUpdateNation({
        mutation: {
            onSuccess: response => queryClient.invalidateQueries({
                queryKey: getGetNationQueryKey(activeUniverse.id, response.data.id)
            }).then(() => queryClient.invalidateQueries({
                queryKey: getGetAllNationsQueryKey(activeUniverse.id)
            })).then(() => onSave(response.data)),
            onError: handleValidationErrors(form.setErrors)
        }
    });

    const {mutate: deleteNation} = useDeleteNation({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({
                queryKey: getGetAllNationsQueryKey(activeUniverse.id)
            }).then(onDelete)
        }
    });

    const {mutate: insertNations} = useInsertAllNations({
        mutation: {
            onSuccess: response => queryClient.invalidateQueries({
                queryKey: [
                    getGetAllNationsQueryKey(activeUniverse.id)
                ],
            }).then(() => {
                if (!selectedSpecies) {
                    return;
                }
                updateSpecies({
                    universe: activeUniverse.id,
                    id: selectedSpecies.id,
                    data: {
                        ...selectedSpecies,
                        nations: selectedSpecies.nations.concat(response.data),
                    }
                });
            }).then(() => onSave(response.data[0])),
            onError: handleValidationErrors(handleDatabaseInsertErrors(form.setErrors))
        }
    });

    return <form
        data-testid="nation-form"
        onSubmit={form.onSubmit(nation => {
            if (nation.id) {
                updateNation({universe: activeUniverse.id, id: nation.id, data: nation});
            } else {
                insertNations({universe: activeUniverse.id, data: [nation]});
            }
        })}
    >
        <Center>
            <Stack>
                <Title order={3}>
                    {t('species:createNation')}
                </Title>
                <Group justify="space-between">
                    <TextInput
                        size="lg"
                        label={t('name')}
                        key={form.key('name')}
                        {...form.getInputProps('name')}
                    />
                    {userPermissions?.canWriteActiveUniverse && initial?.id !== undefined ?
                        <ConfirmationDialog
                            title={t('species:deleteNation')}
                            onConfirmation={() => deleteNation({universe: activeUniverse.id, id: initial.id})}
                            openNode={open =>
                                <Button variant="outline" color="red" onClick={open}>
                                    {t('delete')}
                                </Button>
                            }
                        /> : null
                    }
                </Group>
                <RichTextEditor editor={editor} variant="subtle" data-path="description">
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
                            {t('advantages')}
                        </Title>
                        <CharacterTraitInput
                            form={form}
                            path="advantageTraits"
                        />
                    </Stack>
                    <Stack>
                        <Title order={3}>
                            {t('disadvantages')}
                        </Title>
                        <CharacterTraitInput
                            form={form}
                            path="disadvantageTraits"
                        />
                    </Stack>
                </Group>
                <Group justify="flex-end" pt="md">
                    <Button onClick={onCancel} variant="outline">
                        {t('cancel')}
                    </Button>
                    <Button type="submit">
                        {t('save')}
                    </Button>
                </Group>
            </Stack>
        </Center>
    </form>;
}