import {useEffect, useMemo} from 'react';
import {useTranslation} from 'react-i18next';
import {
    ECalculation,
    EUpgradeEquipmentManipulator,
    EUpgradeRestriction,
    TagRequirement,
    Upgrade,
    UpgradeEffectsInner,
    UpgradeServiceApi
} from '../../../api';
import OverviewPage, {ExtendedColumnDef} from '../../../components/OverviewPage';
import {fetchAllUpgrades} from '../../../components/Database';
import {API_CONFIGURATION} from '../../../components/Constants';
import CurrencyCell from '../../../components/table/CurrencyCell';
import {
    ActionIcon,
    Box,
    Button,
    Group,
    Input,
    List,
    Modal,
    NumberInput,
    Paper,
    Select,
    Stack,
    TextInput,
    Tooltip
} from '@mantine/core';
import {useUniverseContext} from '../../../components/PageBase';
import {useForm, UseFormReturnType} from '@mantine/form';
import {randomId, useDisclosure} from '@mantine/hooks';
import {handleDatabaseInsertErrors, handleValidationErrors} from '../../../components/utils/ErrorUtils';
import {currencyFormatter} from '../../../components/utils/Formatters';
import {
    CalculationSelect,
    UpgradeEquipmentManipulatorSelect,
    UpgradeRestrictionSelect
} from '../../../components/input/EnumSelect';
import {FaRegTrashCan} from 'react-icons/fa6';
import TagRequirementsInput from '../../../components/input/TagRequirementsInput';

const UPGRADE_API = new UpgradeServiceApi(API_CONFIGURATION);

/** Overview over all upgades */
export function UpgradeOverview() {
    const {t} = useTranslation();

    const columns = useMemo<ExtendedColumnDef<Upgrade, any>[]>(
        () => [
            {
                accessorKey: 'name',
                header: t('name'),
            },
            {
                accessorKey: 'effects',
                header: t('upgrade:effects'),
                Cell: cell => {
                    const effects = cell.cell.getValue<UpgradeEffectsInner[]>();
                    if (effects.length < 2) {
                        return effects[0]?.description;
                    } else {
                        return <List>
                            {effects.map((effect, index) => <List.Item key={index}>
                                {effect.description}
                            </List.Item>)}
                        </List>;
                    }
                },
                filterFn: (row, id, filterValue) => {
                    return row.getValue<UpgradeEffectsInner[]>(id).some(effect => effect.description.includes(filterValue));
                }
            },
            {
                accessorKey: 'restriction',
                header: t('upgrade:restriction'),
                filterVariant: 'select',
                mantineFilterMultiSelectProps: {
                    data: Object.values(EUpgradeRestriction).map(rarity => {
                        return {
                            label: t('enum:' + rarity.toLowerCase()),
                            value: rarity
                        };
                    }),
                },
                Cell: cell => t('enum:' + cell.cell.getValue()?.toLowerCase())
            },
            {
                accessorKey: 'tagRequirement',
                header: t('upgrade:tagRequirement'),
                Cell: cell => cell.cell.getValue<TagRequirement>().tagRequirements.map(tags => tags.join(', ')).join(' ' + t('or') + ' '),
                filterFn: (row, id, filterValue) => {
                    return row.getValue<TagRequirement>(id).tagRequirements.some(tags => tags.some(tag => tag.includes(filterValue)));
                }
            },
            {
                accessorKey: 'slots',
                header: t('upgrade:necessary-slots'),
            },
            {
                accessorKey: 'vendorPrice',
                header: t('price'),
                Cell: CurrencyCell
            }
        ], []);

    return <OverviewPage
        fetchData={fetchAllUpgrades()}
        columns={columns}
        identifier="upgrades"
        manipulationDialog={(editMode, refresh, disabled, getInitial) =>
            <CreationDialog
                editMode={editMode}
                refresh={refresh}
                disabled={disabled}
                getInitial={getInitial}
            />}
        deletionDialogTitle={t('upgrade:upgradeDeletionTitle')}
        onDelete={(universe, upgrades) => UPGRADE_API.deleteAllUpgrades(universe, upgrades.map(upgrade => upgrade.id))}
        idKey="id"
    />;
}

