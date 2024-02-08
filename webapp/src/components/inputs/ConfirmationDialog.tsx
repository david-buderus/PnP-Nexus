import { Button, Dialog, DialogActions, DialogContent, DialogTitle } from "@mui/material";
import { useTranslation } from "react-i18next";

/** Props needed for the dialog */
interface ConfirmationDialogProps {
    /** title of the dialog */
    title: string;
    /** text shown in the dialog */
    text?: string;
    /** On close handler */
    onClose: (confirmation: boolean) => void;
    /** If the dialog is open */
    open: boolean;
}

/** A simple confirmation dialog */
export function ConfirmationDialog(props: ConfirmationDialogProps) {
    const { title, text, onClose, open } = props;
    const { t } = useTranslation();

    return <Dialog
        onClose={() => onClose(false)}
        maxWidth="xs"
        open={open}
    >
        <DialogTitle>{title}</DialogTitle>
        {text !== undefined &&
            <DialogContent>
                {text}
            </DialogContent>
        }
        <DialogActions>
            <Button autoFocus onClick={() => onClose(false)}>
                {t('cancel')}
            </Button>
            <Button onClick={() => onClose(true)}>{t('confirm')}</Button>
        </DialogActions>
    </Dialog>;
}
