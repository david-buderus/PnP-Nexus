import { useSearchParams } from "react-router-dom";
import { getUniverseContext, getUserContext } from '../../components/PageBase';
import { Alert, Box, Button, Dialog, DialogActions, DialogTitle, Stack, Tab, Tabs, Typography } from "@mui/material";
import { TextFieldWithError } from "../../components/inputs/InputFields";
import { ReactElement, useEffect, useState } from "react";
import { CharacterSettings, CurrencySettings, ItemSettings, PrimaryAttribute, PrimaryAttributeServiceApi, Universe, UniverseCreationServiceApi, UniverseServiceApi, UniverseSettingsServiceApi } from "../../api";
import { useTranslation } from "react-i18next";
import { API_CONFIGURATION } from "../../components/Constants";
import { handleValidationErrors } from "../../components/ErrorUtils";
import { FaMinus, FaPlus } from "react-icons/fa6";
import { LanguageSelect } from "../../components/inputs/NexusSelect";
import { SettingsProps } from "../../components/settings/SettingsProps";
import { CurrencySettingsDialogContent } from "../../components/settings/CurrencySettingsDialogContent";
import { ItemSettingsDialogContent } from "../../components/settings/ItemSettingsDialogContent";
import { CharacterSettingsDialogContent } from "../../components/settings/CharacterSettingsDialogContent";
import { SecondaryAttributeDialogContent } from "../../components/database/SecondaryAttributeDialogContent";
import { AxiosResponse } from "axios";

const UNIVERSE_API = new UniverseServiceApi(API_CONFIGURATION);
const UNIVERSE_CREATION_API = new UniverseCreationServiceApi(API_CONFIGURATION);
const SETTINGS_API = new UniverseSettingsServiceApi(API_CONFIGURATION);
const PRIMARY_ATTRIBUTE_API = new PrimaryAttributeServiceApi(API_CONFIGURATION);

export function UniverseCreation() {
    const { t } = useTranslation();
    const [step, setSteps] = useState(0);
    const [maxStep, setMaxStep] = useState(0);

    function nextStep() {
        setSteps(step + 1);
        setMaxStep(Math.max(maxStep, step + 1));
    }
    function previousStep() {
        setSteps(step - 1);
    }

    return <Box sx={{ width: '100%' }}>
        <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
            <Tabs
                value={step}
                onChange={(_, value) => setSteps(value)}
                aria-label="basic tabs"
            >
                <Tab label={t("universe:creationStep")} disabled={maxStep > 0} />
                <Tab label={t("universe:currencyStep")} disabled={maxStep < 1} />
                <Tab label={t("universe:itemStep")} disabled={maxStep < 2} />
                <Tab label={t("universe:importStep")} disabled={maxStep < 3} />
                <Tab label={t("universe:primaryAttributeStep")} disabled={maxStep < 4} />
                <Tab label={t("universe:characterStep")} disabled={maxStep < 5} />
                <Tab label={t("universe:secondaryAttributeStep")} disabled={maxStep < 6} />
                <Tab label={t("universe:lastStep")} disabled={maxStep < 7} />
            </Tabs>
        </Box>
        <TabPanel value={step} index={0}>
            <UniverseCreationStep onSave={nextStep} />
        </TabPanel>
        <TabPanel value={step} index={1}>
            <SettingsStep<CurrencySettings>
                nextStep={nextStep}
                previousStep={previousStep}
                getSettings={universe => SETTINGS_API.getCurrencySettings(universe)}
                updateSettings={(universe, settings) => SETTINGS_API.updateCurrencySettings(universe, settings)}
                createChild={props => <CurrencySettingsDialogContent {...props} />}
            />
        </TabPanel>
        <TabPanel value={step} index={2}>
            <SettingsStep<ItemSettings>
                nextStep={nextStep}
                previousStep={previousStep}
                getSettings={universe => SETTINGS_API.getItemSettings(universe)}
                updateSettings={(universe, settings) => SETTINGS_API.updateItemSettings(universe, settings)}
                createChild={props => <ItemSettingsDialogContent {...props} />}
            />
        </TabPanel>
        <TabPanel value={step} index={3}>
            <ImportDefaultsStep
                nextStep={nextStep}
                previousStep={previousStep} />
        </TabPanel>
        <TabPanel value={step} index={4}>
            <PrimaryAttributeStep
                nextStep={nextStep}
                previousStep={previousStep} />
        </TabPanel>
        <TabPanel value={step} index={5}>
            <SettingsStep<CharacterSettings>
                nextStep={nextStep}
                previousStep={previousStep}
                getSettings={universe => SETTINGS_API.getCharacterSettings(universe)}
                updateSettings={(universe, settings) => SETTINGS_API.updateCharacterSettings(universe, settings)}
                createChild={props => <CharacterSettingsDialogContent {...props} />}
            />
        </TabPanel>
        <TabPanel value={step} index={6}>
            <SecondaryAttributeDialogContent onSave={nextStep} />
        </TabPanel>
        <TabPanel value={step} index={7}>

        </TabPanel>
    </Box >;
}

