import { Button, Group, Modal, Text } from "@mantine/core";
import { useDisclosure } from "@mantine/hooks";
import { ReactNode } from "react";
import { useTranslation } from "react-i18next";


/** Props for the confirmation dialog */
export interface ConfirmationDialogProps {
    /** The title of the dialog */
    title: string;
    /** The text of the dialog */
    text?: string;
    /** Callback if the user confirms */
    onConfirmation: () => void;
    /** The node used to open the dialog */
    openNode: (open: () => void) => ReactNode;
}

/** A simple confirmation dialog */
export default function ConfirmationDialog({
    title,
    text,
    onConfirmation,
    openNode
}: ConfirmationDialogProps) {
    const { t } = useTranslation();
    const [opened, { open, close }] = useDisclosure(false);

    return <>
        <Modal opened={opened} onClose={close} title={title} maw={300}>
            <Text>
                {text}
            </Text>
            <Group justify="flex-end">
                <Button autoFocus variant="outline" onClick={close}>
                    {t("cancel")}
                </Button>
                <Button type="submit" onClick={() => {
                    onConfirmation();
                    close();
                }}>
                    {t("confirm")}
                </Button>
            </Group>
        </Modal>
        {openNode(open)}
    </>;
}