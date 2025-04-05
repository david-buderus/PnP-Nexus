import { Checkbox, FormControlLabel, FormGroup, Stack, Typography } from "@mui/material";
import { NumberFieldWithError } from "../inputs/InputFields";
import { useEffect, useState } from "react";
import { ItemSettings } from "../../api";
import { useTranslation } from "react-i18next";
import { SettingsProps } from "./SettingsProps";

export function ItemSettingsDialogContent({ settings, setSettings, errors }: SettingsProps<ItemSettings>) {
    const { t } = useTranslation();
    const [wearFactorEnabled, setWearFactorEnabled] = useState(settings.wearFactor > 0);
    const [wearFactor, setWearFactor] = useState(settings.wearFactor);

    useEffect(() => {
        setSettings({
            ...settings,
            wearFactor: wearFactorEnabled ? wearFactor : -1
        });
    }, [wearFactor, wearFactorEnabled]);

    return <Stack padding={2} justifyContent="center">
        <Typography gutterBottom variant="h3" component="div" align='center'>
            {t('universe:itemSettings')}
        </Typography>
        <Stack spacing={2} justifyContent="center">
            <Typography gutterBottom variant="body2" component="div" align='center'>
                {t("universe:wearFactorTooltip")}
            </Typography>
            <Stack direction="row" justifyContent="center" spacing={2}>
                <NumberFieldWithError
                    fieldId="settings.wearFactor"
                    label={wearFactorEnabled ? t("universe:wearFactor") : t("universe:wearFactorDisabled")}
                    disabled={!wearFactorEnabled}
                    errorMap={errors}
                    integerField
                    value={wearFactor}
                    onChange={setWearFactor}
                    sx={{ width: "40%" }}
                />
                <FormControlLabel
                    control={<Checkbox checked={wearFactorEnabled} onChange={event => {
                        setWearFactorEnabled(event.target.checked);
                    }} />}
                    label={<Typography fontSize={12}> {t("universe:wearFactorEnabled")} </Typography>}
                    labelPlacement="top"
                />
            </Stack>
            <FormGroup>
                <FormControlLabel
                    control={<Checkbox checked={settings.shieldUsingDice} onChange={event => {
                        setSettings({
                            ...settings,
                            shieldUsingDice: event.target.checked
                        });
                    }} />}
                    label={<Typography fontSize={12}> {t("universe:shieldUsingDice")} </Typography>}
                    labelPlacement="end"
                    sx={{ justifyContent: "center" }}
                />
                <FormControlLabel
                    control={<Checkbox checked={settings.usingProtection} onChange={event => {
                        setSettings({
                            ...settings,
                            usingProtection: event.target.checked
                        });
                    }} />}
                    label={<Typography fontSize={12}> {t("universe:usingProtection")} </Typography>}
                    labelPlacement="end"
                    sx={{ justifyContent: "center" }}
                />
            </FormGroup>
        </Stack>
    </Stack>;
}
