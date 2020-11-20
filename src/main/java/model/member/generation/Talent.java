package model.member.generation;

import model.member.ExtendedBattleMember;

import java.util.Collection;
import java.util.HashSet;

public enum Talent {
    fist, oneHandBlade, oneHandBlunt, pole, twoHandBlunt, twoHandBlade,
    small_throwing, big_throwing, improvised,
    bow_crossbow, gun,
    arkan, illusion, light, fire, darkness, water, air, earth, storm, ice, nature, death,
    dodge, instinct, disarming, parry, block,
    run, roll, jump, swim, fly, ride, throwing, aim, work, unleash,
    knowledge;

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

            case small_throwing:
                return "Kleine Wurfwaffe";
            case big_throwing:
                return "Große Wurfwaffe";
            case improvised:
                return "Improvisierte Waffe";

            case bow_crossbow:
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

            case dodge:
                return "Ausweichen";
            case instinct:
                return "Gefahreninstinkt";
            case disarming:
                return "Entwaffnen";
            case parry:
                return "Parieren";
            case block:
                return "Blocken";

            case run:
                return "Laufen";
            case roll:
                return "Abrollen";
            case jump:
                return "Springen";
            case swim:
                return "Schwimmen";
            case fly:
                return "Fliegen";
            case ride:
                return "Reiten";
            case throwing:
                return "Werfen";
            case aim:
                return "Zielen";
            case work:
                return "Körperliche Arbeit";
            case unleash:
                return "Entfesseln";
            case knowledge:
                return "Magisches Wissen";
        }

        return "Sonstiges";
    }

    public PrimaryAttribute[] getNeededAttributes(ExtendedBattleMember member) {
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

            case small_throwing:
                return new PrimaryAttribute[]{PrimaryAttribute.precision, PrimaryAttribute.precision, PrimaryAttribute.dexterity};
            case big_throwing:
                return new PrimaryAttribute[]{PrimaryAttribute.precision, PrimaryAttribute.precision, PrimaryAttribute.strength};
            case improvised:
                return member.getImprovisedAttributes();

            case bow_crossbow:
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

            case dodge:
                return new PrimaryAttribute[]{PrimaryAttribute.maneuverability, PrimaryAttribute.maneuverability, PrimaryAttribute.endurance};
            case instinct:
                if (member.isMage()) {
                    return new PrimaryAttribute[]{PrimaryAttribute.charisma, PrimaryAttribute.charisma, PrimaryAttribute.precision};
                } else {
                    return new PrimaryAttribute[]{PrimaryAttribute.strength, PrimaryAttribute.strength, PrimaryAttribute.precision};
                }
            case disarming:
                return new PrimaryAttribute[]{PrimaryAttribute.dexterity, PrimaryAttribute.dexterity, PrimaryAttribute.resilience};
            case parry:
                return new PrimaryAttribute[]{PrimaryAttribute.strength, PrimaryAttribute.dexterity, PrimaryAttribute.endurance};
            case block:
                return new PrimaryAttribute[]{PrimaryAttribute.endurance, PrimaryAttribute.resilience, PrimaryAttribute.resilience};

            case run:
                return new PrimaryAttribute[]{PrimaryAttribute.endurance, PrimaryAttribute.endurance, PrimaryAttribute.maneuverability};
            case roll:
                return new PrimaryAttribute[]{PrimaryAttribute.dexterity, PrimaryAttribute.resilience, PrimaryAttribute.maneuverability};
            case jump:
                return new PrimaryAttribute[]{PrimaryAttribute.strength, PrimaryAttribute.dexterity, PrimaryAttribute.maneuverability};
            case swim:
                return new PrimaryAttribute[]{PrimaryAttribute.strength, PrimaryAttribute.endurance, PrimaryAttribute.maneuverability};
            case fly:
                return new PrimaryAttribute[]{PrimaryAttribute.strength, PrimaryAttribute.endurance, PrimaryAttribute.dexterity};
            case ride:
                return new PrimaryAttribute[]{PrimaryAttribute.endurance, PrimaryAttribute.dexterity, PrimaryAttribute.maneuverability};
            case throwing:
                return new PrimaryAttribute[]{PrimaryAttribute.precision, PrimaryAttribute.precision, PrimaryAttribute.strength};
            case aim:
                return new PrimaryAttribute[]{PrimaryAttribute.precision, PrimaryAttribute.precision, PrimaryAttribute.dexterity};
            case work:
                return new PrimaryAttribute[]{PrimaryAttribute.strength, PrimaryAttribute.strength, PrimaryAttribute.endurance};
            case unleash:
                return new PrimaryAttribute[]{PrimaryAttribute.strength, PrimaryAttribute.maneuverability, PrimaryAttribute.dexterity};
            case knowledge:
                return new PrimaryAttribute[]{PrimaryAttribute.intelligence, PrimaryAttribute.intelligence, PrimaryAttribute.charisma};
        }

        return new PrimaryAttribute[]{};
    }

    public boolean isMagicTalent() {
        return getMagicTalents().contains(this);
    }

    public static Collection<Talent> getMagicTalents() {
        Collection<Talent> collection = new HashSet<>();
        collection.add(arkan);
        collection.add(illusion);
        collection.add(light);
        collection.add(fire);
        collection.add(darkness);
        collection.add(water);
        collection.add(air);
        collection.add(earth);
        collection.add(storm);
        collection.add(ice);
        collection.add(nature);
        collection.add(death);
        collection.add(knowledge);
        return collection;
    }

    public static Collection<Talent> getPureWeaponTalents() {
        Collection<Talent> collection = new HashSet<>();
        collection.add(oneHandBlade);
        collection.add(twoHandBlade);
        collection.add(oneHandBlunt);
        collection.add(twoHandBlunt);
        collection.add(pole);
        collection.add(small_throwing);
        collection.add(big_throwing);
        collection.add(bow_crossbow);
        collection.add(gun);
        collection.add(parry);
        collection.add(block);
        return collection;
    }
}
