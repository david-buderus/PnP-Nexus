package de.pnp.manager.component.character;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * The description of a {@link PnPCharacter}.
 */
public class CharacterDescription {

    @NotBlank
    private final String name;

    @Positive
    private final int age;

    @NotNull
    private final String profession;

    @NotNull
    private final String gender;

    @NotNull
    private final String backstory;

    public CharacterDescription(String name, int age, String profession, String gender, String backstory) {
        this.name = name;
        this.age = age;
        this.profession = profession;
        this.gender = gender;
        this.backstory = backstory;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getProfession() {
        return profession;
    }

    public String getGender() {
        return gender;
    }

    public String getBackstory() {
        return backstory;
    }
}