function CreationDialog({
    editMode,
    refresh,
    disabled,
    getInitial
}: {
    editMode: boolean,
    refresh: () => void;
    disabled: boolean;
    getInitial: () => Upgrade;
}) {
    const {t} = useTranslation();
    const {activeUniverse, currencySettings} = useUniverseContext();

    const [opened, {open, close}] = useDisclosure(false);
    const form = useForm<Upgrade>({
        mode: 'controlled',
        initialValues: {
            effects: [],
            name: '',
            restriction: 'ARMOR',
            slots: 0,
            tagRequirement: {
                tagRequirements: []
            },
            vendorPrice: 0
        }
    });

    useEffect(() => {
        if (!editMode || !opened) {
            return;
        }
        form.setValues(getInitial());
    }, [opened, getInitial, editMode]);

    function onSubmit(upgrade: Upgrade) {
        if (editMode) {
            UPGRADE_API.updateUpgrade(activeUniverse.name, upgrade.id, upgrade).then(refresh).then(close)
                .catch(handleValidationErrors(form.setErrors));
        } else {
            UPGRADE_API.insertAllUpgrades(activeUniverse.name, [upgrade]).then(refresh).then(close)
                .catch(handleValidationErrors(handleDatabaseInsertErrors(form.setErrors)));
        }
    }

    return <>
        <Modal opened={opened} onClose={close}
               title={editMode ? t('upgrade:upgradeEditTitle') : t('upgrade:upgradeCreationTitle')} maw={300}>
            <form onSubmit={form.onSubmit(onSubmit)}>
                <TextInput
                    label={t('name')}
                    key={form.key('name')}
                    {...form.getInputProps('name')}
                />
                <UpgradeRestrictionSelect
                    key={form.key('restriction')}
                    {...form.getInputProps('restriction')}
                />
                <TagRequirementsInput
                    label={t('upgrade:tagRequirement')}
                    tooltip={t('upgrade:tagRequirementTooltip')}
                    noRequirementsText={t('upgrade:noRequirements')}
                    addTagRequirementText={t('upgrade:addTagRequirement')}
                    key={form.key('tagRequirement')}
                    {...form.getInputProps('tagRequirement')}
                />
                <NumberInput
                    label={t('upgrade:necessary-slots')}
                    key={form.key('slots')}
                    {...form.getInputProps('slots')}
                    allowDecimal={false}
                />
                <Effect form={form}/>
                <Group grow align="flex-start">
                    <NumberInput
                        label={t('price')}
                        key={form.key('vendorPrice')}
                        {...form.getInputProps('vendorPrice')}
                        allowDecimal={false}
                    />
                    <TextInput
                        label={t('resultingPrice')}
                        readOnly
                        value={currencyFormatter(currencySettings, form.getValues().vendorPrice)}
                    />
                </Group>
                <Group justify="flex-end" mt="md">
                    <Button autoFocus variant="outline" onClick={close}>
                        {t('cancel')}
                    </Button>
                    <Button type="submit">
                        {editMode ? t('edit') : t('add')}
                    </Button>
                </Group>
            </form>
        </Modal>
        <Button data-testid={editMode ? 'edit' : 'add'} onClick={open} disabled={disabled}>
            {editMode ? t('edit') : t('add')}
        </Button>
    </>;
}

function Effect({
    form
}: {
    form: UseFormReturnType<Upgrade, (values: Upgrade) => Upgrade>;
}) {
    const {t} = useTranslation();

    return <Stack gap={0}>
        <Input.Label>
            {t('upgrade:effects')}
        </Input.Label>
        <Stack gap="xs">
            {form.getValues().effects.map((effect, index) =>
                <Stack key={'effect-' + index} gap={0}>
                    <Paper shadow="md" p="sm">
                        <Select
                            data={[
                                {value: 'SimpleUpgradeEffect', label: t('upgrade:simpleEffect')},
                                {value: 'EquipmentUpgradeEffect', label: t('upgrade:equipmentEffect')}
                            ]}
                            key={form.key(`effects.${index}.@type`)}
                            {...form.getInputProps(`effects.${index}.@type`)}
                        />
                        {effect['@type'] === 'EquipmentUpgradeEffect' ? <>
                            <UpgradeEquipmentManipulatorSelect
                                label={t('upgrade:upgradeManipulator')}
                                key={form.key(`effects.${index}.upgradeManipulator`)}
                                {...form.getInputProps(`effects.${index}.upgradeManipulator`)}
                            />
                            <Group wrap="nowrap">
                                <CalculationSelect
                                    label={t('upgrade:calculation')}
                                    key={form.key(`effects.${index}.calculation`)}
                                    {...form.getInputProps(`effects.${index}.calculation`)}
                                />
                                <NumberInput
                                    label={t('value')}
                                    key={form.key(`effects.${index}.value`)}
                                    {...form.getInputProps(`effects.${index}.value`)}
                                />
                            </Group>
                        </> : null}
                        <Group wrap="nowrap">
                            <Box
                                style={{flex: 1}}
                            >
                                <TextInput
                                    label={t('description')}
                                    key={form.key(`effects.${index}.description`)}
                                    {...form.getInputProps(`effects.${index}.description`)}
                                />
                            </Box>
                            <ActionIcon variant="outline" color="red" size="input-sm"
                                        onClick={() => form.removeListItem('effects', index)} mt={20}>
                                <FaRegTrashCan/>
                            </ActionIcon>
                        </Group>
                    </Paper>
                </Stack>
            )}
        </Stack>
        <Tooltip label={form.errors['effects']} disabled={!form.errors['effects']}>
            <Button
                onClick={() =>
                    form.insertListItem('effects', {
                        '@type': 'SimpleUpgradeEffect',
                        description: '',
                        upgradeManipulator: EUpgradeEquipmentManipulator.Damage,
                        calculation: ECalculation.Additive,
                        value: 0,
                        key: randomId()
                    })
                }
                mt="md"
                color={form.errors['effects'] ? 'red' : undefined}
            >
                {t('upgrade:addEffect')}
            </Button>
        </Tooltip>
    </Stack>;
}