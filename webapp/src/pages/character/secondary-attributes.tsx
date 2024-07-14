import { useTranslation } from "react-i18next";
import { getUniverseContext } from "../../components/PageBase";
import { SecondaryAttributeDTO, SecondaryAttributeServiceApi, SimpleSecondaryAttributeServiceApi } from "../../api";
import { OverviewBasePage } from "../../components/database/OverviewBasePage";
import { API_CONFIGURATION } from "../../components/Constants";
import { fetchAllPrimaryAttributes } from "../../components/Database";
import { useEffect, useState } from "react";
import { List, ListItem } from "@mui/material";

const SECONDARY_ATTRIBUTE_API = new SecondaryAttributeServiceApi(API_CONFIGURATION);
const SIMPLE_SECONDARY_ATTRIBUTE_API = new SimpleSecondaryAttributeServiceApi(API_CONFIGURATION);

/** Page to give an overview over all secondary attributes */
export function SecondaryAttributesPage() {
    const { t } = useTranslation();
    const { activeUniverse } = getUniverseContext();

    const [supportedVariables, setSupportedVariables] = useState<string[]>([]);

    useEffect(() => {
        if (!activeUniverse) {
            return;
        }
        SIMPLE_SECONDARY_ATTRIBUTE_API.getAllSupportedVariables(activeUniverse.name).then(response => setSupportedVariables(response.data));
    }, [activeUniverse]);

    return <OverviewBasePage<SecondaryAttributeDTO>
        columns={[
            { label: t("name"), id: "name", getter: attribute => attribute.name },
            { label: t("character:consumableAttribute"), id: "consumable", getter: attribute => attribute.consumable ? t("yes") : t("no") },
            { label: t("character:calculationFormula"), id: "calculationFormula", getter: attribute => attribute.calculationFormula }
        ]}
        fields={[
            { fieldId: "name", label: t("name"), fieldType: "STRING" },
            { fieldId: "consumable", label: t("character:consumableAttribute"), fieldType: "BOOLEAN", tooltip: t("character:consumableAttributeTooltip") },
            {
                fieldId: "calculationFormula", label: t("character:calculationFormula"), fieldType: "STRING", tooltip: <>
                    {t("character:calculationFormulaTooltip")}
                    <ul>
                        {supportedVariables.map(v => <li>{"\u2022 " + v}</li>)}
                    </ul>
                </>
            }
        ]}
        sortingKey="name"
        creationDialogTitle={t("character:secondaryAttributeCreationTitle")}
        editDialogTitle={t("character:secondaryAttributeEditTitle")}
        deletionDialogTitle={t("character:secondaryAttributeDeletionTitle")}
        fetchObjects={universe => SIMPLE_SECONDARY_ATTRIBUTE_API.getAllSimpleSecondaryAttributes(universe)}
        removeObjects={(universe, selected) => SECONDARY_ATTRIBUTE_API.deleteAllSecondaryAttributes(universe, selected)}
        editObject={(universe, id, attributes) => SIMPLE_SECONDARY_ATTRIBUTE_API.updateSimpleSecondaryAttribute(universe, id, attributes)}
        createObjects={(universe, attributes) => SIMPLE_SECONDARY_ATTRIBUTE_API.insertAllSimpleSecondaryAttributes(universe, attributes)}
        emptyObject={{
            name: "",
            consumable: false,
            calculationFormula: ""
        }}
    />;
}

