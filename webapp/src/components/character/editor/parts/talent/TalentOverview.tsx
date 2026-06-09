import React, {useContext} from 'react';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {Stack, Text} from '@mantine/core';
import {useCalculateTalentPoints} from '../../../../../api/pn-p-character-service/pn-p-character-service';
import {useUniverseContext} from '../../../../PageBase';
import {useTranslation} from 'react-i18next';

/**
 * Overview over the current talent overview.
 */
export function TalentOverview() {
    const {t} = useTranslation();
    const {characterForm} = useContext(PnPCharacterContext);
    const {activeUniverse} = useUniverseContext();

    const sum = Object.values(characterForm.getValues().talents)
        .map(talent => talent.rawValue).reduce((a, b) => a + b, 0);

    const max = useCalculateTalentPoints(activeUniverse?.id, {level: characterForm.getValues().level.level ?? 1}, {
        query: {enabled: Boolean(activeUniverse?.id)}
    }).data?.data ?? 1;

    return <Stack justify="center" h="100%">
        <Text size="xl">
            {t('character:talentPoints') + ': ' + sum + '/' + max}
        </Text>
    </Stack>;
}