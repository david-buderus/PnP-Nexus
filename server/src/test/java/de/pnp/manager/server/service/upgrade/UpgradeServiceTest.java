package de.pnp.manager.server.service.upgrade;

import de.pnp.manager.component.ECalculation;
import de.pnp.manager.component.upgrade.Upgrade;
import de.pnp.manager.component.upgrade.effect.EItemEquipmentManipulator;
import de.pnp.manager.component.upgrade.effect.EquipmentItemEffect;
import de.pnp.manager.component.upgrade.effect.SimpleItemEffect;
import de.pnp.manager.server.database.upgrade.UpgradeRepository;
import de.pnp.manager.server.service.RepositoryServiceBaseTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Tests for {@link UpgradeService}
 */
public class UpgradeServiceTest extends RepositoryServiceBaseTest<Upgrade, UpgradeRepository, UpgradeService> {

    public UpgradeServiceTest(@Autowired UpgradeService upgradeService, @Autowired UpgradeRepository repository) {
        super(upgradeService, repository, Upgrade.class);
    }

    @Override
    protected List<Upgrade> createObjects() {
        return List.of(createUpgrade().withName("A")
                        .addEffect(new EquipmentItemEffect("More Damage", 2, EItemEquipmentManipulator.DAMAGE,
                                ECalculation.ADDITIVE)).build(),
                createUpgrade().withName("B")
                        .addEffect(new SimpleItemEffect("Nothing")).build(),
                createUpgrade().withName("C")
                        .addEffect(new EquipmentItemEffect("Wall", 10, EItemEquipmentManipulator.ARMOR,
                                ECalculation.MULTIPLICATIVE)).build());
    }
}
