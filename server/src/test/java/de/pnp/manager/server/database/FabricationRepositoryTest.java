package de.pnp.manager.server.database;

import static de.pnp.manager.server.component.ItemBuilder.someItem;
import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.Fabrication;
import de.pnp.manager.component.Fabrication.FabricationItem;
import de.pnp.manager.component.item.Item;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FabricationRepositoryTest extends UniverseTestBase {

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private FabricationRepository fabricationRepository;

  @Test
  void testInsertFabrication() {
    Item result = itemRepository.insert(universe, someItem().withName("Result").buildItem());
    Item itemA = itemRepository.insert(universe, someItem().withName("A").buildItem());
    Item itemB = itemRepository.insert(universe, someItem().withName("B").buildItem());

    Fabrication fabrication = new Fabrication(null, "", "", "",
        new FabricationItem(1, result),
        null, List.of(new FabricationItem(7, itemA),
        new FabricationItem(4, itemB)));

    fabricationRepository.insert(universe, fabrication);
    assertThat(fabricationRepository.getAll(universe)).contains(fabrication);
  }

  @Test
  void testChangeFabricationResult() {
    Item resultOld = itemRepository.insert(universe, someItem().withName("Result old").buildItem());
    Item itemA = itemRepository.insert(universe, someItem().withName("A").buildItem());

    Fabrication fabrication = new Fabrication(null, "", "", "",
        new FabricationItem(1, resultOld), null, List.of(new FabricationItem(2, itemA)));

    fabricationRepository.insert(universe, fabrication);
    assertThat(fabricationRepository.getAll(universe)).contains(fabrication);

    Item resultNew = someItem().withName("Result new").buildItem();
    itemRepository.update(universe, resultOld.getId(), resultNew);

    Optional<Fabrication> optFabrication = fabricationRepository.getAll(universe).stream()
        .findFirst();
    assertThat(optFabrication).isNotEmpty();
    assertThat(optFabrication.get().getProduct().item()).isEqualTo(resultNew);
  }
}