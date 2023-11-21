import OverviewTable from '../components/OverviewTable';
import { getUniverseContext } from '../components/PageBase';
import { useEffect, useState } from 'react';
import { Item, ItemServiceApi, Universe } from '../api';
import { currencyToHumanReadable } from '../components/Utils';

const ITEM_API = new ItemServiceApi();

async function fetchItems(universe: Universe): Promise<Item[]> {
    if (universe === null) {
        return [];
    } else {
        return (await ITEM_API.getAllItems(universe.name)).data;
    }
}

const Items = () => {
    const { activeUniverse } = getUniverseContext();
    const [items, setItems] = useState<Item[]>([]);

    useEffect(() => {
        fetchItems(activeUniverse).then(fetchedItems => setItems(fetchedItems));
    }, [activeUniverse]);

    return <OverviewTable data={items} keyGetter={item => item.id} columns={[
        { name: "Name", getter: item => item.name },
        { name: "Type", getter: item => item.type.name },
        { name: "Subtype", getter: item => item.subtype.name },
        { name: "Rarity", getter: item => item.rarity },
        { name: "Effect", getter: item => item.effect },
        { name: "Description", getter: item => item.description },
        { name: "Price", getter: item => currencyToHumanReadable(activeUniverse, item.vendorPrice) },
        { name: "Min. Stacksize", getter: item => item.minimumStackSize },
        { name: "Max. Stacksize", getter: item => item.maximumStackSize },
        { name: "Note", getter: item => item.note }
    ]}>
    </OverviewTable>;
};

export default Items;
