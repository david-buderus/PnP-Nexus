package de.pnp.manager.app

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import de.pnp.manager.app.network.TCPClient
import de.pnp.manager.app.state.ApplicationState
import info.androidhive.fontawesome.FontDrawable


class SessionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationState.messageHandler.activity = this
        setContentView(R.layout.activity_session)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_session_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navView = findViewById<NavigationView>(R.id.nav_view_session)
        navView.setupWithNavController(navController)

        val drawable = FontDrawable(this, R.string.fa_address_book_solid, true, false)
        drawable.setTextColor(ContextCompat.getColor(this, R.color.black))
        drawable.textSize = 22f

        navView.menu.forEach { item ->
            run {
                item.icon =drawable
            }
        }

        //setSupportActionBar(findViewById(R.id.toolbar))

        //findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
        //    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //            .setAction("Action", null).show()
        //}
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}