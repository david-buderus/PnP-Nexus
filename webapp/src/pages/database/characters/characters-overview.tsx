import {useTranslation} from 'react-i18next';
import React, {useMemo, useState} from 'react';
import OverviewPage from '../../../components/OverviewPage';
import {CharacterDescription, Nation, PnPCharacterDTO, Species} from '../../../api/model';
import {fetchAllCharacters} from '../../../components/Database';
import {ExtendedColumnDef} from '../../../components/table/SortableTable';
import {CharacterEdit} from '../../../components/character/CharacterEdit';
import {useNavigate} from 'react-router-dom';
import {useUniverseContext} from '../../../components/PageBase';
import TruncatedCell from '../../../components/table/TruncatedCell';
import {handleNetworkErrors} from '../../../components/utils/ErrorUtils';
import {
    getGetAllCharactersQueryKey,
    useDeleteAllCharacters
} from '../../../api/pn-p-character-service/pn-p-character-service';
import {useQueryClient} from '@tanstack/react-query';


/** An overview over all characters */
export function CharactersOverview() {
    const {t} = useTranslation();
    const queryClient = useQueryClient();
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

    const {mutateAsync: deleteCharacters} = useDeleteAllCharacters({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({queryKey: getGetAllCharactersQueryKey(activeUniverse.id)}),
            onError: handleNetworkErrors
        }
    });

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
        idKey="id"
        identifier="characters"
        onAdd={() => setEditMode(true)}
        onEdit={c => navigate('/characters/' + c.id + '?universe=' + activeUniverse.id)}
        deletionDialogTitle={t('spell:editTitle')}
        onDelete={(universe, characters) => deleteCharacters({
            universe: universe,
            params: {
                ids: characters.map(c => c.id)
            }
        })}
        manipulationWithReadAccess={true}
    />;
}