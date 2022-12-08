package de.pnp.manager.network.message;

/**
 * <pre>
 * Format: SONN
 * Example: 1203
 *
 * S := State (1 := PreLogin, 2 := Logged In, ..., 8 := Universal Response, 9 := Error Response)
 * O := Origin (0 := Server, 1 := DM, 2 := Player/DM, 3 := everyone)
 * N := ID of the message
 * </pre>
 */
public abstract class MessageIDs {

    //Pre Login
    public static final int LOGIN_REQUEST = 1200;
    public static final int LOGIN_RESPONSE = 1000;

    //Logged In
    public static final int QUERY_SESSIONS = 2200;
    public static final int SESSION_QUERY_RESPONSE = 2000;
    public static final int LOGOUT_REQUEST = 2201;
    public static final int JOIN_SESSION_REQUEST = 2202;
    public static final int JOIN_SESSION_RESPONSE = 2002;

    //In Session
    public static final int UPDATE_SESSION = 3000;
    public static final int DATABASE_REQUEST = 3201;
    public static final int DATABASE_RESPONSE = 3001;

    //In Character
    public static final int REVOKE_CHARACTERS = 4301;
    public static final int UPDATE_TALENTS_NOTIFICATION = 4002;
    public static final int UPDATE_INVENTORY_NOTIFICATION = 4003;
    public static final int UPDATE_TALENTS_REQUEST = 4302;
    public static final int MOVE_ITEM_REQUEST = 4303;
    public static final int CREATE_ITEM_REQUEST = 4304;
    public static final int DELETE_ITEM_REQUEST = 4305;
    public static final int FABRICATE_ITEM_REQUEST = 4206;
    public static final int EQUIP_REQUEST = 4307;
    public static final int UNEQUIP_REQUEST = 4308;
    public static final int CHANGE_EQUIPPED_WEAPONS = 4009;
    public static final int UPDATE_EQUIPMENT_NOTIFICATION = 4010;

    //Universal
    public static final int OK = 8000;
    public static final int LEAVE_SESSION_REQUEST = 8201;
    public static final int ASSIGN_CHARACTERS = 8001;
    public static final int CONTROLLED_CHARACTER_REQUEST = 8203;
    public static final int CONTROLLED_CHARACTER_RESPONSE = 8003;
    public static final int ASSIGN_INVENTORIES = 8004;
    public static final int REVOKE_INVENTORIES = 8005;
    public static final int ACCESSIBLE_CONTAINER_REQUEST = 8206;
    public static final int ACCESSIBLE_CONTAINER_RESPONSE = 8006;

    //Error
    public static final int ERROR = 9000;
    public static final int WRONG_STATE = 9001;
    public static final int DENIED = 9002;
    public static final int NOT_FOUND = 9003;
    public static final int NOT_POSSIBLE = 9004;
}
