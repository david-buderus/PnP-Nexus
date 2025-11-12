import React from 'react';
import {PnPCharacterDto} from '../../api';

export type PnPCharacterContextContent = {
    character: PnPCharacterDto;
}

export const PnPCharacterContext = React.createContext<PnPCharacterContextContent>({
    character: null
});
