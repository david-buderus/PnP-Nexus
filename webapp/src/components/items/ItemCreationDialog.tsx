import { Button, Dialog, DialogTitle, FormControl, MenuItem, Select, Stack } from "@mui/material";
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
    /** The initial shown item class */
    initialItemsClass: ItemClass;
}

/** Creates a dialog to create items */
export function ItemCreationDialog(props: ItemCreationDialogProps) {
    const { open, onClose, itemTypes, materials, initialItemsClass } = props;
    const { activeUniverse } = getUniverseContext();
    const { t } = useTranslation();

    const [errors, setErrors] = useState<Map<string, string>>(new Map<string, string>());
    const [item, setItem] = useState<SomeItem>(null);
    const [itemClass, setItemClass] = useState<ItemClass>(initialItemsClass);

    return <Dialog open={open} onClose={onClose} fullWidth>
        <DialogTitle>Set backup account</DialogTitle>
        <Stack spacing={2} className="p-2">
            <FormControl fullWidth>
                <Select
                    value={itemClass}
                    onChange={event => { setItemClass(event.target.value as ItemClass); }}
                >
                    {["Item", "Weapon", "Shield", "Armor", "Jewellery"].map(clazz => <MenuItem key={clazz} value={clazz}>{t(clazz.toLowerCase())}</MenuItem>)}
                </Select>
            </FormControl>
            <ItemManipulation
                itemTypes={itemTypes}
                materials={materials}
                itemClass={itemClass}
                errors={errors}
                setItem={setItem}
            />
            <div className='w-full pt-2'>
                <div className='float-right'>
                    <Button variant="contained" color="success" onClick={() => {
                        ITEM_API.insertAllItems(activeUniverse.name, [item]).then(() => onClose({}, "succesful")).catch((err: Error | AxiosError) => {
                            if (!axios.isAxiosError(err)) {
                                return;
                            }
                            if (err.response.status !== 400) {
                                return;
                            }
                            const errorMap = new Map<string, string>();
                            Object.entries(err.response.data).forEach(entry => {
                                const [key, value] = entry;
                                errorMap[key.substring("insertAll.objects[0].".length)] = value;
                            });
                            setErrors(errorMap);
                        });
                    }}>
                        {t("add")}
                    </Button>
                </div>
            </div>
        </Stack>
    </Dialog>;
}
