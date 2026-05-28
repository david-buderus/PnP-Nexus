import {NumberInput, NumberInputProps} from '@mantine/core';
import {StatsDto} from '../../../../../api/model';
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

    const totalDisplay = value?.totalValue !== undefined && value?.rawValue !== value?.totalValue ? ` (${value.totalValue})` : '';

    return <TableNumberInput
        value={value?.rawValue}
        defaultValue={defaultValue?.rawValue}
        onChange={n => onChange({
            ...value,
            rawValue: Number(n),
        })}
        rightSection={
            <span style={{
                opacity: 0.7,
                paddingLeft: '4px',
                pointerEvents: 'none',
                whiteSpace: 'nowrap'
            }}>
                {totalDisplay}
            </span>
        }
        rightSectionProps={{
            style: {
                position: 'static',
                width: 'auto',
                display: 'inline-flex',
                alignItems: 'center'
            }
        }}
        {...rest}
    />;
}
