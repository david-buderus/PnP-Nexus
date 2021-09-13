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

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CharacterFragment : Fragment() {

    private lateinit var demoCollectionPagerAdapter: CharacterFragmentPagerAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_character_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        demoCollectionPagerAdapter = CharacterFragmentPagerAdapter(this)
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
class CharacterFragmentPagerAdapter(fm: Fragment) : FragmentStateAdapter(fm) {


    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment

        when (position) {
            0 -> fragment = InfoFragment()
            1 -> fragment = EquipmentFragment()
            2 -> fragment = TalentsFragment()
            else -> fragment = InfoFragment()
        }

        return fragment
    }
}