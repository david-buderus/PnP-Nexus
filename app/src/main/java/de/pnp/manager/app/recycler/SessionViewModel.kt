package de.pnp.manager.app.recycler

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.pnp.manager.app.R
import de.pnp.manager.app.network.TCPClient
import de.pnp.manager.network.message.session.QuerySessions
import de.pnp.manager.network.session.SessionInfo
import java.util.*
import androidx.databinding.library.baseAdapters.BR;
import de.pnp.manager.app.state.ApplicationState

class SessionViewModel : ViewModel() {
    
    val data = MutableLiveData<List<RecyclerItem>>()
    
    init {
        loadData()
    }
    
    fun loadData() {
        TCPClient.sendTo(QuerySessions(Date()))

        // TODO refactor the sendTo to a send and wait for response (async)
        Thread.sleep(50)

        println(ApplicationState.sessions)

        data.value = (ApplicationState.sessions.map { sessionInfo -> sessionInfo.toRecyclerItem() })


        println(data.value)
    }
}

private fun SessionInfo.toRecyclerItem() = RecyclerItem(
    data = this,
    variableId = BR.sessionInfo,
    layoutId = R.layout.session_card
)