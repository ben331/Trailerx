package com.globant.imdb.ui.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.globant.imdb.R
import com.globant.imdb.core.Constants
import com.globant.imdb.data.database.entities.movie.MovieListType
import com.globant.imdb.databinding.ItemMovieProfileBinding
import com.globant.imdb.domain.model.MovieItem
import com.globant.imdb.ui.view.fragments.ProfileFragment

class MovieProfileAdapter: Adapter<MovieProfileViewHolder>() {

    var movieList: List<MovieItem> = emptyList()
    lateinit var moviesListener: ProfileFragment
    lateinit var listType:MovieListType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieProfileViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_movie_profile, parent, false)
        return MovieProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieProfileViewHolder, position: Int) {
        holder.listType = listType
        holder.listener = moviesListener
        with(movieList[position]){
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

class MovieProfileViewHolder(root:View):ViewHolder(root){
    lateinit var listener:MovieListener
    var id:Int = 0
    lateinit var listType:MovieListType

    private val binding = ItemMovieProfileBinding.bind(root)
    val image = binding.imgMovie
    val labelName = binding.labelMovieName
    val labelStars = binding.labelStars

    init {
        binding.imgMovie.setOnClickListener {
            listener.showDetails(id)
        }
        binding.btnBookmarkDelete.setOnClickListener {
            listener.deleteFromList(id, listType)
        }
    }

    interface MovieListener {
        fun showDetails(id:Int)
        fun deleteFromList(id:Int, listType:MovieListType)
    }
}