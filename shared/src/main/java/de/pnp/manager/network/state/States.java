package de.pnp.manager.network.state;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class States {

    public static final State PRE_LOGIN = new State("PreLogin");
    public static final State LOGGED_IN = new State("LoggedIn");
    public static final State IN_SESSION = new State("InSession");
    public static final State IN_CHARACTER = new State("InCharacter");

    public static final State START = PRE_LOGIN;

    public static final Set<State> STATES = new HashSet<>(Arrays.asList(PRE_LOGIN, LOGGED_IN, IN_SESSION, IN_CHARACTER));
}
