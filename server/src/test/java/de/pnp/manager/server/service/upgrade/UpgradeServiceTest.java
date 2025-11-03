package de.pnp.manager.server.service.upgrade;

import de.pnp.manager.component.ECalculation;
import de.pnp.manager.component.upgrade.Upgrade;
import de.pnp.manager.component.upgrade.effect.EUpgradeEquipmentManipulator;
import de.pnp.manager.component.upgrade.effect.EquipmentUpgradeEffect;
import de.pnp.manager.component.upgrade.effect.SimpleUpgradeEffect;
import de.pnp.manager.server.database.upgrade.UpgradeRepository;
import de.pnp.manager.server.service.RepositoryServiceBaseTest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

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
                .addEffect(new EquipmentUpgradeEffect("More Damage", 2, EUpgradeEquipmentManipulator.DAMAGE,
                    ECalculation.ADDITIVE)).build(),
            createUpgrade().withName("B")
                .addEffect(new SimpleUpgradeEffect("Nothing")).build(),
            createUpgrade().withName("C")
                .addEffect(new EquipmentUpgradeEffect("Wall", 10, EUpgradeEquipmentManipulator.ARMOR,
                    ECalculation.MULTIPLICATIVE)).build());
    }
}
