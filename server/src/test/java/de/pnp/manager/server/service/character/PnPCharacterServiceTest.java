package de.pnp.manager.server.service.character;

import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.character.*;
import de.pnp.manager.component.character.dto.CharacterStatsDto;
import de.pnp.manager.component.character.dto.PnPCharacterDTO;
import de.pnp.manager.component.inventory.Inventory;
import de.pnp.manager.component.user.GrantedDatabaseObjectAuthority;
import de.pnp.manager.component.user.PnPUserCreation;
import de.pnp.manager.security.SecurityConstants;
import de.pnp.manager.server.ManipulatesMetadata;
import de.pnp.manager.server.UniverseTestBase;
import de.pnp.manager.server.contoller.PnPCharacterDTOConverter;
import de.pnp.manager.server.database.UserDetailsRepository;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import de.pnp.manager.server.database.character.PnPCharacterRepository;
import de.pnp.manager.server.database.character.SpeciesRepository;
import de.pnp.manager.server.service.DelegateMvc;
import de.pnp.manager.utils.TestSecondaryAttributeBuilder;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

import static de.pnp.manager.server.service.ServiceTestUtils.assertForbidden;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link PnPCharacterService}
 */
@ManipulatesMetadata
@AutoConfigureMockMvc
public class PnPCharacterServiceTest extends UniverseTestBase {

    private static final String BASE_PATH = "/api/{universe}/characters";
    private static final String USER = "character-test-user";

    @Autowired
    private DelegateMvc delegateMvc;

    @Autowired
    private PnPCharacterRepository repository;

    @Autowired
    private SpeciesRepository speciesRepository;

    @Autowired
    private PrimaryAttributeRepository primaryAttributeRepository;

    @Autowired
    private TestSecondaryAttributeBuilder.TestSecondaryAttributeBuilderFactory secondaryAttributeBuilder;

    @Autowired
    private PnPCharacterDTOConverter converter;

    @Autowired
    private UserDetailsRepository userRepository;

    private Species exampleSpecies;
    private PrimaryAttribute examplePrimaryAttribute;
    private SecondaryAttribute exampleSecondaryAttribute;

    @BeforeEach
    void setUp() {
        exampleSpecies = speciesRepository.insert(getUniverseId(), new Species(null, "Example Species", ".", true, List.of(), List.of(), List.of()));
        examplePrimaryAttribute = primaryAttributeRepository.insert(getUniverseId(), new PrimaryAttribute(null, "Power", "POW"));
        exampleSecondaryAttribute = secondaryAttributeBuilder.createAttributeBuilder(getUniverseId())
                .addDependency(examplePrimaryAttribute).withName("Life").withShortName("LP").withFormula("10 * POW").persist().build();
    }

    @Nested
    @DisplayName("as admin")
    class AsAdminTest {

