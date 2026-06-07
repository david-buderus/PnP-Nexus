import {
    ActionIcon,
    Button,
    Grid,
    Group,
    List,
    NumberInput,
    Paper,
    Stack,
    Switch,
    Table,
    Text,
    TextInput,
    Tooltip
} from '@mantine/core';
import {useForm} from '@mantine/form';
import {randomId} from '@mantine/hooks';
import {ReactNode, useEffect, useState} from 'react';
import {useTranslation} from 'react-i18next';
import {FaRegTrashCan} from 'react-icons/fa6';
import {SecondaryAttributeDTO, SecondaryAttributeInfo,} from '../../api/model';
import {
    fetchAllPrimaryAttributes,
    fetchAllSimpleSecondaryAttributes,
    fetchSupportedSecondaryAttributeVariables
} from '../Database';
import {useUniverseContext} from '../PageBase';
import {handleNetworkErrors, handleValidationErrors} from '../utils/ErrorUtils';
import {
    useCalculateResults,
    useGetSupportedFunctions
} from '../../api/binary-expression-tree-service/binary-expression-tree-service';
import {
    getGetAllSimpleSecondaryAttributesQueryKey,
    useSetAllSimpleSecondaryAttributes
} from '../../api/simple-secondary-attribute-service/simple-secondary-attribute-service';
import {useQueryClient} from '@tanstack/react-query';
import {getGetAllSecondaryAttributesQueryKey} from '../../api/secondary-attribute-service/secondary-attribute-service';
import {useGetSecondaryAttributeInfo} from '../../api/universe-creation-service/universe-creation-service';

