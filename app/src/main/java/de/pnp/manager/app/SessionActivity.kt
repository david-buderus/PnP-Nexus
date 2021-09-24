package de.pnp.manager.app

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import de.pnp.manager.app.network.TCPClient
import de.pnp.manager.app.state.ApplicationState
import info.androidhive.fontawesome.FontDrawable


class SessionActivity : AppCompatActivity() {

    val characterMenuGroupId: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationState.messageHandler.activity = this
        setContentView(R.layout.activity_session)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_session_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navView = findViewById<NavigationView>(R.id.nav_view_session)


        navView.setNavigationItemSelectedListener {
            this.onOptionsItemSelected(it)
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

        if (ApplicationState.myCharacters?.any { it.characterID.hashCode() == item.itemId } == true) {
            val character = ApplicationState.myCharacters!!.first { it.characterID.hashCode() == item.itemId }

            val msg = Toast.makeText(this, character.name, Toast.LENGTH_LONG)
            msg.show()

            // TODO data binding here
            val fragment = CharacterFragment(character)

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_session_fragment, fragment)
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.addToBackStack(null)
            transaction.commit()

            findViewById<DrawerLayout>(R.id.session_drawer_layout).closeDrawers()
            return true
        }

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun updateNavMenu() {
        val navView = findViewById<NavigationView>(R.id.nav_view_session)
        navView.menu.clear()
        ApplicationState.myCharacters!!.forEach { navView.menu.add(characterMenuGroupId, it.characterID.hashCode(), Menu.NONE, it.name) }

        val drawable = FontDrawable(this, R.string.fa_address_book_solid, true, false)
        drawable.setTextColor(ContextCompat.getColor(this, R.color.black))
        drawable.textSize = 22f

        navView.menu.forEach { item ->
            run {
                item.icon =drawable
            }
        }

    }
}