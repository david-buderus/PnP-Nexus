package de.pnp.manager.model;

import de.pnp.manager.main.CopyService;
import de.pnp.manager.main.Database;
import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.main.WorkbookService;
import de.pnp.manager.model.manager.BattleHandler;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import de.pnp.manager.model.member.BattleMember;
import de.pnp.manager.model.member.GeneratedExtendedBattleMember;
import de.pnp.manager.model.member.generation.specs.*;
import de.pnp.manager.model.member.interfaces.IBattleMember;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Battle {

    private final String battleID;
    private final StringProperty name;
    private final IntegerProperty round;
    private final ListProperty<IBattleMember> players;
    private final ListProperty<IBattleMember> enemies;
    private final HashMap<IBattleMember, Integer> damageStatistic;
    private final HashMap<IBattleMember, Integer> healStatistic;

    /**
     * Don't create a battle this way.
     * Use {@link BattleHandler#createBattle(String)}.
     */
    public Battle(String battleID) {
        this.battleID = battleID;
        this.players = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.enemies = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.damageStatistic = new HashMap<>();
        this.healStatistic = new HashMap<>();
        this.round = new SimpleIntegerProperty(1);
        this.name = new SimpleStringProperty(LanguageUtility.getMessage("battle.defaultName"));
    }

    public void nextTurn() {
        for (IBattleMember member : players) {
            member.nextTurn();
        }
        for (IBattleMember member : enemies) {
            member.nextTurn();
        }
        round.set(round.get() + 1);
    }

    public void reset() {
        for (IBattleMember member : players) {
            member.reset();
        }
        enemies.clear();
        round.set(0);
        damageStatistic.clear();
        healStatistic.clear();
    }

    public void removeMember(IBattleMember member) {
        players.remove(member);
        enemies.remove(member);
    }

    public boolean isPlayer(IBattleMember member) {
        return players.contains(member);
    }

    public double getAveragePlayerLevel() {
        return players.stream().mapToDouble(IBattleMember::getLevel).average().orElse(1);
    }

    public boolean isSameTeam(IBattleMember... members) {
        boolean allPlayers = true;
        boolean allEnemies = true;

        for (IBattleMember member : members) {
            if (isPlayer(member)) {
                allEnemies = false;
            } else {
                allPlayers = false;
            }
        }

        return allPlayers || allEnemies;
    }

    public void addToDamageStatistic(IBattleMember member, int damage) {
        if (!damageStatistic.containsKey(member)) {
            damageStatistic.put(member, 0);
        }
        damageStatistic.put(member, damageStatistic.get(member) + damage);
    }

    public void addToHealStatistic(IBattleMember member, int heal) {
        if (!healStatistic.containsKey(member)) {
            healStatistic.put(member, 0);
        }
        healStatistic.put(member, healStatistic.get(member) + heal);
    }

    public int getDamageDealt(IBattleMember member) {
        return damageStatistic.getOrDefault(member, 0);
    }

    public int getDamageHealed(IBattleMember member) {
        return healStatistic.getOrDefault(member, 0);
    }

    public ListProperty<IBattleMember> playersProperty() {
        return players;
    }

    public ListProperty<IBattleMember> enemiesProperty() {
        return enemies;
    }

    public IntegerProperty roundProperty() {
        return round;
    }

    public void createPlayer() {
        players.add(new BattleMember(this));
    }

    public void createPlayer(IBattleMember member) {
        players.add(member.cloneMember());
    }

    public void createEnemy() {
        enemies.add(new BattleMember(this));
    }

    public void createEnemy(IBattleMember member) {
        enemies.add(member.cloneMember());
    }

    public void load(File file, boolean enemy) {

        if (file == null) {
            return;
        }

        CopyService copy = new CopyService();
        copy.setFile(file);
        copy.setOnSucceeded(ev1 -> {

            File temp = (File) ev1.getSource().getValue();

            WorkbookService service = new WorkbookService();
            service.setFile(temp);
            service.setOnSucceeded(ev2 -> {
                Workbook wb = (Workbook) ev2.getSource().getValue();

                createMember(wb, enemy);

                try {
                    wb.close();
                    if (!temp.delete()) {
                        throw new IOException("not closed");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            service.start();
        });
        copy.start();
    }

    private void createMember(Workbook wb, boolean enemy) {
        IBattleMember member = new BattleMember(wb);
        if (enemy) {
            enemies.add(member);
        } else {
            players.add(member);
        }
    }

    public void spawnMember(boolean enemy, int level, Characterisation characterisation, Race race,
                            Profession profession, FightingStyle fightingStyle, Specialisation specialisation) {
        GeneratedExtendedBattleMember member = new GeneratedExtendedBattleMember(this, level, characterisation, race,
                profession, fightingStyle, specialisation);

        if (enemy) {
            enemies.add(member);
        } else {
            players.add(member);
        }
    }


    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getBattleID() {
        return battleID;
    }
}
