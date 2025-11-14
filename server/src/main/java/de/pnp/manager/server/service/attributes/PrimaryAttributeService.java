package de.pnp.manager.server.service.attributes;

import com.google.common.collect.Sets;
import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.security.UniverseWrite;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import de.pnp.manager.server.service.RepositoryServiceBase;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service to access {@link PrimaryAttributeRepository}.
 */
@RestController
@RequestMapping("{universe}/primary-attributes")
public class PrimaryAttributeService extends RepositoryServiceBase<PrimaryAttribute, PrimaryAttributeRepository> {

    protected PrimaryAttributeService(@Autowired PrimaryAttributeRepository repository) {
        super(repository);
    }

    @PutMapping
    @UniverseWrite
    @Operation(summary = "Sets all primary attributes of the universe", operationId = "setAllPrimaryAttributes")
    public void setAll(@PathVariable ObjectId universe, @RequestBody List<@Valid PrimaryAttribute> attributes) {
        Set<ObjectId> newIds = attributes.stream().map(PrimaryAttribute::getId).filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<ObjectId> toRemove = Sets.difference(
                repository.getAll(universe).stream().map(PrimaryAttribute::getId).collect(Collectors.toSet()), newIds);
        List<PrimaryAttribute> toUpdate = attributes.stream().filter(DatabaseObject::isPersisted)
                .toList();
        List<PrimaryAttribute> toInsert = attributes.stream().filter(attribute -> !attribute.isPersisted()).toList();

        repository.removeAll(universe, toRemove);
        toUpdate.forEach(attribute -> repository.update(universe, attribute));
        repository.insertAll(universe, toInsert);
    }
}
