import { useTranslation } from "react-i18next";
import { ExtendedItemType, ExtendedItemTypeServiceApi, ItemType, ItemTypeServiceApi, ItemTypeTypeRestrictionEnum } from "../../api";
import { API_CONFIGURATION } from "../../components/Constants";
import { OverviewBasePage } from "../../components/database/OverviewBasePage";
import { useEffect, useState } from "react";
import { getUniverseContext } from "../../components/PageBase";

const ITEM_TYPE_API = new ItemTypeServiceApi(API_CONFIGURATION);
const EXTENDED_ITEM_TYPE_API = new ExtendedItemTypeServiceApi(API_CONFIGURATION);

/** Page to give an overview over all item types */
export function ItemTypePage() {
    const { t } = useTranslation();
    const { activeUniverse } = getUniverseContext();

    const [itemTypes, setItemTypes] = useState<ItemType[]>([]);

    useEffect(() => {
        if (!activeUniverse) {
            return;
        }
        ITEM_TYPE_API.getAllItemTypes(activeUniverse.name).then(response => setItemTypes(response.data));
    }, [activeUniverse]);

    return <OverviewBasePage<ExtendedItemType>
        columns={[
            { label: t("name"), id: "name", getter: itemType => itemType.name },
            { label: t("item:typeRestriction"), id: "typeRestriction", getter: itemType => t(itemType.typeRestriction.toLowerCase()) },
            { label: t("item:broaderVariants"), id: "broaderVariants", getter: itemType => itemType.broaderVariants.map(variant => variant.name).join(", ") },
        ]}
        fields={[
            { fieldId: "name", label: t("name"), fieldType: "STRING" },
            {
                fieldId: "typeRestriction", label: t("item:typeRestriction"), fieldType: "ENUM",
                dependency: Object.values(ItemTypeTypeRestrictionEnum).map(restriction => {
                    return { key: restriction, content: restriction, label: t(restriction.toLowerCase()) };
                })
            },
            { fieldId: "broaderVariants", label: t("item:broaderVariants"), fieldType: "MULTI_DATABASE", dependency: itemTypes, dependencyLabel: "name" }
        ]}
        sortingKey="name"
        creationDialogTitle={"itemTypeCreationTitle"}
        editDialogTitle={"itemTypeEditTitle"}
        deletionDialogTitle={"itemTypeDeletionTitle"}
        fetchObjects={universe => EXTENDED_ITEM_TYPE_API.getAllExtendedItemTypes(universe)}
        removeObjects={(universe, selected) => ITEM_TYPE_API.deleteAllItemTypes(universe, selected)}
        editObject={(universe, id, itemType) => EXTENDED_ITEM_TYPE_API.updateExtendedItemType(universe, id, itemType)}
        createObjects={(universe, objs) => EXTENDED_ITEM_TYPE_API.insertAllExtendedItemTypes(universe, objs)}
        emptyObject={{
            "name": "",
            typeRestriction: ItemTypeTypeRestrictionEnum.Item,
            "broaderVariants": []
        }}
    />;
}