package de.pnp.manager.model.manager;

import de.pnp.manager.model.Battle;
import de.pnp.manager.network.session.ISession;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BattleHandler {

    private static int ID_COUNTER = 0;

    protected static synchronized String getNextBattleID(){
        return "battle-" + DigestUtils.sha256Hex(String.valueOf(++ID_COUNTER));
    }

    // maps sessionId to battles
    public Map<String, ArrayList<Battle>> sessionBattleMap;

    private final Manager manager;

    public BattleHandler(Manager manager) {
        this.sessionBattleMap = new HashMap<>();
        this.manager = manager;
    }

    public Battle createBattle() {
        Collection<? extends ISession> activeSessions = manager.getNetworkHandler().getActiveSessions();

        if (activeSessions.isEmpty()) {
            return null; //Better solution needed
        }

        return createBattle(activeSessions.stream().findFirst().get().getSessionID());
    }

    public Battle createBattle(String sessionID) {
        Battle battle = new Battle(getNextBattleID(), sessionID, manager.getCharacterHandler());

        ArrayList<Battle> battles = this.sessionBattleMap.computeIfAbsent(sessionID, k -> new ArrayList<>());
        battles.add(battle);

        return battle;
    }

    public void deleteBattle(String sessionID, String battleID) {
        Collection<Battle> battles = this.sessionBattleMap.get(sessionID);

        if (battles != null) {
            battles.removeIf(battle -> battle.getBattleID().equals(battleID));
        }
    }

    public void deleteBattle(String battleID) {
        for (ArrayList<Battle> battles : this.sessionBattleMap.values()) {
            battles.removeIf(battle -> battle.getBattleID().equals(battleID));
        }
    }
}
