package de.pnp.manager.component.character;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.server.database.TalentRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A talent of a {@link Character}.
 */
@Document(TalentRepository.REPOSITORY_NAME)
public class Talent extends DatabaseObject {

    /**
     * The human-readable name of this talent.
     */
    @NotBlank
    private final String name;

    /**
     * The group of this category.
     * <p>
     * Examples: magic, social, fighting, ...
     */
    @NotBlank
    private final String group;

    /**
     * The first {@link PrimaryAttribute} for a talent check.
     */
    @DBRef
    @NotNull
    private final PrimaryAttribute firstAttribute;

    /**
     * The second {@link PrimaryAttribute} for a talent check.
     */
    @DBRef
    @NotNull
    private final PrimaryAttribute secondAttribute;

    /**
     * The third {@link PrimaryAttribute} for a talent check.
     */
    @DBRef
    @NotNull
    private final PrimaryAttribute thirdAttribute;


    public Talent(ObjectId id, String name, String group, PrimaryAttribute firstAttribute,
        PrimaryAttribute secondAttribute, PrimaryAttribute thirdAttribute) {
        super(id);
        this.name = Objects.requireNonNull(name);
        this.group = group;
        this.firstAttribute = firstAttribute;
        this.secondAttribute = secondAttribute;
        this.thirdAttribute = thirdAttribute;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public PrimaryAttribute getFirstAttribute() {
        return firstAttribute;
    }

    public PrimaryAttribute getSecondAttribute() {
        return secondAttribute;
    }

    public PrimaryAttribute getThirdAttribute() {
        return thirdAttribute;
    }

    /**
     * The {@link PrimaryAttribute primary attributes} for a talent check in correct order.
     */
    @JsonIgnore
    public List<PrimaryAttribute> getAttributes() {
        return List.of(getFirstAttribute(), getSecondAttribute(), getThirdAttribute());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Talent talent = (Talent) o;
        return Objects.equals(getName(), talent.getName()) && Objects.equals(getGroup(),
            talent.getGroup()) && Objects.equals(getFirstAttribute(), talent.getFirstAttribute())
            && Objects.equals(getSecondAttribute(), talent.getSecondAttribute()) && Objects.equals(
            getThirdAttribute(), talent.getThirdAttribute());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getGroup(), getFirstAttribute(), getSecondAttribute(), getThirdAttribute());
    }
}
