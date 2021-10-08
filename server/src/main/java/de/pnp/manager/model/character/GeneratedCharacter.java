package de.pnp.manager.model.character;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.pnp.manager.main.Database;
import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.main.Utility;
import de.pnp.manager.model.Battle;
import de.pnp.manager.model.Rarity;
import de.pnp.manager.model.item.*;
import de.pnp.manager.model.character.data.ArmorPosition;
import de.pnp.manager.model.character.data.IPrimaryAttribute;
import de.pnp.manager.model.character.data.PrimaryAttribute;
import de.pnp.manager.model.character.data.SecondaryAttribute;
import de.pnp.manager.model.character.generation.GenerationBase;
import de.pnp.manager.model.character.generation.specs.*;
import de.pnp.manager.model.loot.ILootTable;
import de.pnp.manager.model.loot.LootTable;
import de.pnp.manager.model.other.ITalent;
import de.pnp.manager.model.other.Spell;
import de.pnp.manager.model.other.Talent;
import org.apache.commons.configuration2.Configuration;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@JsonSerialize(as = IPnPCharacter.class)
public class GeneratedCharacter extends PnPCharacter {

    protected static Random random = new Random();

    protected Characterisation characterisation;
    protected Race race;
    protected Profession profession;
    protected FightingStyle fightingStyle;
    protected Specialisation specialisation;

    protected boolean usesShield;

    public GeneratedCharacter(String characterID, Battle battle, int level,
                                 Characterisation characterisation, Race race, Profession profession,
                                 FightingStyle fightingStyle, Specialisation specialisation) {
        super(characterID, battle);
        this.characterisation = characterisation;
        this.race = race;
        this.profession = profession;
        this.fightingStyle = fightingStyle;
        this.specialisation = specialisation;
        this.usesShield = usesAlwaysShield() ||
                (isAbleToUseShield() && random.nextDouble() < Utility.getConfig().getDouble("character.chance_to_wear.shield"));
        this.setLevel(level);
        this.setName(profession + " - " + specialisation);

        for (IPrimaryAttribute p : PrimaryAttribute.getValuesWithoutDummy()) {
            this.primaryAttributes.put(p, Utility.getConfig().getInt("character.skillPoints.min"));
        }
        this.generateStats();
        this.calculateSecondaryAttributes();
        this.useSkillPoints();
        this.createModifierBindings();

        //Generate Weapons
        if (usesFirstWeapon()) {
            Collection<Weapon> weaponPool = getSpecificPrimaryWeapons();
            if (!usesExclusivelySpecificPrimaryWeapons()) {
                weaponPool.addAll(Database.weaponList);
            }
            this.getEquippedWeapons().add((Weapon) randomWeapon(getPrimaryWeaponTypes(), weaponPool).getWithUpgrade());
        }

        if (usesSecondWeapon()) {
            Collection<Weapon> weaponPool = getSpecificSecondaryWeapons();
            if (!usesExclusivelySpecificSecondaryWeapons()) {
                weaponPool.addAll(Database.weaponList);
            }
            Collection<String> secondTypes = usesShield ? Database.shieldTypes : getSecondaryWeaponTypes();
            if (secondTypes.size() > 0) {
                this.getEquippedWeapons().add((Weapon) randomWeapon(secondTypes, weaponPool).getWithUpgrade());
            }
        }

        //Generate Armor
        for (ArmorPosition position : ArmorPosition.values()) {
            this.generateArmor(position);
        }

        //Generate Jewellery
        Configuration config = Utility.getConfig();
        Collection<Jewellery> jewelleryPool = getSpecificJewellery();
        if (!usesExclusivelySpecificJewellery()) {
            jewelleryPool.addAll(Database.jewelleryList);
        }

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
                        this.getEquippedJewellery().add((Jewellery) randomJewellery(type, jewelleryPool).getWithUpgrade());

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

        this.getAdvantages().addAll(getCollection(GenerationBase::getAdvantages));
        this.getDisadvantages().addAll(getCollection(GenerationBase::getDisadvantages));
        this.generateSpells();
        this.createResourceProperties();
        this.generateLoot();
        this.addDescription();
    }

