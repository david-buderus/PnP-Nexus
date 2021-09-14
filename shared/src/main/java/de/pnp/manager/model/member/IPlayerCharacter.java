package de.pnp.manager.model.member;

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
     * The history of the player
     */
    @DeserializerIdentifier
    String getHistory();

}
