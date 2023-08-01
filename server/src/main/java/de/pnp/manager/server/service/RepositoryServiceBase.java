package de.pnp.manager.server.service;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.server.database.RepositoryBase;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.Collection;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Base class to provide service access to {@link RepositoryBase}.
 */
@RestController
@Validated
public abstract class RepositoryServiceBase<Obj extends DatabaseObject, Repo extends RepositoryBase<Obj>> {

    /**
     * {@link RepositoryBase} for this service.
     */
    protected final Repo repository;

    protected RepositoryServiceBase(Repo repository) {
        this.repository = repository;
    }

    @GetMapping
    @Operation(summary = "Get all objects from the database", operationId = "getAll")
    public Collection<Obj> getAll(@PathVariable String universe, @RequestParam(required = false) List<ObjectId> ids) {
        if (ids == null || ids.isEmpty()) {
            return repository.getAll(universe);
        }
        return repository.getAll(universe, ids);
    }

    @PostMapping
    @Operation(summary = "Inserts the objects into the database", operationId = "insertAll")
    public Collection<Obj> insertAll(@PathVariable String universe, @RequestBody List<@Valid Obj> objects) {
        return repository.insertAll(universe, objects);
    }

    @DeleteMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes all objects with the given ids from the database", operationId = "deleteAll")
    public void deleteAll(@PathVariable String universe, @RequestParam List<ObjectId> ids) {
        if (!repository.removeAll(universe, ids)) {
            throw createNotFound("Unable to find all resource with the given ids");
        }
    }

    @GetMapping("{id}")
    @Operation(summary = "Get an object from the database", operationId = "get")
    public Obj get(@PathVariable String universe, @PathVariable ObjectId id) {
        return repository.get(universe, id)
            .orElseThrow(() -> createNotFound("Unable to find resource with id '%s'", id));
    }

    @PutMapping("{id}")
    @Operation(summary = "Updates an object in the database", operationId = "update")
    public Obj update(@PathVariable String universe, @PathVariable ObjectId id, @RequestBody @Valid Obj object) {
        return repository.update(universe, id, object);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes an object from the database", operationId = "delete")
    public void delete(@PathVariable String universe, @PathVariable ObjectId id) {
        if (!repository.remove(universe, id)) {
            throw createNotFound("Unable to find resource with id '%s'", id);
        }
    }

    /**
     * Returns a {@link ResponseStatusException} with a 404 status.
     */
    protected ResponseStatusException createNotFound(String text, Object... objects) {
        return new ResponseStatusException(NOT_FOUND, String.format(text, objects));
    }
}
