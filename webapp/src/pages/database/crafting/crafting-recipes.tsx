import {
    ActionIcon,
    Button,
    Group,
    Input,
    Modal,
    NumberInput,
    Paper,
    Stack,
    Text,
    TextInput,
    Tooltip
} from "@mantine/core";
import {useForm} from "@mantine/form";
import {randomId, useDisclosure} from "@mantine/hooks";
import {useEffect, useMemo} from "react";
import {useTranslation} from "react-i18next";
import {CraftingRecipe, CraftingRecipeServiceApi, Item, ItemUsage, Material, SecondaryAttribute} from "../../../api";
import {fetchAllCraftingRecipes, IResourceUsage} from "../../../components/Database";
import OverviewPage, {ExtendedColumnDef} from "../../../components/OverviewPage";
import {useUniverseContext} from "../../../components/PageBase";
import {handleDatabaseInsertErrors, handleValidationErrors} from "../../../components/utils/ErrorUtils";
import {API_CONFIGURATION} from "../../../components/Constants";
import {FaRegTrashCan} from "react-icons/fa6";
import {ItemSelect, ResourceSelect} from "../../../components/input/ObjectSelect";
import {resourceFormatter} from "../../../components/utils/Formatters";

const CRAFTING_API = new CraftingRecipeServiceApi(API_CONFIGURATION);

/** Overview over all crafting recipes */
export function CraftingRecipeOverview() {
    const {t} = useTranslation();

    const columns = useMemo<ExtendedColumnDef<CraftingRecipe, any>[]>(
        () => [
            {
                accessorKey: 'products',
                header: t("crafting:products"),
                Cell: cell => {
                    const items = cell.cell.getValue<ItemUsage[]>();
                    return items.map(resourceFormatter).join(", ");
                },
                filterFn: (row, id, filterValue) => {
                    return row.getValue<ItemUsage[]>(id).some(item => item.resource?.name.includes(filterValue));
                }
            },
            {
                accessorKey: 'profession',
                header: t("crafting:profession")
            },
            {
                accessorKey: 'requirement',
                header: t("crafting:requirement")
            },
            {
                accessorKey: 'otherCircumstances',
                header: t("crafting:otherCircumstances")
            },
            {
                accessorKey: 'materials',
                header: t("materials"),
                Cell: cell => {
                    const items = cell.cell.getValue<IResourceUsage[]>();
                    return items.map(resourceFormatter).join(", ");
                },
                filterFn: (row, id, filterValue) => {
                    return row.getValue<IResourceUsage[]>(id).some(item => item.resource?.name.includes(filterValue));
                }
            }
        ], []);

    return <OverviewPage
        fetchData={fetchAllCraftingRecipes()}
        columns={columns}
        identifier="crafting-recipes"
        manipulationDialog={(editMode, refresh, disabled, getInitial) =>
            <CreationDialog
                editMode={editMode}
                refresh={refresh}
                disabled={disabled}
                getInitial={getInitial}
            />}
        deletionDialogTitle={t("crafting:craftingRecipeDeletionTitle")}
        onDelete={(universe, recipes) => CRAFTING_API.deleteAllCraftingRecipes(universe, recipes.map(recipe => recipe.id))}
        idKey="id"
    />;
}

