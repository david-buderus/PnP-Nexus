import React from 'react';
import { getUniverseContext, getUserContext } from '../components/PageBase';
import { Box, Card, CardActionArea, CardContent, Grid, Typography } from '@mui/material';

function Home() {
    const { activeUniverse, universes, setActiveUniverse } = getUniverseContext();
    const { userPermissions } = getUserContext();

    return <Box sx={{ flexGrow: 1 }}>
        <Grid container
            direction="row"
            justifyContent="center"
            alignItems="flex-start"
            spacing={5}>
            {universes.map(universe =>
                <Card sx={{ maxWidth: 345 }}>
                    <CardActionArea>
                        <CardContent>
                            <Typography gutterBottom variant="h5" component="div">
                                {universe.displayName}
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                                {universe.shortDescription}
                            </Typography>
                        </CardContent>
                    </CardActionArea>
                </Card>
            )}
        </Grid>
    </Box>;
}

export default Home;
