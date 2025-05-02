import { Button, Grid, Group, Stack, Stepper, Textarea, TextInput, Title, Text, ActionIcon } from "@mantine/core";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { API_CONFIGURATION } from "../../components/Constants";
import { PrimaryAttribute, PrimaryAttributeServiceApi, Universe, UniverseCreationServiceApi, UniverseServiceApi, UniverseSettingsServiceApi } from "../../api";
import { useForm } from "@mantine/form";
import { getUniverseContext, getUserContext } from "../../components/PageBase";
import { handleNetworkErrors, handleValidationErrors } from "../../components/utils/ErrorUtils";
import CurrencySettingsForm from "../../components/settings/CurrencySettingsForm";
import ItemSettingsForm from "../../components/settings/ItemSettingsForm";
import LanguageSelect from "../../components/input/LanguageSelect";
import { FaRegTrashCan } from "react-icons/fa6";
import { randomId } from "@mantine/hooks";

const UNIVERSE_API = new UniverseServiceApi(API_CONFIGURATION);
const UNIVERSE_CREATION_API = new UniverseCreationServiceApi(API_CONFIGURATION);
const PRIMARY_ATTRIBUTE_API = new PrimaryAttributeServiceApi(API_CONFIGURATION);

export interface UniverseCreationStepProps {
    nextStep: () => void;
    prevStep: () => void;
}

export default function UniverseCreation() {
    const { t } = useTranslation();
    const [active, setActive] = useState(0);
    const [highestStepVisited, setHighestStepVisited] = useState(active);
    const prevStep = () => setActive((current) => (current > 0 ? current - 1 : current));
    const shouldAllowSelectStep = (step: number) => highestStepVisited >= step && active !== step;

    function nextStep() {
        if (active + 1 > 5) {
            return;
        }

        setActive(active + 1);
        setHighestStepVisited(Math.max(active + 1, highestStepVisited));
    }

    return (
        <>
            <Stepper active={active} onStepClick={setActive}>
                <Stepper.Step label={t("universe:creationStep")} description="TODO" allowStepSelect={false}>
                    <UniverseCreationStep nextStep={nextStep} prevStep={prevStep} />
                </Stepper.Step>
                <Stepper.Step label={t("universe:currencyStep")} description="TODO" allowStepSelect={shouldAllowSelectStep(1)}>
                    <CurrencySettingsForm
                        onSave={nextStep}
                        onSaveText={t("next")}
                    />
                </Stepper.Step>
                <Stepper.Step label={t("universe:itemStep")} description="TODO" allowStepSelect={shouldAllowSelectStep(2)}>
                    <Group justify="center">
                        <ItemSettingsForm
                            onSave={nextStep}
                            onSaveText={t("next")}
                            alternativeButton={<Button onClick={prevStep}>{t("previous")}</Button>}
                        />
                    </Group>
                </Stepper.Step>
                <Stepper.Step label={t("universe:importStep")} description="TODO" allowStepSelect={shouldAllowSelectStep(3)}>
                    <Group justify="center">
                        <ItemImporStep
                            nextStep={nextStep}
                            prevStep={prevStep}
                        />
                    </Group>
                </Stepper.Step>
                <Stepper.Step label={t("universe:primaryAttributeStep")} description="TODO" allowStepSelect={shouldAllowSelectStep(3)}>
                    <PrimaryAttributeStep
                        nextStep={nextStep}
                        prevStep={prevStep}
                    />
                </Stepper.Step>
                <Stepper.Completed>
                    Completed, click back button to get to previous step
                </Stepper.Completed>
            </Stepper>
        </>
    );
}

