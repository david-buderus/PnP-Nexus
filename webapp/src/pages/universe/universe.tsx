import {useTranslation} from 'react-i18next';
import {useUniverseContext, useUserContext} from '../../components/PageBase';
import {useNavigate} from 'react-router';
import {
    ActionIcon,
    Autocomplete,
    Button,
    Card,
    Group,
    Modal,
    Select,
    SimpleGrid,
    Skeleton,
    Stack,
    Table,
    Text,
    Textarea,
    TextInput,
    Title
} from '@mantine/core';
import {useDisclosure} from '@mantine/hooks';
import ConfirmationDialog from '../../components/modal/ConfirmationDialog';
import {PrimaryAttribute, Universe} from '../../api/model';
import {useForm} from '@mantine/form';
import {handleValidationErrors} from '../../components/utils/ErrorUtils';
import {FaRegTrashCan} from 'react-icons/fa6';
import ItemSettingsForm from '../../components/settings/ItemSettingsForm';
import CurrencySettingsForm from '../../components/settings/CurrencySettingsForm';
import CharacterSettingsForm from '../../components/settings/CharacterSettingsForm';
import {numberFormatter, percentageFormatter} from '../../components/utils/Formatters';
import {probabilityForSuccesfulThrows} from '../../components/utils/DiceThrowUtils';
import {fetchAllPrimaryAttributes, fetchAllSimpleSecondaryAttributes} from '../../components/Database';
import {PrimaryAttributeForm} from '../../components/character/PrimaryAttributeForm';
import {SecondaryAttributeForm} from '../../components/character/SecondaryAttributeForm';
import {FaCheck} from 'react-icons/fa';
import EquipmentSettingsForm from '../../components/settings/EquipmentSettingsForm';
import CharacterSheetSettingsDialog from '../../components/settings/CharacterSheetSettingsDialog';
import {useQueryClient} from '@tanstack/react-query';
import {
    getGetAllUniversesQueryKey,
    getGetUniversePermissionsQueryKey,
    useAddUniversePermission,
    useDeleteUniverse,
    useGetUniversePermissions,
    useRemoveUniversePermission,
    useUpdateUniverse
} from '../../api/universe-service/universe-service';
import {useGetDisplayNames} from '../../api/user-service/user-service';

export default function UniverseOverview() {
    const {activeUniverse, setActiveUniverse} = useUniverseContext();
    const {t} = useTranslation();
    const queryClient = useQueryClient();
    const {userPermissions} = useUserContext();
    const [primaryAttributes] = fetchAllPrimaryAttributes();
    const navigate = useNavigate();


    const {mutateAsync: deleteUniverse} = useDeleteUniverse({
        mutation: {
            onSuccess: () => {
                setActiveUniverse(null);
                return queryClient.invalidateQueries({queryKey: getGetAllUniversesQueryKey()});
            }
        }
    });

    return <SimpleGrid cols={4}>
        <Card shadow="md" p="md" maw={400} pb={60}>
            <Title order={4} ta="center">
                {activeUniverse.displayName}
            </Title>
            <Text ta="left">
                {activeUniverse.description}
            </Text>
            {userPermissions.isActiveUniverseOwner &&
                <Group style={{position: 'absolute', bottom: 16, right: 16}}>
                    <ConfirmationDialog
                        title={t('universe:confirmDeletionTitle')}
                        onConfirmation={() =>
                            deleteUniverse({universe: activeUniverse.id}).then(() => navigate('/'))
                        }
                        openNode={open => <Button variant="outline" color="red" onClick={open}>
                            {t('delete')}
                        </Button>}
                    />
                    <EditUniverseDialog/>
                </Group>
            }
        </Card>
        <ItemSettingsCard/>
        <EquipmentSettingsCard/>
        <CurrencySettingsCard/>
        <CharacterSettingsCard primaryAttributes={primaryAttributes}/>
        <CharacterSheetSettingsCard/>
        <PrimaryAttributeCard primaryAttributes={primaryAttributes}/>
        <SecondaryAttributeCard/>
        {userPermissions.isActiveUniverseOwner &&
            <PermissionCard/>}
    </ SimpleGrid>;
}

