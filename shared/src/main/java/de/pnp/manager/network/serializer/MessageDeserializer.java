package de.pnp.manager.network.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.character.AssignCharactersMessage;
import de.pnp.manager.network.message.character.ControlledCharacterRequestMessage;
import de.pnp.manager.network.message.character.ControlledCharacterResponseMessage;
import de.pnp.manager.network.message.character.RevokeCharactersMessage;
import de.pnp.manager.network.message.character.update.talent.UpdateTalentsNotificationMessage;
import de.pnp.manager.network.message.character.update.talent.UpdateTalentsRequestMessage;
import de.pnp.manager.network.message.database.DatabaseRequestMessage;
import de.pnp.manager.network.message.database.DatabaseResponseMessage;
import de.pnp.manager.network.message.error.*;
import de.pnp.manager.network.message.inventory.*;
import de.pnp.manager.network.message.login.LoginRequestMessage;
import de.pnp.manager.network.message.login.LoginResponseMessage;
import de.pnp.manager.network.message.login.LogoutRequestMessage;
import de.pnp.manager.network.message.session.*;
import de.pnp.manager.network.message.universal.OkMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static de.pnp.manager.network.message.MessageIDs.*;

public class MessageDeserializer extends StdDeserializer<BaseMessage> {

    protected Map<Integer, Class<? extends BaseMessage>> typeClassMap;

    public MessageDeserializer() {
        super(BaseMessage.class);
        this.typeClassMap = new HashMap<>();

        //Pre login
        this.typeClassMap.put(LOGIN_REQUEST, LoginRequestMessage.class);
        this.typeClassMap.put(LOGIN_RESPONSE, LoginResponseMessage.class);

        //Logged In
        this.typeClassMap.put(LOGOUT_REQUEST, LogoutRequestMessage.class);
        this.typeClassMap.put(QUERY_SESSIONS, QuerySessions.class);
        this.typeClassMap.put(SESSION_QUERY_RESPONSE, SessionQueryResponse.class);
        this.typeClassMap.put(JOIN_SESSION_REQUEST, JoinSessionRequestMessage.class);
        this.typeClassMap.put(JOIN_SESSION_RESPONSE, JoinSessionResponseMessage.class);

        //In Session
        this.typeClassMap.put(UPDATE_SESSION, UpdateSessionMessage.class);
        this.typeClassMap.put(DATABASE_RESPONSE, DatabaseResponseMessage.class);
        this.typeClassMap.put(DATABASE_REQUEST, DatabaseRequestMessage.class);

        //In Character
        this.typeClassMap.put(REVOKE_CHARACTERS, RevokeCharactersMessage.class);
        this.typeClassMap.put(UPDATE_TALENTS_NOTIFICATION, UpdateTalentsNotificationMessage.class);
        this.typeClassMap.put(UPDATE_TALENTS_REQUEST, UpdateTalentsRequestMessage.class);
        this.typeClassMap.put(UPDATE_INVENTORY_NOTIFICATION, InventoryUpdateNotificationMessage.class);
        this.typeClassMap.put(MOVE_ITEM_REQUEST, MoveItemRequestMessage.class);
        this.typeClassMap.put(CREATE_ITEM_REQUEST, CreateItemRequestMessage.class);
        this.typeClassMap.put(DELETE_ITEM_REQUEST, DeleteItemRequestMessage.class);
        this.typeClassMap.put(FABRICATE_ITEM_REQUEST, FabricateItemRequestMessage.class);

        //Universal
        this.typeClassMap.put(OK, OkMessage.class);
        this.typeClassMap.put(LEAVE_SESSION_REQUEST, LeaveSessionRequestMessage.class);
        this.typeClassMap.put(ASSIGN_CHARACTERS, AssignCharactersMessage.class);
        this.typeClassMap.put(CONTROLLED_CHARACTER_REQUEST, ControlledCharacterRequestMessage.class);
        this.typeClassMap.put(CONTROLLED_CHARACTER_RESPONSE, ControlledCharacterResponseMessage.class);
        this.typeClassMap.put(ASSIGN_INVENTORIES, AssignInventoryMessage.class);
        this.typeClassMap.put(REVOKE_INVENTORIES, RevokeInventoriesMessage.class);

        //Error
        this.typeClassMap.put(NOT_FOUND, NotFoundMessage.class);
        this.typeClassMap.put(WRONG_STATE, WrongStateMessage.class);
        this.typeClassMap.put(DENIED, DeniedMessage.class);
        this.typeClassMap.put(NOT_POSSIBLE, NotPossibleMessage.class);
    }

    @Override
    public BaseMessage deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        JsonNode node = parser.getCodec().readTree(parser);

        try {
            return mapper.treeToValue(node, typeClassMap.get(node.get("id").asInt()));
        } catch (IllegalArgumentException e) {
            throw new IOException("Unknown message id: " + node.get("id").asInt());
        }
    }
}
