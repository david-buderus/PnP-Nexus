import React from 'react';
import {Button, Group, Modal, Stack, Switch} from '@mantine/core';
import {useTranslation} from 'react-i18next';
import {useDisclosure} from '@mantine/hooks';

/** Interface for the context */
export type PnPCharacterPrintContextContent = {
    showItems: boolean;
    showSpells: boolean;
    showTalents: boolean;
    showLevel: boolean;
}

/** Context in character view for printing */
export const PnPCharacterPrintContext = React.createContext<PnPCharacterPrintContextContent>({
    showItems: true, showSpells: true, showTalents: true, showLevel: true
});

/**
 * A modal for print selection.
 */
export function PrintModal({
    printOptions, setPrintOptions
}: {
    printOptions: PnPCharacterPrintContextContent;
    setPrintOptions: (printOptions: PnPCharacterPrintContextContent) => void;
}) {
    const {t} = useTranslation();
    const [opened, {open, close}] = useDisclosure(false);


    return <>
        <Modal opened={opened} onClose={close} title={t('print')}>
            <Stack>
                <Switch
                    label={t('character:printItems')}
                    checked={printOptions.showItems}
                    onChange={event => setPrintOptions({...printOptions, showItems: event.target.checked})}
                />
                <Switch
                    label={t('character:printSpells')}
                    checked={printOptions.showSpells}
                    onChange={event => setPrintOptions({...printOptions, showSpells: event.target.checked})}
                />
                <Switch
                    label={t('character:printTalents')}
                    checked={printOptions.showTalents}
                    onChange={event => setPrintOptions({...printOptions, showTalents: event.target.checked})}
                />
                <Switch
                    label={t('character:printLevel')}
                    checked={printOptions.showLevel}
                    onChange={event => setPrintOptions({...printOptions, showLevel: event.target.checked})}
                />
                <Group justify="flex-end">
                    <Button autoFocus variant="outline" onClick={close}>
                        {t('cancel')}
                    </Button>
                    <Button type="submit" onClick={() => {
                        setTimeout(() => {
                            window.print();
                        }, 500);
                        close();
                    }}>
                        {t('print')}
                    </Button>
                </Group>
            </Stack>
        </Modal>
        <Button onClick={open}>
            {t('print')}
        </Button>
    </>;
}