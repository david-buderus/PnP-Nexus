package de.pnp.manager.component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public abstract class DatabaseObject {

  @Id
  private final ObjectId id;

  protected DatabaseObject(ObjectId id) {
    this.id = id;
  }

  public ObjectId getId() {
    return id;
  }

  @JsonIgnore
  public boolean isPersisted() {
    return id != null;
  }
}
