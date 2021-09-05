package model.member;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import manager.*;
import model.Battle;
import model.Currency;
import model.Rarity;
import model.Spell;
import model.item.*;
import model.loot.LootTable;
import model.member.data.ArmorPiece;
import model.member.data.AttackTypes;
import model.member.generation.*;
import model.member.generation.specs.*;
import model.member.interfaces.IExtendedBattleMember;
import org.apache.commons.configuration2.Configuration;
import org.apache.poi.ss.usermodel.Workbook;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExtendedBattleMember extends BattleMember implements IExtendedBattleMember {

    protected static Random random = new Random();

    protected IntegerProperty strength;
    protected IntegerProperty endurance;
    protected IntegerProperty dexterity;
    protected IntegerProperty intelligence;
    protected IntegerProperty charisma;
    protected IntegerProperty resilience;
    protected IntegerProperty agility;
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
        this.usesShield = usesAlwaysShield() ||
                (isAbleToUseShield() && random.nextDouble() < Utility.getConfig().getDouble("character.chance_to_wear.shield"));
        this.setLevel(level);
        this.setName(profession + " - " + specialisation);

        this.strength = new SimpleIntegerProperty(2);
        this.endurance = new SimpleIntegerProperty(2);
        this.dexterity = new SimpleIntegerProperty(2);
        this.intelligence = new SimpleIntegerProperty(2);
        this.charisma = new SimpleIntegerProperty(2);
        this.resilience = new SimpleIntegerProperty(2);
        this.agility = new SimpleIntegerProperty(2);
        this.precision = new SimpleIntegerProperty(2);

        this.baseDamageMeleeModifier = new SimpleIntegerProperty();
        this.baseDamageRangeModifier = new SimpleIntegerProperty();
        this.magicPowerModifier = new SimpleIntegerProperty();
        this.defenseModifier = new SimpleIntegerProperty();
        this.initModifier = new SimpleIntegerProperty();
        this.maxLifeModifier = new SimpleIntegerProperty();
        this.mentalHealthModifier = new SimpleIntegerProperty();
        this.maxManaModifier = new SimpleIntegerProperty();

        this.baseDamageMelee = createSecondaryAttributeProperty("character.secondaryAttribute.damageMelee", baseDamageMeleeModifier);
        this.baseDamageRange = createSecondaryAttributeProperty("character.secondaryAttribute.damageRange", baseDamageRangeModifier);
        this.magicPower = createSecondaryAttributeProperty("character.secondaryAttribute.magicPower", magicPowerModifier);
        this.baseDefense = createSecondaryAttributeProperty("character.secondaryAttribute.baseDefense", defenseModifier);
        this.initiative = createSecondaryAttributeProperty("character.secondaryAttribute.initiative", initModifier);
        this.maxLife = createSecondaryAttributeProperty("character.secondaryAttribute.maxLife", maxLifeModifier);
        this.mentalHealth = createSecondaryAttributeProperty("character.secondaryAttribute.mentalHealth", mentalHealthModifier);
        this.maxMana = createSecondaryAttributeProperty("character.secondaryAttribute.maxMana", maxManaModifier);

        this.notes = new SimpleStringProperty("");

        this.talents = new HashMap<>();
        for (Talent talent : Database.talentList) {
            talents.put(talent, new SimpleIntegerProperty(0));
        }

        Configuration config = Utility.getConfig();

        this.generateStats();

        Consumer<Equipment> weaponListener = weapon -> {
            if (weapon instanceof Weapon) {
                weapons.remove(weapon);
            }
        };

        Consumer<Equipment> armorListener = armor -> {
            if (armor instanceof Armor) {
                setArmor(ArmorPiece.getArmorPiece(armor.getSubtype()), 0);
                armors.remove(armor);
            }
        };

        //Prepare WeaponList
        this.weapons = FXCollections.observableArrayList();
        this.weapons.addListener((ListChangeListener<? super Weapon>) ob -> {
            for (Weapon weapon : weapons) {
                weapon.removeOnBreakListener(weaponListener);
            }

            while (ob.next()) {
                ObservableList<? extends Weapon> list = ob.getList();
                DecimalFormat format = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.GERMANY);

                for (Weapon weapon : list) {
                    weapon.addOnBreakListener(weaponListener);
                }

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
            Collection<Weapon> weaponPool = getSpecificPrimaryWeapons();
            if (!usesExclusivelySpecificPrimaryWeapons()) {
                weaponPool.addAll(Database.weaponList);
            }
            this.weapons.add((Weapon) randomWeapon(getPrimaryWeaponTypes(), weaponPool).getWithUpgrade());
        }

        if (usesSecondWeapon()) {
            Collection<Weapon> weaponPool = getSpecificSecondaryWeapons();
            if (!usesExclusivelySpecificSecondaryWeapons()) {
                weaponPool.addAll(Database.weaponList);
            }
            Collection<String> secondTypes = usesShield ? Database.shieldTypes : getSecondaryWeaponTypes();
            if (secondTypes.size() > 0) {
                this.weapons.add((Weapon) randomWeapon(secondTypes, weaponPool).getWithUpgrade());
            }
        }

        //Prepare ArmorList
        this.armors = FXCollections.observableArrayList();
        this.armors.addListener((ListChangeListener<? super Armor>) ob -> {
            for (Armor armor : armors) {
                armor.removeOnBreakListener(armorListener);
            }

            while (ob.next()) {
                ObservableList<? extends Armor> list = ob.getList();

                for (Armor armor : list) {
                    try {
                        this.setArmor(ArmorPiece.getArmorPiece(armor.getSubtype()), armor.protectionWithWearBinding());
                        armor.addOnBreakListener(armorListener);
                    } catch (NoSuchElementException ignored) {
                    }
                }
            }
        });

        //Generate Armor
        for (ArmorPosition position : ArmorPosition.values()) {
            this.generateArmor(position);
        }

        Collection<Jewellery> jewelleryPool = getSpecificJewellery();
        if (!usesExclusivelySpecificJewellery()) {
            jewelleryPool.addAll(Database.jewelleryList);
        }

        this.jewellery = FXCollections.observableArrayList();
        if (usesJewellery()) {

            Map<String, Integer> amountPerType = new HashMap<>();
            int maxAmount = 0;

            for (String type : config.getStringArray("character.jewellery.types")) {
                int amount = config.getInt("character.jewellery.amount." + type);
                String localizedType = LanguageUtility.hasMessage("jewellery.type." + type) ?
                        LanguageUtility.getMessage("jewellery.type." + type) : type;

                if (amount > 0) {
                    amountPerType.put(localizedType, amount);
                    maxAmount += amount;
                }
            }

            for (int i = 0; i < maxAmount; i++) {
                Optional<String> opType = amountPerType.keySet().stream().skip(random.nextInt(amountPerType.keySet().size())).findFirst();

                if (opType.isPresent()) {
                    if (random.nextDouble() < getTier() / 100f) {
                        String type = opType.get();
                        this.jewellery.add((Jewellery) randomJewellery(type, jewelleryPool).getWithUpgrade());

                        int newAmount = amountPerType.get(type) - 1;
                        if (newAmount < 1) {
                            amountPerType.remove(type);
                        } else {
                            amountPerType.put(type, newAmount);
                        }
                    }
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

    public ExtendedBattleMember(Workbook wb) {
        this(CharacterSheetParser.parseCharacterSheet(wb));
    }

    public ExtendedBattleMember(CharacterSheetParameterMap parameterMap) {
        super(parameterMap);

        // load rings
        for (int i = 1; i <= 8; i++) {
            String ringx = parameterMap.getValueAsStringOrElse("character.armor.ring." + i, "");
            Item ringItemx = Database.getItemOrElse(ringx, null);
            // TODO if null create DB entry
            if (ringItemx != null) {
                jewellery.add((Jewellery) ringItemx);
            }
        }
        // load amulet
        String amulet = parameterMap.getValueAsStringOrElse("character.armor.amulet", "");
        Item amuletItem = Database.getItemOrElse(amulet, null);
        if (amuletItem != null) {
            jewellery.add((Jewellery) amuletItem);
        }

        // load bracelets
        String bracelet1 = parameterMap.getValueAsStringOrElse("character.armor.bracelet.1", "");
        String bracelet2 = parameterMap.getValueAsStringOrElse("character.armor.bracelet.2", "");
        Item bracelet1Item = Database.getItemOrElse(bracelet1, null);
        Item bracelet2Item = Database.getItemOrElse(bracelet2, null);
        if (bracelet1Item != null) {
            jewellery.add((Jewellery) bracelet1Item);
        }
        if (bracelet2Item != null) {
            jewellery.add((Jewellery) bracelet2Item);
        }

        // load misc
        for (int i = 1; i <= 4; i++) {
            String miscX = parameterMap.getValueAsStringOrElse("character.armor.misc." + i, "");
            Item miscItemX = Database.getItemOrElse(miscX, null);
            // TODO if null create DB entry
            if (miscItemX != null) {
                jewellery.add((Jewellery) miscItemX);
            }
        }

        // load currency
        int cp = parameterMap.getValueAsIntegerOrElse("character.money.copper", 0);
        int sp = parameterMap.getValueAsIntegerOrElse("character.money.silver", 0);
        int gp = parameterMap.getValueAsIntegerOrElse("character.money.gold", 0);

        Currency currency = new Currency(cp, sp, gp);
        this.lootTable.add(currency);


        // load mental health
        this.mentalHealth = new SimpleIntegerProperty(parameterMap.getValueAsIntegerOrElse("secondaryAttribute.mentalHealth", 0));

        // load advantages/disadvantages
        addDescription("character.advantage", Collections.singleton(parameterMap.getValueAsStringOrElse("character.advantages", "")));
        addDescription("character.disadvantage", Collections.singleton(parameterMap.getValueAsStringOrElse("character.disadvantages", "")));

        // load talents
        // TODO
        // load spells

    }

    @Override
    public LootTable getLootTable() {
        LootTable lootTable = super.getLootTable();

        if (dropsWeapons()) {
            weapons.stream().filter(e -> !e.getName().isEmpty()).forEach(e -> lootTable.add(e, 1, 1));
        }

        if (dropsArmor()) {
            armors.stream().filter(e -> !e.getName().isEmpty()).forEach(e -> lootTable.add(e, 1, 1));
        }

        if (dropsJewellery()) {
            jewellery.stream().filter(e -> !e.getName().isEmpty()).forEach(e -> lootTable.add(e, 1, 1));
        }

        return lootTable;
    }

    public void applyWearOnWeapons() {
        for (Weapon weapon : new ArrayList<>(weapons)) {
            if (!Database.shieldTypes.contains(weapon.getSubtype())) {
                weapon.applyWear();
            }
        }
    }

    public void takeDamage(int amount, AttackTypes type, boolean withShield, double penetration, double block, BattleMember source) {
        super.takeDamage(amount, type, withShield, penetration, block, source);

        if (withShield) {
            for (Weapon shield : new ArrayList<>(weapons)) {
                if (Database.shieldTypes.contains(shield.getSubtype())) {
                    shield.applyWear();
                }
            }

            if (getArmor(ArmorPiece.shield) < amount) {
                for (Armor armor : new ArrayList<>(armors)) {
                    if (armor.getSubtype().equalsIgnoreCase(type.toStringProperty().get())) {
                        armor.applyWear();
                    }
                }
            }

        } else {
            for (Armor armor : new ArrayList<>(armors)) {
                if (armor.getSubtype().equalsIgnoreCase(type.toStringProperty().get())) {
                    armor.applyWear();
                }
            }
        }
    }

    private void generateLoot() {
        this.lootTable.add(characterisation.getLootTable(this));
        this.lootTable.add(race.getLootTable(this));
        this.lootTable.add(profession.getLootTable(this));
        this.lootTable.add(fightingStyle.getLootTable(this));
        this.lootTable.add(specialisation.getLootTable(this));
    }

    private void addDescription() {
        if (Utility.getConfig().getBoolean("character.advantages_disadvantages.separatedByType")) {
            addDescription("character.advantage.characterisation", characterisation.getAdvantages());
            addDescription("character.advantage.race", race.getAdvantages());
            addDescription("character.advantage.profession", profession.getAdvantages());
            addDescription("character.advantage.fightingStyle", fightingStyle.getAdvantages());
            addDescription("character.advantage.specialisation", specialisation.getAdvantages());

            notes.set(notes.get() + "\n");

            addDescription("character.disadvantage.characterisation", characterisation.getDisadvantages());
            addDescription("character.disadvantage.race", race.getDisadvantages());
            addDescription("character.disadvantage.profession", profession.getDisadvantages());
            addDescription("character.disadvantage.fightingStyle", fightingStyle.getDisadvantages());
            addDescription("character.disadvantage.specialisation", specialisation.getDisadvantages());
        } else {
            Collection<String> advantages = new ArrayList<>();
            advantages.addAll(characterisation.getAdvantages());
            advantages.addAll(race.getAdvantages());
            advantages.addAll(profession.getAdvantages());
            advantages.addAll(fightingStyle.getAdvantages());
            advantages.addAll(specialisation.getAdvantages());
            addDescription("character.advantage", advantages);

            notes.set(notes.get() + "\n");

            Collection<String> disadvantages = new ArrayList<>();
            disadvantages.addAll(characterisation.getDisadvantages());
            disadvantages.addAll(race.getDisadvantages());
            disadvantages.addAll(profession.getDisadvantages());
            disadvantages.addAll(fightingStyle.getDisadvantages());
            disadvantages.addAll(specialisation.getDisadvantages());
            addDescription("character.disadvantage", disadvantages);
        }
    }

    private void addDescription(String headerKey, Collection<String> lines) {
        if (!lines.isEmpty()) {
            notes.set(notes.get() + LanguageUtility.getMessage(headerKey) + "\n");
            for (String line : lines) {
                String[] parts = line.split("\\r?\\n");
                for (int i = 0; i < parts.length; i++) {
                    if (i == 0) {
                        notes.set(notes.get() + "  - " + parts[i] + "\n");
                    } else {
                        notes.set(notes.get() + "    " + parts[i] + "\n");
                    }
                }

            }
        }
    }

    private void generateArmor(ArmorPosition position) {
        if (usesArmor(position)) {
            Collection<Armor> armorPool = getSpecificArmor(position);
            if (!usesExclusivelySpecificArmor(position)) {
                armorPool.addAll(Database.armorList);
            }
            this.armors.add((Armor) randomArmor(position, armorPool).getWithUpgrade());
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
                notes.set(LanguageUtility.getMessage("character.unusedTalents") + ": " + talentPoints + "\n\n" + notes.get());
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
            final int k = i;
            Rarity rarity = Rarity.getRandomRarity();

            do {
                final Rarity fRarity = rarity;

                weapons = weaponPool.stream()
                        .filter(x -> x.getTier() == k)
                        .filter(x -> x.getRarity() == fRarity)
                        .filter(this::checkRequirements)
                        .filter(x -> types.stream().anyMatch(y -> x.getSubtype().equals(y)))
                        .collect(Collectors.toCollection(ArrayList::new));

                rarity = rarity.getLowerRarity();

            } while (weapons.size() == 0 && rarity != Rarity.common);

            if (weapons.size() == 0) {
                weapons = weaponPool.stream()
                        .filter(x -> x.getTier() == k)
                        .filter(x -> x.getRarity() == Rarity.common)
                        .filter(this::checkRequirements)
                        .filter(x -> types.stream().anyMatch(y -> x.getSubtype().equals(y)))
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

    private Armor randomArmor(final ArmorPosition position, Collection<Armor> armorPool) {
        ArrayList<Armor> armors = equipmentSearch(position.toStringProperty().get(), armorPool);

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
        List<Character> requirementsList = new ArrayList<>();
        for (char c : item.getRequirement().toCharArray()) {
            requirementsList.add(c);
        }

        while (!requirementsList.isEmpty()) {
            int x = Utility.consumeNumber(requirementsList);
            String attribute = Utility.consumeString(requirementsList);

            if (attribute.equalsIgnoreCase(PrimaryAttribute.strength.toShortString())) {
                if (x > getStrength()) {
                    return false;
                }
            } else if (attribute.equalsIgnoreCase(PrimaryAttribute.endurance.toShortString())) {
                if (x > getEndurance()) {
                    return false;
                }
            } else if (attribute.equalsIgnoreCase(PrimaryAttribute.dexterity.toShortString())) {
                if (x > getDexterity()) {
                    return false;
                }
            } else if (attribute.equalsIgnoreCase(PrimaryAttribute.intelligence.toShortString())) {
                if (x > getIntelligence()) {
                    return false;
                }
            } else if (attribute.equalsIgnoreCase(PrimaryAttribute.charisma.toShortString())) {
                if (x > getCharisma()) {
                    return false;
                }
            } else if (attribute.equalsIgnoreCase(PrimaryAttribute.resilience.toShortString())) {
                if (x > getResilience()) {
                    return false;
                }
            } else if (attribute.equalsIgnoreCase(PrimaryAttribute.agility.toShortString())) {
                if (x > getAgility()) {
                    return false;
                }
            } else if (attribute.equalsIgnoreCase(PrimaryAttribute.precision.toShortString())) {
                if (x > getPrecision()) {
                    return false;
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
            final int k = i;
            Rarity rarity = Rarity.getRandomRarity();

            do {
                final Rarity fRarity = rarity;

                equipment = pool.stream()
                        .filter(x -> x.getTier() == k)
                        .filter(x -> x.getRarity() == fRarity)
                        .filter(x -> x.getSubtype().equals(typ))
                        .filter(this::checkRequirements)
                        .collect(Collectors.toCollection(ArrayList::new));

                rarity = rarity.getLowerRarity();

            } while (equipment.size() == 0 && rarity != Rarity.common);

            if (equipment.size() == 0) {
                equipment = pool.stream()
                        .filter(x -> x.getTier() == k)
                        .filter(x -> x.getRarity() == Rarity.common)
                        .filter(x -> x.getSubtype().equals(typ))
                        .filter(this::checkRequirements)
                        .collect(Collectors.toCollection(ArrayList::new));
            }
        }
        return equipment;
    }

    private void generateStats() {
        NumberBinding remainingPoints = Bindings.createIntegerBinding(() -> 50).subtract(strength).subtract(endurance)
                .subtract(dexterity).subtract(intelligence).subtract(charisma).subtract(resilience)
                .subtract(agility).subtract(precision);


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

    private IntegerProperty createSecondaryAttributeProperty(String key, IntegerProperty points) {
        Double[] loadedFactors = (Double[]) Utility.getConfig().getArray(Double.class, key);
        int baseValue = Utility.getConfig().getInt(key + ".base", 0);

        double[] factors = new double[8];
        for (int i = 0; i < loadedFactors.length && i < factors.length; i++) {
            factors[i] = loadedFactors[i];
        }

        DoubleBinding base = strength.multiply(factors[0])
                .add(endurance.multiply(factors[1]))
                .add(dexterity.multiply(factors[2]))
                .add(intelligence.multiply(factors[3]))
                .add(charisma.multiply(factors[4]))
                .add(resilience.multiply(factors[5]))
                .add(agility.multiply(factors[6]))
                .add(precision.multiply(factors[7]))
                .add(baseValue)
                .add(points);

        IntegerProperty result = new SimpleIntegerProperty();
        result.bind(Bindings.createIntegerBinding(() -> (int) Math.round(base.get()), base));
        return result;
    }

    public IntegerProperty getTalent(Talent talent) {
        return talents.get(talent);
    }

    public ObservableList<Weapon> getWeapons() {
        return weapons;
    }

    public ObservableList<Armor> getArmor() {
        return armors;
    }

    public ObservableList<Jewellery> getJewelleries() {
        return jewellery;
    }

    public ObservableList<Spell> getSpells() {
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
                return agility;
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

    private <T> Collection<T> getCollection(Function<GenerationBase, Collection<T>> getter) {
        Collection<T> collection = new ArrayList<>();
        collection.addAll(getter.apply(characterisation));
        collection.addAll(getter.apply(race));
        collection.addAll(getter.apply(profession));
        collection.addAll(getter.apply(fightingStyle));
        collection.addAll(getter.apply(specialisation));
        return collection;
    }

    private boolean getOr(Function<GenerationBase, Boolean> getter) {
        return getter.apply(characterisation) ||
                getter.apply(race) ||
                getter.apply(profession) ||
                getter.apply(fightingStyle) ||
                getter.apply(specialisation);
    }

    private boolean getAnd(Function<GenerationBase, Boolean> getter) {
        return getter.apply(characterisation) &&
                getter.apply(race) &&
                getter.apply(profession) &&
                getter.apply(fightingStyle) &&
                getter.apply(specialisation);
    }

    private Collection<String> getPrimaryWeaponTypes() {
        return getCollection(GenerationBase::getPrimaryWeaponTypes);
    }

    private Collection<String> getSecondaryWeaponTypes() {
        return getCollection(GenerationBase::getSecondaryWeaponTypes);
    }

    private Collection<Weapon> getSpecificPrimaryWeapons() {
        return getCollection(GenerationBase::getSpecificPrimaryWeapons);
    }

    private Collection<Weapon> getSpecificSecondaryWeapons() {
        return getCollection(GenerationBase::getSpecificSecondaryWeapons);
    }

    private Collection<Armor> getSpecificArmor(ArmorPosition position) {
        return getCollection(base -> base.getSpecificArmor(position));
    }

    private Collection<Jewellery> getSpecificJewellery() {
        return getCollection(GenerationBase::getSpecificJewellery);
    }

    private boolean dropsWeapons() {
        return getAnd(GenerationBase::dropsWeapon);
    }

    private boolean dropsArmor() {
        return getAnd(GenerationBase::dropsArmor);
    }

    private boolean dropsJewellery() {
        return getAnd(GenerationBase::dropsJewellery);
    }

    private boolean usesFirstWeapon() {
        return getAnd(GenerationBase::isAbleToUsesPrimaryHand);
    }

    private boolean usesSecondWeapon() {
        return getAnd(GenerationBase::isAbleToUsesSecondaryHand);
    }

    private boolean usesArmor(ArmorPosition position) {
        return getAnd(base -> base.isAbleToUseArmor(position));
    }

    private boolean usesJewellery() {
        return getAnd(GenerationBase::isAbleToUseJewellery);
    }

    private boolean isNotAbleToUseSpells() {
        return getAnd(base -> !base.isAbleToUseSpells());
    }

    private boolean usesAlwaysShield() {
        return getOr(GenerationBase::usesAlwaysShield);
    }

    private boolean isAbleToUseShield() {
        return getAnd(GenerationBase::isAbleToUseShield);
    }

    private boolean usesExclusivelySpecificPrimaryWeapons() {
        return getOr(GenerationBase::usesExclusivelySpecificPrimaryWeapons);
    }

    private boolean usesExclusivelySpecificSecondaryWeapons() {
        return getOr(GenerationBase::usesExclusivelySpecificSecondaryWeapons);
    }

    private boolean usesExclusivelySpecificArmor(ArmorPosition position) {
        return getOr(base -> base.usesExclusivelySpecificArmor(position));
    }

    private boolean usesExclusivelySpecificJewellery() {
        return getOr(GenerationBase::usesExclusivelySpecificJewellery);
    }

    private Collection<PrimaryAttribute> getPrimaryAttributes() {
        return getCollection(GenerationBase::getPrimaryAttributes);
    }

    public Collection<Talent> getMainTalents() {
        return getCollection(GenerationBase::getMainTalents);
    }

    private Collection<Talent> getForbiddenTalents() {
        Collection<Talent> result = getCollection(GenerationBase::getForbiddenTalents);

        if (isNotAbleToUseSpells()) {
            result.addAll(Database.talentList.stream().filter(Talent::isMagicTalent).collect(Collectors.toList()));
        }

        return result;
    }

    private Collection<SecondaryAttribute> getSecondaryAttributes() {
        return getCollection(GenerationBase::getSecondaryAttributes);
    }

    public IntegerProperty strengthProperty() {
        return strength;
    }

    private void setStrength(int strength) {
        this.strength.set(strength);
    }

    public IntegerProperty enduranceProperty() {
        return endurance;
    }

    public void setEndurance(int endurance) {
        this.endurance.set(endurance);
    }

    public IntegerProperty dexterityProperty() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity.set(dexterity);
    }

    public IntegerProperty intelligenceProperty() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence.set(intelligence);
    }

    public IntegerProperty charismaProperty() {
        return charisma;
    }

    public void setCharisma(int charisma) {
        this.charisma.set(charisma);
    }

    public IntegerProperty resilienceProperty() {
        return resilience;
    }

    public void setResilience(int resilience) {
        this.resilience.set(resilience);
    }

    public IntegerProperty agilityProperty() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility.set(agility);
    }

    public IntegerProperty precisionProperty() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision.set(precision);
    }

    public StringProperty notesProperty() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes.set(notes);
    }
}
