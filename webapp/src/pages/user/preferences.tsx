import {useTranslation} from 'react-i18next';
import {useUserContext} from '../../components/PageBase';
import {useForm} from '@mantine/form';
import {PnPUserPreference} from '../../api/model';
import {useEffect, useState} from 'react';
import {Button, Group, Stack, Text} from '@mantine/core';
import {handleValidationErrors} from '../../components/utils/ErrorUtils';
import LanguageSelect from '../../components/input/LanguageSelect';
import {getGetUserPreferencesQueryKey, useUpdateUserPreferences} from '../../api/user-service/user-service';
import {useQueryClient} from '@tanstack/react-query';

/**
 * View to manipulate the preferences of the current logged-in user.
 */
export function UserPreferences() {
    const {t} = useTranslation();
    const queryClient = useQueryClient();
    const {user, userPreferences} = useUserContext();

    const [editMode, setEditMode] = useState(false);

    const form = useForm<PnPUserPreference>({
        mode: 'uncontrolled',
        initialValues: userPreferences
    });

    useEffect(() => {
        form.setInitialValues(userPreferences);
        form.setValues(userPreferences);
    }, [userPreferences]);

    const {mutate: updateUserPreferences} = useUpdateUserPreferences({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({
                queryKey: getGetUserPreferencesQueryKey(user.username),
            }).then(() => setEditMode(false)),
            onError: handleValidationErrors(form.setErrors)
        }
    });

    if (!user) {
        return <Text>
            You need to log in.
        </Text>;
    }

    return <Stack
        gap="xs"
        maw={300}
    >
        <form
            onSubmit={form.onSubmit(editedPreferences => updateUserPreferences({
                username: user.username,
                data: editedPreferences
            }))}
        >
            <LanguageSelect
                key={form.key('language')}
                {...form.getInputProps('language')}
                readOnly={!editMode}
            />
            {editMode ?
                <Group wrap="nowrap" grow pt="xs">
                    <Button
                        onClick={() => {
                            form.setValues(userPreferences);
                            setEditMode(false);
                        }}
                        variant="outline"
                        data-testid="cancel"
                    >
                        {t('cancel')}
                    </Button>
                    <Button
                        type="submit"
                        data-testid="save"
                    >
                        {t('save')}
                    </Button>
                </Group>
                : null}

        </form>
        {!editMode ?
            <Button
                data-testid="edit"
                onClick={() => setEditMode(true)}
            >
                {t('edit')}
            </Button>
            : null}
    </Stack>;
}