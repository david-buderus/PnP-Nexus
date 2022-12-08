package de.pnp.manager.model.character;

import de.pnp.manager.main.*;
import de.pnp.manager.model.Battle;
import de.pnp.manager.model.Currency;
import de.pnp.manager.model.item.*;
import de.pnp.manager.model.loot.LootTable;
import de.pnp.manager.model.manager.PnPCharacterProducer;
import de.pnp.manager.model.character.data.ArmorPosition;
import de.pnp.manager.model.character.data.PrimaryAttribute;
import de.pnp.manager.model.character.data.SecondaryAttribute;
import de.pnp.manager.model.other.Spell;
import de.pnp.manager.model.other.Talent;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.stream.Stream;

public class PnPCharacterFactory {

    public static PnPCharacter createDefaultCharacter(String characterID, Battle battle) {
        return createDefaultCharacter(characterID, battle, ((c, b) -> new PnPCharacter(c, b, new LootTable())));
    }

    public static <C extends PnPCharacter> C createDefaultCharacter(String characterID, Battle battle, PnPCharacterProducer<C> producer) {
        C character = producer.create(characterID, battle);

        character.createModifierBindings();
        character.secondaryAttributeModifiers.get(SecondaryAttribute.HEALTH).setModifier(1);
        character.secondaryAttributeModifiers.get(SecondaryAttribute.MANA).setModifier(1);
        character.secondaryAttributeModifiers.get(SecondaryAttribute.MENTAL_HEALTH).setModifier(1);
        character.secondaryAttributeModifiers.get(SecondaryAttribute.INITIATIVE).setModifier(1);
        character.createResourceProperties();

        return character;
    }

    public static PnPCharacter createCharacter(String characterID, Battle battle, Workbook workbook) {
        return createCharacter(characterID, battle, workbook, PnPCharacter::new);
    }

    public static PlayerCharacter createPlayer(String characterID, Battle battle, Workbook workbook) {
        CharacterSheetParameterMap parameterMap = CharacterSheetParser.parseCharacterSheet(workbook);
        PlayerCharacter character = createCharacter(characterID, battle, parameterMap, PlayerCharacter::new);

        // load occupation
        character.profession.set(parameterMap.getValueAsStringOrElse("character.occupation", ""));

        // load age
        character.age.set(parameterMap.getValueAsStringOrElse("character.age", ""));

        // load age
        character.race.set(parameterMap.getValueAsStringOrElse("character.race", ""));

        // load age
        character.gender.set(parameterMap.getValueAsStringOrElse("character.gender", ""));

        // load inventory
        for (int i = 1; i <= 21; i++) {
            String nameX = parameterMap.getValueAsStringOrElse("character.inventory." + i, "");

            if (nameX.isBlank()) continue;

            Item itemX = Database.getItemOrElse(nameX, null);
            // TODO if null create DB entry
            if (itemX != null) {
                character.inventory.add(itemX);
            }
        }

        // load currency
        int cp = parameterMap.getValueAsIntegerOrElse("character.money.copper", 0);
        int sp = parameterMap.getValueAsIntegerOrElse("character.money.silver", 0);
        int gp = parameterMap.getValueAsIntegerOrElse("character.money.gold", 0);
        character.currency.set(new Currency(cp, sp, gp));

        // load history
        character.history.set(parameterMap.getValueAsStringOrElse("character.history", ""));

        return character;
    }

    private static <C extends PnPCharacter> C createCharacter(String characterID, Battle battle, Workbook workbook, PnPCharacterProducer<C> producer) {
        C character = createCharacter(characterID, battle, CharacterSheetParser.parseCharacterSheet(workbook), producer);

        String lootName = Utility.getConfig().getString("character.sheet.loot");
        if (LanguageUtility.hasMessage("character.sheet." + lootName)) {
            lootName = LanguageUtility.getMessage("character.sheet." + lootName);
        }

        Sheet loot = workbook.getSheet(lootName);

        for (Row row : loot) {
            if (row.getRowNum() > 0) {
                String name = WorkbookUtility.getValue(row, 0);

                if (name.isBlank() || name.equals("0")) {
                    continue;
                }

                int amount = (int) row.getCell(1).getNumericCellValue();
                double chance = row.getCell(2).getNumericCellValue();

                character.lootTable.add(name, amount, chance);
            }
        }

        return character;
    }

