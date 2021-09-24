package de.pnp.manager.app.network

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import de.pnp.manager.app.MainActivity
import de.pnp.manager.app.SessionActivity
import de.pnp.manager.app.model.Client
import de.pnp.manager.app.state.ApplicationState
import de.pnp.manager.network.message.BaseMessage
import de.pnp.manager.network.message.MessageIDs
import de.pnp.manager.network.message.character.AssignCharactersMessage
import de.pnp.manager.network.message.database.DatabaseRequestMessage
import de.pnp.manager.network.message.database.DatabaseResponseMessage
import de.pnp.manager.network.message.login.LoginResponseMessage
import de.pnp.manager.network.message.session.JoinSessionResponseMessage
import de.pnp.manager.network.message.session.SessionQueryResponse
import de.pnp.manager.network.state.BaseMessageStateMachine
import de.pnp.manager.network.state.IEventHandler
import de.pnp.manager.network.state.INonConditionalEventHandler
import de.pnp.manager.network.state.StateMachine
import de.pnp.manager.network.state.States
import java.util.*

class MessageHandler {

    val stateMachine = BaseMessageStateMachine(States.STATES, States.START)
    var activity : Activity? = null

    init {
        // Pre login
        stateMachine.registerTransition(States.PRE_LOGIN, States.LOGGED_IN, MessageIDs.LOGIN_RESPONSE, INonConditionalEventHandler{
            val message = it as LoginResponseMessage
            val client =  Client(message.data.id, message.data.name)
            ApplicationState.client = client
        })

        // Logged In
        stateMachine.registerTransition(States.LOGGED_IN, MessageIDs.SESSION_QUERY_RESPONSE, INonConditionalEventHandler{
            val message = it as SessionQueryResponse
            ApplicationState.sessions.clear()
            ApplicationState.sessions.addAll(message.data)
        })

        // Session Logged In Response
        stateMachine.registerTransition(States.LOGGED_IN, States.IN_SESSION, MessageIDs.JOIN_SESSION_RESPONSE, INonConditionalEventHandler {
            println("response -> " + it)
            val message = it as JoinSessionResponseMessage
            ApplicationState.currentSession = message.data

            TCPClient.sendTo(DatabaseRequestMessage(Date()))

            if (activity is MainActivity) {
                (activity as MainActivity).changeToSessionActivity()
            }
        })

        // Database stuff
        stateMachine.registerTransition(States.IN_SESSION, MessageIDs.DATABASE_RESPONSE, INonConditionalEventHandler{
            val message = it as DatabaseResponseMessage

            ApplicationState.databaseData = message.data

            println(message.data.talents)
        })

        stateMachine.registerTransition(States.IN_SESSION, MessageIDs.ASSIGN_CHARACTERS, INonConditionalEventHandler {
            val message = it as AssignCharactersMessage

            ApplicationState.myCharacters = message.data
            message.data.forEach { a -> println(a.talents) }

            if (activity is SessionActivity)
                (activity as SessionActivity).runOnUiThread { (activity as SessionActivity).updateNavMenu() }
        })
    }
}