function EditUniverseDialog() {
    const {t} = useTranslation();
    const queryClient = useQueryClient();
    const {activeUniverse} = useUniverseContext();

    const [opened, {open, close}] = useDisclosure(false);

    const {mutateAsync: updateUniverse} = useUpdateUniverse({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({queryKey: getGetAllUniversesQueryKey()})
        }
    });

    const form = useForm<Universe>({
        mode: 'uncontrolled',
        initialValues: activeUniverse
    });

    return <>
        <Modal opened={opened} onClose={close} title={t('universe:editUniverse')}>
            <form onSubmit={form.onSubmit((universe) => updateUniverse({
                universeId: universe.id,
                data: universe
            }).then(close).catch(handleValidationErrors(form.setErrors)))}>
                <TextInput
                    data-testid="displayName"
                    label={t('displayName')}
                    key={form.key('displayName')}
                    required
                    {...form.getInputProps('displayName')}
                />
                <Textarea
                    data-testid="shortDescription"
                    label={t('universe:shortDescription')}
                    autosize
                    minRows={2}
                    key={form.key('shortDescription')}
                    {...form.getInputProps('shortDescription')}
                />
                <Textarea
                    data-testid="description"
                    label={t('description')}
                    autosize
                    minRows={4}
                    key={form.key('description')}
                    {...form.getInputProps('description')}
                />
                <Group justify="flex-end">
                    <Button autoFocus variant="outline" onClick={close}>
                        {t('cancel')}
                    </Button>
                    <Button type="submit">
                        {t('confirm')}
                    </Button>
                </Group>
            </form>
        </Modal>
        <Button onClick={open}>
            {t('edit')}
        </Button>
    </>;

}

function ItemSettingsCard() {
    const {itemSettings} = useUniverseContext();
    const {userPermissions} = useUserContext();
    const {t} = useTranslation();
    const [opened, {open, close}] = useDisclosure(false);

    return <Card shadow="md" p="md" maw={400} pb={60}>
        <Title order={5} ta="center">
            {t('universe:itemSettings')}
        </Title>
        {itemSettings?.wearFactor > 0 ?
            <Text ta="left">
                {t('universe:wearFactorDescription', {'wearFactor': itemSettings.wearFactor})}
            </Text>
            :
            <Text ta="left">
                {t('universe:wearFactorDisabled')}
            </Text>
        }
        {itemSettings?.shieldUsingDice ?
            <Text ta="left">
                {t('universe:shieldUsingDiceDescription')}
            </Text>
            : null
        }
        {itemSettings?.usingProtection ?
            <Text ta="left">
                {t('universe:usingProtectionDescription')}
            </Text>
            : null
        }
        <Modal opened={opened} onClose={close}>
            <ItemSettingsForm
                onSave={close}
                onSaveText={t('save')}
            />
        </Modal>
        {userPermissions.isActiveUniverseOwner && <Group style={{position: 'absolute', bottom: 16, right: 16}}>
            <Button onClick={open}>
                {t('edit')}
            </Button>
        </Group>}
    </Card>;
}

function EquipmentSettingsCard() {
    const {equipmentSettings} = useUniverseContext();
    const {userPermissions} = useUserContext();
    const {t} = useTranslation();
    const [opened, {open, close}] = useDisclosure(false);

    return <Card shadow="md" p="md" maw={400} pb={60}>
        <Title order={5} ta="center">
            {t('universe:equipmentSettings')}
        </Title>
        <Text ta="left">
            {t('universe:numberOfHandheldDescription', {'number': equipmentSettings?.numberOfHandheld})}
        </Text>
        <Title order={6} ta="center" pt="md">
            {t('universe:jewelleryDefinitions')}
        </Title>
        <Table>
            <Table.Thead>
                <Table.Tr>
                    <Table.Th>{t('name')}</Table.Th>
                    <Table.Th>{t('tag')}</Table.Th>
                    <Table.Th>{t('amount')}</Table.Th>
                </Table.Tr>
            </Table.Thead>
            <Table.Tbody>
                {equipmentSettings?.jewelleryDefinitions.map((definition, i) => {
                    return <Table.Tr key={i}>
                        <Table.Td>
                            {definition?.name || ''}
                        </Table.Td>
                        <Table.Td>
                            {definition?.tag || ''}
                        </Table.Td>
                        <Table.Td>
                            {definition?.amount || ''}
                        </Table.Td>
                    </Table.Tr>;
                })}
            </Table.Tbody>
        </Table>
        <Modal opened={opened} onClose={close}>
            <EquipmentSettingsForm
                onSave={close}
                onSaveText={t('save')}
            />
        </Modal>
        {userPermissions.isActiveUniverseOwner && <Group style={{position: 'absolute', bottom: 16, right: 16}}>
            <Button onClick={open}>
                {t('edit')}
            </Button>
        </Group>}
    </Card>;
}

