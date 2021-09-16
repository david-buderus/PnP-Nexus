package de.pnp.manager.model.character;

import de.pnp.manager.model.ICurrency;
import de.pnp.manager.network.serializer.DeserializerIdentifier;

public interface IPlayerCharacter extends IPnPCharacter {

    /**
     * The race of the player
     */
    String getRace();

    /**
     * The age of the player
     */
    String getAge();

    /**
     * The current experience of the player
     */
    int getExperience();

    /**
     * The profession of the player
     */
    String getProfession();

    /**
     * The current currency the character owns
     */
    ICurrency getCurrency();

    /**
     * The history of the player
     */
    @DeserializerIdentifier
    String getHistory();

}
