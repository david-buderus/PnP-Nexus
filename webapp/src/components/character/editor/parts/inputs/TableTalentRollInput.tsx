import {NumberInput, NumberInputProps} from '@mantine/core';
import {TalentRollDto} from '../../../../../api';
import {TableNumberInput} from './TableNumberInput';

/**
 * A {@link NumberInput} without any styling, so it looks like normal text in a table cell.
 */
export function TableTalentRollInput(props: {
    value?: TalentRollDto,
    defaultValue?: TalentRollDto
    onChange?: (value: TalentRollDto) => void,
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