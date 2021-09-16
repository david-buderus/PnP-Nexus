package de.pnp.manager.model.manager;

import de.pnp.manager.model.Battle;
import de.pnp.manager.model.character.IPnPCharacter;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CharacterHandler {

    private static int ID_COUNTER = 0;

    protected static synchronized String getNextCharacterID(){
        return "character-" + DigestUtils.sha256Hex(String.valueOf(++ID_COUNTER));
    }

    // maps sessionId to character
    public Map<String, ArrayList<IPnPCharacter>> sessionCharacterMap;

    public CharacterHandler() {
        this.sessionCharacterMap = new HashMap<>();
    }

    /**
     * Creates a character which can be moved between multiple
     * battles. This character will be persisted between session
     * restarts.
     *
     * @param sessionID the id of the session
     * @param battle the first battle (can be null)
     * @param producer the function to create the character
     * @return the resulting character
     */
    public <C extends IPnPCharacter> C createCharacter(String sessionID, Battle battle, PnPCharacterProducer<C> producer) {
        C character = producer.create(getNextCharacterID(), battle);
        this.sessionCharacterMap.computeIfAbsent(sessionID, k -> new ArrayList<>()).add(character);

        return character;
    }

    /**
     * Creates a character that will be used in one
     * battle. This character won't get persisted.
     *
     * @param battle the battle of the user
     * @param producer the function to create the character
     * @return the resulting one time use character
     */
    public <C extends IPnPCharacter> C createOneTimeCharacter(@NotNull Battle battle, PnPCharacterProducer<C> producer) {
        return producer.create(getNextCharacterID(), battle);
    }

    public void deleteCharacter(String sessionID, String characterID) {
        Collection<IPnPCharacter> characters = this.sessionCharacterMap.get(sessionID);

        if (characters != null) {
            characters.removeIf(battle -> battle.getCharacterID().equals(characterID));
        }
    }

    public void deleteCharacter(String characterID) {
        for (ArrayList<IPnPCharacter> characters : this.sessionCharacterMap.values()) {
            characters.removeIf(battle -> battle.getCharacterID().equals(characterID));
        }
    }
}
