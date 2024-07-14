import { useSearchParams } from "react-router-dom";
import { getUniverseContext, getUserContext } from "../../components/PageBase";
import { Alert, Button, Checkbox, Dialog, DialogActions, DialogTitle, FormControlLabel, Link, Stack, TextField, Tooltip, Typography } from "@mui/material";
import { NumberFieldWithError, TextFieldWithError } from "../../components/inputs/TestFieldWithError";
import { useEffect, useState } from "react";
import { ArmorDefinition, CharacterSettings, CurrencyCalculationEntry, CurrencySettings, ItemSettings, JewelleryDefinition, PrimaryAttribute, PrimaryAttributeServiceApi, Universe, UniverseCreationServiceApi, UniverseServiceApi, UniverseSettingsServiceApi } from "../../api";
import { useTranslation } from "react-i18next";
import { API_CONFIGURATION } from "../../components/Constants";
import { handleValidationError } from "../../components/ErrorUtils";
import { FaMinus, FaPlus } from "react-icons/fa6";
import { currencyToHumanReadable, numberFormatter, percentageFormatter, probabilityForSuccesfulThrows } from "../../components/Utils";
import { fetchAll, fetchAllItemTypes, fetchAllPrimaryAttributes } from "../../components/Database";
import { LanguageSelect, NexusSelect } from "../../components/inputs/NexusSelect";
import { Field } from "../../components/database/DatabaseObjectDialog";
import axios, { AxiosError, AxiosRequestConfig, AxiosResponse } from "axios";

const UNIVERSE_API = new UniverseServiceApi(API_CONFIGURATION);
const SETTINGS_API = new UniverseSettingsServiceApi(API_CONFIGURATION);
const UNIVERSE_CREATION_API = new UniverseCreationServiceApi(API_CONFIGURATION);
const PRIMARY_ATTRIBUTE_API = new PrimaryAttributeServiceApi(API_CONFIGURATION);

export function UniverseCreation() {
    const [searchParams, setSearchParams] = useSearchParams();
    const step: number = Number(searchParams.get("step")) ?? 0;

    function nextStep() {
        searchParams.set("step", String(step + 1));
        setSearchParams(searchParams);
    }

    switch (step) {
        case 0:
            return <UniverseCreationStep nextStep={nextStep} />;
        case 1:
            return <CurrencyStep nextStep={nextStep} />;
        case 2:
            return <ItemStep nextStep={nextStep} />;
        case 3:
            return <ImportDefaultsStep nextStep={nextStep} />;
        case 4:
            return <PrimaryAttributeStep nextStep={nextStep} />;
        case 5:
            return <CharacterStep nextStep={nextStep} />;
    }

    return <></>;
}

interface StepProps {
    nextStep: () => void;
}

function UniverseCreationStep({ nextStep }: StepProps) {
    const { t } = useTranslation();
    const { setActiveUniverse, fetchUniverses } = getUniverseContext();

    const [universe, setUniverse] = useState<Universe>({
        name: "",
        displayName: "",
        shortDescription: "",
        description: ""
    });
    const [errors, setErrors] = useState<Map<string, string>>(new Map<string, string>());

    return <Stack spacing={2} padding={2}>
        <Typography gutterBottom variant="h3" component="div" align='center'>
            {t('universe:createUniverse')}
        </Typography>
        <Stack spacing={2} direction="row">
            <TextFieldWithError fieldId="name" errorMap={errors} value={universe.name} onChange={value => {
                setUniverse({
                    ...universe,
                    name: value
                });
            }} label={t("name")} fullWidth />
            <TextFieldWithError fieldId="displayName" errorMap={errors} value={universe.displayName} onChange={value => {
                setUniverse({
                    ...universe,
                    displayName: value
                });
            }} label={t("displayName")} fullWidth />
        </Stack>
        <TextFieldWithError fieldId="shortDescription" errorMap={errors} value={universe.shortDescription} onChange={value => {
            setUniverse({
                ...universe,
                shortDescription: value
            });
        }} label={t("universe:shortDescription")} fullWidth multiline rows={2} />
        <TextFieldWithError fieldId="description" errorMap={errors} value={universe.description} onChange={value => {
            setUniverse({
                ...universe,
                description: value
            });
        }} label={t("description")} fullWidth multiline rows={10} />
        <Stack spacing={2} direction="row" justifyContent="flex-end">
            <Button color="warning" variant="outlined" autoFocus href="/">
                {t('cancel')}
            </Button>
            <Button color="primary" variant="outlined" onClick={() => {
                UNIVERSE_API.createUniverse(universe).then(() => {
                    fetchUniverses().then(() => setActiveUniverse(universe));
                    nextStep();
                }).catch(handleValidationError(setErrors));
            }}>{t('add')}</Button>
        </Stack>
    </Stack>;
}