    private static <C extends PnPCharacter> C createCharacter(String characterID, Battle battle, CharacterSheetParameterMap parameterMap, PnPCharacterProducer<C> producer) {
        C character = producer.create(characterID, battle);
        character.name = new SimpleStringProperty(parameterMap.getValueAsStringOrElse("character.name", LanguageUtility.getMessage("battleMember.defaultName")));

        //Primary
        int minPrim = Utility.getConfig().getInt("character.primaryAttributes.min");
        character.primaryAttributes.put(PrimaryAttribute.STRENGTH, parameterMap.getValueAsIntegerOrElse("primaryAttribute.strength", minPrim));
        character.primaryAttributes.put(PrimaryAttribute.ENDURANCE, parameterMap.getValueAsIntegerOrElse("primaryAttribute.endurance", minPrim));
        character.primaryAttributes.put(PrimaryAttribute.PRECISION, parameterMap.getValueAsIntegerOrElse("primaryAttribute.precision", minPrim));
        character.primaryAttributes.put(PrimaryAttribute.AGILITY, parameterMap.getValueAsIntegerOrElse("primaryAttribute.agility", minPrim));
        character.primaryAttributes.put(PrimaryAttribute.RESILIENCE, parameterMap.getValueAsIntegerOrElse("primaryAttribute.resilience", minPrim));
        character.primaryAttributes.put(PrimaryAttribute.CHARISMA, parameterMap.getValueAsIntegerOrElse("primaryAttribute.charisma", minPrim));
        character.primaryAttributes.put(PrimaryAttribute.INTELLIGENCE, parameterMap.getValueAsIntegerOrElse("primaryAttribute.intelligence", minPrim));
        character.primaryAttributes.put(PrimaryAttribute.DEXTERITY, parameterMap.getValueAsIntegerOrElse("primaryAttribute.dexterity", minPrim));

        //Secondary
        character.secondaryAttributes.put(SecondaryAttribute.MELEE_DAMAGE, parameterMap.getValueAsIntegerOrElse("secondaryAttribute.meleeDamage", 0));
        character.secondaryAttributes.put(SecondaryAttribute.RANGE_DAMAGE, parameterMap.getValueAsIntegerOrElse("secondaryAttribute.rangeDamage", 0));
        character.secondaryAttributes.put(SecondaryAttribute.MAGIC_POWER, parameterMap.getValueAsIntegerOrElse("secondaryAttribute.magicPower", 0));
        character.secondaryAttributes.put(SecondaryAttribute.HEALTH, parameterMap.getValueAsIntegerOrElse("secondaryAttribute.health", 1));
        character.secondaryAttributes.put(SecondaryAttribute.MENTAL_HEALTH, parameterMap.getValueAsIntegerOrElse("secondaryAttribute.mentalHealth", 0));
        character.secondaryAttributes.put(SecondaryAttribute.MANA, parameterMap.getValueAsIntegerOrElse("secondaryAttribute.mana", 1));
        character.secondaryAttributes.put(SecondaryAttribute.INITIATIVE, parameterMap.getValueAsIntegerOrElse("secondaryAttribute.initiative", 1));
        character.secondaryAttributes.put(SecondaryAttribute.DEFENSE, parameterMap.getValueAsIntegerOrElse("secondaryAttribute.defense", 1));
        character.level = new SimpleIntegerProperty(parameterMap.getValueAsIntegerOrElse("character.level", 1));
        character.createModifierBindings();
        character.createResourceProperties();

        // TODO if null, we need to create database entry
        String armorHead = parameterMap.getValueAsStringOrElse("character.armor.head", "");
        if (!armorHead.isBlank()) {
            Item itemHead = Database.getItemOrElse(armorHead, null);
            if (itemHead != null) {
                Armor item = (Armor) itemHead.copy();
                character.getEquippedArmor().put(ArmorPosition.HEAD, item);
            }
        }

        String armorChest = parameterMap.getValueAsStringOrElse("character.armor.upperBody", "");
        if (!armorChest.isBlank()) {
            Item itemChest = Database.getItemOrElse(armorChest, null);
            if (itemChest != null) {
                Armor item = (Armor) itemChest.copy();
                character.getEquippedArmor().put(ArmorPosition.UPPER_BODY, item);
            }
        }

        String armorLegs = parameterMap.getValueAsStringOrElse("character.armor.legs", "");
        if (!armorLegs.isBlank()) {
            Item itemLegs = Database.getItemOrElse(armorLegs, null);
            if (itemLegs != null) {
                Armor item = (Armor) itemLegs.copy();
                character.getEquippedArmor().put(ArmorPosition.LEGS, item);
            }
        }

        String armorArms = parameterMap.getValueAsStringOrElse("character.armor.arm", "");
        if (!armorArms.isBlank()) {
            Item itemArms = Database.getItemOrElse(armorArms, null);
            if (itemArms != null) {
                Armor item = (Armor) itemArms.copy();
                character.getEquippedArmor().put(ArmorPosition.ARM, item);
            }
        }

        // TODO currently assume that weapons 1 and 2 are equipped
        // TODO use weapon stats if the weapon is not in the database
        addWeapon(character, parameterMap.getValueAsStringOrElse("character.weapon.1", ""), true);
        addWeapon(character, parameterMap.getValueAsStringOrElse("character.weapon.2", ""), true);
        addWeapon(character, parameterMap.getValueAsStringOrElse("character.weapon.3", ""), false);
        addWeapon(character, parameterMap.getValueAsStringOrElse("character.weapon.4", ""), false);

        // load rings
        int amountOfRings = Utility.getConfig().getInt("character.jewellery.amount.ring");
        for (int i = 1; i <= amountOfRings; i++) {
            String ringx = parameterMap.getValueAsStringOrElse("character.armor.ring." + i, "");
            Item ringItemx = Database.getItemOrElse(ringx, null);
            // TODO if null create DB entry
            if (ringItemx != null) {
                character.getEquippedJewellery().add((Jewellery) ringItemx);
            }
        }

        // load amulet
        String amulet = parameterMap.getValueAsStringOrElse("character.armor.amulet", "");
        Item amuletItem = Database.getItemOrElse(amulet, null);
        if (amuletItem != null) {
            character.getEquippedJewellery().add((Jewellery) amuletItem);
        }

        // load bracelets
        String bracelet1 = parameterMap.getValueAsStringOrElse("character.armor.bracelet.1", "");
        String bracelet2 = parameterMap.getValueAsStringOrElse("character.armor.bracelet.2", "");
        Item bracelet1Item = Database.getItemOrElse(bracelet1, null);
        Item bracelet2Item = Database.getItemOrElse(bracelet2, null);
        if (bracelet1Item != null) {
            character.getEquippedJewellery().add((Jewellery) bracelet1Item);
        }
        if (bracelet2Item != null) {
            character.getEquippedJewellery().add((Jewellery) bracelet2Item);
        }

        // load misc
        for (int i = 1; i <= 4; i++) {
            String miscX = parameterMap.getValueAsStringOrElse("character.armor.misc." + i, "");
            Item miscItemX = Database.getItemOrElse(miscX, null);
            // TODO if null create DB entry
            if (miscItemX != null) {
                character.getEquippedJewellery().add((Jewellery) miscItemX);
            }
        }

        // load spells
        for (int i = 1; i <= 13; i++) {
            String nameX = parameterMap.getValueAsStringOrElse("character.spell." + i, "");
            Spell spellX = Database.getSpellOrElse(nameX, null);
            // TODO if null create DB entry
            if (spellX != null) {
                character.spells.add(spellX);
            }
        }

        // load talents
        Stream<String> talentKeys = parameterMap.keySet().stream().filter(key -> key.startsWith("talent."));
        talentKeys.forEach(key -> {
            // TODO imrpovised weapon needs primary attributes

            String talentName = LanguageUtility.getMessageProperty(key).get();
            Talent talent = Database.getTalent(talentName);

            character.talents.put(talent, new SimpleIntegerProperty(parameterMap.getValueAsInteger(key)));
        });

        character.advantages.add(parameterMap.getValueAsStringOrElse("character.advantages", ""));
        character.disadvantages.add(parameterMap.getValueAsStringOrElse("character.disadvantages", ""));

        return character;
    }

    private static void addWeapon(IPnPCharacter character, String weaponName, boolean equipped) {
        if (!weaponName.isBlank()) {
            Item weaponItem = Database.getItemOrElse(weaponName, null);

            if ( weaponItem != null) {
                Item copy = weaponItem.copy();
                if (copy instanceof Weapon) {
                    if (equipped) {
                        character.getEquippedWeapons().add((IWeapon) copy);
                    } else {
                        character.getWeapons().add((IWeapon) copy);
                    }
                }
            }
        }
    }
}
