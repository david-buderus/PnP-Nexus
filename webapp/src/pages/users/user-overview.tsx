import { Button, Stack } from "@mui/material";
import OverviewTable from "../../components/OverviewTable";
import { PnPUser, UserServiceApi } from "../../api";
import { API_CONFIGURATION } from "../../components/Constants";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { ConfirmationDialog } from "../../components/inputs/ConfirmationDialog";
import { UserCreationDialog, UserEditDialog } from "../../components/users/UserDialogs";

const USER_API = new UserServiceApi(API_CONFIGURATION);

/** Admin overview over all users */
export function UserOverview() {
    const { t } = useTranslation();

    const [users, setUsers] = useState<PnPUser[]>([]);
    const [selected, setSelected] = useState<PnPUser[keyof PnPUser][]>([]);

    const [openCreationDialog, setOpenCreationDialog] = useState(false);
    const [openEditDialog, setOpenEditDialog] = useState(false);
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
            <Button className='btn' data-testid="add" onClick={() => setOpenCreationDialog(true)}>
                {t("add")}
            </Button>
            <Button className='btn' data-testid="edit" disabled={selected.length !== 1} onClick={() => setOpenEditDialog(true)}>
                {t("edit")}
            </Button>
            <Button className='btn' data-testid="delete" disabled={selected.length === 0} onClick={() => setOpenDeleteDialog(true)}>
                {t("delete")}
            </Button>
            <UserCreationDialog open={openCreationDialog} onClose={sucessful => {
                setOpenCreationDialog(false);
                if (sucessful) {
                    USER_API.getAllUsers().then(response => setUsers(response.data));
                }
            }} />
            <UserEditDialog
                key={openEditDialog ? selected[0] as string : "no-selection"}
                open={openEditDialog}
                user={openEditDialog ? users.find(user => user.username === selected[0]) : null}
                onClose={sucessful => {
                    setOpenEditDialog(false);
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

