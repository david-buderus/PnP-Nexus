package de.pnp.manager.component.item;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.IUniquelyNamedDataObject;
import de.pnp.manager.server.database.TypeTranslationRepository;
import java.util.Collection;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A description that indicates that the {@link Item#getType() type} or
 * {@link Item#getSubtype() subtype} is a more specific variant of another type or subtype. It can
 * be the more specific variant of multiple other types.
 */
@Document(TypeTranslationRepository.REPOSITORY_NAME)
public class TypeTranslation extends DatabaseObject implements IUniquelyNamedDataObject {

  /**
   * The type of this translation.
   */
  @Indexed(unique = true)
  private final String name;

  /**
   * The broader variants of this type.
   */
  private final Collection<String> broaderVariants;

  public TypeTranslation(ObjectId id, String name,
      Collection<String> broaderVariants) {
    super(id);
    this.name = name;
    this.broaderVariants = broaderVariants;
  }

  @Override
  public String getName() {
    return name;
  }

  public Collection<String> getBroaderVariants() {
    return broaderVariants;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TypeTranslation that = (TypeTranslation) o;
    return Objects.equals(getName(), that.getName()) && Objects.equals(
        getBroaderVariants(), that.getBroaderVariants());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName(), getBroaderVariants());
  }
}