function CurrencySettingsCard() {
    const {currencySettings} = useUniverseContext();
    const {userPermissions} = useUserContext();
    const {t} = useTranslation();
    const [opened, {open, close}] = useDisclosure(false);

    return <Card shadow="md" p="md" maw={400} pb={60}>
        <Title order={5} ta="center">
            {t('universe:currencySettings')}
        </Title>
        <Text ta="left">
            {t('universe:baseCurrencyDescription', {
                'currency': currencySettings?.baseCurrency,
                'shortForm': currencySettings?.baseCurrencyShortForm
            })}
            {currencySettings?.calculationEntries.map((entry, index) => ' ' + t('universe:calculationCurrencyDescription', {
                'factor': entry.factor,
                'currency': entry.currency,
                'shortForm': entry.currencyShortForm,
                'prevCurrency': index === 0 ? currencySettings.baseCurrency : currencySettings.calculationEntries[index - 1].currency
            }))}
        </Text>
        <Modal opened={opened} onClose={close} size="auto">
            <CurrencySettingsForm
                onSave={close}
                onSaveText={t('save')}
            />
        </Modal>
        {userPermissions.isActiveUniverseOwner && <Group style={{position: 'absolute', bottom: 16, right: 16}}>
            <Button onClick={open}>
                {t('edit')}
            </Button>
        </Group>}
    </Card>;
}

function CharacterSettingsCard({primaryAttributes}: { primaryAttributes: PrimaryAttribute[]; }) {
    const {characterSettings} = useUniverseContext();
    const {userPermissions} = useUserContext();
    const {t} = useTranslation();
    const attributeLength = primaryAttributes.length;
    const [opened, {open, close}] = useDisclosure(false);

    if (!characterSettings) {
        return <Card shadow="md" p="md" maw={400}>
            <Title order={5} ta="center">
                {t('universe:characterSettings')}
            </Title>
            <Skeleton height={8} radius="xl"/>
            <Skeleton height={8} mt={6} radius="xl"/>
            <Skeleton height={8} mt={6} radius="xl"/>
            <Skeleton height={8} mt={6} radius="xl"/>
            <Skeleton height={8} mt={6} radius="xl"/>
            <Skeleton height={8} mt={6} width="70%" radius="xl"/>
        </Card>;
    }

    return <Card shadow="md" p="md" maw={400} pb={60}>
        <Title order={5} ta="center">
            {t('universe:characterSettings')}
        </Title>
        <Text ta="left">
            {t('universe:primaryAttributeDistributionExplanation', {
                'average': numberFormatter(characterSettings.maxPrimaryAttributeSum / attributeLength),
                'max': numberFormatter(Math.floor(
                    (characterSettings.maxPrimaryAttributeSum - (attributeLength * characterSettings.minPrimaryAttributeValue)) /
                    (characterSettings.maxPrimaryAttributeValue - characterSettings.minPrimaryAttributeValue)
                )),
                'averageChance': percentageFormatter(probabilityForSuccesfulThrows(
                    characterSettings.maxPrimaryAttributeSum / attributeLength,
                    characterSettings.maxPrimaryAttributeSum / attributeLength,
                    characterSettings.maxPrimaryAttributeSum / attributeLength
                )),
                'highestChance': percentageFormatter(probabilityForSuccesfulThrows(characterSettings.maxPrimaryAttributeValue, characterSettings.maxPrimaryAttributeValue, characterSettings.maxPrimaryAttributeValue)),
                'lowestChance': percentageFormatter(probabilityForSuccesfulThrows(characterSettings.minPrimaryAttributeValue, characterSettings.minPrimaryAttributeValue, characterSettings.minPrimaryAttributeValue))
            })}
        </Text>
        <Modal opened={opened} onClose={close} size="auto">
            <CharacterSettingsForm
                onSave={close}
                onSaveText={t('save')}
            />
        </Modal>
        {userPermissions.isActiveUniverseOwner && <Group style={{position: 'absolute', bottom: 16, right: 16}}>
            <Button onClick={open}>
                {t('edit')}
            </Button>
        </Group>}
    </Card>;
}

