import {
    ActionIcon,
    Button,
    Grid,
    Group,
    List,
    NumberInput,
    Paper,
    Stack,
    Switch,
    Table,
    Text,
    TextInput,
    Tooltip
} from "@mantine/core";
import {useForm} from "@mantine/form";
import {randomId} from "@mantine/hooks";
import {ReactNode, useEffect, useState} from "react";
import {useTranslation} from "react-i18next";
import {FaRegTrashCan} from "react-icons/fa6";
import {
    BinaryExpressionTreeServiceApi,
    SecondaryAttributeDTO,
    SecondaryAttributeInfo,
    SimpleSecondaryAttributeServiceApi,
    UniverseCreationServiceApi
} from "../../api";
import {fetchAllPrimaryAttributes, fetchSupportedSecondaryAttributeVariables} from "../Database";
import {useUniverseContext} from "../PageBase";
import {handleNetworkErrors, handleValidationErrors} from "../utils/ErrorUtils";
import {API_CONFIGURATION} from "../Constants";


const SIMPLE_SECONDARY_ATTRIBUTE_API = new SimpleSecondaryAttributeServiceApi(API_CONFIGURATION);
const EXPRESSION_API = new BinaryExpressionTreeServiceApi(API_CONFIGURATION);
const UNIVERSE_CREATION_API = new UniverseCreationServiceApi(API_CONFIGURATION);

