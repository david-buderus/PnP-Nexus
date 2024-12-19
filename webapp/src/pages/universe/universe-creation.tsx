import { useSearchParams } from "react-router-dom";
import { getUniverseContext, getUserContext } from '../../components/PageBase';
import { Alert, Button, Dialog, DialogActions, DialogTitle, Stack, Typography } from "@mui/material";
import { TextFieldWithError } from "../../components/inputs/TestFieldWithError";
import { useEffect, useState } from "react";
import { PrimaryAttribute, PrimaryAttributeServiceApi, Universe, UniverseCreationServiceApi, UniverseServiceApi } from "../../api";
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

const UNIVERSE_API = new UniverseServiceApi(API_CONFIGURATION);
const UNIVERSE_CREATION_API = new UniverseCreationServiceApi(API_CONFIGURATION);
const PRIMARY_ATTRIBUTE_API = new PrimaryAttributeServiceApi(API_CONFIGURATION);

export function UniverseCreation() {
    const [searchParams, setSearchParams] = useSearchParams();
    const step: number = Number(searchParams.get("step")) ?? 0;

    function nextStep() {
        searchParams.set("step", String(step + 1));
        setSearchParams(searchParams);
    }

    switch (step) {
        case 0:
            return <UniverseCreationStep onSave={nextStep} />;
        case 1:
            return <CurrencySettingsDialogContent onSave={nextStep} />;
        case 2:
            return <ItemSettingsDialogContent onSave={nextStep} />;
        case 3:
            return <ImportDefaultsStep onSave={nextStep} />;
        case 4:
            return <PrimaryAttributeStep onSave={nextStep} />;
        case 5:
            return <CharacterSettingsDialogContent onSave={nextStep} />;
        case 6:
            return <SecondaryAttributeDialogContent onSave={nextStep} />;
    }

    return <></>;
}

function UniverseCreationStep({ onSave: nextStep }: SettingsProps) {
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
                UNIVERSE_API.createUniverse(universe).then(() => {
                    fetchUniverses().then(() => setActiveUniverse(universe));
                    nextStep();
                }).catch(handleValidationErrors(setErrors));
            }}>{t('add')}</Button>
        </Stack>
    </Stack>;
}

function ImportDefaultsStep({ onSave: nextStep }: SettingsProps) {
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
            <Button color="success" variant="outlined" disabled={importedEquipmentTypes || language === null} sx={{ minWidth: 300 }} onClick={() => {
                setImportedEquipmentTypes(true);
                UNIVERSE_CREATION_API.createDefaultItemTypes(activeUniverse.name, language);
            }}>{importedEquipmentTypes ? t('universe:successfullyImported') : t('universe:importEquipmentTypes')}</Button>
            <Typography gutterBottom variant="body2" component="div" align='left' sx={{ width: 3 / 4 }}>
                {t("universe:defaultMaterialsExplanation")}
            </Typography>
            <Button color="success" variant="outlined" disabled={importedMaterials || language === null} sx={{ minWidth: 300 }} onClick={() => {
                setImportedMaterials(true);
                UNIVERSE_CREATION_API.createDefaultMaterials(activeUniverse.name, language);
            }}>{importedMaterials ? t('universe:successfullyImported') : t('universe:importMaterials')}</Button>
        </Stack>
        <Stack spacing={2} direction="row" justifyContent="flex-end">
            <Button color="warning" variant="outlined" autoFocus href="/">
                {t('cancel')}
            </Button>
            <Button color="primary" variant="outlined" onClick={() => {
                nextStep();
            }}>{t('next')}</Button>
        </Stack>

    </Stack>;
}

function PrimaryAttributeStep({ onSave }: SettingsProps) {
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
            <Button color="warning" variant="outlined" autoFocus href="/">
                {t('cancel')}
            </Button>
            <Button color="primary" variant="outlined" onClick={() => {
                PRIMARY_ATTRIBUTE_API.setAllPrimaryAttributes(activeUniverse.name, attributes).then(onSave).catch(handleValidationErrors(setErrors));
            }}>{t('save')}</Button>
        </Stack>
    </Stack>;
}
