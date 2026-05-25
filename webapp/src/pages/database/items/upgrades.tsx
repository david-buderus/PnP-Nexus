import {useEffect, useMemo} from 'react';
import {useTranslation} from 'react-i18next';
import {TagRequirement, Upgrade} from '../../../api/model';
import OverviewPage from '../../../components/OverviewPage';
import {fetchAllUpgrades} from '../../../components/Database';
import CurrencyCell from '../../../components/table/CurrencyCell';
import {Button, Group, Modal, NumberInput, TextInput} from '@mantine/core';
import {useUniverseContext} from '../../../components/PageBase';
import {useForm} from '@mantine/form';
import {useDisclosure} from '@mantine/hooks';
import {
    handleDatabaseInsertErrors,
    handleNetworkErrors,
    handleValidationErrors
} from '../../../components/utils/ErrorUtils';
import {currencyFormatter} from '../../../components/utils/Formatters';
import {UpgradeRestrictionSelect} from '../../../components/input/EnumSelect';
import TagRequirementsInput from '../../../components/input/TagRequirementsInput';
import {ExtendedColumnDef} from '../../../components/table/SortableTable';
import {filterItemEffectsCell, ItemEffectsCell} from '../../../components/table/ItemEffectsCell';
import {ItemEffectForm} from '../../../components/input/ItemEffectForm';
import {UpgradeCardModal} from '../../../components/items/UpgradeCard';
import {useDeleteAllMaterials} from '../../../api/material-service/material-service';
import {useQueryClient} from '@tanstack/react-query';
import {
    getGetAllUpgradesQueryKey,
    useInsertAllUpgrades,
    useUpdateUpgrade
} from '../../../api/upgrade-service/upgrade-service';

/** Overview over all upgrades */
export function UpgradeOverview() {
    const {t} = useTranslation();
    const queryClient = useQueryClient();
    const {activeUniverse} = useUniverseContext();

    const {mutateAsync: deleteUpgrade} = useDeleteAllMaterials({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({queryKey: getGetAllUpgradesQueryKey(activeUniverse.id)}),
            onError: handleNetworkErrors
        }
    });

    const columns = useMemo<ExtendedColumnDef<Upgrade, any>[]>(
        () => [
            {
                accessorKey: 'name',
                header: t('name'),
            },
            {
                accessorKey: 'effects',
                header: t('upgrade:effects'),
                cell: ItemEffectsCell,
                filterFn: filterItemEffectsCell
            },
            {
                accessorKey: 'restriction',
                header: t('upgrade:restriction'),
                cell: cell => t('enum:' + cell.cell.getValue()?.toLowerCase())
            },
            {
                accessorKey: 'tagRequirement',
                header: t('upgrade:tagRequirement'),
                cell: cell => cell.getValue<TagRequirement>().tagRequirements.map(tags => tags.join(', ')).join(' ' + t('or') + ' '),
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
                cell: CurrencyCell
            }
        ], []);

    return <OverviewPage
        fetchData={fetchAllUpgrades()}
        columns={columns}
        identifier="upgrades"
        manipulationDialog={(editMode, refresh, disabled, getInitial) =>
            <CreationDialog
                editMode={editMode}
                disabled={disabled}
                getInitial={getInitial}
            />}
        deletionDialogTitle={t('upgrade:upgradeDeletionTitle')}
        onDelete={(universe, upgrades) => deleteUpgrade({
            universe: universe,
            params: {ids: upgrades.map(upgrade => upgrade.id)}
        })}
        idKey="id"
        viewModal={(upgrade, onClose) => <UpgradeCardModal upgrade={upgrade} onClose={onClose}/>}
    />;
}

function CreationDialog({
    editMode,
    disabled,
    getInitial
}: {
    editMode: boolean,
    disabled: boolean;
    getInitial: () => Upgrade;
}) {
    const {t} = useTranslation();
    const queryClient = useQueryClient();
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

    const {mutateAsync: updateUpgrade} = useUpdateUpgrade({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({queryKey: getGetAllUpgradesQueryKey(activeUniverse.id)}).then(close),
            onError: handleValidationErrors(form.setErrors)
        }
    });
    const {mutateAsync: insertUpgrades} = useInsertAllUpgrades({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({queryKey: getGetAllUpgradesQueryKey(activeUniverse.id)}).then(close),
            onError: handleValidationErrors(handleDatabaseInsertErrors(form.setErrors))
        }
    });

    function onSubmit(upgrade: Upgrade) {
        if (editMode) {
            return updateUpgrade({
                universe: activeUniverse.id,
                id: upgrade.id,
                data: upgrade
            });
        } else {
            return insertUpgrades({
                universe: activeUniverse.id,
                data: [upgrade]
            });
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
                    dataTestIdPrefix="tagRequirement."
                    key={form.key('tagRequirement')}
                    {...form.getInputProps('tagRequirement')}
                />
                <NumberInput
                    label={t('upgrade:necessary-slots')}
                    key={form.key('slots')}
                    {...form.getInputProps('slots')}
                    allowDecimal={false}
                />
                <ItemEffectForm
                    form={form}
                    path="effects"
                    restrictions={[form.values.restriction]}
                />
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
