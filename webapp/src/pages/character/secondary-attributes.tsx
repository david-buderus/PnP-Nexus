import { useTranslation } from "react-i18next";
import { getUniverseContext } from "../../components/PageBase";
import { PrimaryAttribute, PrimaryAttributeServiceApi, SecondaryAttribute, SecondaryAttributeServiceApi } from "../../api";
import { OverviewBasePage } from "../../components/database/OverviewBasePage";
import { API_CONFIGURATION } from "../../components/Constants";
import { useEffect, useState } from "react";

const PRIMARY_ATTRIBUTE_API = new PrimaryAttributeServiceApi(API_CONFIGURATION);
const SECONDARY_ATTRIBUTE_API = new SecondaryAttributeServiceApi(API_CONFIGURATION);

export function SecondaryAttributesPage() {
    const { t } = useTranslation();
    const { activeUniverse } = getUniverseContext();

    const [primaryAttributes, setPrimaryAttributes] = useState<PrimaryAttribute[]>([]);

    useEffect(() => {
        if (!activeUniverse) {
            return;
        }
        PRIMARY_ATTRIBUTE_API.getAllPrimaryAttributes(activeUniverse.name).then(response => setPrimaryAttributes(response.data));
    }, [activeUniverse]);

    return <OverviewBasePage<SecondaryAttribute>
        columns={[
            { label: t("name"), id: "name", getter: attribute => attribute.name },
            { label: t("character:consumableAttribute"), id: "consumable", getter: attribute => attribute.consumable ? t("yes") : t("no") },
            { label: t("character:primaryAttributeDependencies"), id: "primaryAttributeDependencies", getter: attribute => attribute.primaryAttributeDependencies.map(dep => dep.factor + " " + dep.primaryAttribute.shortName).join(" + ") }
        ]}
        fields={[
            { fieldId: "name", label: t("name"), fieldType: "STRING" },
            { fieldId: "consumable", label: t("character:consumableAttribute"), fieldType: "BOOLEAN", tooltip: t("character:consumableAttributeTooltip") },
            {
                fieldId: "primaryAttributeDependencies", label: t("character:primaryAttributeDependencies"), fieldType: "COMPLEX_LIST",
                emptyObject: { factor: 1, primaryAttribute: null },
                newListObjectLabel: t("character:addPrimaryAttribute"),
                subFields: [
                    { fieldId: "factor", label: t("factor"), fieldType: "NUMBER" },
                    { fieldId: "primaryAttribute", label: t("primary-attribute"), fieldType: "DATABASE", dependency: primaryAttributes, dependencyLabel: "name" }
                ]
            }
        ]}
        sortingKey="name"
        creationDialogTitle={t("character:secondaryAttributeCreationTitle")}
        editDialogTitle={t("character:secondaryAttributeEditTitle")}
        deletionDialogTitle={t("character:secondaryAttributeDeletionTitle")}
        fetchObjects={universe => SECONDARY_ATTRIBUTE_API.getAllSecondaryAttributes(universe)}
        removeObjects={(universe, selected) => SECONDARY_ATTRIBUTE_API.deleteAllSecondaryAttributes(universe, selected)}
        editObject={(universe, id, attributes) => SECONDARY_ATTRIBUTE_API.updateSecondaryAttribute(universe, id, attributes)}
        createObjects={(universe, attributes) => SECONDARY_ATTRIBUTE_API.insertAllSecondaryAttributes(universe, attributes)}
        emptyObject={{
            name: "",
            consumable: false,
            primaryAttributeDependencies: [{ factor: 1, primaryAttribute: null }]
        }}
    />;
}

