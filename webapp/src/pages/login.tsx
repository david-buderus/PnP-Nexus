import { Alert, Button, CssBaseline, Stack, TextField, ThemeProvider, Typography } from "@mui/material";
import { THEME } from "../components/Constants";
import { useTranslation } from "react-i18next";
import { useNavigate, useSearchParams } from "react-router-dom";
import axios from "axios";
import { useState } from "react";

/** The login page */
export function Login() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    function tryLogin() {
        axios.postForm("/login", {
            username: username,
            password: password
        }).then(response => {
            const redirectUrl = new URL(response.request.responseURL);
            navigate(redirectUrl.pathname + redirectUrl.search);
        });
    }

    return <ThemeProvider theme={THEME}>
        <CssBaseline />
        <Stack
            justifyContent="center"
            alignItems="center"
            spacing={2}
            sx={{ flexGrow: 1, height: '100%' }}
        >
            <Typography gutterBottom variant="h2" component="div" align='center'>
                P&P Nexus
            </Typography>
            <TextField
                data-testid="username-field"
                content={username}
                onChange={event => setUsername(event.target.value)}
                id="username" name="username"
                label={t("username")}
                sx={{ width: 320 }}
                onKeyDown={(e) => {
                    if (e.key === 'Enter') {
                        tryLogin();
                    }
                }}
            />
            <TextField
                data-testid="password-field"
                content={password}
                onChange={event => setPassword(event.target.value)}
                id="password"
                name="password"
                type="password"
                label={t("password")}
                sx={{ width: 320 }}
                onKeyDown={(e) => {
                    if (e.key === 'Enter') {
                        tryLogin();
                    }
                }}
            />
            {searchParams.get("error") &&
                <Alert data-testid="login-alert" severity="error" sx={{ width: 320 }}>{t("wrongPassword")}</Alert>
            }
            <Button data-testid="login-button" variant="contained" sx={{ width: 320 }} onClick={tryLogin}>{t("logIn")}</Button>
        </Stack>
    </ThemeProvider>;
}