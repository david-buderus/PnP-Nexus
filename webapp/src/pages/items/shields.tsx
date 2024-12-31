import { getUniverseContext } from '../../components/PageBase';
import { currencyToHumanReadable } from '../../components/Utils';
import { useTranslation } from 'react-i18next';
import { ERarity, ItemServiceApi, Shield } from '../../api';
import { API_CONFIGURATION } from '../../components/Constants';
import { OverviewBasePage } from '../../components/database/OverviewBasePage';
import { createItemFields, mapArrayResponse, mapResponse } from './items';

const ITEM_API = new ItemServiceApi(API_CONFIGURATION);


const ShieldPage = () => {
    const { t } = useTranslation();
    const { currencySettings } = getUniverseContext();

    return <OverviewBasePage<Shield>
        columns={[
            { label: t("name"), id: "name", getter: item => item.name },
            { label: t("item:tags"), id: "tags", getter: item => Array.from(item.tags).join(", ") },
            { label: t("material"), id: "material", getter: item => item.material?.name },
            { label: t("armor"), id: "armor", getter: item => item.armor, numeric: true },
            { label: t("weight"), id: "weight", getter: item => item.weight, numeric: true },
            { label: t("hit"), id: "hit", getter: item => item.hit, numeric: true },
            { label: t("initiative"), id: "initiative", getter: item => item.initiative, numeric: true },
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
        ]}
        fields={createItemFields()}
        sortingKey="name"
        creationDialogTitle={t("item:creationTitle")}
        editDialogTitle={t("item:editTitle")}
        deletionDialogTitle={t("upgrade:confirmDeletionTitle")}
        fetchObjects={universe => ITEM_API.getAllShields(universe)}
        removeObjects={(universe, selected) => ITEM_API.deleteAllItems(universe, selected)}
        editObject={(universe, id, itemType) => ITEM_API.updateItem(universe, id, itemType).then(mapResponse<Shield>)}
        createObjects={(universe, objs) => ITEM_API.insertAllItems(universe, objs).then(mapArrayResponse<Shield>)}
        emptyObject={{
            "@type": "Shield",
            name: "",
            tags: new Set<string>(),
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
            hit: 0,
            initiative: 0,
            dice: { dices: [] },
            upgradeSlots: 0
        }}
    />;
};

export default ShieldPage;
