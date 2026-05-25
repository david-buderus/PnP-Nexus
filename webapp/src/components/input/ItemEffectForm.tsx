import {UseFormReturnType} from '@mantine/form';
import {useTranslation} from 'react-i18next';
import {SomeItemEffect} from '../Constants';
import {
    ActionIcon,
    Box,
    Button,
    Group,
    Input,
    NumberInput,
    Paper,
    Select,
    Stack,
    Text,
    TextInput,
    Tooltip
} from '@mantine/core';
import {CalculationSelect, UpgradeEquipmentManipulatorSelect} from './EnumSelect';
import {FaRegTrashCan} from 'react-icons/fa6';
import {ECalculation, EItemEquipmentManipulator, EUpgradeRestriction} from '../../api/model';
import {randomId} from '@mantine/hooks';

/** A form for item effects */
export function ItemEffectForm<T>({
    form,
    path,
    restrictions
}: {
    form: UseFormReturnType<T>;
    path: string;
    restrictions: EUpgradeRestriction[];
}) {
    const {t} = useTranslation();

    const effects = form.values[path] as SomeItemEffect[] || [];

    return <Stack gap={0}>
        <Input.Label>
            {t('upgrade:effects')}
        </Input.Label>
        <Stack gap="xs">
            {effects.length > 0 ? (
                effects.map((effect, index) =>
                    <Stack key={'effect-' + index} gap={0}>
                        <Paper shadow="md" p="sm">
                            <Select
                                data={[
                                    {value: 'SimpleItemEffect', label: t('upgrade:simpleEffect')},
                                    {value: 'EquipmentItemEffect', label: t('upgrade:equipmentEffect')}
                                ]}
                                key={form.key(`${path}.${index}.@type`)}
                                {...form.getInputProps(`${path}.${index}.@type`)}
                            />
                            {effect['@type'] === 'EquipmentItemEffect' ? <>
                                <UpgradeEquipmentManipulatorSelect
                                    label={t('upgrade:upgradeManipulator')}
                                    key={form.key(`${path}.${index}.upgradeManipulator`)}
                                    {...form.getInputProps(`${path}.${index}.upgradeManipulator`)}
                                    restrictions={restrictions}
                                />
                                <Group wrap="nowrap">
                                    <CalculationSelect
                                        label={t('upgrade:calculation')}
                                        key={form.key(`${path}.${index}.calculation`)}
                                        {...form.getInputProps(`${path}.${index}.calculation`)}
                                    />
                                    <NumberInput
                                        label={t('value')}
                                        key={form.key(`${path}.${index}.value`)}
                                        {...form.getInputProps(`${path}.${index}.value`)}
                                    />
                                </Group>
                            </> : null}
                            <Group wrap="nowrap">
                                <Box
                                    style={{flex: 1}}
                                >
                                    <TextInput
                                        label={t('description')}
                                        key={form.key(`${path}.${index}.description`)}
                                        {...form.getInputProps(`${path}.${index}.description`)}
                                    />
                                </Box>
                                <ActionIcon
                                    variant="outline"
                                    color="red"
                                    size="input-sm"
                                    onClick={() => form.removeListItem(path, index)}
                                    mt={20}
                                    data-testid={path + '-sub-' + index}
                                >
                                    <FaRegTrashCan/>
                                </ActionIcon>
                            </Group>
                        </Paper>
                    </Stack>
                )) : (
                <Text c="dimmed" ta="center">
                    {t('nothing-here')}
                </Text>
            )}
        </Stack>
        <Tooltip label={form.errors[path]} disabled={!form.errors[path]}>
            <Button
                onClick={() =>
                    form.insertListItem(path, {
                        '@type': 'SimpleItemEffect',
                        description: '',
                        upgradeManipulator: EItemEquipmentManipulator.SLOTS,
                        calculation: ECalculation.ADDITIVE,
                        value: 0,
                        key: randomId()
                    } as any)
                }
                mt="md"
                color={form.errors[path] ? 'red' : undefined}
                data-testid={path + '-add'}
            >
                {t('upgrade:addEffect')}
            </Button>
        </Tooltip>
    </Stack>;
}