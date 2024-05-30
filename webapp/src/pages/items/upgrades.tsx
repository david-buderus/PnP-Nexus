
import { useTranslation } from "react-i18next";
import { ItemType, ItemTypeServiceApi, Upgrade, UpgradeEffectUpgradeManipulatorEnum, UpgradeServiceApi } from "../../api";
import { API_CONFIGURATION } from "../../components/Constants";
import { OverviewBasePage } from "../../components/database/OverviewBasePage";
import { useEffect, useState } from "react";
import { getUniverseContext } from "../../components/PageBase";
import { currencyToHumanReadable } from "../../components/Utils";

const ITEM_TYPE_API = new ItemTypeServiceApi(API_CONFIGURATION);
const UPGRADE_API = new UpgradeServiceApi(API_CONFIGURATION);

/** Page to give an overview over all upgrades */
export function UpgradePage() {
    const { t } = useTranslation();
    const { activeUniverse } = getUniverseContext();

    const [itemTypes, setItemTypes] = useState<ItemType[]>([]);

    useEffect(() => {
        if (activeUniverse) {
            ITEM_TYPE_API.getAllItemTypes(activeUniverse.name).then(response => setItemTypes(response.data));
        }
    }, [activeUniverse]);

    return <OverviewBasePage<Upgrade>
        columns={[
            { label: t("name"), id: "name", getter: upgrade => upgrade.name },
            { label: t("upgrade:effects"), id: "effects", getter: upgrade => upgrade.effects.map(effect => effect.description).join(", ") },
            { label: t("upgrade:target"), id: "target", getter: upgrade => upgrade.target.name },
            { label: t("upgrade:necessary-slots"), id: "slots", getter: upgrade => upgrade.slots, numeric: true },
            { label: t("price"), id: "vendorPrice", getter: upgrade => currencyToHumanReadable(activeUniverse, upgrade.vendorPrice) },
        ]}
        fields={[
            { fieldId: "name", label: t("name"), fieldType: "STRING" },
            { fieldId: "target", label: t("upgrade:target"), fieldType: "DATABASE", dependency: itemTypes, dependencyLabel: "name" },
            { fieldId: "slots", label: t("upgrade:necessary-slots"), fieldType: "NUMBER" },
            { fieldId: "vendorPrice", label: t("price"), fieldType: "PRICE" },
            {
                fieldId: "effects", label: t("upgrade:effects"), fieldType: "COMPLEX_LIST", aligment: "column",
                emptyObject: { "@type": "SimpleUpgradeEffect", description: "", upgradeManipulator: UpgradeEffectUpgradeManipulatorEnum.None },
                newListObjectLabel: t("upgrade:addEffect"),
                subFields: [
                    {
                        fieldId: "@type", label: t("upgrade:effectType"), fieldType: "ENUM", dependency: [
                            { key: "SimpleUpgradeEffect", content: "SimpleUpgradeEffect", label: t("upgrade:simpleEffect") },
                            { key: "AdditiveUpgradeEffect", content: "AdditiveUpgradeEffect", label: t("upgrade:additiveEffect") },
                            { key: "MultiplicativeUpgradeEffect", content: "MultiplicativeUpgradeEffect", label: t("upgrade:multiplicativeEffect") },
                        ]
                    },
                    {
                        fieldId: "effect-row", label: "", fieldType: "STACK", subFields: [
                            { fieldId: "value", label: t("value"), fieldType: "NUMBER", visibleForTypes: ["AdditiveUpgradeEffect"] },
                            { fieldId: "factor", label: t("factor"), fieldType: "NUMBER", visibleForTypes: ["MultiplicativeUpgradeEffect"] },
                            {
                                fieldId: "upgradeManipulator", label: t("upgrade:upgradeManipulator"), fieldType: "ENUM",
                                visibleForTypes: ["AdditiveUpgradeEffect", "MultiplicativeUpgradeEffect"],
                                dependency: Object.values(UpgradeEffectUpgradeManipulatorEnum).map(manipulator => {
                                    return { key: manipulator, content: manipulator, label: t("upgrade:" + manipulator.toLowerCase()) };
                                })
                            }
                        ],
                        visibleForTypes: ["AdditiveUpgradeEffect", "MultiplicativeUpgradeEffect"]
                    },
                    { fieldId: "description", label: t("description"), fieldType: "STRING" }
                ]
            }
        ]}
        sortingKey="name"
        creationDialogTitle={t("upgrade:upgradeCreationTitle")}
        editDialogTitle={t("upgrade:upgradeEditTitle")}
        deletionDialogTitle={t("upgrade:upgradeDeletionTitle")}
        fetchObjects={universe => UPGRADE_API.getAllUpgrades(universe)}
        removeObjects={(universe, selected) => UPGRADE_API.deleteAllUpgrades(universe, selected)}
        editObject={(universe, id, itemType) => UPGRADE_API.updateUpgrade(universe, id, itemType)}
        createObjects={(universe, objs) => UPGRADE_API.insertAllUpgrades(universe, objs)}
        emptyObject={{
            name: "",
            target: null,
            slots: 1,
            vendorPrice: 0,
            effects: []
        }}
    />;
}