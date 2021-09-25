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
    public static final int DISMISS_CHARACTERS = 4301;
    public static final int UPDATE_TALENTS_NOTIFICATION = 4002;
    public static final int UPDATE_INVENTORY_NOTIFICATION = 4003;
    public static final int UPDATE_TALENTS_REQUEST = 4302;
    public static final int MOVE_ITEM_REQUEST = 4303;

    //Universal
    public static final int OK = 8000;
    public static final int LEAVE_SESSION_REQUEST = 8201;
    public static final int ASSIGN_CHARACTERS = 8001;
    public static final int CONTROLLED_CHARACTER_REQUEST = 8203;
    public static final int CONTROLLED_CHARACTER_RESPONSE = 8003;

    //Error
    public static final int ERROR = 9000;
    public static final int WRONG_STATE = 9001;
    public static final int DENIED = 9002;
    public static final int NOT_FOUND = 9003;
    public static final int NOT_POSSIBLE = 9004;
}
