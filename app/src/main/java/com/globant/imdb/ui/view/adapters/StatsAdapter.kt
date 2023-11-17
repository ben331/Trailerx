package com.globant.imdb.ui.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.globant.imdb.R
import com.globant.imdb.data.model.user.StatsModel
import com.globant.imdb.databinding.ItemShortcutBinding


class StatsAdapter: Adapter<ShortcutViewHolder>() {

    var statsList: List<StatsModel> = init()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortcutViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_shortcut, parent, false)
        return ShortcutViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShortcutViewHolder, position: Int) {
        with(statsList[position]){
            holder.id = position
            holder.title.text = title
            holder.textBox.text = content
            holder.score.text = score.toString()
        }
    }
    override fun getItemCount(): Int {
        return statsList.size
    }

    private fun init(): List<StatsModel>{
        return listOf(
            StatsModel("Calificar y obtener recomendaciones", "Calificaciones", 0),
            StatsModel("Agregar a listas", "Listas", 0),
            StatsModel("Agregar a favoritos", "Favoritos", 0),
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