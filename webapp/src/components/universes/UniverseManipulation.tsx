import { Button, Checkbox, FormControlLabel, Grid, IconButton, Paper, Stack, Tooltip, Typography } from "@mui/material";
import { CurrencyCalculation, CurrencyCalculationEntry, Universe, UniverseSettings } from "../../api";
import { useState } from "react";
import { NumberFieldWithError, TextFieldWithError } from "../inputs/TestFieldWithError";
import { useTranslation } from "react-i18next";
import { FaMinus, FaPlus } from "react-icons/fa";

/** Props needed to manipulate a universe */
export interface UniverseManipulationProps {
    /** The universe which should get manipulated */
    universe: Universe,
    /** Callback to set the universe */
    setUniverse: (universe: Universe) => void;
    /** The current errors */
    errors: Map<string, string>;
}

/** Input to manipulate a universe */
export function UniverseManipulation(props: UniverseManipulationProps) {
    const { universe, setUniverse, errors } = props;
    const { t } = useTranslation();

    function setSettings(settings: UniverseSettings) {
        setUniverse({
            ...universe,
            settings: settings
        });
    }

    return <Stack spacing={2}>
        <TextFieldWithError fieldId="displayName" errorMap={errors} value={universe.displayName} onChange={value => {
            setUniverse({
                ...universe,
                displayName: value
            });
        }} label={t("displayName")} fullWidth />
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
        <UniverseSettingsManipulation settings={universe.settings} setSettings={setSettings} errors={errors} />
    </Stack>;
}

/** Props needed to manipulate the settings of a universe */
export interface UniverseSettingsManipulationProps {
    /** The universe settings which should get manipulated */
    settings: UniverseSettings,
    /** Callback to set the universe settings */
    setSettings: (universeSettings: UniverseSettings) => void;
    /** The current errors */
    errors: Map<string, string>;
}

/** Input to manipulate the settings of a universe */
export function UniverseSettingsManipulation(props: UniverseSettingsManipulationProps) {
    const { settings, setSettings, errors } = props;
    const { t } = useTranslation();
    const [wearFactorEnabled, setWearFactorEnabled] = useState(settings.wearFactor > 0);
    const currencyCalculation = settings.currencyCalculation;
    const currencyCalculationEntries = currencyCalculation.calculationEntries;

    function setCurrencyCalcuation(calculation: CurrencyCalculation) {
        setSettings({
            ...settings,
            currencyCalculation: calculation
        });
    }

    function setCurrencyCalculationEntries(entries: CurrencyCalculationEntry[]) {
        setCurrencyCalcuation({
            ...currencyCalculation,
            calculationEntries: entries
        });
    }

    return <Stack spacing={2}>
        <Typography gutterBottom variant="h6" component="div">
            {t("universe:settings")}
        </Typography>
        <Stack direction="row" alignItems="flex-start" spacing={2}>
            <NumberFieldWithError fieldId="settings.wearFactor" errorMap={errors} integerField value={settings.wearFactor} onChange={value => {
                setSettings({
                    ...settings,
                    wearFactor: wearFactorEnabled ? value : -1
                });
            }} label={wearFactorEnabled ? t("universe:wearFactor") : t("universe:wearFactorDisabled")} fullWidth disabled={!wearFactorEnabled} />
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
        <Typography gutterBottom variant="h6" component="div">
            {t("universe:currency")}
        </Typography>
        <Stack direction="row" spacing={2}>
            <TextFieldWithError fieldId="settings.currencyCalculation.baseCurrency" errorMap={errors} value={currencyCalculation.baseCurrency} onChange={value => {
                setCurrencyCalcuation({
                    ...currencyCalculation,
                    baseCurrency: value
                });
            }} label={t("universe:baseCurrency")} fullWidth />
            <TextFieldWithError fieldId="settings.currencyCalculation.baseCurrencyShortForm" errorMap={errors} value={currencyCalculation.baseCurrencyShortForm} onChange={value => {
                setCurrencyCalcuation({
                    ...currencyCalculation,
                    baseCurrencyShortForm: value
                });
            }} label={t("universe:baseCurrencyShortForm")} tooltip={t("universe:baseCurrencyShortFormTooltip")} />
        </Stack>
        {currencyCalculationEntries.map((entry, index) => {
            const fieldIdPrefix = "settings.currencyCalculation.calculationEntries[" + index + "].";
            const currencyFactor = t("universe:currencyFactor", {
                smallerCoin: (index === 0 ? currencyCalculation.baseCurrency : currencyCalculationEntries[index - 1].currency) || "???",
                largerCoin: entry.currency || "???"
            });

            return <Stack spacing={2}>
                <Stack direction="row" spacing={2}>
                    <TextFieldWithError fieldId={fieldIdPrefix + "currency"} errorMap={errors} value={entry.currency} onChange={currency => {
                        setCurrencyCalculationEntries(currencyCalculationEntries.map((e, i) => index !== i ? e : { factor: e.factor, currency: currency, currencyShortForm: e.currencyShortForm }));
                    }} label={t("universe:calculationCurrency")} fullWidth />
                    <Tooltip title={t("universe:baseCurrencyShortFormTooltip")} placement="right-start">
                        <TextFieldWithError fieldId={fieldIdPrefix + "currencyShortForm"} errorMap={errors} value={entry.currencyShortForm} onChange={currencyShortForm => {
                            setCurrencyCalculationEntries(currencyCalculationEntries.map((e, i) => index !== i ? e : { factor: e.factor, currency: e.currency, currencyShortForm: currencyShortForm }));
                        }} label={t("universe:calculationCurrencyShortForm")} />
                    </Tooltip>
                </Stack>
                <Stack direction="row" spacing={2} alignItems="flex-start">
                    <NumberFieldWithError fieldId={fieldIdPrefix + "factor"} errorMap={errors} value={entry.factor} onChange={factor => {
                        setCurrencyCalculationEntries(currencyCalculationEntries.map((e, i) => index !== i ? e : { factor: factor, currency: e.currency, currencyShortForm: e.currencyShortForm }));
                    }} label={t("universe:calculationFactor")} sx={{ width: 1 / 4 }} />
                    <Typography component="div" variant="h6" sx={{ width: 2 / 4 }} paddingTop={1.5}>
                        {currencyFactor}
                    </Typography>
                    <Button onClick={() => {
                        setCurrencyCalculationEntries(currencyCalculationEntries.filter((_, i) => i !== index));
                    }} sx={{ width: 1 / 4, paddingTop: 1.5 }} > <FaMinus size={20} /> </Button>
                </Stack>
            </Stack>;
        })}
        <Button fullWidth onClick={() => setCurrencyCalculationEntries(currencyCalculationEntries.concat([{ factor: 10, currency: "", currencyShortForm: "" }]))} startIcon={<FaPlus />}> {t("universe:addAnotherCoin")}  </Button>
    </Stack>;
}