function CurrencyStep({ nextStep }: StepProps) {
    const { t } = useTranslation();
    const { activeUniverse } = getUniverseContext();

    const [settings, setSettings] = useState<CurrencySettings>({
        baseCurrency: "",
        baseCurrencyShortForm: "",
        calculationEntries: []
    });
    const [errors, setErrors] = useState<Map<string, string>>(new Map<string, string>());
    const [priceExample, setPriceExample] = useState(1234);

    useEffect(() => {
        if (!activeUniverse) {
            return;
        }
        SETTINGS_API.getCurrencySettings(activeUniverse.name).then(response => setSettings(response.data));
    }, [activeUniverse]);

    const currencyCalculationEntries = settings.calculationEntries;
    function setCurrencyCalculationEntries(entries: CurrencyCalculationEntry[]) {
        setSettings({
            ...settings,
            calculationEntries: entries
        });
    }

    return <Stack padding={2} justifyContent="center">
        <Typography gutterBottom variant="h3" component="div" align='center'>
            {t('universe:currencySettings')}
        </Typography>
        <Stack spacing={8} direction="row" justifyContent="center">
            <Stack spacing={2} width="40%">
                <Stack direction="row" spacing={2}>
                    <TextFieldWithError fieldId="baseCurrency" errorMap={errors} value={settings.baseCurrency} onChange={value => {
                        setSettings({
                            ...settings,
                            baseCurrency: value
                        });
                    }} label={t("universe:baseCurrency")} fullWidth />
                    <TextFieldWithError fieldId="baseCurrencyShortForm" errorMap={errors} value={settings.baseCurrencyShortForm} onChange={value => {
                        setSettings({
                            ...settings,
                            baseCurrencyShortForm: value
                        });
                    }} label={t("universe:baseCurrencyShortForm")} tooltip={t("universe:baseCurrencyShortFormTooltip")} />
                </Stack>
                {currencyCalculationEntries.map((entry, index) => {
                    const fieldIdPrefix = "calculationEntries[" + index + "].";
                    const currencyFactor = t("universe:currencyFactor", {
                        smallerCoin: (index === 0 ? settings.baseCurrency : settings.calculationEntries[index - 1].currency) || "???",
                        largerCoin: entry.currency || "???"
                    });

                    return <Stack spacing={2} key={"calculationEntries-stack-" + index}>
                        <Stack direction="row" spacing={2}>
                            <TextFieldWithError
                                fullWidth
                                fieldId={fieldIdPrefix + "currency"}
                                errorMap={errors} value={entry.currency}
                                label={t("universe:calculationCurrency")}
                                onChange={currency => {
                                    setCurrencyCalculationEntries(currencyCalculationEntries.map((e, i) => index !== i ? e : { factor: e.factor, currency: currency, currencyShortForm: e.currencyShortForm }));
                                }}
                            />
                            <TextFieldWithError
                                fieldId={fieldIdPrefix + "currencyShortForm"}
                                errorMap={errors}
                                value={entry.currencyShortForm}
                                label={t("universe:calculationCurrencyShortForm")}
                                tooltip={t("universe:baseCurrencyShortFormTooltip")}
                                onChange={currencyShortForm => {
                                    setCurrencyCalculationEntries(currencyCalculationEntries.map((e, i) => index !== i ? e : { factor: e.factor, currency: e.currency, currencyShortForm: currencyShortForm }));
                                }}
                            />
                        </Stack>
                        <Stack direction="row" spacing={2} alignItems="flex-start">
                            <NumberFieldWithError
                                fieldId={fieldIdPrefix + "factor"}
                                errorMap={errors}
                                value={entry.factor}
                                label={t("universe:calculationFactor")}
                                sx={{ width: 1 / 4 }}
                                onChange={factor => {
                                    setCurrencyCalculationEntries(currencyCalculationEntries.map((e, i) => index !== i ? e : { factor: factor, currency: e.currency, currencyShortForm: e.currencyShortForm }));
                                }}
                            />
                            <Typography key={"calculationEntries-text-" + index} component="div" variant="h6" sx={{ width: 2 / 4 }} paddingTop={1.5}>
                                {currencyFactor}
                            </Typography>
                            <Button key={"calculationEntries-sub-" + index} onClick={() => {
                                setCurrencyCalculationEntries(currencyCalculationEntries.filter((_, i) => i !== index));
                            }} sx={{ width: 1 / 4, paddingTop: 1.5 }} > <FaMinus size={20} /> </Button>
                        </Stack>
                    </Stack>;
                })}
                <Button
                    key="calculationEntries-add"
                    fullWidth
                    onClick={() => setCurrencyCalculationEntries(currencyCalculationEntries.concat([{ factor: 10, currency: "", currencyShortForm: "" }]))}
                    startIcon={<FaPlus />}
                >
                    {t("universe:addAnotherCoin")}
                </Button>
            </Stack>
            <Stack spacing={2} width="40%">
                <Typography gutterBottom variant="body2" component="div" align='left'>
                    {t("universe:currencyExplanation")}
                </Typography>
                <NumberFieldWithError
                    key="priceExample"
                    fieldId="priceExample"
                    integerField
                    label={t("universe:priceExample")}
                    value={priceExample}
                    onChange={setPriceExample}
                    fullWidth
                />
                <TextField
                    key={"priceExample-resulting"}
                    label={t("resultingPrice")}
                    data-testid="resultingPrice"
                    variant="outlined"
                    value={currencyToHumanReadable(settings, priceExample)}
                    InputProps={{ readOnly: true }}
                    fullWidth
                />
                <Stack spacing={2} direction="row" justifyContent="flex-end">
                    <Button color="warning" variant="outlined" autoFocus href="/">
                        {t('cancel')}
                    </Button>
                    <Button color="primary" variant="outlined" onClick={() => {
                        SETTINGS_API.updateCurrencySettings(activeUniverse.name, settings).then(nextStep).catch(handleValidationError(setErrors));
                    }}>{t('save')}</Button>
                </Stack>
            </Stack>
        </Stack>
    </Stack>;
}

