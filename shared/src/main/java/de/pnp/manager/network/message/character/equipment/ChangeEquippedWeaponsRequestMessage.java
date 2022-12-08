package de.pnp.manager.network.message.character.equipment;

import de.pnp.manager.model.item.IWeapon;
import de.pnp.manager.network.message.DataMessage;

import java.util.Collection;
import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.CHANGE_EQUIPPED_WEAPONS;

public class ChangeEquippedWeaponsRequestMessage extends DataMessage<ChangeEquippedWeaponsRequestMessage.ChangeEquippedWeaponsData> {

    public ChangeEquippedWeaponsRequestMessage() {
    }

    public ChangeEquippedWeaponsRequestMessage(String characterID, Collection<? extends IWeapon> toEquip, Collection<? extends IWeapon> toUnequip, Date timestamp) {
        this(new ChangeEquippedWeaponsData(characterID, toEquip, toUnequip), timestamp);
    }

    public ChangeEquippedWeaponsRequestMessage(ChangeEquippedWeaponsData data, Date timestamp) {
        super(CHANGE_EQUIPPED_WEAPONS, timestamp);
        this.setData(data);
    }

    public static class ChangeEquippedWeaponsData {

        protected String characterID;
        protected Collection<? extends IWeapon> toEquip;
        protected Collection<? extends IWeapon> toUnequip;

        public ChangeEquippedWeaponsData() {
        }

        public ChangeEquippedWeaponsData(String characterID, Collection<? extends IWeapon> toEquip, Collection<? extends IWeapon> toUnequip) {
            this.characterID = characterID;
            this.toEquip = toEquip;
            this.toUnequip = toUnequip;
        }

        public String getCharacterID() {
            return characterID;
        }

        public void setCharacterID(String characterID) {
            this.characterID = characterID;
        }

        public Collection<? extends IWeapon> getToEquip() {
            return toEquip;
        }

        public void setToEquip(Collection<? extends IWeapon> toEquip) {
            this.toEquip = toEquip;
        }

        public Collection<? extends IWeapon> getToUnequip() {
            return toUnequip;
        }

        public void setToUnequip(Collection<? extends IWeapon> toUnequip) {
            this.toUnequip = toUnequip;
        }
    }
}
