import {currencyFormatter} from '../utils/Formatters';
import {useUniverseContext} from '../PageBase';
import {CellContext} from '@tanstack/table-core';

/** Renders a set of tags as a cell */
export default function CurrencyCell<T>(cell: CellContext<T, number>) {
    const {currencySettings} = useUniverseContext();
    return currencyFormatter(currencySettings, cell.getValue());
}