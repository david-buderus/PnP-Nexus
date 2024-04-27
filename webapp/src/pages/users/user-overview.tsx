import { Autocomplete, Button, Checkbox, Dialog, DialogActions, DialogTitle, FormControlLabel, FormGroup, Stack, TextField } from "@mui/material";
import OverviewTable from "../../components/OverviewTable";
import { PnPUser, PnPUserCreation, UserServiceApi } from "../../api";
import { API_CONFIGURATION } from "../../components/Constants";
import { useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { ConfirmationDialog } from "../../components/inputs/ConfirmationDialog";
import { handleValidationError } from "../../components/ErrorUtils";
import { TextFieldWithError } from "../../components/inputs/TestFieldWithError";
import { getUniverseContext } from "../../components/PageBase";

const USER_API = new UserServiceApi(API_CONFIGURATION);

/** Admin overview over all users */
export function UserOverview() {
    const { t } = useTranslation();

    const [users, setUsers] = useState<PnPUser[]>([]);
    const [selected, setSelected] = useState<PnPUser[keyof PnPUser][]>([]);

    const [openCreationDialog, setOpenCreationDialog] = useState(false);
    const [openDeleteDialog, setOpenDeleteDialog] = useState(false);

    useEffect(() => {
        USER_API.getAllUsers().then(response => setUsers(response.data));
    }, []);

    return <Stack spacing={2} padding={2}>
        <OverviewTable
            data={users}
            id="username"
            sortBy="username"
            selectedState={[selected, setSelected]}
            columns={[
                { label: t("username"), id: "username", getter: user => user.username },
                { label: t("displayName"), id: "displayName", getter: user => user.displayName },
                { label: t("email"), id: "email", getter: user => user.email }
            ]}
        />
        <Stack spacing={2} direction="row" justifyContent="flex-end">
            <Button className='btn' onClick={() => setOpenCreationDialog(true)}>
                {t("add")}
            </Button>
            <Button className='btn' disabled={selected.length === 0} onClick={() => setOpenDeleteDialog(true)}>
                {t("delete")}
            </Button>
            <UserCreationDialog open={openCreationDialog} onClose={sucessful => {
                setOpenCreationDialog(false);
                if (sucessful) {
                    USER_API.getAllUsers().then(response => setUsers(response.data));
                }
            }} />
            <ConfirmationDialog
                title={t('user:confirmDeletionUser')}
                open={openDeleteDialog}
                onClose={confirmation => {
                    setOpenDeleteDialog(false);
                    if (!confirmation) {
                        return;
                    }
                    USER_API.removeUsers(selected).then(sucessful => {
                        if (sucessful) {
                            USER_API.getAllUsers().then(response => setUsers(response.data));
                        }
                    });
                }}
            />
        </Stack>
    </Stack>;
}

/** Props needed for the dialog */
interface UserCreationDialogProps {
    /** If the dialog is open */
    open: boolean;
    /** On close handler */
    onClose: (event: unknown, reason: "backdropClick" | "escapeKeyDown" | "successful" | "cancel") => void;
}

function UserCreationDialog({ open, onClose }: UserCreationDialogProps) {
    const { t } = useTranslation();
    const { universes } = getUniverseContext();
    const universeOptions = useMemo(() => {
        return universes.map(universe => {
            return { label: universe.displayName, id: universe.name };
        });
    }, [universes]);

    const [errors, setErrors] = useState<Map<string, string>>(new Map<string, string>());
    const [userCreation, setUserCreation] = useState<PnPUserCreation>({
        username: "",
        displayName: "",
        password: "",
        authorities: []
    });
    const [adminRights, setAdminRights] = useState(false);
    const [universeCreationRights, setUniverseCreationRights] = useState(false);
    const [readRights, setReadRights] = useState([]);
    const [writeRights, setWriteRights] = useState([]);
    const [ownerRights, setOwnerRights] = useState([]);

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
        </Stack>
        <DialogActions>
            <Button autoFocus onClick={() => onClose({}, "cancel")}>
                {t('cancel')}
            </Button>
            <Button onClick={() => {
                const authorities = [];
                if (adminRights) {
                    authorities.push({
                        "@type": "Role",
                        "role": "ADMIN"
                    });
                }
                if (!adminRights && universeCreationRights) {
                    authorities.push({
                        "@type": "Role",
                        role: "UNIVERSE_CREATOR"
                    });
                }
                readRights.forEach(right => authorities.push({
                    "@type": "UniverseAuthority",
                    universe: right.id,
                    permission: "READ"

                }));
                writeRights.forEach(right => authorities.push({
                    "@type": "UniverseAuthority",
                    universe: right.id,
                    permission: "WRITE"

                }));
                ownerRights.forEach(right => authorities.push({
                    "@type": "UniverseAuthority",
                    universe: right.id,
                    permission: "OWNER"

                }));

                USER_API.createUser({
                    ...userCreation,
                    authorities: authorities
                }).then(() => onClose({}, "successful")).catch(handleValidationError(setErrors));
            }}>{t('create')}</Button>
        </DialogActions>
    </Dialog>;
}
