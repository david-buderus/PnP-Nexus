import React from 'react';
import {PnPCharacterDto} from '../../api';

/** Content of the context */
export type PnPCharacterContextContent = {
    character: PnPCharacterDto;
}

/** Context for anything where a {@link PnPCharacterDto character} is needed */
export const PnPCharacterContext = React.createContext<PnPCharacterContextContent>({
    character: null
});
