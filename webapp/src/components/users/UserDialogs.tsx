import { useTranslation } from "react-i18next";
import { getUniverseContext } from "../PageBase";
import { useEffect, useMemo, useState } from "react";
import { GrantedUniverseAuthorityDTO, PnPUser, PnPUserCreation, RoleAuthorityDTO, UserServiceApi } from "../../api";
import { Autocomplete, Button, Checkbox, Dialog, DialogActions, DialogTitle, FormControlLabel, FormGroup, Stack, TextField } from "@mui/material";
import { TextFieldWithError } from "../inputs/TestFieldWithError";
import { handleValidationError } from "../ErrorUtils";
import { API_CONFIGURATION } from "../Constants";

const USER_API = new UserServiceApi(API_CONFIGURATION);

/** Props needed for the dialog */
interface UserCreationDialogProps {
    /** If the dialog is open */
    open: boolean;
    /** On close handler */
    onClose: (event: unknown, reason: "backdropClick" | "escapeKeyDown" | "successful" | "cancel") => void;
}

/** Dialog to create users */
export function UserCreationDialog({ open, onClose }: UserCreationDialogProps) {
    const { t } = useTranslation();

    const [errors, setErrors] = useState<Map<string, string>>(new Map<string, string>());
    const [userCreation, setUserCreation] = useState<PnPUserCreation>({
        username: "",
        displayName: "",
        password: "",
        authorities: []
    });
    const [authorities, setAuthorities] = useState([]);

    return <Dialog open={open} onClose={onClose} fullWidth data-testid="user-creation-dialog">
        <DialogTitle>{t('user:createUser')}</DialogTitle>
        <Stack spacing={2} padding={2}>
            <TextFieldWithError fieldId="username" label={t("username")} value={userCreation.username} onChange={value => setUserCreation({
                ...userCreation,
                username: value
            })} errorMap={errors} />
            <TextFieldWithError fieldId="displayName" label={t("displayName")} value={userCreation.displayName} onChange={value => setUserCreation({
                ...userCreation,
                displayName: value
            })} errorMap={errors} />
            <TextFieldWithError fieldId="email" label={t("email")} value={userCreation.email} onChange={value => setUserCreation({
                ...userCreation,
                email: value
            })} errorMap={errors} />
            <TextFieldWithError fieldId="password" label={t("password")} value={userCreation.password} onChange={value => setUserCreation({
                ...userCreation,
                password: value
            })} errorMap={errors} type="password" />
            <PermissionManipulation authorities={[]} setAuthorities={setAuthorities} />
        </Stack>
        <DialogActions>
            <Button autoFocus onClick={() => onClose({}, "cancel")}>
                {t('cancel')}
            </Button>
            <Button onClick={() => {
                USER_API.createUser({
                    ...userCreation,
                    authorities: authorities
                }).then(() => onClose({}, "successful")).catch(handleValidationError(setErrors));
            }}>{t('create')}</Button>
        </DialogActions>
    </Dialog>;
}

/** Props needed for the dialog */
interface UserEditDialogProps {
    /** The user which gets edited */
    user: PnPUser,
    /** If the dialog is open */
    open: boolean;
    /** On close handler */
    onClose: (event: unknown, reason: "backdropClick" | "escapeKeyDown" | "successful" | "cancel") => void;
}

/** Dialog to edit users */
export function UserEditDialog({ user, open, onClose }: UserEditDialogProps) {
    const { t } = useTranslation();

    const [errors, setErrors] = useState<Map<string, string>>(new Map<string, string>());
    const [editUser, setEditUser] = useState<PnPUser>(user);
    const [orginialAuthorities, setOrginialAuthorities] = useState([]);
    const [editAuthorities, setEditAuthorities] = useState([]);

    useEffect(() => {
        if (user === null) {
            return;
        }
        USER_API.getPermissions(user.username).then(response => setOrginialAuthorities(response.data));
    }, [user]);

    return <Dialog open={open} onClose={onClose} fullWidth data-testid="user-edit-dialog">
        <DialogTitle>{t('user:editUser')}</DialogTitle>
        <Stack spacing={2} padding={2}>
            <TextField label={t("username")} data-testid="username" value={user?.username} InputProps={{ readOnly: true }} />
            <TextFieldWithError fieldId="displayName" label={t("displayName")} value={editUser?.displayName} onChange={value => setEditUser({
                ...editUser,
                displayName: value
            })} errorMap={errors} />
            <TextFieldWithError fieldId="email" label={t("email")} value={editUser?.email} onChange={value => setEditUser({
                ...editUser,
                email: value
            })} errorMap={errors} />
            <PermissionManipulation key={orginialAuthorities.toString()} authorities={orginialAuthorities} setAuthorities={setEditAuthorities} />
        </Stack>
        <DialogActions>
            <Button autoFocus onClick={() => onClose({}, "cancel")}>
                {t('cancel')}
            </Button>
            <Button onClick={() => {
                USER_API.updateUser(user.username, editUser).then(() => USER_API.updatePermissions(user.username, editAuthorities))
                    .then(() => onClose({}, "successful")).catch(handleValidationError(setErrors));
            }}>{t('edit')}</Button>
        </DialogActions>
    </Dialog>;
}

