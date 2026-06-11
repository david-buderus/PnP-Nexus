import {ReactNode, useEffect} from 'react';
import {useUniverseContext} from '../PageBase';
import {useTranslation} from 'react-i18next';
import {useForm} from '@mantine/form';
import {CharacterSettingsDto} from '../../api/model';
import {ActionIcon, Button, Grid, Group, Input, NumberInput, Stack, Text, TextInput, Title} from '@mantine/core';
import {numberFormatter, percentageFormatter} from '../utils/Formatters';
import {handleValidationErrors} from '../utils/ErrorUtils';
import {probabilityForSuccesfulThrows} from '../utils/DiceThrowUtils';
import {fetchAllPrimaryAttributes} from '../Database';
import {
    getGetCharacterSettingsDtoQueryKey,
    getGetCharacterSettingsQueryKey,
    useGetCharacterSettingsDto,
    useUpdateCharacterSettingsDto
} from '../../api/universe-settings-service/universe-settings-service';
import {useQueryClient} from '@tanstack/react-query';
import {useGetSupportedFunctions} from '../../api/binary-expression-tree-service/binary-expression-tree-service';
import FormularInput from '../input/FormularInput';
import {FaRegTrashCan} from 'react-icons/fa6';

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
    const {activeUniverse} = useUniverseContext();
    const form = useForm<CharacterSettingsDto>({
        mode: 'controlled',
        initialValues: {
            minPrimaryAttributeValue: 1,
            maxPrimaryAttributeValue: 10,
            maxPrimaryAttributeSum: 40,
            tierFormula: '',
            talentPointFormula: '',
            inventorySizes: []
        }
    });

    const characterSettings = useGetCharacterSettingsDto(activeUniverse?.id, {
        query: {enabled: Boolean(activeUniverse?.id)}
    }).data?.data;
    const supportedFunctions = useGetSupportedFunctions().data?.data ?? [];

    useEffect(() => {
        if (!characterSettings) {
            return;
        }
        form.setInitialValues(characterSettings);
        form.setValues(characterSettings);
    }, [characterSettings]);

    const {mutateAsync: updateCharacterSettings} = useUpdateCharacterSettingsDto({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({queryKey: getGetCharacterSettingsQueryKey(activeUniverse.id)})
                .then(() => queryClient.invalidateQueries({queryKey: getGetCharacterSettingsDtoQueryKey(activeUniverse.id)}))
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
            <Grid columns={2}>
                <Grid.Col span={1}>
                    <Stack gap="sm">
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
                        <FormularInput
                            label={t('universe:tierFormula')}
                            supportedVariables={['LVL']}
                            supportedFunctions={supportedFunctions}
                            key={form.key('tierFormula')}
                            {...form.getInputProps('tierFormula')}
                        />
                        <FormularInput
                            label={t('universe:talentPointFormula')}
                            supportedVariables={['LVL', 'TIER']}
                            supportedFunctions={supportedFunctions}
                            key={form.key('talentPointFormula')}
                            {...form.getInputProps('talentPointFormula')}
                        />
                        <Stack gap="xs" mt="md">
                            <Input.Label>
                                {t('universe:inventorySizes')}
                            </Input.Label>
                            {form.values.inventorySizes.map((_, index) => (
                                <Group key={index} align="flex-start" gap="xs">
                                    <TextInput
                                        flex={1}
                                        placeholder={t('universe:backpackExample')}
                                        key={form.key(`inventorySizes.${index}.name`)}
                                        {...form.getInputProps(`inventorySizes.${index}.name`)}
                                    />
                                    <NumberInput
                                        flex={1}
                                        allowDecimal={false}
                                        min={1}
                                        key={form.key(`inventorySizes.${index}.size`)}
                                        {...form.getInputProps(`inventorySizes.${index}.size`)}
                                    />
                                    <ActionIcon
                                        variant="outline"
                                        size="lg"
                                        color="red"
                                        onClick={() => form.removeListItem('inventorySizes', index)}
                                    >
                                        <FaRegTrashCan size={16}/>
                                    </ActionIcon>
                                </Group>
                            ))}
                            <Button
                                variant="outline"
                                onClick={() => form.insertListItem('inventorySizes', {
                                    name: '',
                                    size: 20
                                })}
                            >
                                {t('universe:addInventoryType')}
                            </Button>
                        </Stack>
                    </Stack>
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