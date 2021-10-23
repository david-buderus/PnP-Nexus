package de.pnp.manager.network.eventhandler.equipment;

import de.pnp.manager.main.Utility;
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
import de.pnp.manager.network.message.character.equipment.EquipRequestMessage;
import de.pnp.manager.network.message.character.equipment.EquipmentType;
import de.pnp.manager.network.message.character.equipment.EquipmentUpdateNotificationMessage;
import de.pnp.manager.network.message.error.DeniedMessage;
import de.pnp.manager.network.message.error.NotPossibleMessage;
import de.pnp.manager.network.message.inventory.InventoryUpdateNotificationMessage;
import de.pnp.manager.network.state.INonConditionalEventHandler;
import org.apache.commons.configuration2.Configuration;

import java.util.*;

import static de.pnp.manager.main.LanguageUtility.getMessage;
import static javafx.application.Platform.runLater;

public class EquipHandler implements INonConditionalEventHandler<BaseMessage> {

    protected Client client;
    protected Calendar calendar;
    protected Manager manager;

    public EquipHandler(Client client, Calendar calendar, Manager manager) {
        this.client = client;
        this.calendar = calendar;
        this.manager = manager;
    }

    @Override
    public void applyNonConditionalEvent(BaseMessage message) {
        if (message instanceof EquipRequestMessage) {
            EquipmentData data = ((EquipRequestMessage) message).getData();

            if (client.hasAccessToInventory(data.getInventoryID())) {
                if (client.getControlledCharacters().contains(data.getCharacterID())) {
                    IInventory inventory = manager.getInventoryHandler().getInventory(client.getSessionID(), data.getInventoryID());

                    if (inventory != null && inventory.contains(data.getEquipment())) {

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
                        client.sendMessage(new NotPossibleMessage(getMessage("message.items.amount.notEnough"), calendar.getTime()));
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

            Configuration config = Utility.getConfig();

            if (character.getWeapons().size() < config.getInt("character.max_weapon_stored")) {

                runLater(() -> {
                    character.getWeapons().add(weapon);
                    if (data.getType() == EquipmentType.EQUIPPED_WEAPON) {
                        character.getEquippedWeapons().add(weapon);
                    }
                });
                inventory.remove(weapon);

                sendNotification(data, weapon);

            } else {
                client.sendMessage(new NotPossibleMessage(getMessage("message.equipment.noFreeSlot"), calendar.getTime()));
            }

        } catch (ClassCastException e) {
            client.sendMessage(new NotPossibleMessage(getMessage("message.equipment.wrongType"), calendar.getTime()));
        }
    }

    private void handleArmor(EquipmentData data, IInventory inventory, PnPCharacter character) {
        try {
            Armor armor = (Armor) data.getEquipment();
            ArmorPosition position = ArmorPosition.getArmorPosition(armor.getType());

            if (character.getEquippedArmor().get(position) == null) {

                runLater(() -> character.getEquippedArmor().put(position, armor));
                inventory.remove(armor);

                sendNotification(data, armor);

            } else {
                client.sendMessage(new NotPossibleMessage(getMessage("message.equipment.noFreeSlot"), calendar.getTime()));
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

            Map<String, Integer> slots = Utility.getJewelleryCharacterSlots();
            int max = slots.getOrDefault(jewellery.getType(), slots.get(null));

            if (character.getEquippedJewellery().stream().filter(j -> j.getType().equalsIgnoreCase(jewellery.getType())).count() < max) {

                runLater(() -> character.getEquippedJewellery().add(jewellery));
                inventory.remove(jewellery);

                sendNotification(data, jewellery);

            } else {
                client.sendMessage(new NotPossibleMessage(getMessage("message.equipment.noFreeSlot"), calendar.getTime()));
            }

        } catch (ClassCastException e) {
            client.sendMessage(new NotPossibleMessage(getMessage("message.equipment.wrongType"), calendar.getTime()));
        }
    }

    private void sendNotification(EquipmentData data, IEquipment equipment) {
        manager.getNetworkHandler().broadcast(new InventoryUpdateNotificationMessage(
                data.getInventoryID(),
                Collections.emptyList(),
                Collections.singleton(equipment),
                calendar.getTime()
        ), manager.getNetworkHandler().getClientsWithInventoryAccess(data.getInventoryID()));

        if (data.getType() == EquipmentType.EQUIPPED_WEAPON) {
            manager.getNetworkHandler().broadcast(new EquipmentUpdateNotificationMessage(
                    Arrays.asList(
                            new EquipmentUpdateNotificationMessage.EquipmentUpdateData(
                                    data.getCharacterID(),
                                    EquipmentType.EQUIPPED_WEAPON,
                                    Collections.singleton(equipment),
                                    Collections.emptyList()
                            ),
                            new EquipmentUpdateNotificationMessage.EquipmentUpdateData(
                                    data.getCharacterID(),
                                    EquipmentType.WEAPON,
                                    Collections.singleton(equipment),
                                    Collections.emptyList()
                            )
                    ),
                    calendar.getTime()
            ), manager.getNetworkHandler().getClientsWithCharacterControl(data.getCharacterID()));
        } else {
            manager.getNetworkHandler().broadcast(new EquipmentUpdateNotificationMessage(
                    data.getCharacterID(),
                    data.getType(),
                    Collections.singleton(equipment),
                    Collections.emptyList(),
                    calendar.getTime()
            ), manager.getNetworkHandler().getClientsWithCharacterControl(data.getCharacterID()));
        }
    }
}
