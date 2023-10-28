package de.pnp.manager.component.character;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.attributes.EPrimaryAttribute;
import de.pnp.manager.server.database.TalentRepository;
import java.util.List;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A talent of a {@link Character}.
 */
@Document(TalentRepository.REPOSITORY_NAME)
public class Talent extends DatabaseObject {

    /**
     * The human-readable name of this talent.
     */
    private final String name;

    /**
     * The group of this category.
     * <p>
     * Examples: magic, social, fighting, ...
     */
    private final String group;

    /**
     * The first {@link EPrimaryAttribute} for a talent check.
     */
    private final EPrimaryAttribute firstAttribute;

    /**
     * The second {@link EPrimaryAttribute} for a talent check.
     */
    private final EPrimaryAttribute secondAttribute;

    /**
     * The third {@link EPrimaryAttribute} for a talent check.
     */
    private final EPrimaryAttribute thirdAttribute;


    public Talent(ObjectId id, String name, String group, EPrimaryAttribute firstAttribute,
        EPrimaryAttribute secondAttribute, EPrimaryAttribute thirdAttribute) {
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

    public EPrimaryAttribute getFirstAttribute() {
        return firstAttribute;
    }

    public EPrimaryAttribute getSecondAttribute() {
        return secondAttribute;
    }

    public EPrimaryAttribute getThirdAttribute() {
        return thirdAttribute;
    }

    /**
     * The {@link EPrimaryAttribute primary attributes} for a talent check in correct order.
     */
    @JsonIgnore
    public List<EPrimaryAttribute> getAttributes() {
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
        return getName().equals(talent.getName()) && Objects.equals(getGroup(),
            talent.getGroup()) && getFirstAttribute() == talent.getFirstAttribute()
            && getSecondAttribute() == talent.getSecondAttribute()
            && getThirdAttribute() == talent.getThirdAttribute();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getGroup(), getFirstAttribute(), getSecondAttribute(),
            getThirdAttribute());
    }
}
