import React from 'react';

/** Interface for the context */
export type PnPCharacterSheetContextContent = {
    allowEdit: boolean;
}

/** Context in sheet editor */
export const PnPCharacterSheetContext = React.createContext<PnPCharacterSheetContextContent>(null);