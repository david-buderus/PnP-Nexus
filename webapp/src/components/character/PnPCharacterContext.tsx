import React, {useMemo} from 'react';
import {PnPCharacterDTO} from '../../api';
import {UseFormReturnType} from '@mantine/form';
import {useUniverseContext} from '../PageBase';
import {fetchAllPrimaryAttributes} from '../Database';

/** Content of the context */
export type PnPCharacterContextContent = {
    characterForm: UseFormReturnType<PnPCharacterDTO>;
    allowEdit: boolean;
}

/** Context for anything where a {@link PnPCharacterDto character} is needed */
export const PnPCharacterContext = React.createContext<PnPCharacterContextContent>({
    characterForm: null,
    allowEdit: false
});


export function useEmptyCharacter(): PnPCharacterDTO {
    const {characterSettings} = useUniverseContext();
    const [primaryAttributes] = fetchAllPrimaryAttributes();

    return useMemo(() => ({
        description: {
            affiliations: '',
            appearance: '',
            backstory: '',
            deficits: '',
            gender: '',
            goals: '',
            name: '',
            personality: '',
            profession: ''
        },
        level: {},
        origin: undefined,
        advantageTraits: [],
        disadvantageTraits: [],
        customFields: {},
        equipment: {
            armor: {},
            jewellery: {},
            shieldEquipment: undefined,
            weaponEquipments: []
        },
        inventory: {
            coin: 0,
            inventory: {
                items: [],
                maxSize: 100
            },
        },
        spells: [],
        stats: {
            primaryStats: Object.fromEntries(primaryAttributes.map(u => [u.id, {
                flatModifier: 0,
                rawValue: characterSettings?.minPrimaryAttributeValue ?? 0,
                totalValue: characterSettings?.minPrimaryAttributeValue ?? 0
            }])),
            secondaryStats: {}
        },
        talents: {},
    }), [characterSettings, primaryAttributes]);
}