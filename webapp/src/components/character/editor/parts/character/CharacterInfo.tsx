import {Combobox, ComboboxProps, InputBase, Table, useCombobox} from '@mantine/core';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext} from 'react';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {TableTextInput} from '../inputs/TableTextInput';
import {CharacterOrigin} from '../../../../../api';
import {fetchAllSpecies} from '../../../../Database';

/** Shows name and co of the character */
export function CharacterInfo() {
    const {t} = useTranslation();
    const {characterForm, allowEdit} = useContext(PnPCharacterContext);

    return <Table
        variant="vertical"
        layout="fixed"
        withTableBorder
    >
        <Table.Tbody>
            <Table.Tr h={TABLE_ROW_HEIGHT}>
                <Table.Th style={TABLE_STYLE}>{t('name')}</Table.Th>
                <Table.Td style={TABLE_STYLE}>
                    <TableTextInput
                        readOnly={!allowEdit}
                        key={characterForm.key('description.name')}
                        {...characterForm.getInputProps('description.name')}
                    />
                </Table.Td>
            </Table.Tr>

            <Table.Tr h={TABLE_ROW_HEIGHT}>
                <Table.Th style={TABLE_STYLE}>{t('species')}</Table.Th>
                <Table.Td style={TABLE_STYLE}>
                    <CharacterOriginSelect
                        readOnly={!allowEdit}
                        key={characterForm.key('description.origin')}
                        {...characterForm.getInputProps('description.origin')}
                    />
                </Table.Td>
            </Table.Tr>

            <Table.Tr h={TABLE_ROW_HEIGHT}>
                <Table.Th style={TABLE_STYLE}>{t('crafting:profession')}</Table.Th>
                <Table.Td style={TABLE_STYLE}>
                    <TableTextInput
                        readOnly={!allowEdit}
                        key={characterForm.key('description.profession')}
                        {...characterForm.getInputProps('description.profession')}
                    />
                </Table.Td>
            </Table.Tr>
        </Table.Tbody>
    </Table>;
}

function CharacterOriginSelect(props: {
    value?: CharacterOrigin,
    onChange?: (v: CharacterOrigin) => void
} & Omit<ComboboxProps, 'onChange' | 'value'>) {
    const {value, onChange, readOnly, ...other} = props;
    const [data] = fetchAllSpecies();
    const combobox = useCombobox({
        onDropdownClose: () => combobox.resetSelectedOption(),
    });

    const parse = (val: string | null) => {
        if (!val) {
            return null;
        }
        const [type, id] = val.split(':');

        if (type === 'species') {
            const s = data.find((s) => s.id === id);
            return s ? {species: s, nation: null} : null;
        }

        for (const s of data) {
            const n = s.nations.find((n) => n.id === id);
            if (n) {
                return {species: s, nation: n};
            }
        }
        return null;
    };

    return (
        <Combobox
            store={combobox}
            onOptionSubmit={id => onChange(parse(id))}
            readOnly={readOnly}
            {...other}
        >
            {/* Target input */}
            <Combobox.Target>
                <InputBase
                    component="button"
                    type="button"
                    pointer
                    onClick={() => !readOnly && combobox.toggleDropdown()}
                    unstyled
                    styles={{
                        root: {
                            width: '100%',
                        },
                        input: {
                            padding: 0,
                            margin: 0,
                            border: 'none',
                            outline: 'none',
                            background: 'transparent',
                            width: '100%',
                            textAlign: 'left',

                            // 6 is the padding of the TableCells
                            height: TABLE_ROW_HEIGHT - 6,
                            lineHeight: `${TABLE_ROW_HEIGHT - 6}px`,

                            fontSize: TABLE_STYLE.fontSize,
                            overflow: TABLE_STYLE.overflow,
                            textOverflow: TABLE_STYLE.textOverflow,
                            whiteSpace: TABLE_STYLE.whiteSpace,
                        },
                    }}
                >
                    {value ? (
                        value.nation ? (
                            `${value.species.name} / ${value.nation.name}`
                        ) : (
                            value.species.name
                        )
                    ) : null}
                </InputBase>
            </Combobox.Target>

            {/* Dropdown */}
            <Combobox.Dropdown>
                <Combobox.Options>
                    {data.map((species) => (
                        <div key={species.id}>
                            <Combobox.Option
                                value={`species:${species.id}`}
                                style={{
                                    fontWeight: 600,
                                    paddingLeft: 6,
                                    lineHeight: '24px',
                                }}
                            >
                                {species.name}
                            </Combobox.Option>
                            {species.nations.map((nation) => (
                                <Combobox.Option
                                    key={nation.id}
                                    value={`nation:${nation.id}`}
                                    style={{
                                        paddingLeft: 22,
                                        lineHeight: '24px',
                                        fontSize: '0.9em',
                                    }}
                                >
                                    {nation.name}
                                </Combobox.Option>
                            ))}
                        </div>
                    ))}
                </Combobox.Options>
            </Combobox.Dropdown>
        </Combobox>
    );
}