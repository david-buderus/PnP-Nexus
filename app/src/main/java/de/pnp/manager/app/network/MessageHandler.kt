package de.pnp.manager.app.network

import de.pnp.manager.app.model.Client
import de.pnp.manager.app.state.ApplicationState
import de.pnp.manager.network.message.MessageIDs
import de.pnp.manager.network.message.login.LoginResponseMessage
import de.pnp.manager.network.message.session.SessionQueryResponse
import de.pnp.manager.network.state.BaseMessageStateMachine
import de.pnp.manager.network.state.StateMachine
import de.pnp.manager.network.state.States

object MessageHandler {

    val stateMachine = BaseMessageStateMachine(States.STATES, States.START)

    init {
        // Pre login
        stateMachine.registerTransition(States.PRE_LOGIN, States.LOGGED_IN, MessageIDs.LOGIN_RESPONSE) {
            val message = it as LoginResponseMessage
            val client =  Client(message.data.id, message.data.name)
            ApplicationState.client = client
        }

        // Logged In
        stateMachine.registerTransition(States.LOGGED_IN, MessageIDs.SESSION_QUERY_RESPONSE) {
            val message = it as SessionQueryResponse
            ApplicationState.sessions.clear()
            ApplicationState.sessions.addAll(message.data)
        }
    }
}