import {ActionIcon, Group, Select, Slider, Stack, Switch, Table, TextInput, Tooltip} from '@mantine/core';
import React, {useContext, useState} from 'react';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {FaMinus, FaPlus} from 'react-icons/fa6';
import {PageElementSettings} from '../PageElementSettings';

type CellValues = {
    content: string;
    isCustomId: boolean;
}

type RowValues = {
    cellValues: CellValues[];
}

type ColumnDefinition = {
    label: string;
    size: number
}

/** Definition of a table */
export type TableDefinition = {
    columns: ColumnDefinition[]
    rowValues: RowValues[];
}

/** Empty table definition */
export const EMPTY_TABLE_DEFINITION = {
    columns: [{
        label: '',
        size: 50
    }, {
        label: '',
        size: 50
    }],
    rowValues: [{
        cellValues: [{
            content: '',
            isCustomId: false
        }, {
            content: '',
            isCustomId: false
        }]
    }, {
        cellValues: [{
            content: '',
            isCustomId: false
        }, {
            content: '',
            isCustomId: false
        }]
    }]
};

/** Part to show table with custom fields */
export function CustomTable({
    definition,
    setDefinition
}: {
    definition: TableDefinition;
    setDefinition: (d: TableDefinition) => void;
}) {
    const {characterForm} = useContext(PnPCharacterContext);
    const character = characterForm.getValues();

    return <>
        <Table
            withTableBorder
            striped
        >
            <Table.Tbody>
                <Table.Tr h={TABLE_ROW_HEIGHT + 3}>
                    {definition.columns.map((column, index) => (
                        <Table.Th style={{...TABLE_STYLE, width: `${column?.size ?? 50}%`}} key={index}>
                            {column?.label ?? ''}
                        </Table.Th>
                    ))}
                </Table.Tr>
                {definition.rowValues.map((rowValue, rowIndex) => (
                    <Table.Tr h={TABLE_ROW_HEIGHT} key={rowIndex}>
                        {definition.columns.map((_, colIndex) => {
                            const cell = rowValue?.cellValues[colIndex];

                            return <Table.Td style={TABLE_STYLE} key={colIndex}>
                                {(cell?.isCustomId ? character?.customFields?.[cell?.content] : cell?.content) ?? ''}
                            </Table.Td>;
                        })}
                    </Table.Tr>
                ))}
            </Table.Tbody>
        </Table>
        <PageElementSettings>
            <CustomTablePartSettings
                definition={definition}
                setDefinition={setDefinition}
            />
        </PageElementSettings>
    </>;
}

