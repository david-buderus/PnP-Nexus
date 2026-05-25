import {Dice} from '../../api/model';
import {diceFormatter} from '../utils/Formatters';
import {ReactNode} from 'react';
import {CellContext} from '@tanstack/table-core';

/** Renders a set of tags as a cell */
export default function DiceCell<T>(cell: CellContext<T, Dice>): ReactNode {
    return diceFormatter(cell.getValue());
}