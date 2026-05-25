import {
    Anchor,
    Button,
    Checkbox,
    FileButton,
    Group,
    Paper,
    ScrollArea,
    Stack,
    Text,
    TextInput,
    Title
} from '@mantine/core';
import {useState} from 'react';
import {useTranslation} from 'react-i18next';
import fileDownload from 'js-file-download';
import axios, {AxiosResponse} from 'axios';
import {useUniverseContext} from '../../components/PageBase';
import {useMutation, useQueryClient} from '@tanstack/react-query';
import {getGetAllUniversesQueryKey} from '../../api/universe-service/universe-service';
import {exportBackup} from '../../api/backup-service/backup-service';

/** View with most common admin features  */
export function Admin() {
    return <Stack>
        <BackupImport/>
        <BackupExport/>
    </Stack>;
}

function BackupImport() {
    const {t} = useTranslation();
    const queryClient = useQueryClient();

    const [file, setFile] = useState<File | null>(null);
    const [uploading, setUploading] = useState(false);

    return <Stack
        gap="xs"
    >
        <Title order={3}>
            {t('admin:uploadBackup')}
        </Title>
        <Group gap="xs">
            <TextInput
                value={file?.name ?? ''}
                readOnly
                miw={400}
            />
            <FileButton onChange={setFile} accept="application/zip">
                {(props) => <Button {...props}>{t('admin:selectBackup')}</Button>}
            </FileButton>
            <Button
                onClick={() => {
                    setUploading(true);
                    axios.postForm('/api/backup/import', {
                        backup: file
                    })
                        .then(() => {
                            setUploading(false);
                            return queryClient.invalidateQueries({
                                queryKey: getGetAllUniversesQueryKey()
                            });
                        })
                        .catch(() => setUploading(false));
                }}
                disabled={file === null}
                loading={uploading}
            >
                {t('admin:upload')}
            </Button>
        </Group>
    </Stack>;
}

function BackupExport() {
    const {t} = useTranslation();
    const {universes} = useUniverseContext();

    const [backupUniverses, setBackupUniverses] = useState<string[]>(universes.map(u => u.id));
    const [backupUniverseSearch, setBackupUniverseSearch] = useState('');

    const {mutate, isPending} = useMutation({
        mutationFn: (variables: { universes: string[] }) =>
            exportBackup(variables, {
                responseType: 'blob'
            }),

        onSuccess: response => {
            fileDownload(response.data as unknown as Blob, extractFilename(response));
        },
        onError: err => {
            console.error('Backup file download failed:', err);
        }
    });

    return <Stack
        gap="xs"
        maw={423}
    >
        <Title order={3}>
            {t('admin:downloadBackup')}
        </Title>
        <Paper shadow="md" p={4}>
            <Group justify="space-around" wrap="nowrap" maw={400}>
                <Text size="xs">
                    {t('admin:downloadBackupDescription')}
                    {' ('}
                    <Anchor onClick={() => setBackupUniverses(universes.map(u => u.id))}>
                        {t('admin:selectAll')}
                    </Anchor>
                    {', '}
                    <Anchor onClick={() => setBackupUniverses([])}>
                        {t('admin:selectNone')}
                    </Anchor>
                    {')'}
                </Text>
                <TextInput
                    value={backupUniverseSearch}
                    onChange={event => setBackupUniverseSearch(event.target.value)}
                    label={t('search')}
                    size="xs"
                />
            </Group>
            <Checkbox.Group
                value={backupUniverses}
                onChange={setBackupUniverses}
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
                            || universe.id.toLowerCase().includes(backupUniverseSearch.toLowerCase())
                            || universe.displayName.toLowerCase().includes(backupUniverseSearch.toLowerCase()) ?
                                <Checkbox.Card
                                    radius="md"
                                    p="md"
                                    value={universe.id}
                                    key={universe.id}
                                >
                                    <Group wrap="nowrap" align="flex-start">
                                        <Checkbox.Indicator/>
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
                <Button onClick={() => mutate({universes: backupUniverses})} loading={isPending}>
                    {t('admin:download')}
                </Button>
            </Group>
        </Paper>
    </Stack>;
}

function extractFilename(response: AxiosResponse<any, any>) {
    const disposition = response.headers['content-disposition'] as string;

    if (!disposition) {
        return 'unknown-file';
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