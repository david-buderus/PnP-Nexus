package de.pnp.manager.server.database;

import de.pnp.manager.Tag;
import de.pnp.manager.exception.UniverseNotFoundException;
import de.pnp.manager.server.database.universe.UniverseRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Repository for {@link Tag tags}.
 */
@Component
public class TagRepository {

    /**
     * Name of the repository
     */
    public static final String REPOSITORY_NAME = "tag";

    @Autowired
    private MongoConfig config;

    @Autowired
    private UniverseRepository universeRepository;


    /**
     * Returns all objects in this repository.
     */
    public Collection<Tag> getAll(ObjectId universe) {
        return getTemplate(universe).findAll(Tag.class, REPOSITORY_NAME);
    }

    /**
     * Saves the given objects in the repository.
     */
    public Collection<Tag> saveAll(ObjectId universe, Collection<Tag> collection) {
        Collection<Tag> persistedObjects = new ArrayList<>();
        for (Tag tag : collection) {
            getTemplate(universe).save(tag, REPOSITORY_NAME);
        }
        return persistedObjects;
    }

    /**
     * Removes the tags.
     */
    public boolean removeAll(ObjectId universe, Collection<Tag> tags) {
        Set<String> names = tags.stream().map(Tag::name).collect(Collectors.toSet());

        List<Object> deletedIds = getTemplate(universe).findAllAndRemove(
                Query.query(Criteria.where("_id").in(names)), REPOSITORY_NAME);
        return deletedIds.size() == names.size();
    }

    /**
     * Returns the {@link MongoTemplate} to manipulate the database of the given universe.
     */
    protected MongoTemplate getTemplate(ObjectId universe) {
        if (!universeRepository.exists(universe)) {
            throw new UniverseNotFoundException(universe);
        }
        return config.universeMongoTemplate(universe);
    }
}
