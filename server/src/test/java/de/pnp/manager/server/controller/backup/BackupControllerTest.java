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
        Universe universe = new Universe(new ObjectId(), "Test-Universe");
        ObjectId universeName = universe.getId();
        assertThat(universeRepository.insert(universe)).isNotNull();

        Material material = materialRepository.insert(universeName,
                new Material(null, "Iron", List.of()));
        Collection<Item> items = itemRepository.insertAll(universeName, List.of(
                itemBuilder.createItemBuilder(universeName).withName("Item").buildItem(),
                itemBuilder.createItemBuilder(universeName).withName("Weapon").withMaterial(material)
                        .buildArmor()
        ));
        PrimaryAttribute primaryAttribute = primaryAttributeRepository.insert(universeName,
                new PrimaryAttribute(null, "Primary", "PRI"));
        Talent talent = talentRepository.insert(universeName,
                new Talent(null, "Magic", tagSet("Magic"), primaryAttribute, primaryAttribute, primaryAttribute));
        Collection<Spell> spells = spellRepository.insertAll(universeName,
                List.of(spellBuilder.createSpellBuilder(universeName).withName("Spell")
                        .withCost(10, items.stream().findFirst().orElseThrow()).withTalents(talent).build()));

        File backupZip = tempDir.resolve("backup.zip").toFile();

        try (FileOutputStream outputStream = new FileOutputStream(backupZip)) {
            exportController.export(outputStream, null);
        }

        assertThat(universeRepository.remove(universeName)).isTrue();
        assertThat(universeRepository.exists(universeName)).isFalse();

        try (FileInputStream inputStream = new FileInputStream(backupZip)) {
            importController.importBackup(inputStream);
        }

        assertThat(materialRepository.getAll(universeName)).containsExactly(material);
        assertThat(itemRepository.getAll(universeName)).containsExactlyInAnyOrderElementsOf(items);
        assertThat(talentRepository.getAll(universeName)).containsExactly(talent);
        assertThat(primaryAttributeRepository.getAll(universeName)).containsExactly(primaryAttribute);
        assertThat(spellRepository.getAll(universeName)).containsExactlyInAnyOrderElementsOf(spells);
    }
}
