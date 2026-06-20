import React, {useContext} from 'react';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {Select, Stack, Text} from '@mantine/core';
import {useCalculateTalentPoints} from '../../../../../api/pn-p-character-service/pn-p-character-service';
import {useUniverseContext} from '../../../../PageBase';
import {useTranslation} from 'react-i18next';
import {PageElementSettings} from '../PageElementSettings';

/**
 * Overview over the current talent overview.
 */
export function TalentOverview({
    fontSize,
    setFontSize
}: {
    fontSize: string;
    setFontSize: (fontSize: string) => void;
}) {
    const {t} = useTranslation();
    const {characterForm} = useContext(PnPCharacterContext);
    const {activeUniverse} = useUniverseContext();

    const sum = Object.values(characterForm.getValues().talents)
        .map(talent => talent.rawValue).reduce((a, b) => a + b, 0);

    const max = useCalculateTalentPoints(activeUniverse?.id, {level: characterForm.getValues().level.level ?? 1}, {
        query: {enabled: Boolean(activeUniverse?.id)}
    }).data?.data ?? 1;

    return <Stack justify="center" h="100%">
        <Text size={fontSize}>
            {t('character:talentPoints') + ': '}
            <Text span c={sum > max ? 'red' : undefined} inherit>{sum}</Text>
            {'/' + max}
        </Text>
        <PageElementSettings>
            <Stack>
                <Select
                    label={t('sheetEditor:fontSize')}
                    value={fontSize ?? 'md'}
                    onChange={e => setFontSize(e)}
                    data={['xs', 'sm', 'md', 'lg', 'xl']}
                />
            </Stack>
        </PageElementSettings>
    </Stack>;
}