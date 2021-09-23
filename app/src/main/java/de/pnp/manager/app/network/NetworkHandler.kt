package de.pnp.manager.app.network

import android.os.StrictMode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import de.pnp.manager.app.network.serialization.SerializationModule
import de.pnp.manager.app.state.ApplicationState
import de.pnp.manager.network.message.BaseMessage
import de.pnp.manager.network.message.session.JoinSessionRequestMessage
import de.pnp.manager.network.session.ISession
import de.pnp.manager.network.session.SessionInfo
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.util.*

object TCPClient {

    private lateinit var socket: Socket
    private lateinit var readerThread: Thread
    var active = false
    private val handler = null
    private lateinit var output: PrintWriter
    private lateinit var input: BufferedReader
    private val mapper: ObjectMapper = ObjectMapper()

    private lateinit var currentSession: ISession

    init {
        this.mapper.registerKotlinModule()
        this.mapper.registerModule(SerializationModule())
    }

    fun start(ip: String, port: Int) {
        val thread = Thread {
            try {
                println(ip + " " + port)
                socket =
                    Socket(ip, port)
                this.input = BufferedReader(InputStreamReader(socket.getInputStream()));
                this.output = PrintWriter(socket.getOutputStream(), true)
                active = true

                readerThread = Thread {

                    while (active) {
                        try {
                            val inputLine: String = input.readLine()
                            println("new message: " + inputLine)
                            val message = mapper.readValue(inputLine, BaseMessage::class.java)
                            ApplicationState.messageHandler.stateMachine.fire(message)

                            println(message)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
                readerThread.isDaemon = true
                readerThread.start()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        thread.start()
        thread.join()
    }

    fun sendTo(message: BaseMessage?) {
        Thread {
            if (message != null) {
                output.println(mapper.writeValueAsString(message))
            }
        }.start()
    }

    fun connectToSession(sessionInfo: SessionInfo) {

        val message = JoinSessionRequestMessage(sessionInfo.sessionID, null, Date())

        sendTo(message)
    }
}