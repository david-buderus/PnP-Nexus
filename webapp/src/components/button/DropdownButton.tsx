import {ActionIcon, Button, ButtonProps, Group, Menu, MenuItemProps} from '@mantine/core';
import {FaChevronDown} from 'react-icons/fa6';
import {ReactNode} from 'react';

/** Props for the dropdown button */
export type DropdownButtonProps = ButtonProps & {
    /** Label used for the primary button */
    label: ReactNode;
    /** On clock handler for the primary button */
    onClick?: () => void;
    /** Props for the dropdown items */
    dropdownItems: (MenuItemProps & {
        label: string;
        onClick?: () => void;
    })[]
};

/** A button with a dropdown button which contains a menu with different clickable options. */
export function DropdownButton({
    label,
    onClick,
    variant,
    size = 'sm',
    dropdownItems,
    ...props
}: DropdownButtonProps) {
    return <Group wrap="nowrap" gap={0}>
        <Button
            variant={variant}
            size={size}
            onClick={onClick}
            {...props}
            style={{
                borderTopRightRadius: 0,
                borderBottomRightRadius: 0
            }}
        >
            {label}
        </Button>
        <Menu transitionProps={{transition: 'pop'}} position="bottom-end" withinPortal>
            <Menu.Target>
                <ActionIcon
                    variant={variant}
                    size={'input-' + size}
                    style={{
                        borderTopLeftRadius: 0,
                        borderBottomLeftRadius: 0,
                        borderLeft: 0
                    }}
                >
                    <FaChevronDown/>
                </ActionIcon>
            </Menu.Target>
            <Menu.Dropdown>
                {dropdownItems.map((item, index) => {
                    const {label: itemLabel, onClick: onItemClick, ...itemProps} = item;

                    return <Menu.Item key={index} onClick={onItemClick} {...itemProps}>
                        {itemLabel}
                    </Menu.Item>;
                })}
            </Menu.Dropdown>
        </Menu>
    </Group>;
}