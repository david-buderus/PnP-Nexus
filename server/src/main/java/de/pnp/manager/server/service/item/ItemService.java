package de.pnp.manager.server.service.item;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.equipable.Armor;
import de.pnp.manager.component.item.equipable.Jewellery;
import de.pnp.manager.component.item.equipable.Shield;
import de.pnp.manager.component.item.equipable.Weapon;
import de.pnp.manager.security.UniverseRead;
import de.pnp.manager.server.database.item.ItemRepository;
import de.pnp.manager.server.service.RepositoryServiceBase;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * Service to access {@link ItemRepository}.
 */
@RestController
@RequestMapping("api/{universe}/items")
public class ItemService extends RepositoryServiceBase<Item, ItemRepository> {

    protected ItemService(@Autowired ItemRepository repository) {
        super(repository);
    }

    @GetMapping("weapons")
    @UniverseRead
    @Operation(summary = "Get all weapons from the database", operationId = "getAllWeapons")
    public Collection<Weapon> getAllWeapons(@PathVariable ObjectId universe) {
        return repository.getAllWeapons(universe);
    }

    @GetMapping("shields")
    @UniverseRead
    @Operation(summary = "Get all weapons from the database", operationId = "getAllShields")
    public Collection<Shield> getAllShields(@PathVariable ObjectId universe) {
        return repository.getAllShields(universe);
    }

    @GetMapping("armor")
    @UniverseRead
    @Operation(summary = "Get all weapons from the database", operationId = "getAllArmor")
    public Collection<Armor> getAllArmor(@PathVariable ObjectId universe) {
        return repository.getAllArmor(universe);
    }

    @GetMapping("jewellery")
    @UniverseRead
    @Operation(summary = "Get all weapons from the database", operationId = "getAllJewellery")
    public Collection<Jewellery> getAllJewellery(@PathVariable ObjectId universe) {
        return repository.getAllJewellery(universe);
    }
}
