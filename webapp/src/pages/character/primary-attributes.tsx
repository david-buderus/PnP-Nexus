import { useTranslation } from "react-i18next";
import { PrimaryAttribute, PrimaryAttributeServiceApi } from "../../api";
import { OverviewBasePage } from "../../components/database/OverviewBasePage";
import { API_CONFIGURATION } from "../../components/Constants";

const ATTRIBUTE_API = new PrimaryAttributeServiceApi(API_CONFIGURATION);

/** Page to give an overview over all primary attributes */
export function PrimaryAttributesPage() {
    const { t } = useTranslation();

    return <OverviewBasePage<PrimaryAttribute>
        columns={[
            { label: t("name"), id: "name", getter: attribute => attribute.name },
            { label: t("character:shortName"), id: "shortName", getter: attribute => attribute.shortName }
        ]}
        fields={[
            { fieldId: "name", label: t("name"), fieldType: "STRING" },
            { fieldId: "shortName", label: t("character:shortName"), fieldType: "STRING" },
        ]}
        sortingKey="name"
        creationDialogTitle={t("character:primaryAttributeCreationTitle")}
        editDialogTitle={t("character:primaryAttributeEditTitle")}
        deletionDialogTitle={t("character:primaryAttributeDeletionTitle")}
        fetchObjects={universe => ATTRIBUTE_API.getAllPrimaryAttributes(universe)}
        removeObjects={(universe, selected) => ATTRIBUTE_API.deleteAllPrimaryAttributes(universe, selected)}
        editObject={(universe, id, talent) => ATTRIBUTE_API.updatePrimaryAttribute(universe, id, talent)}
        createObjects={(universe, talents) => ATTRIBUTE_API.insertAllPrimaryAttributes(universe, talents)}
        emptyObject={{
            name: "",
            shortName: ""
        }}
    />;
}

