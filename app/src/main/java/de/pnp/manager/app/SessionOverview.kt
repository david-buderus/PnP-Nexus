package de.pnp.manager.app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import de.pnp.manager.app.network.TCPClient
import de.pnp.manager.network.message.session.QuerySessions
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SessionOverview : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sessions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TCPClient.sendTo(QuerySessions(Date()))

        /*view.findViewById<Button>(R.id.button_connect_to_server).setOnClickListener {
            findNavController().navigate(R.id.action_SessionOverviewFragment_to_CharacterFragment)
        } */

    }
}