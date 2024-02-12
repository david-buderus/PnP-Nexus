import React, { useState } from 'react';
import { getUniverseContext, getUserContext } from '../components/PageBase';
import { Box, Card, CardActionArea, CardContent, Grid, Paper, Stack, Typography, styled } from '@mui/material';
import { useTranslation } from 'react-i18next';
import { FaPlus } from "react-icons/fa";
import { UniverseCreationDialog } from '../components/universes/UniverseCreationDialog';
import { Link, useSearchParams } from "react-router-dom";

function Home() {
    const { t } = useTranslation();
    const [searchParams] = useSearchParams();
    const { universes, setActiveUniverse, fetchUniverses } = getUniverseContext();
    const { userPermissions } = getUserContext();

    const [openUniverseCreationDialog, setOpenUniverseCreationDialog] = useState(false);

    if (universes.length === 0 && !userPermissions.canCreateUniverses) {
        return <Grid
            container
            direction="column"
            justifyContent="flex-start"
            alignItems="center"
        >
            <Typography gutterBottom variant="h5" component="div" align='center'>
                {t("universe:noUniverses")}
            </Typography>
            <Typography gutterBottom variant="body2" component="div" align='center'>
                {t("universe:noUniversesDetails")}
            </Typography>
        </Grid>;
    }

    return <Box>
        <Grid container
            direction="row"
            justifyContent="center"
            alignItems="flex-start"
            spacing={3}
        >
            {universes.map(universe =>
                <Grid item key={universe.name}>
                    <Card >
                        <CardActionArea
                            sx={{ width: 250, height: 250 }}
                            onClick={() => setActiveUniverse(universe)}
                            component={Link}
                            to={{
                                pathname: "/universe",
                                search: searchParams.toString()
                            }}
                        >
                            <CardContent>
                                <Grid
                                    container
                                    direction="column"
                                    justifyContent="flex-start"
                                    alignItems="center"
                                >
                                    <Typography gutterBottom variant="h5" component="div" align='center'>
                                        {universe.displayName}
                                    </Typography>
                                    <Typography variant="body2" color="text.secondary">
                                        {universe.shortDescription}
                                    </Typography>
                                </Grid>
                            </CardContent>
                        </CardActionArea>
                    </Card>
                </Grid>
            )}
            {userPermissions.canCreateUniverses && <Grid item>
                <Card >
                    <CardActionArea sx={{ width: 250, height: 250 }} onClick={() => setOpenUniverseCreationDialog(true)}>
                        <CardContent>
                            <Grid
                                container
                                direction="column"
                                justifyContent="center"
                                alignItems="center"
                            >
                                <Typography gutterBottom variant="h5" component="div" align='center'>
                                    {t("universe:createUniverse")}
                                </Typography>
                                <FaPlus size={30} />
                            </Grid>
                        </CardContent>
                    </CardActionArea>
                </Card>
            </Grid>}
        </Grid>
        <UniverseCreationDialog open={openUniverseCreationDialog} onClose={(event, reason) => {
            if (reason === "successful") {
                fetchUniverses();
            }
            setOpenUniverseCreationDialog(false);
        }}
        />
    </Box>;
}

export default Home;
