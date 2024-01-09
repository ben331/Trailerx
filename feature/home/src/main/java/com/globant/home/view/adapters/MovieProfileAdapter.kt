package com.globant.home.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.globant.common.CategoryType
import com.globant.common.Constants
import com.globant.home.R
import com.globant.home.databinding.ItemMovieProfileBinding
import com.globant.home.view.fragments.ProfileFragment
import com.globant.movies.model.MovieItem

class MovieProfileAdapter: Adapter<MovieProfileViewHolder>() {

    var movieList: List<MovieItem> = emptyList()
    lateinit var moviesListener: ProfileFragment
    lateinit var listType:CategoryType

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
    lateinit var listener: MovieListener
    var id:Int = 0
    lateinit var listType:CategoryType

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
        fun deleteFromList(id:Int, listType:CategoryType)
    }
}