package de.pnp.manager.component.character.traits;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

/**
 * A simple trait.
 */
public class SimpleCharacterTrait implements ICharacterTrait {

    @NotBlank
    private final String description;

    @JsonCreator
    public SimpleCharacterTrait(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleCharacterTrait that = (SimpleCharacterTrait) o;
        return Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription());
    }
}
