package de.pnp.manager.model.member;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.pnp.manager.model.item.IArmor;
import de.pnp.manager.model.item.IJewellery;
import de.pnp.manager.model.item.IWeapon;
import de.pnp.manager.model.loot.ILootTable;
import de.pnp.manager.model.member.data.IArmorPosition;
import de.pnp.manager.model.member.data.IPrimaryAttribute;
import de.pnp.manager.model.member.data.ISecondaryAttribute;
import de.pnp.manager.model.member.state.IMemberState;
import de.pnp.manager.model.other.ISpell;
import de.pnp.manager.model.other.ITalent;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@JsonSerialize(as = IPnPCharacter.class)
public class PnPCharacter implements IPnPCharacter {

    protected StringProperty name;
    protected IntegerProperty level;

    protected IntegerProperty health;
    protected IntegerProperty mana;
    protected IntegerProperty mentalHealth;
    protected ListProperty<IMemberState> memberStates;
    protected ListProperty<IWeapon> weapons;
    protected ListProperty<IWeapon> equippedWeapons;
    protected ListProperty<IJewellery> jewellery;
    protected ListProperty<IJewellery> equippedJewellery;
    protected ListProperty<IArmor> armor;
    protected MapProperty<IArmorPosition, IArmor> equippedArmor;
    protected ListProperty<ISpell> spells;
    protected MapProperty<ITalent, IntegerProperty> talents;

    protected IntegerProperty maxHealthModifier;
    protected IntegerBinding maxHealth;
    protected IntegerProperty maxManaModifier;
    protected IntegerBinding maxMana;
    protected IntegerProperty maxMentalHealthModifier;
    protected IntegerBinding maxMentalHealth;
    protected IntegerProperty staticInitiativeModifier;
    protected IntegerBinding staticInitiative;

    protected Collection<String> advantages;
    protected Collection<String> disadvantages;
    protected Map<IPrimaryAttribute, Integer> primaryAttributes;
    protected Map<ISecondaryAttribute, Integer> secondaryAttributes;
    protected ILootTable lootTable;
    protected IInventory inventory;

    @Override
    public int getInitiative() {
        return 0;
    }

    @Override
    public int getBaseDefense() {
        return 0;
    }

    @Override
    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    @Override
    public int getLevel() {
        return level.get();
    }

    public IntegerProperty levelProperty() {
        return level;
    }

    public void setLevel(int level) {
        this.level.set(level);
    }

    @Override
    public Collection<String> getAdvantages() {
        return advantages;
    }

    @Override
    public Collection<String> getDisadvantages() {
        return disadvantages;
    }

    @Override
    public int getHealth() {
        return health.get();
    }

    public IntegerProperty healthProperty() {
        return health;
    }

    public void setHealth(int health) {
        this.health.set(health);
    }

    @Override
    public int getMaxHealth() {
        return maxHealth.get();
    }

    public IntegerBinding maxHealthProperty() {
        return maxHealth;
    }

    public int getMaxHealthModifier() {
        return maxHealthModifier.get();
    }

    public IntegerProperty maxHealthModifierProperty() {
        return maxHealthModifier;
    }

    public void setMaxHealthModifier(int maxHealthModifier) {
        this.maxHealthModifier.set(maxHealthModifier);
    }

    @Override
    public int getMana() {
        return mana.get();
    }

