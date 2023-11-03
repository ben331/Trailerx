package com.globant.imdb.ui.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.globant.imdb.R
import com.globant.imdb.data.model.user.Shortcut
import com.globant.imdb.databinding.ItemShortcutBinding


class ShortcutAdapter: Adapter<ShortcutViewHolder>() {

    var shortcutList: List<Shortcut> = init()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortcutViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_shortcut, parent, false)
        return ShortcutViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShortcutViewHolder, position: Int) {
        with(shortcutList[position]){
            holder.id = position
            holder.title.text = title
            holder.textBox.text = content
            holder.score.text = score.toString()
        }
    }
    override fun getItemCount(): Int {
        return shortcutList.size
    }

    private fun init(): List<Shortcut>{
        return listOf(
            Shortcut("Calificar y obtener recomendaciones", "Calificaciones", 0),
            Shortcut("Agregar a listas", "Listas", 0),
            Shortcut("Agregar a favoritos", "Favoritos", 0),
        )
    }
}

class ShortcutViewHolder(root:View):ViewHolder(root){
    var id:Int = 0

    private val binding = ItemShortcutBinding.bind(root)
    val title = binding.titleShortcut
    val textBox = binding.textBoxShortcut
    val score = binding.score
}