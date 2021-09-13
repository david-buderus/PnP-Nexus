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
            val port : Int = view.findViewById<EditText>(R.id.editTextPort).text.toString().toInt()
            val ip = view.findViewById<EditText>(R.id.editTextIP).text.toString()

            TCPClient.start(ip, port)

            findNavController().navigate(R.id.action_LoginFragment_to_SessionOverviewFragment)
        }

    }
}