function CreationDialog({
    editMode,
    refresh,
    disabled,
    getInitial
}: {
    editMode: boolean,
    refresh: () => void;
    disabled: boolean;
    getInitial: () => CraftingRecipe;
}) {
    const {t} = useTranslation();
    const {activeUniverse} = useUniverseContext();

    const [opened, {open, close}] = useDisclosure(false);
    const form = useForm<CraftingRecipe>({
        mode: 'controlled',
        initialValues: {
            products: [{
                amount: 1,
                resource: null
            }],
            materials: [{
                amount: 1,
                resource: null
            }],
            otherCircumstances: "",
            profession: "",
            requirement: ""
        }
    });

    useEffect(() => {
        if (!editMode || !opened) {
            return;
        }
        form.setValues(getInitial());
    }, [opened, getInitial, editMode]);

    function onSubmit(recipe: CraftingRecipe) {
        if (editMode) {
            CRAFTING_API.updateCraftingRecipe(activeUniverse.name, recipe.id, addTypeAnnotationToRecipe(recipe)).then(refresh).then(close)
                .catch(handleValidationErrors(form.setErrors));
        } else {
            CRAFTING_API.insertAllCraftingRecipes(activeUniverse.name, [addTypeAnnotationToRecipe(recipe)]).then(refresh).then(close)
                .catch(handleValidationErrors(handleDatabaseInsertErrors(form.setErrors)));
        }
    }

    return <>
        <Modal opened={opened} onClose={close}
               title={editMode ? t("crafting:craftingRecipeEditTitle") : t("crafting:craftingRecipeCreationTitle")}
               maw={300}>
            <form onSubmit={form.onSubmit(onSubmit)}>
                <Input.Label>
                    {t("crafting:products")}
                </Input.Label>
                <Paper shadow="md" p="sm">
                    {form.getValues().products.length > 0 ? (
                        <Group>
                            <Text fw={500} size="sm" style={{flex: 1}} pr={50}>
                                {t("amount")}
                            </Text>
                            <Text fw={500} size="sm" pr={195}>
                                {t("item")}
                            </Text>
                        </Group>
                    ) : (
                        <Text c="dimmed" ta="center">
                            {t("nothing-here")}
                        </Text>
                    )}
                    <Stack gap="xs">
                        {form.getValues().products.map((resource, index) => {
                            if (!resource["key"]) {
                                resource["key"] = randomId();
                            }

                            return <Group key={"product-" + resource["key"]} wrap="nowrap" align="flex-start">
                                <NumberInput
                                    key={form.key(`products.${index}.amount`)}
                                    {...form.getInputProps(`products.${index}.amount`)}
                                />
                                <ItemSelect
                                    key={form.key(`products.${index}.resource`)}
                                    {...form.getInputProps(`products.${index}.resource`)}
                                />
                                <ActionIcon variant="outline" color="red" size="input-sm"
                                            onClick={() => form.removeListItem('products', index)}>
                                    <FaRegTrashCan/>
                                </ActionIcon>
                            </Group>;
                        })}
                    </Stack>
                    <Tooltip label={form.errors["products"]} disabled={!form.errors["products"]}>
                        <Button
                            onClick={() =>
                                form.insertListItem('products', {
                                    amount: 1,
                                    resource: null,
                                    key: randomId()
                                })
                            }
                            mt="md"
                            color={form.errors["products"] ? "red" : undefined}
                        >
                            {t("crafting:addProduct")}
                        </Button>
                    </Tooltip>
                </Paper>
                <TextInput
                    mt="sm"
                    label={t("crafting:profession")}
                    key={form.key('profession')}
                    {...form.getInputProps('profession')}
                />
                <TextInput
                    label={t("crafting:requirement")}
                    key={form.key('requirement')}
                    {...form.getInputProps('requirement')}
                />
                <TextInput
                    label={t("crafting:otherCircumstances")}
                    key={form.key('otherCircumstances')}
                    {...form.getInputProps('otherCircumstances')}
                />
                <Input.Label>
                    {t("materials")}
                </Input.Label>
                <Paper shadow="md" p="sm">
                    {form.getValues().materials.length > 0 ? (
                        <Group>
                            <Text fw={500} size="sm" style={{flex: 1}} pr={50}>
                                {t("amount")}
                            </Text>
                            <Text fw={500} size="sm" pr={160}>
                                {t("crafting:resource")}
                            </Text>
                        </Group>
                    ) : (
                        <Text c="dimmed" ta="center">
                            {t("nothing-here")}
                        </Text>
                    )}
                    <Stack gap="xs">
                        {form.getValues().materials.map((resource, index) => {
                            if (!resource["key"]) {
                                resource["key"] = randomId();
                            }

                            return <Group key={"material-" + resource["key"]} wrap="nowrap" align="flex-start">
                                <NumberInput
                                    key={form.key(`materials.${index}.amount`)}
                                    {...form.getInputProps(`materials.${index}.amount`)}
                                />
                                <ResourceSelect
                                    key={form.key(`materials.${index}.resource`)}
                                    {...form.getInputProps(`materials.${index}.resource`)}
                                />
                                <ActionIcon variant="outline" color="red" size="input-sm"
                                            onClick={() => form.removeListItem('materials', index)}>
                                    <FaRegTrashCan/>
                                </ActionIcon>
                            </Group>;
                        })}
                    </Stack>
                    <Tooltip label={form.errors["materials"]} disabled={!form.errors["materials"]}>
                        <Button
                            onClick={() =>
                                form.insertListItem('materials', {
                                    amount: 1,
                                    resource: null,
                                    key: randomId()
                                })
                            }
                            mt="md"
                            color={form.errors["materials"] ? "red" : undefined}
                        >
                            {t("crafting:addResource")}
                        </Button>
                    </Tooltip>
                </Paper>
                <Group justify="flex-end" mt="md">
                    <Button autoFocus variant="outline" onClick={close}>
                        {t("cancel")}
                    </Button>
                    <Button type="submit">
                        {editMode ? t("edit") : t("add")}
                    </Button>
                </Group>
            </form>
        </Modal>
        <Button data-testid={editMode ? "edit" : "add"} onClick={open} disabled={disabled}>
            {editMode ? t("edit") : t("add")}
        </Button>
    </>;
}

function addTypeAnnotationToRecipe(recipe: CraftingRecipe): CraftingRecipe {
    return {
        ...recipe,
        products: recipe.products.map(addTypeAnnotationToUsage),
        materials: recipe.materials.map(addTypeAnnotationToUsage)
    };
}

/** Adds the necessary types to the usage */
export function addTypeAnnotationToUsage<E extends IResourceUsage>(usage: E): E {
    if (usage === undefined || usage === null) {
        return usage;
    }
    if (usage.resource === undefined || usage.resource === null) {
        return {
            ...usage,
            "@type": "ItemUsage"
        };
    }
    if ((usage.resource as Item).rarity !== undefined) {
        return {
            ...usage,
            "@type": "ItemUsage"
        };
    }
    if ((usage.resource as Material).items !== undefined) {
        return {
            ...usage,
            "@type": "MaterialUsage"
        };
    }
    if ((usage.resource as SecondaryAttribute).consumable !== undefined) {
        return {
            ...usage,
            "@type": "CharacterResourceUsage"
        };
    }
    return usage;
}