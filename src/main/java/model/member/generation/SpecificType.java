package model.member.generation;

import model.loot.LootTable;
import model.member.ExtendedBattleMember;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public enum SpecificType {
    fist, oneHandBlade, oneHandBlunt, pole, twoHandBlunt, twoHandBlade,
    bow, crossbow, gun,
    arkan, illusion, light, fire, darkness, water, air, earth, storm, ice, nature, death;

    public static Random rand = new Random();

    @Override
    public String toString() {
        switch (this) {
            case fist:
                return "Faustkampf";
            case oneHandBlade:
                return "Klingenwaffe Einhändig";
            case twoHandBlade:
                return "Klingenwaffe Zweihändig";
            case pole:
                return "Stangenwaffe";
            case oneHandBlunt:
                return "Stumpfe Waffe Einhändig";
            case twoHandBlunt:
                return "Stumpfe Waffe Zweihändig";

            case bow:
                return "Bogen";
            case crossbow:
                return "Armbrust";
            case gun:
                return "Gewehr";

            case arkan:
                return "Arkanmagie";
            case illusion:
                return "Illusionsmagie";
            case light:
                return "Lichtmagie";
            case fire:
                return "Feuermagie";
            case darkness:
                return "Finstermagie";
            case water:
                return "Wassermagie";
            case air:
                return "Luftmagie";
            case earth:
                return "Erdmagie";
            case storm:
                return "Sturmmagie";
            case ice:
                return "Frostmagie";
            case nature:
                return "Naturmagie";
            case death:
                return "Totenmagie";
        }

        return "Sonstiges";
    }

    public PrimaryAttribute[] getNeededAttributes() {
        switch (this) {
            case fist:
                return new PrimaryAttribute[]{PrimaryAttribute.strength, PrimaryAttribute.strength, PrimaryAttribute.maneuverability};
            case oneHandBlade:
                return new PrimaryAttribute[]{PrimaryAttribute.strength, PrimaryAttribute.maneuverability, PrimaryAttribute.dexterity};
            case oneHandBlunt:
                return new PrimaryAttribute[]{PrimaryAttribute.strength, PrimaryAttribute.strength, PrimaryAttribute.dexterity};
            case pole:
                return new PrimaryAttribute[]{PrimaryAttribute.strength, PrimaryAttribute.maneuverability, PrimaryAttribute.dexterity};
            case twoHandBlunt:
                return new PrimaryAttribute[]{PrimaryAttribute.strength, PrimaryAttribute.strength, PrimaryAttribute.resilience};
            case twoHandBlade:
                return new PrimaryAttribute[]{PrimaryAttribute.strength, PrimaryAttribute.strength, PrimaryAttribute.dexterity};

            case bow:
                return new PrimaryAttribute[]{PrimaryAttribute.dexterity, PrimaryAttribute.dexterity, PrimaryAttribute.precision};
            case crossbow:
                return new PrimaryAttribute[]{PrimaryAttribute.dexterity, PrimaryAttribute.dexterity, PrimaryAttribute.precision};
            case gun:
                return new PrimaryAttribute[]{PrimaryAttribute.precision, PrimaryAttribute.dexterity, PrimaryAttribute.endurance};

            case arkan:
                return new PrimaryAttribute[]{PrimaryAttribute.intelligence, PrimaryAttribute.intelligence, PrimaryAttribute.precision};
            case illusion:
                return new PrimaryAttribute[]{PrimaryAttribute.intelligence, PrimaryAttribute.charisma, PrimaryAttribute.charisma};
            case light:
                return new PrimaryAttribute[]{PrimaryAttribute.intelligence, PrimaryAttribute.charisma, PrimaryAttribute.precision};
            case fire:
                return new PrimaryAttribute[]{PrimaryAttribute.charisma, PrimaryAttribute.charisma, PrimaryAttribute.dexterity};
            case darkness:
                return new PrimaryAttribute[]{PrimaryAttribute.intelligence, PrimaryAttribute.charisma, PrimaryAttribute.resilience};
            case water:
                return new PrimaryAttribute[]{PrimaryAttribute.intelligence, PrimaryAttribute.intelligence, PrimaryAttribute.dexterity};
            case air:
                return new PrimaryAttribute[]{PrimaryAttribute.intelligence, PrimaryAttribute.precision, PrimaryAttribute.precision};
            case earth:
                return new PrimaryAttribute[]{PrimaryAttribute.strength, PrimaryAttribute.resilience, PrimaryAttribute.resilience};
            case storm:
                return new PrimaryAttribute[]{PrimaryAttribute.intelligence, PrimaryAttribute.charisma, PrimaryAttribute.dexterity};
            case ice:
                return new PrimaryAttribute[]{PrimaryAttribute.intelligence, PrimaryAttribute.dexterity, PrimaryAttribute.dexterity};
            case nature:
                return new PrimaryAttribute[]{PrimaryAttribute.intelligence, PrimaryAttribute.charisma, PrimaryAttribute.precision};
            case death:
                return new PrimaryAttribute[]{PrimaryAttribute.charisma, PrimaryAttribute.resilience, PrimaryAttribute.resilience};
        }

        return new PrimaryAttribute[]{};
    }

    public boolean canUseShield(){
        switch (this){
            case oneHandBlade:
            case oneHandBlunt:
            case pole:
                return true;
            default:
                return false;
        }
    }

    public String[] getConcreteFirsthand(){
        switch (this){
            case fist:
                return new String[]{"Faustwaffe", "Klaue"};
            case oneHandBlade:
                return new String[]{"Dolch", "Einhand-Axt", "Einhand-Schwert"};
            case oneHandBlunt:
                return new String[]{"Einhand-Keule"};
            case pole:
                return new String[]{"Stangenwaffe"};
            case twoHandBlunt:
                return new String[]{"Zweihand-Keule", "Kriegshammer"};
            case twoHandBlade:
                return new String[]{"Zweihand-Axt", "Zweihand-Schwert"};
            case bow:
                return new String[]{"Bogen"};
            case crossbow:
                return new String[]{"Armbrust"};
            case gun:
                return new String[]{"Gewehr"};
            case arkan:
            case illusion:
            case light:
            case fire:
            case darkness:
            case water:
            case air:
            case earth:
            case storm:
            case ice:
            case nature:
            case death:
                return new String[]{"Stab"};
        }

        return new String[]{};
    }

    public String[] getConcreteSecondhand(boolean usesShield){
        if(usesShield){
            return new String[]{"Großschild", "Schild"};
        }

        switch (this){
            case fist:
                return new String[]{"Faustwaffe", "Klaue"};
            case oneHandBlade:
                return new String[]{"Dolch", "Einhand-Axt", "Einhand-Schwert"};
            case oneHandBlunt:
                return new String[]{"Einhand-Keule"};
            case pole:
            case twoHandBlunt:
            case twoHandBlade:
            case bow:
            case crossbow:
            case gun:
                return new String[]{};
            case arkan:
            case illusion:
            case light:
            case fire:
            case darkness:
            case water:
            case air:
            case earth:
            case storm:
            case ice:
            case nature:
            case death:
                return new String[]{"Zauberbuch"};
        }

        return new String[]{};
    }

    public Collection<Talent> getMainTalents(boolean usesShield){
        ArrayList<Talent> talents = new ArrayList<>();

        switch (this){
            case fist:
                talents.add(Talent.fist);
                talents.add(Talent.dodge);
                break;
            case oneHandBlade:
                talents.add(Talent.oneHandBlade);
                talents.add(Talent.parry);
                break;
            case oneHandBlunt:
                talents.add(Talent.oneHandBlunt);
                talents.add(Talent.parry);
                break;
            case pole:
                talents.add(Talent.pole);
                talents.add(Talent.parry);
                break;
            case twoHandBlunt:
                talents.add(Talent.twoHandBlunt);
                talents.add(Talent.dodge);
                break;
            case twoHandBlade:
                talents.add(Talent.twoHandBlade);
                talents.add(Talent.parry);
                break;

            case bow:
            case crossbow:
                talents.add(Talent.bow_crossbow);
                talents.add(Talent.aim);
                talents.add(Talent.dodge);
                break;
            case gun:
                talents.add(Talent.gun);
                talents.add(Talent.aim);
                talents.add(Talent.dodge);
                break;

            case arkan:
                talents.add(Talent.arkan);
                break;
            case illusion:
                talents.add(Talent.illusion);
                break;
            case light:
                talents.add(Talent.light);
                break;
            case fire:
                talents.add(Talent.fire);
                break;
            case darkness:
                talents.add(Talent.darkness);
                break;
            case water:
                talents.add(Talent.water);
                break;
            case air:
                talents.add(Talent.air);
                break;
            case earth:
                talents.add(Talent.earth);
                break;
            case storm:
                talents.add(Talent.storm);
                break;
            case ice:
                talents.add(Talent.ice);
                break;
            case nature:
                talents.add(Talent.nature);
                break;
            case death:
                talents.add(Talent.death);
                break;
        }

        if(usesShield){
            talents.add(Talent.block);
        }

        return talents;
    }

    public Talent[] getForbiddenTalents(){
        return new Talent[]{};
    }

    public LootTable getLootTable(ExtendedBattleMember member){
        LootTable lootTable =  new LootTable();

        switch (this){
            case bow:
                lootTable.add(getMetal(member.getTier()) + "pfeil", 10, 0.25);
                lootTable.add("Silberpfeil", 10, 0.02);
                break;
            case crossbow:
                lootTable.add(getMetal(member.getTier()) + "bolzen", 10, 0.25);
                lootTable.add("Silberbolzen", 10, 0.02);
                break;
            case gun:
                lootTable.add(getMetal(member.getTier()) + "kugel", 10, 0.25);
                lootTable.add("Silberkugel", 10, 0.02);
                break;
            case oneHandBlade:
            case twoHandBlade:
                switch (member.getTier()){
                    case 4:
                        lootTable.add("exzellenter Schleifstein", 1, 0.04);
                    case 3:
                        lootTable.add("exquisiter Schleifstein", 1, 0.04);
                    case 2:
                        lootTable.add("Schleifstein", 1, 0.04);
                        break;
                }
        }

        return lootTable;
    }

    private String getMetal(int tier){
        switch (tier){
            case 1:
                return "Eisen";
            case 2:
                return "Stahl";
            case 3:
                return "Mithril";
            case 4:
                return "Orichalum";
            case 5:
                return "Adamantium";
        }
        return "Holz";
    }
}
