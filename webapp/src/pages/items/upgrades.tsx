
import { useTranslation } from "react-i18next";
import { ECalculation, EUpgradeEquipmentManipulator, EUpgradeRestriction, Upgrade, UpgradeServiceApi } from "../../api";
import { API_CONFIGURATION } from "../../components/Constants";
import { OverviewBasePage } from "../../components/database/OverviewBasePage";
import { getUniverseContext } from '../../components/PageBase';
import { currencyToHumanReadable } from "../../components/Utils";
import { fetchAllTags } from "../../components/Database";

const UPGRADE_API = new UpgradeServiceApi(API_CONFIGURATION);

/** Page to give an overview over all upgrades */
export function UpgradePage() {
    const { t } = useTranslation();
    const { currencySettings } = getUniverseContext();
    const [tags] = fetchAllTags();

    return <OverviewBasePage<Upgrade>
        columns={[
            { label: t("name"), id: "name", getter: upgrade => upgrade.name },
            { label: t("upgrade:effects"), id: "effects", getter: upgrade => upgrade.effects.map(effect => effect.description).join(", ") },
            { label: t("upgrade:restriction"), id: "restriction", getter: upgrade => upgrade.restriction },
            { label: t("item:tagRequirement"), id: "tagRequirement", getter: upgrade => upgrade.tagRequirement.tagRequirements.map(tags => tags.join(", ")).join(" " + t("or") + " ") },
            { label: t("upgrade:necessary-slots"), id: "slots", getter: upgrade => upgrade.slots, numeric: true },
            { label: t("price"), id: "vendorPrice", getter: upgrade => currencyToHumanReadable(currencySettings, upgrade.vendorPrice) },
        ]}
        fields={[
            { fieldId: "name", label: t("name"), fieldType: "STRING" },
            {
                fieldId: "restriction", label: t("upgrade:restriction"), fieldType: "ENUM", dependency: Object.values(EUpgradeRestriction).map(restriction => {
                    return { key: restriction, content: restriction, label: t(restriction.toLowerCase()) };
                })
            },
            {
                fieldId: "tagRequirement", label: t("upgrade:tagRequirement"), fieldType: "COMPLEX_ENTRY", aligment: "column",
                emptyObject: { tagRequirements: [] },
                subFields: [
                    {
                        fieldId: "tagRequirements", label: t("upgrade:tagRequirement"), fieldType: "COMPLEX_LIST",
                        newListObjectLabel: t("upgrade:addTagRequirement"), tooltip: t("upgrade:tagRequirementTooltip"),
                        emptyObject: [],
                        subFields: [
                            { fieldId: "", label: t("tags"), fieldType: "TAG_LIST", directField: true, dependency: tags }
                        ]
                    }
                ]
            },
            { fieldId: "slots", label: t("upgrade:necessary-slots"), fieldType: "NUMBER" },
            { fieldId: "vendorPrice", label: t("price"), fieldType: "PRICE" },
            {
                fieldId: "effects", label: t("upgrade:effects"), fieldType: "COMPLEX_LIST", aligment: "column",
                emptyObject: { "@type": "SimpleUpgradeEffect", description: "" },
                newListObjectLabel: t("upgrade:addEffect"),
                subFields: [
                    {
                        fieldId: "@type", label: t("upgrade:effectType"), fieldType: "ENUM", dependency: [
                            { key: "SimpleUpgradeEffect", content: "SimpleUpgradeEffect", label: t("upgrade:simpleEffect") },
                            { key: "EquipmentUpgradeEffect", content: "EquipmentUpgradeEffect", label: t("upgrade:equipmentEffect") }
                        ]
                    },
                    {
                        fieldId: "upgradeManipulator", label: t("upgrade:upgradeManipulator"), fieldType: "ENUM",
                        visibleForTypes: ["EquipmentUpgradeEffect"],
                        dependency: Object.values(EUpgradeEquipmentManipulator).map(manipulator => {
                            return { key: manipulator, content: manipulator, label: t("upgrade:" + manipulator.toLowerCase()) };
                        })
                    },
                    {
                        fieldId: "effect-row", label: "", fieldType: "STACK", subFields: [
                            {
                                fieldId: "calculation", label: t("upgrade:calculation"), fieldType: "ENUM",
                                dependency: Object.values(ECalculation).map(manipulator => {
                                    return { key: manipulator, content: manipulator, label: t("upgrade:" + manipulator.toLowerCase()) };
                                })
                            },
                            { fieldId: "value", label: t("value"), fieldType: "NUMBER" }
                        ],
                        visibleForTypes: ["EquipmentUpgradeEffect"]
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
            restriction: EUpgradeRestriction.Item,
            tagRequirement: { tagRequirements: [] },
            slots: 1,
            vendorPrice: 0,
            effects: []
        }}
    />;
}