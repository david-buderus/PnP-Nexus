import { AppBar, Autocomplete, Box, IconButton, Menu, MenuItem, Stack, TextField, Toolbar, Typography, styled } from "@mui/material";
import { Universe } from "../api";
import { Link, useSearchParams } from "react-router-dom";
import { useState } from "react";
import { HiUserCircle } from "react-icons/hi2";
import { useTranslation } from "react-i18next";
import axios from "axios";

const CustomAppBar = styled(AppBar, {})(({ theme }) => ({
    zIndex: theme.zIndex.drawer + 1,
    transition: theme.transitions.create(['width', 'margin'], {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
    })
}));

/** The props for the app bar */
export interface NexusAppBarProps {
    /** All known universes */
    universes: Universe[];
    /** The currently active universe */
    activeUniverse: Universe;
    /** Callback to change the active universe */
    setActiveUniverse: (universe: Universe) => void;
}

/** Creates the app bar */
export function NexusAppBar(props: NexusAppBarProps) {
    const { universes, activeUniverse, setActiveUniverse } = props;
    const { t } = useTranslation();
    const [searchParams] = useSearchParams();

    const [userMenuAnchor, setUserMenuAnchor] = useState<null | HTMLElement>(null);

    const handleUserMenu = (event: React.MouseEvent<HTMLElement>) => {
        setUserMenuAnchor(event.currentTarget);
    };

    const handleUserMenuClose = () => {
        setUserMenuAnchor(null);
    };

    return <CustomAppBar position="fixed">
        <Toolbar>
            <Typography variant="h4" noWrap
                component={Link}
                to={{
                    pathname: "/",
                    search: searchParams.toString()
                }}
                sx={{
                    marginRight: 5
                }}>
                P&P Nexus
            </Typography>
            {universes.length > 0 &&
                <Autocomplete
                    className="float-left w-60 h-11"
                    data-testid="universe-selector"
                    options={universes}
                    getOptionLabel={(option: Universe) => {
                        return option?.displayName;
                    }}
                    isOptionEqualToValue={(option: Universe, value: Universe) => option.name === value.name}
                    renderInput={(params) => <TextField {...params} className='h-10' variant='standard' size="small" label="Universe" />}
                    onChange={(_, value) => {
                        setActiveUniverse(value);
                    }}
                    value={activeUniverse}
                />
            }
            <Stack direction="row" justifyContent="flex-end" sx={{ flexGrow: 1 }}>
                <IconButton
                    size="large"
                    aria-label="account of current user"
                    aria-controls="menu-appbar"
                    aria-haspopup="true"
                    onClick={handleUserMenu}
                    color="inherit"
                >
                    <HiUserCircle />
                </IconButton>
                <Menu
                    id="menu-appbar"
                    anchorEl={userMenuAnchor}
                    anchorOrigin={{
                        vertical: 'bottom',
                        horizontal: 'right',
                    }}
                    keepMounted
                    transformOrigin={{
                        vertical: 'bottom',
                        horizontal: 'right',
                    }}
                    open={Boolean(userMenuAnchor)}
                    onClose={handleUserMenuClose}
                >
                    <MenuItem onClick={handleUserMenuClose}>Profile</MenuItem>
                    <MenuItem onClick={handleUserMenuClose}>My account</MenuItem>
                    <MenuItem onClick={() => {
                        axios.post("/logout");
                        window.location.reload();
                    }}>{t("logout")}</MenuItem>
                </Menu>
            </Stack>
        </Toolbar>
    </CustomAppBar>;
}