function CustomTablePartSettings({
    definition,
    setDefinition
}: {
    definition: TableDefinition
    setDefinition: (d: TableDefinition) => void
}) {
    const {t} = useTranslation();
    const [selectedIndex, setSelectedIndex] = useState<number>(0);
    const selectedRow = definition.rowValues[selectedIndex];

    return <Stack>
        <Stack gap={0}>
            <Group wrap="nowrap" justify="space-between">
                {t('sheetEditor:headers')}
                <Group wrap="nowrap" justify="flex-end" gap="2">
                    <ActionIcon
                        size="xs"
                        variant="filled"
                        disabled={definition.columns.length < 2}
                        onClick={() => setDefinition(removeLastColumn(definition))}
                    >
                        <FaMinus/>
                    </ActionIcon>
                    <ActionIcon
                        size="xs"
                        variant="filled"
                        onClick={() => setDefinition(addEmptyColumn(definition))}
                    >
                        <FaPlus/>
                    </ActionIcon>
                </Group>
            </Group>
            {definition.columns.map((column, index) => (
                <React.Fragment key={index}>
                    <TextInput
                        value={column.label}
                        onChange={e => {
                            const copy = copyTableDef(definition);
                            copy.columns[index] = {
                                ...copy.columns[index],
                                label: e.target.value
                            };
                            setDefinition(copy);
                        }}
                    />
                    <Slider
                        size="xs"
                        min={0}
                        max={100}
                        step={1}
                        styles={{markLabel: {display: 'none'}}}
                        value={column.size}
                        onChange={e => {
                            const copy = copyTableDef(definition);
                            copy.columns[index] = {
                                ...copy.columns[index],
                                size: e
                            };
                            setDefinition(copy);
                        }}
                    />
                </React.Fragment>
            ))}
        </Stack>
        <Stack gap={0}>
            <Group wrap="nowrap" justify="space-between">
                {t('sheetEditor:rows')}
                <Group wrap="nowrap" justify="flex-end" gap="2">
                    <Select
                        size="xs"
                        w={80}
                        variant="unstyled"
                        allowDeselect={false}
                        data={Array.from({length: definition.rowValues.length}, (_, i) => ({
                            label: String(i + 1),
                            value: String(i),
                        }))}
                        styles={{
                            input: {textAlign: 'right'},
                            dropdown: {textAlign: 'right'},
                        }}
                        value={String(selectedIndex)}
                        onChange={s => setSelectedIndex(Number(s))}
                        comboboxProps={{withinPortal: false}}
                    />
                    <ActionIcon
                        size="xs"
                        variant="filled"
                        disabled={definition.rowValues.length < 2}
                        onClick={() => setDefinition(removeLastRow(definition))}
                    >
                        <FaMinus/>
                    </ActionIcon>
                    <ActionIcon
                        size="xs"
                        variant="filled"
                        onClick={() => setDefinition(addEmptyRow(definition))}
                    >
                        <FaPlus/>
                    </ActionIcon>
                </Group>
            </Group>
            {selectedRow.cellValues.map((cell, index) => (
                <Group wrap="nowrap" key={index} gap="xs">
                    <TextInput
                        flex={1}
                        value={cell?.content}
                        onChange={(e) => {
                            const copy = copyTableDef(definition);
                            copy.rowValues[selectedIndex].cellValues[index] = {
                                ...cell,
                                content: e.target.value,
                            };
                            setDefinition(copy);
                        }}
                    />
                    <Tooltip label={t('sheetEditor:asCustomFieldId') + '. ' + t('sheetEditor:customFieldIdTooltip')}>
                        <div>
                            <Switch
                                checked={cell?.isCustomId}
                                onChange={e => {
                                    const copy = copyTableDef(definition);
                                    copy.rowValues[selectedIndex].cellValues[index] = {
                                        ...cell,
                                        isCustomId: e.target.checked,
                                    };
                                    setDefinition(copy);
                                }}
                            />
                        </div>
                    </Tooltip>
                </Group>
            ))}
        </Stack>
    </Stack>;
}

function copyTableDef(definition: TableDefinition): TableDefinition {
    return {
        columns: [...definition.columns],
        rowValues: definition.rowValues.map(v => ({
            cellValues: [...v.cellValues]
        }))
    };
}

function addEmptyColumn(definition: TableDefinition): TableDefinition {
    const copy = copyTableDef(definition);
    copy.columns.push({
        label: '',
        size: 50
    });
    for (let rowValue of copy.rowValues) {
        rowValue.cellValues.push({
            content: '',
            isCustomId: false
        });
    }
    return copy;
}

function removeLastColumn(definition: TableDefinition): TableDefinition {
    const copy = copyTableDef(definition);
    copy.columns.splice(copy.columns.length - 1, 1);
    for (let rowValue of copy.rowValues) {
        rowValue.cellValues.splice(copy.columns.length - 1, 1);
    }
    return copy;
}

function addEmptyRow(definition: TableDefinition): TableDefinition {
    const copy = copyTableDef(definition);
    copy.rowValues.push({
        cellValues: copy.columns.map(_ => ({
            content: '',
            isCustomId: false
        }))
    });
    return copy;
}

function removeLastRow(definition: TableDefinition): TableDefinition {
    const copy = copyTableDef(definition);
    copy.rowValues.splice(copy.rowValues.length - 1, 1);
    return copy;
}