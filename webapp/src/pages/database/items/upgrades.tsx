import {useEffect, useMemo} from 'react';
import {useTranslation} from 'react-i18next';
import {TagRequirement, Upgrade, UpgradeServiceApi} from '../../../api';
import OverviewPage from '../../../components/OverviewPage';
import {fetchAllUpgrades} from '../../../components/Database';
import {API_CONFIGURATION} from '../../../components/Constants';
import CurrencyCell from '../../../components/table/CurrencyCell';
import {Button, Group, Modal, NumberInput, TextInput} from '@mantine/core';
import {useUniverseContext} from '../../../components/PageBase';
import {useForm} from '@mantine/form';
import {useDisclosure} from '@mantine/hooks';
import {handleDatabaseInsertErrors, handleValidationErrors} from '../../../components/utils/ErrorUtils';
import {currencyFormatter} from '../../../components/utils/Formatters';
import {UpgradeRestrictionSelect} from '../../../components/input/EnumSelect';
import TagRequirementsInput from '../../../components/input/TagRequirementsInput';
import {ExtendedColumnDef} from '../../../components/table/SortableTable';
import {filterItemEffectsCell, ItemEffectsCell} from '../../../components/table/ItemEffectsCell';
import {ItemEffectForm} from '../../../components/input/ItemEffectForm';

const UPGRADE_API = new UpgradeServiceApi(API_CONFIGURATION);

/** Overview over all upgrades */
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
            UPGRADE_API.updateUpgrade(activeUniverse.id, upgrade.id, upgrade).then(refresh).then(close)
                .catch(handleValidationErrors(form.setErrors));
        } else {
            UPGRADE_API.insertAllUpgrades(activeUniverse.id, [upgrade]).then(refresh).then(close)
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
                <ItemEffectForm form={form} path="effects"/>
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
