import { API_CONFIGURATION, ItemClass, SomeItem } from "../Constants";
import OverviewTable, { Column } from '../../components/OverviewTable';
import { getUniverseContext, getUserContext } from '../../components/PageBase';
import { useEffect, useState } from 'react';
import { ItemServiceApi, ItemType, ItemTypeServiceApi, Material, MaterialServiceApi, Universe } from '../../api';
import { Button } from '@mui/material';
import { useTranslation } from 'react-i18next';
import { ItemCreationDialog } from '../../components/items/ItemCreationDialog';
import { ItemEditDialog } from '../../components/items/ItemEditDialog';
import { ConfirmationDialog } from '../../components/inputs/ConfirmationDialog';
import { NoUniverse } from "../../pages/noUniverse";

const ITEM_API = new ItemServiceApi(API_CONFIGURATION);
const ITEM_TYPE_API = new ItemTypeServiceApi(API_CONFIGURATION);
const MATERIAL_API = new MaterialServiceApi(API_CONFIGURATION);

/** Props needed for the items page */
interface ItemBasePageProps<I> {
    /** Item class which will be shown */
    itemClass: ItemClass;
    /** The columns shown on the item page */
    columns: Column<I>[];
}

async function fetchItems<I>(universe: Universe, itemClass: ItemClass): Promise<I[]> {
    if (universe === null) {
        return [];
    } else if (itemClass !== "Item") {
        return (await ITEM_API.getAllItems(universe.name)).data.filter(item => item["@type"] === itemClass).map(item => item as I);
    } else {
        return (await ITEM_API.getAllItems(universe.name)).data.map(item => item as I);
    }
}

async function deleteItems<I>(universe: Universe, ids: I[keyof I][]): Promise<boolean> {
    if (universe === null) {
        return false;
    }
    const response = await ITEM_API.deleteAllItems(universe.name, ids.map(id => id as string));
    return response.status < 400;
}

async function fetchItemTypes(universe: Universe): Promise<ItemType[]> {
    if (universe === null) {
        return [];
    } else {
        return (await ITEM_TYPE_API.getAllItemTypes(universe.name)).data;
    }
}

async function fetchMaterials(universe: Universe): Promise<Material[]> {
    if (universe === null) {
        return [];
    } else {
        return (await MATERIAL_API.getAllMaterials(universe.name)).data;
    }
}

/** Table to show specific item class */
export function ItemBasePage<I extends SomeItem>(props: ItemBasePageProps<I>) {
    const { itemClass, columns } = props;
    const { t } = useTranslation();
    const { activeUniverse } = getUniverseContext();
    const { userPermissions } = getUserContext();
    const [items, setItems] = useState<I[]>([]);
    const [itemTypes, setItemTypes] = useState<ItemType[]>([]);
    const [materials, setMaterials] = useState<Material[]>([]);
    const [selected, setSelected] = useState<I[keyof I][]>([]);

    const [openItemCreationDialog, setOpenItemCreationDialog] = useState(false);
    const [openItemEditDialog, setOpenItemEditDialog] = useState(false);
    const [openDeleteDialog, setOpenDeleteDialog] = useState(false);

    useEffect(() => {
        fetchItems<I>(activeUniverse, itemClass).then(fetchedItems => setItems(fetchedItems));
    }, [activeUniverse]);

    useEffect(() => {
        fetchItemTypes(activeUniverse).then(fetchedItemTypes => setItemTypes(fetchedItemTypes));
    }, [activeUniverse]);

    useEffect(() => {
        fetchMaterials(activeUniverse).then(fetchedMaterials => setMaterials(fetchedMaterials));
    }, [activeUniverse]);

    if (!activeUniverse) {
        return <NoUniverse />;
    }

    return <div>
        <OverviewTable id='id' sortBy="name" data={items} columns={columns} selectedState={[selected, setSelected]}>
        </OverviewTable>
        {
            userPermissions.canWriteActiveUniverse &&
            <div className='w-full pt-2'>
                <div className='float-right'>
                    <Button className='btn' onClick={() => setOpenItemCreationDialog(true)}>
                        {t("add")}
                    </Button>
                    <Button className='btn' disabled={selected.length !== 1} onClick={() => setOpenItemEditDialog(true)}>
                        {t("edit")}
                    </Button>
                    <Button className='btn' disabled={selected.length === 0} onClick={() => setOpenDeleteDialog(true)}>
                        {t("delete")}
                    </Button>
                </div>
                <ItemCreationDialog
                    open={openItemCreationDialog}
                    onClose={(event, reason) => {
                        if (reason === "succesful") {
                            fetchItems<I>(activeUniverse, itemClass).then(fetchedItems => setItems(fetchedItems));
                        }
                        setOpenItemCreationDialog(false);
                    }}
                    itemTypes={itemTypes}
                    materials={materials}
                    initialItemsClass={itemClass}
                />
                <ItemEditDialog
                    key={openItemEditDialog ? selected[0] as string : "no-selection"}
                    open={openItemEditDialog}
                    onClose={(event, reason) => {
                        if (reason === "succesful") {
                            fetchItems<I>(activeUniverse, itemClass).then(fetchedItems => setItems(fetchedItems));
                        }
                        setOpenItemEditDialog(false);
                    }}
                    itemTypes={itemTypes}
                    materials={materials}
                    itemToEdit={openItemEditDialog ? items.find(item => item.id === selected[0]) : null}
                />
                <ConfirmationDialog
                    title={t('item:confirmDeletionTitle')}
                    open={openDeleteDialog}
                    onClose={confirmation => {
                        setOpenDeleteDialog(false);
                        if (!confirmation) {
                            return;
                        }
                        deleteItems(activeUniverse, selected).then(sucessful => {
                            if (sucessful) {
                                fetchItems<I>(activeUniverse, itemClass).then(fetchedItems => setItems(fetchedItems));
                            }
                        });
                    }}
                />
            </div>
        }
    </div>;
}
