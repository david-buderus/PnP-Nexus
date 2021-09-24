package de.pnp.manager.app

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import de.pnp.manager.app.databinding.FragmentCharacterViewBinding
import de.pnp.manager.app.databinding.FragmentSessionsBinding
import de.pnp.manager.model.character.IPnPCharacter

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CharacterFragment(var character: IPnPCharacter) : Fragment() {

    private lateinit var demoCollectionPagerAdapter: CharacterFragmentPagerAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        return FragmentCharacterViewBinding.inflate(inflater, container, false).also {
            it.character = this.character
            it.lifecycleOwner = viewLifecycleOwner
        }.root

        //return inflater.inflate(R.layout.fragment_character_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        demoCollectionPagerAdapter = CharacterFragmentPagerAdapter(this, character)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = demoCollectionPagerAdapter

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)

        TabLayoutMediator(tabLayout, viewPager) {
            tab, position ->
            run {
                when (position) {
                    0 -> tab.text = "Info"
                    1 -> tab.text = "Equipment"
                    2 -> tab.text = "Talents"
                    else -> tab.text = "Info"
                }
            }
        }.attach()


        super.onViewCreated(view, savedInstanceState)
    }
}

// Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
class CharacterFragmentPagerAdapter(fm: Fragment, var character: IPnPCharacter) : FragmentStateAdapter(fm) {


    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment

        when (position) {
            0 -> fragment = InfoFragment(character)
            1 -> fragment = EquipmentFragment(character)
            2 -> fragment = TalentsFragment(character)
            else -> fragment = InfoFragment(character)
        }

        return fragment
    }
}