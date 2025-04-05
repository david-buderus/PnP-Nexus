import { getUniverseContext } from '../PageBase';
import { Button, Stack, TextField, Typography } from "@mui/material";
import { NumberFieldWithError, TextFieldWithError } from "../inputs/InputFields";
import { useEffect, useState } from "react";
import { CurrencyCalculationEntry, CurrencySettings, UniverseSettingsServiceApi } from "../../api";
import { useTranslation } from "react-i18next";
import { handleValidationErrors } from "../ErrorUtils";
import { FaMinus, FaPlus } from "react-icons/fa6";
import { currencyToHumanReadable } from "../Utils";
import { SettingsProps } from "./SettingsProps";
import { API_CONFIGURATION } from "../Constants";

const SETTINGS_API = new UniverseSettingsServiceApi(API_CONFIGURATION);

export function CurrencySettingsDialogContent({ settings, setSettings, errors }: SettingsProps<CurrencySettings>) {
    const { t } = useTranslation();
    const { activeUniverse } = getUniverseContext();
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
                                }} />
                            <TextFieldWithError
                                fieldId={fieldIdPrefix + "currencyShortForm"}
                                errorMap={errors}
                                value={entry.currencyShortForm}
                                label={t("universe:calculationCurrencyShortForm")}
                                tooltip={t("universe:baseCurrencyShortFormTooltip")}
                                onChange={currencyShortForm => {
                                    setCurrencyCalculationEntries(currencyCalculationEntries.map((e, i) => index !== i ? e : { factor: e.factor, currency: e.currency, currencyShortForm: currencyShortForm }));
                                }} />
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
                                }} />
                            <Typography key={"calculationEntries-text-" + index} component="div" variant="h6" sx={{ width: 2 / 4 }} paddingTop={1.5}>
                                {currencyFactor}
                            </Typography>
                            <Button key={"calculationEntries-sub-" + index} onClick={() => {
                                setCurrencyCalculationEntries(currencyCalculationEntries.filter((_, i) => i !== index));
                            }} sx={{ width: 1 / 4, paddingTop: 1.5 }}> <FaMinus size={20} /> </Button>
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
                    fullWidth />
                <TextField
                    key={"priceExample-resulting"}
                    label={t("resultingPrice")}
                    data-testid="resultingPrice"
                    variant="outlined"
                    value={currencyToHumanReadable(settings, priceExample)}
                    InputProps={{ readOnly: true }}
                    fullWidth />
            </Stack>
        </Stack>
    </Stack>;
}
