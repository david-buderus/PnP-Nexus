import { useTranslation } from "react-i18next";
import { ItemServiceApi, Material, MaterialServiceApi } from "../../api";
import { API_CONFIGURATION, SomeItem } from "../../components/Constants";
import { OverviewBasePage } from "../../components/OverviewBasePage";
import { useEffect, useState } from "react";
import { getUniverseContext } from "../../components/PageBase";

const ITEM_API = new ItemServiceApi(API_CONFIGURATION);
const MATERIAL_API = new MaterialServiceApi(API_CONFIGURATION);

/** Page to give an overview over all materials */
export function MaterialPage() {
    const { t } = useTranslation();
    const { activeUniverse } = getUniverseContext();

    const [items, setItems] = useState<SomeItem[]>([]);

    useEffect(() => {
        if (activeUniverse) {
            ITEM_API.getAllItems(activeUniverse.name).then(response => setItems(response.data));
        }
    }, [activeUniverse]);

    return <OverviewBasePage<Material>
        columns={[
            { label: t("name"), id: "name", getter: itemType => itemType.name },
            { label: t("items"), id: "items", getter: itemType => itemType.items.map(entry => entry.amount + " " + entry.item.name).join(", ") },
        ]}
        fields={[
            { fieldId: "name", label: t("name"), fieldType: "STRING" },
            {
                fieldId: "items", label: t("items"), fieldType: "COMPLEX_LIST",
                emptyObject: { amount: 1, item: null },
                newListObjectLabel: t("item:addItem"),
                subFields: [
                    { fieldId: "amount", label: t("amount"), fieldType: "NUMBER" },
                    { fieldId: "item", label: t("item"), fieldType: "DATABASE", dependency: items, dependencyLabel: "name" }
                ]
            }
        ]}
        sortingKey="name"
        creationDialogTitle={t("item:materialCreationTitle")}
        editDialogTitle={t("item:materialEditTitle")}
        deletionDialogTitle={t("item:materialDeletionTitle")}
        fetchObjects={universe => MATERIAL_API.getAllMaterials(universe)}
        removeObjects={(universe, selected) => MATERIAL_API.deleteAllMaterials(universe, selected)}
        editObject={(universe, id, itemType) => MATERIAL_API.updateMaterial(universe, id, itemType)}
        createObjects={(universe, objs) => MATERIAL_API.insertAllMaterials(universe, objs)}
        emptyObject={{
            name: "",
            items: []
        }}
    />;
}