function ItemStep({ nextStep }: StepProps) {
    const { t } = useTranslation();
    const { activeUniverse } = getUniverseContext();

    const [settings, setSettings] = useState<ItemSettings>({
        wearFactor: 10
    });
    const [wearFactorEnabled, setWearFactorEnabled] = useState(settings.wearFactor > 0);
    const [errors, setErrors] = useState<Map<string, string>>(new Map<string, string>());

    useEffect(() => {
        if (!activeUniverse) {
            return;
        }
        SETTINGS_API.getItemSettings(activeUniverse.name).then(response => setSettings(response.data));
    }, [activeUniverse]);

    return <Stack padding={2} justifyContent="center">
        <Typography gutterBottom variant="h3" component="div" align='center'>
            {t('universe:itemSettings')}
        </Typography>
        <Stack spacing={2} justifyContent="center" >
            <Stack direction="row" justifyContent="center" spacing={2}>
                <NumberFieldWithError
                    fieldId="settings.wearFactor"
                    label={wearFactorEnabled ? t("universe:wearFactor") : t("universe:wearFactorDisabled")}
                    disabled={!wearFactorEnabled}
                    errorMap={errors}
                    integerField
                    value={settings.wearFactor}
                    onChange={value => {
                        setSettings({
                            ...settings,
                            wearFactor: wearFactorEnabled ? value : -1
                        });
                    }}
                    sx={{ width: "40%" }}
                />
                <Tooltip title={t("universe:wearFactorTooltip")} placement="right-start">
                    <FormControlLabel
                        control={<Checkbox checked={wearFactorEnabled} onChange={event => {
                            const checked = event.target.checked;
                            setWearFactorEnabled(checked);
                            setSettings({
                                ...settings,
                                wearFactor: checked ? settings.wearFactor : -1
                            });
                        }} />}
                        label={<Typography fontSize={12}> {t("universe:wearFactorEnabled")} </Typography>}
                        labelPlacement="top"
                    />
                </Tooltip>
            </Stack>
            <Stack spacing={2} direction="row" justifyContent="flex-end">
                <Button color="warning" variant="outlined" autoFocus href="/">
                    {t('cancel')}
                </Button>
                <Button color="primary" variant="outlined" onClick={() => {
                    SETTINGS_API.updateItemSettings(activeUniverse.name, settings).then(nextStep).catch(handleValidationError(setErrors));
                }}>{t('save')}</Button>
            </Stack>
        </Stack>
    </Stack>;
}

