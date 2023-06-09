package de.pnp.manager.server.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public abstract class RepositoryBase<E> {

  @Autowired
  private MongoConfig config;

  protected String collection;
  protected Class<E> clazz;

  public RepositoryBase(Class<E> clazz, String collection) {
    this.clazz = clazz;
    this.collection = collection;
  }

  public void insert(String universe, E object) {
    getTemplate(universe).insert(object, collection);
  }

  public E get(String universe, Object id) {
    return getTemplate(universe).findById(id, clazz, collection);
  }

  protected MongoTemplate getTemplate(String database) {
    return config.mongoTemplate(database);
  }
}
