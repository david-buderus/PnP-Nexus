package de.pnp.manager.app

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.graphics.drawable.Drawable.createFromStream
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import de.pnp.manager.app.databinding.FragmentInfoBinding
import de.pnp.manager.model.character.IPnPCharacter
import java.io.InputStream
import java.net.URL

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

        val imageView = view.findViewById<ImageView>(R.id.imageView2)
        Thread {
            val bitmap = loadImageFromWebOperations("https://imgl.krone.at/scaled/2347804/v0780ce/schema_org_4_3.jpg")
            this.activity!!.runOnUiThread{
                imageView.setImageBitmap(bitmap)
            }
        }.start()
    }

    private fun loadImageFromWebOperations(url: String): Bitmap? {
        return try {
            val inputStream: InputStream = URL(url).getContent() as InputStream
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            println("Exc=$e")
            null
        }
    }

}