function UniverseCreationStep({ nextStep }: UniverseCreationStepProps) {
    const { t } = useTranslation();
    const form = useForm<Universe>({
        mode: 'uncontrolled',
        initialValues: {
            name: "",
            displayName: "",
            shortDescription: "",
            description: ""
        }
    });
    const { setActiveUniverse, fetchUniverses } = getUniverseContext();

    return <Stack align="center">
        <Title order={3} ta="center">
            {t('universe:createUniverse')}
        </Title>
        <form onSubmit={form.onSubmit((universe) => UNIVERSE_API.createUniverse(universe).then(() =>
            fetchUniverses().then(() => setActiveUniverse(universe)).then(nextStep)
        ).catch(handleValidationErrors(form.setErrors)))}>
            <Grid columns={2}>
                <Grid.Col span={1}>
                    <TextInput
                        data-testid="name"
                        label={t("name")}
                        key={form.key('name')}
                        required
                        {...form.getInputProps('name')}
                    />
                </Grid.Col>
                <Grid.Col span={1}>
                    <TextInput
                        data-testid="displayName"
                        label={t("displayName")}
                        key={form.key('displayName')}
                        required
                        {...form.getInputProps('displayName')}
                    />
                </Grid.Col>
                <Grid.Col span={2}>
                    <Textarea
                        data-testid="shortDescription"
                        label={t("universe:shortDescription")}
                        autosize
                        minRows={2}
                        key={form.key('shortDescription')}
                        {...form.getInputProps('shortDescription')}
                    />
                </Grid.Col>
                <Grid.Col span={2}>
                    <Textarea
                        data-testid="description"
                        label={t("description")}
                        autosize
                        minRows={4}
                        key={form.key('description')}
                        {...form.getInputProps('description')}
                    />
                </Grid.Col>
                <Grid.Col span={2}>
                    <Button fullWidth mt="xl" type="submit" data-testid="add-button">
                        {t("add")}
                    </Button>
                </Grid.Col>
            </Grid>
        </form>
    </Stack>;
}

function ItemImporStep({ nextStep, prevStep }: UniverseCreationStepProps) {
    const { t } = useTranslation();
    const { activeUniverse } = getUniverseContext();
    const { userPreferences } = getUserContext();

    const [language, setLanguage] = useState<string>(userPreferences?.language ?? null);
    const [importedMaterials, setImportedMaterials] = useState(false);

    return <Stack align="center" maw={500}>
        <Title order={3} ta="center">
            {t('universe:importDefaultsStep')}
        </Title>
        <Text ta='left'>
            {t("universe:startingExplanation")}
        </Text>
        <LanguageSelect
            language={language}
            onChange={setLanguage}
        />
        <Text ta='left'>
            {t("universe:defaultMaterialsExplanation")}
        </Text>
        <Button color="success" variant="outlined" disabled={importedMaterials || language === null} onClick={() => {
            setImportedMaterials(true);
            UNIVERSE_CREATION_API.createDefaultMaterials(activeUniverse.name, language);
        }}>
            {importedMaterials ? t('universe:successfullyImported') : t('universe:importMaterials')}
        </Button>

        <Group justify="flex-end" pt="md">
            <Button onClick={prevStep}>
                {t("previous")}
            </Button>
            <Button type="submit" onClick={nextStep}>
                {t("next")}
            </Button>
        </Group>
    </Stack>;
}

function PrimaryAttributeStep({ nextStep, prevStep }: UniverseCreationStepProps) {
    const { t } = useTranslation();
    const { activeUniverse } = getUniverseContext();

    const form = useForm<{
        attributes: Array<PrimaryAttribute & { key: string; }>;
    }>({
        mode: 'uncontrolled',
        initialValues: {
            attributes: Array(8).fill({ shortName: "", name: "", key: randomId() })
        }
    });

    useEffect(() => {
        if (!activeUniverse) {
            return;
        }
        PRIMARY_ATTRIBUTE_API.getAllPrimaryAttributes(activeUniverse.name).then(response => {
            if (response.data.length > 0) {
                form.setValues({
                    attributes: response.data.map(a => {
                        return {
                            ...a,
                            key: randomId()
                        };
                    })
                });
            }
        }).catch(handleNetworkErrors);
    }, [activeUniverse]);

    return <Stack align="center">
        <Title order={3} ta="center">
            {t('universe:primaryAttributesStep')}
        </Title>
        <form onSubmit={form.onSubmit((attributes) => PRIMARY_ATTRIBUTE_API.setAllPrimaryAttributes(activeUniverse.name, attributes.attributes)
            .then(nextStep).catch(handleValidationErrors(form.setErrors))
        )}>
            {form.getValues().attributes.map((item, index) => {
                return <Group key={item.key} mt="xs">
                    <TextInput
                        label={t("name")}
                        key={form.key(`${index}.name`)}
                        required
                        {...form.getInputProps(`attributes.${index}.name`)}
                    />
                    <TextInput
                        label={t("character:shortName")}
                        key={form.key(`attributes.${index}.shortName`)}
                        required
                        {...form.getInputProps(`attributes.${index}.shortName`)}
                    />
                    <ActionIcon variant="outline" color="red" onClick={() => form.removeListItem('attributes', index)}>
                        <FaRegTrashCan size={16} />
                    </ActionIcon>
                </Group>;
            })}
            <Button
                mt="md"
                onClick={() =>
                    form.insertListItem('attributes', { name: '', shortName: '', key: randomId() })
                }
            >
                {t("universe:addAnotherAttribute")}
            </Button>
        </form>
    </Stack >;
}