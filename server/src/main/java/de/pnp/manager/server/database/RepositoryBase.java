package de.pnp.manager.server.database;

import static org.springframework.data.mongodb.core.mapping.BasicMongoPersistentProperty.ID_FIELD_NAME;

import com.google.common.base.Preconditions;
import com.mongodb.client.result.DeleteResult;
import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.server.exception.AlreadyPersistedException;
import de.pnp.manager.server.exception.UniverseNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Base class for repositories.
 */
public abstract class RepositoryBase<E extends DatabaseObject> {

  @Autowired
  private MongoConfig config;

  @Autowired
  private UniverseRepository universeRepository;

  /**
   * The name of the collection.
   */
  protected String collectionName;

  /**
   * The class used in this repository.
   */
  protected Class<E> clazz;

  public RepositoryBase(Class<E> clazz, String collectionName) {
    this.clazz = clazz;
    this.collectionName = collectionName;
  }

  /**
   * Returns the object with the given id.
   */
  public Optional<E> get(String universe, ObjectId id) {
    return Optional.ofNullable(getTemplate(universe).findById(id, clazz, collectionName));
  }

  /**
   * Returns the object which matches the {@link Query}.
   */
  public Optional<E> get(String universe, Query query) {
    return Optional.ofNullable(getTemplate(universe).findOne(query, clazz, collectionName));
  }

  /**
   * Returns all objects in this repository.
   */
  public Collection<E> getAll(String universe) {
    return getTemplate(universe).findAll(clazz, collectionName);
  }

  /**
   * Returns all objects in this repository with the given ids.
   */
  public Collection<E> getAll(String universe, Collection<ObjectId> ids) {
    return getAll(universe, Query.query(Criteria.where(ID_FIELD_NAME).in(ids)));
  }

  /**
   * Returns all objects in this repository which match the given query.
   */
  public Collection<E> getAll(String universe, Query query) {
    return getTemplate(universe).find(query, clazz, collectionName);
  }

  /**
   * Inserts the given object into repository. Returns the inserted object including its id.
   *
   * @throws AlreadyPersistedException if the given object
   *                                   {@link DatabaseObject#isPersisted() is already persisted}.
   */
  public E insert(String universe, E object) {
    if (object.isPersisted()) {
      throw new AlreadyPersistedException(object);
    }
    onPrePersistent(universe, object);
    E persistedObject = getTemplate(universe).insert(object, collectionName);
    onPostPersistent(universe, persistedObject);
    return persistedObject;
  }

  /**
   * Inserts the given objects into repository. Returns the inserted objects including their ids.
   *
   * @throws AlreadyPersistedException if at least one object
   *                                   {@link DatabaseObject#isPersisted() is already persisted}.
   */
  public Collection<E> insertAll(String universe, Collection<E> collection) {
    if (collection.stream().anyMatch(DatabaseObject::isPersisted)) {
      throw new AlreadyPersistedException(
          collection.stream().filter(DatabaseObject::isPersisted).toList());
    }
    collection.forEach(object1 -> onPrePersistent(universe, object1));
    Collection<E> persistedObjects = getTemplate(universe).insert(collection, collectionName);
    persistedObjects.forEach(object -> onPostPersistent(universe, object));
    return persistedObjects;
  }

  /**
   * Updates the object in the database and returns the new object stored under the given id.
   * <p>
   * The object has to have an id.
   */
  public E update(String universe, E object) {
    return update(universe, object.getId(), object);
  }

  /**
   * Updates the object in the database which is stored under the given id.
   */
  public E update(String universe, ObjectId id, E object) {
    Preconditions.checkNotNull(id);
    onPrePersistent(universe, object);
    E persistedObject = getTemplate(universe).findAndReplace(
        Query.query(Criteria.where("_id").is(id)),
        object, FindAndReplaceOptions.options().returnNew(), collectionName);
    onPostPersistent(universe, object);
    return persistedObject;
  }

  /**
   * Removes the object stored under the given id.
   */
  public boolean remove(String universe, ObjectId id) {
    Preconditions.checkNotNull(id);
    DeleteResult result = getTemplate(universe).remove(Query.query(Criteria.where("_id").is(id)),
        collectionName);
    return result.wasAcknowledged();
  }

  /**
   * Removes the objects stored under the given ids.
   */
  public boolean removeAll(String universe, Collection<?> ids) {
    List<Object> deletedIds = getTemplate(universe).findAllAndRemove(
        Query.query(Criteria.where("_id").in(ids)), collectionName);
    return deletedIds.size() == ids.size();
  }

  /**
   * Returns the {@link MongoTemplate} to manipulate the database of the given universe.
   */
  protected MongoTemplate getTemplate(String universe) {
    if (!universeRepository.exists(universe)) {
      throw new UniverseNotFoundException(universe);
    }
    return config.mongoTemplate(universe);
  }

  /**
   * Possibility to add validations or something similar before the repository tries to persist the
   * object.
   */
  protected void onPrePersistent(String universe, E object) {
    // no op
  }

  /**
   * Possibility to add hooks or something similar after the repository has successfully persisted
   * the object.
   * <p>
   * This means the object will always have an {@link DatabaseObject#getId() id}.
   */
  protected void onPostPersistent(String universe, E object) {
    // no op
  }
}
