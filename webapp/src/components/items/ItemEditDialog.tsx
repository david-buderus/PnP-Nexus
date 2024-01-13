import { Button, Dialog, DialogTitle, Stack } from "@mui/material";
import { useTranslation } from "react-i18next";
import { ItemServiceApi, ItemType, Material } from "../../api";
import { useState } from "react";
import { getUniverseContext } from "../PageBase";
import axios, { AxiosError } from "axios";
import { ItemManipulation } from "./ItemManipulation";
import { ItemClass, SomeItem } from "../Constants";

const ITEM_API = new ItemServiceApi();

/** Props needed for the dialog */
interface ItemCreationDialogProps {
    /** If the dialog is open */
    open: boolean;
    /** On close handler */
    onClose: (event: unknown, reason: "backdropClick" | "escapeKeyDown" | "succesful") => void;
    /** The known item types */
    itemTypes: ItemType[];
    /** The known materials */
    materials: Material[];
    /** Item which gets edited */
    itemToEdit: SomeItem;
}

/** Creates a dialog to edit an item */
export function ItemEditDialog(props: ItemCreationDialogProps) {
    const { open, onClose, itemTypes, materials, itemToEdit } = props;
    const { activeUniverse } = getUniverseContext();
    const { t } = useTranslation();

    const [errors, setErrors] = useState<Map<string, string>>(new Map<string, string>());
    const [item, setItem] = useState(itemToEdit);
    const itemClass = open ? item["@type"] as ItemClass : "Item";

    return <Dialog open={open} onClose={onClose} fullWidth>
        <DialogTitle>Set backup account</DialogTitle>
        <Stack spacing={2} className="p-2">
            <ItemManipulation
                itemTypes={itemTypes}
                materials={materials}
                itemClass={itemClass}
                errors={errors}
                item={item}
                setItem={setItem}
            />
            <div className='w-full pt-2'>
                <div className='float-right'>
                    <Button variant="contained" color="success" onClick={() => {
                        ITEM_API.updateItem(activeUniverse.name, item.id, item).then(() => onClose({}, "succesful")).catch((err: Error | AxiosError) => {
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
                    }}>
                        {t("edit")}
                    </Button>
                </div>
            </div>
        </Stack>
    </Dialog>;
}
