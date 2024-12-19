import { Button, Paper, Stack, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, Tooltip, Typography } from "@mui/material";
import { useTranslation } from "react-i18next";
import { getUniverseContext } from '../PageBase';
import { BinaryExpressionTreeServiceApi, SecondaryAttributeDTO, SecondaryAttributeInfo, SimpleSecondaryAttributeServiceApi, UniverseCreationServiceApi } from "../../api";
import { API_CONFIGURATION } from "../Constants";
import { useEffect, useState } from "react";
import { NumberFieldWithError, TextFieldWithError } from "../inputs/TestFieldWithError";
import { FaMinus, FaPlus } from "react-icons/fa6";
import { Field } from "./DatabaseObjectDialog";
import { fetchAllPrimaryAttributes } from "../Database";
import { handleValidationErrors } from "../ErrorUtils";

const SIMPLE_SECONDARY_ATTRIBUTE_API = new SimpleSecondaryAttributeServiceApi(API_CONFIGURATION);
const EXPRESSION_API = new BinaryExpressionTreeServiceApi(API_CONFIGURATION);
const UNIVERSE_CREATION_API = new UniverseCreationServiceApi(API_CONFIGURATION);

export function SecondaryAttributeDialogContent({ onSave }: { onSave: () => void; }) {
    const { t } = useTranslation();
    const { activeUniverse } = getUniverseContext();

    const [attributes, setAttributes] = useState<SecondaryAttributeDTO[]>(Array(8).fill({ name: "", consumable: false, calculationFormula: "" }));
    const [errors, setErrors] = useState<Map<string, string>>(new Map<string, string>());

    const [primaryAttributes] = fetchAllPrimaryAttributes();
    const [supportedVariables, setSupportedVariables] = useState<string[]>([]);
    const [primaryValues, setPrimaryValues] = useState<Map<string, number>>(new Map<string, number>());
    const [calculatedAttributeValues, setCalculatedAttributeValues] = useState<number[]>([]);
    const [attributeInfos, setAttributeInfos] = useState<SecondaryAttributeInfo[]>([]);
    const [loadingAttributeInfos, setLoadingAttributeInfos] = useState(false);

    useEffect(() => {
        if (!activeUniverse) {
            return;
        }
        SIMPLE_SECONDARY_ATTRIBUTE_API.getAllSimpleSecondaryAttributes(activeUniverse.name).then(response => {
            if (response.data.length > 0) {
                setAttributes(response.data);
            }
        });
        SIMPLE_SECONDARY_ATTRIBUTE_API.getAllSupportedVariables(activeUniverse.name).then(response => setSupportedVariables(response.data));
    }, [activeUniverse]);

    useEffect(() => {
        if (!activeUniverse) {
            return;
        }
        const calculate = setTimeout(() => {
            EXPRESSION_API.calculateResults({
                formulas: attributes.map(att => att.calculationFormula),
                constants: Object.fromEntries(primaryValues)
            }).then(response => setCalculatedAttributeValues(response.data));
        }, 200);

        return () => clearTimeout(calculate);
    }, [activeUniverse, attributes, primaryValues]);

    return <Stack padding={2} spacing={2} justifyContent="center">
        <Typography gutterBottom variant="h3" component="div" align='center'>
            {t('secondary-attributes')}
        </Typography>
        <Stack spacing={2} justifyContent="center">
            <Stack direction="row" justifyContent="center" spacing={10}>
                <Stack spacing={2} width="40%">
                    {attributes.map((entry, index) => {
                        const fieldIdPrefix = "setAll.attributes[" + index + "].";

                        return <Stack spacing={2} justifyContent="center" key={"attributes-base-stack-" + index}>
                            <Stack spacing={2} key={"attributes-stack-" + index} direction="row">
                                <TextFieldWithError
                                    fieldId={fieldIdPrefix + "name"}
                                    errorMap={errors}
                                    value={entry.name}
                                    label={t("name")}
                                    fullWidth
                                    onChange={name => {
                                        setAttributes(attributes.map((e, i) => index !== i ? e : { ...e, name: name }));
                                    }}
                                />
                                <TextFieldWithError
                                    fieldId={fieldIdPrefix + "calculationFormula"}
                                    errorMap={errors}
                                    value={entry.calculationFormula}
                                    label={t("character:calculationFormula")}
                                    fullWidth
                                    tooltip={
                                        <>
                                            {t("character:calculationFormulaTooltip")}
                                            <ul key={"tooltip-supported-variables-" + index}>
                                                {supportedVariables.map(v => <li key={"tooltip-supported-variables-" + index + "-" + v}>{"\u2022 " + v}</li>)}
                                            </ul>
                                        </>
                                    }
                                    onChange={calculationFormula => {
                                        setAttributes(attributes.map((e, i) => index !== i ? e : { ...e, calculationFormula: calculationFormula }));
                                    }}
                                />
                            </Stack>
                            <Stack spacing={2} key={"attributes-stack2-" + index} direction="row">
                                <Field<SecondaryAttributeDTO, boolean>
                                    field={{
                                        fieldId: "consumable",
                                        fullId: fieldIdPrefix + "consumable",
                                        label: t("character:consumableAttribute"),
                                        fieldType: "BOOLEAN",
                                        tooltip: t("character:consumableAttributeTooltip")
                                    }}
                                    errors={errors}
                                    databaseObject={entry}
                                    setDatabaseObject={attribute => {
                                        setAttributes(attributes.map((e, i) => index !== i ? e : attribute));
                                    }}
                                />
                                <Button key={"fieldIdPrefix-sub-" + index} onClick={() => {
                                    setAttributes(attributes.filter((_, i) => i !== index));
                                }} sx={{ width: 1 / 12, paddingTop: 1.5 }} > <FaMinus size={20} /> </Button>
                            </Stack>
                        </Stack>;
                    })}
                    <Button
                        key="attributes-add"
                        fullWidth
                        onClick={() => setAttributes(attributes.concat([{ name: "", consumable: false, calculationFormula: "" }]))}
                        startIcon={<FaPlus />}
                    >
                        {t("universe:addAnotherAttribute")}
                    </Button>
                </Stack>

                <Stack spacing={2} width="40%" alignItems="center">
                    <Typography gutterBottom variant="body2" component="div" align='left'>
                        {t("universe:secondaryAttributeExplanation")}
                    </Typography>
                    <TableContainer component={Paper} sx={{ width: "78%" }}>
                        <Table size="small" aria-label="a dense table">
                            <TableHead>
                                <TableRow>
                                    <TableCell>{t("secondary-attribute")}</TableCell>
                                    <TableCell align="right">Min</TableCell>
                                    <TableCell align="right">Max</TableCell>
                                    <TableCell align="right">Avg</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {attributes.map((attribute, i) => {
                                    const info = attributeInfos[i];

                                    return <TableRow
                                        key={i}
                                        sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                                    >
                                        <TableCell component="th" scope="row">
                                            {attribute?.name || ""}
                                        </TableCell>
                                        <TableCell align="right">{info ? info.notPrecise ?
                                            <Tooltip title={t("universe:secondaryAttributeExplanationTooltip")}>
                                                <div>/</div>
                                            </Tooltip>
                                            : info.min : 0
                                        }</TableCell>
                                        <TableCell align="right">{info ? info.notPrecise ?
                                            <Tooltip title={t("universe:secondaryAttributeExplanationTooltip")}>
                                                <div>/</div>
                                            </Tooltip>
                                            : info.max : 0}</TableCell>
                                        <TableCell align="right">{info ? info.average : 0}</TableCell>
                                    </TableRow>;
                                })}
                            </TableBody>
                        </Table>
                    </TableContainer>
                    <Button
                        fullWidth
                        disabled={loadingAttributeInfos}
                        onClick={() => {
                            setLoadingAttributeInfos(true);
                            UNIVERSE_CREATION_API.getSecondaryAttributeInfo(activeUniverse.name, attributes)
                                .then(response => setAttributeInfos(response.data))
                                .finally(() => setLoadingAttributeInfos(false));
                        }}
                    >
                        {t("universe:getSecondaryAttributeInfos")}
                    </Button>
                    <Typography gutterBottom variant="body2" component="div" align='left'>
                        {t("universe:secondaryAttributeTesting")}
                    </Typography>
                    <Stack direction="row" justifyContent="center" spacing={10} key="result-stack">
                        <Stack spacing={2} width="40%" key="primary-result-stack">
                            {primaryAttributes.map((primaryAttribute, i) =>
                                <NumberFieldWithError
                                    key={"primary-attribute-field-" + i}
                                    fieldId={"primary-attribute-field-" + i}
                                    label={primaryAttribute.name}
                                    value={primaryValues.get(primaryAttribute.shortName)}
                                    onChange={value => setPrimaryValues(new Map(primaryValues).set(primaryAttribute.shortName, value))}
                                    integerField
                                />
                            )}
                        </Stack>
                        <Stack spacing={2} width="40%" key="secondary-result-stack">
                            {attributes.map((secondaryAttribute, i) =>
                                <TextField
                                    key={"secondary-attribute-result-field-" + i}
                                    label={secondaryAttribute.name}
                                    value={
                                        calculatedAttributeValues[i] === undefined
                                            || calculatedAttributeValues[i].toString() === 'NaN'
                                            || Number.isNaN(calculatedAttributeValues[i])
                                            ? "???" : Math.round(calculatedAttributeValues[i])}
                                />
                            )}
                        </Stack>
                    </Stack>
                </Stack>
            </Stack>
        </Stack>
        <Stack spacing={2} direction="row" justifyContent="flex-end">
            <Button color="warning" variant="outlined" autoFocus href="/">
                {t('cancel')}
            </Button>
            <Button color="primary" variant="outlined" onClick={() => {
                SIMPLE_SECONDARY_ATTRIBUTE_API.setAllSimpleSecondaryAttributes(activeUniverse.name, attributes).then(onSave).catch(handleValidationErrors(setErrors));
            }}>{t('save')}</Button>
        </Stack>
    </Stack>;
}