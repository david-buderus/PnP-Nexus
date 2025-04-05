import { getUniverseContext } from '../../components/PageBase';
import { currencyToHumanReadable } from '../../components/Utils';
import { useTranslation } from 'react-i18next';
import { Armor, EArmorSlot, ERarity, ItemServiceApi } from '../../api';
import { API_CONFIGURATION } from '../../components/Constants';
import { OverviewBasePage } from '../../components/database/OverviewBasePage';
import { createItemFields, mapArrayResponse, mapResponse } from './items';
import { Column } from '../../components/OverviewTable';

const ITEM_API = new ItemServiceApi(API_CONFIGURATION);

const ArmorPage = () => {
    const { t } = useTranslation();
    const { currencySettings, itemSettings } = getUniverseContext();

    const columns: Column<Armor>[] = [
        { label: t("name"), id: "name", getter: item => item.name },
        { label: t("item:tags"), id: "tags", getter: item => item.tags.join(", ") },
        { label: t("material"), id: "material", getter: item => item.material?.name },
        { label: t("armor"), id: "armor", getter: item => item.armor, numeric: true },
        { label: t("weight"), id: "weight", getter: item => item.weight, numeric: true },
        { label: t("item:armorSlot"), id: "armorSlot", getter: item => t("enum:" + item.armorSlot.toLocaleLowerCase()) },
        { label: t("rarity"), id: "rarity", getter: item => t("enum:" + item.rarity.toLowerCase()) },
        { label: t("tier"), id: "tier", getter: item => item.tier, numeric: true },
        { label: t("effect"), id: "effect", getter: item => item.effect },
        { label: t("description"), id: "description", getter: item => item.description },
        { label: t("upgradeSlots"), id: "upgradeSlots", getter: item => item.upgradeSlots, numeric: true },
        { label: t("requirement"), id: "requirement", getter: item => item.requirement },
        { label: t("price"), id: "vendorPrice", getter: item => currencyToHumanReadable(currencySettings, item.vendorPrice) },
        { label: t("item:minStackSize"), id: "minimumStackSize", getter: item => item.minimumStackSize, numeric: true, defaultVisible: false },
        { label: t("item:maxStackSize"), id: "maximumStackSize", getter: item => item.maximumStackSize, numeric: true, defaultVisible: false },
        { label: t("note"), id: "note", getter: item => item.note, defaultVisible: false }
    ];

    if (itemSettings.usingProtection) {
        columns.splice(5, 0, { label: t("protection"), id: "protection", getter: item => item.protection, numeric: true });
    }

    return <OverviewBasePage<Armor>
        columns={columns}
        fields={createItemFields()}
        sortingKey="name"
        creationDialogTitle={t("item:creationTitle")}
        editDialogTitle={t("item:editTitle")}
        deletionDialogTitle={t("upgrade:confirmDeletionTitle")}
        fetchObjects={universe => ITEM_API.getAllArmor(universe)}
        removeObjects={(universe, selected) => ITEM_API.deleteAllItems(universe, selected)}
        editObject={(universe, id, itemType) => ITEM_API.updateItem(universe, id, itemType).then(mapResponse<Armor>)}
        createObjects={(universe, objs) => ITEM_API.insertAllItems(universe, objs).then(mapArrayResponse<Armor>)}
        emptyObject={{
            "@type": "Armor",
            name: "",
            tags: [],
            rarity: ERarity.Common,
            tier: 1,
            effect: "",
            description: "",
            requirement: "",
            vendorPrice: 0,
            minimumStackSize: 0,
            maximumStackSize: 100,
            note: "",
            material: null,
            armor: 0,
            weight: 0,
            protection: 0,
            armorSlot: EArmorSlot.Body,
            upgradeSlots: 0
        }}
    />;
};

export default ArmorPage;
