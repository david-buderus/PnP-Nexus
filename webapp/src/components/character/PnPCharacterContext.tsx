import React from 'react';
import {PnPCharacterDTO} from '../../api';
import {UseFormReturnType} from '@mantine/form';

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
