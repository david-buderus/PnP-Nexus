import {useEffect} from 'react';
import {useUniverseContext} from '../PageBase';
import {useTranslation} from 'react-i18next';
import {useForm} from '@mantine/form';
import {CharacterSheetSettings, PnPCharacterSheet, UniverseSettingsServiceApi} from '../../api';
import {Button, Group, Modal, Stack, Text} from '@mantine/core';
import {API_CONFIGURATION} from '../Constants';
import {handleNetworkErrors, handleValidationErrors} from '../utils/ErrorUtils';
import {fetchAllCharacterSheets} from '../Database';
import {useDisclosure} from '@mantine/hooks';
import {ObjectSelect} from '../input/ObjectSelect';

const SETTINGS_API = new UniverseSettingsServiceApi(API_CONFIGURATION);

/** Settings dialog for character sheets */
export default function CharacterSheetSettingsDialog() {
    const {t} = useTranslation();
    const {activeUniverse, sheetSettings, refreshSettings} = useUniverseContext();
    const [sheets] = fetchAllCharacterSheets();
    const [opened, {open, close}] = useDisclosure(false);

    const form = useForm<CharacterSheetSettings>({
        mode: 'controlled',
        initialValues: sheetSettings
    });

    useEffect(() => {
        if (!activeUniverse) {
            return;
        }

        SETTINGS_API.getCharacterSheetSettings(activeUniverse.id).then(response => form.setValues(response.data)).catch(handleNetworkErrors);
    }, [activeUniverse]);

    return <>
        <Modal opened={opened} onClose={close} size="auto" title={t('universe:characterSheetSettings')}>
            <form
                onSubmit={form.onSubmit(s => SETTINGS_API.updateCharacterSheetSettings(activeUniverse.id, s)
                    .then(() => {
                        refreshSettings();
                        close();
                    }).catch(handleValidationErrors(form.setErrors)))}>
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