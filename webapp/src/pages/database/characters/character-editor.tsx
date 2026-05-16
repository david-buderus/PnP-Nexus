import {useNavigate, useParams} from 'react-router-dom';
import {useUniverseContext} from '../../../components/PageBase';
import React, {useEffect, useState} from 'react';
import {PnPCharacterDTO, PnPCharacterServiceApi} from '../../../api';
import {API_CONFIGURATION} from '../../../components/Constants';
import {CharacterEdit} from '../../../components/character/CharacterEdit';


const CHARACTER_API = new PnPCharacterServiceApi(API_CONFIGURATION);

/** Page to show the character sheet editor */
export function CharacterEditor() {
    const {character} = useParams();
    const {activeUniverse} = useUniverseContext();
    const navigate = useNavigate();

    const [initialCharacter, setInitialCharacter] = useState<PnPCharacterDTO>(null);

    useEffect(() => {
        if (!character) {
            return;
        }
        CHARACTER_API.getCharacter(activeUniverse.id, character).then(response => {
            setInitialCharacter(response.data);
        });
    }, [character]);

    return <CharacterEdit
        character={initialCharacter}
        onCancel={() => navigate('/characters?universe=' + activeUniverse.id)}
        onDelete={() => navigate('/characters?universe=' + activeUniverse.id)}
    />;
}