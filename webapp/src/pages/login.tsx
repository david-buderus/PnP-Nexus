import {Anchor, Button, Checkbox, Container, Group, Paper, PasswordInput, TextInput, Title} from "@mantine/core";
import {useForm} from "@mantine/form";
import axios from "axios";
import {useTranslation} from "react-i18next";
import {useNavigate, useSearchParams} from "react-router-dom";

/** Login screen */
export default function Login() {
    const {t} = useTranslation();
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();

    function tryLogin(username: string, password: string, rememberMe: boolean) {
        axios.postForm("/login", {
            username: username,
            password: password,
            "remember-me": rememberMe
        }).then(response => {
            const redirectUrl = new URL(response.request.responseURL);
            navigate(redirectUrl.pathname + redirectUrl.search);
        });
    }

    const form = useForm({
        mode: 'uncontrolled',
        initialValues: {
            username: '',
            password: '',
            rememberMe: false
        },
        validate: {
            password: () => searchParams.get("error") ? t("wrongPassword") : null
        }
    });

    return (
        <Container size={420} my={40}>
            <Title ta="center">
                P&P Nexus
            </Title>

            <Paper withBorder shadow="md" p={30} mt={30} radius="md">
                <form
                    onSubmit={form.onSubmit((values) =>
                        tryLogin(values.username, values.password, values.rememberMe))}>
                    <TextInput
                        data-testid="username-field"
                        label={t("username")}
                        key={form.key('username')}
                        required
                        {...form.getInputProps('username')}
                    />
                    <PasswordInput
                        data-testid="password-field"
                        label={t("password")}
                        key={form.key('password')}
                        required
                        mt="md"
                        {...form.getInputProps('password')}
                    />
                    <Group justify="space-between" mt="lg">
                        <Checkbox
                            label="Remember me"
                            key={form.key('rememberMe')}
                            {...form.getInputProps('rememberMe', {type: "checkbox"})}
                        />
                        <Anchor component="button" size="sm">
                            Forgot password?
                        </Anchor>
                    </Group>
                    <Button fullWidth mt="xl" type="submit" data-testid="login-button">
                        {t("logIn")}
                    </Button>
                </form>
            </Paper>
        </Container>
    );
}