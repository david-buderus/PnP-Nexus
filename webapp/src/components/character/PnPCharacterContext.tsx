import React from 'react';
import {PnPCharacterDto} from '../../api';
import {UseFormReturnType} from '@mantine/form';

/** Content of the context */
export type PnPCharacterContextContent = {
    characterForm: UseFormReturnType<PnPCharacterDto>;
    allowEdit: boolean;
}

/** Context for anything where a {@link PnPCharacterDto character} is needed */
export const PnPCharacterContext = React.createContext<PnPCharacterContextContent>({
    characterForm: null,
    allowEdit: false
});
