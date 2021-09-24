package de.pnp.manager.app.session_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.allViews
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import de.pnp.manager.app.R

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SessionLobbyFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_session_lobby, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}