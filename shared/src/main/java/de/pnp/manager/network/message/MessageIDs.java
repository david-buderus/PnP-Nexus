package de.pnp.manager.network.message;

/**
 * <pre>
 * Format: SONN
 * Example: 1203
 *
 * S := State (1 := PreLogin, 2 := Logged In, ...)
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
}
