package de.pnp.manager.server.service.attributes;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.google.common.collect.Sets;
import de.pnp.manager.component.attributes.SecondaryAttributeDTO;
import de.pnp.manager.security.UniverseRead;
import de.pnp.manager.security.UniverseWrite;
import de.pnp.manager.server.contoller.SecondaryAttributeDTOController;
import de.pnp.manager.server.database.attributes.SecondaryAttributeRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service to access {@link SecondaryAttributeRepository}.
 */
@RestController
@Validated
@RequestMapping("api/{universe}/simple-secondary-attributes")
public class SimpleSecondaryAttributeService {

    @Autowired
    private SecondaryAttributeDTOController attributeController;

    @Autowired
    private SecondaryAttributeRepository repository;

    @GetMapping
    @UniverseRead
    @Operation(summary = "Get all objects from the database", operationId = "getAllSimpleSecondaryAttributes")
    public Collection<SecondaryAttributeDTO> getAll(@PathVariable String universe,
        @RequestParam(required = false) List<ObjectId> ids) {
        return attributeController.getAll(universe, ids);
    }

    @PostMapping
    @UniverseWrite
    @Operation(summary = "Inserts the objects into the database", operationId = "insertAllSimpleSecondaryAttributes")
    public Collection<SecondaryAttributeDTO> insertAll(@PathVariable String universe,
        @RequestBody List<@Valid SecondaryAttributeDTO> objects) {
        return attributeController.insertAll(universe, objects);
    }

    @PutMapping("{id}")
    @UniverseWrite
    @Operation(summary = "Updates an object in the database", operationId = "updateSimpleSecondaryAttribute")
    public SecondaryAttributeDTO update(@PathVariable String universe, @PathVariable ObjectId id,
        @RequestBody @Valid SecondaryAttributeDTO object) {
        if (object.id() != null && !Objects.equals(id, object.id())) {
            throw new ResponseStatusException(BAD_REQUEST, "The id of the object does not match.");
        }
        return attributeController.update(universe, object);
    }

    @PutMapping
    @UniverseWrite
    @Operation(summary = "Sets all secondary attributes of the universe", operationId = "setAllSimpleSecondaryAttributes")
    public void setAll(@PathVariable String universe, @RequestBody List<@Valid SecondaryAttributeDTO> attributes) {
        Set<ObjectId> newIds = attributes.stream().map(SecondaryAttributeDTO::id).filter(Objects::nonNull)
            .collect(Collectors.toSet());

        Set<ObjectId> toRemove = Sets.difference(
            attributeController.getAll(universe).stream().map(SecondaryAttributeDTO::id).collect(Collectors.toSet()),
            newIds);
        List<SecondaryAttributeDTO> toUpdate = attributes.stream().filter(SecondaryAttributeDTO::isPersisted)
            .toList();
        List<SecondaryAttributeDTO> toInsert = attributes.stream().filter(attribute -> !attribute.isPersisted())
            .toList();

        repository.removeAll(universe, toRemove);
        toUpdate.forEach(attribute -> attributeController.update(universe, attribute));
        attributeController.insertAll(universe, toInsert);
    }

    @GetMapping("supported-variables")
    @UniverseRead
    @Operation(summary = "Get all supported variables", operationId = "getAllSupportedVariables")
    public Collection<String> getAllSupportedVariables(@PathVariable String universe) {
        return attributeController.getSupportedVariables(universe);
    }
}