    private void generateStats() {
        Configuration config = Utility.getConfig();
        int remainingPoints = config.getInt("character.skillPoints.max")
                - PrimaryAttribute.getValuesWithoutDummy().length * config.getInt("character.skillPoints.min");

        // Skill first into the attributes the main talents need
        for (IPrimaryAttribute attribute : getMainTalents().stream()
                .map(Talent::getAttributes).map(Arrays::asList).flatMap(Collection::stream).collect(Collectors.toList())) {

            int oldValue = getPrimaryAttributes().get(attribute);

            int gain = 3 + random.nextInt(3);

            if (gain > remainingPoints) {
                gain = remainingPoints;
            }

            int newValue = Math.min(oldValue + gain, 12);
            getPrimaryAttributes().put(attribute, newValue);

            remainingPoints -= newValue - oldValue;
        }

        // Skill then into the attributes that were specified
        for (PrimaryAttribute attribute : getMainPrimaryAttributes()) {
            int oldValue = getPrimaryAttributes().get(attribute);

            int gain = 2 + random.nextInt(4);

            if (gain > remainingPoints) {
                gain = remainingPoints;
            }

            int newValue = Math.min(oldValue + gain, 12);
            getPrimaryAttributes().put(attribute, newValue);

            remainingPoints -= newValue - oldValue;
        }

        // Skill lastly into random attributes
        PrimaryAttribute[] values = PrimaryAttribute.getValuesWithoutDummy();
        while (remainingPoints != 0) {
            PrimaryAttribute attribute = values[random.nextInt(values.length)];

            int oldValue = getPrimaryAttributes().get(attribute);

            int gain = 1 + random.nextInt(2);

            if (gain > remainingPoints) {
                gain = remainingPoints;
            }

            int newValue = Math.min(oldValue + gain, 12);
            getPrimaryAttributes().put(attribute, newValue);

            remainingPoints -= newValue - oldValue;
        }
    }

    private void calculateSecondaryAttributes() {
        this.secondaryAttributes.put(SecondaryAttribute.meleeDamage, calculateSecondaryAttribute("character.secondaryAttribute.damageMelee"));
        this.secondaryAttributes.put(SecondaryAttribute.rangeDamage, calculateSecondaryAttribute("character.secondaryAttribute.damageRange"));
        this.secondaryAttributes.put(SecondaryAttribute.magicPower, calculateSecondaryAttribute("character.secondaryAttribute.magicPower"));
        this.secondaryAttributes.put(SecondaryAttribute.defense, calculateSecondaryAttribute("character.secondaryAttribute.baseDefense"));
        this.secondaryAttributes.put(SecondaryAttribute.initiative, calculateSecondaryAttribute("character.secondaryAttribute.initiative"));
        this.secondaryAttributes.put(SecondaryAttribute.health, calculateSecondaryAttribute("character.secondaryAttribute.maxLife"));
        this.secondaryAttributes.put(SecondaryAttribute.mana, calculateSecondaryAttribute("character.secondaryAttribute.mentalHealth"));
        this.secondaryAttributes.put(SecondaryAttribute.mentalHealth, calculateSecondaryAttribute("character.secondaryAttribute.maxMana"));
    }

    private int calculateSecondaryAttribute(String key) {
        Float[] loadedFactors = (Float[]) Utility.getConfig().getArray(Float.class, key);
        int baseValue = Utility.getConfig().getInt(key + ".base", 0);

        float[] factors = new float[8];
        for (int i = 0; i < loadedFactors.length && i < factors.length; i++) {
            factors[i] = loadedFactors[i];
        }

        float result = getPrimaryAttributes().get(PrimaryAttribute.STRENGTH) * factors[0] +
                getPrimaryAttributes().get(PrimaryAttribute.ENDURANCE) * factors[1] +
                getPrimaryAttributes().get(PrimaryAttribute.DEXTERITY) * factors[2] +
                getPrimaryAttributes().get(PrimaryAttribute.INTELLIGENCE) * factors[3] +
                getPrimaryAttributes().get(PrimaryAttribute.CHARISMA) * factors[4] +
                getPrimaryAttributes().get(PrimaryAttribute.RESILIENCE) * factors[5] +
                getPrimaryAttributes().get(PrimaryAttribute.AGILITY) * factors[6] +
                getPrimaryAttributes().get(PrimaryAttribute.PRECISION) * factors[7] +
                baseValue;

        return Math.round(result);
    }