function ImportDefaultsStep({ nextStep }: StepProps) {
    const { t } = useTranslation();
    const { activeUniverse } = getUniverseContext();
    const { userPreferences } = getUserContext();

    const [language, setLanguage] = useState<string>(userPreferences?.language ?? null);
    const [importedEquipmentTypes, setImportedEquipmentTypes] = useState(false);
    const [importedMaterials, setImportedMaterials] = useState(false);

    return <Stack padding={2} justifyContent="center">
        <Typography gutterBottom variant="h3" component="div" align='center'>
            {t('universe:importDefaultsStep')}
        </Typography>
        <Stack spacing={2} justifyContent="center" alignItems="center" >
            <Typography gutterBottom variant="body2" component="div" align='left' sx={{ width: 3 / 4 }}>
                {t("universe:startingExplanation")}
            </Typography>
            <LanguageSelect
                language={language}
                onChange={setLanguage}
            />
            <Typography gutterBottom variant="body2" component="div" align='left' sx={{ width: 3 / 4 }}>
                {t("universe:defaultItemTypesExplanation")}
            </Typography>
            <Button color="success" variant="outlined" disabled={importedEquipmentTypes || language === null} sx={{ minWidth: 300 }} onClick={() => {
                setImportedEquipmentTypes(true);
                UNIVERSE_CREATION_API.createDefaultItemTypes(activeUniverse.name, language);
            }}>{importedEquipmentTypes ? t('universe:successfullyImported') : t('universe:importEquipmentTypes')}</Button>
            <Typography gutterBottom variant="body2" component="div" align='left' sx={{ width: 3 / 4 }}>
                {t("universe:defaultMaterialsExplanation")}
            </Typography>
            <Button color="success" variant="outlined" disabled={importedMaterials || language === null} sx={{ minWidth: 300 }} onClick={() => {
                setImportedMaterials(true);
                UNIVERSE_CREATION_API.createDefaultMaterials(activeUniverse.name, language);
            }}>{importedMaterials ? t('universe:successfullyImported') : t('universe:importMaterials')}</Button>
        </Stack>
        <Stack spacing={2} direction="row" justifyContent="flex-end">
            <Button color="warning" variant="outlined" autoFocus href="/">
                {t('cancel')}
            </Button>
            <Button color="primary" variant="outlined" onClick={() => {
                nextStep();
            }}>{t('next')}</Button>
        </Stack>

    </Stack>;
}

