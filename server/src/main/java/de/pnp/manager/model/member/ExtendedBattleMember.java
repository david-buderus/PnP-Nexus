package de.pnp.manager.model.member;

import de.pnp.manager.main.CharacterSheetParameterMap;
import de.pnp.manager.main.Database;
import de.pnp.manager.main.LanguageUtility;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import de.pnp.manager.model.Battle;
import de.pnp.manager.model.attribute.IPrimaryAttribute;
import de.pnp.manager.model.attribute.ISecondaryAttribute;
import de.pnp.manager.model.item.Armor;
import de.pnp.manager.model.item.Jewellery;
import de.pnp.manager.model.item.Weapon;
import de.pnp.manager.model.loot.LootTable;
import de.pnp.manager.model.member.data.ArmorPiece;
import de.pnp.manager.model.member.data.AttackTypes;
import de.pnp.manager.model.member.generation.PrimaryAttribute;
import de.pnp.manager.model.member.generation.SecondaryAttribute;
import de.pnp.manager.model.member.interfaces.IBattleMember;
import de.pnp.manager.model.member.interfaces.IExtendedBattleMember;
import de.pnp.manager.model.member.state.interfaces.IDefenseMemberState;
import de.pnp.manager.model.member.state.interfaces.IMemberState;
import de.pnp.manager.model.other.Spell;
import de.pnp.manager.model.other.Talent;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.*;
import java.util.stream.Stream;

public abstract class ExtendedBattleMember extends BattleMember implements IExtendedBattleMember {

    protected final Map<IPrimaryAttribute, IntegerProperty> primaryAttributes = new HashMap<>();
    protected final Map<ISecondaryAttribute, IntegerProperty> secondaryAttributeModifier = new HashMap<>();

    protected final HashMap<Talent, IntegerProperty> talents = new HashMap<>();
    protected final ObservableList<Weapon> weapons = FXCollections.observableArrayList();
    protected final ObservableList<Armor> armors = FXCollections.observableArrayList();
    protected final ObservableList<Jewellery> jewellery = FXCollections.observableArrayList();
    protected final ObservableList<Spell> spells = FXCollections.observableArrayList();

    protected final StringProperty notes = new SimpleStringProperty("");

    protected ExtendedBattleMember(Battle battle) {
        super(battle);
    }

    protected ExtendedBattleMember(Battle battle, LootTable lootTable, HashMap<ArmorPiece, IntegerProperty> armor) {
        super(battle, lootTable, armor);
    }

    protected ExtendedBattleMember(Battle battle, LootTable lootTable) {
        super(battle, lootTable);
    }

    protected ExtendedBattleMember(CharacterSheetParameterMap parameterMap) {
        super(parameterMap);

        // load spells
        for (int i = 1; i <= 13; i++) {
            String nameX = parameterMap.getValueAsStringOrElse("character.spell." + i, "");
            Spell spellX = Database.getSpellOrElse(nameX, null);
            // TODO if null create DB entry
            if (spellX != null) {
                spells.add(spellX);
            }
        }

        // load talents
        Stream<String> talentKeys = parameterMap.keySet().stream().filter(key -> key.startsWith("talent."));
        talentKeys.forEach(key -> {
            // TODO imrpovised weapon needs primary attributes

            String talentName = LanguageUtility.getMessageProperty(key).get();
            Talent talent = Database.getTalent(talentName);

            this.talents.put(talent, new SimpleIntegerProperty(parameterMap.getValueAsInteger(key)));
        });

        // load missing secondary health
        this.secondaryAttributes.put(SecondaryAttribute.mentalHealth, new SimpleIntegerProperty(parameterMap.getValueAsIntegerOrElse("secondaryAttribute.mentalHealth", 0)));
        this.secondaryAttributes.put(SecondaryAttribute.meleeDamage, new SimpleIntegerProperty(parameterMap.getValueAsIntegerOrElse("secondaryAttribute.meleeDamage", 0)));
        this.secondaryAttributes.put(SecondaryAttribute.rangeDamage, new SimpleIntegerProperty(parameterMap.getValueAsIntegerOrElse("secondaryAttribute.rangeDamage", 0)));
        this.secondaryAttributes.put(SecondaryAttribute.magicPower, new SimpleIntegerProperty(parameterMap.getValueAsIntegerOrElse("secondaryAttribute.magicPower", 0)));

        // load primary attributes

        primaryAttributes.put(PrimaryAttribute.strength, new SimpleIntegerProperty(parameterMap.getValueAsIntegerOrElse("primaryAttribute.strength", 1)));
        primaryAttributes.put(PrimaryAttribute.endurance, new SimpleIntegerProperty(parameterMap.getValueAsIntegerOrElse("primaryAttribute.endurance", 1)));
        primaryAttributes.put(PrimaryAttribute.precision, new SimpleIntegerProperty(parameterMap.getValueAsIntegerOrElse("primaryAttribute.precision", 1)));
        primaryAttributes.put(PrimaryAttribute.agility, new SimpleIntegerProperty(parameterMap.getValueAsIntegerOrElse("primaryAttribute.agility", 1)));
        primaryAttributes.put(PrimaryAttribute.resilience, new SimpleIntegerProperty(parameterMap.getValueAsIntegerOrElse("primaryAttribute.resilience", 1)));
        primaryAttributes.put(PrimaryAttribute.charisma, new SimpleIntegerProperty(parameterMap.getValueAsIntegerOrElse("primaryAttribute.charisma", 1)));
        primaryAttributes.put(PrimaryAttribute.intelligence, new SimpleIntegerProperty(parameterMap.getValueAsIntegerOrElse("primaryAttribute.intelligence", 1)));
        primaryAttributes.put(PrimaryAttribute.dexterity, new SimpleIntegerProperty(parameterMap.getValueAsIntegerOrElse("primaryAttribute.dexterity", 1)));

    }