    private void useSkillPoints() {
        int skillPoints = getLevel();
        int talentPoints = Utility.getConfig().getInt("character.talentPoints");
        Collection<SecondaryAttribute> secondaryAttributes = getMainSecondaryAttributes();

        while (skillPoints > 0) {
            if (random.nextDouble() < Utility.getConfig().getDouble("character.skillPoints.chanceSecondaryAttribute")
                    && secondaryAttributes.size() > 0) {
                SecondaryAttribute attribute = secondaryAttributes.stream()
                        .skip(random.nextInt(secondaryAttributes.size())).findFirst().orElse(null);

                if (attribute == null) {
                    continue;
                }

                switch (attribute) {
                    case meleeDamage:
                    case rangeDamage:
                    case magicPower:
                    case defense:
                        if (skillPoints > 4) {
                            getSecondaryAttributes().put(attribute, getSecondaryAttributes().get(attribute) + 1);
                            skillPoints -= 5;
                        }
                        break;
                    case health:
                    case mana:
                        getSecondaryAttributes().put(attribute, getSecondaryAttributes().get(attribute) + 1);
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
                    .filter(ITalent::isMagicTalent).filter(x -> talents.get(x).get() > 0).count() > 2) {
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

        Collection<ITalent> magicTalents = talents.keySet().stream()
                .filter(ITalent::isMagicTalent)
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
                    .filter(x -> getMainTalents().contains((Talent) x.getTalent()))
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

    private void generateArmor(ArmorPosition position) {
        if (usesArmor(position)) {
            Collection<Armor> armorPool = getSpecificArmor(position);
            if (!usesExclusivelySpecificArmor(position)) {
                armorPool.addAll(Database.armorList);
            }
            this.getEquippedArmor().put(position, (Armor) randomArmor(position, armorPool).getWithUpgrade());
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

        for (IPrimaryAttribute attribute : talent.getAttributes()) {
            if (getPrimaryAttributes().get(attribute) < 7) {
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

            if (attribute.equalsIgnoreCase(PrimaryAttribute.STRENGTH.toShortString())) {
                if (x > getPrimaryAttributes().get(PrimaryAttribute.STRENGTH)) {
                    return false;
                }
            } else if (attribute.equalsIgnoreCase(PrimaryAttribute.ENDURANCE.toShortString())) {
                if (x > getPrimaryAttributes().get(PrimaryAttribute.ENDURANCE)) {
                    return false;
                }
            } else if (attribute.equalsIgnoreCase(PrimaryAttribute.DEXTERITY.toShortString())) {
                if (x > getPrimaryAttributes().get(PrimaryAttribute.DEXTERITY)) {
                    return false;
                }
            } else if (attribute.equalsIgnoreCase(PrimaryAttribute.INTELLIGENCE.toShortString())) {
                if (x > getPrimaryAttributes().get(PrimaryAttribute.INTELLIGENCE)) {
                    return false;
                }
            } else if (attribute.equalsIgnoreCase(PrimaryAttribute.CHARISMA.toShortString())) {
                if (x > getPrimaryAttributes().get(PrimaryAttribute.CHARISMA)) {
                    return false;
                }
            } else if (attribute.equalsIgnoreCase(PrimaryAttribute.RESILIENCE.toShortString())) {
                if (x > getPrimaryAttributes().get(PrimaryAttribute.RESILIENCE)) {
                    return false;
                }
            } else if (attribute.equalsIgnoreCase(PrimaryAttribute.AGILITY.toShortString())) {
                if (x > getPrimaryAttributes().get(PrimaryAttribute.AGILITY)) {
                    return false;
                }
            } else if (attribute.equalsIgnoreCase(PrimaryAttribute.PRECISION.toShortString())) {
                if (x > getPrimaryAttributes().get(PrimaryAttribute.PRECISION)) {
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

    private void generateLoot() {
        this.lootTable.add(characterisation.getLootTable(this));
        this.lootTable.add(race.getLootTable(this));
        this.lootTable.add(profession.getLootTable(this));
        this.lootTable.add(fightingStyle.getLootTable(this));
        this.lootTable.add(specialisation.getLootTable(this));
    }

    @Override
    public ILootTable getFinishedLootTable() {
        LootTable result = new LootTable();
        result.add(lootTable);

        if (dropsWeapons()) {
            for (IEquipment equipment : getWeapons()) {
                result.add(equipment, 1,1);
            }
            for (IEquipment equipment : getEquippedWeapons()) {
                result.add(equipment, 1,1);
            }

        }
        if (dropsArmor()) {
            for (IEquipment equipment : getArmor()) {
                result.add(equipment, 1,1);
            }
            for (IEquipment equipment : getEquippedArmor().values()) {
                result.add(equipment, 1,1);
            }
        }
        if (dropsJewellery()) {
            for (IEquipment equipment : getJewellery()) {
                result.add(equipment, 1,1);
            }
            for (IEquipment equipment : getEquippedJewellery()) {
                result.add(equipment, 1,1);
            }
        }
        for (IItem item : getInventory()) {
            result.add(item, 1, 1);
        }

        return result;
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

    protected void addDescription(String headerKey, Collection<String> lines) {
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

    public boolean dropsWeapons() {
        return getAnd(GenerationBase::dropsWeapon);
    }

    public boolean dropsArmor() {
        return getAnd(GenerationBase::dropsArmor);
    }

    public boolean dropsJewellery() {
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

    private Collection<PrimaryAttribute> getMainPrimaryAttributes() {
        return getCollection(GenerationBase::getPrimaryAttributes);
    }

    private Collection<Talent> getMainTalents() {
        return getCollection(GenerationBase::getMainTalents);
    }

    private Collection<Talent> getForbiddenTalents() {
        Collection<Talent> result = getCollection(GenerationBase::getForbiddenTalents);

        if (isNotAbleToUseSpells()) {
            result.addAll(Database.talentList.stream().filter(Talent::isMagicTalent).collect(Collectors.toList()));
        }

        return result;
    }

    private Collection<SecondaryAttribute> getMainSecondaryAttributes() {
        return getCollection(GenerationBase::getSecondaryAttributes);
    }
}
