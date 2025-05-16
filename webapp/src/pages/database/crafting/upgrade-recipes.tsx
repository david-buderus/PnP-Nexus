import { Modal, TextInput, Group, Button, NumberInput, Text, ActionIcon, Input, Stack, Paper, Tooltip } from "@mantine/core";
import { useForm } from "@mantine/form";
import { randomId, useDisclosure } from "@mantine/hooks";
import { useMemo, useEffect } from "react";
import { useTranslation } from "react-i18next";
import { Upgrade, UpgradeRecipe, UpgradeRecipeServiceApi } from "../../../api";
import { fetchAllUpgradeRecipes, fetchAllUpgrades, IResourceUsage } from "../../../components/Database";
import OverviewPage, { ExtendedColumnDef } from "../../../components/OverviewPage";
import { useUniverseContext } from "../../../components/PageBase";
import { handleValidationErrors, handleDatabaseInsertErrors } from "../../../components/utils/ErrorUtils";
import { API_CONFIGURATION } from "../../../components/Constants";
import { FaRegTrashCan } from "react-icons/fa6";
import { ObjectMultiSelect, ObjectSelect, ResourceSelect } from "../../../components/input/ObjectSelect";
import { resourceFormatter } from "../../../components/utils/Formatters";
import { addTypeAnnotationToUsage } from "./crafting-recipes";

const UPGRADE_RECIPE_API = new UpgradeRecipeServiceApi(API_CONFIGURATION);

/** Overview over all upgrade recipes */
export function UpgradeRecipeOverview() {
    const { t } = useTranslation();

    const columns = useMemo<ExtendedColumnDef<UpgradeRecipe, any>[]>(
        () => [
            {
                accessorKey: 'upgrade',
                header: t("upgrade"),
                Cell: (cell) => {
                    return cell.cell.getValue<Upgrade>()?.name;
                },
                filterFn: (row, id, filterValue) => {
                    return row.getValue<Upgrade>(id)?.name.includes(filterValue);
                }
            },
            {
                accessorKey: 'requirement',
                header: t("crafting:requirement")
            },
            {
                accessorKey: 'requiredUpgrades',
                header: t("crafting:requiredUpgrades"),
                Cell: (cell) => {
                    return cell.cell.getValue<Upgrade[]>().map(upgrade => upgrade?.name).join(", ");
                },
                filterFn: (row, id, filterValue) => {
                    return row.getValue<Upgrade[]>(id).some(upgrade => upgrade?.name.includes(filterValue));
                }
            },
            {
                accessorKey: 'materials',
                header: t("materials"),
                Cell: (cell) => {
                    const items = cell.cell.getValue<IResourceUsage[]>();
                    return items.map(resourceFormatter).join(", ");
                },
                filterFn: (row, id, filterValue) => {
                    return row.getValue<IResourceUsage[]>(id).some(item => item.resource?.name.includes(filterValue));
                }
            }
        ], []);

    return <OverviewPage
        fetchData={fetchAllUpgradeRecipes()}
        columns={columns}
        identifier="upgrade-recipes"
        manipulationDialog={(editMode, refresh, disabled, getInitial) => <CreationDialog editMode={editMode} refresh={refresh} disabled={disabled} getInitial={getInitial} />}
        deletionDialogTitle={t("crafting:upgradeRecipeDeletionTitle")}
        onDelete={(universe, recipes) => UPGRADE_RECIPE_API.deleteAllUpgradeRecipes(universe, recipes.map(recipe => recipe.id))}
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
    getInitial: () => UpgradeRecipe;
}) {
    const { t } = useTranslation();
    const { activeUniverse } = useUniverseContext();
    const [upgrades] = fetchAllUpgrades();

    const [opened, { open, close }] = useDisclosure(false);
    const form = useForm<UpgradeRecipe>({
        mode: 'controlled',
        initialValues: {
            upgrade: null,
            materials: [{
                amount: 1,
                resource: null
            }],
            requirement: "",
            requiredUpgrades: []
        }
    });

    useEffect(() => {
        if (!editMode || !opened) {
            return;
        }
        form.setValues(getInitial());
    }, [opened, getInitial, editMode]);

    function onSubmit(recipe: UpgradeRecipe) {
        if (editMode) {
            UPGRADE_RECIPE_API.updateUpgradeRecipe(activeUniverse.name, recipe.id, addTypeAnnotationToRecipe(recipe)).then(refresh).then(close)
                .catch(handleValidationErrors(form.setErrors));
        } else {
            UPGRADE_RECIPE_API.insertAllUpgradeRecipes(activeUniverse.name, [addTypeAnnotationToRecipe(recipe)]).then(refresh).then(close)
                .catch(handleValidationErrors(handleDatabaseInsertErrors(form.setErrors)));
        }
    }

    return <>
        <Modal opened={opened} onClose={close} title={editMode ? t("crafting:upgradeRecipeEditTitle") : t("crafting:upgradeRecipeCreationTitle")} maw={300}>
            <form onSubmit={form.onSubmit(onSubmit)}>
                <ObjectSelect<Upgrade>
                    label={t("upgrade")}
                    key={form.key('upgrade')}
                    {...form.getInputProps('upgrade')}
                    data={upgrades}
                    idKey="id"
                    labelKey="name"
                />
                <TextInput
                    label={t("crafting:requirement")}
                    key={form.key('requirement')}
                    {...form.getInputProps('requirement')}
                />
                <ObjectMultiSelect<Upgrade>
                    label={t("crafting:requiredUpgrades")}
                    key={form.key('requiredUpgrades')}
                    {...form.getInputProps('requiredUpgrades')}
                    data={upgrades}
                    idKey="id"
                    labelKey="name"
                />
                <Input.Label>
                    {t("materials")}
                </Input.Label>
                <Paper shadow="md" p="sm">
                    {form.getValues().materials.length > 0 ? (
                        <Group>
                            <Text fw={500} size="sm" style={{ flex: 1 }} pr={50}>
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
                                <ActionIcon variant="outline" color="red" size="input-sm" onClick={() => form.removeListItem('materials', index)}>
                                    <FaRegTrashCan />
                                </ActionIcon>
                            </Group>;
                        })}
                    </Stack>
                    <Tooltip label={form.errors["materials"]}>
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

function addTypeAnnotationToRecipe(recipe: UpgradeRecipe): UpgradeRecipe {
    return {
        ...recipe,
        materials: recipe.materials.map(addTypeAnnotationToUsage)
    };
}