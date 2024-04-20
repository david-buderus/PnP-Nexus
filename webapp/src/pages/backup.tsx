import { Button, Stack, TextField, styled } from "@mui/material";
import { BackupServiceApi } from "../api";
import { ChangeEvent, useState } from "react";
import { useTranslation } from "react-i18next";
import { API_CONFIGURATION } from "../components/Constants";
import axios from "axios";


const BACKUP_API = new BackupServiceApi(API_CONFIGURATION);

const config = {
    headers: {
        'Content-Type': 'application/zip'
    }
};

const VisuallyHiddenInput = styled('input')({
    clip: 'rect(0 0 0 0)',
    clipPath: 'inset(50%)',
    height: 1,
    overflow: 'hidden',
    position: 'absolute',
    bottom: 0,
    left: 0,
    whiteSpace: 'nowrap',
    width: 1,
});

/** The backup page */
export function Backup() {
    const { t } = useTranslation();

    const [file, setFile] = useState<File>();

    return <Stack spacing={2} padding={2}>
        <Stack spacing={2} direction="row">
            <TextField label={t("backup")} value={file?.name ?? ""} InputProps={{ readOnly: true }} />
            <Button
                component="label"
                variant="contained"
                tabIndex={-1}
            >
                Select file
                <VisuallyHiddenInput type="file" accept="application/zip" onChange={event => setFile(event.target.files[0])} />
            </Button>
        </Stack>
        <Button
            variant="contained"
            onClick={() => {
                axios.postForm("/api/backup/import", {
                    backup: file
                });
            }}
        >
            Upload
        </Button>
    </Stack>;
}