function PrimaryAttributeStep({ nextStep }: StepProps) {
    const { t } = useTranslation();
    const { activeUniverse } = getUniverseContext();

    const [attributes, setAttributes] = useState<PrimaryAttribute[]>(Array(8).fill({ shortName: "", name: "" }));
    const [errors, setErrors] = useState<Map<string, string>>(new Map<string, string>());

    useEffect(() => {
        if (!activeUniverse) {
            return;
        }
        PRIMARY_ATTRIBUTE_API.getAllPrimaryAttributes(activeUniverse.name).then(response => {
            if (response.data.length > 0) {
                setAttributes(response.data);
            }
        });
    }, [activeUniverse]);

    return <Stack padding={2} justifyContent="center">
        <Typography gutterBottom variant="h3" component="div" align='center'>
            {t('universe:primaryAttributesStep')}
        </Typography>
        <Stack spacing={2}>
            {attributes.map((entry, index) => {
                const fieldIdPrefix = "setAll.attributes[" + index + "].";

                return <Stack spacing={2} key={"attributes-stack-" + index} direction="row">
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
                        fieldId={fieldIdPrefix + "shortName"}
                        errorMap={errors}
                        value={entry.shortName}
                        label={t("character:shortName")}
                        sx={{ width: 1 / 4 }}
                        onChange={shortName => {
                            setAttributes(attributes.map((e, i) => index !== i ? e : { ...e, shortName: shortName }));
                        }}
                    />
                    <Button key={"fieldIdPrefix-sub-" + index} onClick={() => {
                        setAttributes(attributes.filter((_, i) => i !== index));
                    }} sx={{ width: 1 / 12, paddingTop: 1.5 }} > <FaMinus size={20} /> </Button>
                </Stack>;
            })}
            <Button
                key="attributes-add"
                fullWidth
                onClick={() => setAttributes(attributes.concat([{ shortName: "", name: "" }]))}
                startIcon={<FaPlus />}
            >
                {t("universe:addAnotherAttribute")}
            </Button>
        </Stack>
        <Stack spacing={2} direction="row" justifyContent="flex-end">
            <Button color="warning" variant="outlined" autoFocus href="/">
                {t('cancel')}
            </Button>
            <Button color="primary" variant="outlined" onClick={() => {
                PRIMARY_ATTRIBUTE_API.setAllPrimaryAttributes(activeUniverse.name, attributes).then(nextStep).catch(handleValidationError(setErrors));
            }}>{t('save')}</Button>
        </Stack>
    </Stack>;
}

