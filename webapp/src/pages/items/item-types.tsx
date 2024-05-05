import { useTranslation } from "react-i18next";
import { ItemType, ItemTypeServiceApi, ItemTypeTypeRestrictionEnum } from "../../api";
import { API_CONFIGURATION } from "../../components/Constants";
import { OverviewBasePage } from "../../components/OverviewBasePage";

const ITEM_TYPE_API = new ItemTypeServiceApi(API_CONFIGURATION);

export function ItemTypePage() {
    const { t } = useTranslation();

    return <OverviewBasePage<ItemType>
        columns={[
            { label: t("name"), id: "name", getter: itemType => itemType.name },
            { label: t("item:typeRestriction"), id: "typeRestriction", getter: itemType => t(itemType.typeRestriction.toLowerCase()) },
        ]}
        fields={[
            { fieldId: "name", label: t("name"), fieldType: "STRING" },
            {
                fieldId: "typeRestriction", label: t("item:typeRestriction"), fieldType: "ENUM",
                dependency: Object.values(ItemTypeTypeRestrictionEnum).map(restriction => {
                    return { key: restriction, content: restriction, label: t(restriction.toLowerCase()) };
                })
            }
        ]}
        sortingKey="name"
        creationDialogTitle={""}
        editDialogTitle={""}
        deletionDialogTitle={""}
        fetchObjects={universe => ITEM_TYPE_API.getAllItemTypes(universe)}
        removeObjects={(universe, selected) => ITEM_TYPE_API.deleteAllItemTypes(universe, selected)}
        editObject={(universe, id, itemType) => ITEM_TYPE_API.updateItemType(universe, id, itemType)}
        createObjects={(universe, objs) => ITEM_TYPE_API.insertAllItemTypes(universe, objs)}
        emptyObject={{
            "name": "",
            typeRestriction: ItemTypeTypeRestrictionEnum.Item
        }}
    />;
}