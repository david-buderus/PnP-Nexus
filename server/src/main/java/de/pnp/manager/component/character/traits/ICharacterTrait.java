package de.pnp.manager.component.character.traits;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.pnp.manager.component.character.traits.StatTrait.PrimaryStatTrait;
import de.pnp.manager.component.character.traits.StatTrait.SecondaryStatTrait;

/**
 * Can influence a character's stats, rolls and others.
 */
@JsonSubTypes({
    @JsonSubTypes.Type(value = SimpleCharacterTrait.class, name = "SimpleCharacterTrait"),
    @JsonSubTypes.Type(value = TalentCharacterTrait.class, name = "TalentCharacterTrait"),
    @JsonSubTypes.Type(value = PrimaryStatTrait.class, name = "PrimaryStatTrait"),
    @JsonSubTypes.Type(value = SecondaryStatTrait.class, name = "SecondaryStatTrait")
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public interface ICharacterTrait {

    /**
     * Returns a human-readable description of the trait.
     */
    String getDescription();
}
