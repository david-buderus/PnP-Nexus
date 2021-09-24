package de.pnp.manager.app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import de.pnp.manager.app.databinding.FragmentInfoBinding
import de.pnp.manager.model.character.IPnPCharacter

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class InfoFragment(var character: IPnPCharacter) : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return FragmentInfoBinding.inflate(inflater, container, false).also {
            it.character = this.character
            it.lifecycleOwner = viewLifecycleOwner
        }.root
        //return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}