import {Button, Center, Group, Input, Stack, Switch, TextInput, Title} from '@mantine/core';
import {useTranslation} from 'react-i18next';
import StarterKit from '@tiptap/starter-kit';
import {BubbleMenu, useEditor} from '@tiptap/react';
import {Link, RichTextEditor} from '@mantine/tiptap';
import Underline from '@tiptap/extension-underline';
import TextAlign from '@tiptap/extension-text-align';
import {Species, SpeciesServiceApi} from '../../api';
import {useUniverseContext, useUserContext} from '../PageBase';
import {API_CONFIGURATION} from '../Constants';
import {useForm} from '@mantine/form';
import {handleDatabaseInsertErrors, handleValidationErrors} from '../utils/ErrorUtils';
import ConfirmationDialog from '../modal/ConfirmationDialog';
import {CharacterTraitInput} from '../input/CharacterTraitInput';

const SPECIES_API = new SpeciesServiceApi(API_CONFIGURATION);

/**
 * A form to create/edit species
 */
export function SpeciesForm({
    initial,
    onSave,
    onDelete,
    onCancel
}: {
    initial?: Species;
    onSave: (s: Species) => void;
    onDelete: () => void;
    onCancel: () => void;
}) {
    const {t} = useTranslation();
    const {activeUniverse} = useUniverseContext();
    const {userPermissions} = useUserContext();

    const form = useForm<Species>({
        mode: 'controlled',
        initialValues: initial ?? {
            name: '',
            description: '',
            nations: [],
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

    return <form
        data-testid="species-form"
        onSubmit={form.onSubmit(species => {
            if (species.id) {
                SPECIES_API.updateSpecies(activeUniverse.id, species.id, species)
                    .then(response => onSave(response.data)).catch(handleValidationErrors(form.setErrors));
            } else {
                SPECIES_API.insertAllSpeciess(activeUniverse.id, [species])
                    .then(response => onSave(response.data[0]))
                    .catch(handleValidationErrors(handleDatabaseInsertErrors(form.setErrors)));
            }
        })}
    >
        <Center>
            <Stack>
                <Title order={3}>
                    {t('species:createSpecies')}
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
                            title={t('species:deleteSpecies')}
                            onConfirmation={() => {
                                SPECIES_API.deleteSpecies(activeUniverse.id, initial.id).then(onDelete);
                            }}
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
                <Switch
                    label={t('species:playable')}
                    key={form.key('playable')}
                    {...form.getInputProps('playable', {type: 'checkbox'})}
                />
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