import {TextInput, TextInputProps} from '@mantine/core';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import {TableErrorIndicator} from './TableErrorIndicator';

/**
 * A {@link TextInput} without any styling, so it looks like normal text in a table cell.
 */
export function TableTextInput(props: TextInputProps) {
    const {error, ...otherProps} = props;

    return <TableErrorIndicator label={error}>
        <TextInput
            {...otherProps}
            unstyled
            styles={{
                root: {
                    width: '100%',
                },
                input: {
                    padding: 0,
                    margin: 0,
                    border: 'none',
                    outline: 'none',
                    background: 'transparent',
                    width: '100%',

                    // 6 is the padding of the TableCells
                    height: TABLE_ROW_HEIGHT - 6,
                    lineHeight: `${TABLE_ROW_HEIGHT - 6}px`,

                    fontSize: TABLE_STYLE.fontSize,
                    overflow: TABLE_STYLE.overflow,
                    textOverflow: TABLE_STYLE.textOverflow,
                    whiteSpace: TABLE_STYLE.whiteSpace,
                },
            }}
        />
    </TableErrorIndicator>;
}