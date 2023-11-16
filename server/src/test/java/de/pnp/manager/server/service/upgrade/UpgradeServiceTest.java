package de.pnp.manager.server.service.upgrade;

import de.pnp.manager.component.upgrade.Upgrade;
import de.pnp.manager.component.upgrade.effect.AdditiveUpgradeEffect;
import de.pnp.manager.component.upgrade.effect.EUpgradeManipulator;
import de.pnp.manager.component.upgrade.effect.MultiplicativeUpgradeEffect;
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
                .addEffect(new AdditiveUpgradeEffect("More Damage", EUpgradeManipulator.DAMAGE, 2)).build(),
            createUpgrade().withName("B")
                .addEffect(SimpleUpgradeEffect.create("Nothing")).build(),
            createUpgrade().withName("C")
                .addEffect(new MultiplicativeUpgradeEffect("Wall", EUpgradeManipulator.ARMOR, 10)).build());
    }
}
