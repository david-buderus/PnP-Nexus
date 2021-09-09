package de.pnp.manager.model.member;

import de.pnp.manager.model.battle.info.IAttackType;

public interface IBattleMember extends IMember {

    void nextTurn();

    void heal(int amount, IBattleMember source);

    void takeDamage(int amount, IAttackType type, boolean withShield, double penetration, double block, IBattleMember source);

    int getLife();

    void setLife(int life);

    int getMaxLife();

    void setMaxLife(int maxLife);

    int getMana();

    void setMana(int mana);

    int getMaxMana();

    void setMaxMana(int maxMana);

    default void decreaseMana(int amount, IBattleMember source) {
        this.setMana(Math.max(0, this.getMana() - amount));
    }

    default void increaseMana(int amount, IBattleMember source) {
        this.setMana(Math.min(this.getMaxMana(), this.getMana() + amount));
    }
}
