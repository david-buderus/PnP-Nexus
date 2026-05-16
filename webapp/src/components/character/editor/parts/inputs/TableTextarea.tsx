import {Textarea, TextareaProps, Tooltip} from '@mantine/core';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';

/**
 * A {@link TableTextarea} without any styling, so it looks like normal text in a table cell.
 */
export function TableTextarea(props: TextareaProps) {
    const {error, ...otherProps} = props;

    return <Tooltip label={error} disabled={!error}>
        <Textarea
            {...otherProps}
            unstyled
            autosize={false}
            style={{height: '100%'}}
            styles={{
                root: {
                    width: '100%',
                    height: '100%'
                },
                wrapper: {
                    height: '100%',
                },
                input: {
                    padding: 0,
                    margin: 0,
                    border: 'none',
                    outline: 'none',
                    background: 'transparent',
                    width: '100%',
                    height: '100%',
                    resize: 'none',

                    lineHeight: `${TABLE_ROW_HEIGHT - 6}px`,

                    fontSize: TABLE_STYLE.fontSize,
                    overflow: TABLE_STYLE.overflow,
                    textOverflow: TABLE_STYLE.textOverflow,
                    whiteSpace: TABLE_STYLE.whiteSpace,
                },
            }}
        />
    </Tooltip>;
}