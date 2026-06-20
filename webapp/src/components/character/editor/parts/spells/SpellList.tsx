import {ActionIcon, Group, NumberInput, Popover, Table, Text} from '@mantine/core';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext, useMemo, useState} from 'react';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {resourceFormatter, spellCastFormatter} from '../../../../utils/Formatters';
import {resizeArray} from '../../../../utils/Utils';
import {PageElementSettings} from '../PageElementSettings';
import {fetchAllSpells} from '../../../../Database';
import {SpellSearchCard} from '../../../../spells/SpellSearchCard';
import {IconCircleMinus, IconCirclePlus} from '@tabler/icons-react';
import {Spell} from '../../../../../api/model';
import {SpellCardModal} from '../../../../spells/SpellCard';
import {PnPCharacterPrintContext} from '../../../PnPCharacterPrintContext';


/** Shows the spells of the character */
export function SpellList({
    numberOfRows, setNumberOfRows
}: {
    numberOfRows: number;
    setNumberOfRows: (n: number) => void;
}) {
    const {t} = useTranslation();
    const {characterForm, allowEdit} = useContext(PnPCharacterContext);
    const {showSpells} = useContext(PnPCharacterPrintContext);
    const character = characterForm.getValues();
    const spells = useMemo(() => resizeArray(character.spells, numberOfRows), [character.spells, numberOfRows]);

    const [lastClicked, setLastClicked] = useState<Spell>(null);

    return <>
        <Table
            withTableBorder
            withColumnBorders
            striped
            layout="fixed"
        >
            <Table.Tbody>
                <Table.Tr h={TABLE_ROW_HEIGHT}>
                    <Table.Th style={{width: '15%', ...TABLE_STYLE}}>{t('name')}</Table.Th>
                    <Table.Th style={{width: '45%', ...TABLE_STYLE}}>{t('effect')}</Table.Th>
                    <Table.Th style={{width: '8%', ...TABLE_STYLE}}>{t('spell:castTimeShort')}</Table.Th>
                    <Table.Th style={{width: '8%', ...TABLE_STYLE}}>{t('spell:cooldownShort')}</Table.Th>
                    <Table.Th style={{width: '12%', ...TABLE_STYLE}}>{t('spell:cost')}</Table.Th>
                    <Table.Th style={{width: '12%', ...TABLE_STYLE}}>{t('spell:cast')}</Table.Th>
                </Table.Tr>
                {spells.map((spell, index) => {
                    if (!spell) {
                        return <Table.Tr key={index} h={TABLE_ROW_HEIGHT}>
                            <Table.Td style={TABLE_STYLE}/>
                            <Table.Td style={TABLE_STYLE}/>
                            <Table.Td style={TABLE_STYLE}/>
                            <Table.Td style={TABLE_STYLE}/>
                            <Table.Td style={TABLE_STYLE}/>
                            <Table.Td style={TABLE_STYLE}>
                                <Group justify="flex-end">
                                    <SpellAdditionPopover/>
                                </Group>
                            </Table.Td>
                        </Table.Tr>;
                    }
                    return <Table.Tr
                        key={index}
                        h={TABLE_ROW_HEIGHT}
                        onClick={allowEdit ? () => setLastClicked(spell) : null}
                    >
                        <Table.Td style={TABLE_STYLE}>
                            <div className={!showSpells ? 'no-print' : undefined}>
                                {spell.name}
                            </div>
                        </Table.Td>
                        <Table.Td style={TABLE_STYLE}>
                            <div className={!showSpells ? 'no-print' : undefined}>
                                {spell.effect}
                            </div>
                        </Table.Td>
                        <Table.Td style={TABLE_STYLE}>
                            <div className={!showSpells ? 'no-print' : undefined}>
                                {spell.castTime}
                            </div>
                        </Table.Td>
                        <Table.Td style={TABLE_STYLE}>
                            <div className={!showSpells ? 'no-print' : undefined}>
                                {spell.cooldown}
                            </div>
                        </Table.Td>
                        <Table.Td style={TABLE_STYLE}>
                            <div className={!showSpells ? 'no-print' : undefined}>
                                {spell.cost?.map(resourceFormatter)?.join(', ') ?? ''}
                            </div>
                        </Table.Td>
                        <Table.Td style={TABLE_STYLE}>
                            <Group
                                justify="space-between"
                                wrap="nowrap"
                                style={{width: '100%'}}
                                className={!showSpells ? 'no-print' : undefined}
                            >
                                <Text
                                    size={TABLE_STYLE.fontSize}
                                    truncate="end"
                                    style={{flex: 1, minWidth: 0}}
                                >
                                    {spellCastFormatter(spell.cast, t)}
                                </Text>
                                {allowEdit ?
                                    <ActionIcon
                                        variant="subtle"
                                        size={TABLE_ROW_HEIGHT - 8}
                                        className="no-drag"
                                        style={{flexShrink: 0}}
                                        onClick={e => {
                                            characterForm.removeListItem('spells', index);
                                            e.stopPropagation();
                                        }}
                                    >
                                        <IconCircleMinus color="red" size={14}/>
                                    </ActionIcon> : null
                                }
                            </Group>
                        </Table.Td>
                    </Table.Tr>;
                })}
            </Table.Tbody>
        </Table>
        <SpellCardModal
            spell={lastClicked}
            onClose={() => setLastClicked(null)}
        />
        <PageElementSettings>
            <NumberInput
                label={t('sheetEditor:numberOfRows')}
                value={numberOfRows}
                onChange={e => setNumberOfRows(Number(e))}
                min={1}
                allowDecimal={false}
            />
        </PageElementSettings>
    </>;
}

function SpellAdditionPopover() {
    const {allowEdit, characterForm} = useContext(PnPCharacterContext);
    const [spells] = fetchAllSpells();
    const filteredSpells = useMemo(() => spells.filter(spell => !characterForm.values.spells.includes(spell)),
        [spells, characterForm.values.spells]);

    if (!allowEdit) {
        return null;
    }

    return (
        <Popover position="bottom" withArrow shadow="md">
            <Popover.Target>
                <ActionIcon
                    variant="subtle"
                    size={TABLE_ROW_HEIGHT - 8}
                    className="no-drag"
                >
                    <IconCirclePlus size={14}/>
                </ActionIcon>
            </Popover.Target>
            <Popover.Dropdown>
                <SpellSearchCard
                    spells={filteredSpells}
                    onSelect={spell => characterForm.insertListItem('spells', spell)}
                />
            </Popover.Dropdown>
        </Popover>
    );
}