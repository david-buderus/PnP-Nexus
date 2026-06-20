import {Button, Group, Modal, Stack, TagsInput, TextInput} from '@mantine/core';
import {useForm} from '@mantine/form';
import {useDisclosure} from '@mantine/hooks';
import {useEffect, useMemo, useState} from 'react';
import {useTranslation} from 'react-i18next';
import {PrimaryAttribute, Talent} from '../../../api/model';
import {fetchAllPrimaryAttributes, fetchAllTags, fetchAllTalents} from '../../../components/Database';
import OverviewPage from '../../../components/OverviewPage';
import {useUniverseContext} from '../../../components/PageBase';
import {
    handleDatabaseInsertErrors,
    handleNetworkErrors,
    handleValidationErrors
} from '../../../components/utils/ErrorUtils';
import {ObjectSelect} from '../../../components/input/ObjectSelect';
import {filterNamedCell, NamedCell} from '../../../components/table/NamedCell';
import TagCell, {filterTagCell} from '../../../components/table/TagCell';
import {ExtendedColumnDef} from '../../../components/table/SortableTable';
import {useQueryClient} from '@tanstack/react-query';
import {
    getGetAllTalentsQueryKey,
    useDeleteAllTalents,
    useInsertAllTalents,
    useUpdateTalent
} from '../../../api/talent-service/talent-service';


/** Overview over all talents */
export function TalentOverview() {
    const {t} = useTranslation();
    const {activeUniverse} = useUniverseContext();
    const queryClient = useQueryClient();

    const [toEdit, setToEdit] = useState<Talent>(null);
    const [openedAdd, {open: openAdd, close: closeAdd}] = useDisclosure(false);

    const {mutateAsync: deleteTalents} = useDeleteAllTalents({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({queryKey: getGetAllTalentsQueryKey(activeUniverse.id)}),
            onError: handleNetworkErrors
        }
    });

    const columns = useMemo<ExtendedColumnDef<Talent, any>[]>(
        () => [
            {
                accessorKey: 'name',
                header: t('name'),
            },
            {
                accessorKey: 'tags',
                header: t('tags'),
                cell: TagCell,
                filterFn: filterTagCell
            },
            {
                accessorKey: 'firstAttribute',
                header: t('character:firstAttribute'),
                cell: NamedCell,
                filterFn: filterNamedCell
            },
            {
                accessorKey: 'secondAttribute',
                header: t('character:secondAttribute'),
                cell: NamedCell,
                filterFn: filterNamedCell
            },
            {
                accessorKey: 'thirdAttribute',
                header: t('character:thirdAttribute'),
                cell: NamedCell,
                filterFn: filterNamedCell
            }
        ], []);

    return <Stack>
        <OverviewPage
            fetchData={fetchAllTalents()}
            columns={columns}
            idKey="id"
            identifier="talents"
            deletionDialogTitle={t('character:talentDeletionTitle')}
            onDelete={(universe, talents) => deleteTalents({
                universe: universe,
                params: {ids: talents.map(talent => talent.id)}
            })}
            onAdd={openAdd}
            onEdit={s => setToEdit(s)}
        />
        <CreationDialog editMode={false} opened={openedAdd} close={closeAdd} initial={null}/>
        <CreationDialog editMode={true} opened={toEdit !== null} close={() => setToEdit(null)} initial={toEdit}/>
    </Stack>;
}

function CreationDialog({
    editMode,
    opened,
    close,
    initial
}: {
    editMode: boolean,
    opened: boolean,
    close: () => void
    initial: Talent
}) {
    const {t} = useTranslation();
    const queryClient = useQueryClient();
    const {activeUniverse} = useUniverseContext();
    const [attributes] = fetchAllPrimaryAttributes();
    const [tags] = fetchAllTags();

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
        if (!initial) {
            return;
        }
        form.setValues(initial);
        form.setInitialValues(initial);
    }, [initial]);

    const {mutateAsync: updateTalent} = useUpdateTalent({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({queryKey: getGetAllTalentsQueryKey(activeUniverse.id)}).then(close),
            onError: handleValidationErrors(form.setErrors)
        }
    });
    const {mutateAsync: insertTalents} = useInsertAllTalents({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({queryKey: getGetAllTalentsQueryKey(activeUniverse.id)}).then(close),
            onError: handleValidationErrors(handleDatabaseInsertErrors(form.setErrors))
        }
    });

    function onSubmit(talent: Talent) {
        if (editMode) {
            return updateTalent({
                universe: activeUniverse.id,
                id: talent.id,
                data: talent,
            });
        } else {
            return insertTalents({
                universe: activeUniverse.id,
                data: [talent]
            });
        }
    }

    return <Modal opened={opened} onClose={close}
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
    </Modal>;
}
