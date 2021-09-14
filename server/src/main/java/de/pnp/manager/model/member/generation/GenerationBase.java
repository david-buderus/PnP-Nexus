package de.pnp.manager.model.member.generation;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.member.data.ArmorPosition;
import de.pnp.manager.model.member.data.PrimaryAttribute;
import de.pnp.manager.model.member.data.SecondaryAttribute;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import de.pnp.manager.model.interfaces.WithToStringProperty;
import de.pnp.manager.model.item.Armor;
import de.pnp.manager.model.item.Jewellery;
import de.pnp.manager.model.item.Weapon;
import de.pnp.manager.model.loot.LootTable;
import de.pnp.manager.model.member.BattleMember;
import de.pnp.manager.model.other.Talent;

import java.util.*;

public abstract class GenerationBase implements WithToStringProperty {

    protected Random random;
    protected String name;

    protected Collection<String> advantages;
    protected Collection<String> disadvantages;

    protected boolean dropsWeapon;
    protected boolean dropsArmor;
    protected boolean dropsJewellery;

    protected boolean ableToUsesPrimaryHand;
    protected boolean ableToUsesSecondaryHand;
    protected boolean ableToUseShield;
    protected Map<ArmorPosition, Boolean> ableToUseArmor;
    protected boolean ableToUseJewellery;
    protected boolean ableToUseSpells;
    protected boolean usesAlwaysShield;

    protected boolean usesExclusivelySpecificPrimaryWeapons;
    protected boolean usesExclusivelySpecificSecondaryWeapons;
    protected HashMap<ArmorPosition, Boolean> usesExclusivelySpecificArmor;
    protected boolean usesExclusivelySpecificJewellery;

    protected Collection<Talent> mainTalents;
    protected Collection<Talent> forbiddenTalents;
    protected Collection<GenerationBase> parents;
    protected Collection<PrimaryAttribute> primaryAttributes;
    protected Collection<SecondaryAttribute> secondaryAttributes;
    protected Collection<Drop> drops;
    protected Collection<String> primaryWeaponTypes;
    protected Collection<String> secondaryWeaponTypes;
    protected Collection<Weapon> specificPrimaryWeapons;
    protected Collection<Weapon> specificSecondaryWeapons;
    protected Map<ArmorPosition, Collection<Armor>> specificArmor;
    protected Collection<Jewellery> specificJewellery;

    protected GenerationBase() {
        this.random = new Random();
        this.specificArmor = new HashMap<>();
        this.ableToUseArmor = new HashMap<>();
        this.usesExclusivelySpecificArmor = new HashMap<>();
        this.advantages = new ArrayList<>();
        this.disadvantages = new ArrayList<>();
        this.mainTalents = new ArrayList<>();
        this.forbiddenTalents = new ArrayList<>();
        this.parents = new ArrayList<>();
        this.primaryAttributes = new ArrayList<>();
        this.secondaryAttributes = new ArrayList<>();
        this.drops = new ArrayList<>();
        this.primaryWeaponTypes = new ArrayList<>();
        this.secondaryWeaponTypes = new ArrayList<>();
        this.specificPrimaryWeapons = new ArrayList<>();
        this.specificSecondaryWeapons = new ArrayList<>();
        this.specificJewellery = new ArrayList<>();
    }

