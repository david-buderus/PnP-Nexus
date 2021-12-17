package de.pnp.manager.model.character;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.pnp.manager.model.item.IArmor;
import de.pnp.manager.model.item.IJewellery;
import de.pnp.manager.model.item.IWeapon;
import de.pnp.manager.model.loot.ILootTable;
import de.pnp.manager.model.character.data.IArmorPosition;
import de.pnp.manager.model.character.data.IPrimaryAttribute;
import de.pnp.manager.model.character.data.ISecondaryAttribute;
import de.pnp.manager.model.character.state.IMemberState;
import de.pnp.manager.model.other.ISpell;
import de.pnp.manager.model.other.ITalent;

import java.util.Collection;
import java.util.Map;

public interface IPnPCharacter {

    /**
     * The name of the character
     */
    String getName();

    /**
     * The unique id (unique per session) of the character
     */
    String getCharacterID();

    /**
     * The level of the character
     */
    int getLevel();

    /**
     * The tier of the character
     */
    @JsonIgnore
    default int getTier() {
        return (int) Math.ceil(((float) getLevel()) / 5);
    }

    /**
     * The advantages of the character
     */
    Collection<String> getAdvantages();

    /**
     * The advantages of the character
     */
    Collection<String> getDisadvantages();

    /**
     * The current health
     */
    int getHealth();

    /**
     * The max health of the character.
     * Includes all modifier
     */
    int getMaxHealth();

    /**
     * If the player is dead
     */
    @JsonIgnore
    default boolean isDead() {
        return getHealth() <= 0 || getMentalHealth() <= 0;
    }

    /**
     * The current mana
     */
    int getMana();

    /**
     * The max mana of the character.
     * Includes all modifier
     */
    int getMaxMana();

    /**
     * The current mental health
     */
    int getMentalHealth();

    /**
     * The max mental health of the character.
     * Includes all modifier
     */
    int getMaxMentalHealth();

    /**
     * The initiative of the character
     * used to calculate his turn timer in a battle.
     * Includes all modifier
     */
    @JsonIgnore
    int getInitiative();

    /**
     * The base defense of the character
     * used to calculate incoming damage.
     * Includes all modifier
     */
    @JsonIgnore
    int getBaseDefense();

    /**
     * The base value of each primary attribute
     */
    Map<IPrimaryAttribute, Integer> getPrimaryAttributes();

    /**
     * The base value of each secondary attribute
     */
    Map<ISecondaryAttribute, Integer> getSecondaryAttributes();

    /**
     * The memberstates of the character
     */
    Collection<IMemberState> getMemberStates();

    /**
     * All weapons the character has
     */
    Collection<IWeapon> getWeapons();

    /**
     * The weapons the character currently uses.
     * Always a subset of {@link #getWeapons()}
     */
    Collection<IWeapon> getEquippedWeapons();

    /**
     * The jewellery the character currently uses
     */
    Collection<IJewellery> getEquippedJewellery();

    /**
     * The armor the character currently uses
     */
    Map<IArmorPosition, IArmor> getEquippedArmor();

    /**
     * The protection the character has at the specific position
     */
    @JsonIgnore
    default int getProtection(IArmorPosition position) {
        IArmor armor = getEquippedArmor().get(position);
        return armor != null ? armor.getProtectionWithWear() : 0;
    }

    /**
     * The protection given by all equipped shields
     */
    @JsonIgnore
    default int getShieldProtection() {
        int protection = 0;

        for (IWeapon weapon : getEquippedWeapons()) {
            if (weapon.isShield()) {
                protection += weapon.getDamage();
            }
        }

        return protection;
    }

    /**
     * All spells the character can use
     */
    Collection<ISpell> getSpells();

    /**
     * All skillpoints of each talent of the character.
     * All talents not in the map should be interpreted as '0'.
     */
    Map<ITalent, Integer> getTalents();

    /**
     * The inventory of the character
     */
    IInventory getInventory();

    /**
     * Loottable of the character.
     * This includes all equipment of the character
     * that should drop and the inventory.
     */
    ILootTable getLootTable();
}
