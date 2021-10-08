package de.pnp.manager.model;

import de.pnp.manager.model.manager.Manager;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.character.data.AttackTypes;
import de.pnp.manager.testHelper.TestWithDatabaseAccess;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ResourceLock(value = "SERVER_SOCKET", mode = ResourceAccessMode.READ_WRITE)
public class BattleTest extends TestWithDatabaseAccess {

    @Test
    public void test() {
        try (Manager manager = new Manager()) {
            Battle b = manager.getBattleHandler().createBattle();

            b.createEnemy();
            b.createPlayer();

            PnPCharacter enemy = b.enemiesProperty().stream().findFirst().orElseThrow();
            PnPCharacter player = b.playersProperty().stream().findFirst().orElseThrow();

            assertFalse(b.isSameTeam(enemy, player));
            assertTrue(b.isPlayer(player));
            assertFalse(b.isPlayer(enemy));

            boolean finished = false;

            while (!finished) {
                enemy.takeDamage(5, AttackTypes.DIRECT, false, 1, 0, player);
                b.nextTurn();
                finished = b.enemiesProperty().stream().anyMatch(PnPCharacter::isDead) || b.playersProperty().stream().anyMatch(PnPCharacter::isDead);
            }

            assertTrue(b.roundProperty().get() > 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
