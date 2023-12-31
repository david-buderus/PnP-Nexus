import OverviewTable from '../../components/OverviewTable';
import { getUniverseContext, getUserContext } from '../../components/PageBase';
import { useEffect, useState } from 'react';
import { Item, ItemServiceApi, Universe } from '../../api';
import { currencyToHumanReadable } from '../../components/Utils';
import { NoUniverse } from '../NoUniverse';
import { Button } from '@mui/material';
import { useTranslation } from 'react-i18next';

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
    const { t } = useTranslation();
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
            { label: t("name"), id: "name", getter: item => item.name, numeric: false },
            { label: t("item:type"), id: "type", getter: item => item.type.name, numeric: false },
            { label: t("item:subtype"), id: "subtype", getter: item => item.subtype.name, numeric: false },
            { label: t("rarity"), id: "rarity", getter: item => t("enum:" + item.rarity.toLowerCase()), numeric: false },
            { label: t("effect"), id: "effect", getter: item => item.effect, numeric: false },
            { label: t("description"), id: "description", getter: item => item.description, numeric: false },
            { label: t("price"), id: "vendorPrice", getter: item => currencyToHumanReadable(activeUniverse, item.vendorPrice), numeric: false },
            { label: t("item:minStackSize"), id: "minimumStackSize", getter: item => item.minimumStackSize, numeric: true },
            { label: t("item:maxStackSize"), id: "maximumStackSize", getter: item => item.maximumStackSize, numeric: true },
            { label: t("note"), id: "note", getter: item => item.note, numeric: false }
        ]} selectedState={selectedState}>
        </OverviewTable>
        {
            userPermissions.canWriteActiveUniverse ?
                (<div className='w-full pt-2'>
                    <div className='float-right'>
                        <Button className='btn'>
                            {t("add")}
                        </Button>
                        <Button className='btn' disabled={selected.length !== 1}>
                            {t("edit")}
                        </Button>
                        <Button className='btn' disabled={selected.length === 0} onClick={_ => {
                            deleteItems(activeUniverse, selected).then(sucessful => {
                                if (sucessful) {
                                    fetchItems(activeUniverse).then(fetchedItems => setItems(fetchedItems));
                                }
                            })
                        }}>
                            {t("delete")}
                        </Button>
                    </div>
                </div>)
                : null
        }
    </div>;
};

export default Items;
