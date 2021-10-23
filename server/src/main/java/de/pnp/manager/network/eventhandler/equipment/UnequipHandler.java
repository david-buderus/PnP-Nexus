package de.pnp.manager.network.eventhandler.equipment;

import de.pnp.manager.model.character.IInventory;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.character.data.ArmorPosition;
import de.pnp.manager.model.item.Armor;
import de.pnp.manager.model.item.IEquipment;
import de.pnp.manager.model.item.Jewellery;
import de.pnp.manager.model.item.Weapon;
import de.pnp.manager.model.manager.Manager;
import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.character.equipment.EquipmentData;
import de.pnp.manager.network.message.character.equipment.EquipmentType;
import de.pnp.manager.network.message.character.equipment.EquipmentUpdateNotificationMessage;
import de.pnp.manager.network.message.character.equipment.UnequipRequestMessage;
import de.pnp.manager.network.message.error.DeniedMessage;
import de.pnp.manager.network.message.error.NotPossibleMessage;
import de.pnp.manager.network.message.inventory.InventoryUpdateNotificationMessage;
import de.pnp.manager.network.state.INonConditionalEventHandler;

import java.util.Calendar;
import java.util.Collections;
import java.util.NoSuchElementException;

import static de.pnp.manager.main.LanguageUtility.getMessage;
import static javafx.application.Platform.runLater;

public class UnequipHandler implements INonConditionalEventHandler<BaseMessage> {

    protected Client client;
    protected Calendar calendar;
    protected Manager manager;

    public UnequipHandler(Client client, Calendar calendar, Manager manager) {
        this.client = client;
        this.calendar = calendar;
        this.manager = manager;
    }

    @Override
    public void applyNonConditionalEvent(BaseMessage message) {
        if (message instanceof UnequipRequestMessage) {
            EquipmentData data = ((UnequipRequestMessage) message).getData();

            if (client.hasAccessToInventory(data.getInventoryID())) {
                if (client.getControlledCharacters().contains(data.getCharacterID())) {
                    IInventory inventory = manager.getInventoryHandler().getInventory(client.getSessionID(), data.getInventoryID());

                    if (inventory != null && inventory.hasSpaceFor(data.getEquipment())) {

                        PnPCharacter character = manager.getCharacterHandler().getCharacter(client.getSessionID(), data.getCharacterID());

                        switch (data.getType()) {
                            case EQUIPPED_WEAPON:
                            case WEAPON:
                                handleWeapon(data, inventory, character);
                                break;
                            case ARMOR:
                                handleArmor(data, inventory, character);
                                break;
                            case JEWELLERY:
                                handleJewellery(data, inventory, character);
                                break;
                        }

                    } else {
                        client.sendMessage(new NotPossibleMessage(getMessage("message.items.space.notEnough"), calendar.getTime()));
                    }

                } else {
                    client.sendMessage(new DeniedMessage(getMessage("message.error.denied.character"), calendar.getTime()));
                }
            } else {
                client.sendMessage(new DeniedMessage(getMessage("message.error.denied.inventory"), calendar.getTime()));
            }
        }
    }

    private void handleWeapon(EquipmentData data, IInventory inventory, PnPCharacter character) {
        try {
            Weapon weapon = (Weapon) data.getEquipment();

            if (data.getType() == EquipmentType.EQUIPPED_WEAPON && !character.getEquippedWeapons().contains(weapon)) {
                client.sendMessage(new NotPossibleMessage(getMessage("message.equipment.notEquipped"), calendar.getTime()));

            } else {
                if (character.getWeapons().contains(weapon)) {

                    if (character.getEquippedWeapons().contains(weapon)) {
                        manager.getNetworkHandler().broadcast(new EquipmentUpdateNotificationMessage(
                                data.getCharacterID(),
                                EquipmentType.EQUIPPED_WEAPON,
                                Collections.emptyList(),
                                Collections.singleton(weapon),
                                calendar.getTime()
                        ), manager.getNetworkHandler().getClientsWithCharacterControl(data.getCharacterID()));
                    }

                    runLater(() -> {
                        character.getWeapons().remove(weapon);
                        character.getEquippedWeapons().remove(weapon);
                    });
                    inventory.add(weapon);

                    manager.getNetworkHandler().broadcast(new EquipmentUpdateNotificationMessage(
                            data.getCharacterID(),
                            EquipmentType.WEAPON,
                            Collections.emptyList(),
                            Collections.singleton(weapon),
                            calendar.getTime()
                    ), manager.getNetworkHandler().getClientsWithCharacterControl(data.getCharacterID()));

                    manager.getNetworkHandler().broadcast(new InventoryUpdateNotificationMessage(
                            data.getInventoryID(),
                            Collections.singleton(weapon),
                            Collections.emptyList(),
                            calendar.getTime()
                    ), manager.getNetworkHandler().getClientsWithInventoryAccess(data.getInventoryID()));

                } else {
                    client.sendMessage(new NotPossibleMessage(getMessage("message.equipment.notEquipped"), calendar.getTime()));
                }
            }


        } catch (ClassCastException e) {
            client.sendMessage(new NotPossibleMessage(getMessage("message.equipment.wrongType"), calendar.getTime()));
        }
    }

    private void handleArmor(EquipmentData data, IInventory inventory, PnPCharacter character) {
        try {
            Armor armor = (Armor) data.getEquipment();
            ArmorPosition position = ArmorPosition.getArmorPosition(armor.getType());

            if (character.getEquippedArmor().get(position).equals(armor)) {

                runLater(() -> character.getEquippedArmor().put(position, null));
                inventory.add(armor);

                sendNotification(data, armor);

            } else {
                client.sendMessage(new NotPossibleMessage(getMessage("message.equipment.notEquipped"), calendar.getTime()));
            }

        } catch (ClassCastException e) {
            client.sendMessage(new NotPossibleMessage(getMessage("message.equipment.wrongType"), calendar.getTime()));
        } catch (NoSuchElementException e) {
            client.sendMessage(new NotPossibleMessage(getMessage("message.equipment.unknownType"), calendar.getTime()));
        }
    }

    private void handleJewellery(EquipmentData data, IInventory inventory, PnPCharacter character) {
        try {
            Jewellery jewellery = (Jewellery) data.getEquipment();

            if (character.getEquippedJewellery().contains(jewellery)) {

                runLater(() -> character.getEquippedJewellery().remove(jewellery));
                inventory.add(jewellery);

                sendNotification(data, jewellery);

            } else {
                client.sendMessage(new NotPossibleMessage(getMessage("message.equipment.notEquipped"), calendar.getTime()));
            }

        } catch (ClassCastException e) {
            client.sendMessage(new NotPossibleMessage(getMessage("message.equipment.wrongType"), calendar.getTime()));
        }
    }

    private void sendNotification(EquipmentData data, IEquipment equipment) {
        manager.getNetworkHandler().broadcast(new InventoryUpdateNotificationMessage(
                data.getInventoryID(),
                Collections.singleton(equipment),
                Collections.emptyList(),
                calendar.getTime()
        ), manager.getNetworkHandler().getClientsWithInventoryAccess(data.getInventoryID()));

        manager.getNetworkHandler().broadcast(new EquipmentUpdateNotificationMessage(
                data.getCharacterID(),
                data.getType(),
                Collections.emptyList(),
                Collections.singleton(equipment),
                calendar.getTime()
        ), manager.getNetworkHandler().getClientsWithCharacterControl(data.getCharacterID()));
    }
}
