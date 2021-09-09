package de.pnp.manager.model;

import de.pnp.manager.model.member.data.AttackTypes;
import de.pnp.manager.model.member.interfaces.IBattleMember;
import org.junit.jupiter.api.Test;
import de.pnp.manager.testHelper.TestWithDatabaseAccess;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BattleTest extends TestWithDatabaseAccess {

    @Test
    public void test() {
        Battle b = new Battle();

        b.createEnemy();
        b.createPlayer();

        IBattleMember enemy = b.enemiesProperty().stream().findFirst().get();
        IBattleMember player = b.playersProperty().stream().findFirst().get();

        assertFalse(b.isSameTeam(enemy, player));
        assertTrue(b.isPlayer(player));
        assertFalse(b.isPlayer(enemy));

        boolean finished = false;

        while (!finished) {
            enemy.takeDamage(5, AttackTypes.direct, false, 1, 0, player);
            b.nextTurn();
            finished = b.enemiesProperty().stream().anyMatch(IBattleMember::isDead) || b.playersProperty().stream().anyMatch(IBattleMember::isDead);
        }

        assertTrue(b.roundProperty().get() > 0);
    }
}
