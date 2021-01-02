package model.member;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import manager.Database;
import manager.Utility;
import model.Battle;
import model.Spell;
import model.item.*;
import model.member.data.ArmorPiece;
import model.member.generation.*;
import model.member.generation.specs.*;
import org.apache.commons.lang.math.NumberUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class ExtendedBattleMember extends BattleMember {

    protected static Random random = new Random();

    protected IntegerProperty strength;
    protected IntegerProperty endurance;
    protected IntegerProperty dexterity;
    protected IntegerProperty intelligence;
    protected IntegerProperty charisma;
    protected IntegerProperty resilience;
    protected IntegerProperty maneuverability;
    protected IntegerProperty precision;

    protected ReadOnlyIntegerProperty baseDamageMelee;
    protected ReadOnlyIntegerProperty baseDamageRange;
    protected ReadOnlyIntegerProperty magicPower;
    protected ReadOnlyIntegerProperty mentalHealth;

    protected IntegerProperty baseDamageMeleeModifier;
    protected IntegerProperty baseDamageRangeModifier;
    protected IntegerProperty magicPowerModifier;
    protected IntegerProperty defenseModifier;
    protected IntegerProperty initModifier;
    protected IntegerProperty maxLifeModifier;
    protected IntegerProperty mentalHealthModifier;
    protected IntegerProperty maxManaModifier;

    protected StringProperty notes;

    protected Characterisation characterisation;
    protected Race race;
    protected Profession profession;
    protected FightingStyle fightingStyle;
    protected Specialisation specialisation;

    protected HashMap<Talent, IntegerProperty> talents;
    protected ObservableList<Weapon> weapons;
    protected ObservableList<Armor> armors;
    protected ObservableList<Jewellery> jewellery;
    protected ObservableList<Spell> spells;

    protected boolean usesShield;

    public ExtendedBattleMember(Battle battle, int level,
                                Characterisation characterisation, Race race, Profession profession,
                                FightingStyle fightingStyle, Specialisation specialisation) {
        super(battle);
        this.characterisation = characterisation;
        this.race = race;
        this.profession = profession;
        this.fightingStyle = fightingStyle;
        this.specialisation = specialisation;
        this.usesShield = fightingStyle.usesAlwaysShield() || (specialisation.isAbleToUseShield() && random.nextBoolean());
        this.setLevel(level);
        this.setName(profession + " - " + specialisation);

        this.strength = new SimpleIntegerProperty(2);
        this.endurance = new SimpleIntegerProperty(2);
        this.dexterity = new SimpleIntegerProperty(2);
        this.intelligence = new SimpleIntegerProperty(2);
        this.charisma = new SimpleIntegerProperty(2);
        this.resilience = new SimpleIntegerProperty(2);
        this.maneuverability = new SimpleIntegerProperty(2);
        this.precision = new SimpleIntegerProperty(2);

        this.baseDamageMeleeModifier = new SimpleIntegerProperty();
        this.baseDamageRangeModifier = new SimpleIntegerProperty();
        this.magicPowerModifier = new SimpleIntegerProperty();
        this.defenseModifier = new SimpleIntegerProperty();
        this.initModifier = new SimpleIntegerProperty();
        this.maxLifeModifier = new SimpleIntegerProperty();
        this.mentalHealthModifier = new SimpleIntegerProperty();
        this.maxManaModifier = new SimpleIntegerProperty();

        this.baseDamageMelee = createProperty(0.18, strength, 0.09, dexterity, 0.03, endurance, baseDamageMeleeModifier);
        this.baseDamageRange = createProperty(0.18, precision, 0.09, dexterity, 0.03, maneuverability, baseDamageRangeModifier);
        this.magicPower = createProperty(0.18, intelligence, 0.08, charisma, 0.04, precision, magicPowerModifier);
        this.baseDefense = createProperty(0.105, resilience, 0.09, maneuverability, 0.105, endurance, defenseModifier);
        this.initiative = createProperty(0.2, maneuverability, 0.16, precision, 0.16, charisma, initModifier, 2);
        this.maxLife = createProperty(4.5, resilience, 0.9, endurance, 0.4, strength, maxLifeModifier);
        this.mentalHealth = createProperty(0.9, resilience, 0.4, intelligence, 0.2, charisma, mentalHealthModifier, 7);
        this.maxMana = createProperty(4.2, intelligence, 1.6, charisma, maxManaModifier, 10);

        this.notes = new SimpleStringProperty("");

        this.talents = new HashMap<>();
        for (Talent talent : Database.talentList) {
            talents.put(talent, new SimpleIntegerProperty(0));
        }

        this.generateStats();

        //Prepare WeaponList
        this.weapons = FXCollections.observableArrayList();
        this.weapons.addListener((ListChangeListener<? super Weapon>) ob -> {
            while (ob.next()) {
                ObservableList<? extends Weapon> list = ob.getList();
                DecimalFormat format = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.GERMANY);

                try {
                    if (list.size() == 1) {
                        double init = Math.ceil(format.parse(list.get(0).getInitiative()).doubleValue());
                        this.initModifier.set((int) init);
                    }
                    if (list.size() == 2) {
                        double init1 = format.parse(list.get(0).getInitiative()).doubleValue();
                        double init2 = format.parse(list.get(1).getInitiative()).doubleValue();
                        this.initModifier.set(
                                (int) Math.min(Math.min(Math.floor(init1), Math.floor(init2)), Math.floor(init1 + init2))
                        );

                        if (usesShield) {
                            this.setArmor(ArmorPiece.shield, list.get(1).getDamage());
                        }
                    }
                } catch (NumberFormatException | ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        //Generate Weapons
        if (usesFirstWeapon()) {
            Collection<Weapon> concreteFirstWeapons = getSpecificPrimaryWeapons();
            Weapon firstHand;
            if (concreteFirstWeapons.isEmpty()) {
                firstHand = randomWeapon(getPrimaryWeaponTypes(), Database.weaponList);
            } else {
                firstHand = randomWeapon(
                        concreteFirstWeapons.stream().map(Item::getSubTyp).collect(Collectors.toSet()),
                        concreteFirstWeapons);
            }
            this.weapons.add((Weapon) firstHand.getWithUpgrade());
        }

        if (usesSecondWeapon()) {
            Collection<Weapon> concreteSecondWeapons = getSpecificSecondaryWeapons();
            if (concreteSecondWeapons.isEmpty()) {
                Collection<String> secondTypes = usesShield ? Database.shieldTypes : getSecondaryWeaponTypes();
                if (secondTypes.size() > 0) {
                    this.weapons.add((Weapon) randomWeapon(secondTypes, Database.weaponList).getWithUpgrade());
                }
            } else {
                this.weapons.add((Weapon) randomWeapon(
                        //Allow all types mentioned in concreteSecondWeapons
                        concreteSecondWeapons.stream().map(Item::getSubTyp).collect(Collectors.toSet()),
                        concreteSecondWeapons).getWithUpgrade());
            }
        }

        if (dropsWeapons()) {
            for (Equipment equip : weapons) {
                if (!equip.getName().isEmpty()) {
                    this.lootTable.add(equip, 1, 1);
                }
            }
        }

        //Prepare ArmorList
        this.armors = FXCollections.observableArrayList();
        this.armors.addListener((ListChangeListener<? super Armor>) ob -> {
            while (ob.next()) {
                ObservableList<? extends Armor> list = ob.getList();

                for (Armor armor : list) {
                    if (ArmorPiece.getArmorPiece(armor.getSubTyp()) == null) {
                        continue;
                    }

                    this.setArmor(ArmorPiece.getArmorPiece(armor.getSubTyp()), armor.getProtection());
                }
            }
        });

        //Generate Armor
        for (ArmorPosition position : ArmorPosition.values()) {
            this.generateArmor(position);
        }

        if (dropsArmor()) {
            for (Equipment equip : armors) {
                if (!equip.getName().isEmpty()) {
                    this.lootTable.add(equip, 1, 1);
                }
            }
        }

        Collection<Jewellery> jewelleryPool;
        Collection<Jewellery> concreteJewellery = Collections.emptyList(); // TODO
        if (concreteJewellery.isEmpty()) {
            jewelleryPool = Database.jewelleryList;
        } else {
            jewelleryPool = concreteJewellery;
        }
        this.jewellery = FXCollections.observableArrayList();
        if (usesJewellery()) {
            for (int i = 0; i < 8; i++) {
                if (random.nextDouble() < getTier() / 100f) {
                    this.jewellery.add((Jewellery) randomJewellery("Ring", jewelleryPool).getWithUpgrade());
                }
            }
            for (int i = 0; i < 8; i++) {
                if (random.nextDouble() < getTier() / 100f) {
                    this.jewellery.add((Jewellery) randomJewellery("Armband", jewelleryPool).getWithUpgrade());
                }
            }
            if (random.nextDouble() < getTier() / 100f) {
                this.jewellery.add((Jewellery) randomJewellery("Kette", jewelleryPool).getWithUpgrade());
            }
        }

        if (dropsJewellery()) {
            for (Equipment equip : jewellery) {
                if (!equip.getName().isEmpty()) {
                    this.lootTable.add(equip, 1, 1);
                }
            }
        }

        this.useSkillPoints();
        this.spells = FXCollections.observableArrayList();
        this.generateSpells();

        this.setLife(this.getMaxLife());
        this.setMana(this.getMaxMana());

        this.generateLoot();
        this.addDescription();
    }

    private void generateLoot() {
        this.lootTable.add(characterisation.getLootTable(this));
        this.lootTable.add(race.getLootTable(this));
        this.lootTable.add(profession.getLootTable(this));
        this.lootTable.add(fightingStyle.getLootTable(this));
        this.lootTable.add(specialisation.getLootTable(this));
    }

    private void addDescription() {
        addDescription("Charakterisierungsvorteile", characterisation.getAdvantages());
        addDescription("Rassenvorteile", race.getAdvantages());
        addDescription("Berufsvorteile", profession.getAdvantages());
        addDescription("Kampfvorteile", fightingStyle.getAdvantages());
        addDescription("Spezifische Vorteile", specialisation.getAdvantages());

        addDescription("Charakterisierungsnachteile", characterisation.getDisadvantages());
        addDescription("Rassennachteile", race.getDisadvantages());
        addDescription("Berufsnachteile", profession.getDisadvantages());
        addDescription("Kampfnachteile", fightingStyle.getDisadvantages());
        addDescription("Spezifische Nachteile", specialisation.getDisadvantages());
    }

    private void addDescription(String header, Collection<String> lines) {
        if (!lines.isEmpty()) {
            notes.set(notes.get() + header + "\n");
            for (String line : lines) {
                notes.set(notes.get() + "  - " + line + "\n");
            }
        }
    }

    private void generateArmor(ArmorPosition position) {
        if (usesArmor(position)) {
            Collection<Armor> concreteArmor = getSpecificArmor(position);
            Collection<Armor> armorPool;
            if (concreteArmor.isEmpty()) {
                armorPool = Database.armorList;
            } else {
                armorPool = concreteArmor;
            }
            this.armors.add((Armor) randomArmor(position.toString(), armorPool).getWithUpgrade());
        }
    }

    private void useSkillPoints() {
        int skillPoints = getLevel();
        int talentPoints = 13; //Smaller character sheet, so less points
        Collection<SecondaryAttribute> secondaryAttributes = getSecondaryAttributes();

        while (skillPoints > 0) {
            if (random.nextDouble() < 0.25 && secondaryAttributes.size() > 0) { //25% chance to buff a secondary attribute
                SecondaryAttribute attribute = secondaryAttributes.stream()
                        .skip(random.nextInt(secondaryAttributes.size())).findFirst().orElse(null);

                if (attribute == null) {
                    continue;
                }

                IntegerProperty modifier = getModifier(attribute);

                switch (attribute) {
                    case meleeDamage:
                    case rangeDamage:
                    case magicPower:
                    case defense:
                        if (skillPoints > 4) {
                            modifier.set(modifier.get() + 1);
                            skillPoints -= 5;
                        }
                        break;
                    case health:
                    case mana:
                        modifier.set(modifier.get() + 1);
                        skillPoints -= 1;
                        break;
                }
            } else { //Use point to buff a talent
                skillPoints -= 1;
                talentPoints += 1;
            }
        }

        //Skill first maximal points into main talents
        for (Talent talent : getMainTalents()) {
            int points = Math.min(talentPoints, Math.min(5, Math.max(3, getTier() + 1)));

            talents.get(talent).set(points);
            talentPoints -= points;
        }

        //Use only talents which are allowed and are usable by the member
        Collection<Talent> forbiddenTalents = getForbiddenTalents();
        ArrayList<Talent> usableTalents = Database.talentList.stream()
                .filter(x -> !forbiddenTalents.contains(x))
                .filter(this::checkRequirements)
                .collect(Collectors.toCollection(ArrayList::new));

        int loopCheck = 100; //If there were no talent to skill into, aboard
        while (talentPoints > 0) {
            Talent talent = usableTalents.get(random.nextInt(usableTalents.size()));

            //Dont skill in more than three types of magic
            if (talent.isMagicTalent() && talents.keySet().stream()
                    .filter(Talent::isMagicTalent).filter(x -> talents.get(x).get() > 0).count() > 2) {
                continue;
            }

            int points = Math.min(talentPoints, 1 + random.nextInt(3));
            int oldPoints = talents.get(talent).get();
            int newPoints = Math.max(Math.min(oldPoints + points, getTier() + 1), oldPoints);
            int gain = newPoints - oldPoints;

            talents.get(talent).set(newPoints);
            talentPoints -= gain;

            if (gain > 0) {
                loopCheck = 100;
            } else {
                loopCheck -= 1;
            }
            if (loopCheck < 1) {
                notes.set("Unbenutzte Talentpunkte: " + talentPoints + "\n\n" + notes.get());
                break;
            }
        }
    }

    private void generateSpells() {

        if (isNotAbleToUseSpells()) {
            return;
        }

        Collection<Talent> magicTalents = talents.keySet().stream()
                .filter(Talent::isMagicTalent)
                .filter(x -> talents.get(x).get() > 0).collect(Collectors.toCollection(ArrayList::new));

        if (magicTalents.isEmpty()) {
            return;
        }

        //Only use spells which can be used
        ArrayList<Spell> spells = Database.spellList.stream()
                .filter(x -> x.getTier() <= getTier())
                .filter(x -> magicTalents.contains(x.getTalent())).collect(Collectors.toCollection(ArrayList::new));

        int spellAmount = 2 + getTier();

        //Get a spell of each tier
        for (int i = 1; i <= getTier() && spellAmount > 0; i++) {
            final int k = i;
            ArrayList<Spell> tieredSpells = spells.stream().filter(x -> x.getTier() == k)
                    .filter(x -> getMainTalents().contains(x.getTalent()))
                    .collect(Collectors.toCollection(ArrayList::new));

            if (tieredSpells.size() > 0) {
                Spell spell = tieredSpells.get(random.nextInt(tieredSpells.size()));
                this.spells.add(spell);
                spells.remove(spell);
                spellAmount -= 1;
            }
        }

        //Fill with random matching spells
        while (spellAmount > 0) {
            if (spells.size() > 0) {
                Spell spell = spells.get(random.nextInt(spells.size()));
                this.spells.add(spell);
                spells.remove(spell);
                spellAmount -= 1;
            } else {
                break;
            }
        }
    }

    private Weapon randomWeapon(Collection<String> types, Collection<Weapon> weaponPool) {
        ArrayList<Weapon> weapons = new ArrayList<>();
        for (int i = getTier(); i > 0 && weapons.size() == 0; i--) {
            int k = i;
            String rarity = Utility.getRandomRarity();
            weapons = weaponPool.stream()
                    .filter(x -> x.getTier() == k)
                    .filter(x -> x.getRarity().equals(rarity))
                    .filter(this::checkRequirements)
                    .filter(x -> types.stream().anyMatch(y -> x.getSubTyp().equals(y)))
                    .collect(Collectors.toCollection(ArrayList::new));

            if (weapons.size() == 0) {
                weapons = weaponPool.stream()
                        .filter(x -> x.getTier() == k)
                        .filter(x -> x.getRarity().equals("gewöhnlich"))
                        .filter(this::checkRequirements)
                        .filter(x -> types.stream().anyMatch(y -> x.getSubTyp().equals(y)))
                        .collect(Collectors.toCollection(ArrayList::new));
            }
        }

        Weapon weapon;
        if (weapons.size() == 0) {
            weapon = new Weapon();
        } else {
            weapon = weapons.get(random.nextInt(weapons.size()));
        }
        return weapon;
    }

    private Armor randomArmor(final String position, Collection<Armor> armorPool) {
        ArrayList<Armor> armors = equipmentSearch(position, armorPool);

        Armor armor;
        if (armors.size() == 0) {
            armor = new Armor();
        } else {
            armor = armors.get(random.nextInt(armors.size()));
        }
        return armor;
    }

    protected boolean checkRequirements(final Talent talent) {
        int counter = 3;

        for (PrimaryAttribute attribute : talent.getAttributes()) {
            if (getAttribute(attribute).get() < 7) {
                counter--;
            }
        }

        return counter > 1;
    }

    protected boolean checkRequirements(final Item item) {
        String[] parsed = item.getRequirement().split(" ");

        for (int i = 0; i < parsed.length; i += 2) {
            if (NumberUtils.isDigits(parsed[i])) {
                if (i + 1 >= parsed.length) {
                    return false;
                }
                int x = Integer.parseInt(parsed[i]);

                switch (parsed[i + 1]) {
                    case "KK":
                        if (x > getStrength()) {
                            return false;
                        }
                        break;
                    case "AU":
                        if (x > getEndurance()) {
                            return false;
                        }
                        break;
                    case "GE":
                        if (x > getDexterity()) {
                            return false;
                        }
                        break;
                    case "IN":
                        if (x > getIntelligence()) {
                            return false;
                        }
                        break;
                    case "CH":
                        if (x > getCharisma()) {
                            return false;
                        }
                        break;
                    case "BL":
                        if (x > getResilience()) {
                            return false;
                        }
                        break;
                    case "BW":
                        if (x > getManeuverability()) {
                            return false;
                        }
                        break;
                    case "GN":
                        if (x > getPrecision()) {
                            return false;
                        }
                        break;
                }
            }
        }

        return true;
    }

    private Jewellery randomJewellery(final String typ, Collection<Jewellery> jewelleryPool) {
        ArrayList<Jewellery> jewelleries = equipmentSearch(typ, jewelleryPool);

        Jewellery jewellery;
        if (jewelleries.size() == 0) {

            jewellery = new Jewellery();
            jewellery.setName("");
        } else {
            jewellery = jewelleries.get(random.nextInt(jewelleries.size()));
        }
        return jewellery;
    }

    private <Eq extends Equipment> ArrayList<Eq> equipmentSearch(final String typ, Collection<Eq> pool) {
        ArrayList<Eq> equipment = new ArrayList<>();
        for (int i = getTier(); i > 0 && equipment.size() == 0; i--) {
            int k = i;
            String rarity = Utility.getRandomRarity();
            equipment = pool.stream()
                    .filter(x -> x.getTier() == k)
                    .filter(x -> x.getRarity().equals(rarity))
                    .filter(x -> x.getSubTyp().equals(typ))
                    .filter(this::checkRequirements)
                    .collect(Collectors.toCollection(ArrayList::new));

            if (equipment.size() == 0) {
                equipment = pool.stream()
                        .filter(x -> x.getTier() == k)
                        .filter(x -> x.getRarity().equals("gewöhnlich"))
                        .filter(x -> x.getSubTyp().equals(typ))
                        .filter(this::checkRequirements)
                        .collect(Collectors.toCollection(ArrayList::new));
            }
        }
        return equipment;
    }

    private void generateStats() {
        NumberBinding remainingPoints = Bindings.createIntegerBinding(() -> 50).subtract(strength).subtract(endurance)
                .subtract(dexterity).subtract(intelligence).subtract(charisma).subtract(resilience)
                .subtract(maneuverability).subtract(precision);


        // Skill first into the attributes the main talents need
        for (PrimaryAttribute attribute : getMainTalents().stream()
                .map(Talent::getAttributes).map(Arrays::asList).flatMap(Collection::stream).collect(Collectors.toList())) {
            IntegerProperty att = getAttribute(attribute);

            int gain = 3 + random.nextInt(3);

            if (gain > remainingPoints.intValue()) {
                gain = remainingPoints.intValue();
            }

            att.set(Math.min(att.get() + gain, 12));
        }

        // Skill than into the attributes that were specified
        generateStats(specialisation, remainingPoints);
        generateStats(fightingStyle, remainingPoints);
        generateStats(profession, remainingPoints);
        generateStats(race, remainingPoints);
        generateStats(characterisation, remainingPoints);

        // Skill lastly into random attributes
        PrimaryAttribute[] values = PrimaryAttribute.values();
        while (remainingPoints.intValue() != 0) {
            int gain = random.nextInt(3);

            if (gain > remainingPoints.intValue()) {
                gain = remainingPoints.intValue();
            }

            PrimaryAttribute attribute = values[random.nextInt(values.length)];
            IntegerProperty att = getAttribute(attribute);

            att.set(Math.min(att.get() + gain, 12));
        }
    }

    private void generateStats(GenerationBase generation, NumberBinding remainingPoints) {
        for (PrimaryAttribute attribute : generation.getPrimaryAttributes()) {
            IntegerProperty att = getAttribute(attribute);

            int gain = 2 + random.nextInt(4);

            if (gain > remainingPoints.intValue()) {
                gain = remainingPoints.intValue();
            }

            att.set(Math.min(att.get() + gain, 12));
        }
    }

    private IntegerProperty createProperty(double s1, IntegerProperty n1, double s2, IntegerProperty n2,
                                           double s3, IntegerProperty n3, IntegerProperty points) {
        return createProperty(s1, n1, s2, n2, s3, n3, points, 0);
    }

    private IntegerProperty createProperty(double s1, IntegerProperty n1, double s2, IntegerProperty n2,
                                           double s3, IntegerProperty n3, IntegerProperty points, int b) {
        DoubleBinding base = n1.multiply(s1).add(n2.multiply(s2)).add(n3.multiply(s3)).add(b).add(points);
        IntegerProperty result = new SimpleIntegerProperty();
        result.bind(Bindings.createIntegerBinding(() -> (int) Math.round(base.get()), base));
        return result;
    }

    private IntegerProperty createProperty(double s1, IntegerProperty n1, double s2,
                                           IntegerProperty n2, IntegerProperty points, int b) {
        DoubleBinding base = n1.multiply(s1).add(n2.multiply(s2)).add(b).add(points);
        IntegerProperty result = new SimpleIntegerProperty();
        result.bind(Bindings.createIntegerBinding(() -> (int) Math.round(base.get()), base));
        return result;
    }

    public IntegerProperty getTalent(Talent talent) {
        return talents.get(talent);
    }

    public Collection<Weapon> getWeapons() {
        return weapons;
    }

    public Collection<Armor> getArmor() {
        return armors;
    }

    public Collection<Jewellery> getJewelleries() {
        return jewellery;
    }

    public Collection<Spell> getSpells() {
        return spells;
    }

    public IntegerProperty getAttribute(PrimaryAttribute attribute) {
        switch (attribute) {
            case strength:
                return strength;
            case endurance:
                return endurance;
            case dexterity:
                return dexterity;
            case intelligence:
                return intelligence;
            case charisma:
                return charisma;
            case resilience:
                return resilience;
            case agility:
                return maneuverability;
            case precision:
                return precision;
        }
        return dexterity;
    }

    public ReadOnlyIntegerProperty getAttribute(SecondaryAttribute attribute) {
        switch (attribute) {
            case meleeDamage:
                return baseDamageMelee;
            case rangeDamage:
                return baseDamageRange;
            case magicPower:
                return magicPower;
            case defense:
                return baseDefense;
            case initiative:
                return initiative;
            case health:
                return maxLife;
            case mentalHealth:
                return mentalHealth;
            case mana:
                return maxMana;
        }
        return maxLife;
    }

    public IntegerProperty getModifier(SecondaryAttribute attribute) {
        switch (attribute) {
            case meleeDamage:
                return baseDamageMeleeModifier;
            case rangeDamage:
                return baseDamageRangeModifier;
            case magicPower:
                return magicPowerModifier;
            case defense:
                return defenseModifier;
            case initiative:
                return initModifier;
            case health:
                return maxLifeModifier;
            case mentalHealth:
                return mentalHealthModifier;
            case mana:
                return maxManaModifier;
        }
        return baseDamageMeleeModifier;
    }

    private Collection<String> getPrimaryWeaponTypes() {
        Collection<String> collection = characterisation.getPrimaryWeaponTypes();
        collection.addAll(race.getPrimaryWeaponTypes());
        collection.addAll(profession.getPrimaryWeaponTypes());
        collection.addAll(fightingStyle.getPrimaryWeaponTypes());
        collection.addAll(specialisation.getPrimaryWeaponTypes());
        return collection;
    }

    private Collection<String> getSecondaryWeaponTypes() {
        Collection<String> collection = characterisation.getSecondaryWeaponTypes();
        collection.addAll(race.getSecondaryWeaponTypes());
        collection.addAll(profession.getSecondaryWeaponTypes());
        collection.addAll(fightingStyle.getSecondaryWeaponTypes());
        collection.addAll(specialisation.getSecondaryWeaponTypes());
        return collection;
    }

    private Collection<Weapon> getSpecificPrimaryWeapons() {
        Collection<Weapon> collection = characterisation.getSpecificPrimaryWeapons();
        collection.addAll(race.getSpecificPrimaryWeapons());
        collection.addAll(profession.getSpecificPrimaryWeapons());
        collection.addAll(fightingStyle.getSpecificPrimaryWeapons());
        collection.addAll(specialisation.getSpecificPrimaryWeapons());
        return collection;
    }

    private Collection<Weapon> getSpecificSecondaryWeapons() {
        Collection<Weapon> collection = characterisation.getSpecificSecondaryWeapons();
        collection.addAll(race.getSpecificSecondaryWeapons());
        collection.addAll(profession.getSpecificSecondaryWeapons());
        collection.addAll(fightingStyle.getSpecificSecondaryWeapons());
        collection.addAll(specialisation.getSpecificSecondaryWeapons());
        return collection;
    }


    private Collection<Armor> getSpecificArmor(ArmorPosition position) {
        Collection<Armor> collection = characterisation.getSpecificArmor(position);
        collection.addAll(race.getSpecificArmor(position));
        collection.addAll(profession.getSpecificArmor(position));
        collection.addAll(fightingStyle.getSpecificArmor(position));
        collection.addAll(specialisation.getSpecificArmor(position));
        return collection;
    }

    private boolean dropsWeapons() {
        return characterisation.dropsWeapon() &&
                race.dropsWeapon() &&
                profession.dropsWeapon() &&
                fightingStyle.dropsWeapon() &&
                specialisation.dropsWeapon();
    }

    private boolean dropsArmor() {
        return characterisation.dropsArmor() &&
                race.dropsArmor() &&
                profession.dropsArmor() &&
                fightingStyle.dropsArmor() &&
                specialisation.dropsArmor();
    }

    private boolean dropsJewellery() {
        return characterisation.dropsJewellery() &&
                race.dropsJewellery() &&
                profession.dropsJewellery() &&
                fightingStyle.dropsJewellery() &&
                specialisation.dropsJewellery();
    }

    private boolean usesFirstWeapon() {
        return characterisation.isAbleToUsesPrimaryHand() &&
                race.isAbleToUsesPrimaryHand() &&
                profession.isAbleToUsesPrimaryHand() &&
                fightingStyle.isAbleToUsesPrimaryHand() &&
                specialisation.isAbleToUsesPrimaryHand();
    }

    private boolean usesSecondWeapon() {
        return characterisation.isAbleToUsesSecondaryHand() &&
                race.isAbleToUsesSecondaryHand() &&
                profession.isAbleToUsesSecondaryHand() &&
                fightingStyle.isAbleToUsesSecondaryHand() &&
                specialisation.isAbleToUsesSecondaryHand();
    }

    private boolean usesArmor(ArmorPosition position) {
        return characterisation.isAbleToUseArmor(position) &&
                race.isAbleToUseArmor(position) &&
                profession.isAbleToUseArmor(position) &&
                fightingStyle.isAbleToUseArmor(position) &&
                specialisation.isAbleToUseArmor(position);
    }

    private boolean usesJewellery() {
        return characterisation.isAbleToUseJewellery() &&
                race.isAbleToUseJewellery() &&
                profession.isAbleToUseJewellery() &&
                fightingStyle.isAbleToUseJewellery() &&
                specialisation.isAbleToUseJewellery();
    }

    private boolean isNotAbleToUseSpells() {
        return !characterisation.isAbleToUseSpells() ||
                !race.isAbleToUseSpells() ||
                !profession.isAbleToUseSpells() ||
                !fightingStyle.isAbleToUseSpells() ||
                !specialisation.isAbleToUseSpells();
    }

    private Collection<PrimaryAttribute> getPrimaryAttributes() {
        Collection<PrimaryAttribute> result = new ArrayList<>();
        result.addAll(characterisation.getPrimaryAttributes());
        result.addAll(race.getPrimaryAttributes());
        result.addAll(profession.getPrimaryAttributes());
        result.addAll(fightingStyle.getPrimaryAttributes());
        result.addAll(specialisation.getPrimaryAttributes());

        return result;
    }

    public Collection<Talent> getMainTalents() {
        Collection<Talent> result = new ArrayList<>();
        result.addAll(characterisation.getMainTalents());
        result.addAll(race.getMainTalents());
        result.addAll(profession.getMainTalents());
        result.addAll(fightingStyle.getMainTalents());
        result.addAll(specialisation.getMainTalents());

        return result;
    }

    private Collection<Talent> getForbiddenTalents() {
        Collection<Talent> result = new ArrayList<>();
        result.addAll(characterisation.getForbiddenTalents());
        result.addAll(race.getForbiddenTalents());
        result.addAll(profession.getForbiddenTalents());
        result.addAll(fightingStyle.getForbiddenTalents());
        result.addAll(specialisation.getForbiddenTalents());

        if (isNotAbleToUseSpells()) {
            result.addAll(Database.talentList.stream().filter(Talent::isMagicTalent).collect(Collectors.toList()));
        }

        return result;
    }

    private Collection<SecondaryAttribute> getSecondaryAttributes() {
        Collection<SecondaryAttribute> result = new ArrayList<>();
        result.addAll(characterisation.getSecondaryAttributes());
        result.addAll(race.getSecondaryAttributes());
        result.addAll(profession.getSecondaryAttributes());
        result.addAll(fightingStyle.getSecondaryAttributes());
        result.addAll(specialisation.getSecondaryAttributes());

        return result;
    }

    public int getStrength() {
        return strength.get();
    }

    public IntegerProperty strengthProperty() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength.set(strength);
    }

    public int getEndurance() {
        return endurance.get();
    }

    public IntegerProperty enduranceProperty() {
        return endurance;
    }

    public void setEndurance(int endurance) {
        this.endurance.set(endurance);
    }

    public int getDexterity() {
        return dexterity.get();
    }

    public IntegerProperty dexterityProperty() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity.set(dexterity);
    }

    public int getIntelligence() {
        return intelligence.get();
    }

    public IntegerProperty intelligenceProperty() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence.set(intelligence);
    }

    public int getCharisma() {
        return charisma.get();
    }

    public IntegerProperty charismaProperty() {
        return charisma;
    }

    public void setCharisma(int charisma) {
        this.charisma.set(charisma);
    }

    public int getResilience() {
        return resilience.get();
    }

    public IntegerProperty resilienceProperty() {
        return resilience;
    }

    public void setResilience(int resilience) {
        this.resilience.set(resilience);
    }

    public int getManeuverability() {
        return maneuverability.get();
    }

    public IntegerProperty maneuverabilityProperty() {
        return maneuverability;
    }

    public void setManeuverability(int maneuverability) {
        this.maneuverability.set(maneuverability);
    }

    public int getPrecision() {
        return precision.get();
    }

    public IntegerProperty precisionProperty() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision.set(precision);
    }

    public String getNotes() {
        return notes.get();
    }

    public StringProperty notesProperty() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes.set(notes);
    }
}
