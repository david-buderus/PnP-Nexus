import { Grid, Typography } from "@mui/material";
import { useTranslation } from "react-i18next";

/**
 * Page to show on a universe specific page if no universe has been selected.
 */
export function NoUniverse() {
    const { t } = useTranslation();

    return <Grid
        container
        direction="column"
        justifyContent="flex-start"
        alignItems="center"
    >
        <Typography gutterBottom variant="h5" component="div" align='center'>
            {t("universe:noUniverseSelected")}
        </Typography>
        <Typography gutterBottom variant="body2" component="div" align='center'>
            {t("universe:noUniverseSelectedDetails")}
        </Typography>
    </Grid>;
}
