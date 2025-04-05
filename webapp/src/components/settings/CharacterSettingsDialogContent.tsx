import { getUserContext } from '../PageBase';
import { getUniverseContext } from '../PageBase';
import { Alert, Button, Dialog, DialogActions, DialogTitle, Link, Stack, Typography } from "@mui/material";
import { NumberFieldWithError } from "../inputs/InputFields";
import { useState } from "react";
import { CharacterSettings, JewelleryDefinition, UniverseCreationServiceApi } from "../../api";
import { useTranslation } from "react-i18next";
import { numberFormatter, percentageFormatter, probabilityForSuccesfulThrows } from "../Utils";
import { fetchAllPrimaryAttributes } from "../Database";
import { Field } from "../database/DatabaseObjectDialog";
import { SettingsProps } from "./SettingsProps";
import axios, { AxiosError, AxiosRequestConfig, AxiosResponse } from "axios";
import { LanguageSelect } from "../inputs/NexusSelect";
import { API_CONFIGURATION } from "../Constants";

const UNIVERSE_CREATION_API = new UniverseCreationServiceApi(API_CONFIGURATION);

export function CharacterSettingsDialogContent({ settings, setSettings, errors }: SettingsProps<CharacterSettings>) {
    const { t } = useTranslation();
    const [openJewelleryImport, setOpenJewelleryImport] = useState(false);

    const [primaryAttributes] = fetchAllPrimaryAttributes();
    const attributeLength = primaryAttributes.length;

    return <Stack padding={2} justifyContent="center">
        <Typography gutterBottom variant="h3" component="div" align='center'>
            {t('universe:characterSettings')}
        </Typography>
        <Stack spacing={2} justifyContent="center">
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
                        }} />
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
                        }} />
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
                        }} />
                    <Field<CharacterSettings, Number>
                        field={{ fieldId: "numberOfHandheld", label: t("universe:numberOfHandheld"), fieldType: "NUMBER" }}
                        errors={errors}
                        databaseObject={settings}
                        setDatabaseObject={setSettings} />
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
                                { fieldId: "tag", label: t("tag"), fieldType: "STRING" }
                            ]
                        }}
                        errors={errors}
                        databaseObject={settings}
                        setDatabaseObject={setSettings} />
                </Stack>
            </Stack>
        </Stack>
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
            importFunction={(universe: string, language: string) => UNIVERSE_CREATION_API.getDefaultJewelleryDefinitions(universe, language)} />
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
