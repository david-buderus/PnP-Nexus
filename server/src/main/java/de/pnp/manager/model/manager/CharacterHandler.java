package de.pnp.manager.model.manager;

import de.pnp.manager.model.Battle;
import de.pnp.manager.model.character.PnPCharacter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharacterHandler {

    private static int ID_COUNTER = 0;

    protected static synchronized String getNextCharacterID(){
        return "character-" + DigestUtils.sha256Hex(String.valueOf(++ID_COUNTER));
    }

    // maps sessionId to character
    protected Map<String, ObservableList<PnPCharacter>> sessionCharacterMap;

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
    public <C extends PnPCharacter> C createCharacter(String sessionID, Battle battle, PnPCharacterProducer<C> producer) {
        C character = producer.create(getNextCharacterID(), battle);
        this.sessionCharacterMap.computeIfAbsent(sessionID, k -> FXCollections.observableArrayList()).add(character);

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
    public <C extends PnPCharacter> C createOneTimeCharacter(@NotNull Battle battle, PnPCharacterProducer<C> producer) {
        return producer.create(getNextCharacterID(), battle);
    }

    public void deleteCharacter(String sessionID, String characterID) {
        Collection<PnPCharacter> characters = this.sessionCharacterMap.get(sessionID);

        if (characters != null) {
            characters.removeIf(battle -> battle.getCharacterID().equals(characterID));
        }
    }

    public void deleteCharacter(String characterID) {
        for (List<PnPCharacter> characters : this.sessionCharacterMap.values()) {
            characters.removeIf(battle -> battle.getCharacterID().equals(characterID));
        }
    }

    public ObservableList<PnPCharacter> getCharacters(String sessionID) {
        return sessionCharacterMap.computeIfAbsent(sessionID, id -> FXCollections.observableArrayList());
    }
}
