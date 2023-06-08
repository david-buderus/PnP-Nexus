package de.pnp.manager.network.eventhandler.equipment;

import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.manager.Manager;
import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.character.equipment.ChangeEquippedWeaponsRequestMessage;
import de.pnp.manager.network.message.character.equipment.EquipmentType;
import de.pnp.manager.network.message.character.equipment.EquipmentUpdateNotificationMessage;
import de.pnp.manager.network.message.error.DeniedMessage;
import de.pnp.manager.network.message.error.NotPossibleMessage;
import de.pnp.manager.network.state.INonConditionalEventHandler;

import java.util.Calendar;

import static de.pnp.manager.main.LanguageUtility.getMessage;

public class ChangeEquippedWeaponsHandler implements INonConditionalEventHandler<BaseMessage> {

    protected Client client;
    protected Calendar calendar;
    protected Manager manager;

    public ChangeEquippedWeaponsHandler(Client client, Calendar calendar, Manager manager) {
        this.client = client;
        this.calendar = calendar;
        this.manager = manager;
    }

    @Override
    public void applyNonConditionalEvent(BaseMessage message) {
        if (message instanceof ChangeEquippedWeaponsRequestMessage) {
            ChangeEquippedWeaponsRequestMessage.ChangeEquippedWeaponsData data = ((ChangeEquippedWeaponsRequestMessage) message).getData();

            if (client.getControlledCharacters().contains(data.getCharacterID())) {
                PnPCharacter character = manager.getCharacterHandler().getCharacter(client.getSessionID(), data.getCharacterID());

                if (character != null
                        && character.getWeapons().containsAll(data.getToEquip())
                        && character.getEquippedWeapons().containsAll(data.getToUnequip())
                ) {
                    character.getEquippedWeapons().removeAll(data.getToUnequip());
                    character.getEquippedWeapons().addAll(data.getToEquip());

                    manager.getNetworkHandler().broadcast(new EquipmentUpdateNotificationMessage(
                            data.getCharacterID(),
                            EquipmentType.EQUIPPED_WEAPON,
                            data.getToEquip(),
                            data.getToUnequip(),
                            calendar.getTime()
                    ), manager.getNetworkHandler().getClientsWithCharacterControl(data.getCharacterID()));

                } else {
                    client.sendMessage(new NotPossibleMessage(getMessage("message.items.amount.notEnough"), calendar.getTime()));
                }

            } else {
                client.sendMessage(new DeniedMessage(getMessage("message.error.denied.character"), calendar.getTime()));
            }
        }
    }
}