package de.pnp.manager.server.database;

import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.IUniquelyNamedDataObject;
import de.pnp.manager.server.database.interfaces.IUniquelyNamedRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/**
 * A base class for {@link RepositoryBase repository} tests.
 * <p>
 * It tests the basic functionalities like insert, get, remove, update.
 */
public abstract class RepositoryTestBase<E extends DatabaseObject, Repo extends RepositoryBase<E>> extends
    UniverseTestBase {

  /**
   * The main repository for the test.
   */
  protected Repo repository;

  public RepositoryTestBase(Repo repository) {
    this.repository = repository;
  }

  @Test
  void testInsert() {
    E object = createObject();
    E persistedObject = repository.insert(universe, object);

    assertThat(persistedObject).isEqualTo(object);
    assertThat(repository.getAll(universe)).contains(object);
    assertThat(repository.get(universe, persistedObject.getId())).contains(object);

    if (repository instanceof IUniquelyNamedRepository<?> uniqueNameRepository) {
      IUniquelyNamedDataObject namedObject = (IUniquelyNamedDataObject) object;
      Optional<?> optional = uniqueNameRepository.get(universe, namedObject.getName());
      assertThat(optional).isNotEmpty();
      assertThat(optional.get()).isEqualTo(object);
    }
  }

  @Test
  void testRemove() {
    E object = repository.insert(universe, createObject());
    assertThat(repository.getAll(universe)).contains(object);

    assertThat(repository.remove(universe, object.getId())).isTrue();
    assertThat(repository.getAll(universe)).isEmpty();
    assertThat(repository.get(universe, object.getId())).isEmpty();

    if (repository instanceof IUniquelyNamedRepository<?> uniqueNameRepository) {
      IUniquelyNamedDataObject namedObject = (IUniquelyNamedDataObject) object;
      assertThat(uniqueNameRepository.get(universe, namedObject.getName())).isEmpty();
    }
  }

  @Test
  void testUpdate() {
    E object = repository.insert(universe, createObject());
    assertThat(repository.getAll(universe)).contains(object);

    E change = createSlightlyChangeObject();
    E persistedChange = repository.update(universe, object.getId(), change);
    assertThat(persistedChange).isEqualTo(change);
    assertThat(persistedChange.getId()).isEqualTo(object.getId());
    assertThat(repository.getAll(universe)).contains(change);
    assertThat(repository.get(universe, persistedChange.getId())).contains(change);

    if (repository instanceof IUniquelyNamedRepository<?> uniqueNameRepository) {
      IUniquelyNamedDataObject namedObject = (IUniquelyNamedDataObject) object;
      Optional<?> optional = uniqueNameRepository.get(universe, namedObject.getName());
      assertThat(optional).isNotEmpty();
      assertThat(optional.get()).isEqualTo(change);
    }
  }

  /**
   * A supplier for an {@link DatabaseObject} to use in the tests.
   */
  protected abstract E createObject();

  /**
   * A supplier for an {@link DatabaseObject} that is a slight modification of
   * {@link #createObject()} to use in the tests.
   * <p>
   * This method gets always called after {@link #createObject()}. That means you can reuse objects
   * that were persisted in other repositories in the other method.
   */
  protected abstract E createSlightlyChangeObject();
}