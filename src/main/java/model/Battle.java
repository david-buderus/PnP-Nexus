package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import manager.CopyService;
import manager.WorkbookService;
import model.loot.LootTable;
import model.member.BattleMember;
import model.member.ExtendedBattleMember;
import model.member.data.ArmorPiece;
import model.member.generation.SpecificType;
import model.member.generation.characterisation.Characterisation;
import model.member.generation.fightingtype.FightingType;
import model.member.generation.profession.Profession;
import model.member.generation.race.Race;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class Battle {

    private final IntegerProperty round;
    private final ListProperty<BattleMember> players;
    private final ListProperty<BattleMember> enemies;
    private final HashMap<BattleMember, Integer> damageStatistic;
    private final HashMap<BattleMember, Integer> healStatistic;
    private final Random random;

    public Battle() {
        this.players = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.enemies = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.damageStatistic = new HashMap<>();
        this.healStatistic = new HashMap<>();
        this.round = new SimpleIntegerProperty(1);
        this.random = new Random();
    }

    public void nextTurn() {
        for (BattleMember member : players) {
            member.nextTurn();
        }
        for (BattleMember member : enemies) {
            member.nextTurn();
        }
        round.set(round.get() + 1);
    }

    public void reset() {
        for (BattleMember member : players) {
            member.reset();
        }
        enemies.clear();
        round.set(0);
        damageStatistic.clear();
        healStatistic.clear();
    }

    public void removeMember(BattleMember member) {
        players.remove(member);
        enemies.remove(member);
    }

    public boolean isPlayer(BattleMember member) {
        return players.contains(member);
    }

    public double getAveragePlayerLevel() {
        return players.stream().mapToDouble(BattleMember::getLevel).average().orElse(1);
    }

    public boolean isSameTeam(BattleMember... members) {
        boolean allPlayers = true;
        boolean allEnemies = true;

        for (BattleMember member : members) {
            if (isPlayer(member)) {
                allEnemies = false;
            } else {
                allPlayers = false;
            }
        }

        return allPlayers || allEnemies;
    }

    public void addToDamageStatistic(BattleMember member, int damage) {
        if (!damageStatistic.containsKey(member)) {
            damageStatistic.put(member, 0);
        }
        damageStatistic.put(member, damageStatistic.get(member) + damage);
    }

    public void addToHealStatistic(BattleMember member, int heal) {
        if (!healStatistic.containsKey(member)) {
            healStatistic.put(member, 0);
        }
        healStatistic.put(member, healStatistic.get(member) + heal);
    }

    public int getDamageDealt(BattleMember member) {
        return damageStatistic.getOrDefault(member, 0);
    }

    public int getDamageHealed(BattleMember member) {
        return healStatistic.getOrDefault(member, 0);
    }

    public ListProperty<BattleMember> playersProperty() {
        return players;
    }

    public ListProperty<BattleMember> enemiesProperty() {
        return enemies;
    }

    public IntegerProperty roundProperty() {
        return round;
    }

    public void createPlayer() {
        players.add(new BattleMember(this));
    }

    public void createPlayer(BattleMember member) {
        players.add(member.cloneMember());
    }

    public void createEnemy() {
        enemies.add(new BattleMember(this));
    }

    public void createEnemy(BattleMember member) {
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
        LootTable lootTable = new LootTable();
        Sheet loot = wb.getSheet("Loot");

        for (Row row : loot) {
            if (row.getRowNum() > 0) {
                String name = this.getValue(row, 0);

                if (name.isEmpty() || name.equals("0")) {
                    continue;
                }

                int amount = (int) row.getCell(1).getNumericCellValue();
                double chance = row.getCell(2).getNumericCellValue();

                lootTable.add(name, amount, chance);
            }
        }

        Sheet character = wb.getSheet("Gegnerbogen");
        BattleMember member = new BattleMember(this, lootTable);

        member.setName(this.getStringInCell(character, "B", 57));
        member.setMaxLife(this.getIntegerInCell(character, "B", 58));
        member.setMaxMana(this.getIntegerInCell(character, "B", 59));
        member.setInitiative(this.getIntegerInCell(character, "B", 60));
        member.setLevel(this.getIntegerInCell(character, "B", 63));

        // Protection
        member.setArmor(ArmorPiece.head, this.getIntegerInCell(character, "D", 29));
        member.setArmor(ArmorPiece.upperBody, this.getIntegerInCell(character, "D", 31));
        member.setArmor(ArmorPiece.legs, this.getIntegerInCell(character, "D", 35));
        member.setArmor(ArmorPiece.arm, this.getIntegerInCell(character, "D", 32));

        if (this.getIntegerInCell(character, "C", 60) == 2) {
            member.setArmor(ArmorPiece.shield, this.getIntegerInCell(character, "B", 62));
        }

        member.setDefense(this.getIntegerInCell(character, "B", 61));

        if (enemy) {
            enemies.add(member);
        } else {
            players.add(member);
        }
    }

    public void spawnMember(boolean enemy, int level, Characterisation characterisation, Race race,
                            Profession profession, FightingType fightingType, SpecificType specificType) {
        ExtendedBattleMember member = new ExtendedBattleMember(this, level, characterisation, race,
                profession, fightingType, specificType);

        if (enemy) {
            enemies.add(member);
        } else {
            players.add(member);
        }
    }

    protected String getValue(Row row, int i) {
        try {
            return row.getCell(i).getStringCellValue();
        } catch (Exception e) {
            return "";
        }
    }

    protected int getIntegerInCell(Sheet sheet, String s, int pos) {
        try {
            return (int) getCell(sheet, s, pos).getNumericCellValue();
        } catch (Exception e) {
            return 0;
        }
    }

    protected String getStringInCell(Sheet sheet, String s, int pos) {
        try {
            return getCell(sheet, s, pos).getStringCellValue();
        } catch (Exception e) {
            return "";
        }
    }

    protected Cell getCell(Sheet sheet, String s, int pos) {
        return sheet.getRow(pos - 1).getCell(CellReference.convertColStringToIndex(s));
    }
}
