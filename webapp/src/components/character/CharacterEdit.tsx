import {PnPCharacterDTO, PnPCharacterSheet, StatsDto, Universe} from '../../api/model';
import {useTranslation} from 'react-i18next';
import {useUniverseContext, useUserContext} from '../PageBase';
import {useEmptyCharacter} from './PnPCharacterContext';
import {useForm, UseFormReturnType} from '@mantine/form';
import React, {useEffect, useMemo, useState} from 'react';
import {useDebouncedCallback} from '@mantine/hooks';
import {Anchor, Breadcrumbs, Button, Center, Group, Select, Stack} from '@mantine/core';
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
import {fetchAllCharacterSheets} from '../Database';
import {getGetPermissionsQueryKey} from '../../api/user-service/user-service';

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
    const {user} = useUserContext();
    const emptyCharacter = useEmptyCharacter();
    const [selectedSheet, setSelectedSheet] = useState<PnPCharacterSheet>(null);
    const [sheets] = fetchAllCharacterSheets();

    const sortedSheets = useMemo(() => [
        sheetSettings?.playerSheet,
        sheetSettings?.enemySheet,
        ...sheets.filter(s =>
            s.id !== sheetSettings?.playerSheet?.id
            && s.id !== sheetSettings?.enemySheet?.id
        ),
    ].filter(s => Boolean(s)), [sheetSettings, sheets]);
    useEffect(() => {
        if (sortedSheets.length === 0) {
            return;
        }
        setSelectedSheet(sortedSheets[0]);
    }, [sortedSheets]);

    const form = useForm<PnPCharacterDTO>({
        initialValues: character ?? emptyCharacter,
        cascadeUpdates: true,
    });
    useEffect(() => {
        form.setValues(character ?? emptyCharacter);
        form.initialize(character ?? emptyCharacter);
        form.resetDirty();
    }, [character, emptyCharacter]);

    useCharacterChangeListener(form, activeUniverse);

    const {mutate: updateCharacter} = useUpdateCharacter({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({
                queryKey: getGetCharacterQueryKey(activeUniverse.id, form.values.id)
            }).then(() => queryClient.invalidateQueries({
                queryKey: getGetAllCharactersQueryKey(activeUniverse.id)
            })).then(() => form.resetDirty()).then(() => notifications.show({
                title: t('saveSuccessful'),
                message: t('saveSuccessfulMessage', {'type': t('character')}),
                color: 'green'
            })).then(onSave),
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
            onSuccess: response => queryClient.invalidateQueries({
                queryKey: getGetAllCharactersQueryKey(activeUniverse.id)
            }).then(() => queryClient.invalidateQueries({
                queryKey: getGetPermissionsQueryKey(user.username)
            })).then(() => form.setFieldValue('id', response.data[0].id))
                .then(() => form.resetDirty())
                .then(() => notifications.show({
                    title: t('saveSuccessful'),
                    message: t('saveSuccessfulMessage', {'type': t('character')}),
                    color: 'green'
                })).then(onSave),
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
                        sheet={selectedSheet}
                    />
                    <Stack>
                        <Select
                            data={sortedSheets.map(s => {
                                return {
                                    value: s.id,
                                    label: s.name + (s.id === sheetSettings?.playerSheet?.id ? ' (' + t('universe:playerSheet') + ')'
                                        : s.id === sheetSettings?.enemySheet?.id ? ' (' + t('universe:enemySheet') + ')' : '')
                                };
                            })}
                            value={selectedSheet?.id}
                            onChange={v => setSelectedSheet(sortedSheets.find(s => s?.id === v) ?? null)}
                            searchable
                        />
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


function useCharacterChangeListener(form: UseFormReturnType<PnPCharacterDTO>, activeUniverse: Universe) {
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

                Object.entries(entries.talents).forEach(([key, value]) => {
                    if (values.talents[key]) {
                        form.setFieldValue(`talents.${key}.totalValue`, value.totalValue);
                    } else {
                        form.setFieldValue(`talents.${key}`, value);
                    }
                });
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
    form.watch('talents', () => {
        handleStatsChange();
    });
    form.watch('equipment', () => {
        handleStatsChange();
    });
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