package de.pnp.manager.server.database;

import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.IUniquelyNamedDataObject;
import de.pnp.manager.server.component.ItemBuilder;
import de.pnp.manager.server.component.ItemBuilder.ItemBuilderFactory;
import de.pnp.manager.server.database.interfaces.IUniquelyNamedRepository;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A base class for {@link RepositoryBase repository} tests.
 * <p>
 * It tests the basic functionalities like insert, get, remove, update.
 */
public abstract class RepositoryTestBase<E extends DatabaseObject, Repo extends RepositoryBase<E>> extends
    UniverseTestBase {

  /**
   * A factory to create {@link ItemBuilder}.
   */
  @Autowired
  protected ItemBuilderFactory itemBuilder;

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
      IUniquelyNamedDataObject namedChange = (IUniquelyNamedDataObject) persistedChange;
      Optional<?> optional = uniqueNameRepository.get(universe, namedChange.getName());
      assertThat(optional).isNotEmpty();
      assertThat(optional.get()).isEqualTo(change);
    }
  }

  /**
   * A helper method to tests links between to repositories.
   * <p>
   * The originalLink needs to be persisted.
   */
  @SuppressWarnings("unchecked")
  protected <A extends E, Link extends DatabaseObject> void testRepositoryLink(
      Function<A, Link> linkGetter,
      RepositoryBase<Link> linkRepository, A object, Link originalLink,
      Link changedLink) {
    assertThat(linkRepository.get(universe, originalLink.getId())).as(
        "The original link has to be already persisted.").isNotEmpty();

    E persistedObject = repository.insert(universe, object);
    assertThat(repository.get(universe, persistedObject.getId())).contains(object);

    linkRepository.update(universe, originalLink.getId(), changedLink);
    assertThat(linkRepository.get(universe, originalLink.getId())).contains(changedLink);

    Optional<E> optional = repository.get(universe, persistedObject.getId());
    assertThat(optional).isNotEmpty();
    assertThat(linkGetter.apply((A) optional.get())).isEqualTo(changedLink);
  }

  /**
   * A helper method to test links between two repositories.
   * <p>
   * The originalLinks need to be persisted.
   */
  @SuppressWarnings("unchecked")
  protected <A extends E, Link extends DatabaseObject> void testRepositoryCollectionLink(
      Function<A, Collection<Link>> linkGetter,
      RepositoryBase<Link> linkRepository, A object, Collection<Link> originalLinks,
      Map<Link, Link> changedLinks) {
    assertThat(linkRepository.getAll(universe)).as(
        "The original links have to be already persisted.").containsAll(originalLinks);

    E persistedObject = repository.insert(universe, object);
    assertThat(repository.get(universe, persistedObject.getId())).contains(object);

    for (Link originalLink : originalLinks) {
      Link changedLink = changedLinks.get(originalLink);

      if (changedLink != null) {
        linkRepository.update(universe, originalLink.getId(), changedLink);
        assertThat(linkRepository.get(universe, originalLink.getId())).contains(changedLink);
      } else {
        linkRepository.remove(universe, originalLink.getId());
        assertThat(linkRepository.get(universe, originalLink.getId())).isEmpty();
      }
    }

    Optional<E> optional = repository.get(universe, persistedObject.getId());
    assertThat(optional).isNotEmpty();
    assertThat(linkGetter.apply((A) optional.get())).containsExactlyInAnyOrderElementsOf(
        changedLinks.values());
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
