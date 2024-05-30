import { useTranslation } from "react-i18next";
import { getUniverseContext } from "../../components/PageBase";
import { CharacterResourceRecipeEntry, ItemRecipeEntry, ItemServiceApi, Material, MaterialRecipeEntry, MaterialServiceApi, SecondaryAttribute, SecondaryAttributeServiceApi, Upgrade, UpgradeRecipe, UpgradeRecipeServiceApi, UpgradeServiceApi } from "../../api";
import { useEffect, useMemo, useState } from "react";
import { API_CONFIGURATION, SomeItem } from "../../components/Constants";
import { OverviewBasePage } from "../../components/database/OverviewBasePage";
import { addTypeAnnotationToEntry, recipeEntryToString } from "./crafting-recipes";

const UPGRADE_RECIPE_API = new UpgradeRecipeServiceApi(API_CONFIGURATION);
const UPGRADE_API = new UpgradeServiceApi(API_CONFIGURATION);
const ITEM_API = new ItemServiceApi(API_CONFIGURATION);
const MATERIAL_API = new MaterialServiceApi(API_CONFIGURATION);
const ATTRIBUTE_API = new SecondaryAttributeServiceApi(API_CONFIGURATION);

/** Page to give an overview over all upgrade recipies */
export function UpgradeRecipesPage() {
    const { t } = useTranslation();
    const { activeUniverse } = getUniverseContext();

    const [upgrades, setUpgrades] = useState<Upgrade[]>([]);
    const [items, setItems] = useState<SomeItem[]>([]);
    const [materials, setMaterials] = useState<Material[]>([]);
    const [secondaryAttributes, setSecondaryAttributes] = useState<SecondaryAttribute[]>([]);

    const resources: (CharacterResourceRecipeEntry | ItemRecipeEntry | MaterialRecipeEntry)[] = useMemo(() => {
        return [].concat(items).concat(materials).concat(secondaryAttributes);
    }, [items, materials, secondaryAttributes]);

    useEffect(() => {
        if (!activeUniverse) {
            return;
        }
        Promise.all([
            UPGRADE_API.getAllUpgrades(activeUniverse.name),
            ITEM_API.getAllItems(activeUniverse.name),
            MATERIAL_API.getAllMaterials(activeUniverse.name),
            ATTRIBUTE_API.getAllSecondaryAttributes(activeUniverse.name)
        ]).then(([upgradeResponse, itemReponse, materialsResponse, attributeResponse]) => {
            setUpgrades(upgradeResponse.data);
            setItems(itemReponse.data);
            setMaterials(materialsResponse.data);
            setSecondaryAttributes(attributeResponse.data.filter(attribute => attribute.consumable));
        });
    }, [activeUniverse]);

    return <OverviewBasePage<UpgradeRecipe>
        columns={[
            { label: t("upgrade"), id: "upgrade", getter: recipe => recipe.upgrade.name },
            { label: t("crafting:requirement"), id: "requirement", getter: recipe => recipe.requirement },
            { label: t("crafting:requiredUpgrades"), id: "requiredUpgrades", getter: recipe => recipe.requiredUpgrades.map(upgrade => upgrade.name).join(", ") },
            { label: t("materials"), id: "materials", getter: recipe => recipe.materials.map(recipeEntryToString).join(", ") }
        ]}
        fields={[
            { fieldId: "upgrade", label: t("upgrade"), fieldType: "DATABASE", dependency: upgrades, dependencyLabel: "name" },
            { fieldId: "requirement", label: t("crafting:requirement"), fieldType: "STRING" },
            { fieldId: "requiredUpgrades", label: t("crafting:requiredUpgrades"), fieldType: "MULTI_DATABASE", dependency: upgrades, dependencyLabel: "name" },
            {
                fieldId: "materials", label: t("materials"), fieldType: "COMPLEX_LIST",
                emptyObject: { amount: 1, resource: null },
                newListObjectLabel: t("crafting:addResource"),
                subFields: [
                    { fieldId: "amount", label: t("amount"), fieldType: "NUMBER" },
                    { fieldId: "resource", label: t("crafting:resource"), fieldType: "DATABASE", dependency: resources, dependencyLabel: "name" }
                ]
            }
        ]}
        sortingKey="upgrade"
        creationDialogTitle={t("crafting:upgradeRecipeCreationTitle")}
        editDialogTitle={t("crafting:upgradeRecipeEditTitle")}
        deletionDialogTitle={t("crafting:upgradeRecipeDeletionTitle")}
        fetchObjects={universe => UPGRADE_RECIPE_API.getAllUpgradeRecipes(universe)}
        removeObjects={(universe, selected) => UPGRADE_RECIPE_API.deleteAllUpgradeRecipes(universe, selected)}
        editObject={(universe, id, recipe) => UPGRADE_RECIPE_API.updateUpgradeRecipe(universe, id, addTypeAnnotationToRecipe(recipe))}
        createObjects={(universe, recipes) => UPGRADE_RECIPE_API.insertAllUpgradeRecipes(universe, recipes.map(addTypeAnnotationToRecipe))}
        emptyObject={{
            upgrade: null,
            requirement: "",
            requiredUpgrades: [],
            materials: [{ amount: 1, resource: null }]
        }}
    />;
}

function addTypeAnnotationToRecipe(recipe: UpgradeRecipe): UpgradeRecipe {
    return {
        ...recipe,
        materials: recipe.materials.map(addTypeAnnotationToEntry)
    };
}