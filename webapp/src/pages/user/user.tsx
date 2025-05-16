import { useTranslation } from "react-i18next";
import { useUserContext } from "../../components/PageBase";
import { useEffect, useState } from "react";
import { AuthenticationServiceApi, PasswordChange, PnPUser, UserServiceApi } from "../../api";
import { Button, Group, Modal, PasswordInput, Stack, Text, TextInput } from "@mantine/core";
import { useForm } from "@mantine/form";
import { API_CONFIGURATION } from "../../components/Constants";
import { handleValidationErrors } from "../../components/utils/ErrorUtils";
import ConfirmationDialog from "../../components/modal/ConfirmationDialog";
import { useDisclosure } from "@mantine/hooks";

const USER_API = new UserServiceApi(API_CONFIGURATION);
const AUTH_API = new AuthenticationServiceApi(API_CONFIGURATION);

/** View to change data about the currently logged in user */
export function User() {
    const { t } = useTranslation();
    const { user, refreshUser } = useUserContext();

    const [editMode, setEditMode] = useState(false);

    const form = useForm<PnPUser>({
        mode: 'uncontrolled',
        initialValues: user
    });

    useEffect(() => {
        form.setValues(user);
    }, [user]);

    if (!user) {
        return <Text>
            You need to log in.
        </Text>;
    }

    return <Stack
        gap="xs"
        maw={300}
    >
        <TextInput
            label={t("username")}
            value={user.username}
            readOnly
        />
        <form
            onSubmit={form.onSubmit((editedUser) => USER_API.updateUser(user.username, editedUser)
                .then(() => {
                    setEditMode(false);
                    refreshUser();
                })
                .catch(handleValidationErrors(form.setErrors)))}
        >
            <TextInput
                label={t("displayName")}
                key={form.key("displayName")}
                {...form.getInputProps("displayName")}
                readOnly={!editMode}
            />
            <TextInput
                label={t("email")}
                key={form.key("email")}
                {...form.getInputProps("email")}
                readOnly={!editMode}
            />
            {editMode ?
                <Group wrap="nowrap" grow pt="xs">
                    <Button
                        onClick={() => setEditMode(false)}
                        variant="outline"
                    >
                        {t("cancel")}
                    </Button>
                    <Button
                        type="submit"
                    >
                        {t("save")}
                    </Button>
                </Group>
                : null}

        </form>
        {!editMode ?
            <Button
                onClick={() => setEditMode(true)}
            >
                {t("edit")}
            </Button>
            : null}
        <ChangePasswordDialog />
        <ConfirmationDialog
            title={t("user:confirmDeleteUser")}
            onConfirmation={() => USER_API.removeUser(user.username).then(() => window.location.reload())}
            openNode={open =>
                <Button
                    onClick={open}
                    color="red"
                    variant="outline"
                >
                    {t("user:deleteUser")}
                </Button>}
        />
    </Stack>;
}

function ChangePasswordDialog() {
    const { t } = useTranslation();
    const [opened, { open, close }] = useDisclosure(false);

    const [confirmPassword, setConfirmPassword] = useState("");
    const form = useForm<PasswordChange>({
        mode: 'controlled',
        initialValues: {
            oldPassword: '',
            newPassword: ''
        }
    });
    const passwordNotMatching = confirmPassword !== form.getValues().newPassword;

    return <>
        <Modal opened={opened} onClose={close} title={t("user:changePasswordTitle")} maw={300}>
            <form
                onSubmit={form.onSubmit((change) => AUTH_API.updatePassword(change)
                    .then(close).catch(handleValidationErrors(form.setErrors)))}
            >
                <PasswordInput
                    label={t("user:oldPassword")}
                    key={form.key("oldPassword")}
                    {...form.getInputProps("oldPassword")}
                />
                <PasswordInput
                    label={t("user:newPassword")}
                    key={form.key("newPassword")}
                    {...form.getInputProps("newPassword")}
                />
                <PasswordInput
                    label={t("user:confirmPassword")}
                    value={confirmPassword}
                    onChange={(event) => setConfirmPassword(event.target.value)}
                    error={passwordNotMatching && confirmPassword.length > 0 ? t("user:passwordNotMatching") : undefined}
                />
                <Group justify="flex-end" pt="md">
                    <Button autoFocus variant="outline" onClick={close}>
                        {t("cancel")}
                    </Button>
                    <Button type="submit" disabled={passwordNotMatching}>
                        {t("save")}
                    </Button>
                </Group>
            </form>
        </Modal>
        <Button onClick={open}        >
            {t("user:changePasswordTitle")}
        </Button>
    </>;
}