/** A form to adjust all secondary attributes */
export function SecondaryAttributeForm({
    onSave, onSaveText, alternativeButton
}: {
    onSave: () => void;
    onSaveText: string;
    alternativeButton?: ReactNode;
}) {
    const {t} = useTranslation();
    const {activeUniverse, characterSettings} = useUniverseContext();

    const [primaryAttributes] = fetchAllPrimaryAttributes();
    const [supportedVariables] = fetchSupportedSecondaryAttributeVariables();
    const [attributeInfos, setAttributeInfos] = useState<SecondaryAttributeInfo[]>([]);
    const [loadingAttributeInfos, setLoadingAttributeInfos] = useState(false);

    const [primaryValues, setPrimaryValues] = useState<Map<string, number>>(new Map<string, number>());
    const [calculatedAttributeValues, setCalculatedAttributeValues] = useState<number[]>([]);

    const form = useForm<{
        attributes: (SecondaryAttributeDTO & { key: string; })[];
    }>({
        mode: 'controlled',
        initialValues: {
            attributes: Array(8).fill({name: "", consumable: false, calculationFormula: ""}).map(a => {
                return {...a, key: randomId()};
            })
        }
    });

    useEffect(() => {
        if (!activeUniverse) {
            return;
        }
        SIMPLE_SECONDARY_ATTRIBUTE_API.getAllSimpleSecondaryAttributes(activeUniverse.id).then(response => {
            if (response.data.length > 0) {
                form.setValues({
                    attributes: response.data.map(a => ({
                        ...a,
                        key: randomId()
                    }))
                });
            }
        }).catch(handleNetworkErrors);
    }, [activeUniverse]);

    useEffect(() => {
        if (!activeUniverse) {
            return;
        }
        const calculate = setTimeout(() => {
            EXPRESSION_API.calculateResults({
                formulas: form.getValues().attributes.map(att => att.calculationFormula),
                constants: Object.fromEntries(primaryValues)
            }).then(response => setCalculatedAttributeValues(response.data));
        }, 200);

        return () => clearTimeout(calculate);
    }, [activeUniverse, form.getValues().attributes, primaryValues]);

    return <Grid>
        <Grid.Col span="content">
            <form
                onSubmit={form.onSubmit((attributes) => SIMPLE_SECONDARY_ATTRIBUTE_API.setAllSimpleSecondaryAttributes(activeUniverse.id, attributes.attributes)
                    .then(onSave).catch(handleValidationErrors(form.setErrors))
                )}>
                <Table>
                    <Table.Thead>
                        <Table.Tr>
                            <Table.Th>{t("name")}</Table.Th>
                            <Table.Th>{t("character:shortName")}</Table.Th>
                            <Table.Th>{t("character:calculationFormula")}</Table.Th>
                            <Table.Th colSpan={2} style={{width: 150}}>{t("character:consumableAttribute")}</Table.Th>
                        </Table.Tr>
                    </Table.Thead>
                    <Table.Tbody>
                        {form.getValues().attributes.map((attribute, index) => {
                            return <Table.Tr key={attribute.key}>
                                <Table.Td>
                                    <TextInput
                                        key={form.key(`attributes.${index}.name`)}
                                        {...form.getInputProps(`attributes.${index}.name`)}
                                        required
                                    />
                                </Table.Td>
                                <Table.Td>
                                    <TextInput
                                        key={form.key(`attributes.${index}.shortName`)}
                                        {...form.getInputProps(`attributes.${index}.shortName`)}
                                        required
                                    />
                                </Table.Td>
                                <Table.Td>
                                    <Tooltip label={<>
                                        {t("character:calculationFormulaTooltip")}
                                        <List>
                                            {supportedVariables.map(v => <List.Item
                                                key={"tooltip-supported-variables-" + index + "-" + v}>{v}</List.Item>)}
                                        </List>
                                    </>} key={form.key(`attributes.${index}.consumable`) + "-calculationFormula"}>
                                        <TextInput
                                            key={form.key(`attributes.${index}.calculationFormula`)}
                                            required
                                            {...form.getInputProps(`attributes.${index}.calculationFormula`)}
                                        />
                                    </Tooltip>
                                </Table.Td>
                                <Table.Td>
                                    <Tooltip label={t("character:consumableAttributeTooltip")}
                                             key={form.key(`attributes.${index}.consumable`) + "-tooltip"}>
                                        <div>
                                            <Switch
                                                key={form.key(`attributes.${index}.consumable`)}
                                                {...form.getInputProps(`attributes.${index}.consumable`, {type: 'checkbox'})}
                                            />
                                        </div>
                                    </Tooltip>
                                </Table.Td>
                                <Table.Td>
                                    <ActionIcon variant="outline" size="lg" color="red"
                                                onClick={() => form.removeListItem('attributes', index)}>
                                        <FaRegTrashCan size={16}/>
                                    </ActionIcon>
                                </Table.Td>
                            </Table.Tr>;
                        })}
                    </Table.Tbody>
                    {form.getValues().attributes.length === 0 ?
                        <Table.Caption c="dimmed" ta="center">
                            {t("nothing-here")}
                        </Table.Caption> : null}
                </Table>
                <Button
                    mt="md"
                    onClick={() =>
                        form.insertListItem('attributes', {
                            name: '',
                            calculationFormula: '',
                            consumable: false,
                            key: randomId()
                        })
                    }
                >
                    {t("universe:addAnotherAttribute")}
                </Button>

                <Group justify="flex-end" pt="md">
                    {alternativeButton}
                    <Button type="submit">
                        {onSaveText}
                    </Button>
                </Group>
            </form>
        </Grid.Col>
        <Grid.Col span="content">
            <Paper shadow="md" p="md">
                <Stack>
                    <Text ta="left">
                        {t("universe:secondaryAttributeExplanation")}
                    </Text>
                    <Table>
                        <Table.Thead>
                            <Table.Tr>
                                <Table.Th>{t("secondary-attribute")}</Table.Th>
                                <Table.Th>Min</Table.Th>
                                <Table.Th>Max</Table.Th>
                                <Table.Th>Avg</Table.Th>
                            </Table.Tr>
                        </Table.Thead>
                        <Table.Tbody>
                            {form.getValues().attributes.map((attribute, i) => {
                                const info = attributeInfos[i];

                                return <Table.Tr key={i}>
                                    <Table.Td component="th" scope="row">
                                        {attribute?.name || ""}
                                    </Table.Td>
                                    <Table.Td align="right">{info ? info.notPrecise ?
                                        <Tooltip label={t("universe:secondaryAttributeExplanationTooltip")}>
                                            <div>/</div>
                                        </Tooltip>
                                        : info.min : 0
                                    }</Table.Td>
                                    <Table.Td align="right">{info ? info.notPrecise ?
                                        <Tooltip label={t("universe:secondaryAttributeExplanationTooltip")}>
                                            <div>/</div>
                                        </Tooltip>
                                        : info.max : 0}
                                    </Table.Td>
                                    <Table.Td align="right">{info ? info.average : 0}</Table.Td>
                                </Table.Tr>;
                            })}
                        </Table.Tbody>
                    </Table>
                    <Button
                        fullWidth
                        loading={loadingAttributeInfos}
                        onClick={() => {
                            setLoadingAttributeInfos(true);
                            UNIVERSE_CREATION_API.getSecondaryAttributeInfo(activeUniverse.id, form.getValues().attributes)
                                .then(response => setAttributeInfos(response.data))
                                .finally(() => setLoadingAttributeInfos(false));
                        }}
                    >
                        {t("universe:getSecondaryAttributeInfos")}
                    </Button>
                </Stack>
            </Paper>
            <Paper shadow="md" p="md" mt="md">
                <Stack>
                    <Text ta="left">
                        {t("universe:secondaryAttributeTesting")}
                    </Text>
                    <Grid columns={2}>
                        <Grid.Col span={1}>
                            {primaryAttributes.map((primaryAttribute, i) =>
                                <NumberInput
                                    key={"primary-attribute-field-" + i}
                                    label={primaryAttribute.name}
                                    value={primaryValues.get(primaryAttribute.shortName)}
                                    onChange={value => setPrimaryValues(new Map(primaryValues).set(primaryAttribute.shortName, Number(value)))}
                                    allowDecimal={false}
                                    clampBehavior="strict"
                                    min={characterSettings.minPrimaryAttributeValue}
                                    max={characterSettings.maxPrimaryAttributeValue}
                                />
                            )}
                        </Grid.Col>
                        <Grid.Col span={1}>
                            {form.getValues().attributes.map((secondaryAttribute, i) =>
                                <TextInput
                                    key={"secondary-attribute-result-field-" + i}
                                    label={secondaryAttribute.name ? secondaryAttribute.name : "???"}
                                    readOnly
                                    value={
                                        calculatedAttributeValues[i] === undefined
                                        || calculatedAttributeValues[i].toString() === 'NaN'
                                        || Number.isNaN(calculatedAttributeValues[i])
                                            ? "???" : Math.round(calculatedAttributeValues[i])}
                                />
                            )}
                        </Grid.Col>
                    </Grid>
                </Stack>
            </Paper>
        </Grid.Col>
    </Grid>;
}