import { MRT_Cell } from "mantine-react-table";
import { currencyFormatter } from "../utils/Formatters";
import { useUniverseContext } from "../PageBase";

/** Renders a set of tags as a cell */
export default function CurrencyCell({ cell }: { cell: MRT_Cell<any, number>; }) {
    const { currencySettings } = useUniverseContext();
    return currencyFormatter(currencySettings, cell.getValue());
}