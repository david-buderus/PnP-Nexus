import React, {useEffect, useState} from 'react';
import {useUniverseContext} from '../PageBase';
import {useTranslation} from 'react-i18next';
import {useForm} from '@mantine/form';
import {CurrencyCalculationEntry, CurrencySettings} from '../../api/model';
import {
    ActionIcon,
    Button,
    Flex,
    Grid,
    Group,
    NumberInput,
    Stack,
    Text,
    TextInput,
    Title,
    Tooltip
} from '@mantine/core';
import {currencyFormatter} from '../utils/Formatters';
import {randomId} from '@mantine/hooks';
import {FaRegTrashCan} from 'react-icons/fa6';
import {handleValidationErrors} from '../utils/ErrorUtils';
import {
    getGetCurrencySettingsQueryKey,
    useUpdateCurrencySettings
} from '../../api/universe-settings-service/universe-settings-service';
import {useQueryClient} from '@tanstack/react-query';

/** Form for currency settings */
export default function CurrencySettingsForm({
    onSave, onSaveText
}: {
    onSave: () => void;
    onSaveText: string;
}) {
    const {t} = useTranslation();
    const queryClient = useQueryClient();
    const {activeUniverse, currencySettings} = useUniverseContext();
    const [priceExample, setPriceExample] = useState<string | number>(1234);
    const form = useForm<CurrencySettings>({
        mode: 'controlled',
        initialValues: {
            baseCurrency: '',
            baseCurrencyShortForm: '',
            calculationEntries: []
        }
    });

    useEffect(() => {
        form.setInitialValues(currencySettings);
        form.setValues(currencySettings);
    }, [currencySettings]);

    const {mutateAsync: updateCurrencySettings} = useUpdateCurrencySettings({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({queryKey: getGetCurrencySettingsQueryKey(activeUniverse.id)})
        }
    });

    return <Stack align="center">
        <Title order={3} ta="center">
            {t('universe:currencySettings')}
        </Title>
        <Grid columns={2} gutter="5vw">
            <Grid.Col span={1}>
                <form
                    onSubmit={form.onSubmit((settings) => updateCurrencySettings({
                        universe: activeUniverse.id,
                        data: settings
                    }).then(onSave).catch(handleValidationErrors(form.setErrors)))}
                >
                    <Grid columns={2}>
                        <Grid.Col span={1}>
                            <TextInput
                                label={t('universe:baseCurrency')}
                                key={form.key('baseCurrency')}
                                required
                                {...form.getInputProps('baseCurrency')}
                            />
                        </Grid.Col>
                        <Grid.Col span={1}>
                            <Tooltip label={t('universe:baseCurrencyShortFormTooltip')}>
                                <TextInput
                                    label={t('universe:baseCurrencyShortForm')}
                                    key={form.key('baseCurrencyShortForm')}
                                    required
                                    {...form.getInputProps('baseCurrencyShortForm')}
                                />
                            </Tooltip>
                        </Grid.Col>
                        {form.getValues().calculationEntries.map((entry, index) => {
                                const currencyFactor = t('universe:currencyFactor', {
                                    smallerCoin: (index === 0 ? form.getValues().baseCurrency : form.getValues().calculationEntries[index - 1].currency) || '???',
                                    largerCoin: entry.currency || '???'
                                });
                                return <React.Fragment key={index}>
                                    <Grid.Col span={1}>
                                        <TextInput
                                            label={t('universe:calculationCurrency')}
                                            key={form.key(`calculationEntries.${index}.currency`)}
                                            required
                                            {...form.getInputProps(`calculationEntries.${index}.currency`)}
                                        />
                                    </Grid.Col>
                                    <Grid.Col span={1}>
                                        <Tooltip label={t('universe:baseCurrencyShortFormTooltip')}>
                                            <TextInput
                                                label={t('universe:calculationCurrencyShortForm')}
                                                key={form.key(`calculationEntries.${index}.currencyShortForm`)}
                                                required
                                                {...form.getInputProps(`calculationEntries.${index}.currencyShortForm`)}
                                            />
                                        </Tooltip>
                                    </Grid.Col>
                                    <Grid.Col span={2}>
                                        <Group justify="space-between" align="flex-end">
                                            <Flex align="flex-start" gap="sm">
                                                <NumberInput
                                                    label={t('universe:calculationFactor')}
                                                    key={form.key(`calculationEntries.${index}.factor`)}
                                                    required
                                                    {...form.getInputProps(`calculationEntries.${index}.factor`)}
                                                    allowDecimal={false}
                                                />
                                                <Text mt="25">
                                                    {currencyFactor}
                                                </Text>
                                            </Flex>
                                            <ActionIcon variant="outline" color="red"
                                                        onClick={() => form.removeListItem('calculationEntries', index)}>
                                                <FaRegTrashCan size="xl"/>
                                            </ActionIcon>
                                        </Group>
                                    </Grid.Col>
                                </React.Fragment>;
                            }
                        )}
                        <Grid.Col span={2}>
                            <Button
                                onClick={() =>
                                    form.insertListItem('calculationEntries', {
                                        currency: '',
                                        currencyShortForm: '',
                                        factor: 10,
                                        key: randomId()
                                    } as CurrencyCalculationEntry)
                                }
                            >
                                {t('universe:addAnotherCoin')}
                            </Button>
                        </Grid.Col>
                        <Grid.Col span={2}>
                            <Group justify="flex-end">
                                <Button type="submit">
                                    {onSaveText}
                                </Button>
                            </Group>
                        </Grid.Col>
                    </Grid>
                </form>
            </Grid.Col>
            <Grid.Col span={1}>
                <Stack>
                    <Text ta="left">
                        {t('universe:currencyExplanation')}
                    </Text>
                    <Group wrap="nowrap" grow>
                        <NumberInput
                            key="priceExample"
                            label={t('universe:priceExample')}
                            value={priceExample}
                            onChange={setPriceExample}
                            allowDecimal={false}
                        />
                        <TextInput
                            key={'priceExample-resulting'}
                            label={t('resultingPrice')}
                            data-testid="resultingPrice"
                            variant="outlined"
                            value={currencyFormatter(form.getValues(), priceExample)}
                            readOnly
                        />
                    </Group>
                </Stack>
            </Grid.Col>
        </Grid>
    </Stack>;
}