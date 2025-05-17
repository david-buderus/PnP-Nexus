import { ReactNode, useEffect, useState } from "react";
import { useUniverseContext, useUserContext } from "../PageBase";
import { useTranslation } from "react-i18next";
import { useForm } from "@mantine/form";
import { CharacterSettings, CurrencySettings, JewelleryDefinition, UniverseCreationServiceApi, UniverseSettingsServiceApi } from "../../api";
import { Group, Stack, Title, Text, NumberInput, TextInput, Grid, ActionIcon, Button, Flex, Tooltip, Anchor, Modal, Alert } from "@mantine/core";
import { currencyFormatter, numberFormatter, percentageFormatter } from "../utils/Formatters";
import { randomId, useDisclosure } from "@mantine/hooks";
import { FaRegTrashCan } from "react-icons/fa6";
import { API_CONFIGURATION } from "../Constants";
import { handleNetworkErrors, handleValidationErrors } from "../utils/ErrorUtils";
import { probabilityForSuccesfulThrows } from "../utils/DiceThrowUtils";
import { fetchAllPrimaryAttributes } from "../Database";
import LanguageSelect from "../input/LanguageSelect";
import axios, { AxiosError } from "axios";


const SETTINGS_API = new UniverseSettingsServiceApi(API_CONFIGURATION);
const UNIVERSE_CREATION_API = new UniverseCreationServiceApi(API_CONFIGURATION);

export default function CharacterSettingsForm({
    onSave, onSaveText, alternativeButton
}: {
    onSave: () => void;
    onSaveText: string;
    alternativeButton?: ReactNode;
}) {
    const { t } = useTranslation();
    const { activeUniverse } = useUniverseContext();
    const form = useForm<CharacterSettings>({
        mode: 'controlled',
        initialValues: {
            jewelleryDefinitions: []
        }
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
        <form onSubmit={form.onSubmit((settings) => SETTINGS_API.updateCharacterSettings(activeUniverse.name, settings).then(onSave)
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
                    <NumberInput
                        label={t("universe:numberOfHandheld")}
                        key={form.key("numberOfHandheld")}
                        {...form.getInputProps("numberOfHandheld")}
                        allowDecimal={false}
                    />
                </Grid.Col >
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
            </Grid >
            <Stack pt="lg">
                <Title order={5} ta="center">
                    {t('universe:jewelleryDefinitions')}
                </Title>
                <Text ta='center'>
                    {t("universe:jewelleryDefinitionsExplanation")}
                    {" "}
                    <JewelleryImportModal setJewelleryDefinition={definitions => form.setFieldValue("jewelleryDefinitions", definitions)} />
                </Text>
                <Grid columns={4} justify="center">
                    {settings.jewelleryDefinitions.length > 0 ? (
                        <>
                            <Grid.Col span={1}>
                                <Text fw={500} size="sm" style={{ flex: 1 }}>
                                    {t("name")}
                                </Text>
                            </Grid.Col>
                            <Grid.Col span={1}>
                                <Text fw={500} size="sm" pr={160}>
                                    {t("tag")}
                                </Text>
                            </Grid.Col>
                            <Grid.Col span={1}>
                                <Text fw={500} size="sm" pr={160}>
                                    {t("amount")}
                                </Text>
                            </Grid.Col>
                            <Grid.Col span="content">
                                <ActionIcon size="lg" style={{ 'visibility': 'hidden' }} />
                            </Grid.Col>
                        </>
                    ) : (
                        <Grid.Col span={3}>
                            <Text c="dimmed" ta="center">
                                {t("nothing-here")}
                            </Text>
                        </Grid.Col>
                    )}
                    {form.getValues().jewelleryDefinitions.flatMap((item, index) => {
                        return [
                            <Grid.Col key={index + "-name"} span={1}>
                                <TextInput
                                    key={form.key(`jewelleryDefinitions.${index}.name`)}
                                    required
                                    {...form.getInputProps(`jewelleryDefinitions.${index}.name`)}
                                />
                            </Grid.Col>,
                            <Grid.Col key={index + "-tag"} span={1}>
                                <TextInput
                                    key={form.key(`jewelleryDefinitions.${index}.tag`)}
                                    required
                                    {...form.getInputProps(`jewelleryDefinitions.${index}.tag`)}
                                />
                            </Grid.Col>,
                            <Grid.Col key={index + "-amount"} span={1}>
                                <NumberInput
                                    key={form.key(`jewelleryDefinitions.${index}.amount`)}
                                    required
                                    {...form.getInputProps(`jewelleryDefinitions.${index}.amount`)}
                                    allowDecimal={false}
                                />
                            </Grid.Col>,
                            <Grid.Col key={index + "-button"} span="content">
                                <ActionIcon variant="outline" size="lg" color="red" onClick={() => form.removeListItem('jewelleryDefinitions', index)}>
                                    <FaRegTrashCan size={16} />
                                </ActionIcon>
                            </Grid.Col>
                        ];
                    })}
                    <Grid.Col span={1}>
                        <Button
                            mt="md"
                            onClick={() =>
                                form.insertListItem('jewelleryDefinitions', { name: '', tag: '', amount: 1, key: randomId() })
                            }
                        >
                            {t("universe:addAnotherJewelleryDefinition")}
                        </Button>
                    </Grid.Col>
                </Grid>
            </Stack>

            <Group justify="flex-end" pt="md">
                {alternativeButton}
                <Button type="submit">
                    {onSaveText}
                </Button>
            </Group>
        </form>
    </Stack >;
}

function JewelleryImportModal({
    setJewelleryDefinition
}: {
    setJewelleryDefinition: (jewelleryDefinitions: JewelleryDefinition[]) => void;
}) {
    const { t } = useTranslation();
    const [opened, { open, close }] = useDisclosure(false);

    const { activeUniverse } = useUniverseContext();
    const { userPreferences } = useUserContext();

    const [language, setLanguage] = useState<string>(userPreferences?.language ?? null);
    const [error, setError] = useState(false);

    return (
        <>
            <Modal opened={opened} onClose={close} title={t("universe:jewelleryImportTitle")}>
                <Text ta='left'>
                    {t("universe:jewelleryDefinitionsExplanation")}
                </Text>
                <LanguageSelect
                    value={language}
                    onChange={setLanguage}
                />
                {error && <Alert variant="light" color="red" title={t("universe:importMissingRequirements")} />}
                <Group pt="md" justify="flex-end">
                    <Button data-testid="dialog-cancel" autoFocus onClick={close}>
                        {t('cancel')}
                    </Button>
                    <Button
                        variant="contained"
                        color="success"
                        disabled={!language}
                        onClick={() => {
                            UNIVERSE_CREATION_API.getDefaultJewelleryDefinitions(activeUniverse.name, language).then(response => {
                                setJewelleryDefinition(response.data);
                                close();
                            }).catch((err: Error | AxiosError) => {
                                if (!axios.isAxiosError(err)) {
                                    return;
                                }
                                if (err.response.status !== 400) {
                                    return;
                                }
                                setError(true);
                            });
                        }}>{t('universe:importDefaults')}</Button>
                </Group>
            </Modal>
            <Anchor onClick={open}>
                {t("universe:importDefaultJewelleryDefinitions")}
            </Anchor>
        </>
    );
}