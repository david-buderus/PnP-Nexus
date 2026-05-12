import {ActionIcon, Popover, PopoverWidth} from '@mantine/core';
import React, {useContext} from 'react';
import {IconSettings} from '@tabler/icons-react';
import {PnPCharacterSheetContext} from '../../PnPCharacterSheetContext';

/** A popover behind a settings button in the top right corner */
export function PageElementSettings({
    children, width
}: {
    children: React.ReactNode | React.ReactNode[];
    width?: PopoverWidth
}) {
    const {allowEdit} = useContext(PnPCharacterSheetContext);

    if (!allowEdit) {
        return null;
    }

    return (
        <Popover width={width} position="bottom" withArrow shadow="md">
            <Popover.Target>
                <ActionIcon
                    variant="subtle"
                    size="sm"
                    className="no-drag"
                    style={{
                        position: 'absolute',
                        top: 2,
                        right: 20,
                        zIndex: 10, // Ensure it stays above everything
                    }}
                >
                    <IconSettings size={14}/>
                </ActionIcon>
            </Popover.Target>
            <Popover.Dropdown>
                {children}
            </Popover.Dropdown>
        </Popover>
    );
}