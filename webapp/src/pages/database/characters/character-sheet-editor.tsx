import {useParams} from 'react-router-dom';
import {useEffect, useState} from 'react';
import {PnPCharacterSheet, PnPCharacterSheetServiceApi} from '../../../api';
import {API_CONFIGURATION} from '../../../components/Constants';
import {useUniverseContext} from '../../../components/PageBase';
import {PnPCharacterSheetEditor} from '../../../components/character/editor/PnPCharacterSheetEditor';

const SHEET_API = new PnPCharacterSheetServiceApi(API_CONFIGURATION);

/** Page to show the character sheet editor */
export function CharacterSheetEditor() {
    const {sheet} = useParams();
    const {activeUniverse} = useUniverseContext();

    const [initialSheet, setInitialSheet] = useState<PnPCharacterSheet>(null);

    useEffect(() => {
        if (!sheet) {
            return;
        }
        SHEET_API.getPnPCharacterSheet(activeUniverse.name, sheet).then(response => {
            setInitialSheet(response.data);
        });
    }, [sheet]);

    return <PnPCharacterSheetEditor
        initialSheet={initialSheet}
    />;
}