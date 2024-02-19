import { useState } from "react";
import { useTranslation } from "react-i18next";
import { Universe, UniverseServiceApi } from "../../api";
import { Button, Dialog, DialogActions, DialogTitle, Box } from "@mui/material";
import { API_CONFIGURATION } from "../Constants";
import { UniverseManipulation } from "./UniverseManipulation";
import { handleValidationError } from "../ErrorUtils";

const UNIVERSE_API = new UniverseServiceApi(API_CONFIGURATION);

/** Props needed for the dialog */
interface UniverseEditDialogProps {
    /** If the dialog is open */
    open: boolean;
    /** On close handler */
    onClose: (event: unknown, reason: "backdropClick" | "escapeKeyDown" | "successful" | "cancel") => void;
    /** The universe which should be edited */
    universeToEdit: Universe;
}


/** Creates a dialog to create universes */
export function UniverseEditDialog(props: UniverseEditDialogProps) {
    const { open, onClose, universeToEdit } = props;
    const { t } = useTranslation();

    const [errors, setErrors] = useState<Map<string, string>>(new Map<string, string>());
    const [universe, setUniverse] = useState<Universe>(universeToEdit);

    return <Dialog open={open} onClose={onClose} fullWidth data-testid="universe-edit-dialog">
        <DialogTitle>{t('universe:editUniverse')}</DialogTitle>
        <Box className="p-2">
            <UniverseManipulation
                universe={universe}
                setUniverse={setUniverse}
                errors={errors}
            />
        </Box>
        <DialogActions>
            <Button autoFocus onClick={() => onClose({}, "cancel")}>
                {t('cancel')}
            </Button>
            <Button onClick={() => {
                UNIVERSE_API.updateUniverse(universe.name, universe).then(() => onClose({}, "successful")).catch(handleValidationError(setErrors));
            }}>{t('edit')}</Button>
        </DialogActions>
    </Dialog>;
}