    public LootTable getLootTable(BattleMember member) {
        LootTable result = new LootTable();

        for (GenerationBase parent : parents) {
            result.add(parent.getLootTable(member));
        }

        for (Drop drop : drops) {
            drop.addToLootTable(result, member);
        }

        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<String> getAdvantages() {
        Collection<String> result = new ArrayList<>(advantages);
        for (GenerationBase parent : parents) {
            result.addAll(parent.getAdvantages());
        }
        return result;
    }

    public void setAdvantages(Collection<String> advantages) {
        this.advantages = advantages;
    }

    public Collection<String> getDisadvantages() {
        Collection<String> result = new ArrayList<>(disadvantages);
        for (GenerationBase parent : parents) {
            result.addAll(parent.getDisadvantages());
        }
        return result;
    }

    public void setDisadvantages(Collection<String> disadvantages) {
        this.disadvantages = disadvantages;
    }

    public boolean dropsWeapon() {
        return dropsWeapon && parents.stream().allMatch(GenerationBase::dropsWeapon);
    }

    public void setDropsWeapon(boolean dropsWeapon) {
        this.dropsWeapon = dropsWeapon;
    }

    public boolean dropsArmor() {
        return dropsArmor && parents.stream().allMatch(GenerationBase::dropsArmor);
    }

    public void setDropsArmor(boolean dropsArmor) {
        this.dropsArmor = dropsArmor;
    }

    public boolean dropsJewellery() {
        return dropsJewellery && parents.stream().allMatch(GenerationBase::dropsJewellery);
    }

    public void setDropsJewellery(boolean dropsJewellery) {
        this.dropsJewellery = dropsJewellery;
    }

    public boolean isAbleToUsesPrimaryHand() {
        return ableToUsesPrimaryHand && parents.stream().allMatch(GenerationBase::isAbleToUsesPrimaryHand);
    }

    public void setAbleToUsesPrimaryHand(boolean ableToUsesPrimaryHand) {
        this.ableToUsesPrimaryHand = ableToUsesPrimaryHand;
    }

    public boolean isAbleToUsesSecondaryHand() {
        return ableToUsesSecondaryHand && parents.stream().allMatch(GenerationBase::isAbleToUsesSecondaryHand);
    }

    public void setAbleToUsesSecondaryHand(boolean ableToUsesSecondaryHand) {
        this.ableToUsesSecondaryHand = ableToUsesSecondaryHand;
    }

    public boolean isAbleToUseShield() {
        return ableToUseShield && parents.stream().allMatch(GenerationBase::isAbleToUseShield);
    }

    public void setAbleToUseShield(boolean ableToUseShield) {
        this.ableToUseShield = ableToUseShield;
    }

    public boolean isAbleToUseArmor(ArmorPosition position) {
        return ableToUseArmor.getOrDefault(position, false) && parents.stream().allMatch(g -> g.isAbleToUseArmor(position));
    }

    public void setAbleToUseArmor(ArmorPosition position, boolean ableToUseArmor) {
        this.ableToUseArmor.put(position, ableToUseArmor);
    }

    public boolean isAbleToUseJewellery() {
        return ableToUseJewellery && parents.stream().allMatch(GenerationBase::isAbleToUseJewellery);
    }

    public void setAbleToUseJewellery(boolean ableToUseJewellery) {
        this.ableToUseJewellery = ableToUseJewellery;
    }

    public boolean isAbleToUseSpells() {
        return ableToUseSpells || parents.stream().anyMatch(GenerationBase::isAbleToUseSpells);
    }

    public void setAbleToUseSpells(boolean ableToUseSpells) {
        this.ableToUseSpells = ableToUseSpells;
    }

    public boolean usesAlwaysShield() {
        return usesAlwaysShield || parents.stream().anyMatch(GenerationBase::usesAlwaysShield);
    }

    public boolean usesExclusivelySpecificPrimaryWeapons() {
        return usesExclusivelySpecificPrimaryWeapons || parents.stream().anyMatch(GenerationBase::usesExclusivelySpecificPrimaryWeapons);
    }

    public void setUsesExclusivelySpecificPrimaryWeapons(boolean usesExclusivelySpecificPrimaryWeapons) {
        this.usesExclusivelySpecificPrimaryWeapons = usesExclusivelySpecificPrimaryWeapons;
    }

    public boolean usesExclusivelySpecificSecondaryWeapons() {
        return usesExclusivelySpecificSecondaryWeapons || parents.stream().anyMatch(GenerationBase::usesExclusivelySpecificSecondaryWeapons);
    }

    public void setUsesExclusivelySpecificSecondaryWeapons(boolean usesExclusivelySpecificSecondaryWeapons) {
        this.usesExclusivelySpecificSecondaryWeapons = usesExclusivelySpecificSecondaryWeapons;
    }

    public boolean usesExclusivelySpecificArmor(ArmorPosition armorPosition) {
        return this.usesExclusivelySpecificArmor.getOrDefault(armorPosition, false)
                || parents.stream().anyMatch(base -> base.usesExclusivelySpecificArmor(armorPosition));
    }

    public void setUsesExclusivelySpecificArmor(ArmorPosition armorPosition, boolean usesExclusivelySpecificArmor) {
        this.usesExclusivelySpecificArmor.put(armorPosition, usesExclusivelySpecificArmor);
    }

    public boolean usesExclusivelySpecificJewellery() {
        return usesExclusivelySpecificJewellery || parents.stream().anyMatch(GenerationBase::usesExclusivelySpecificJewellery);
    }

    public void setUsesExclusivelySpecificJewellery(boolean usesExclusivelySpecificJewellery) {
        this.usesExclusivelySpecificJewellery = usesExclusivelySpecificJewellery;
    }

    public void setUsesAlwaysShield(boolean usesAlwaysShield) {
        this.usesAlwaysShield = usesAlwaysShield;
    }

    public Collection<? extends GenerationBase> getParents() {
        return parents;
    }

    public void addParent(GenerationBase parent) {
        this.parents.add(parent);
    }

    public Collection<Talent> getMainTalents() {
        Collection<Talent> result = new ArrayList<>(mainTalents);
        for (GenerationBase parent : parents) {
            result.addAll(parent.getMainTalents());
        }
        return result;
    }

    public void addMainTalent(Talent talent) {
        this.mainTalents.add(talent);
    }

    public Collection<Talent> getForbiddenTalents() {
        Collection<Talent> result = new ArrayList<>(forbiddenTalents);
        for (GenerationBase parent : parents) {
            result.addAll(parent.getForbiddenTalents());
        }
        return result;
    }

    public void addForbiddenTalent(Talent talent) {
        this.forbiddenTalents.add(talent);
    }

    public Collection<PrimaryAttribute> getPrimaryAttributes() {
        Collection<PrimaryAttribute> result = new ArrayList<>(primaryAttributes);
        for (GenerationBase parent : parents) {
            result.addAll(parent.getPrimaryAttributes());
        }
        return result;
    }

    public void setPrimaryAttributes(Collection<PrimaryAttribute> primaryAttributes) {
        this.primaryAttributes = primaryAttributes;
    }

    public Collection<SecondaryAttribute> getSecondaryAttributes() {
        Collection<SecondaryAttribute> result = new ArrayList<>(secondaryAttributes);
        for (GenerationBase parent : parents) {
            result.addAll(parent.getSecondaryAttributes());
        }
        return result;
    }

    public void setSecondaryAttributes(Collection<SecondaryAttribute> secondaryAttributes) {
        this.secondaryAttributes = secondaryAttributes;
    }

    public Collection<Drop> getDrops() {
        return drops;
    }

    public void setDrops(Collection<Drop> drops) {
        this.drops = drops;
    }

    public Collection<Weapon> getSpecificPrimaryWeapons() {
        Collection<Weapon> result = new ArrayList<>(specificPrimaryWeapons);
        for (GenerationBase parent : parents) {
            result.addAll(parent.getSpecificPrimaryWeapons());
        }
        return result;
    }

    public void setSpecificPrimaryWeapons(Collection<Weapon> specificPrimaryWeapons) {
        this.specificPrimaryWeapons = specificPrimaryWeapons;
    }

    public Collection<Weapon> getSpecificSecondaryWeapons() {
        Collection<Weapon> result = new ArrayList<>(specificSecondaryWeapons);
        for (GenerationBase parent : parents) {
            result.addAll(parent.getSpecificSecondaryWeapons());
        }
        return result;
    }

    public void setSpecificSecondaryWeapons(Collection<Weapon> specificSecondaryWeapons) {
        this.specificSecondaryWeapons = specificSecondaryWeapons;
    }

    public Collection<Armor> getSpecificArmor(ArmorPosition position) {
        Collection<Armor> result = new ArrayList<>(specificArmor.getOrDefault(position, Collections.emptyList()));
        for (GenerationBase parent : parents) {
            result.addAll(parent.getSpecificArmor(position));
        }
        return result;
    }

    public void setSpecificArmor(ArmorPosition position, Collection<Armor> armor) {
        this.specificArmor.put(position, armor);
    }

    public Collection<Jewellery> getSpecificJewellery() {
        return specificJewellery;
    }

    public void setSpecificJewellery(Collection<Jewellery> specificJewellery) {
        this.specificJewellery = specificJewellery;
    }

    public Collection<String> getPrimaryWeaponTypes() {
        Collection<String> result = new ArrayList<>(primaryWeaponTypes);
        for (GenerationBase parent : parents) {
            result.addAll(parent.getPrimaryWeaponTypes());
        }
        return result;
    }

    public void setPrimaryWeaponTypes(Collection<String> primaryWeaponTypes) {
        this.primaryWeaponTypes = primaryWeaponTypes;
    }

    public Collection<String> getSecondaryWeaponTypes() {
        Collection<String> result = new ArrayList<>(secondaryWeaponTypes);
        for (GenerationBase parent : parents) {
            result.addAll(parent.getSecondaryWeaponTypes());
        }
        return result;
    }

    public void setSecondaryWeaponTypes(Collection<String> secondaryWeaponTypes) {
        this.secondaryWeaponTypes = secondaryWeaponTypes;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return new ReadOnlyStringWrapper(name);
    }

    public Collection<String> getPropertyList() {
        Collection<String> list = new ArrayList<>();

        if (dropsWeapon()) {
            list.add(LanguageUtility.getMessage("search.enemies.properties.dropWeapon"));
        }
        if (dropsArmor()) {
            list.add(LanguageUtility.getMessage("search.enemies.properties.dropArmor"));
        }
        if (dropsJewellery()) {
            list.add(LanguageUtility.getMessage("search.enemies.properties.dropJewellery"));
        }
        if (isAbleToUsesPrimaryHand()) {
            list.add(LanguageUtility.getMessage("search.enemies.properties.usesPrimaryHand"));
        }
        if (isAbleToUsesSecondaryHand()) {
            list.add(LanguageUtility.getMessage("search.enemies.properties.usesSecondaryHand"));
        }
        if (isAbleToUseShield()) {
            list.add(LanguageUtility.getMessage("search.enemies.properties.canUseShield"));
        }
        if (isAbleToUseArmor(ArmorPosition.head)) {
            list.add(LanguageUtility.getMessage("search.enemies.properties.canUseHelm"));
        }
        if (isAbleToUseArmor(ArmorPosition.upperBody)) {
            list.add(LanguageUtility.getMessage("search.enemies.properties.canUseHarness"));
        }
        if (isAbleToUseArmor(ArmorPosition.arm)) {
            list.add(LanguageUtility.getMessage("search.enemies.properties.canUseBracers"));
        }
        if (isAbleToUseArmor(ArmorPosition.legs)) {
            list.add(LanguageUtility.getMessage("search.enemies.properties.canUseLegArmor"));
        }
        if (isAbleToUseJewellery()) {
            list.add(LanguageUtility.getMessage("search.enemies.properties.canUseJewellery"));
        }
        if (usesAlwaysShield()) {
            list.add(LanguageUtility.getMessage("search.enemies.properties.useAlwaysShield"));
        }
        if (isAbleToUseSpells()) {
            list.add(LanguageUtility.getMessage("search.enemies.properties.canUseSpells"));
        }
        if (usesExclusivelySpecificPrimaryWeapons()) {
            list.add(LanguageUtility.getMessage("search.enemies.properties.usesOnlySpecificPrimaryHand"));
        }
        if (usesExclusivelySpecificSecondaryWeapons()) {
            list.add(LanguageUtility.getMessage("search.enemies.properties.usesOnlySpecificSecondaryHand"));
        }
        if (usesExclusivelySpecificArmor(ArmorPosition.head)) {
            list.add(LanguageUtility.getMessage("search.enemies.properties.usesOnlySpecificHelm"));
        }
        if (usesExclusivelySpecificArmor(ArmorPosition.upperBody)) {
            list.add(LanguageUtility.getMessage("search.enemies.properties.usesOnlySpecificHarness"));
        }
        if (usesExclusivelySpecificArmor(ArmorPosition.arm)) {
            list.add(LanguageUtility.getMessage("search.enemies.properties.usesOnlySpecificBracers"));
        }
        if (usesExclusivelySpecificArmor(ArmorPosition.legs)) {
            list.add(LanguageUtility.getMessage("search.enemies.properties.usesOnlySpecificLegArmor"));
        }
        if (usesExclusivelySpecificJewellery()) {
            list.add(LanguageUtility.getMessage("search.enemies.properties.usesOnlySpecificJewellery"));
        }

        return list;
    }
}
