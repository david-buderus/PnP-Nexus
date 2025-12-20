import {NumberInput, NumberInputProps} from '@mantine/core';
import {StatsDto} from '../../../../../api';
import {TableNumberInput} from './TableNumberInput';

/**
 * A {@link NumberInput} without any styling, so it looks like normal text in a table cell.
 */
export function TableStatsInput(props: {
    value?: StatsDto,
    defaultValue?: StatsDto
    onChange?: (value: StatsDto) => void,
} & Omit<NumberInputProps, 'value' | 'onChange' | 'defaultValue'>) {
    const {value, onChange, defaultValue, ...rest} = props;

    return <TableNumberInput
        value={value?.rawValue}
        defaultValue={defaultValue?.rawValue}
        onChange={n => onChange({
            ...value,
            rawValue: Number(n),
        })}
        {...rest}
    />;
}