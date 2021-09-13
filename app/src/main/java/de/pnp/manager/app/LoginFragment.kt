package de.pnp.manager.app

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import de.pnp.manager.app.network.TCPClient
import de.pnp.manager.network.message.login.LoginRequestMessage
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_connect_to_server).setOnClickListener {
            val port : Int =
                if (view.findViewById<EditText>(R.id.editTextPort).text.toString().isEmpty())
                    42020
                else
                    view.findViewById<EditText>(R.id.editTextPort).text.toString().toInt()


            val ip =
                if (view.findViewById<EditText>(R.id.editTextIP).text.toString().isEmpty())
                    "192.168.0.59"
                else
                    view.findViewById<EditText>(R.id.editTextIP).text.toString()


            TCPClient.start(ip, port)

            TCPClient.sendTo(LoginRequestMessage("test", Date()))

            Thread.sleep(50)

            findNavController().navigate(R.id.action_LoginFragment_to_SessionOverviewFragment)
        }

    }
}