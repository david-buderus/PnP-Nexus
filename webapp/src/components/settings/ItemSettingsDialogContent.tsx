import { getUniverseContext } from '../PageBase';
import { Button, Checkbox, FormControlLabel, Stack, Tooltip, Typography } from "@mui/material";
import { NumberFieldWithError } from "../inputs/TestFieldWithError";
import { useEffect, useState } from "react";
import { ItemSettings, UniverseSettingsServiceApi } from "../../api";
import { useTranslation } from "react-i18next";
import { handleValidationErrors } from "../ErrorUtils";
import { SettingsProps } from "./SettingsProps";
import { API_CONFIGURATION } from "../Constants";

const SETTINGS_API = new UniverseSettingsServiceApi(API_CONFIGURATION);

export function ItemSettingsDialogContent({ onSave }: SettingsProps) {
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
        <Stack spacing={2} justifyContent="center">
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
                    sx={{ width: "40%" }} />
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
                        labelPlacement="top" />
                </Tooltip>
            </Stack>
            <Stack spacing={2} direction="row" justifyContent="flex-end">
                <Button color="warning" variant="outlined" autoFocus href="/">
                    {t('cancel')}
                </Button>
                <Button color="primary" variant="outlined" onClick={() => {
                    SETTINGS_API.updateItemSettings(activeUniverse.name, settings).then(onSave).catch(handleValidationErrors(setErrors));
                }}>{t('save')}</Button>
            </Stack>
        </Stack>
    </Stack>;
}
