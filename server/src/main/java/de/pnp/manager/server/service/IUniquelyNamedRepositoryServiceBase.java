package de.pnp.manager.server.service;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.IUniquelyNamedDataObject;
import de.pnp.manager.server.database.RepositoryBase;
import de.pnp.manager.server.database.interfaces.IUniquelyNamedRepository;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Collection;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Base class to provide service access to {@link IUniquelyNamedRepository}.
 */
public class IUniquelyNamedRepositoryServiceBase<Obj extends DatabaseObject & IUniquelyNamedDataObject,
    Repo extends RepositoryBase<Obj> & IUniquelyNamedRepository<Obj>> extends
    RepositoryServiceBase<Obj, Repo> {

    protected IUniquelyNamedRepositoryServiceBase(Repo repository) {
        super(repository);
    }

    @PostMapping("all/named")
    @Operation(summary = "Get all objects with the given names from the database", operationId = "getAllByName")
    public Collection<Obj> getAllByName(@PathVariable String universe, @RequestBody Collection<String> names) {
        return repository.getAllByName(universe, names);
    }

    @GetMapping("named/{name}")
    @Operation(summary = "Get an object from the database", operationId = "getByName")
    public Obj getByName(@PathVariable String universe, @PathVariable String name) {
        return repository.get(universe, name)
            .orElseThrow(() -> createNotFound("Unable to find resource with name '%s'", name));
    }
}
