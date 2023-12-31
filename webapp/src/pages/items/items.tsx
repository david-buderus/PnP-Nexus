import OverviewTable from '../../components/OverviewTable';
import { getUniverseContext, getUserContext } from '../../components/PageBase';
import { useEffect, useState } from 'react';
import { Item, ItemServiceApi, Universe } from '../../api';
import { currencyToHumanReadable } from '../../components/Utils';
import { NoUniverse } from '../NoUniverse';
import { Button } from '@mui/material';

const ITEM_API = new ItemServiceApi();

async function fetchItems(universe: Universe): Promise<Item[]> {
    if (universe === null) {
        return [];
    } else {
        return (await ITEM_API.getAllItems(universe.name)).data;
    }
}

async function deleteItems(universe: Universe, ids: string[]): Promise<boolean> {
    if (universe === null) {
        return false;
    }
    const response = await ITEM_API.deleteAllItems(universe.name, ids);
    return response.status < 400;
}

const Items = () => {
    const { activeUniverse } = getUniverseContext();
    const { userPermissions } = getUserContext();
    const [items, setItems] = useState<Item[]>([]);
    const selectedState = useState<string[]>([]);
    const [selected, _] = selectedState;

    useEffect(() => {
        fetchItems(activeUniverse).then(fetchedItems => setItems(fetchedItems));
    }, [activeUniverse]);

    return <div>
        <OverviewTable id='id' sortBy="name" data={items} columns={[
            { label: "Name", id: "name", getter: item => item.name, numeric: false },
            { label: "Type", id: "type", getter: item => item.type.name, numeric: false },
            { label: "Subtype", id: "subtype", getter: item => item.subtype.name, numeric: false },
            { label: "Rarity", id: "rarity", getter: item => item.rarity, numeric: false },
            { label: "Effect", id: "effect", getter: item => item.effect, numeric: false },
            { label: "Description", id: "description", getter: item => item.description, numeric: false },
            { label: "Price", id: "vendorPrice", getter: item => currencyToHumanReadable(activeUniverse, item.vendorPrice), numeric: false },
            { label: "Min. Stacksize", id: "minimumStackSize", getter: item => item.minimumStackSize, numeric: true },
            { label: "Max. Stacksize", id: "maximumStackSize", getter: item => item.maximumStackSize, numeric: true },
            { label: "Note", id: "note", getter: item => item.note, numeric: false }
        ]} selectedState={selectedState}>
        </OverviewTable>
        {
            userPermissions.canWriteActiveUniverse ?
                (<div className='w-full pt-2'>
                    <div className='float-right'>
                        <Button className='btn'>
                            New
                        </Button>
                        <Button className='btn' disabled={selected.length !== 1}>
                            Edit
                        </Button>
                        <Button className='btn' disabled={selected.length === 0} onClick={_ => {
                            deleteItems(activeUniverse, selected).then(sucessful => {
                                if (sucessful) {
                                    fetchItems(activeUniverse).then(fetchedItems => setItems(fetchedItems));
                                }
                            })
                        }}>
                            Delete
                        </Button>
                    </div>
                </div>)
                : null
        }
    </div>;
};

export default Items;
