import { Anchor, Button, Checkbox, FileButton, Group, Paper, ScrollArea, Stack, Text, TextInput, Title } from "@mantine/core";
import { useState } from "react";
import { useTranslation } from "react-i18next";
import { BackupServiceApi } from "../../api";
import { API_CONFIGURATION } from "../../components/Constants";
import fileDownload from "js-file-download";
import axios, { AxiosResponse } from "axios";
import { useUniverseContext } from "../../components/PageBase";

const BACKUP_API = new BackupServiceApi(API_CONFIGURATION);

/** View with most common admin features  */
export function Admin() {
    return <Stack>
        <BackupImport />
        <BackupExport />
    </Stack>;
}

function BackupImport() {
    const { t } = useTranslation();
    const { fetchUniverses } = useUniverseContext();

    const [file, setFile] = useState<File | null>(null);
    const [uploading, setUploading] = useState(false);

    return <Stack
        gap="xs"
    >
        <Title order={3}>
            {t("admin:uploadBackup")}
        </Title>
        <Group gap="xs">
            <TextInput
                value={file?.name ?? ''}
                readOnly
                miw={400}
            />
            <FileButton onChange={setFile} accept="application/zip">
                {(props) => <Button {...props}>{t("admin:selectBackup")}</Button>}
            </FileButton>
            <Button
                onClick={() => {
                    setUploading(true);
                    axios.postForm("/api/backup/import", {
                        backup: file
                    })
                        .then(() => {
                            setUploading(false);
                            fetchUniverses();
                        })
                        .catch(() => setUploading(false));
                }}
                disabled={file === null}
                loading={uploading}
            >
                {t("admin:upload")}
            </Button>
        </Group>
    </Stack>;
}

function BackupExport() {
    const { t } = useTranslation();
    const { universes } = useUniverseContext();

    const [downloading, setDownloading] = useState(false);
    const [backupUniverses, setBackupUniverses] = useState<string[]>(universes.map(u => u.name));
    const [backupUniverseSearch, setBackupUniverseSearch] = useState('');

    return <Stack
        gap="xs"
        maw={423}
    >
        <Title order={3}>
            {t("admin:downloadBackup")}
        </Title>
        <Paper shadow="md" p={4}>
            <Checkbox.Group
                value={backupUniverses}
                onChange={setBackupUniverses}
                description={
                    <Group justify="space-around" wrap="nowrap" maw={400}>
                        <Text size="xs">
                            {t("admin:downloadBackupDescription")}
                            {" ("}
                            <Anchor onClick={() => setBackupUniverses(universes.map(u => u.name))}>
                                {t("admin:selectAll")}
                            </Anchor>
                            {", "}
                            <Anchor onClick={() => setBackupUniverses([])}>
                                {t("admin:selectNone")}
                            </Anchor>
                            {")"}
                        </Text>
                        <TextInput
                            value={backupUniverseSearch}
                            onChange={event => setBackupUniverseSearch(event.target.value)}
                            label={t("search")}
                            size="xs"
                        />
                    </Group>
                }
            >
                <ScrollArea
                    h={300}
                >
                    <Stack
                        pt="md"
                        gap="xs"
                        maw={400}
                    >
                        {universes.map(universe =>
                            backupUniverseSearch.length === 0
                                || universe.name.toLowerCase().includes(backupUniverseSearch.toLowerCase())
                                || universe.displayName.toLowerCase().includes(backupUniverseSearch.toLowerCase()) ?
                                <Checkbox.Card
                                    radius="md"
                                    p="md"
                                    value={universe.name}
                                    key={universe.name}
                                >
                                    <Group wrap="nowrap" align="flex-start">
                                        <Checkbox.Indicator />
                                        <div>
                                            <Title order={6}> {universe.displayName}</Title>
                                            <Text> {universe.shortDescription}</Text>
                                        </div>
                                    </Group>
                                </Checkbox.Card>
                                : null
                        )
                        }
                    </Stack>
                </ScrollArea>
            </Checkbox.Group>
            <Group justify="flex-end">
                <Button onClick={() => {
                    setDownloading(true);
                    BACKUP_API.exportBackup(backupUniverses, { responseType: "blob" }).then(response => {
                        setDownloading(false);
                        fileDownload(response.data as Blob, extractFilename(response));
                    }).catch(() => setDownloading(false));
                }} loading={downloading}>
                    {t("admin:download")}
                </Button>
            </Group>
        </Paper>
    </Stack>;
}

function extractFilename(response: AxiosResponse<any, any>) {
    const disposition = response.headers['content-disposition'] as string;

    if (!disposition) {
        return "unknown-file";
    }

    const rawFilename = disposition
        .split(';')
        .map(n => n.trim())
        .find(n => n.startsWith('filename=')).substring(9).trim();

    if (rawFilename.startsWith('"')) {
        return rawFilename.substring(1, rawFilename.length - 1);
    }
    return rawFilename;
}