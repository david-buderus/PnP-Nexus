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
import java.io.*
import java.net.Socket
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.Semaphore

object TCPClient {

    private lateinit var socket: Socket
    private lateinit var readerThread: Thread
    var active = false
    var sendingSemaphore : Semaphore = Semaphore(-1)
    private val handler = null
    private var output: PrintWriter? = null
    private var input: BufferedReader? = null
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
                this.input = BufferedReader(InputStreamReader(socket.getInputStream(), Charsets.UTF_8));
                this.output = PrintWriter(OutputStreamWriter(socket.getOutputStream(), Charsets.UTF_8), true)
                active = true

                readerThread = Thread {
                    while (active) {
                        try {
                            val inputLine: String = input!!.readLine()
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
                sendingSemaphore.release()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        thread.start()
        thread.join()
    }

    fun sendTo(message: BaseMessage?) {
        Thread {
            if (sendingSemaphore.availablePermits() == -1) {
                sendingSemaphore.acquire()
            }
            if (message != null) {
                output?.println(mapper.writeValueAsString(message))
            }
        }.start()
    }

    fun connectToSession(sessionInfo: SessionInfo) {

        val message = JoinSessionRequestMessage(sessionInfo.sessionID, null, Date())

        sendTo(message)
    }
}