package com.globant.imdb.ui.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.globant.imdb.databinding.ItemMovieBinding
import com.globant.imdb.R
import com.globant.imdb.core.Constants
import com.globant.imdb.data.model.movies.Movie
import com.globant.imdb.ui.view.HomeFragment


class MovieAdapter: Adapter<MovieViewHolder>() {

    var movieList: List<Movie> = emptyList()
    lateinit var moviesListener: HomeFragment
    var numberList:Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.listener = moviesListener
        with(movieList[position]){
            holder.numberList = numberList
            holder.id = id
            holder.labelName.text = title
            holder.labelStars.text= popularity.toString()
            val imageUrl = (Constants.IMAGES_BASE_URL + backdropPath)
            moviesListener.renderImage(imageUrl, holder.image)
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    interface ImageRenderListener {
        fun renderImage(url:String, image:ImageView)
    }

}

class MovieViewHolder(root:View):ViewHolder(root){
    lateinit var listener:MovieListener
    var id:Int = 0
    var numberList:Int = 0

    private val binding = ItemMovieBinding.bind(root)
    val image = binding.imgMovie
    val labelName = binding.labelMovieName
    val labelStars = binding.labelStars

    init {
        binding.imgMovie.setOnClickListener {
            listener.showDetails(id)
        }
        binding.btnBookmarkAdd.setOnClickListener {
            listener.addToList(id, numberList)
        }
    }

    interface MovieListener {
        fun showDetails(id:Int)
        fun addToList(id:Int, numberList: Int)
    }
}