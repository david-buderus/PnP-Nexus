import { useTranslation } from "react-i18next";
import { CraftingRecipe, CraftingRecipeServiceApi } from "../../api";
import { API_CONFIGURATION, SomeItem } from "../../components/Constants";
import { getUniverseContext } from '../../components/PageBase';
import { OverviewBasePage } from "../../components/database/OverviewBasePage";
import { IResourceUsage, addTypeAnnotationToUsage, fetchAllResources, resourceUsageToString } from "../../components/database/ResourceUsageUtils";
import { fetchAllItems } from "../../components/Database";

const CRAFTING_API = new CraftingRecipeServiceApi(API_CONFIGURATION);

/** Page to give an overview over all crafting recipes */
export function CraftingRecipesPage() {
    const { t } = useTranslation();
    const { activeUniverse } = getUniverseContext();

    const [items] = fetchAllItems();

    const resources: IResourceUsage[] = fetchAllResources(activeUniverse);

    return <OverviewBasePage<CraftingRecipe>
        columns={[
            { label: t("crafting:product"), id: "product", getter: recipe => resourceUsageToString(recipe.product) },
            { label: t("crafting:sideProduct"), id: "sideProduct", getter: recipe => resourceUsageToString(recipe.sideProduct) },
            { label: t("crafting:profession"), id: "profession", getter: recipe => recipe.profession },
            { label: t("crafting:requirement"), id: "requirement", getter: recipe => recipe.requirement },
            { label: t("crafting:otherCircumstances"), id: "otherCircumstances", getter: recipe => recipe.otherCircumstances },
            { label: t("materials"), id: "materials", getter: recipe => recipe.materials.map(resourceUsageToString).join(", ") }
        ]}
        fields={[
            {
                fieldId: "product", label: t("crafting:product"), fieldType: "COMPLEX_ENTRY",
                emptyObject: { amount: 1, item: null },
                subFields: [
                    { fieldId: "amount", label: t("amount"), fieldType: "NUMBER" },
                    { fieldId: "resource", label: t("crafting:product"), fieldType: "DATABASE", dependency: items, dependencyLabel: "name" }
                ]
            },
            {
                fieldId: "sideProduct", label: t("crafting:sideProduct"), fieldType: "COMPLEX_ENTRY",
                emptyObject: { amount: 1, item: null },
                subFields: [
                    { fieldId: "amount", label: t("amount"), fieldType: "NUMBER" },
                    { fieldId: "resource", label: t("crafting:sideProduct"), fieldType: "DATABASE", dependency: items, dependencyLabel: "name" }
                ]
            },
            { fieldId: "profession", label: t("crafting:profession"), fieldType: "STRING" },
            { fieldId: "requirement", label: t("crafting:requirement"), fieldType: "STRING" },
            { fieldId: "otherCircumstances", label: t("crafting:otherCircumstances"), fieldType: "STRING" },
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
        sortingKey="product"
        creationDialogTitle={t("crafting:craftingRecipeCreationTitle")}
        editDialogTitle={t("crafting:craftingRecipeEditTitle")}
        deletionDialogTitle={t("crafting:craftingRecipeDeletionTitle")}
        fetchObjects={universe => CRAFTING_API.getAllCraftingRecipes(universe)}
        removeObjects={(universe, selected) => CRAFTING_API.deleteAllCraftingRecipes(universe, selected)}
        editObject={(universe, id, recipe) => CRAFTING_API.updateCraftingRecipe(universe, id, addTypeAnnotationToRecipe(recipe))}
        createObjects={(universe, recipes) => CRAFTING_API.insertAllCraftingRecipes(universe, recipes.map(addTypeAnnotationToRecipe))}
        emptyObject={{
            product: { amount: 1, resource: null },
            sideProduct: { amount: 0, resource: null },
            profession: "",
            requirement: "",
            otherCircumstances: "",
            materials: [{ amount: 1, resource: null }]
        }}
    />;
}

function addTypeAnnotationToRecipe(recipe: CraftingRecipe): CraftingRecipe {
    return {
        ...recipe,
        product: addTypeAnnotationToUsage(recipe.product),
        sideProduct: recipe.sideProduct === null || recipe.sideProduct.resource === null ? null : addTypeAnnotationToUsage(recipe.sideProduct),
        materials: recipe.materials.map(addTypeAnnotationToUsage)
    };
}
