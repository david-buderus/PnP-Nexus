import {PnPCharacterDTO, PnPCharacterServiceApi, StatsDto} from '../../api';
import {useTranslation} from 'react-i18next';
import {useUniverseContext} from '../PageBase';
import {useEmptyCharacter} from './PnPCharacterContext';
import {useForm} from '@mantine/form';
import React, {useEffect} from 'react';
import {useDebouncedCallback} from '@mantine/hooks';
import {Anchor, Breadcrumbs, Button, Center, Group, Stack} from '@mantine/core';
import {handleDatabaseInsertErrors, handleValidationErrors} from '../utils/ErrorUtils';
import {PnPCharacterView} from './PnPCharacterView';
import ConfirmationDialog from '../modal/ConfirmationDialog';
import {API_CONFIGURATION} from '../Constants';

const CHARACTER_API = new PnPCharacterServiceApi(API_CONFIGURATION);

/** Allows to edit the given character */
export function CharacterEdit({
    character,
    onCancel = () => {
        // NOP
    },
    onSave = () => {
        // NOP
    },
    onDelete = () => {
        // NOP
    },
}: {
    character?: PnPCharacterDTO;
    onCancel?: () => void;
    onSave?: () => void;
    onDelete?: () => void;
}) {
    const {t} = useTranslation();
    const {sheetSettings, activeUniverse} = useUniverseContext();
    const emptyCharacter = useEmptyCharacter();

    const form = useForm<PnPCharacterDTO>({
        initialValues: character ?? emptyCharacter,
        cascadeUpdates: true,
    });
    useEffect(() => {
        form.setValues(character ?? emptyCharacter);
        form.initialize(character ?? emptyCharacter);
        form.resetDirty();
    }, [character, emptyCharacter]);
    useEffect(() => {

    }, [character]);

    const handleStatsChange = useDebouncedCallback(async () => {
        const values = form.getValues();
        const response = await CHARACTER_API.recalculateEntries(activeUniverse.id, values);
        const entries = response.data;

        Object.entries(entries.primaryStats).forEach(([key, value]) => {
            if (values.stats.primaryStats[key]) {
                form.setFieldValue(`stats.primaryStats.${key}.totalValue`, value.totalValue);
            } else {
                form.setFieldValue(`stats.primaryStats.${key}`, value);
            }
        });

        Object.entries(entries.secondaryStats).forEach(([key, value]) => {
            if (values.stats.primaryStats[key]) {
                form.setFieldValue(`stats.secondaryStats.${key}.rawValue`, value.totalValue);
                form.setFieldValue(`stats.secondaryStats.${key}.totalValue`, value.totalValue);
            } else {
                form.setFieldValue(`stats.secondaryStats.${key}`, value);
            }
        });
        form.setFieldValue('talents', entries.talents);
    }, 100);

    form.watch('stats.primaryStats', event => {
        if (!haveStatsChange(event.previousValue, event.value)) {
            return;
        }
        handleStatsChange();
    });
    form.watch('stats.secondaryStats', event => {
        if (!haveFlatModifierChange(event.previousValue, event.value)) {
            return;
        }
        handleStatsChange();
    });

    return <Center>
        <Stack>
            <Breadcrumbs>
                <ConfirmationDialog
                    title={t('unsavedChangesTitle')}
                    text={t('unsavedChangesDescription')}
                    onConfirmation={onCancel}
                    openNode={(open) =>
                        <Anchor
                            onClick={() => {
                                if (form.isDirty()) {
                                    open();
                                } else {
                                    onCancel();
                                }
                            }}
                            variant="outline"
                        >
                            {t('overview')}
                        </Anchor>}
                />
                <Anchor>
                    {character?.description?.name ?? ''}
                </Anchor>
            </Breadcrumbs>
            <form
                onSubmit={form.onSubmit(c => {
                    if (c.id) {
                        CHARACTER_API.updateCharacter(activeUniverse.id, c.id, c)
                            .then(() => form.resetDirty())
                            .then(onSave)
                            .catch(handleValidationErrors(form.setErrors));
                    } else {
                        CHARACTER_API.insertAllCharacters(activeUniverse.id, [c])
                            .then(() => form.resetDirty())
                            .then(onSave)
                            .catch(handleValidationErrors(handleDatabaseInsertErrors(form.setErrors)));
                    }
                })}
            >
                <Group wrap="nowrap" align="flex-start">
                    <PnPCharacterView
                        characterForm={form}
                        allowEdit={true}
                        sheet={sheetSettings?.playerSheet}
                    />
                    <Stack>
                        <Button type="submit">
                            {t('save')}
                        </Button>
                        {character?.id !== undefined ?
                            <ConfirmationDialog
                                title={t('sheetEditor:deleteSheet')}
                                onConfirmation={() => {
                                    CHARACTER_API.deleteCharacter(activeUniverse.id, character.id).then(onDelete);
                                }}
                                openNode={open =>
                                    <Button variant="outline" color="red" onClick={open}>
                                        {t('delete')}
                                    </Button>
                                }
                            /> : null
                        }
                        <ConfirmationDialog
                            title={t('unsavedChangesTitle')}
                            text={t('unsavedChangesDescription')}
                            onConfirmation={onCancel}
                            openNode={(open) =>
                                <Button
                                    onClick={() => {
                                        if (form.isDirty()) {
                                            open();
                                        } else {
                                            onCancel();
                                        }
                                    }}
                                    variant="outline"
                                >
                                    {t('close')}
                                </Button>}
                        />
                    </Stack>
                </Group>
            </form>
        </Stack>
    </Center>;
}

function haveStatsChange(prevValues: Record<string, StatsDto>, values: Record<string, StatsDto>): boolean {
    if (Object.keys(prevValues).length !== Object.keys(values).length) {
        return true;
    }

    return Object.entries(prevValues).some(([key, prevStat]) => {
        const currentStat = values[key];
        return prevStat.rawValue !== currentStat?.rawValue || prevStat.flatModifier !== currentStat?.flatModifier;
    });
}

function haveFlatModifierChange(prevValues: Record<string, StatsDto>, values: Record<string, StatsDto>): boolean {
    if (Object.keys(prevValues).length !== Object.keys(values).length) {
        return true;
    }

    return Object.entries(prevValues).some(([key, prevStat]) => {
        const currentStat = values[key];
        return prevStat.flatModifier !== currentStat?.flatModifier;
    });
}