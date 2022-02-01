package de.pnp.manager.manager.interfaces;

import de.pnp.manager.model.character.IPnPCharacter;
import de.pnp.manager.model.interfaces.IBattle;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ICharacterHandler<PnPCharacterImpl extends IPnPCharacter> {

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
    <C extends PnPCharacterImpl> C createCharacter(String sessionID, IBattle battle, PnPCharacterProducer<C> producer);

    /**
     * Creates a character that will be used in one
     * battle. This character won't get persisted.
     *
     * @param battle the battle of the user
     * @param producer the function to create the character
     * @return the resulting one time use character
     */
    <C extends PnPCharacterImpl> C createOneTimeCharacter(@NotNull IBattle battle, PnPCharacterProducer<C> producer);

    void deleteCharacter(String sessionID, String characterID);

    void deleteCharacter(String characterID);

    List<PnPCharacterImpl> getCharacters(String sessionID);

    default PnPCharacterImpl getCharacter(String sessionID, String characterID) {
        return getCharacters(sessionID).stream().filter(c -> c.getCharacterID().equals(characterID)).findFirst().orElse(null);
    }
}
