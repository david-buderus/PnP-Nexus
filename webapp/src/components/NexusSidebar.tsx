import { Button, CSSObject, Collapse, Divider, Drawer, IconButton, List, ListItem, ListItemButton, ListItemIcon, ListItemText, Theme, styled, useTheme } from "@mui/material";
import { useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { MdOutlineExpandLess, MdOutlineExpandMore } from "react-icons/md";
import { FiArrowLeftCircle, FiArrowRightCircle } from "react-icons/fi";

const drawerWidth = 240;

const DrawerHeader = styled('div')(({ theme }) => ({
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'flex-end',
    padding: theme.spacing(0, 1),
    // necessary for content to be below app bar
    ...theme.mixins.toolbar,
}));

const openedMixin = (theme: Theme): CSSObject => ({
    width: drawerWidth,
    transition: theme.transitions.create('width', {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.enteringScreen,
    }),
    overflowX: 'hidden',
});

const closedMixin = (theme: Theme): CSSObject => ({
    transition: theme.transitions.create('width', {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
    }),
    overflowX: 'hidden',
    width: `calc(${theme.spacing(7)} + 1px)`,
    [theme.breakpoints.up('sm')]: {
        width: `calc(${theme.spacing(8)} + 1px)`,
    },
});

const CustomDrawer = styled(Drawer, { shouldForwardProp: (prop) => prop !== 'open' })(
    ({ theme, open }) => ({
        width: drawerWidth,
        flexShrink: 0,
        whiteSpace: 'nowrap',
        boxSizing: 'border-box',
        ...(open && {
            ...openedMixin(theme),
            '& .MuiDrawer-paper': openedMixin(theme),
        }),
        ...(!open && {
            ...closedMixin(theme),
            '& .MuiDrawer-paper': closedMixin(theme),
        }),
    }),
);

export interface NexusAppBarProps {
    open?: boolean;
    entries: MenuEntryProps[];
    handleDrawerChange: () => void;
}

export interface MenuEntryProps {
    id: string;
    label: string;
    link: string;
    icon: React.ReactNode;
    subEntries?: SubMenuEntryProps[];
}

export interface SubMenuEntryProps {
    id: string;
    label: string;
    link: string;
    icon: React.ReactNode;
}

interface InternalMenuEntryProps extends MenuEntryProps {
    searchParams: URLSearchParams;
}

function MenuEntry(props: InternalMenuEntryProps) {
    const { id, label, link, icon, subEntries, searchParams } = props;
    const [open, setOpen] = useState(false);
    const hasSubEntries = subEntries !== undefined;

    const handleClick = () => {
        setOpen(!open);
    };

    if (!hasSubEntries) {
        return <ListItem key={id} data-testid={id} disablePadding >
            <ListItemButton
                onClick={handleClick}
                component={Link}
                to={{
                    pathname: link,
                    search: searchParams.toString()
                }}
            >
                <ListItemIcon>
                    {icon}
                </ListItemIcon>
                <ListItemText primary={label} />
            </ListItemButton>
        </ListItem>;
    }

    return <ListItem key={id} data-testid={id} disablePadding sx={{ display: 'block' }}>
        <ListItemButton
            onClick={handleClick}
            component={Link}
            to={{
                pathname: link,
                search: searchParams.toString()
            }}
        >
            <ListItemIcon>
                {icon}
            </ListItemIcon>
            <ListItemText primary={label} />
            {open ? <MdOutlineExpandLess /> : <MdOutlineExpandMore />}
        </ListItemButton>
        <Collapse in={open} timeout="auto" unmountOnExit>
            <List component="div" disablePadding>
                {subEntries.map(entry =>
                    <ListItemButton
                        sx={{ pl: 4 }}
                        component={Link}
                        to={{
                            pathname: entry.link,
                            search: searchParams.toString()
                        }}
                    >
                        <ListItemIcon>
                            {entry.icon}
                        </ListItemIcon>
                        <ListItemText primary={entry.label} />
                    </ListItemButton>
                )}
            </List>
        </Collapse>
    </ListItem>;
}

export function NexusSidebar(props: NexusAppBarProps) {
    const { open, entries, handleDrawerChange } = props;
    const [searchParams] = useSearchParams();

    return <CustomDrawer variant="permanent" open={open}>
        <DrawerHeader />
        <IconButton onClick={handleDrawerChange}>
            {open ? <FiArrowRightCircle /> : <FiArrowLeftCircle />}
        </IconButton>
        <List>
            {entries.map(entry => (
                <MenuEntry {...entry} searchParams={searchParams} />
            ))}
        </List>
    </CustomDrawer>;
}