import {useNavigate, useParams} from 'react-router-dom';
import {useUniverseContext} from '../../../components/PageBase';
import React from 'react';
import {CharacterEdit} from '../../../components/character/CharacterEdit';
import {useGetCharacter} from '../../../api/pn-p-character-service/pn-p-character-service';


/** Page to show the character sheet editor */
export function CharacterEditor() {
    const {character} = useParams();
    const {activeUniverse} = useUniverseContext();
    const navigate = useNavigate();

    const initialCharacter = useGetCharacter(activeUniverse.id, character, {
        query: {enabled: Boolean(character)}
    }).data?.data ?? null;

    return <CharacterEdit
        character={initialCharacter}
        onCancel={() => navigate('/characters?universe=' + activeUniverse.id)}
        onDelete={() => navigate('/characters?universe=' + activeUniverse.id)}
    />;
}