import {MRT_Cell} from "mantine-react-table";
import {Dice} from "../../api";
import {diceFormatter} from "../utils/Formatters";
import {ReactNode} from "react";

/** Renders a set of tags as a cell */
export default function DiceCell({cell}: { cell: MRT_Cell<any, Dice>; }): ReactNode {
    return diceFormatter(cell.getValue());
}