function CharacterSheetSettingsCard() {
    const {sheetSettings} = useUniverseContext();
    const {userPermissions} = useUserContext();
    const {t} = useTranslation();

    return <Card shadow="md" p="md" maw={400} pb={60}>
        <Title order={5} ta="center">
            {t('universe:characterSheetSettings')}
        </Title>
        {sheetSettings?.playerSheet ?
            <Text ta="left">
                {t('universe:playerSheetDescription', {
                    'sheet': sheetSettings.playerSheet.name
                })}
            </Text>
            :
            <Text ta="left">
                {t('universe:noPlayerSheetDescription')}
            </Text>
        }
        {sheetSettings?.enemySheet ?
            <Text ta="left">
                {t('universe:enemySheetDescription', {
                    'sheet': sheetSettings.enemySheet.name
                })}
            </Text>
            :
            <Text ta="left">
                {t('universe:noEnemySheetDescription')}
            </Text>
        }
        {userPermissions.isActiveUniverseOwner && <Group style={{position: 'absolute', bottom: 16, right: 16}}>
            <CharacterSheetSettingsDialog/>
        </Group>}
    </Card>;
}

function PrimaryAttributeCard({primaryAttributes}: {
    primaryAttributes: PrimaryAttribute[],
}) {
    const {userPermissions} = useUserContext();
    const {t} = useTranslation();
    const [opened, {open, close}] = useDisclosure(false);

    return <Card shadow="md" p="md" maw={400} pb={60}>
        <Title order={5} ta="center">
            {t('primary-attributes')}
        </Title>
        <Table>
            <Table.Thead>
                <Table.Tr>
                    <Table.Th>{t('name')}</Table.Th>
                    <Table.Th>{t('character:shortName')}</Table.Th>
                </Table.Tr>
            </Table.Thead>
            <Table.Tbody>
                {primaryAttributes.map((attribute, i) => {
                    return <Table.Tr key={i}>
                        <Table.Td>
                            {attribute?.name || ''}
                        </Table.Td>
                        <Table.Td>
                            {attribute?.shortName || ''}
                        </Table.Td>
                    </Table.Tr>;
                })}
            </Table.Tbody>
        </Table>
        <Modal opened={opened} onClose={close} title={t('primary-attributes')}>
            <PrimaryAttributeForm
                onSave={close}
                onSaveText={t('save')}
            />
        </Modal>
        {userPermissions.isActiveUniverseOwner && <Group style={{position: 'absolute', bottom: 16, right: 16}}>
            <Button onClick={open}>
                {t('edit')}
            </Button>
        </Group>}
    </Card>;
}

function SecondaryAttributeCard() {
    const {userPermissions} = useUserContext();
    const {t} = useTranslation();
    const [secondaryAttribute, refresh] = fetchAllSimpleSecondaryAttributes();
    const [opened, {open, close}] = useDisclosure(false);

    return <Card shadow="md" p="md" maw={400} pb={60}>
        <Title order={5} ta="center">
            {t('secondary-attributes')}
        </Title>
        <Table>
            <Table.Thead>
                <Table.Tr>
                    <Table.Th>{t('name')}</Table.Th>
                    <Table.Th>{t('character:shortName')}</Table.Th>
                    <Table.Th>{t('character:calculationFormula')}</Table.Th>
                    <Table.Th>{t('character:consumableAttribute')}</Table.Th>
                </Table.Tr>
            </Table.Thead>
            <Table.Tbody>
                {secondaryAttribute.map((attribute, i) => {
                    return <Table.Tr key={i}>
                        <Table.Td>
                            {attribute?.name || ''}
                        </Table.Td>
                        <Table.Td>
                            {attribute?.shortName || ''}
                        </Table.Td>
                        <Table.Td>
                            {attribute?.calculationFormula || ''}
                        </Table.Td>
                        <Table.Td>
                            {attribute?.consumable ? <FaCheck/> : null}
                        </Table.Td>
                    </Table.Tr>;
                })}
            </Table.Tbody>
        </Table>
        <Modal opened={opened} onClose={close} title={t('secondary-attributes')} size="auto">
            <SecondaryAttributeForm
                onSave={() => {
                    close();
                    refresh();
                }}
                onSaveText={t('save')}
            />
        </Modal>
        {userPermissions.isActiveUniverseOwner && <Group style={{position: 'absolute', bottom: 16, right: 16}}>
            <Button onClick={open}>
                {t('edit')}
            </Button>
        </Group>}
    </Card>;
}