function CharacterStep({ nextStep }: StepProps) {
    const { t } = useTranslation();
    const { activeUniverse } = getUniverseContext();

    const [settings, setSettings] = useState<CharacterSettings>({
        minPrimaryAttributeValue: 2,
        maxPrimaryAttributeValue: 12,
        maxPrimaryAttributeSum: 50,
        numberOfHandheld: 2,
        armorDefinitions: [{
            "name": "",
            "type": null
        }],
        jewelleryDefinitions: [{
            "amount": 1,
            "name": "",
            "type": null
        }]
    });
    const [errors, setErrors] = useState<Map<string, string>>(new Map<string, string>());
    const [openArmorImport, setOpenArmorImport] = useState(false);
    const [openJewelleryImport, setOpenJewelleryImport] = useState(false);

    const [primaryAttributes] = fetchAllPrimaryAttributes();
    const [itemTypes] = fetchAllItemTypes();

    useEffect(() => {
        if (!activeUniverse) {
            return;
        }
        SETTINGS_API.getCharacterSettings(activeUniverse.name).then(response => setSettings(response.data));
    }, [activeUniverse]);

    const attributeLength = primaryAttributes.length;

    return <Stack padding={2} justifyContent="center">
        <Typography gutterBottom variant="h3" component="div" align='center'>
            {t('universe:characterSettings')}
        </Typography>
        <Stack spacing={2} justifyContent="center" >
            <Stack direction="row" justifyContent="center" spacing={10}>
                <Stack spacing={2} width="40%">
                    <NumberFieldWithError
                        fieldId="minPrimaryAttributeValue"
                        label={t("universe:minPrimaryAttributeValue")}
                        errorMap={errors}
                        integerField
                        value={settings.minPrimaryAttributeValue}
                        onChange={value => {
                            setSettings({
                                ...settings,
                                minPrimaryAttributeValue: value
                            });
                        }}
                    />
                    <NumberFieldWithError
                        fieldId="maxPrimaryAttributeValue"
                        label={t("universe:maxPrimaryAttributeValue")}
                        errorMap={errors}
                        integerField
                        value={settings.maxPrimaryAttributeValue}
                        onChange={value => {
                            setSettings({
                                ...settings,
                                maxPrimaryAttributeValue: value
                            });
                        }}
                    />
                    <NumberFieldWithError
                        fieldId="maxPrimaryAttributeSum"
                        label={t("universe:maxPrimaryAttributeSum")}
                        errorMap={errors}
                        integerField
                        value={settings.maxPrimaryAttributeSum}
                        onChange={value => {
                            setSettings({
                                ...settings,
                                maxPrimaryAttributeSum: value
                            });
                        }}
                    />
                    <Field<CharacterSettings, Number>
                        field={{ fieldId: "numberOfHandheld", label: t("universe:numberOfHandheld"), fieldType: "NUMBER" }}
                        errors={errors}
                        databaseObject={settings}
                        setDatabaseObject={setSettings}
                    />
                </Stack>
                <Typography gutterBottom variant="body2" component="div" align='left' width="40%">
                    {t("universe:primaryAttributeDistributionExplanation", {
                        "average": numberFormatter(settings.maxPrimaryAttributeSum / attributeLength),
                        "max": numberFormatter(Math.floor(
                            (settings.maxPrimaryAttributeSum - (attributeLength * settings.minPrimaryAttributeValue)) /
                            (settings.maxPrimaryAttributeValue - settings.minPrimaryAttributeValue)
                        )),
                        "averageChance": percentageFormatter(probabilityForSuccesfulThrows(
                            settings.maxPrimaryAttributeSum / attributeLength,
                            settings.maxPrimaryAttributeSum / attributeLength,
                            settings.maxPrimaryAttributeSum / attributeLength
                        )),
                        "highestChance": percentageFormatter(probabilityForSuccesfulThrows(settings.maxPrimaryAttributeValue, settings.maxPrimaryAttributeValue, settings.maxPrimaryAttributeValue)),
                        "lowestChance": percentageFormatter(probabilityForSuccesfulThrows(settings.minPrimaryAttributeValue, settings.minPrimaryAttributeValue, settings.minPrimaryAttributeValue))
                    })}
                </Typography>
            </Stack>
            <Stack direction="row" justifyContent="center" spacing={10}>
                <Stack spacing={2} width="40%">
                    <Typography gutterBottom variant="h5" component="div" align='center'>
                        {t('universe:armorDefinitions')}
                    </Typography>
                    <Typography gutterBottom paragraph variant="body2" component="div" align='left'>
                        {t("universe:armorDefinitionsExplanation")}{" "}
                        <Link
                            component="button"
                            onClick={() => setOpenArmorImport(true)}
                        >
                            {t("universe:importDefaultArmorDefinitions")}
                        </Link>
                    </Typography>
                    <Field<CharacterSettings, ArmorDefinition>
                        field={{
                            fieldId: "armorDefinitions",
                            label: "",
                            fieldType: "COMPLEX_LIST",
                            emptyObject: { amount: 1, item: null },
                            subFields: [
                                { fieldId: "name", label: t("name"), fieldType: "STRING" },
                                { fieldId: "type", label: t("item-type"), fieldType: "DATABASE", dependency: itemTypes, dependencyLabel: "name" }
                            ]
                        }}
                        errors={errors}
                        databaseObject={settings}
                        setDatabaseObject={setSettings}
                    />
                </Stack>
                <Stack spacing={2} width="40%">
                    <Typography gutterBottom variant="h5" component="div" align='center'>
                        {t('universe:jewelleryDefinitions')}
                    </Typography>
                    <Typography gutterBottom paragraph variant="body2" component="div" align='left'>
                        {t("universe:jewelleryDefinitionsExplanation")}{" "}
                        <Link
                            component="button"
                            onClick={() => setOpenJewelleryImport(true)}
                        >
                            {t("universe:importDefaultJewelleryDefinitions")}
                        </Link>
                    </Typography>
                    <Field<CharacterSettings, JewelleryDefinition>
                        field={{
                            fieldId: "jewelleryDefinitions",
                            label: "",
                            fieldType: "COMPLEX_LIST",
                            emptyObject: { amount: 1, item: null },
                            subFields: [
                                { fieldId: "amount", label: t("amount"), fieldType: "NUMBER" },
                                { fieldId: "name", label: t("name"), fieldType: "STRING" },
                                { fieldId: "type", label: t("item-type"), fieldType: "DATABASE", dependency: itemTypes, dependencyLabel: "name" }
                            ]
                        }}
                        errors={errors}
                        databaseObject={settings}
                        setDatabaseObject={setSettings} />
                </Stack>
            </Stack>
            <Stack spacing={2} direction="row" justifyContent="flex-end">
                <Button color="warning" variant="outlined" autoFocus href="/">
                    {t('cancel')}
                </Button>
                <Button color="primary" variant="outlined" onClick={() => {
                    SETTINGS_API.updateCharacterSettings(activeUniverse.name, settings).then(nextStep).catch(handleValidationError(setErrors));
                }}>{t('save')}</Button>
            </Stack>
        </Stack>
        <ImportDefaultsDialog<ArmorDefinition>
            title={t("universe:armorImportTitle")}
            explanation={t("universe:armorImportExplanation")}
            open={openArmorImport}
            onClose={definitions => {
                setOpenArmorImport(false);
                if (definitions === null) {
                    return;
                }
                setSettings({
                    ...settings,
                    armorDefinitions: definitions
                });
            }}
            importFunction={(universe: string, language: string) => UNIVERSE_CREATION_API.getDefaultArmorDefinitions(universe, language)}
        />
        <ImportDefaultsDialog<JewelleryDefinition>
            title={t("universe:jewelleryImportTitle")}
            explanation={t("universe:jewelleryImportExplanation")}
            open={openJewelleryImport}
            onClose={definitions => {
                setOpenJewelleryImport(false);
                if (definitions === null) {
                    return;
                }
                setSettings({
                    ...settings,
                    jewelleryDefinitions: definitions
                });
            }}
            importFunction={(universe: string, language: string) => UNIVERSE_CREATION_API.getDefaultJewelleryDefinitions(universe, language)}
        />
    </Stack>;
}

