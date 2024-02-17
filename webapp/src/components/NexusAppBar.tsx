import { AppBar, Autocomplete, TextField, Toolbar, Typography, styled } from "@mui/material";
import { Universe } from "../api";
import { Link, useSearchParams } from "react-router-dom";

const CustomAppBar = styled(AppBar, {})(({ theme }) => ({
    zIndex: theme.zIndex.drawer + 1,
    transition: theme.transitions.create(['width', 'margin'], {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
    })
}));

/** The props for the app bar */
export interface NexusAppBarProps {
    universes: Universe[];
    activeUniverse: Universe;
    setActiveUniverse: (universe: Universe) => void;
}

/** Creates the app bar */
export function NexusAppBar(props: NexusAppBarProps) {
    const { universes, activeUniverse, setActiveUniverse } = props;
    const [searchParams] = useSearchParams();

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
        </Toolbar>
    </CustomAppBar>;
}