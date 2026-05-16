import {useTranslation} from 'react-i18next';
import React, {useMemo, useState} from 'react';
import OverviewPage from '../../../components/OverviewPage';
import {CharacterDescription, Nation, PnPCharacterDTO, PnPCharacterServiceApi, Species} from '../../../api';
import {fetchAllCharacters} from '../../../components/Database';
import {Button} from '@mantine/core';
import {ExtendedColumnDef} from '../../../components/table/SortableTable';
import {API_CONFIGURATION} from '../../../components/Constants';
import {CharacterEdit} from '../../../components/character/CharacterEdit';
import {useNavigate} from 'react-router-dom';
import {useUniverseContext} from '../../../components/PageBase';
import TruncatedCell from '../../../components/table/TruncatedCell';

const CHARACTER_API = new PnPCharacterServiceApi(API_CONFIGURATION);

/** An overview over all characters */
export function CharactersOverview() {
    const {t} = useTranslation();
    const {activeUniverse} = useUniverseContext();
    const [allCharacters, refreshCharacters, loading] = fetchAllCharacters();
    const [editMode, setEditMode] = useState<boolean>(false);
    const navigate = useNavigate();

    const columns = useMemo<ExtendedColumnDef<PnPCharacterDTO, any>[]>(
        () => [
            {
                accessorKey: 'description.name',
                header: t('name'),
            },
            {
                accessorKey: 'level.level',
                header: t('character:level'),
                defaultHidden: true
            },
            {
                accessorKey: 'origin.species',
                header: t('species'),
                cell: cell => cell.getValue<Species>()?.name ?? '',
                filterFn: (row, id, filterValue) => {
                    return row.getValue<Species>(id)?.name.includes(filterValue);
                }
            },
            {
                accessorKey: 'origin.nation',
                header: t('nation'),
                cell: cell => cell.getValue<CharacterDescription>()?.name ?? '',
                filterFn: (row, id, filterValue) => {
                    return row.getValue<Nation>(id)?.name.includes(filterValue);
                }
            },
            {
                accessorKey: 'description.backstory',
                header: t('character:backstory'),
                defaultHidden: true,
                cell: TruncatedCell,
                filterFn: () => false
            }
        ], []);

    if (editMode) {
        return <CharacterEdit
            onCancel={() => {
                setEditMode(false);
                refreshCharacters();
            }}
            onDelete={() => {
                setEditMode(false);
                refreshCharacters();
            }}
        />;
    }

    return <OverviewPage
        fetchData={[allCharacters, refreshCharacters, loading]}
        columns={columns}
        identifier="characters"
        manipulationDialog={(editButton, _, disabled, getInitial) => {
            if (editButton) {
                return <Button disabled={disabled}
                               onClick={() => navigate('/characters/' + getInitial().id + '?universe=' + activeUniverse.id)}>
                    {t('edit')}
                </Button>;
            }
            return <Button onClick={() => setEditMode(true)} disabled={disabled}>
                {t('add')}
            </Button>;
        }}
        deletionDialogTitle={t('spell:editTitle')}
        onDelete={(universe, characters) =>
            CHARACTER_API.deleteAllCharacters(universe, characters.map(c => c.id))}
        idKey="id"
    />;
}