function ImportDefaultsDialog<Definitions>({
    title,
    explanation,
    open,
    onClose,
    importFunction
}: {
    title: string;
    explanation: string;
    open: boolean;
    onClose: (definitons: Definitions[]) => void;
    importFunction: (universe: string, language: string, options?: AxiosRequestConfig) => Promise<AxiosResponse<Definitions[], any>>;
}) {
    const { t } = useTranslation();
    const { activeUniverse } = getUniverseContext();
    const { userPreferences } = getUserContext();

    const [language, setLanguage] = useState<string>(userPreferences?.language ?? null);
    const [error, setError] = useState(false);

    return <Dialog
        open={open}
        onClose={() => onClose(null)}
        fullWidth
    >
        <DialogTitle>{title}</DialogTitle>
        <Stack spacing={2} padding={2}>
            <Typography gutterBottom variant="body2" component="div" align='left'>
                {explanation}
            </Typography>
            <LanguageSelect
                language={language}
                onChange={setLanguage}
            />
            {error && <Alert severity="error">
                {t("universe:importMissingRequirements")}
            </Alert>}
        </Stack>
        <DialogActions>
            <Button data-testid="dialog-cancel" autoFocus onClick={() => onClose(null)}>
                {t('cancel')}
            </Button>
            <Button data-testid="dialog-action" variant="contained" color="success" onClick={() => {
                importFunction(activeUniverse.name, language).then(response => onClose(response.data)).catch((err: Error | AxiosError) => {
                    if (!axios.isAxiosError(err)) {
                        return;
                    }
                    if (err.response.status !== 400) {
                        return;
                    }
                    setError(true);
                });
            }}>{t('universe:importDefaults')}</Button>
        </DialogActions>
    </Dialog >;
}
