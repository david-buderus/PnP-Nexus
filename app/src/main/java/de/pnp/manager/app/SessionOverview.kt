package de.pnp.manager.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import de.pnp.manager.app.network.TCPClient
import de.pnp.manager.app.state.ApplicationState
import de.pnp.manager.network.message.session.QuerySessions
import de.pnp.manager.network.session.ISession
import de.pnp.manager.network.session.SessionInfo
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SessionOverview : Fragment() {

    companion object {
        val sessions: Collection<ISession> = ArrayList()
    }

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

        Thread.sleep(50)

        // TODO observer pattern
        view.findViewById<RecyclerView>(R.id.rec_view_sessions).adapter = CardAdapter(ApplicationState.sessions)

        /*view.findViewById<Button>(R.id.button_connect_to_server).setOnClickListener {
            findNavController().navigate(R.id.action_SessionOverviewFragment_to_CharacterFragment)
        } */

    }
}

class CardAdapter(private val sessionInfos: List<SessionInfo>) : RecyclerView.Adapter<SessionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.session_card, parent, false)
        return SessionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        val sessionInfo = sessionInfos[position]

        if (sessionInfo.maxClients == -1) {
            holder.sessionCapacity.text = String.format("%d/∞",sessionInfo.actualClients)
        } else {
            holder.sessionCapacity.text = String.format("%d/%d",sessionInfo.actualClients, sessionInfo.maxClients)
        }

        holder.sessionName.text = sessionInfo.sessionName
    }

    override fun getItemCount(): Int {
        return this.sessionInfos.size
    }

}

class SessionViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    val cardView : CardView = itemView.findViewById(R.id.session_card_view)
    val sessionHostName : TextView = itemView.findViewById(R.id.session_host_name)
    val sessionCapacity : TextView = itemView.findViewById(R.id.session_capacity)
    val sessionName : TextView = itemView.findViewById(R.id.session_name)
    val sessionStateName : TextView = itemView.findViewById(R.id.session_state_name)
}