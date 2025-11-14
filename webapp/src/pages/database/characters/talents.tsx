import {Button, Group, Modal, TagsInput, TextInput} from '@mantine/core';
import {useForm} from '@mantine/form';
import {useDisclosure} from '@mantine/hooks';
import {useEffect, useMemo} from 'react';
import {useTranslation} from 'react-i18next';
import {PrimaryAttribute, Talent, TalentServiceApi} from '../../../api';
import {fetchAllPrimaryAttributes, fetchAllTags, fetchAllTalents} from '../../../components/Database';
import OverviewPage, {ExtendedColumnDef} from '../../../components/OverviewPage';
import {useUniverseContext} from '../../../components/PageBase';
import {handleDatabaseInsertErrors, handleValidationErrors} from '../../../components/utils/ErrorUtils';
import {API_CONFIGURATION} from '../../../components/Constants';
import {ObjectSelect} from '../../../components/input/ObjectSelect';
import {filterNamedCell, NamedCell} from '../../../components/table/NamedCell';
import TagCell, {filterTagCell} from '../../../components/table/TagCell';

const TALENT_API = new TalentServiceApi(API_CONFIGURATION);

/** Overview over all talents */
export function TalentOverview() {
    const {t} = useTranslation();

    const columns = useMemo<ExtendedColumnDef<Talent, any>[]>(
        () => [
            {
                accessorKey: 'name',
                header: t('name'),
            },
            {
                accessorKey: 'tags',
                header: t('tags'),
                Cell: TagCell,
                filterFn: filterTagCell
            },
            {
                accessorKey: 'firstAttribute',
                header: t('character:firstAttribute'),
                Cell: NamedCell,
                filterFn: filterNamedCell
            },
            {
                accessorKey: 'secondAttribute',
                header: t('character:secondAttribute'),
                Cell: NamedCell,
                filterFn: filterNamedCell
            },
            {
                accessorKey: 'thirdAttribute',
                header: t('character:thirdAttribute'),
                Cell: NamedCell,
                filterFn: filterNamedCell
            }
        ], []);

    return <OverviewPage
        fetchData={fetchAllTalents()}
        columns={columns}
        identifier="talents"
        manipulationDialog={(editMode, refresh, disabled, getInitial) => <CreationDialog editMode={editMode}
                                                                                         refresh={refresh}
                                                                                         disabled={disabled}
                                                                                         getInitial={getInitial}/>}
        deletionDialogTitle={t('character:talentDeletionTitle')}
        onDelete={(universe, talents) => TALENT_API.deleteAllTalents(universe, talents.map(talent => talent.id))}
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
    getInitial: () => Talent;
}) {
    const {t} = useTranslation();
    const {activeUniverse} = useUniverseContext();
    const [attributes] = fetchAllPrimaryAttributes();
    const [tags] = fetchAllTags();

    const [opened, {open, close}] = useDisclosure(false);
    const form = useForm<Talent>({
        mode: 'controlled',
        initialValues: {
            name: '',
            tags: [],
            firstAttribute: undefined,
            secondAttribute: undefined,
            thirdAttribute: undefined
        }
    });

    useEffect(() => {
        if (!editMode || !opened) {
            return;
        }
        form.setValues(getInitial());
    }, [opened, getInitial, editMode]);

    function onSubmit(talent: Talent) {
        if (editMode) {
            TALENT_API.updateTalent(activeUniverse.id, talent.id, talent).then(refresh).then(close)
                .catch(handleValidationErrors(form.setErrors));
        } else {
            TALENT_API.insertAllTalents(activeUniverse.id, [talent]).then(refresh).then(close)
                .catch(handleValidationErrors(handleDatabaseInsertErrors(form.setErrors)));
        }
    }

    return <>
        <Modal opened={opened} onClose={close}
               title={editMode ? t('character:talentEditTitle') : t('character:talentCreationTitle')} maw={300}>
            <form onSubmit={form.onSubmit(onSubmit)}>
                <TextInput
                    label={t('name')}
                    key={form.key('name')}
                    {...form.getInputProps('name')}
                />
                <TagsInput
                    label={t('tags')}
                    data={tags}
                    clearable
                    key={form.key('tags')}
                    {...form.getInputProps('tags')}
                />
                <ObjectSelect<PrimaryAttribute>
                    label={t('character:firstAttribute')}
                    key={form.key('firstAttribute')}
                    {...form.getInputProps('firstAttribute')}
                    data={attributes}
                    idKey="id"
                    labelKey="name"
                />
                <ObjectSelect<PrimaryAttribute>
                    label={t('character:secondAttribute')}
                    key={form.key('secondAttribute')}
                    {...form.getInputProps('secondAttribute')}
                    data={attributes}
                    idKey="id"
                    labelKey="name"
                />
                <ObjectSelect<PrimaryAttribute>
                    label={t('character:thirdAttribute')}
                    key={form.key('thirdAttribute')}
                    {...form.getInputProps('thirdAttribute')}
                    data={attributes}
                    idKey="id"
                    labelKey="name"
                />
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
