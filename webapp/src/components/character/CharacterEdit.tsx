import {PnPCharacterDTO, StatsDto} from '../../api/model';
import {useTranslation} from 'react-i18next';
import {useUniverseContext} from '../PageBase';
import {useEmptyCharacter} from './PnPCharacterContext';
import {useForm} from '@mantine/form';
import React, {useEffect} from 'react';
import {useDebouncedCallback} from '@mantine/hooks';
import {Anchor, Breadcrumbs, Button, Center, Group, Stack} from '@mantine/core';
import {handleDatabaseInsertErrors, handleNetworkErrors, handleValidationErrors} from '../utils/ErrorUtils';
import {PnPCharacterView} from './PnPCharacterView';
import ConfirmationDialog from '../modal/ConfirmationDialog';
import {
    getGetAllCharactersQueryKey,
    getGetCharacterQueryKey,
    useDeleteCharacter,
    useInsertAllCharacters,
    useRecalculateEntries,
    useUpdateCharacter
} from '../../api/pn-p-character-service/pn-p-character-service';
import {useQueryClient} from '@tanstack/react-query';
import {notifications} from '@mantine/notifications';

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
    const queryClient = useQueryClient();
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

    const {mutate: recalculateEntries} = useRecalculateEntries({
        mutation: {
            onSuccess: response => {
                const values = form.getValues();
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
            },
            onError: handleNetworkErrors
        }
    });

    const handleStatsChange = useDebouncedCallback(() => {
        recalculateEntries({universe: activeUniverse.id, data: form.values});
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
    form.watch('advantageTraits', () => {
        handleStatsChange();
    });
    form.watch('disadvantageTraits', () => {
        handleStatsChange();
    });

    const {mutate: updateCharacter} = useUpdateCharacter({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({
                queryKey: getGetCharacterQueryKey(activeUniverse.id, form.values.id)
            }).then(() => queryClient.invalidateQueries({
                queryKey: getGetAllCharactersQueryKey(activeUniverse.id)
            })).then(() => form.resetDirty()).then(onSave),
            onError: error => {
                handleValidationErrors(handleDatabaseInsertErrors(form.setErrors))(error);
                notifications.show({
                    title: t('error:validationFailedNotification'),
                    message: t('error:validationFailedMessage', {'type': t('character')}),
                    color: 'red'
                });
            }
        }
    });
    const {mutate: insertCharacter} = useInsertAllCharacters({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({
                queryKey: getGetAllCharactersQueryKey(activeUniverse.id)
            }).then(() => form.resetDirty()).then(onSave),
            onError: error => {
                handleValidationErrors(handleDatabaseInsertErrors(form.setErrors))(error);
                notifications.show({
                    title: t('error:validationFailedNotification'),
                    message: t('error:validationFailedMessage', {'type': t('character')}),
                });
            }
        }
    });
    const {mutate: deleteCharacter} = useDeleteCharacter({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({
                queryKey: getGetAllCharactersQueryKey(activeUniverse.id)
            }).then(onDelete),
            onError: handleNetworkErrors
        }
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
                        updateCharacter({
                            universe: activeUniverse.id,
                            id: c.id,
                            data: c
                        });
                    } else {
                        insertCharacter({
                            universe: activeUniverse.id,
                            data: [c]
                        });
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
                                onConfirmation={() => deleteCharacter({
                                    universe: activeUniverse.id,
                                    id: character.id
                                })}
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