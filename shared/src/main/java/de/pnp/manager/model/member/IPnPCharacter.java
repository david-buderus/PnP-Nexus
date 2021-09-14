package de.pnp.manager.model.member;

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

import java.util.Collection;
import java.util.Map;

public interface IPnPCharacter {

    /**
     * The name of the character
     */
    String getName();

    /**
     * The level of the character
     */
    int getLevel();

    /**
     * The tier of the character
     */
    default int getTier() {
        return (int) Math.ceil(((float) getLevel())/5);
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
    default boolean isDead() {
        return getHealth() <= 0;
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
    int getInitiative();

    /**
     * The initiative of the character.
     * Includes weapons except memberstates
     */
    int getStaticInitiative();

    /**
     * The base defense of the character
     * used to calculate incoming damage.
     * Includes all modifier
     */
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
     * The weapons the character currently uses
     */
    Collection<IWeapon> getEquippedWeapons();

    /**
     * All jewellery the character has
     */
    Collection<IJewellery> getJewellery();

    /**
     * The jewellery the character currently uses
     */
    Collection<IJewellery> getEquippedJewellery();

    /**
     * All jewellery the character has
     */
    Collection<IArmor> getArmor();

    /**
     * The jewellery the character currently uses
     */
    Map<IArmorPosition, IArmor> getEquippedArmor();

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
