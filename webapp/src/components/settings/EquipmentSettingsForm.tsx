import {ReactNode, useEffect, useState} from "react";
import {useUniverseContext, useUserContext} from "../PageBase";
import {useTranslation} from "react-i18next";
import {useForm} from "@mantine/form";
import {
    EquipmentSettings,
    JewelleryDefinition,
    UniverseCreationServiceApi,
    UniverseSettingsServiceApi
} from "../../api";
import {
    ActionIcon,
    Alert,
    Anchor,
    Button,
    Group,
    Modal,
    NumberInput,
    Stack,
    Table,
    Text,
    TextInput,
    Title
} from "@mantine/core";
import {randomId, useDisclosure} from "@mantine/hooks";
import {FaRegTrashCan} from "react-icons/fa6";
import {API_CONFIGURATION} from "../Constants";
import {handleNetworkErrors, handleValidationErrors} from "../utils/ErrorUtils";
import LanguageSelect from "../input/LanguageSelect";
import axios, {AxiosError} from "axios";


const SETTINGS_API = new UniverseSettingsServiceApi(API_CONFIGURATION);
const UNIVERSE_CREATION_API = new UniverseCreationServiceApi(API_CONFIGURATION);

export default function EquipmentSettingsForm({
    onSave, onSaveText, alternativeButton
}: {
    onSave: () => void;
    onSaveText: string;
    alternativeButton?: ReactNode;
}) {
    const {t} = useTranslation();
    const {activeUniverse, equipmentSettings} = useUniverseContext();
    const form = useForm<EquipmentSettings>({
        mode: 'controlled',
        initialValues: equipmentSettings
    });

    const settings = form.getValues();

    useEffect(() => {
        if (!activeUniverse) {
            return;
        }

        SETTINGS_API.getEquipmentSettings(activeUniverse.name).then(response => form.setValues(response.data)).catch(handleNetworkErrors);
    }, [activeUniverse]);

    return <Stack align="center">
        <Title order={3} ta="center">
            {t('universe:characterSettings')}
        </Title>
        <form
            onSubmit={form.onSubmit(s => SETTINGS_API.updateEquipmentSettings(activeUniverse.name, s).then(onSave)
                .catch(handleValidationErrors(form.setErrors)))}>
            <NumberInput
                label={t("universe:numberOfHandheld")}
                key={form.key("numberOfHandheld")}
                {...form.getInputProps("numberOfHandheld")}
                allowDecimal={false}
            />
            <Stack pt="lg">
                <Title order={5} ta="center">
                    {t('universe:jewelleryDefinitions')}
                </Title>
                <Text ta='center'>
                    {t("universe:jewelleryDefinitionsExplanation")}
                    {" "}
                    <JewelleryImportModal
                        setJewelleryDefinition={definitions => form.setFieldValue("jewelleryDefinitions", definitions)}/>
                </Text>
                <Table>
                    <Table.Thead>
                        <Table.Tr>
                            <Table.Th>{t("name")}</Table.Th>
                            <Table.Th>{t("tag")}</Table.Th>
                            <Table.Th>{t("amount")}</Table.Th>
                            <Table.Th></Table.Th>
                        </Table.Tr>
                    </Table.Thead>
                    <Table.Tbody>
                        {settings.jewelleryDefinitions.map((_, index) => {
                            return <Table.Tr key={index}>
                                <Table.Td>
                                    <TextInput
                                        key={form.key(`jewelleryDefinitions.${index}.name`)}
                                        required
                                        {...form.getInputProps(`jewelleryDefinitions.${index}.name`)}
                                    />
                                </Table.Td>
                                <Table.Td>
                                    <TextInput
                                        key={form.key(`jewelleryDefinitions.${index}.tag`)}
                                        required
                                        {...form.getInputProps(`jewelleryDefinitions.${index}.tag`)}
                                    />
                                </Table.Td>
                                <Table.Td>
                                    <NumberInput
                                        key={form.key(`jewelleryDefinitions.${index}.amount`)}
                                        required
                                        {...form.getInputProps(`jewelleryDefinitions.${index}.amount`)}
                                        allowDecimal={false}
                                    />
                                </Table.Td>
                                <Table.Td>
                                    <ActionIcon variant="outline" size="lg" color="red"
                                                onClick={() => form.removeListItem('jewelleryDefinitions', index)}>
                                        <FaRegTrashCan size={16}/>
                                    </ActionIcon>
                                </Table.Td>
                            </Table.Tr>;
                        })}
                    </Table.Tbody>
                    {settings.jewelleryDefinitions.length === 0 ?
                        <Table.Caption c="dimmed" ta="center">
                            {t("nothing-here")}
                        </Table.Caption> : null}
                </Table>
                <Group justify="flex-end">
                    <Button
                        mt="md"
                        onClick={() =>
                            form.insertListItem('jewelleryDefinitions', {name: '', tag: '', amount: 1, key: randomId()})
                        }
                    >
                        {t("universe:addAnotherJewelleryDefinition")}
                    </Button>
                </Group>
            </Stack>

            <Group justify="flex-end" pt="md">
                {alternativeButton}
                <Button type="submit">
                    {onSaveText}
                </Button>
            </Group>
        </form>
    </Stack>;
}

function JewelleryImportModal({
    setJewelleryDefinition
}: {
    setJewelleryDefinition: (jewelleryDefinitions: JewelleryDefinition[]) => void;
}) {
    const {t} = useTranslation();
    const [opened, {open, close}] = useDisclosure(false);

    const {activeUniverse} = useUniverseContext();
    const {userPreferences} = useUserContext();

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
                {error && <Alert variant="light" color="red" title={t("universe:importMissingRequirements")}/>}
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