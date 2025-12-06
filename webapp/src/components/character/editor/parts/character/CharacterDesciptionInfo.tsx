import {useNode} from '@craftjs/core';
import {NumberInput, Stack, Table} from '@mantine/core';
import React, {useContext} from 'react';
import {getPartStyle, TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {ObjectSelect} from '../../../../input/ObjectSelect';
import {CharacterDescription} from '../../../../../api';

/** The description key with its human-readable name */
type Description = {
    id: keyof CharacterDescription;
    name: string;
}

/** Part to show text */
export const CharacterDescriptionInfo = ({description, numberOfRows}: {
    description: Description;
    numberOfRows: number;
}) => {
    const {characterForm} = useContext(PnPCharacterContext);
    const character = characterForm.getValues();
    const {connectors: {connect, drag}, selected} = useNode((state => ({
        selected: state.events.selected
    })));

    return <Table
        withTableBorder
        striped
        ref={ref => connect(drag(ref))}
        style={getPartStyle(selected)}
    >
        <Table.Tbody>
            <Table.Tr h={TABLE_ROW_HEIGHT + 3}>
                <Table.Th style={TABLE_STYLE}>{description?.name ?? '???'}</Table.Th>
            </Table.Tr>
            <Table.Tr h={TABLE_ROW_HEIGHT * numberOfRows}>
                <Table.Td style={{whiteSpace: 'pre-line', textAlign: 'left', verticalAlign: 'top', ...TABLE_STYLE}}>
                    {description?.id ? character?.description[description.id] ?? '' : ''}
                </Table.Td>
            </Table.Tr>
        </Table.Tbody>
    </Table>;
};

const CharacterDescriptionInfoSettings = () => {
    const {t} = useTranslation();
    const {actions: {setProp}, description, numberOfRows} = useNode(node => ({
        description: node.data.props.description as Description,
        numberOfRows: node.data.props.numberOfRows
    }));

    return <Stack>
        <ObjectSelect<Description>
            data={[
                {
                    id: 'appearance',
                    name: t('character:appearance'),
                },
                {
                    id: 'personality',
                    name: t('character:personality'),
                },
                {
                    id: 'goals',
                    name: t('character:goals'),
                },
                {
                    id: 'deficits',
                    name: t('character:deficits'),
                },
                {
                    id: 'affiliations',
                    name: t('character:affiliations'),
                },
                {
                    id: 'backstory',
                    name: t('character:backstory'),
                }
            ]}
            idKey="id"
            labelKey="name"
            value={description}
            onChange={d => {
                setProp(props => {
                    props.description = d;
                });
            }}
        />
        <NumberInput
            value={numberOfRows}
            onChange={e => {
                setProp(props => {
                    props.numberOfRows = Number(e);
                });
            }}
            min={1}
        />
    </Stack>;
};

CharacterDescriptionInfo.craft = {
    name: 'sheetEditor:characterDescription',
    related: {
        settings: CharacterDescriptionInfoSettings
    }
};