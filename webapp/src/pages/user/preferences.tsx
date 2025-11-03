import {useTranslation} from 'react-i18next';
import {useUserContext} from '../../components/PageBase';
import {useForm} from '@mantine/form';
import {PnPUserPreference, UserServiceApi} from '../../api';
import {useEffect, useState} from 'react';
import {Button, Group, Stack, Text} from '@mantine/core';
import {API_CONFIGURATION} from '../../components/Constants';
import {handleValidationErrors} from '../../components/utils/ErrorUtils';
import LanguageSelect from '../../components/input/LanguageSelect';

const USER_API = new UserServiceApi(API_CONFIGURATION);

/**
 * View to manipulate the preferences of the current logged-in user.
 */
export function UserPreferences() {
    const {t} = useTranslation();
    const {user, userPreferences, refreshUser} = useUserContext();

    const [editMode, setEditMode] = useState(false);

    const form = useForm<PnPUserPreference>({
        mode: 'uncontrolled',
        initialValues: userPreferences
    });

    useEffect(() => {
        form.setValues(userPreferences);
    }, [userPreferences]);

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
            onSubmit={form.onSubmit((editedPreferences) => USER_API.updateUserPreferences(user.username, editedPreferences)
                .then(() => {
                    setEditMode(false);
                    refreshUser();
                })
                .catch(handleValidationErrors(form.setErrors)))}
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