function PermissionCard() {
    const {activeUniverse} = useUniverseContext();
    const queryClient = useQueryClient();
    const {t} = useTranslation();

    const universePermissions = useGetUniversePermissions(activeUniverse.id).data?.data ?? [];

    const {mutate: removeUniversePermission} = useRemoveUniversePermission({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({
                queryKey: getGetUniversePermissionsQueryKey(activeUniverse.id),
            })
        }
    });

    return <Card shadow="md" p="md" maw={400} pb={60}>
        <Title order={5} ta="center">
            {t('universe:permissions')}
        </Title>
        <Table>
            <Table.Thead>
                <Table.Tr>
                    <Table.Th>{t('displayName')}</Table.Th>
                    <Table.Th>{t('permission')}</Table.Th>
                    <Table.Th></Table.Th>
                </Table.Tr>
            </Table.Thead>
            <Table.Tbody>
                {universePermissions.map((permission, i) => {
                    return <Table.Tr key={i}>
                        <Table.Td>
                            {permission?.displayName || ''}
                        </Table.Td>
                        <Table.Td>
                            {t('permission:' + permission.dto.permission.toLocaleLowerCase())}
                        </Table.Td>
                        <Table.Td>
                            <ConfirmationDialog
                                title={t('universe:confirmPermissionDeletionTitle')}
                                onConfirmation={() => removeUniversePermission({
                                    universe: activeUniverse.id,
                                    params: {displayName: permission.displayName}
                                })}
                                openNode={open => <ActionIcon variant="outline" color="red" onClick={open}>
                                    <FaRegTrashCan/>
                                </ActionIcon>}
                            />
                        </Table.Td>
                    </Table.Tr>;
                })}
            </Table.Tbody>
        </Table>
        <Group style={{position: 'absolute', bottom: 16, right: 16}}>
            <PermissionDialog/>
        </Group>
    </Card>;
}

function PermissionDialog() {
    const {t} = useTranslation();
    const queryClient = useQueryClient();
    const {activeUniverse} = useUniverseContext();
    const [opened, {open, close}] = useDisclosure(false);
    const displayNames = useGetDisplayNames().data?.data ?? [];

    const form = useForm({
        mode: 'uncontrolled',
        initialValues: {
            displayName: '',
            permission: 'READ'
        }
    });

    const {mutate: addUniversePermission} = useAddUniversePermission({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({
                queryKey: getGetUniversePermissionsQueryKey(activeUniverse.id),
            }).then(close),
            onError: err => {
                if (err.response.status !== 404) {
                    handleValidationErrors(form.setErrors)(err);
                    return;
                }
                form.setFieldError('displayName', t('user:unknownUser'));
            }
        }
    });

    return <>
        <Modal opened={opened} onClose={close} title={t('universe:addPermission')}>
            <form
                onSubmit={form.onSubmit((values) => addUniversePermission({
                    universe: activeUniverse.id,
                    params: {displayName: values.displayName, accessPermission: values.permission}
                }))}>
                <Stack>
                    <Autocomplete
                        label={t('name')}
                        data={displayNames}
                        key={form.key('displayName')}
                        {...form.getInputProps('displayName')}
                    />
                    <Select
                        label={t('permission')}
                        data={[
                            {
                                value: 'READ',
                                label: t('permission:read')
                            },
                            {
                                value: 'WRITE',
                                label: t('permission:write')
                            },
                            {
                                value: 'OWNER',
                                label: t('permission:owner')
                            }
                        ]}
                        key={form.key('permission')}
                        {...form.getInputProps('permission')}
                    />
                    <Group justify="flex-end">
                        <Button autoFocus variant="outline" onClick={close}>
                            {t('cancel')}
                        </Button>
                        <Button type="submit">
                            {t('add')}
                        </Button>
                    </Group>
                </Stack>
            </form>
        </Modal>
        <Button onClick={open}>
            {t('add')}
        </Button>
    </>;
}