/** A form to adjust all secondary attributes */
export function SecondaryAttributeForm({
    onSave, onSaveText, alternativeButton
}: {
    onSave: () => void;
    onSaveText: string;
    alternativeButton?: ReactNode;
}) {
    const {t} = useTranslation();
    const queryClient = useQueryClient();
    const {activeUniverse, characterSettings} = useUniverseContext();

    const [primaryAttributes] = fetchAllPrimaryAttributes();
    const [secondaryAttributes] = fetchAllSimpleSecondaryAttributes();
    const [supportedVariables] = fetchSupportedSecondaryAttributeVariables();
    const supportedFunctions = useGetSupportedFunctions().data?.data ?? [];
    const [attributeInfos, setAttributeInfos] = useState<SecondaryAttributeInfo[]>([]);

    const [primaryValues, setPrimaryValues] = useState<Map<string, number>>(new Map<string, number>());
    const [calculatedAttributeValues, setCalculatedAttributeValues] = useState<number[]>([]);

    const form = useForm<{
        attributes: (SecondaryAttributeDTO & { key: string; })[];
    }>({
        mode: 'controlled',
        initialValues: {
            attributes: Array(8).fill({name: '', consumable: false, calculationFormula: ''}).map(a => {
                return {...a, key: randomId()};
            })
        }
    });

    const {mutate: calculateResults} = useCalculateResults({
        mutation: {
            onSuccess: response => setCalculatedAttributeValues(response.data),
            onError: handleNetworkErrors
        }
    });

    const {mutate: setAttributes} = useSetAllSimpleSecondaryAttributes({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({queryKey: getGetAllSimpleSecondaryAttributesQueryKey(activeUniverse.id)})
                .then(() => queryClient.invalidateQueries({queryKey: getGetAllSecondaryAttributesQueryKey(activeUniverse.id)}))
                .then(onSave),
            onError: handleValidationErrors(form.setErrors)
        }
    });

    const {mutate: getAttributeInfo, isPending: loadingAttributeInfos} = useGetSecondaryAttributeInfo({
        mutation: {
            onSuccess: response => setAttributeInfos(response.data),
            onError: handleNetworkErrors
        }
    });

    useEffect(() => {
        if (!secondaryAttributes || secondaryAttributes.length === 0) {
            return;
        }
        form.setValues({
            attributes: secondaryAttributes.map(a => ({
                ...a,
                key: randomId()
            }))
        });
    }, [secondaryAttributes]);

    useEffect(() => {
        if (!activeUniverse) {
            return;
        }
        const calculate = setTimeout(() => {
            calculateResults({
                data: {
                    formulas: form.values.attributes.map(att => att.calculationFormula),
                    constants: Object.fromEntries(primaryValues)
                }
            });
        }, 200);

        return () => clearTimeout(calculate);
    }, [activeUniverse, form.values.attributes, primaryValues]);

    return <Grid>
        <Grid.Col span="content">
            <form
                onSubmit={form.onSubmit(a => setAttributes({
                    universe: activeUniverse.id,
                    data: a.attributes
                }))}>
                <Table>
                    <Table.Thead>
                        <Table.Tr>
                            <Table.Th>{t('name')}</Table.Th>
                            <Table.Th>{t('character:shortName')}</Table.Th>
                            <Table.Th>{t('character:calculationFormula')}</Table.Th>
                            <Table.Th colSpan={2} style={{width: 150}}>{t('character:consumableAttribute')}</Table.Th>
                        </Table.Tr>
                    </Table.Thead>
                    <Table.Tbody>
                        {form.getValues().attributes.map((attribute, index) => {
                            return <Table.Tr key={attribute.key}>
                                <Table.Td>
                                    <TextInput
                                        key={form.key(`attributes.${index}.name`)}
                                        {...form.getInputProps(`attributes.${index}.name`)}
                                        required
                                    />
                                </Table.Td>
                                <Table.Td>
                                    <TextInput
                                        key={form.key(`attributes.${index}.shortName`)}
                                        {...form.getInputProps(`attributes.${index}.shortName`)}
                                        required
                                    />
                                </Table.Td>
                                <Table.Td>
                                    <Tooltip label={<>
                                        {t('character:calculationFormulaTooltip')}
                                        <List>
                                            {supportedVariables.map(v => <List.Item
                                                key={'tooltip-supported-variables-' + index + '-' + v}>{v}</List.Item>)}
                                        </List>
                                        {t('character:calculationFormulaFunctionTooltip')}
                                        <List>
                                            {supportedFunctions.map(f => <List.Item
                                                key={'tooltip-supported-function-' + index + '-' + f}>{f}</List.Item>)}
                                        </List>
                                    </>} key={form.key(`attributes.${index}.consumable`) + '-calculationFormula'}>
                                        <TextInput
                                            key={form.key(`attributes.${index}.calculationFormula`)}
                                            required
                                            {...form.getInputProps(`attributes.${index}.calculationFormula`)}
                                        />
                                    </Tooltip>
                                </Table.Td>
                                <Table.Td>
                                    <Tooltip label={t('character:consumableAttributeTooltip')}
                                             key={form.key(`attributes.${index}.consumable`) + '-tooltip'}>
                                        <div>
                                            <Switch
                                                key={form.key(`attributes.${index}.consumable`)}
                                                {...form.getInputProps(`attributes.${index}.consumable`, {type: 'checkbox'})}
                                            />
                                        </div>
                                    </Tooltip>
                                </Table.Td>
                                <Table.Td>
                                    <ActionIcon variant="outline" size="lg" color="red"
                                                onClick={() => form.removeListItem('attributes', index)}>
                                        <FaRegTrashCan size={16}/>
                                    </ActionIcon>
                                </Table.Td>
                            </Table.Tr>;
                        })}
                    </Table.Tbody>
                    {form.getValues().attributes.length === 0 ?
                        <Table.Caption c="dimmed" ta="center">
                            {t('nothing-here')}
                        </Table.Caption> : null}
                </Table>
                <Button
                    mt="md"
                    onClick={() =>
                        form.insertListItem('attributes', {
                            name: '',
                            shortName: '',
                            calculationFormula: '',
                            consumable: false,
                            key: randomId()
                        })
                    }
                >
                    {t('universe:addAnotherAttribute')}
                </Button>

                <Group justify="flex-end" pt="md">
                    {alternativeButton}
                    <Button type="submit">
                        {onSaveText}
                    </Button>
                </Group>
            </form>
        </Grid.Col>
        <Grid.Col span="content">
            <Paper shadow="md" p="md">
                <Stack>
                    <Text ta="left">
                        {t('universe:secondaryAttributeExplanation')}
                    </Text>
                    <Table>
                        <Table.Thead>
                            <Table.Tr>
                                <Table.Th>{t('secondary-attribute')}</Table.Th>
                                <Table.Th>Min</Table.Th>
                                <Table.Th>Max</Table.Th>
                                <Table.Th>Avg</Table.Th>
                            </Table.Tr>
                        </Table.Thead>
                        <Table.Tbody>
                            {form.getValues().attributes.map((attribute, i) => {
                                const info = attributeInfos[i];

                                return <Table.Tr key={i}>
                                    <Table.Td component="th" scope="row">
                                        {attribute?.name || ''}
                                    </Table.Td>
                                    <Table.Td align="right">{info ? info.notPrecise ?
                                        <Tooltip label={t('universe:secondaryAttributeExplanationTooltip')}>
                                            <div>/</div>
                                        </Tooltip>
                                        : info.min : 0
                                    }</Table.Td>
                                    <Table.Td align="right">{info ? info.notPrecise ?
                                        <Tooltip label={t('universe:secondaryAttributeExplanationTooltip')}>
                                            <div>/</div>
                                        </Tooltip>
                                        : info.max : 0}
                                    </Table.Td>
                                    <Table.Td align="right">{info ? info.average : 0}</Table.Td>
                                </Table.Tr>;
                            })}
                        </Table.Tbody>
                    </Table>
                    <Button
                        fullWidth
                        loading={loadingAttributeInfos}
                        onClick={() => getAttributeInfo({
                            universe: activeUniverse.id,
                            data: form.values.attributes,
                        })}
                    >
                        {t('universe:getSecondaryAttributeInfos')}
                    </Button>
                </Stack>
            </Paper>
            <Paper shadow="md" p="md" mt="md">
                <Stack>
                    <Text ta="left">
                        {t('universe:secondaryAttributeTesting')}
                    </Text>
                    <Grid columns={2}>
                        <Grid.Col span={1}>
                            {primaryAttributes.map((primaryAttribute, i) =>
                                <NumberInput
                                    key={'primary-attribute-field-' + i}
                                    label={primaryAttribute.name}
                                    value={primaryValues.get(primaryAttribute.shortName)}
                                    onChange={value => setPrimaryValues(new Map(primaryValues).set(primaryAttribute.shortName, Number(value)))}
                                    allowDecimal={false}
                                    clampBehavior="strict"
                                    min={characterSettings.minPrimaryAttributeValue}
                                    max={characterSettings.maxPrimaryAttributeValue}
                                />
                            )}
                        </Grid.Col>
                        <Grid.Col span={1}>
                            {form.getValues().attributes.map((secondaryAttribute, i) =>
                                <TextInput
                                    key={'secondary-attribute-result-field-' + i}
                                    label={secondaryAttribute.name ? secondaryAttribute.name : '???'}
                                    readOnly
                                    value={
                                        calculatedAttributeValues[i] === undefined
                                        || calculatedAttributeValues[i].toString() === 'NaN'
                                        || Number.isNaN(calculatedAttributeValues[i])
                                            ? '???' : Math.round(calculatedAttributeValues[i])}
                                />
                            )}
                        </Grid.Col>
                    </Grid>
                </Stack>
            </Paper>
        </Grid.Col>
    </Grid>;
}