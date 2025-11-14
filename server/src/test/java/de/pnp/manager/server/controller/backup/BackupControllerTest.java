package de.pnp.manager.server.controller.backup;

import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.character.Talent;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.spell.Spell;
import de.pnp.manager.component.universe.Universe;
import de.pnp.manager.server.ManipulatesMetadata;
import de.pnp.manager.server.contoller.UserController;
import de.pnp.manager.server.database.MaterialRepository;
import de.pnp.manager.server.database.SpellRepository;
import de.pnp.manager.server.database.TalentRepository;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import de.pnp.manager.server.database.item.ItemRepository;
import de.pnp.manager.server.database.universe.UniverseRepository;
import de.pnp.manager.utils.TestItemBuilder.TestItemBuilderFactory;
import de.pnp.manager.utils.TestSpellBuilder.TestSpellBuilderFactory;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import static de.pnp.manager.utils.TestUtils.tagSet;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link BackupExportController} and {@link BackupImportController}.
 */
@ManipulatesMetadata
@SpringBootTest
public class BackupControllerTest {

    @Autowired
    private TestItemBuilderFactory itemBuilder;

    @Autowired
    private TestSpellBuilderFactory spellBuilder;

    @Autowired
    private UniverseRepository universeRepository;

    @Autowired
    private UserController userController;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private SpellRepository spellRepository;

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private PrimaryAttributeRepository primaryAttributeRepository;

    @Autowired
    private BackupExportController exportController;

    @Autowired
    private BackupImportController importController;

    @AfterEach
    void tearDown() {
        for (Universe universe : universeRepository.getAll()) {
            universeRepository.remove(universe.getId());
        }
        for (String username : userController.getAllUsernames()) {
            userController.removeUser(username);
        }
    }

    @Test
    void testExportAndImport(@TempDir Path tempDir) throws IOException {
        Universe universe = universeRepository.insert(new Universe(null, "Test-Universe"));
        assertThat(universe).isNotNull();
        ObjectId universeId = universe.getId();

        Material material = materialRepository.insert(universeId,
                new Material(null, "Iron", List.of()));
        Collection<Item> items = itemRepository.insertAll(universeId, List.of(
                itemBuilder.createItemBuilder(universeId).withName("Item").buildItem(),
                itemBuilder.createItemBuilder(universeId).withName("Weapon").withMaterial(material)
                        .buildArmor()
        ));
        PrimaryAttribute primaryAttribute = primaryAttributeRepository.insert(universeId,
                new PrimaryAttribute(null, "Primary", "PRI"));
        Talent talent = talentRepository.insert(universeId,
                new Talent(null, "Magic", tagSet("Magic"), primaryAttribute, primaryAttribute, primaryAttribute));
        Collection<Spell> spells = spellRepository.insertAll(universeId,
                List.of(spellBuilder.createSpellBuilder(universeId).withName("Spell")
                        .withCost(10, items.stream().findFirst().orElseThrow()).withTalents(talent).build()));

        File backupZip = tempDir.resolve("backup.zip").toFile();

        try (FileOutputStream outputStream = new FileOutputStream(backupZip)) {
            exportController.export(outputStream, null);
        }

        assertThat(universeRepository.remove(universeId)).isTrue();
        assertThat(universeRepository.exists(universeId)).isFalse();

        try (FileInputStream inputStream = new FileInputStream(backupZip)) {
            importController.importBackup(inputStream);
        }

        assertThat(materialRepository.getAll(universeId)).containsExactly(material);
        assertThat(itemRepository.getAll(universeId)).containsExactlyInAnyOrderElementsOf(items);
        assertThat(talentRepository.getAll(universeId)).containsExactly(talent);
        assertThat(primaryAttributeRepository.getAll(universeId)).containsExactly(primaryAttribute);
        assertThat(spellRepository.getAll(universeId)).containsExactlyInAnyOrderElementsOf(spells);
    }
}
