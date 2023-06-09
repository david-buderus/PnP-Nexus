package de.pnp.manager.server.database;

import com.mongodb.client.result.DeleteResult;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public abstract class RepositoryBase<E> {

  @Autowired
  private MongoConfig config;

  protected String collectionName;
  protected Class<E> clazz;

  public RepositoryBase(Class<E> clazz, String collectionName) {
    this.clazz = clazz;
    this.collectionName = collectionName;
  }

  public Optional<E> get(String universe, Object id) {
    return Optional.ofNullable(getTemplate(universe).findById(id, clazz, collectionName));
  }

  public Collection<E> getAll(String universe) {
    return getTemplate(universe).findAll(clazz, collectionName);
  }

  public void insert(String universe, E object) {
    getTemplate(universe).insert(object, collectionName);
  }

  public void insertAll(String universe, Collection<E> collection) {
    getTemplate(universe).insert(collection, collectionName);
  }

  public boolean remove(String universe, Object id) {
    DeleteResult result = getTemplate(universe).remove(Query.query(Criteria.where("_id").is(id)),
        collectionName);
    return result.wasAcknowledged();
  }

  public boolean removeAll(String universe, Collection<?> ids) {
    List<Object> deletedIds = getTemplate(universe).findAllAndRemove(
        Query.query(Criteria.where("_id").in(ids)), collectionName);
    return deletedIds.size() == ids.size();
  }

  protected MongoTemplate getTemplate(String database) {
    return config.mongoTemplate(database);
  }
}
