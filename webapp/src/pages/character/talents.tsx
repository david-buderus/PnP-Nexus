import { useTranslation } from "react-i18next";
import { getUniverseContext } from '../../components/PageBase';
import { PrimaryAttribute, PrimaryAttributeServiceApi, Talent, TalentServiceApi } from "../../api";
import { OverviewBasePage } from "../../components/database/OverviewBasePage";
import { API_CONFIGURATION } from "../../components/Constants";
import { useEffect, useState } from "react";

const TALENT_API = new TalentServiceApi(API_CONFIGURATION);
const ATTRIBUTE_API = new PrimaryAttributeServiceApi(API_CONFIGURATION);

/** Page to give an overview over all talents */
export function TalentsPage() {
    const { t } = useTranslation();
    const { activeUniverse } = getUniverseContext();

    const [attributes, setAttributes] = useState<PrimaryAttribute[]>([]);

    useEffect(() => {
        if (!activeUniverse) {
            return;
        }
        ATTRIBUTE_API.getAllPrimaryAttributes(activeUniverse.name).then(response => setAttributes(response.data));
    }, [activeUniverse]);

    return <OverviewBasePage<Talent>
        columns={[
            { label: t("name"), id: "name", getter: talent => talent.name },
            { label: t("character:group"), id: "group", getter: talent => talent.group },
            { label: t("character:firstAttribute"), id: "firstAttribute", getter: talent => talent.firstAttribute.name },
            { label: t("character:secondAttribute"), id: "secondAttribute", getter: talent => talent.secondAttribute.name },
            { label: t("character:thirdAttribute"), id: "thirdAttribute", getter: talent => talent.thirdAttribute.name },
        ]}
        fields={[
            { fieldId: "name", label: t("name"), fieldType: "STRING" },
            { fieldId: "group", label: t("character:group"), fieldType: "STRING" },
            { fieldId: "firstAttribute", label: t("character:firstAttribute"), fieldType: "DATABASE", dependency: attributes, dependencyLabel: "name" },
            { fieldId: "secondAttribute", label: t("character:secondAttribute"), fieldType: "DATABASE", dependency: attributes, dependencyLabel: "name" },
            { fieldId: "thirdAttribute", label: t("character:thirdAttribute"), fieldType: "DATABASE", dependency: attributes, dependencyLabel: "name" },
        ]}
        sortingKey="name"
        creationDialogTitle={t("character:talentCreationTitle")}
        editDialogTitle={t("character:talentEditTitle")}
        deletionDialogTitle={t("character:talentDeletionTitle")}
        fetchObjects={universe => TALENT_API.getAllTalents(universe)}
        removeObjects={(universe, selected) => TALENT_API.deleteAllTalents(universe, selected)}
        editObject={(universe, id, talent) => TALENT_API.updateTalent(universe, id, talent)}
        createObjects={(universe, talents) => TALENT_API.insertAllTalents(universe, talents)}
        emptyObject={{
            name: "",
            group: "",
            firstAttribute: null,
            secondAttribute: null,
            thirdAttribute: null
        }}
    />;
}

