import { useForm } from "@mantine/form";
import { useEffect, ReactNode } from "react";
import { useTranslation } from "react-i18next";
import { UniverseSettingsServiceApi, ItemSettings } from "../../api";
import { API_CONFIGURATION } from "../Constants";
import { useUniverseContext } from "../PageBase";
import { handleNetworkErrors, handleValidationErrors } from "../utils/ErrorUtils";
import { Button, Group, Stack, Title, Text, NumberInput, Checkbox } from "@mantine/core";

const SETTINGS_API = new UniverseSettingsServiceApi(API_CONFIGURATION);

export default function ItemSettingsForm({
    onSave, onSaveText, alternativeButton
}: {
    onSave: () => void;
    onSaveText: string;
    alternativeButton?: ReactNode;
}) {
    const { t } = useTranslation();
    const { activeUniverse } = useUniverseContext();
    const form = useForm<ItemSettings>({
        mode: 'controlled',
        initialValues: {
            shieldUsingDice: false,
            usingProtection: false,
            wearFactor: 0
        }
    });

    useEffect(() => {
        if (!activeUniverse) {
            return;
        }

        SETTINGS_API.getItemSettings(activeUniverse.name).then(response => form.setValues(response.data)).catch(handleNetworkErrors);
    }, [activeUniverse]);

    return <Stack align="stretch" justify="center" maw={500}>
        <Title order={3} ta="center">
            {t('universe:itemSettings')}
        </Title>
        <form
            onSubmit={form.onSubmit((settings) => SETTINGS_API.updateItemSettings(activeUniverse.name, {
                ...settings
            }).then(onSave).catch(handleValidationErrors(form.setErrors)))}
        >
            <NumberInput
                label={t("universe:wearFactor")}
                key={form.key("wearFactor")}
                {...form.getInputProps("wearFactor")}
                allowDecimal={false}
            />
            <Text ta='left' size="xs">
                {t("universe:wearFactorTooltip")}
            </Text>
            <Checkbox
                mt="md"
                label={t("universe:shieldUsingDice")}
                key={form.key('shieldUsingDice')}
                {...form.getInputProps('shieldUsingDice', { type: 'checkbox' })}
            />
            <Checkbox
                mt="md"
                label={t("universe:usingProtection")}
                key={form.key('usingProtection')}
                {...form.getInputProps('usingProtection', { type: 'checkbox' })}
            />
            <Group justify="flex-end" pt="md">
                {alternativeButton}
                <Button type="submit">
                    {onSaveText}
                </Button>
            </Group>
        </form>
    </Stack>;
}