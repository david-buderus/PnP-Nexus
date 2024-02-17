import { CSSObject, Collapse, Drawer, IconButton, List, ListItem, ListItemButton, ListItemIcon, ListItemText, Theme, styled } from "@mui/material";
import { useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { MdOutlineExpandLess, MdOutlineExpandMore } from "react-icons/md";
import { FiArrowLeftCircle, FiArrowRightCircle } from "react-icons/fi";

const DRAWER_WIDTH = 240;

function openedMixin(theme: Theme): CSSObject {
    return {
        width: DRAWER_WIDTH,
        transition: theme.transitions.create('width', {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
        overflowX: 'hidden',
    };
}

function closedMixin(theme: Theme): CSSObject {
    return {
        transition: theme.transitions.create('width', {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen,
        }),
        overflowX: 'hidden',
        width: `calc(${theme.spacing(7)} + 1px)`,
        [theme.breakpoints.up('sm')]: {
            width: `calc(${theme.spacing(8)} + 1px)`,
        },
    };
}

const DrawerHeader = styled('div')(({ theme }) => ({
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'flex-end',
    padding: theme.spacing(0, 1),
    // necessary for content to be below app bar
    ...theme.mixins.toolbar,
}));

const CustomDrawer = styled(Drawer, { shouldForwardProp: (prop) => prop !== 'open' })(
    ({ theme, open }) => ({
        width: DRAWER_WIDTH,
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

/** Props for the app bar */
export interface NexusAppBarProps {
    collapsed?: boolean;
    entries: MenuEntryProps[];
    handleDrawerChange: () => void;
}

/** Definition of a menu */
export interface MenuEntryProps {
    id: string;
    label: string;
    link: string;
    icon: React.ReactNode;
    subEntries?: SubMenuEntryProps[];
}

/** Definition of a submenu */
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
                key={id + "_button"}
                onClick={handleClick}
                component={Link}
                to={{
                    pathname: link,
                    search: searchParams.toString()
                }}
            >
                <ListItemIcon key={id + "_icon"}>
                    {icon}
                </ListItemIcon>
                <ListItemText key={id + "_text"} primary={label} />
            </ListItemButton>
        </ListItem>;
    }

    return <ListItem key={id} data-testid={id} disablePadding sx={{ display: 'block' }}>
        <ListItemButton
            key={id + "_button"}
            onClick={handleClick}
            component={Link}
            to={{
                pathname: link,
                search: searchParams.toString()
            }}
        >
            <ListItemIcon key={id + "_icon"}>
                {icon}
            </ListItemIcon>
            <ListItemText key={id + "_text"} primary={label} />
            {open ? <MdOutlineExpandLess /> : <MdOutlineExpandMore />}
        </ListItemButton>
        <Collapse in={open} timeout="auto" unmountOnExit>
            <List key={id + "_submenu"} component="div" disablePadding>
                {subEntries.map(entry =>
                    <ListItemButton
                        key={entry.id}
                        data-testid={entry.id}
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

/** Creates the sidebar */
export function NexusSidebar(props: NexusAppBarProps) {
    const { collapsed, entries, handleDrawerChange } = props;
    const [searchParams] = useSearchParams();

    return <CustomDrawer variant="permanent" open={collapsed}>
        <DrawerHeader />
        <IconButton id="collapse_button" onClick={handleDrawerChange} >
            {collapsed ? <FiArrowLeftCircle /> : <FiArrowRightCircle />}
        </IconButton>
        <List id="entries">
            {entries.map(entry => (
                <MenuEntry {...entry} searchParams={searchParams} />
            ))}
        </List>
    </CustomDrawer>;
}