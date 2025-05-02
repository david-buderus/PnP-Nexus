import { Button, Container, TextInput, Title, Text, Anchor, Paper, PasswordInput, Group, Checkbox } from "@mantine/core";
import { useForm } from "@mantine/form";
import axios from "axios";
import { useTranslation } from "react-i18next";
import { useNavigate, useSearchParams } from "react-router-dom";


export default function Login() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();

    function tryLogin(username: string, password: string) {
        axios.postForm("/login", {
            username: username,
            password: password
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
        },
        validate: {
            password: (value) => searchParams.get("error") ? t("wrongPassword") : null
        }
    });

    return (
        <Container size={420} my={40}>
            <Title ta="center">
                P&P Nexus
            </Title>
            <Text c="dimmed" size="sm" ta="center" mt={5}>
                Do not have an account yet?{' '}
                <Anchor size="sm" component="button">
                    Create account
                </Anchor>
            </Text>

            <Paper withBorder shadow="md" p={30} mt={30} radius="md">
                <form onSubmit={form.onSubmit((values) => tryLogin(values.username, values.password))}>
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
                        <Checkbox label="Remember me" />
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