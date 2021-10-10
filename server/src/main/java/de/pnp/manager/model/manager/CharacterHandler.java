package de.pnp.manager.model.manager;

import de.pnp.manager.model.Battle;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.other.ITalent;
import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.message.character.RevokeCharactersMessage;
import de.pnp.manager.network.message.character.update.talent.UpdateTalentsNotificationMessage;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;

import java.rmi.server.UID;
import java.util.*;

public class CharacterHandler {

    private static short ID_COUNTER = 0;

    protected static synchronized String getNextCharacterID(){
        return "P-" + new UID(++ID_COUNTER);
    }

    // maps sessionId to character
    protected Map<String, ObservableList<PnPCharacter>> sessionCharacterMap;
    protected Manager manager;

    public CharacterHandler(Manager manager) {
        this.sessionCharacterMap = new HashMap<>();
        this.manager = manager;
    }

    /**
     * Creates a character which can be moved between multiple
     * battles. This character will be persisted between session
     * restarts.
     *
     * @param sessionID the id of the session
     * @param battle the first battle (can be null)
     * @param producer the function to create the character
     * @return the resulting character
     */
    public <C extends PnPCharacter> C createCharacter(String sessionID, Battle battle, PnPCharacterProducer<C> producer) {
        C character = producer.create(getNextCharacterID(), battle);
        this.sessionCharacterMap.computeIfAbsent(sessionID, k -> createObservableList()).add(character);

        return character;
    }

    /**
     * Creates a character that will be used in one
     * battle. This character won't get persisted.
     *
     * @param battle the battle of the user
     * @param producer the function to create the character
     * @return the resulting one time use character
     */
    public <C extends PnPCharacter> C createOneTimeCharacter(@NotNull Battle battle, PnPCharacterProducer<C> producer) {
        return producer.create(getNextCharacterID(), battle);
    }

    public void deleteCharacter(String sessionID, String characterID) {
        Collection<PnPCharacter> characters = this.sessionCharacterMap.get(sessionID);

        if (characters != null) {
            characters.removeIf(battle -> battle.getCharacterID().equals(characterID));
            manager.getNetworkHandler().activeBroadcast(
                    new RevokeCharactersMessage(characterID, Calendar.getInstance().getTime()),
                    manager.getNetworkHandler().clientsProperty().filtered(c -> c.getControlledCharacters().contains(characterID))
            );
        }
    }

    public void deleteCharacter(String characterID) {
        for (List<PnPCharacter> characters : this.sessionCharacterMap.values()) {
            characters.removeIf(battle -> battle.getCharacterID().equals(characterID));
        }
    }

    public ObservableList<PnPCharacter> getCharacters(String sessionID) {
        return sessionCharacterMap.computeIfAbsent(sessionID, id -> createObservableList());
    }

    public PnPCharacter getCharacter(String sessionID, String characterID) {
        return getCharacters(sessionID).stream().filter(c -> c.getCharacterID().equals(characterID)).findFirst().orElse(null);
    }

    private ObservableList<PnPCharacter> createObservableList() {
        ObservableList<PnPCharacter> list = FXCollections.observableArrayList();

        list.addListener((ListChangeListener<PnPCharacter>) listChange -> {
            while (listChange.next()) {
                if (listChange.wasAdded()) {
                    for (PnPCharacter character : listChange.getAddedSubList()) {

                        character.getObservableTalents().addListener((MapChangeListener<ITalent, IntegerProperty>) mapChange -> {
                            if (mapChange.wasAdded()) {
                                mapChange.getValueAdded().addListener((ob, o, n) -> {
                                    Calendar calendar = Calendar.getInstance();

                                    for (Client client : manager.getNetworkHandler().clientsProperty()) {
                                        if (client.getControlledCharacters().contains(character.getCharacterID())) {
                                            client.sendMessage(
                                                    new UpdateTalentsNotificationMessage(
                                                            character.getCharacterID(),
                                                            mapChange.getKey(),
                                                            n.intValue(),
                                                            calendar.getTime()
                                                    )
                                            );
                                        }
                                    }
                                });
                            }
                        });
                        for (ITalent talent : character.getObservableTalents().keySet()) {
                            IntegerProperty property = character.getObservableTalents().get(talent);

                            property.addListener((ob, o, n) -> {
                                Calendar calendar = Calendar.getInstance();

                                for (Client client : manager.getNetworkHandler().clientsProperty()) {
                                    if (client.getControlledCharacters().contains(character.getCharacterID())) {
                                        client.sendMessage(
                                                new UpdateTalentsNotificationMessage(
                                                        character.getCharacterID(),
                                                        talent,
                                                        n.intValue(),
                                                        calendar.getTime()
                                                )
                                        );
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });

        return list;
    }
}
