import {useTranslation} from 'react-i18next';
import {useMemo, useState} from 'react';
import OverviewPage, {ExtendedColumnDef} from '../../../components/OverviewPage';
import {Nation, PnPCharacterDto, Species} from '../../../api';
import {fetchAllCharacters} from '../../../components/Database';
import {Button, Center} from '@mantine/core';
import {useUniverseContext} from '../../../components/PageBase';
import {PnPCharacterView} from '../../../components/character/PnPCharacterView';
import {useForm} from '@mantine/form';

/** A character without any values */
export const EMPTY_CHARACTERS: PnPCharacterDto = {
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

/** An overview over all characters */
export function CharactersOverview() {
    const {t} = useTranslation();
    const [allCharacters, refreshCharacters, loading] = fetchAllCharacters();
    const [editMode, setEditMode] = useState<boolean>(false);

    const columns = useMemo<ExtendedColumnDef<PnPCharacterDto, any>[]>(
        () => [
            {
                accessorKey: 'description.name',
                header: t('name'),
            },
            {
                accessorKey: 'origin.species',
                header: t('species'),
                Cell: cell => cell.cell.getValue<Species>()?.name ?? '',
                filterFn: (row, id, filterValue) => {
                    return row.getValue<Species>(id)?.name.includes(filterValue);
                }
            },
            {
                accessorKey: 'origin.nation',
                header: t('nation'),
                Cell: cell => cell.cell.getValue<Nation>()?.name ?? '',
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
    const {sheetSettings} = useUniverseContext();
    const form = useForm<PnPCharacterDto>({
        initialValues: EMPTY_CHARACTERS
    });

    return <Center>
        <PnPCharacterView
            characterForm={form}
            allowEdit={true}
            sheet={sheetSettings.playerSheet}
        />
    </Center>;
}