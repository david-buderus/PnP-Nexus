import {useTranslation} from 'react-i18next';
import {useMemo} from 'react';
import OverviewPage, {ExtendedColumnDef} from '../../../components/OverviewPage';
import {Nation, PnPCharacterDto, Species} from '../../../api';
import {fetchAllCharacters} from '../../../components/Database';

/** An overview over all characters */
export function CharactersOverview() {
    const {t} = useTranslation();

    const columns = useMemo<ExtendedColumnDef<PnPCharacterDto, any>[]>(
        () => [
            {
                accessorKey: 'description.name',
                header: t('name'),
            },
            {
                accessorKey: 'species',
                header: t('species'),
                Cell: cell => cell.cell.getValue<Species>()?.name ?? '',
                filterFn: (row, id, filterValue) => {
                    return row.getValue<Species>(id)?.name.includes(filterValue);
                }
            },
            {
                accessorKey: 'nation',
                header: t('nation'),
                Cell: cell => cell.cell.getValue<Nation>()?.name ?? '',
                filterFn: (row, id, filterValue) => {
                    return row.getValue<Nation>(id)?.name.includes(filterValue);
                }
            }
        ], []);

    return <OverviewPage
        fetchData={fetchAllCharacters()}
        columns={columns}
        identifier="characters"
        manipulationDialog={(editMode, refresh, disabled, getInitial) => <></>}
        deletionDialogTitle={t('spell:editTitle')}
        onDelete={(universe, spells) => Promise.reject()}
        idKey="id"
    />;
}