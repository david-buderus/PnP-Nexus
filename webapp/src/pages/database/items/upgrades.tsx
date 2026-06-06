import {useEffect, useMemo, useState} from 'react';
import {useTranslation} from 'react-i18next';
import {TagRequirement, Upgrade} from '../../../api/model';
import OverviewPage from '../../../components/OverviewPage';
import {fetchAllUpgrades} from '../../../components/Database';
import CurrencyCell from '../../../components/table/CurrencyCell';
import {Button, Group, Modal, NumberInput, Stack, TextInput} from '@mantine/core';
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
import {useQueryClient} from '@tanstack/react-query';
import {
    getGetAllUpgradesQueryKey,
    useDeleteAllUpgrades,
    useInsertAllUpgrades,
    useUpdateUpgrade
} from '../../../api/upgrade-service/upgrade-service';

/** Overview over all upgrades */
export function UpgradeOverview() {
    const {t} = useTranslation();
    const queryClient = useQueryClient();
    const {activeUniverse} = useUniverseContext();

    const [toEdit, setToEdit] = useState<Upgrade>(null);
    const [openedAdd, {open: openAdd, close: closeAdd}] = useDisclosure(false);

    const {mutateAsync: deleteUpgrade} = useDeleteAllUpgrades({
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

    return <Stack>
        <OverviewPage
            fetchData={fetchAllUpgrades()}
            columns={columns}
            idKey="id"
            identifier="upgrades"
            deletionDialogTitle={t('upgrade:upgradeDeletionTitle')}
            onDelete={(universe, upgrades) => deleteUpgrade({
                universe: universe,
                params: {ids: upgrades.map(upgrade => upgrade.id)}
            })}
            viewModal={(upgrade, onClose) => <UpgradeCardModal upgrade={upgrade} onClose={onClose}/>}
            onAdd={openAdd}
            onEdit={s => setToEdit(s)}
        />
        <CreationDialog editMode={false} opened={openedAdd} close={closeAdd} upgrade={null}/>
        <CreationDialog editMode={true} opened={toEdit !== null} close={() => setToEdit(null)} upgrade={toEdit}/>
    </Stack>;
}

function CreationDialog({
    editMode,
    opened,
    close,
    upgrade
}: {
    editMode: boolean,
    opened: boolean,
    close: () => void
    upgrade: Upgrade
}) {
    const {t} = useTranslation();
    const queryClient = useQueryClient();
    const {activeUniverse, currencySettings} = useUniverseContext();

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
        if (!upgrade) {
            return;
        }
        form.setValues(upgrade);
        form.setInitialValues(upgrade);
    }, [upgrade]);

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

    function onSubmit(u: Upgrade) {
        if (editMode) {
            return updateUpgrade({
                universe: activeUniverse.id,
                id: u.id,
                data: u
            });
        } else {
            return insertUpgrades({
                universe: activeUniverse.id,
                data: [u]
            });
        }
    }

    return <Modal
        opened={opened}
        onClose={close}
        title={editMode ? t('upgrade:upgradeEditTitle') : t('upgrade:upgradeCreationTitle')}
        maw={300}
    >
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
    </Modal>;
}