/** Props needed for the permission fields */
interface PermissionManipulationProps {
    /** Already existing authorities  */
    authorities: (GrantedUniverseAuthorityDTO | RoleAuthorityDTO)[];
    /** Callback for the currently selected authorities */
    setAuthorities: (authorities: (GrantedUniverseAuthorityDTO | RoleAuthorityDTO)[]) => void;
}

function PermissionManipulation({ authorities, setAuthorities }: PermissionManipulationProps) {
    const { t } = useTranslation();
    const { universes } = getUniverseContext();
    const universeOptions = useMemo(() => {
        return universes.map(universe => {
            return { label: universe.displayName, id: universe.name };
        });
    }, [universes]);

    function getUniverseRights(right: "READ" | "WRITE" | "OWNER") {
        return authorities.filter(auth => (auth as GrantedUniverseAuthorityDTO)?.permission === right)
            .map(auth => universeOptions.find(opt => opt.id === (auth as GrantedUniverseAuthorityDTO).universe));
    }

    const [adminRights, setAdminRights] = useState<boolean>(authorities.find(auth => (auth as RoleAuthorityDTO)?.role === "ADMIN") !== undefined);
    const [universeCreationRights, setUniverseCreationRights] = useState<boolean>(authorities.find(auth => (auth as RoleAuthorityDTO)?.role === "UNIVERSE_CREATOR") !== undefined);
    const [readRights, setReadRights] = useState(getUniverseRights("READ"));
    const [writeRights, setWriteRights] = useState(getUniverseRights("WRITE"));
    const [ownerRights, setOwnerRights] = useState(getUniverseRights("OWNER"));

    useEffect(() => {
        const newAuthorities = [];
        if (adminRights) {
            newAuthorities.push({
                "@type": "Role",
                "role": "ADMIN"
            });
        }
        if (!adminRights && universeCreationRights) {
            newAuthorities.push({
                "@type": "Role",
                role: "UNIVERSE_CREATOR"
            });
        }
        readRights.forEach(right => newAuthorities.push({
            "@type": "UniverseAuthority",
            universe: right.id,
            permission: "READ"

        }));
        writeRights.forEach(right => newAuthorities.push({
            "@type": "UniverseAuthority",
            universe: right.id,
            permission: "WRITE"

        }));
        ownerRights.forEach(right => newAuthorities.push({
            "@type": "UniverseAuthority",
            universe: right.id,
            permission: "OWNER"

        }));

        setAuthorities(newAuthorities);
    }, [adminRights, universeCreationRights, readRights, writeRights, ownerRights]);

    return <Stack spacing={2}>
        <FormGroup row>
            <FormControlLabel control={<Checkbox
                checked={adminRights}
                onChange={event => setAdminRights(event.target.checked)}
                inputProps={{ 'aria-label': 'controlled' }}
            />} label={t("user:adminRights")} />
            <FormControlLabel control={<Checkbox
                checked={universeCreationRights || adminRights}
                onChange={event => setUniverseCreationRights(event.target.checked)}
                disabled={adminRights}
                inputProps={{ 'aria-label': 'controlled' }}
            />} label={t("user:universeCreationRights")} />
        </FormGroup>
        <Autocomplete
            fullWidth
            multiple
            options={universeOptions}
            value={readRights}
            onChange={(_, value) =>
                setReadRights(value)
            }
            isOptionEqualToValue={(option, value) =>
                option?.id === value?.id
            }
            renderInput={(params) => <TextField
                {...params}
                label={t("user:universeReadRights")}
            />}
            data-testid="universe-read-rights"
        />
        <Autocomplete
            fullWidth
            multiple
            options={universeOptions}
            value={writeRights}
            onChange={(_, value) =>
                setWriteRights(value)
            }
            isOptionEqualToValue={(option, value) =>
                option?.id === value?.id
            }
            renderInput={(params) => <TextField
                {...params}
                label={t("user:universeWriteRights")}
            />}
            data-testid="universe-write-rights"
        />
        <Autocomplete
            fullWidth
            multiple
            options={universeOptions}
            value={ownerRights}
            onChange={(_, value) =>
                setOwnerRights(value)
            }
            isOptionEqualToValue={(option, value) =>
                option?.id === value?.id
            }
            renderInput={(params) => <TextField
                {...params}
                label={t("user:universeOwnerRights")}
            />}
            data-testid="universe-owner-rights"
        />
    </Stack>;
}