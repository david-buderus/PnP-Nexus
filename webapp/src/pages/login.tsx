import { Alert, Button, CssBaseline, Stack, TextField, ThemeProvider, Typography } from "@mui/material";
import { THEME } from "../components/Constants";
import { useTranslation } from "react-i18next";
import { useSearchParams } from "react-router-dom";

/** The login page */
export function Login() {
    const { t } = useTranslation();
    const [searchParams] = useSearchParams();

    return <ThemeProvider theme={THEME}>
        <CssBaseline />
        <Stack
            justifyContent="center"
            alignItems="center"
            spacing={2}
            sx={{ flexGrow: 1, height: '100%' }}
            component="form"
            action="/login"
            method="post"
        >
            <Typography gutterBottom variant="h2" component="div" align='center'>
                P&P Nexus
            </Typography>
            <TextField id="username" name="username" label={t("username")} sx={{ width: 320 }} />
            <TextField id="password" name="password" type="password" label={t("password")} sx={{ width: 320 }} />
            {searchParams.get("error") &&
                <Alert severity="error" sx={{ width: 320 }}>{t("wrongPassword")}</Alert>
            }
            <Button variant="contained" type="submit" sx={{ width: 320 }}>{t("logIn")}</Button>
        </Stack>
    </ThemeProvider>;
}