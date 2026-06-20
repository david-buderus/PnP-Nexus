import {useTranslation} from 'react-i18next';
import {useUserContext} from '../../components/PageBase';
import {useEffect, useState} from 'react';
import {PasswordChange, PnPUser} from '../../api/model';
import {Button, Group, Modal, PasswordInput, Stack, Text, TextInput} from '@mantine/core';
import {useForm} from '@mantine/form';
import {handleValidationErrors} from '../../components/utils/ErrorUtils';
import ConfirmationDialog from '../../components/modal/ConfirmationDialog';
import {useDisclosure} from '@mantine/hooks';
import {useQueryClient} from '@tanstack/react-query';
import {getGetUserQueryKey, useRemoveUser, useUpdateUser} from '../../api/user-service/user-service';
import {useUpdatePassword} from '../../api/authentication-service/authentication-service';

/** View to change data about the currently logged-in user */
export function User() {
    const {t} = useTranslation();
    const queryClient = useQueryClient();
    const {user} = useUserContext();

    const [editMode, setEditMode] = useState(false);

    const form = useForm<PnPUser>({
        mode: 'uncontrolled',
        initialValues: user
    });

    useEffect(() => {
        form.setInitialValues(user);
        form.setValues(user);
    }, [user]);

    const {mutate: updateUser} = useUpdateUser({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({
                queryKey: getGetUserQueryKey(user.username),
            }).then(() => setEditMode(false)),
            onError: handleValidationErrors(form.setErrors)
        }
    });

    const {mutate: removeUser} = useRemoveUser({
        mutation: {
            onSuccess: () => window.location.reload()
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
        <TextInput
            label={t('username')}
            value={user.username}
            data-testid="username"
            readOnly
        />
        <form
            onSubmit={form.onSubmit(editedUser => updateUser({username: user.username, data: editedUser}))}
        >
            <TextInput
                label={t('displayName')}
                key={form.key('displayName')}
                {...form.getInputProps('displayName')}
                readOnly={!editMode}
            />
            <TextInput
                label={t('email')}
                key={form.key('email')}
                {...form.getInputProps('email')}
                readOnly={!editMode}
            />
            {editMode ?
                <Group wrap="nowrap" grow pt="xs">
                    <Button
                        onClick={() => {
                            setEditMode(false);
                            form.setValues(user);
                        }}
                        data-testid="cancel"
                        variant="outline"
                    >
                        {t('cancel')}
                    </Button>
                    <Button
                        data-testid="save"
                        type="submit"
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
        <ChangePasswordDialog/>
        <ConfirmationDialog
            title={t('user:confirmDeleteUser')}
            onConfirmation={() => removeUser({username: user.username})}
            openNode={open =>
                <Button
                    onClick={open}
                    color="red"
                    variant="outline"
                >
                    {t('user:deleteUser')}
                </Button>}
        />
    </Stack>;
}

function ChangePasswordDialog() {
    const {t} = useTranslation();
    const [opened, {open, close}] = useDisclosure(false);

    const [confirmPassword, setConfirmPassword] = useState('');
    const form = useForm<PasswordChange>({
        mode: 'controlled',
        initialValues: {
            oldPassword: '',
            newPassword: ''
        }
    });
    const passwordNotMatching = confirmPassword !== form.getValues().newPassword;

    const {mutate: updatePassword} = useUpdatePassword({
        mutation: {
            onSuccess: () => close(),
            onError: handleValidationErrors(form.setErrors)
        }
    });

    return <>
        <Modal
            opened={opened}
            onClose={close}
            title={t('user:changePasswordTitle')}
            maw={300}
            data-testid="change-password-dialog"
        >
            <form
                onSubmit={form.onSubmit(change => updatePassword({data: change}))}
            >
                <PasswordInput
                    label={t('user:oldPassword')}
                    key={form.key('oldPassword')}
                    {...form.getInputProps('oldPassword')}
                />
                <PasswordInput
                    label={t('user:newPassword')}
                    key={form.key('newPassword')}
                    {...form.getInputProps('newPassword')}
                />
                <PasswordInput
                    label={t('user:confirmPassword')}
                    value={confirmPassword}
                    onChange={(event) => setConfirmPassword(event.target.value)}
                    data-testid="confirmPassword"
                    error={passwordNotMatching && confirmPassword.length > 0 ? t('user:passwordNotMatching') : undefined}
                />
                <Group justify="flex-end" pt="md">
                    <Button autoFocus variant="outline" onClick={close}>
                        {t('cancel')}
                    </Button>
                    <Button type="submit" disabled={passwordNotMatching}>
                        {t('save')}
                    </Button>
                </Group>
            </form>
        </Modal>
        <Button
            onClick={open}
            data-testid="change-password"
        >
            {t('user:changePasswordTitle')}
        </Button>
    </>;
}