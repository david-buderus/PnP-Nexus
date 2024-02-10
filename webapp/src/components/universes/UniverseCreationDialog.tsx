import { useState } from "react";
import { useTranslation } from "react-i18next";
import { Universe, UniverseServiceApi } from "../../api";
import { Button, Dialog, DialogActions, DialogTitle, Stack } from "@mui/material";
import { API_CONFIGURATION } from "../Constants";
import axios, { AxiosError } from "axios";
import { UniverseManipulation } from "./UniverseManipulation";
import { TextFieldWithError } from "../inputs/TestFieldWithError";

const UNIVERSE_API = new UniverseServiceApi(API_CONFIGURATION);

/** Props needed for the dialog */
interface UniverseCreationDialogProps {
    /** If the dialog is open */
    open: boolean;
    /** On close handler */
    onClose: (event: unknown, reason: "backdropClick" | "escapeKeyDown" | "successful" | "cancel") => void;
}


/** Creates a dialog to create universes */
export function UniverseCreationDialog(props: UniverseCreationDialogProps) {
    const { open, onClose } = props;
    const { t } = useTranslation();

    const [errors, setErrors] = useState<Map<string, string>>(new Map<string, string>());
    const [name, setName] = useState<string>("");
    const [universe, setUniverse] = useState<Universe>(null);

    return <Dialog open={open} onClose={onClose} fullWidth data-testid="universe-creation-dialog">
        <DialogTitle>{t('universe:createUniverse')}</DialogTitle>
        <Stack spacing={2} className="p-2">
            <TextFieldWithError fieldId="name" errorMap={errors} value={name} onChange={setName} label={t("name")} fullWidth />
            <UniverseManipulation
                name={name}
                universe={universe}
                setUniverse={setUniverse}
                errors={errors}
            />
        </Stack>
        <DialogActions>
            <Button autoFocus onClick={() => onClose({}, "cancel")}>
                {t('cancel')}
            </Button>
            <Button onClick={() => {
                UNIVERSE_API.createUniverse(universe).then(() => onClose({}, "successful")).catch((err: Error | AxiosError) => {
                    if (!axios.isAxiosError(err)) {
                        return;
                    }
                    if (err.response.status !== 400) {
                        return;
                    }
                    const errorMap = new Map<string, string>();
                    Object.entries(err.response.data).forEach(entry => {
                        const [key, value] = entry;
                        errorMap[key] = value;
                    });
                    setErrors(errorMap);
                });
            }}>{t('add')}</Button>
        </DialogActions>
    </Dialog>;
}