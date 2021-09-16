package de.pnp.manager.model;

import de.pnp.manager.main.CopyService;
import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.main.WorkbookService;
import de.pnp.manager.model.manager.BattleHandler;
import de.pnp.manager.model.manager.CharacterHandler;
import de.pnp.manager.model.manager.PnPCharacterProducer;
import de.pnp.manager.model.character.GeneratedCharacter;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.character.generation.specs.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Battle {

    private final String battleID;
    private final String sessionID;
    private final CharacterHandler characterHandler;
    private final StringProperty name;
    private final IntegerProperty round;
    private final ListProperty<PnPCharacter> players;
    private final ListProperty<PnPCharacter> enemies;
    private final HashMap<PnPCharacter, Integer> damageStatistic;
    private final HashMap<PnPCharacter, Integer> healStatistic;

    /**
     * Don't create a battle this way.
     * Use {@link BattleHandler#createBattle(String)}.
     */
    public Battle(String battleID, String sessionID, CharacterHandler characterHandler) {
        this.battleID = battleID;
        this.sessionID = sessionID;
        this.characterHandler = characterHandler;
        this.players = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.enemies = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.damageStatistic = new HashMap<>();
        this.healStatistic = new HashMap<>();
        this.round = new SimpleIntegerProperty(1);
        this.name = new SimpleStringProperty(LanguageUtility.getMessage("battle.defaultName"));
    }

    public void nextTurn() {
        for (PnPCharacter character : players) {
            character.nextTurn();
        }
        for (PnPCharacter character : enemies) {
            character.nextTurn();
        }
        round.set(round.get() + 1);
    }

    public void reset() {
        for (PnPCharacter character : players) {
            character.reset();
        }
        enemies.clear();
        round.set(0);
        damageStatistic.clear();
        healStatistic.clear();
    }

    public void removeMember(PnPCharacter character) {
        players.remove(character);
        enemies.remove(character);
    }

    public boolean isPlayer(PnPCharacter character) {
        return players.contains(character);
    }

    public double getAveragePlayerLevel() {
        return players.stream().mapToDouble(PnPCharacter::getLevel).average().orElse(1);
    }

    public boolean isSameTeam(PnPCharacter... characters) {
        boolean allPlayers = true;
        boolean allEnemies = true;

        for (PnPCharacter character : characters) {
            if (isPlayer(character)) {
                allEnemies = false;
            } else {
                allPlayers = false;
            }
        }

        return allPlayers || allEnemies;
    }

    public void addToDamageStatistic(PnPCharacter character, int damage) {
        if (!damageStatistic.containsKey(character)) {
            damageStatistic.put(character, 0);
        }
        damageStatistic.put(character, damageStatistic.get(character) + damage);
    }

    public void addToHealStatistic(PnPCharacter character, int heal) {
        if (!healStatistic.containsKey(character)) {
            healStatistic.put(character, 0);
        }
        healStatistic.put(character, healStatistic.get(character) + heal);
    }

    public int getDamageDealt(PnPCharacter character) {
        return damageStatistic.getOrDefault(character, 0);
    }

    public int getDamageHealed(PnPCharacter character) {
        return healStatistic.getOrDefault(character, 0);
    }

    public ListProperty<PnPCharacter> playersProperty() {
        return players;
    }

    public ListProperty<PnPCharacter> enemiesProperty() {
        return enemies;
    }

    public IntegerProperty roundProperty() {
        return round;
    }

    public void createPlayer() {
        players.add(characterHandler.createOneTimeCharacter(this, PnPCharacterProducer.DEFAULT));
    }

    public void createPlayer(PnPCharacter character) {
        //players.add(character.cloneMember());
    }

    public void createEnemy() {
        enemies.add(characterHandler.createOneTimeCharacter(this, PnPCharacterProducer.DEFAULT));
    }

    public void createEnemy(PnPCharacter character) {
        //enemies.add(character.cloneMember());
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
        /*PnPCharacter character = new BattleMember(wb);
        if (enemy) {
            enemies.add(character);
        } else {
            players.add(character);
        }*/
    }

    public void spawnMember(boolean enemy, int level, Characterisation characterisation, Race race,
                            Profession profession, FightingStyle fightingStyle, Specialisation specialisation) {
        GeneratedCharacter character = characterHandler.createOneTimeCharacter(this, (characterID, battle) ->
                new GeneratedCharacter(characterID, battle, level, characterisation, race, profession, fightingStyle, specialisation)
        );

        if (enemy) {
            enemies.add(character);
        } else {
            players.add(character);
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
