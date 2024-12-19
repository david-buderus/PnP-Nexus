import { useTranslation } from "react-i18next";
import { getUniverseContext } from '../../components/PageBase';
import { Spell, SpellServiceApi, Talent, TalentServiceApi } from "../../api";
import { OverviewBasePage } from "../../components/database/OverviewBasePage";
import { API_CONFIGURATION } from "../../components/Constants";
import { useEffect, useState } from "react";
import { IResourceUsage, addTypeAnnotationToUsage, fetchAllResources, resourceUsageToString } from "../../components/database/ResourceUsageUtils";

const SPELL_API = new SpellServiceApi(API_CONFIGURATION);
const TALENT_API = new TalentServiceApi(API_CONFIGURATION);

/** Page to give an overview over all spells */
export function SpellsPage() {
    const { t } = useTranslation();
    const { activeUniverse } = getUniverseContext();

    const [talents, setTalents] = useState<Talent[]>([]);
    const resources: IResourceUsage[] = fetchAllResources(activeUniverse);

    useEffect(() => {
        if (!activeUniverse) {
            return;
        }
        TALENT_API.getAllTalents(activeUniverse.name).then(response => setTalents(response.data));
    }, [activeUniverse]);

    return <OverviewBasePage<Spell>
        columns={[
            { label: t("name"), id: "name", getter: spell => spell.name },
            { label: t("effect"), id: "effect", getter: spell => spell.effect },
            { label: t("spell:cost"), id: "cost", getter: spell => spell.cost.map(resourceUsageToString).join(", ") },
            { label: t("spell:additionalCost"), id: "cost", getter: spell => spell.additionalCost },
            { label: t("spell:castTime"), id: "castTime", getter: spell => spell.castTime },
            { label: t("talents"), id: "talents", getter: spell => spell.talents.map(talent => talent.name).join(", ") },
            { label: t("tier"), id: "tier", getter: spell => spell.tier, numeric: true },
        ]}
        fields={[
            { fieldId: "name", label: t("name"), fieldType: "STRING" },
            { fieldId: "effect", label: t("effect"), fieldType: "STRING" },
            { fieldId: "castTime", label: t("spell:castTime"), fieldType: "STRING" },
            { fieldId: "talents", label: t("talents"), fieldType: "MULTI_DATABASE", dependency: talents, dependencyLabel: "name" },
            { fieldId: "tier", label: t("tier"), fieldType: "NUMBER" },
            { fieldId: "additionalCost", label: t("spell:additionalCost"), fieldType: "STRING" },
            {
                fieldId: "cost", label: t("spell:cost"), fieldType: "COMPLEX_LIST",
                emptyObject: { amount: 1, resource: null },
                newListObjectLabel: t("spell:addCost"),
                subFields: [
                    { fieldId: "amount", label: t("amount"), fieldType: "NUMBER" },
                    { fieldId: "resource", label: t("crafting:resource"), fieldType: "DATABASE", dependency: resources, dependencyLabel: "name" }
                ]
            }
        ]}
        sortingKey="name"
        creationDialogTitle={t("spell:creationTitle")}
        editDialogTitle={t("crafting:editTitle")}
        deletionDialogTitle={t("crafting:deletionTitle")}
        fetchObjects={universe => SPELL_API.getAllSpells(universe)}
        removeObjects={(universe, selected) => SPELL_API.deleteAllSpells(universe, selected)}
        editObject={(universe, id, spell) => SPELL_API.updateSpell(universe, id, addTypeAnnotationToSpell(spell))}
        createObjects={(universe, spells) => SPELL_API.insertAllSpells(universe, spells.map(addTypeAnnotationToSpell))}
        emptyObject={{
            name: "",
            effect: "",
            cost: [{ amount: 1, resource: null }],
            additionalCost: "",
            castTime: "",
            talents: [],
            tier: 1
        }}
    />;
}

function addTypeAnnotationToSpell(recipe: Spell): Spell {
    return {
        ...recipe,
        cost: recipe.cost.map(addTypeAnnotationToUsage)
    };
}