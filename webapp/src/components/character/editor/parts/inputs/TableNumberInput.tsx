import {NumberInput, NumberInputProps} from '@mantine/core';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';

/**
 * A {@link NumberInput} without any styling, so it looks like normal text in a table cell.
 */
export function TableNumberInput(props: NumberInputProps) {
    return <NumberInput
        {...props}
        unstyled
        styles={{
            root: {
                width: '100%',
                display: 'flex',
                justifyContent: 'flex-end', // Aligns values to the right like your example
            },
            wrapper: {
                width: '100%',
                display: 'flex',
                flexDirection: 'row',
                justifyContent: 'flex-end',
                alignItems: 'center',
            },
            input: {
                padding: 0,
                margin: 0,
                border: 'none',
                outline: 'none',
                background: 'transparent',
                textAlign: 'right', // Aligns the raw number to the right

                // Flexible width for the input part
                width: 'auto',
                maxWidth: '60%',

                height: TABLE_ROW_HEIGHT - 6,
                lineHeight: `${TABLE_ROW_HEIGHT - 6}px`,

                fontSize: TABLE_STYLE.fontSize,
                overflow: 'visible', // Prevents cutting off the total value
                whiteSpace: 'nowrap',
            },
            section: {
                // Ensure the rightSection (totalValue) is aligned vertically
                height: TABLE_ROW_HEIGHT - 6,
                display: 'flex',
                alignItems: 'center',
                fontSize: TABLE_STYLE.fontSize,
            }
        }}
    />;
}