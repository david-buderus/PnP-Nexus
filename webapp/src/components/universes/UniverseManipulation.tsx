import { Button, Checkbox, FormControlLabel, Grid, IconButton, Paper, Stack, Tooltip, Typography } from "@mui/material";
import { CurrencyCalculationEntry, Universe } from "../../api";
import { useEffect, useState } from "react";
import { TextFieldWithError } from "../inputs/TestFieldWithError";
import { useTranslation } from "react-i18next";
import { FaMinus, FaPlus } from "react-icons/fa";

export interface UniverseManipulationProps {
    name: string,
    universe: Universe,
    setUniverse: (universe: Universe) => void;
    /** The current errors */
    errors: Map<string, string>;
}

export function UniverseManipulation(props: UniverseManipulationProps) {
    const { name, universe, setUniverse, errors } = props;
    const { t } = useTranslation();
    const [displayName, setDisplayName] = useState(universe?.displayName ?? "");
    const [shortDescription, setShortDescription] = useState(universe?.shortDescription ?? "");
    const [wearFactor, setWearFactor] = useState<string>(universe?.settings?.wearFactor?.toString() ?? "");
    const [wearFactorEnabled, setWearFactorEnabled] = useState(universe?.settings?.wearFactor > 0);
    const [baseCurrency, setBaseCurrency] = useState(universe?.settings?.currencyCalculation?.baseCurrency ?? "");
    const [baseCurrencyShortform, setBaseCurrencyShortform] = useState(universe?.settings?.currencyCalculation?.baseCurrencyShortForm ?? "");
    const [currencyCalculationEntries, setCurrencyCalculationEntries] = useState<CurrencyCalculationEntry[]>(universe?.settings?.currencyCalculation?.calculationEntries ?? []);

    useEffect(() => {
        setUniverse({
            name: name,
            displayName: displayName,
            shortDescription: shortDescription,
            settings: {
                wearFactor: wearFactorEnabled ? Number(wearFactor) : 0,
                currencyCalculation: {
                    baseCurrency: baseCurrency,
                    baseCurrencyShortForm: baseCurrencyShortform,
                    calculationEntries: currencyCalculationEntries
                }
            }
        });
    }, [displayName, shortDescription, wearFactor, wearFactorEnabled, baseCurrency, baseCurrencyShortform, currencyCalculationEntries]);

    return <Stack spacing={2}>
        <TextFieldWithError fieldId="displayName" errorMap={errors} value={displayName} onChange={setDisplayName} label={t("universe:displayName")} fullWidth />
        <TextFieldWithError fieldId="shortDescription" errorMap={errors} value={shortDescription} onChange={setShortDescription} label={t("universe:shortDescription")} fullWidth multiline rows={2} />
        <Typography gutterBottom variant="h6" component="div">
            {t("universe:settings")}
        </Typography>
        <Stack direction="row" spacing={2}>
            <TextFieldWithError fieldId="settings.wearFactor" errorMap={errors} integerField value={wearFactor} onChange={setWearFactor} label={wearFactorEnabled ? t("universe:wearFactor") : t("universe:wearFactorDisabled")} fullWidth disabled={!wearFactorEnabled} />
            <Tooltip title={t("universe:wearFactorTooltip")} placement="right-start">
                <FormControlLabel
                    control={<Checkbox checked={wearFactorEnabled} onChange={event => setWearFactorEnabled(event.target.checked)} />}
                    label={<Typography fontSize={12}> {t("universe:wearFactorEnabled")} </Typography>}
                    labelPlacement="top"

                />
            </Tooltip>
        </Stack>
        <Typography gutterBottom variant="h6" component="div">
            {t("universe:currency")}
        </Typography>
        <Stack direction="row" spacing={2}>
            <TextFieldWithError fieldId="settings.currencyCalculation.baseCurrency" errorMap={errors} value={baseCurrency} onChange={setBaseCurrency} label={t("universe:baseCurrency")} fullWidth />
            <TextFieldWithError fieldId="settings.currencyCalculation.baseCurrencyShortForm" errorMap={errors} value={baseCurrencyShortform} onChange={setBaseCurrencyShortform} label={t("universe:baseCurrencyShortForm")} tooltip={t("universe:baseCurrencyShortFormTooltip")} />
        </Stack>
        {currencyCalculationEntries.map((entry, index) => {
            const fieldIdPrefix = "settings.currencyCalculation.calculationEntries[" + index + "].";
            const currencyFactor = t("universe:currencyFactor", {
                smallerCoin: (index === 0 ? baseCurrency : currencyCalculationEntries[index - 1].currency) || "???",
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
                <Stack direction="row" spacing={2} alignItems="center">
                    <TextFieldWithError fieldId={fieldIdPrefix + "factor"} errorMap={errors} value={entry.factor.toString()} onChange={factor => {
                        setCurrencyCalculationEntries(currencyCalculationEntries.map((e, i) => index !== i ? e : { factor: Number(factor), currency: e.currency, currencyShortForm: e.currencyShortForm }));
                    }} label={t("universe:calculationFactor")} sx={{ width: 1 / 4 }} />
                    <Typography component="div" variant="h6" sx={{ width: 2 / 4 }}>
                        {currencyFactor}
                    </Typography>
                    <Button onClick={() => setCurrencyCalculationEntries(currencyCalculationEntries.filter((_, i) => i !== index))} sx={{ width: 1 / 4 }} > <FaMinus size={20} /> </Button>
                </Stack>
            </Stack>;
        })}
        <Button fullWidth onClick={() => setCurrencyCalculationEntries(currencyCalculationEntries.concat([{ factor: 10, currency: "", currencyShortForm: "" }]))} startIcon={<FaPlus />}> {t("universe:addAnotherCoin")}  </Button>
    </Stack>;
}