import {ReactNode, useEffect} from "react";
import {useUniverseContext} from "../PageBase";
import {useTranslation} from "react-i18next";
import {useForm} from "@mantine/form";
import {CharacterSettings, UniverseSettingsServiceApi} from "../../api";
import {Button, Grid, Group, NumberInput, Stack, Text, Title} from "@mantine/core";
import {numberFormatter, percentageFormatter} from "../utils/Formatters";
import {API_CONFIGURATION} from "../Constants";
import {handleNetworkErrors, handleValidationErrors} from "../utils/ErrorUtils";
import {probabilityForSuccesfulThrows} from "../utils/DiceThrowUtils";
import {fetchAllPrimaryAttributes} from "../Database";


const SETTINGS_API = new UniverseSettingsServiceApi(API_CONFIGURATION);

export default function CharacterSettingsForm({
    onSave, onSaveText, alternativeButton
}: {
    onSave: () => void;
    onSaveText: string;
    alternativeButton?: ReactNode;
}) {
    const {t} = useTranslation();
    const {activeUniverse, characterSettings} = useUniverseContext();
    const form = useForm<CharacterSettings>({
        mode: 'controlled',
        initialValues: characterSettings
    });

    const [primaryAttributes] = fetchAllPrimaryAttributes();
    const attributeLength = primaryAttributes.length;
    const settings = form.getValues();

    useEffect(() => {
        if (!activeUniverse) {
            return;
        }

        SETTINGS_API.getCharacterSettings(activeUniverse.name).then(response => form.setValues(response.data)).catch(handleNetworkErrors);
    }, [activeUniverse]);

    return <Stack align="center">
        <Title order={3} ta="center">
            {t('universe:characterSettings')}
        </Title>
        <form onSubmit={form.onSubmit(s => SETTINGS_API.updateCharacterSettings(activeUniverse.name, s).then(onSave)
            .catch(handleValidationErrors(form.setErrors)))}>
            <Grid columns={2} gutter="5vw">
                <Grid.Col span={1}>
                    <NumberInput
                        label={t("universe:minPrimaryAttributeValue")}
                        key={form.key("minPrimaryAttributeValue")}
                        {...form.getInputProps("minPrimaryAttributeValue")}
                        allowDecimal={false}
                    />
                    <NumberInput
                        label={t("universe:maxPrimaryAttributeValue")}
                        key={form.key("maxPrimaryAttributeValue")}
                        {...form.getInputProps("maxPrimaryAttributeValue")}
                        allowDecimal={false}
                    />
                    <NumberInput
                        label={t("universe:maxPrimaryAttributeSum")}
                        key={form.key("maxPrimaryAttributeSum")}
                        {...form.getInputProps("maxPrimaryAttributeSum")}
                        allowDecimal={false}
                    />
                </Grid.Col>
                <Grid.Col span={1}>
                    <Text ta='left'>
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
                    </Text>
                </Grid.Col>
            </Grid>
            <Group justify="flex-end" pt="md">
                {alternativeButton}
                <Button type="submit">
                    {onSaveText}
                </Button>
            </Group>
        </form>
    </Stack>;
}