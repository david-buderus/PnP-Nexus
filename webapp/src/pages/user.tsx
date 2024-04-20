import { useTranslation } from "react-i18next";
import { getUserContext } from "../components/PageBase";
import { Button, Stack, TextField, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import { TextFieldWithError } from "../components/inputs/TestFieldWithError";
import { NexusSelect } from "../components/inputs/NexusSelect";
import { PnPUser, PnPUserPreference, UserServiceApi } from "../api";
import { API_CONFIGURATION } from "../components/Constants";
import { handleValidationError } from "../components/ErrorUtils";

const USER_API = new UserServiceApi(API_CONFIGURATION);

/**
 * View to manipulate the profile of the current logged-in user.
 */
export function UserProfile() {
    const { t } = useTranslation();
    const { user, userPreferences, refreshUser } = getUserContext();

    const [editMode, setEditMode] = useState(false);
    const [editUser, setEditUser] = useState<PnPUser>(user);
    const [errors, setErrors] = useState<Map<string, string>>(new Map<string, string>());

    useEffect(() => {
        setEditUser(user);
    }, [user]);

    if (!user || !editUser) {
        return <Typography>
            You need to log in.
        </Typography>;
    }

    return <Stack spacing={2} padding={2}>
        <TextField label={t("username")} value={user.username} InputProps={{ readOnly: !editMode }} />
        <TextFieldWithError fieldId="displayname" label={t("displayName")} value={editUser.displayName ?? ""} onChange={value => setEditUser({
            ...editUser,
            displayName: value
        })} errorMap={errors} InputProps={{ readOnly: !editMode }} />
        <TextFieldWithError fieldId="email" label={t("email")} value={editUser.email ?? ""} onChange={value => setEditUser({
            ...editUser,
            email: value
        })} errorMap={errors} InputProps={{ readOnly: !editMode }} />
        {editMode ?
            <Stack spacing={2} direction="row">
                <Button onClick={() => {
                    setEditMode(false);
                    setEditUser(user);
                }}> {t("cancel")}</Button>
                <Button onClick={() => {
                    USER_API.updateUser(user.username, editUser).catch(handleValidationError(setErrors)).then(() => {
                        refreshUser();
                        setEditMode(false);
                    });
                }}> {t("save")}</Button>
            </Stack>
            :
            <Button onClick={() => {
                setEditMode(true);
            }}> {t("edit")}</Button>
        }

    </Stack>;
}

/**
 * View to manipulate the preferences of the current logged-in user.
 */
export function UserPreferences() {
    const { t } = useTranslation();
    const { user, userPreferences, refreshUser } = getUserContext();

    const [editMode, setEditMode] = useState(false);
    const [editPreferences, setEditPreferences] = useState<PnPUserPreference>(userPreferences);
    const [errors, setErrors] = useState<Map<string, string>>(new Map<string, string>());

    useEffect(() => {
        setEditPreferences(userPreferences);
    }, [userPreferences]);

    if (!user) {
        return <Typography>
            You need to log in.
        </Typography>;
    }

    return <Stack spacing={2} padding={2}>
        <TextField label={t("username")} value={user.username} InputProps={{ readOnly: !editMode }} />
        <NexusSelect label={t("language")} value={editPreferences.language} inputProps={{ readOnly: !editMode }} onChange={event => setEditPreferences({
            ...userPreferences,
            language: event.target.value
        })}
            values={[
                {
                    key: "en",
                    content: "en",
                    label: "English"
                },
                {
                    key: "de",
                    content: "de",
                    label: "Deutsch"
                }
            ]}
        />
        {editMode ?
            <Stack spacing={2} direction="row">
                <Button onClick={() => {
                    setEditMode(false);
                    setEditPreferences(userPreferences);
                }}> {t("cancel")}</Button>
                <Button onClick={() => {
                    USER_API.updateUserPreferences(user.username, editPreferences).catch(handleValidationError(setErrors)).then(() => {
                        refreshUser();
                        setEditMode(false);
                    });
                }}> {t("save")}</Button>
            </Stack>
            :
            <Button onClick={() => {
                setEditMode(true);
            }}> {t("edit")}</Button>
        }

    </Stack>;
}