import {ActionIcon, Box, Group, List, Popover, Stack, Switch, Table, Text, Tooltip} from '@mantine/core';
import React, {useContext, useMemo} from 'react';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {PageElementSettings} from '../PageElementSettings';
import {IconCircleMinus, IconCirclePlus, IconEditCircle} from '@tabler/icons-react';
import {randomId} from '@mantine/hooks';
import {SomeCharacterTrait} from '../../../../Constants';
import {ICharacterTrait, SingleCharacterTraitInput} from '../../../../input/CharacterTraitInput';
import {FormErrors} from '@mantine/form';
import {ECalculation} from '../../../../../api/model';

/** Part to show text */
export function AdvantagesInfo({showsAdvantages, setShowsAdvantages}: {
    showsAdvantages: boolean;
    setShowsAdvantages: (b: boolean) => void;
}) {
    const {t} = useTranslation();
    const {characterForm, allowEdit} = useContext(PnPCharacterContext);
    const character = characterForm.getValues();

    const entries = useMemo(() => {
        if (!character) {
            return [];
        }
        if (showsAdvantages) {
            return character.advantageTraits;
        } else {
            return character.disadvantageTraits;
        }
    }, [character, showsAdvantages]);

    const speciesTraits = (showsAdvantages ? character?.origin?.species?.advantageTraits : character?.origin?.species?.disadvantageTraits) ?? [];
    const nationTraits = (showsAdvantages ? character?.origin?.nation?.advantageTraits : character?.origin?.nation?.disadvantageTraits) ?? [];
    const path = showsAdvantages ? 'advantageTraits' : 'disadvantageTraits';

    return <>
        <Table
            withTableBorder
            striped
            style={{height: '100%', tableLayout: 'fixed'}}
        >
            <Table.Tbody>
                <Table.Tr h={TABLE_ROW_HEIGHT + 3}>
                    <Table.Th style={TABLE_STYLE}>
                        {showsAdvantages ? t('advantages') : t('disadvantages')}
                    </Table.Th>
                </Table.Tr>
                <Table.Tr>
                    <Table.Td
                        style={{whiteSpace: 'pre-line', textAlign: 'left', verticalAlign: 'top', ...TABLE_STYLE}}>
                        <List size="sm">
                            {speciesTraits.map((entry, index) =>
                                <List.Item key={'species-' + index}>
                                    <Text
                                        size="sm"
                                        truncate="end"
                                        style={{flex: 1, minWidth: 0}}
                                    >
                                        {entry.description}
                                    </Text>
                                </List.Item>
                            )}
                            {nationTraits.map((entry, index) =>
                                <List.Item key={'nation-' + index}>
                                    <Text
                                        size="sm"
                                        truncate="end"
                                        style={{flex: 1, minWidth: 0}}
                                    >
                                        {entry.description}
                                    </Text>
                                </List.Item>
                            )}
                            {entries.map((entry, index) => {
                                const errors = collectErrors(path + '.' + index, characterForm.errors);

                                return <List.Item
                                    key={'character-' + index}
                                    styles={{
                                        itemWrapper: {width: '100%', minWidth: 0},
                                        itemLabel: {width: '100%', minWidth: 0}
                                    }}
                                >
                                    <Group align="flex-start" wrap="nowrap" style={{width: '100%'}}>
                                        <Tooltip label={
                                            <Stack gap="xs">
                                                {errors.map(([key, errorMessage]) => (
                                                    <Text key={key} size="xs">
                                                        {errorMessage}
                                                    </Text>
                                                ))}
                                            </Stack>
                                        } disabled={errors.length == 0}>
                                            <Text
                                                size="sm"
                                                truncate="end"
                                                style={{flex: 1, minWidth: 0}}
                                                c={errors.length > 0 ? 'red' : undefined}
                                            >
                                                {entry.description ? entry.description : t('nothing-here')}
                                            </Text>
                                        </Tooltip>
                                        {allowEdit ?
                                            <Group
                                                wrap="nowrap"
                                                gap={1}
                                                style={{flexShrink: 0}}
                                                onClick={e => e.stopPropagation()}
                                            >
                                                <TraitEditPopover
                                                    showsAdvantages={showsAdvantages}
                                                    index={index}
                                                />
                                                <ActionIcon
                                                    variant="subtle"
                                                    size={TABLE_ROW_HEIGHT - 8}
                                                    className="no-drag"
                                                    style={{flexShrink: 0}}
                                                    onClick={() => characterForm.removeListItem(path, index)}
                                                >
                                                    <IconCircleMinus color="red" size={14}/>
                                                </ActionIcon>
                                            </Group> : null
                                        }
                                    </Group>
                                </List.Item>;
                            })}
                        </List>
                    </Table.Td>
                </Table.Tr>
            </Table.Tbody>
        </Table>
        <PageElementSettings>
            <Switch
                label={showsAdvantages ? t('advantages') : t('disadvantages')}
                checked={showsAdvantages}
                onChange={e => setShowsAdvantages(e.target.checked)}
            />
        </PageElementSettings>
        <TraitAdditionPopover showsAdvantages={showsAdvantages}/>
    </>;
}

function TraitAdditionPopover({showsAdvantages}: { showsAdvantages: boolean }) {
    const {allowEdit, characterForm} = useContext(PnPCharacterContext);
    const path = showsAdvantages ? 'advantageTraits' : 'disadvantageTraits';

    if (!allowEdit) {
        return null;
    }

    return <ActionIcon
        variant="subtle"
        size="sm"
        className="no-drag"
        style={{
            position: 'absolute',
            top: 2,
            right: 2,
            zIndex: 10, // Ensure it stays above everything
        }}
        onClick={() => {
            characterForm.insertListItem(path, {
                '@type': 'SimpleCharacterTrait',
                description: '',
                attribute: null,
                calculation: ECalculation.ADDITIVE,
                rollModifier: 1,
                talent: null,
                key: randomId()
            } as SomeCharacterTrait);
        }}
    >
        <IconCirclePlus size={14}/>
    </ActionIcon>;
}

function TraitEditPopover({showsAdvantages, index}: { showsAdvantages: boolean; index: number; }) {
    const {allowEdit, characterForm} = useContext(PnPCharacterContext);
    const path = showsAdvantages ? 'advantageTraits' : 'disadvantageTraits';
    const trait = showsAdvantages ? characterForm.values.advantageTraits[index] : characterForm.values.disadvantageTraits[index];

    if (!allowEdit) {
        return null;
    }

    return <Popover
        position="bottom"
        withArrow
        shadow="md"
    >
        <Popover.Target>
            <ActionIcon
                variant="subtle"
                size={TABLE_ROW_HEIGHT - 8}
                className="no-drag"
            >
                <IconEditCircle size={14}/>
            </ActionIcon>
        </Popover.Target>
        <Popover.Dropdown>
            <Box w={350}>
                <SingleCharacterTraitInput
                    trait={trait as ICharacterTrait}
                    form={characterForm}
                    path={path}
                    index={index}
                />
            </Box>
        </Popover.Dropdown>
    </Popover>;
}

function collectErrors(path: string, errors: FormErrors) {
    return Object.entries(errors).filter(([key]) =>
        key === path || key.startsWith(`${path}.`)
    );
}