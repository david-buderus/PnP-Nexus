import { TextInput, TextInputProps } from "@mantine/core";
import { Dice } from "../../api";
import { useState } from "react";
import { diceFormatter } from "../utils/Formatters";
import { useTranslation } from "react-i18next";

export interface DiceInputProps extends Omit<TextInputProps, "value" | "onChange"> {
    /** The current value of the text field. */
    value?: Dice,
    /** On change hook for the value. */
    onChange?: (value: Dice) => void;
}

export default function DiceInput({
    value, onChange, ...rest
}: DiceInputProps) {
    const { t } = useTranslation();
    const [stringValue, setStringValue] = useState(value === undefined ? "" : diceFormatter(value));
    const [validDice, setValidDice] = useState(true);

    return <TextInput
        value={stringValue}
        onChange={event => {
            setStringValue(event.currentTarget.value);

            const newDice = parseDice(event.currentTarget.value);
            onChange(newDice);
            setValidDice(newDice !== null);
        }}
        {...rest}
        error={validDice ? undefined : t("error:notDice")}
    />;
}

const IS_DICE = /^\s*([1-9][0-9]*)?\s*D[1-9][0-9]*(\s*\+\s*([1-9][0-9]*)?\s*D[1-9][0-9]*)*\s*$/;

function parseDice(s: string): Dice {
    if (!s) {
        return {
            dices: []
        };
    }
    if (!IS_DICE.test(s)) {
        return null;
    }

    return {
        dices: s.split("+").map(d => d.split("D").map(d => d.trim())).map(d => {
            return {
                numberOfThrows: d[0] ? Number(d[0]) : 1,
                dice: Number(d[1])
            };
        })
    };
}