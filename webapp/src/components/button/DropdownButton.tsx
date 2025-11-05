import {ActionIcon, Button, ButtonProps, Group, Menu, MenuItemProps} from '@mantine/core';
import {FaChevronDown} from 'react-icons/fa6';
import {ReactNode} from 'react';
import {Link, To} from 'react-router-dom';

/** Props for the dropdown button */
export type DropdownButtonProps = ButtonProps & {
    /** Label used for the primary button */
    label: ReactNode;
    /** On click handler for the primary button */
    onClick?: () => void;
    /** The data testid */
    'data-testid'?: string;
    /** Props for the dropdown items */
    dropdownItems: (MenuItemProps & {
        label: string;
        onClick?: () => void;
        link?: To;
        'data-testid'?: string;
    })[]
};

/** A button with a dropdown button which contains a menu with different clickable options. */
export function DropdownButton({
    label,
    onClick,
    variant,
    size = 'sm',
    dropdownItems,
    'data-testid': dataTestId,
    ...props
}: DropdownButtonProps) {
    return <Group wrap="nowrap" gap={0} data-testid={dataTestId}>
        <Button
            variant={variant}
            size={size}
            onClick={onClick}
            data-testid="main-button"
            {...props}
            style={{
                borderTopRightRadius: 0,
                borderBottomRightRadius: 0
            }}
        >
            {label}
        </Button>
        <Menu transitionProps={{transition: 'pop'}} position="bottom-end" withinPortal data-testid="dropdownMenu">
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
                    const {label: itemLabel, onClick: onItemClick, link, ...itemProps} = item;

                    if (link) {
                        return <Menu.Item
                            key={index}
                            component={Link}
                            to={link}
                            onClick={onItemClick}
                            {...itemProps}
                        >
                            {itemLabel}
                        </Menu.Item>;
                    }

                    return <Menu.Item key={index} onClick={onItemClick} {...itemProps}>
                        {itemLabel}
                    </Menu.Item>;
                })}
            </Menu.Dropdown>
        </Menu>
    </Group>;
}