interface TabPanelProps {
    children?: React.ReactNode;
    index: number;
    value: number;
}

function TabPanel(props: TabPanelProps) {
    const { children, value, index, ...other } = props;

    return <div
        role="tabpanel"
        hidden={value !== index}
        id={`simple-tabpanel-${index}`}
        aria-labelledby={`simple-tab-${index}`}
        {...other}
    >
        {value === index && <Box sx={{ p: 3 }}>{children}</Box>}
    </div>
        ;
}

interface StepProps {
    nextStep: () => void;
    previousStep: () => void;
}

function UniverseCreationStep({ onSave }: { onSave: () => void; }) {
    const { t } = useTranslation();
    const { setActiveUniverse, fetchUniverses } = getUniverseContext();

    const [universe, setUniverse] = useState<Universe>({
        name: "",
        displayName: "",
        shortDescription: "",
        description: ""
    });
    const [errors, setErrors] = useState<Map<string, string>>(new Map<string, string>());

    return <Stack spacing={2} padding={2}>
        <Typography gutterBottom variant="h3" component="div" align='center'>
            {t('universe:createUniverse')}
        </Typography>
        <Stack spacing={2} direction="row">
            <TextFieldWithError fieldId="name" errorMap={errors} value={universe.name} onChange={value => {
                setUniverse({
                    ...universe,
                    name: value
                });
            }} label={t("name")} fullWidth />
            <TextFieldWithError fieldId="displayName" errorMap={errors} value={universe.displayName} onChange={value => {
                setUniverse({
                    ...universe,
                    displayName: value
                });
            }} label={t("displayName")} fullWidth />
        </Stack>
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
        <Stack spacing={2} direction="row" justifyContent="flex-end">
            <Button color="warning" variant="outlined" autoFocus href="/">
                {t('cancel')}
            </Button>
            <Button color="primary" variant="outlined" onClick={() => {
                UNIVERSE_API.createUniverse(universe).then(() =>
                    fetchUniverses().then(() => setActiveUniverse(universe)).then(onSave)
                ).catch(handleValidationErrors(setErrors));
            }}>{t('add')}</Button>
        </Stack>
    </Stack>;
}

function SettingsStep<S>({
    nextStep, previousStep, getSettings, updateSettings, createChild
}: {
    getSettings: (universe: string) => Promise<AxiosResponse<S, any>>;
    updateSettings: (universe: string, s: S) => Promise<AxiosResponse<any, any>>;
    createChild: (props: SettingsProps<S>) => ReactElement<SettingsProps<S>>;
} & StepProps) {
    const { t } = useTranslation();
    const { activeUniverse } = getUniverseContext();
    const [settings, setSettings] = useState<S>();
    const [errors, setErrors] = useState<Map<string, string>>(new Map<string, string>());

    useEffect(() => {
        if (!activeUniverse) {
            return;
        }
        getSettings(activeUniverse.name).then(response => setSettings(response.data));
    }, [activeUniverse]);

    if (settings === undefined) {
        return <></>;
    }

    return <Stack justifyContent="center">
        {createChild({
            settings,
            setSettings,
            errors
        })}
        <Stack padding={2} spacing={2} direction="row" justifyContent="flex-end">
            <Button color="warning" variant="outlined" onClick={previousStep}>
                {t('previous')}
            </Button>
            <Button color="primary" variant="outlined" onClick={() => {
                updateSettings(activeUniverse.name, settings).then(nextStep).catch(handleValidationErrors(setErrors));
            }}>{t('next')}</Button>
        </Stack>
    </Stack>;
}

