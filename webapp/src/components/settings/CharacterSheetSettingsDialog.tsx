import {useEffect} from 'react';
import {useUniverseContext} from '../PageBase';
import {useTranslation} from 'react-i18next';
import {useForm} from '@mantine/form';
import {CharacterSheetSettings, PnPCharacterSheet} from '../../api/model';
import {Button, Group, Modal, Stack, Text} from '@mantine/core';
import {handleValidationErrors} from '../utils/ErrorUtils';
import {fetchAllCharacterSheets} from '../Database';
import {useDisclosure} from '@mantine/hooks';
import {ObjectSelect} from '../input/ObjectSelect';
import {
    getGetCharacterSheetSettingsQueryKey,
    useUpdateCharacterSheetSettings
} from '../../api/universe-settings-service/universe-settings-service';
import {useQueryClient} from '@tanstack/react-query';

/** Settings dialog for character sheets */
export default function CharacterSheetSettingsDialog() {
    const {t} = useTranslation();
    const queryClient = useQueryClient();
    const {activeUniverse, sheetSettings} = useUniverseContext();
    const [sheets] = fetchAllCharacterSheets();
    const [opened, {open, close}] = useDisclosure(false);

    const form = useForm<CharacterSheetSettings>({
        mode: 'controlled',
        initialValues: sheetSettings
    });

    useEffect(() => {
        form.setInitialValues(sheetSettings);
        form.setValues(sheetSettings);
    }, [sheetSettings]);

    const {mutateAsync: updateCharacterSheetSettings} = useUpdateCharacterSheetSettings({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({queryKey: getGetCharacterSheetSettingsQueryKey(activeUniverse.id)})
        }
    });

    return <>
        <Modal opened={opened} onClose={close} size="auto" title={t('universe:characterSheetSettings')}>
            <form
                onSubmit={form.onSubmit(s => updateCharacterSheetSettings({universe: activeUniverse.id, data: s})
                    .then(() => close()).catch(handleValidationErrors(form.setErrors)))}
            >
                <Stack>
                    <Text>
                        {t('universe:characterSheetSettingsModalDescription')}
                    </Text>
                    <ObjectSelect<PnPCharacterSheet>
                        label={t('playerSheet')}
                        key={form.key('playerSheet')}
                        {...form.getInputProps('playerSheet')}
                        data={sheets}
                        idKey="id"
                        labelKey="name"
                    />
                    <ObjectSelect<PnPCharacterSheet>
                        label={t('enemySheet')}
                        key={form.key('enemySheet')}
                        {...form.getInputProps('enemySheet')}
                        data={sheets}
                        idKey="id"
                        labelKey="name"
                    />
                    <Group justify="flex-end" pt="md">
                        <Button onClick={close}>
                            {t('cancel')}
                        </Button>
                        <Button type="submit">
                            {t('save')}
                        </Button>
                    </Group>
                </Stack>
            </form>
        </Modal>
        <Button onClick={open}>
            {t('edit')}
        </Button>
    </>;
}