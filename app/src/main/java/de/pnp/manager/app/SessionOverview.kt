package de.pnp.manager.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import de.pnp.manager.app.databinding.FragmentSessionsBinding
import de.pnp.manager.app.network.TCPClient
import de.pnp.manager.app.recycler.SessionViewModel
import de.pnp.manager.app.state.ApplicationState
import de.pnp.manager.network.message.session.QuerySessions
import de.pnp.manager.network.session.ISession
import de.pnp.manager.network.session.SessionInfo
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SessionOverview : Fragment() {

    val viewModel: SessionViewModel by viewModels()

    companion object {
        val sessions: Collection<ISession> = ArrayList()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return FragmentSessionsBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.sessions_swipe_refresh)

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadData()
            swipeRefreshLayout.isRefreshing = false
        }

    }
}