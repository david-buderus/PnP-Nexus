package de.pnp.manager.server.controller.backup;

import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.Spell;
import de.pnp.manager.component.Universe;
import de.pnp.manager.component.attributes.EPrimaryAttribute;
import de.pnp.manager.component.character.Talent;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.server.component.TestItemBuilder.TestItemBuilderFactory;
import de.pnp.manager.server.database.ItemRepository;
import de.pnp.manager.server.database.MaterialRepository;
import de.pnp.manager.server.database.SpellRepository;
import de.pnp.manager.server.database.TalentRepository;
import de.pnp.manager.server.database.UniverseRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Tests for {@link BackupExportController} and {@link BackupImportController}.
 */
@SpringBootTest
public class BackupControllerTest {

    @Autowired
    private TestItemBuilderFactory itemBuilder;

    @Autowired
    private UniverseRepository universeRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private SpellRepository spellRepository;

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private BackupExportController exportController;

    @Autowired
    private BackupImportController importController;

    @AfterEach
    void tearDown() {
        for (Universe universe : universeRepository.getAll()) {
            universeRepository.remove(universe.getName());
        }
    }

    @Test
    void testExportAndImport(@TempDir Path tempDir) throws IOException {
        Universe universe = new Universe("test", "Test-Universe");
        String universeName = universe.getName();
        assertThat(universeRepository.insert(universe)).isNotNull();

        Material material = materialRepository.insert(universeName,
            new Material(null, "Iron", List.of()));
        Collection<Item> items = itemRepository.insertAll(universeName, List.of(
            itemBuilder.createItemBuilder(universeName).withName("Item").buildItem(),
            itemBuilder.createItemBuilder(universeName).withName("Weapon").withMaterial(material)
                .buildArmor()
        ));
        Talent talent = talentRepository.insert(universeName,
            new Talent(null, "Magic", "Magic", EPrimaryAttribute.INTELLIGENCE,
                EPrimaryAttribute.INTELLIGENCE, EPrimaryAttribute.CHARISMA));
        Collection<Spell> spells = spellRepository.insertAll(universeName,
            List.of(new Spell(null, "Spell", "", "", "", List.of(talent), 2)));

        File backupZip = tempDir.resolve("backup.zip").toFile();

        try (FileOutputStream outputStream = new FileOutputStream(backupZip)) {
            exportController.exportUniverses(outputStream);
        }

        assertThat(universeRepository.remove(universeName)).isTrue();
        assertThat(universeRepository.exists(universeName)).isFalse();

        try (FileInputStream inputStream = new FileInputStream(backupZip)) {
            importController.importBackup(inputStream);
        }

        assertThat(materialRepository.getAll(universeName)).containsExactly(material);
        assertThat(itemRepository.getAll(universeName)).containsExactlyInAnyOrderElementsOf(items);
        assertThat(talentRepository.getAll(universeName)).containsExactly(talent);
        assertThat(spellRepository.getAll(universeName)).containsExactlyInAnyOrderElementsOf(spells);
    }
}
