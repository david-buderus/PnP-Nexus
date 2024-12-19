import { Dialog } from "@mui/material";
import { ReactNode } from "react";
import { createRoot } from "react-dom/client";

export type CloseReasons = "backdropClick" | "escapeKeyDown" | "successful" | "cancel";

export function openDialog(
    content: (close: (event: unknown, reason: CloseReasons) => void) => ReactNode,
    onClose?: (event: unknown, reason: CloseReasons) => void
) {
    const root = createRoot(document.createElement('div'));

    function onCloseWrapper(event: unknown, reason: CloseReasons) {
        root.unmount();
        if (onClose) {
            onClose(event, reason);
        }
    }

    root.render(
        <Dialog
            open={true}
            onClose={onCloseWrapper}
            fullWidth
        >
            {content(onCloseWrapper)}
        </Dialog>
    );
}