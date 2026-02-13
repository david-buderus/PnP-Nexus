import React, {ReactNode} from 'react';
import {useNode} from '@craftjs/core';
import {Stack} from '@mantine/core';
import {getPartStyle} from '../Constants';
import {MantineSpacing} from '@mantine/core/lib/core';

/** A part which contains {@link Stack} */
export const StackPart = ({children, ...props}: {
    children?: ReactNode;
    gap?: MantineSpacing;
}) => {
    const {
        connectors: {connect, drag},
        selected,
    } = useNode(node => ({
        selected: node.events.selected,
    }));

    const isEmpty = React.Children.count(children) === 0;

    return (
        <div
            ref={(ref) => ref && connect(drag(ref))}
            style={getPartStyle(selected, isEmpty)}
        >
            <Stack {...props}>{children}</Stack>
        </div>
    );
};

StackPart.craft = {
    name: 'sheetEditor:vertical'
};