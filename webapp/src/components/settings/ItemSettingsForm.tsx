import {useForm} from '@mantine/form';
import {ReactNode, useEffect} from 'react';
import {useTranslation} from 'react-i18next';
import {ItemSettings} from '../../api/model';
import {useUniverseContext} from '../PageBase';
import {handleValidationErrors} from '../utils/ErrorUtils';
import {Button, Checkbox, Group, NumberInput, Stack, Text, Title} from '@mantine/core';
import {
    getGetItemSettingsQueryKey,
    useUpdateItemSettings
} from '../../api/universe-settings-service/universe-settings-service';
import {useQueryClient} from '@tanstack/react-query';

/** Form for item settings */
export default function ItemSettingsForm({
    onSave, onSaveText, alternativeButton
}: {
    onSave: () => void;
    onSaveText: string;
    alternativeButton?: ReactNode;
}) {
    const {t} = useTranslation();
    const queryClient = useQueryClient();
    const {activeUniverse, itemSettings} = useUniverseContext();
    const form = useForm<ItemSettings>({
        mode: 'controlled',
        initialValues: {
            shieldUsingDice: false,
            usingProtection: false,
            wearFactor: 0
        }
    });

    useEffect(() => {
        form.setInitialValues(itemSettings);
        form.setValues(itemSettings);
    }, [itemSettings]);

    const {mutateAsync: updateItemSettings} = useUpdateItemSettings({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({queryKey: getGetItemSettingsQueryKey(activeUniverse.id)})
        }
    });

    return <Stack align="stretch" justify="center" maw={500}>
        <Title order={3} ta="center">
            {t('universe:itemSettings')}
        </Title>
        <form
            onSubmit={form.onSubmit(settings => updateItemSettings({
                universe: activeUniverse.id,
                data: settings
            }).then(onSave).catch(handleValidationErrors(form.setErrors)))}
        >
            <NumberInput
                label={t('universe:wearFactor')}
                key={form.key('wearFactor')}
                {...form.getInputProps('wearFactor')}
                allowDecimal={false}
            />
            <Text ta="left" size="xs">
                {t('universe:wearFactorTooltip')}
            </Text>
            <Checkbox
                mt="md"
                label={t('universe:shieldUsingDice')}
                key={form.key('shieldUsingDice')}
                {...form.getInputProps('shieldUsingDice', {type: 'checkbox'})}
            />
            <Checkbox
                mt="md"
                label={t('universe:usingProtection')}
                key={form.key('usingProtection')}
                {...form.getInputProps('usingProtection', {type: 'checkbox'})}
            />
            <Group justify="flex-end" pt="md">
                {alternativeButton}
                <Button type="submit">
                    {onSaveText}
                </Button>
            </Group>
        </form>
    </Stack>;
}