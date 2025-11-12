import React from 'react';

/** Interface for the context */
export type PnPCharacterSheetContextContent = {
    /** The currently selected page */
    selectedPage: number;
}

/** Context in sheet editor */
export const PnPCharacterSheetContext = React.createContext<PnPCharacterSheetContextContent>(null);