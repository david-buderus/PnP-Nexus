package de.pnp.manager.webapp.database;

import static org.junit.jupiter.api.Assertions.fail;

import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.math.BinaryExpressionTree;
import de.pnp.manager.component.math.IExpressionVariable.PrimaryAttributeVariable;
import de.pnp.manager.component.math.IllegalFormulaException;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import de.pnp.manager.server.database.attributes.SecondaryAttributeRepository;
import de.pnp.manager.webapp.pages.MainMenu;
import de.pnp.manager.webapp.pages.OverviewBasePage;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests the secondary attribute overview page.
 */
@TestServer(EServerTestConfiguration.CHARACTERS)
public class SecondaryAttributePageTest extends
    UniquelyNamedOverviewTestBase<SecondaryAttribute, SecondaryAttributeRepository> {

    @Autowired
    private PrimaryAttributeRepository primaryAttributeRepository;

    public SecondaryAttributePageTest(@Autowired SecondaryAttributeRepository repository) {
        super(repository);
    }

    @Override
    protected OverviewBasePage openTestPage(MainMenu mainMenu) {
        return mainMenu.openSecondaryAttributePage();
    }

    @Override
    protected List<Pair<String, Comparator<SecondaryAttribute>>> getSorters() {
        return List.of();
    }

    @Override
    protected SecondaryAttribute getWrongObject() {
        return new SecondaryAttribute(null, "", false, createDependencies(""));
    }

    @Override
    protected List<String> getExpectedErrorFields() {
        return List.of("name", "calculationFormula");
    }

    @Override
    protected SecondaryAttribute getCorrectObject() {
        return new SecondaryAttribute(null, "Melee Damage", false, createDependencies("0.5 * STR",
            primaryAttributeRepository.get(getUniverseName(), "Strength").orElseThrow()));
    }

    @Override
    protected SecondaryAttribute getEditedObject() {
        return new SecondaryAttribute(null, "Mana Points", true,
            getOriginalModifyObject().getCalculationFormula());
    }

    @Override
    protected String getChangeIdentifier() {
        return "Mana Points";
    }

    @Override
    protected String getEditObjectName() {
        return "Mana";
    }

    private BinaryExpressionTree createDependencies(String formula, PrimaryAttribute... attributes) {
        try {
            return BinaryExpressionTree.from(formula,
                Arrays.stream(attributes).map(PrimaryAttributeVariable::new).collect(
                    Collectors.toSet()));
        } catch (IllegalFormulaException e) {
            return fail(e);
        }
    }
}
