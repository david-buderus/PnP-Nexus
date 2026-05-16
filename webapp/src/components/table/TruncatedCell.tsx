import {Text} from '@mantine/core';
import {CellContext} from '@tanstack/table-core';

/** Renders only the first two lines of the text */
export default function TruncatedCell<T>(cell: CellContext<T, string>) {
    return <Text lineClamp={2} style={{whiteSpace: 'pre-line'}}>
        {cell.getValue() ?? ''}
    </Text>;
}