function ImportDefaultsStep({ nextStep, previousStep }: StepProps) {
    const { t } = useTranslation();
    const { activeUniverse } = getUniverseContext();
    const { userPreferences } = getUserContext();

    const [language, setLanguage] = useState<string>(userPreferences?.language ?? null);
    const [importedEquipmentTypes, setImportedEquipmentTypes] = useState(false);
    const [importedMaterials, setImportedMaterials] = useState(false);

    return <Stack padding={2} justifyContent="center">
        <Typography gutterBottom variant="h3" component="div" align='center'>
            {t('universe:importDefaultsStep')}
        </Typography>
        <Stack spacing={2} justifyContent="center" alignItems="center" >
            <Typography gutterBottom variant="body2" component="div" align='left' sx={{ width: 3 / 4 }}>
                {t("universe:startingExplanation")}
            </Typography>
            <LanguageSelect
                language={language}
                onChange={setLanguage}
            />
            <Typography gutterBottom variant="body2" component="div" align='left' sx={{ width: 3 / 4 }}>
                {t("universe:defaultItemTypesExplanation")}
            </Typography>
            <Typography gutterBottom variant="body2" component="div" align='left' sx={{ width: 3 / 4 }}>
                {t("universe:defaultMaterialsExplanation")}
            </Typography>
            <Button color="success" variant="outlined" disabled={importedMaterials || language === null} sx={{ minWidth: 300 }} onClick={() => {
                setImportedMaterials(true);
                UNIVERSE_CREATION_API.createDefaultMaterials(activeUniverse.name, language);
            }}>{importedMaterials ? t('universe:successfullyImported') : t('universe:importMaterials')}</Button>
        </Stack>
        <Stack spacing={2} direction="row" justifyContent="flex-end">
            <Button color="warning" variant="outlined" onClick={previousStep}>
                {t('previous')}
            </Button>
            <Button color="primary" variant="outlined" onClick={() => {
                nextStep();
            }}>{t('next')}</Button>
        </Stack>

    </Stack>;
}

function PrimaryAttributeStep({ nextStep, previousStep }: StepProps) {
    const { t } = useTranslation();
    const { activeUniverse } = getUniverseContext();

    const [attributes, setAttributes] = useState<PrimaryAttribute[]>(Array(8).fill({ shortName: "", name: "" }));
    const [errors, setErrors] = useState<Map<string, string>>(new Map<string, string>());

    useEffect(() => {
        if (!activeUniverse) {
            return;
        }
        PRIMARY_ATTRIBUTE_API.getAllPrimaryAttributes(activeUniverse.name).then(response => {
            if (response.data.length > 0) {
                setAttributes(response.data);
            }
        });
    }, [activeUniverse]);

    return <Stack padding={2} justifyContent="center">
        <Typography gutterBottom variant="h3" component="div" align='center'>
            {t('universe:primaryAttributesStep')}
        </Typography>
        <Stack spacing={2}>
            {attributes.map((entry, index) => {
                const fieldIdPrefix = "setAll.attributes[" + index + "].";

                return <Stack spacing={2} key={"attributes-stack-" + index} direction="row">
                    <TextFieldWithError
                        fieldId={fieldIdPrefix + "name"}
                        errorMap={errors}
                        value={entry.name}
                        label={t("name")}
                        fullWidth
                        onChange={name => {
                            setAttributes(attributes.map((e, i) => index !== i ? e : { ...e, name: name }));
                        }}
                    />
                    <TextFieldWithError
                        fieldId={fieldIdPrefix + "shortName"}
                        errorMap={errors}
                        value={entry.shortName}
                        label={t("character:shortName")}
                        sx={{ width: 1 / 4 }}
                        onChange={shortName => {
                            setAttributes(attributes.map((e, i) => index !== i ? e : { ...e, shortName: shortName }));
                        }}
                    />
                    <Button key={"fieldIdPrefix-sub-" + index} onClick={() => {
                        setAttributes(attributes.filter((_, i) => i !== index));
                    }} sx={{ width: 1 / 12, paddingTop: 1.5 }} > <FaMinus size={20} /> </Button>
                </Stack>;
            })}
            <Button
                key="attributes-add"
                fullWidth
                onClick={() => setAttributes(attributes.concat([{ shortName: "", name: "" }]))}
                startIcon={<FaPlus />}
            >
                {t("universe:addAnotherAttribute")}
            </Button>
        </Stack>
        <Stack spacing={2} direction="row" justifyContent="flex-end">
            <Button color="warning" variant="outlined" onClick={previousStep}>
                {t('previous')}
            </Button>
            <Button color="primary" variant="outlined" onClick={() => {
                PRIMARY_ATTRIBUTE_API.setAllPrimaryAttributes(activeUniverse.name, attributes).then(nextStep).catch(handleValidationErrors(setErrors));
            }}>{t('next')}</Button>
        </Stack>
    </Stack>;
}
