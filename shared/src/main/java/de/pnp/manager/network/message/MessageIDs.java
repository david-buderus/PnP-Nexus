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

    //Universal
    public static final int OK = 8000;

    //Error
    public static final int ERROR = 9000;
    public static final int WRONG_STATE = 9001;
    public static final int DENIED = 9002;
}
