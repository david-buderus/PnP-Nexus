package de.pnp.manager.component.item;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.server.database.MaterialRepository;
import java.util.Collection;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A material in the universe.
 */
@Document(MaterialRepository.REPOSITORY_NAME)
public class Material extends DatabaseObject {

  /**
   * The human-readable name of this material.
   */
  @Indexed(unique = true)
  protected final String name;

  /**
   * Items which represent this material.
   * <p>
   * An example would be: The material iron get represented by the item iron ingot.
   */
  @DBRef
  protected final Collection<Item> items;

  public Material(ObjectId id, String name, Collection<Item> items) {
    super(id);
    this.name = name;
    this.items = items;
  }

  public String getName() {
    return name;
  }

  public Collection<Item> getItems() {
    return items;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Material material = (Material) o;
    return name.equals(material.name) && items.equals(material.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, items);
  }
}
