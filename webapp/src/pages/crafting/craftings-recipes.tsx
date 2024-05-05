import { useTranslation } from "react-i18next";
import { CharacterResourceRecipeEntry, CraftingRecipe, CraftingRecipeServiceApi, Item, ItemRecipeEntry, ItemServiceApi, Material, MaterialRecipeEntry, MaterialServiceApi, SecondaryAttribute, SecondaryAttributeServiceApi } from "../../api";
import { API_CONFIGURATION, SomeItem } from "../../components/Constants";
import { getUniverseContext } from "../../components/PageBase";
import { useEffect, useMemo, useState } from "react";
import { OverviewBasePage } from "../../components/database/OverviewBasePage";

const CRAFTING_API = new CraftingRecipeServiceApi(API_CONFIGURATION);
const ITEM_API = new ItemServiceApi(API_CONFIGURATION);
const MATERIAL_API = new MaterialServiceApi(API_CONFIGURATION);
const ATTRIBUTE_API = new SecondaryAttributeServiceApi(API_CONFIGURATION);

/** Page to give an overview over all materials */
export function CraftingRecipesPage() {
    const { t } = useTranslation();
    const { activeUniverse } = getUniverseContext();

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
            ITEM_API.getAllItems(activeUniverse.name),
            MATERIAL_API.getAllMaterials(activeUniverse.name),
            ATTRIBUTE_API.getAllSecondaryAttributes(activeUniverse.name)
        ]).then(([itemReponse, materialsResponse, attributeResponse]) => {
            setItems(itemReponse.data);
            setMaterials(materialsResponse.data);
            setSecondaryAttributes(attributeResponse.data);
        });
    }, [activeUniverse]);

    return <OverviewBasePage<CraftingRecipe>
        columns={[
            { label: t("crafting:product"), id: "product", getter: recipe => recipeEntryToString(recipe.product) },
            { label: t("crafting:sideProduct"), id: "sideProduct", getter: recipe => recipeEntryToString(recipe.sideProduct) },
            { label: t("crafting:profession"), id: "profession", getter: recipe => recipe.profession },
            { label: t("crafting:requirement"), id: "requirement", getter: recipe => recipe.requirement },
            { label: t("crafting:otherCircumstances"), id: "otherCircumstances", getter: recipe => recipe.otherCircumstances },
            { label: t("materials"), id: "materials", getter: recipe => recipe.materials.map(recipeEntryToString).join(", ") }
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
                emptyObject: { amount: 1, item: null },
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
            materials: []
        }}
    />;
}

function addTypeAnnotationToRecipe(recipe: CraftingRecipe): CraftingRecipe {
    return {
        ...recipe,
        product: addTypeAnnotationToEntry(recipe.product),
        sideProduct: recipe.sideProduct.resource === null ? null : addTypeAnnotationToEntry(recipe.sideProduct),
        materials: recipe.materials.map(addTypeAnnotationToEntry)
    };
}

function addTypeAnnotationToEntry<E extends ItemRecipeEntry | MaterialRecipeEntry | CharacterResourceRecipeEntry>(entry: E): E {
    if (entry === undefined || entry === null) {
        return entry;
    }
    if (entry.resource === undefined || entry.resource === null) {
        return {
            ...entry,
            "@type": "ItemRecipeEntry"
        };
    }
    if ((entry.resource as Item).type !== undefined) {
        return {
            ...entry,
            "@type": "ItemRecipeEntry"
        };
    }
    if ((entry.resource as Material).items !== undefined) {
        return {
            ...entry,
            "@type": "MaterialRecipeEntry"
        };
    }
    if ((entry.resource as SecondaryAttribute).consumable !== undefined) {
        return {
            ...entry,
            "@type": "CharacterResourceRecipeEntry"
        };
    }
    return entry;
}

function recipeEntryToString(entry: ItemRecipeEntry | MaterialRecipeEntry | CharacterResourceRecipeEntry): string {
    if (!entry) {
        return "";
    }
    return entry.amount + " " + entry.resource.name;
}