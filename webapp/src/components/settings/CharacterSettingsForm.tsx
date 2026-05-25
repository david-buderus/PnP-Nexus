import {ReactNode, useEffect} from 'react';
import {useUniverseContext} from '../PageBase';
import {useTranslation} from 'react-i18next';
import {useForm} from '@mantine/form';
import {CharacterSettings} from '../../api/model';
import {Button, Grid, Group, NumberInput, Stack, Text, Title} from '@mantine/core';
import {numberFormatter, percentageFormatter} from '../utils/Formatters';
import {handleValidationErrors} from '../utils/ErrorUtils';
import {probabilityForSuccesfulThrows} from '../utils/DiceThrowUtils';
import {fetchAllPrimaryAttributes} from '../Database';
import {
    getGetCharacterSheetSettingsQueryKey,
    useUpdateCharacterSettings
} from '../../api/universe-settings-service/universe-settings-service';
import {useQueryClient} from '@tanstack/react-query';

/** Form for character settings */
export default function CharacterSettingsForm({
    onSave, onSaveText, alternativeButton
}: {
    onSave: () => void;
    onSaveText: string;
    alternativeButton?: ReactNode;
}) {
    const {t} = useTranslation();
    const queryClient = useQueryClient();
    const {activeUniverse, characterSettings} = useUniverseContext();
    const form = useForm<CharacterSettings>({
        mode: 'controlled',
        initialValues: characterSettings
    });

    useEffect(() => {
        form.setInitialValues(characterSettings);
        form.setValues(characterSettings);
    }, [characterSettings]);

    const {mutateAsync: updateCharacterSettings} = useUpdateCharacterSettings({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({queryKey: getGetCharacterSheetSettingsQueryKey(activeUniverse.id)})
        }
    });

    const [primaryAttributes] = fetchAllPrimaryAttributes();
    const attributeLength = primaryAttributes.length;
    const settings = form.getValues();

    return <Stack align="center">
        <Title order={3} ta="center">
            {t('universe:characterSettings')}
        </Title>
        <form onSubmit={form.onSubmit(s => updateCharacterSettings({universe: activeUniverse.id, data: s})
            .then(onSave).catch(handleValidationErrors(form.setErrors)))}
        >
            <Grid columns={2} gutter="5vw">
                <Grid.Col span={1}>
                    <NumberInput
                        label={t('universe:minPrimaryAttributeValue')}
                        key={form.key('minPrimaryAttributeValue')}
                        {...form.getInputProps('minPrimaryAttributeValue')}
                        allowDecimal={false}
                    />
                    <NumberInput
                        label={t('universe:maxPrimaryAttributeValue')}
                        key={form.key('maxPrimaryAttributeValue')}
                        {...form.getInputProps('maxPrimaryAttributeValue')}
                        allowDecimal={false}
                    />
                    <NumberInput
                        label={t('universe:maxPrimaryAttributeSum')}
                        key={form.key('maxPrimaryAttributeSum')}
                        {...form.getInputProps('maxPrimaryAttributeSum')}
                        allowDecimal={false}
                    />
                </Grid.Col>
                <Grid.Col span={1}>
                    <Text ta="left">
                        {t('universe:primaryAttributeDistributionExplanation', {
                            'average': numberFormatter(settings.maxPrimaryAttributeSum / attributeLength),
                            'max': numberFormatter(Math.floor(
                                (settings.maxPrimaryAttributeSum - (attributeLength * settings.minPrimaryAttributeValue)) /
                                (settings.maxPrimaryAttributeValue - settings.minPrimaryAttributeValue)
                            )),
                            'averageChance': percentageFormatter(probabilityForSuccesfulThrows(
                                settings.maxPrimaryAttributeSum / attributeLength,
                                settings.maxPrimaryAttributeSum / attributeLength,
                                settings.maxPrimaryAttributeSum / attributeLength
                            )),
                            'highestChance': percentageFormatter(probabilityForSuccesfulThrows(settings.maxPrimaryAttributeValue, settings.maxPrimaryAttributeValue, settings.maxPrimaryAttributeValue)),
                            'lowestChance': percentageFormatter(probabilityForSuccesfulThrows(settings.minPrimaryAttributeValue, settings.minPrimaryAttributeValue, settings.minPrimaryAttributeValue))
                        })}
                    </Text>
                </Grid.Col>
            </Grid>
            <Group justify="flex-end" pt="md">
                {alternativeButton}
                <Button type="submit">
                    {onSaveText}
                </Button>
            </Group>
        </form>
    </Stack>;
}