        @BeforeEach
        protected void setup() {
            userController.createNewUser(
                    PnPUserCreation.simple(USER, List.of(new SimpleGrantedAuthority(SecurityConstants.ADMIN_ROLE))));
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testCreate() throws Exception {
            runCreateTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testUpdate() throws Exception {
            PnPCharacterDTO exampleCharacter = converter.convert(getUniverseId(), repository.insert(getUniverseId(), converter.convert(getUniverseId(), example("Bernd"))));
            runUpdateTest(exampleCharacter);
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testDelete() throws Exception {
            PnPCharacterDTO exampleCharacter = converter.convert(getUniverseId(), repository.insert(getUniverseId(), converter.convert(getUniverseId(), example("Bernd"))));
            runDeleteTest(exampleCharacter);
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testGet() throws Exception {
            PnPCharacterDTO exampleCharacter = converter.convert(getUniverseId(), repository.insert(getUniverseId(), converter.convert(getUniverseId(), example("Bernd"))));
            PnPCharacterDTO otherCharacter = converter.convert(getUniverseId(), repository.insert(getUniverseId(), converter.convert(getUniverseId(), example("Olaf"))));
            runGetTest(exampleCharacter, otherCharacter);
        }
    }

    @Nested
    @DisplayName("as creator/owner")
    class AsCreatorTest {

        @BeforeEach
        protected void setup() {
            userController.createNewUser(PnPUserCreation.simple(USER, List.of(
                    GrantedDatabaseObjectAuthority.readAuthority(getUniverseId())
            )));
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testCreate() throws Exception {
            runCreateTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testUpdate() throws Exception {
            PnPCharacterDTO exampleCharacter = converter.convert(getUniverseId(), repository.insert(getUniverseId(), converter.convert(getUniverseId(), example("Bernd"))));
            userRepository.addGrantedAuthority(USER, GrantedDatabaseObjectAuthority.ownerAuthority(exampleCharacter.id()));
            runUpdateTest(exampleCharacter);
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testDelete() throws Exception {
            PnPCharacterDTO exampleCharacter = converter.convert(getUniverseId(), repository.insert(getUniverseId(), converter.convert(getUniverseId(), example("Bernd"))));
            userRepository.addGrantedAuthority(USER, GrantedDatabaseObjectAuthority.ownerAuthority(exampleCharacter.id()));
            runDeleteTest(exampleCharacter);
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testOwnerRole() throws Exception {
            PnPCharacterDTO exampleCharacter = create(example("Example"));
            assertThat(userRepository.loadUserByUsername(USER).getAuthorities()).filteredOn(
                            auth -> auth instanceof GrantedDatabaseObjectAuthority)
                    .anyMatch(auth -> ((GrantedDatabaseObjectAuthority) auth).isOwner(exampleCharacter.id()));
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testGet() throws Exception {
            PnPCharacterDTO exampleCharacter = converter.convert(getUniverseId(), repository.insert(getUniverseId(), converter.convert(getUniverseId(), example("Bernd"))));
            userRepository.addGrantedAuthority(USER, GrantedDatabaseObjectAuthority.ownerAuthority(exampleCharacter.id()));
            PnPCharacterDTO otherCharacter = converter.convert(getUniverseId(), repository.insert(getUniverseId(), converter.convert(getUniverseId(), example("Olaf"))));
            runGetOnlyOneTest(exampleCharacter, otherCharacter);
        }
    }

    @Nested
    @DisplayName("as writer")
    class AsWriterTest {

        @BeforeEach
        protected void setup() {
            userController.createNewUser(PnPUserCreation.simple(USER, List.of(
                    GrantedDatabaseObjectAuthority.readAuthority(getUniverseId())
            )));
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testCreate() throws Exception {
            runCreateTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testUpdate() throws Exception {
            PnPCharacterDTO exampleCharacter = converter.convert(getUniverseId(), repository.insert(getUniverseId(), converter.convert(getUniverseId(), example("Bernd"))));
            userRepository.addGrantedAuthority(USER, GrantedDatabaseObjectAuthority.writeAuthority(exampleCharacter.id()));
            runUpdateTest(exampleCharacter);
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testDelete() {
            runNotAllowedDeleteTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testGet() throws Exception {
            PnPCharacterDTO exampleCharacter = converter.convert(getUniverseId(), repository.insert(getUniverseId(), converter.convert(getUniverseId(), example("Bernd"))));
            userRepository.addGrantedAuthority(USER, GrantedDatabaseObjectAuthority.writeAuthority(exampleCharacter.id()));
            PnPCharacterDTO otherCharacter = converter.convert(getUniverseId(), repository.insert(getUniverseId(), converter.convert(getUniverseId(), example("Olaf"))));
            runGetOnlyOneTest(exampleCharacter, otherCharacter);
        }
    }

    @Nested
    @DisplayName("as reader")
    class AsReaderTest {

        @BeforeEach
        protected void setup() {
            userController.createNewUser(PnPUserCreation.simple(USER, List.of(
                    GrantedDatabaseObjectAuthority.readAuthority(getUniverseId())
            )));
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testCreate() throws Exception {
            runCreateTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testUpdate() {
            PnPCharacterDTO exampleCharacter = converter.convert(getUniverseId(), repository.insert(getUniverseId(), converter.convert(getUniverseId(), example("Bernd"))));
            userRepository.addGrantedAuthority(USER, GrantedDatabaseObjectAuthority.readAuthority(exampleCharacter.id()));
            runNotAllowedUpdateTest(exampleCharacter);
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testDelete() {
            runNotAllowedDeleteTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testGet() throws Exception {
            PnPCharacterDTO exampleCharacter = converter.convert(getUniverseId(), repository.insert(getUniverseId(), converter.convert(getUniverseId(), example("Bernd"))));
            userRepository.addGrantedAuthority(USER, GrantedDatabaseObjectAuthority.readAuthority(exampleCharacter.id()));
            PnPCharacterDTO otherCharacter = converter.convert(getUniverseId(), repository.insert(getUniverseId(), converter.convert(getUniverseId(), example("Olaf"))));
            runGetOnlyOneTest(exampleCharacter, otherCharacter);
        }
    }

    @Nested
    @DisplayName("as no rights")
    class AsNoRightsTest {

        @BeforeEach
        protected void setup() {
            userController.createNewUser(PnPUserCreation.simple(USER, List.of(
                    GrantedDatabaseObjectAuthority.readAuthority(getUniverseId())
            )));
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testCreate() throws Exception {
            runCreateTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testUpdate() {
            PnPCharacterDTO exampleCharacter = converter.convert(getUniverseId(), repository.insert(getUniverseId(), converter.convert(getUniverseId(), example("Bernd"))));
            runNotAllowedUpdateTest(exampleCharacter);
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testDelete() {
            runNotAllowedDeleteTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testGet() throws Exception {
            PnPCharacterDTO exampleCharacter = converter.convert(getUniverseId(), repository.insert(getUniverseId(), converter.convert(getUniverseId(), example("Bernd"))));
            PnPCharacterDTO otherCharacter = converter.convert(getUniverseId(), repository.insert(getUniverseId(), converter.convert(getUniverseId(), example("Olaf"))));

            assertForbidden(() -> getOne(exampleCharacter.id()));
            assertForbidden(() -> getOne(otherCharacter.id()));
            assertThat(getAll()).isEmpty();
        }
    }

    @Nested
    @DisplayName("as no universe rights")
    class AsNoUniverseRightsTest {

        @BeforeEach
        protected void setup() {
            userController.createNewUser(PnPUserCreation.simple(USER, List.of()));
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testCreate() {
            runNotAllowedCreateTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testUpdate() {
            PnPCharacterDTO exampleCharacter = converter.convert(getUniverseId(), repository.insert(getUniverseId(), converter.convert(getUniverseId(), example("Bernd"))));
            runNotAllowedUpdateTest(exampleCharacter);
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testDelete() {
            runNotAllowedDeleteTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testGet() {
            PnPCharacterDTO exampleCharacter = converter.convert(getUniverseId(), repository.insert(getUniverseId(), converter.convert(getUniverseId(), example("Bernd"))));
            PnPCharacterDTO otherCharacter = converter.convert(getUniverseId(), repository.insert(getUniverseId(), converter.convert(getUniverseId(), example("Olaf"))));

            assertForbidden(() -> getOne(exampleCharacter.id()));
            assertForbidden(() -> getOne(otherCharacter.id()));
            assertForbidden(PnPCharacterServiceTest.this::getAll);
        }
    }

    private void runCreateTest() throws Exception {
        PnPCharacterDTO persistedCharacter = create(example("Example"));
        assertThat(getOne(persistedCharacter.id())).isEqualTo(persistedCharacter);
        assertThat(getAll()).containsExactly(persistedCharacter);
    }

    private void runNotAllowedCreateTest() {
        assertForbidden(() -> create(example("Not allowed create")));
    }

    private void runUpdateTest(PnPCharacterDTO exampleCharacter) throws Exception {
        assertThat(getOne(exampleCharacter.id())).isEqualTo(exampleCharacter);

        PnPCharacterDTO changedCharacter = example(exampleCharacter.id(), "Olaf");
        PnPCharacterDTO returnValue = update(changedCharacter);
        assertThat(returnValue).isEqualTo(changedCharacter);
        assertThat(getOne(exampleCharacter.id())).isEqualTo(changedCharacter);
        assertThat(getAll()).containsExactly(changedCharacter);
    }

    private void runNotAllowedUpdateTest(PnPCharacterDTO exampleCharacter) {
        assertForbidden(() -> update(exampleCharacter));
    }

    private void runDeleteTest(PnPCharacterDTO exampleCharacter) throws Exception {
        assertThat(getOne(exampleCharacter.id())).isEqualTo(exampleCharacter);

        deleteOne(exampleCharacter.id());

        assertThat(getAll()).isEmpty();
        assertThatThrownBy(() -> getOne(exampleCharacter.id())).isInstanceOf(ResponseStatusException.class)
                .extracting(e -> ((ResponseStatusException) e).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(userRepository.getAllUsernames().stream()
                .flatMap(username -> userRepository.loadUserByUsername(username).getAuthorities().stream()
                        .filter(GrantedDatabaseObjectAuthority.class::isInstance))
        ).noneMatch(authority -> ((GrantedDatabaseObjectAuthority) authority).getObjectId().equals(exampleCharacter.id()));
    }

    private void runNotAllowedDeleteTest() {
        PnPCharacterDTO exampleCharacter = converter.convert(getUniverseId(), repository.insert(getUniverseId(), converter.convert(getUniverseId(), example("Bernd"))));

        assertThatThrownBy(() -> deleteOne(exampleCharacter.id())).isInstanceOf(ResponseStatusException.class)
                .extracting(e -> ((ResponseStatusException) e).getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private void runGetTest(PnPCharacterDTO exampleCharacter, PnPCharacterDTO otherCharacter) throws Exception {
        assertThat(getOne(exampleCharacter.id())).isEqualTo(exampleCharacter);
        assertThat(getOne(otherCharacter.id())).isEqualTo(otherCharacter);
        assertThat(getAll()).containsExactly(exampleCharacter, otherCharacter);
    }

    private void runGetOnlyOneTest(PnPCharacterDTO exampleCharacter, PnPCharacterDTO otherCharacter) throws Exception {
        assertThat(getOne(exampleCharacter.id())).isEqualTo(exampleCharacter);
        assertForbidden(() -> getOne(otherCharacter.id()));
        assertThat(getAll()).containsExactly(exampleCharacter);
    }

    private PnPCharacterDTO create(PnPCharacterDTO dto) throws Exception {
        return delegateMvc.insertList(PnPCharacterDTO.class, BASE_PATH, List.of(dto), getUniverseId()).getFirst();
    }

    private PnPCharacterDTO getOne(ObjectId id) throws Exception {
        return delegateMvc.getOne(PnPCharacterDTO.class, BASE_PATH + "/{id}", getUniverseId(), id);
    }

    private List<PnPCharacterDTO> getAll() throws Exception {
        return delegateMvc.getList(PnPCharacterDTO.class, BASE_PATH, getUniverseId());
    }

    private PnPCharacterDTO update(PnPCharacterDTO dto) throws Exception {
        return delegateMvc.update(PnPCharacterDTO.class, BASE_PATH + "/{id}", dto, getUniverseId(), dto.id());
    }

    private void deleteOne(ObjectId id) throws Exception {
        delegateMvc.deleteObjects(BASE_PATH + "/{id}", getUniverseId(), id);
    }

    private PnPCharacterDTO example(String name) {
        return example(null, name);
    }

    private PnPCharacterDTO example(ObjectId id, String name) {
        return new PnPCharacterDTO(id,
                new CharacterDescription(name, "Smith", "male", "A smith", "A smith", "Boring", "Smith a sword", "None", ""),
                new CharacterLevel(3, 0, 0),
                new CharacterOrigin(exampleSpecies, null),
                List.of(), List.of(),
                new CharacterStatsDto(
                        Map.of(examplePrimaryAttribute.getId(), new CharacterStatsDto.StatsDto(4, 0, 4)),
                        Map.of(exampleSecondaryAttribute.getId(), new CharacterStatsDto.StatsDto(40, 0, 40))
                ),
                Map.of(),
                new CharacterEquipment(List.of(), List.of(), List.of(), List.of(), Map.of(), Map.of()),
                new CharacterInventory(Map.of("Backpack", new Inventory(20, List.of())), 200),
                List.of(),
                Map.of());
    }
}
