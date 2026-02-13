import {useTranslation} from 'react-i18next';
import {useMemo, useState} from 'react';
import OverviewPage from '../../../components/OverviewPage';
import {Nation, PnPCharacterDTO, PnPCharacterServiceApi, Species} from '../../../api';
import {fetchAllCharacters} from '../../../components/Database';
import {Button, Center} from '@mantine/core';
import {useUniverseContext} from '../../../components/PageBase';
import {PnPCharacterView} from '../../../components/character/PnPCharacterView';
import {useForm} from '@mantine/form';
import {ExtendedColumnDef} from '../../../components/table/SortableTable';
import {API_CONFIGURATION} from '../../../components/Constants';

/** A character without any values */
export const EMPTY_CHARACTERS: PnPCharacterDTO = {
    description: {
        affiliations: '',
        appearance: '',
        backstory: '',
        deficits: '',
        gender: '',
        goals: '',
        name: '',
        personality: '',
        profession: ''
    },
    level: {},
    origin: {
        species: undefined,
        nation: undefined
    },
    advantageTraits: [],
    disadvantageTraits: [],
    customFields: {},
    equipment: {
        armor: {},
        jewellery: {},
        shieldEquipment: undefined,
        weaponEquipments: []
    },
    inventory: {
        coin: 0,
        inventory: {
            items: [],
            maxSize: 100
        },
    },
    spells: [],
    stats: {
        primaryStats: {},
        secondaryStats: {}
    },
    talents: {},
};

const CHARACTER_API = new PnPCharacterServiceApi(API_CONFIGURATION);

/** An overview over all characters */
export function CharactersOverview() {
    const {t} = useTranslation();
    const [allCharacters, refreshCharacters, loading] = fetchAllCharacters();
    const [editMode, setEditMode] = useState<boolean>(false);

    const columns = useMemo<ExtendedColumnDef<PnPCharacterDTO, any>[]>(
        () => [
            {
                accessorKey: 'description.name',
                header: t('name'),
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
                cell: cell => cell.getValue<Nation>()?.name ?? '',
                filterFn: (row, id, filterValue) => {
                    return row.getValue<Nation>(id)?.name.includes(filterValue);
                }
            }
        ], []);

    if (editMode) {
        return <CharacterEdit/>;
    }

    return <OverviewPage
        fetchData={[allCharacters, refreshCharacters, loading]}
        columns={columns}
        identifier="characters"
        manipulationDialog={(editButton, _, disabled, getInitial) => {
            if (editButton) {
                return <Button disabled={disabled}>
                    {t('edit')}
                </Button>;
            }
            return <Button onClick={() => setEditMode(true)} disabled={disabled}>
                {t('add')}
            </Button>;
        }}
        deletionDialogTitle={t('spell:editTitle')}
        onDelete={(universe, characters) => Promise.reject()}
        idKey="id"
    />;
}

function CharacterEdit() {
    const {sheetSettings, activeUniverse} = useUniverseContext();
    const form = useForm<PnPCharacterDTO>({
        initialValues: EMPTY_CHARACTERS,
        cascadeUpdates: true,
    });

    form.watch('stats.primaryStats', () => {
        CHARACTER_API.recalculateEntries(activeUniverse.id, form.getValues()).then(response => {
            const entries = response.data;
            form.setFieldValue('stats.secondaryStats', entries.secondaryStats);
            form.setFieldValue('talents', entries.talents);
        });
    });

    return <Center>
        <PnPCharacterView
            characterForm={form}
            allowEdit={true}
            sheet={sheetSettings.playerSheet}
        />
    </Center>;
}