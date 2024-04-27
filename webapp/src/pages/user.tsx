import { useTranslation } from "react-i18next";
import { getUserContext } from "../components/PageBase";
import { Button, Dialog, DialogActions, DialogTitle, Stack, TextField, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import { TextFieldWithError } from "../components/inputs/TestFieldWithError";
import { NexusSelect } from "../components/inputs/NexusSelect";
import { AuthenticationServiceApi, PnPUser, PnPUserPreference, UserServiceApi } from "../api";
import { API_CONFIGURATION } from "../components/Constants";
import { handleValidationError } from "../components/ErrorUtils";
import { AxiosResponse } from "axios";

const USER_API = new UserServiceApi(API_CONFIGURATION);
const AUTH_API = new AuthenticationServiceApi(API_CONFIGURATION);

/**
 * View to manipulate the profile of the current logged-in user.
 */
export function UserProfile() {
    const { t } = useTranslation();
    const { user } = getUserContext();

    const [editMode, setEditMode] = useState(false);
    const [editUser, setEditUser] = useState<PnPUser>(user);
    const [errors, setErrors] = useState<Map<string, string>>(new Map<string, string>());
    const [openChangePassword, setOpenChangePassword] = useState(false);

    useEffect(() => {
        setEditUser(user);
    }, [user]);

    if (!user || !editUser) {
        return <Typography>
            You need to log in.
        </Typography>;
    }

    return <Stack spacing={2} padding={2} width={300}>
        <TextField label={t("username")} data-testid="username" value={user.username} InputProps={{ readOnly: !editMode }} />
        <TextFieldWithError fieldId="displayname" label={t("displayName")} value={editUser.displayName ?? ""} onChange={value => setEditUser({
            ...editUser,
            displayName: value
        })} errorMap={errors} InputProps={{ readOnly: !editMode }} />
        <TextFieldWithError fieldId="email" label={t("email")} value={editUser.email ?? ""} onChange={value => setEditUser({
            ...editUser,
            email: value
        })} errorMap={errors} InputProps={{ readOnly: !editMode }} />
        <Button variant="outlined" data-testid="change-password" onClick={() => setOpenChangePassword(true)}>
            {t('user:changePasswordTitle')}
        </Button>
        <ControlButtons
            editMode={editMode}
            setEditMode={setEditMode}
            setErrors={setErrors}
            onCancel={() => setEditUser(user)}
            onSave={() => USER_API.updateUser(user.username, editUser)}
        />
        <ChangePasswordDialog
            open={openChangePassword}
            onClose={() => setOpenChangePassword(false)}
        />
    </Stack>;
}

/**
 * View to manipulate the preferences of the current logged-in user.
 */
export function UserPreferences() {
    const { t } = useTranslation();
    const { user, userPreferences } = getUserContext();

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

    return <Stack spacing={2} padding={2} width={300}>
        <TextField data-testid="username" label={t("username")} value={user.username} inputProps={{ readOnly: !editMode }} />
        <NexusSelect label={t("language")} value={editPreferences?.language} inputProps={{ readOnly: !editMode }} error={errors.has("language")}
            data-testid="language"
            onChange={event => setEditPreferences({
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
        <ControlButtons
            editMode={editMode}
            setEditMode={setEditMode}
            setErrors={setErrors}
            onCancel={() => setEditPreferences(userPreferences)}
            onSave={() => USER_API.updateUserPreferences(user.username, editPreferences)}
        />
    </Stack>;
}

/** Props for the control buttons */
interface ControlButtonsProps {
    /** Indicates if the menu is in edit mode */
    editMode: boolean;
    /** Sets the edit mode */
    setEditMode: (mode: boolean) => void;
    /** Callback for errors */
    setErrors: (errors: Map<string, string>) => void;
    /** Callback for on cancel */
    onCancel: () => void;
    /** Callback for on save */
    onSave: () => Promise<AxiosResponse<void, any>>;
}

function ControlButtons({
    editMode,
    setEditMode,
    setErrors,
    onCancel,
    onSave
}: ControlButtonsProps) {
    const { t } = useTranslation();
    const { refreshUser } = getUserContext();

    if (!editMode) {
        return <Button variant="outlined" onClick={() => {
            setEditMode(true);
        }} data-testid="edit"> {t("edit")}</Button>;
    }

    return <Stack spacing={2} direction="row" justifyContent="flex-end">
        <Button variant="outlined" color="secondary" style={{ width: 100 }} onClick={() => {
            setEditMode(false);
            onCancel();
        }} data-testid="cancel"> {t("cancel")}</Button>
        <Button variant="contained" style={{ width: 100 }} onClick={() => {
            onSave().then(() => {
                refreshUser();
                setEditMode(false);
            }).catch(handleValidationError(setErrors));
        }} data-testid="save"> {t("save")}</Button>
    </Stack>;
}

/** Props the change password dialog */
interface ChangePasswordDialogProps {
    /** If the dialog is open */
    open: boolean;
    /** On close handler */
    onClose: (event: unknown, reason: "backdropClick" | "escapeKeyDown" | "cancel" | "successful") => void;
}

function ChangePasswordDialog({
    open,
    onClose
}: ChangePasswordDialogProps) {
    const { t } = useTranslation();

    const [oldPassword, setOldPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [errors, setErrors] = useState<Map<string, string>>(new Map<string, string>());

    const passwordError = new Map();

    if (confirmPassword !== newPassword) {
        passwordError.set("confirmPassword", t("user:passwordNotMatching"));
    }

    return <Dialog open={open} onClose={onClose} fullWidth data-testid="change-password-dialog">
        <DialogTitle>{t('user:changePasswordTitle')}</DialogTitle>
        <Stack spacing={2} className="p-2">
            <TextFieldWithError
                label={t("user:oldPassword")}
                fieldId="oldPassword"
                value={oldPassword}
                onChange={setOldPassword}
                errorMap={errors}
                type="password"
            />
            <TextFieldWithError
                label={t("user:newPassword")}
                fieldId="newPassword"
                value={newPassword}
                onChange={setNewPassword}
                errorMap={errors}
                type="password"
            />
            <TextFieldWithError
                label={t("user:confirmPassword")}
                fieldId="confirmPassword"
                value={confirmPassword}
                onChange={setConfirmPassword}
                errorMap={passwordError}
                type="password"
            />
        </Stack>
        <DialogActions>
            <Button onClick={() => onClose({}, "cancel")}>
                {t('cancel')}
            </Button>
            <Button autoFocus disabled={confirmPassword !== newPassword} onClick={() => {
                AUTH_API.updatePassword({
                    oldPassword: oldPassword,
                    newPassword: newPassword
                }).then(() => onClose({}, "successful")).catch(handleValidationError(setErrors));
            }} data-testid="save-password">{t('save')}</Button>
        </DialogActions>
    </Dialog>;
}
