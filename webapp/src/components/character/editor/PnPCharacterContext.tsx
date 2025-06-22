import React from "react";
import {PnPCharacterDto} from "../../../api";

export interface PnPCharacterContextContent {
    character: PnPCharacterDto;
}

export const PnPCharacterContext = React.createContext<PnPCharacterContextContent>({
    character: null
});
