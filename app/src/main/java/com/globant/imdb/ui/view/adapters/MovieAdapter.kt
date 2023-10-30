package com.globant.imdb.ui.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.globant.imdb.databinding.ItemMovieBinding
import com.globant.imdb.R
import com.globant.imdb.core.RetrofitHelper
import com.globant.imdb.data.model.Movie
import com.globant.imdb.ui.view.HomeFragment

class MovieAdapter: Adapter<MovieViewHolder>() {

    lateinit var movieList: MutableLiveData<List<Movie>>
    lateinit var moviesListener: HomeFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.listener = moviesListener
        with(movieList.value?.get(position)!!){
            holder.id = id
            holder.labelName.text = title
            holder.labelStars.text= popularity.toString()
            val imageUrl = (RetrofitHelper.IMAGES_BASE_URL + backdropPath)
            moviesListener.renderImage(imageUrl, holder.image)
        }
    }

    override fun getItemCount(): Int {
        return movieList.value?.size ?: 0
    }

    interface ImageRenderListener {
        fun renderImage(url:String, image:ImageView)
    }

}

class MovieViewHolder(root:View):ViewHolder(root){
    lateinit var listener:MovieListener
    var id:Int = 0

    private val binding = ItemMovieBinding.bind(root)
    val image = binding.imgMovie
    val labelName = binding.labelMovieName
    val labelStars = binding.labelStars

    init {
        binding.imgMovie.setOnClickListener {
            listener.showDetails(id)
        }
        binding.btnBookmarkAdd.setOnClickListener {
            listener.addToWatchList(id)
        }
    }

    interface MovieListener {
        fun showDetails(id:Int)
        fun addToWatchList(id:Int)
    }
}