    public IntegerProperty manaProperty() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana.set(mana);
    }

    @Override
    public int getMaxMana() {
        return maxMana.get();
    }

    public IntegerBinding maxManaProperty() {
        return maxMana;
    }

    public int getMaxManaModifier() {
        return maxManaModifier.get();
    }

    public IntegerProperty maxManaModifierProperty() {
        return maxManaModifier;
    }

    public void setMaxManaModifier(int maxManaModifier) {
        this.maxManaModifier.set(maxManaModifier);
    }

    @Override
    public int getMentalHealth() {
        return mentalHealth.get();
    }

    public IntegerProperty mentalHealthProperty() {
        return mentalHealth;
    }

    public void setMentalHealth(int mentalHealth) {
        this.mentalHealth.set(mentalHealth);
    }

    @Override
    public int getMaxMentalHealth() {
        return maxMentalHealth.get();
    }

    public IntegerBinding maxMentalHealthProperty() {
        return maxMentalHealth;
    }

    public int getMaxMentalHealthModifier() {
        return maxMentalHealthModifier.get();
    }

    public IntegerProperty maxMentalHealthModifierProperty() {
        return maxMentalHealthModifier;
    }

    public void setMaxMentalHealthModifier(int maxMentalHealthModifier) {
        this.maxMentalHealthModifier.set(maxMentalHealthModifier);
    }

    @Override
    public int getStaticInitiative() {
        return staticInitiative.get();
    }

    public IntegerBinding staticInitiativeProperty() {
        return staticInitiative;
    }

    public int getStaticInitiativeModifier() {
        return staticInitiativeModifier.get();
    }

    public IntegerProperty staticInitiativeModifierProperty() {
        return staticInitiativeModifier;
    }

    public void setStaticInitiativeModifier(int staticInitiativeModifier) {
        this.staticInitiativeModifier.set(staticInitiativeModifier);
    }

    @Override
    public Map<IPrimaryAttribute, Integer> getPrimaryAttributes() {
        return primaryAttributes;
    }

    @Override
    public Map<ISecondaryAttribute, Integer> getSecondaryAttributes() {
        return secondaryAttributes;
    }

    @Override
    public ObservableList<IMemberState> getMemberStates() {
        return memberStates.get();
    }

    public ListProperty<IMemberState> memberStatesProperty() {
        return memberStates;
    }

    public void setMemberStates(ObservableList<IMemberState> memberStates) {
        this.memberStates.set(memberStates);
    }

    @Override
    public ObservableList<IWeapon> getWeapons() {
        return weapons.get();
    }

    public ListProperty<IWeapon> weaponsProperty() {
        return weapons;
    }

    public void setWeapons(ObservableList<IWeapon> weapons) {
        this.weapons.set(weapons);
    }

    @Override
    public ObservableList<IWeapon> getEquippedWeapons() {
        return equippedWeapons.get();
    }

    public ListProperty<IWeapon> equippedWeaponsProperty() {
        return equippedWeapons;
    }

    public void setEquippedWeapons(ObservableList<IWeapon> equippedWeapons) {
        this.equippedWeapons.set(equippedWeapons);
    }

    @Override
    public ObservableList<IJewellery> getJewellery() {
        return jewellery.get();
    }

    public ListProperty<IJewellery> jewelleryProperty() {
        return jewellery;
    }

    public void setJewellery(ObservableList<IJewellery> jewellery) {
        this.jewellery.set(jewellery);
    }

    @Override
    public ObservableList<IJewellery> getEquippedJewellery() {
        return equippedJewellery.get();
    }

    public ListProperty<IJewellery> equippedJewelleryProperty() {
        return equippedJewellery;
    }

    public void setEquippedJewellery(ObservableList<IJewellery> equippedJewellery) {
        this.equippedJewellery.set(equippedJewellery);
    }

    @Override
    public ObservableList<IArmor> getArmor() {
        return armor.get();
    }

    public ListProperty<IArmor> armorProperty() {
        return armor;
    }

    public void setArmor(ObservableList<IArmor> armor) {
        this.armor.set(armor);
    }

    @Override
    public ObservableMap<IArmorPosition, IArmor> getEquippedArmor() {
        return equippedArmor.get();
    }

    public MapProperty<IArmorPosition, IArmor> equippedArmorProperty() {
        return equippedArmor;
    }

    public void setEquippedArmor(ObservableMap<IArmorPosition, IArmor> equippedArmor) {
        this.equippedArmor.set(equippedArmor);
    }

    @Override
    public ObservableList<ISpell> getSpells() {
        return spells.get();
    }

    public ListProperty<ISpell> spellsProperty() {
        return spells;
    }

    public void setSpells(ObservableList<ISpell> spells) {
        this.spells.set(spells);
    }

    @Override
    public Map<ITalent, Integer> getTalents() {
        return talents.get().entrySet()
                .stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().get()
                ));
    }

    public ObservableMap<ITalent, IntegerProperty> getObservableTalents() {
        return talents.get();
    }

    public void setTalents(Map<ITalent, IntegerProperty> talents) {
        for (ITalent talent : talents.keySet()) {
            this.talents.put(talent, talents.get(talent));
        }
    }

    public MapProperty<ITalent, IntegerProperty> talentsProperty() {
        return talents;
    }

    public void setObservableTalents(ObservableMap<ITalent, IntegerProperty> talents) {
        this.talents.set(talents);
    }

    @Override
    public IInventory getInventory() {
        return inventory;
    }

    @Override
    public ILootTable getLootTable() {
        return lootTable;
    }
}
