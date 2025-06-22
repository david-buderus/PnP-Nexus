/** Style to highlight clicked parts */
const HIGHLIGHT_STYLE = {
    backgroundColor: 'rgba(173, 216, 230, 0.3)',
    border: '1px solid rgba(173, 216, 230, 0.6)',
    borderRadius: '4px',

    '@media print': {
        backgroundColor: 'transparent',
        border: 'none',
    }
};

/** Style to highlight empty layouts */
const EMPTY_STYLE = {
    backgroundColor: 'rgba(173, 216, 230, 0.3)',
    border: '1px solid rgba(173, 216, 230, 0.6)',
    borderRadius: '4px',
    minHeight: '40px', // ensures space to drop even if empty
};

/** Style for tables */
export const TABLE_STYLE = {
    padding: '3px 6px',
    fontSize: '10px'
};

/** The height for an empty table row */
export const EMPTY_TABLE_ROW_HEIGHT = 20;

/** Returns a style matching parameters */
export function getPartStyle(selected: boolean, empty?: boolean) {
    if (empty) {
        return EMPTY_STYLE;
    }
    if (selected) {
        return HIGHLIGHT_STYLE;
    }
    return undefined;
}