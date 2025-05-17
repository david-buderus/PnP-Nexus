import {Button, Grid, Group, Stack, Stepper, Text, Textarea, TextInput, Title} from "@mantine/core";
import {useState} from "react";
import {useTranslation} from "react-i18next";
import {API_CONFIGURATION} from "../../components/Constants";
import {Universe, UniverseCreationServiceApi, UniverseServiceApi} from "../../api";
import {useForm} from "@mantine/form";
import {useUniverseContext, useUserContext} from "../../components/PageBase";
import {handleValidationErrors} from "../../components/utils/ErrorUtils";
import CurrencySettingsForm from "../../components/settings/CurrencySettingsForm";
import ItemSettingsForm from "../../components/settings/ItemSettingsForm";
import LanguageSelect from "../../components/input/LanguageSelect";
import CharacterSettingsForm from "../../components/settings/CharacterSettingsForm";
import {PrimaryAttributeForm} from "../../components/character/PrimaryAttributeForm";
import {SecondaryAttributeForm} from "../../components/character/SecondaryAttributeForm";
import EquipmentSettingsForm from "../../components/settings/EquipmentSettingsForm";

const UNIVERSE_API = new UniverseServiceApi(API_CONFIGURATION);
const UNIVERSE_CREATION_API = new UniverseCreationServiceApi(API_CONFIGURATION);

/** The props needed for a universe creation step */
export interface UniverseCreationStepProps {
    /** Callback to go to the next step */
    nextStep: () => void;
    /** Callback to go to the previous step */
    prevStep: () => void;
}

/** A view to create universes with a wizard */
export default function UniverseCreation() {
    const {t} = useTranslation();
    const [active, setActive] = useState(0);
    const [highestStepVisited, setHighestStepVisited] = useState(active);
    const prevStep = () => setActive(current => (current > 0 ? current - 1 : current));
    const shouldAllowSelectStep = (step: number) => highestStepVisited >= step && active !== step;

    function nextStep() {
        if (active + 1 > 10) {
            return;
        }

        setActive(active + 1);
        setHighestStepVisited(Math.max(active + 1, highestStepVisited));
    }

    return (
        <>
            <Stepper active={active} onStepClick={setActive}>
                <Stepper.Step label={t("universe:creationStep")} allowStepSelect={false}>
                    <UniverseCreationStep nextStep={nextStep} prevStep={prevStep}/>
                </Stepper.Step>
                <Stepper.Step label={t("universe:currencyStep")} allowStepSelect={shouldAllowSelectStep(1)}>
                    <CurrencySettingsForm
                        onSave={nextStep}
                        onSaveText={t("next")}
                    />
                </Stepper.Step>
                <Stepper.Step label={t("universe:itemStep")} allowStepSelect={shouldAllowSelectStep(2)}>
                    <Group justify="center">
                        <ItemSettingsForm
                            onSave={nextStep}
                            onSaveText={t("next")}
                            alternativeButton={<Button onClick={prevStep}>{t("previous")}</Button>}
                        />
                    </Group>
                </Stepper.Step>
                <Stepper.Step label={t("universe:equipmentStep")} allowStepSelect={shouldAllowSelectStep(3)}>
                    <Group justify="center">
                        <EquipmentSettingsForm
                            onSave={nextStep}
                            onSaveText={t("next")}
                            alternativeButton={<Button onClick={prevStep}>{t("previous")}</Button>}
                        />
                    </Group>
                </Stepper.Step>
                <Stepper.Step label={t("universe:importStep")} allowStepSelect={shouldAllowSelectStep(4)}>
                    <Group justify="center">
                        <ItemImporStep
                            nextStep={nextStep}
                            prevStep={prevStep}
                        />
                    </Group>
                </Stepper.Step>
                <Stepper.Step label={t("universe:primaryAttributeStep")} allowStepSelect={shouldAllowSelectStep(5)}>
                    <Group justify="center">
                        <PrimaryAttributeStep
                            nextStep={nextStep}
                            prevStep={prevStep}
                        />
                    </Group>
                </Stepper.Step>
                <Stepper.Step label={t("universe:characterStep")} allowStepSelect={shouldAllowSelectStep(6)}>
                    <Group justify="center">
                        <CharacterSettingsForm
                            onSave={nextStep}
                            onSaveText={t("next")}
                            alternativeButton={<Button onClick={prevStep}>{t("previous")}</Button>}
                        />
                    </Group>
                </Stepper.Step>
                <Stepper.Step label={t("universe:secondaryAttributeStep")} allowStepSelect={shouldAllowSelectStep(7)}>
                    <Group justify="center">
                        <SecondaryAttributeStep
                            nextStep={nextStep}
                            prevStep={prevStep}
                        />
                    </Group>
                </Stepper.Step>
                <Stepper.Completed>
                    Completed, click back button to get to previous step
                </Stepper.Completed>
            </Stepper>
        </>
    );
}

function UniverseCreationStep({nextStep}: UniverseCreationStepProps) {
    const {t} = useTranslation();
    const form = useForm<Universe>({
        mode: 'uncontrolled',
        initialValues: {
            name: "",
            displayName: "",
            shortDescription: "",
            description: ""
        }
    });
    const {setActiveUniverse, fetchUniverses} = useUniverseContext();

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

function ItemImporStep({nextStep, prevStep}: UniverseCreationStepProps) {
    const {t} = useTranslation();
    const {activeUniverse} = useUniverseContext();
    const {userPreferences} = useUserContext();

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
            value={language}
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

function PrimaryAttributeStep({nextStep, prevStep}: UniverseCreationStepProps) {
    const {t} = useTranslation();

    return <Stack align="center">
        <Title order={3} ta="center">
            {t('universe:primaryAttributesStep')}
        </Title>
        <PrimaryAttributeForm
            onSave={nextStep}
            onSaveText={t("next")}
            alternativeButton={
                <Button onClick={prevStep}>
                    {t("previous")}
                </Button>
            }
        />
    </Stack>;
}

function SecondaryAttributeStep({nextStep, prevStep}: UniverseCreationStepProps) {
    const {t} = useTranslation();

    return <Stack align="center">
        <Title order={3} ta="center">
            {t('universe:primaryAttributesStep')}
        </Title>
        <SecondaryAttributeForm
            onSave={nextStep}
            onSaveText={t("next")}
            alternativeButton={
                <Button onClick={prevStep}>
                    {t("previous")}
                </Button>
            }
        />
    </Stack>;
}