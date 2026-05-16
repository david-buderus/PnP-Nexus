import {Indicator, Tooltip} from '@mantine/core';
import React from 'react';

/** Indicator for errors for table input fields */
export function TableErrorIndicator({label, children}: {
    label?: React.ReactNode,
    children?: React.ReactNode | React.ReactNode[]
}) {
    return <Tooltip label={label} disabled={!label}>
        <Indicator color="red" position="middle-start" disabled={!label}>
            {children}
        </Indicator>
    </Tooltip>;
}