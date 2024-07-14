package de.pnp.manager.server.service.item;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import de.pnp.manager.component.item.ExtendedItemType;
import de.pnp.manager.security.UniverseRead;
import de.pnp.manager.security.UniverseWrite;
import de.pnp.manager.server.contoller.ExtendedItemTypeController;
import de.pnp.manager.server.database.item.ItemTypeRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
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
 * Service to access {@link ItemTypeRepository}.
 */
@RestController
@Validated
@RequestMapping("api/{universe}/extended-item-types")
public class ExtendedItemTypeService {

    @Autowired
    private ExtendedItemTypeController itemTypeController;

    @GetMapping
    @UniverseRead
    @Operation(summary = "Get all objects from the database", operationId = "getAllExtendedItemTypes")
    public Collection<ExtendedItemType> getAll(@PathVariable String universe,
        @RequestParam(required = false) List<ObjectId> ids) {
        return itemTypeController.getAll(universe, ids);
    }

    @PostMapping
    @UniverseWrite
    @Operation(summary = "Inserts the objects into the database", operationId = "insertAllExtendedItemTypes")
    public Collection<ExtendedItemType> insertAll(@PathVariable String universe,
        @RequestBody List<@Valid ExtendedItemType> objects) {
        return itemTypeController.insertAll(universe, objects);
    }

    @PutMapping("{id}")
    @UniverseWrite
    @Operation(summary = "Updates an object in the database", operationId = "updateExtendedItemType")
    public ExtendedItemType update(@PathVariable String universe, @PathVariable ObjectId id,
        @RequestBody @Valid ExtendedItemType object) {
        if (object.getId() != null && !Objects.equals(id, object.getId())) {
            throw new ResponseStatusException(BAD_REQUEST, "The id of the object does not match.");
        }
        return itemTypeController.update(universe, object);
    }

}