    protected ExtendedBattleMember(Workbook wb) {
        super(wb);
    }

    @Override
    public IntegerProperty getTalent(Talent talent) {
        return talents.get(talent);
    }

    @Override
    public Collection<Talent> getTalents() {
        return talents.keySet();
    }

    @Override
    public IntegerProperty getAttribute(IPrimaryAttribute attribute) {
        return primaryAttributes.get(attribute);
    }

    @Override
    public ReadOnlyIntegerProperty getAttribute(SecondaryAttribute attribute) {
        return secondaryAttributes.get(attribute);
    }

    @Override
    public IntegerProperty getModifier(SecondaryAttribute attribute) {
        return secondaryAttributeModifier.get(attribute);
    }

    @Override
    public ObservableList<Weapon> getWeapons() {
        return weapons;
    }

    @Override
    public ObservableList<Armor> getArmor() {
        return armors;
    }

    @Override
    public ObservableList<Jewellery> getJewelleries() {
        return jewellery;
    }

    @Override
    public ObservableList<Spell> getSpells() {
        return spells;
    }

    @Override
    public IntegerProperty strengthProperty() {
        return primaryAttributes.get(PrimaryAttribute.strength);
    }

    @Override
    public IntegerProperty enduranceProperty() {
        return primaryAttributes.get(PrimaryAttribute.endurance);
    }

    @Override
    public IntegerProperty dexterityProperty() {
        return primaryAttributes.get(PrimaryAttribute.dexterity);
    }

    @Override
    public IntegerProperty intelligenceProperty() {
        return primaryAttributes.get(PrimaryAttribute.intelligence);
    }

    @Override
    public IntegerProperty charismaProperty() {
        return primaryAttributes.get(PrimaryAttribute.charisma);
    }

    @Override
    public IntegerProperty resilienceProperty() {
        return primaryAttributes.get(PrimaryAttribute.resilience);
    }

    @Override
    public IntegerProperty agilityProperty() {
        return primaryAttributes.get(PrimaryAttribute.agility);
    }

    @Override
    public IntegerProperty precisionProperty() {
        return primaryAttributes.get(PrimaryAttribute.precision);
    }

    @Override
    public StringProperty notesProperty() {
        return notes;
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

    @Override
    protected int calculateDefense(AttackTypes type, boolean withShield, double penetration, double block) {
        int defense = 0;
        double reduction = 1 - penetration;

        switch (type) {
            case direct:
                return 0;
            case head:
                defense += getArmorItem(ArmorPiece.head).isPresent() ? getArmorItem(ArmorPiece.head).get().getProtectionWithWear() : 0;
                break;
            case upperBody:
                defense += getArmorItem(ArmorPiece.upperBody).isPresent() ? getArmorItem(ArmorPiece.upperBody).get().getProtectionWithWear() : 0;
                break;
            case arm:
                defense += getArmorItem(ArmorPiece.arm).isPresent() ? getArmorItem(ArmorPiece.arm).get().getProtectionWithWear() : 0;
                break;
            case legs:
                defense += getArmorItem(ArmorPiece.legs).isPresent() ? getArmorItem(ArmorPiece.legs).get().getProtectionWithWear() : 0;
                break;
        }
        if (withShield) {
            defense += (getArmorItem(ArmorPiece.shield).isPresent() ? getArmorItem(ArmorPiece.shield).get().getProtectionWithWear() : 0) * block;
        }
        defense *= reduction;

        for (IMemberState state : this.states) {
            if (state instanceof IDefenseMemberState) {
                defense = ((IDefenseMemberState) state).apply(this, defense);
            }
        }

        return defense + baseDefenseProperty().get();
    }

    @Override
    public void takeDamage(int amount, AttackTypes type, boolean withShield, double penetration, double block, IBattleMember source) {
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

    private Optional<Armor> getArmorItem(ArmorPiece armorPiece) {
        return this.armors.stream().filter(item -> ArmorPiece.getArmorPiece(item.getSubtype()).equals(armorPiece)).findFirst();
    }
}
