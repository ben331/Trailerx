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

class MovieAdapter: Adapter<MovieViewHolder>() {

    lateinit var movieList: MutableLiveData<List<Movie>>
    lateinit var imageRenderListener: ImageRenderListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.labelName.text = movieList.value?.get(position)?.title ?: "error"
        holder.labelStars.text = movieList.value?.get(position)?.popularity.toString()
        val imageUrl =
            (RetrofitHelper.imageUrl + movieList.value?.get(position)?.backdropPath)
        imageRenderListener.renderImage(imageUrl, holder.image)
    }

    override fun getItemCount(): Int {
        return movieList.value?.size ?: 0
    }

    interface ImageRenderListener {
        fun renderImage(url:String, image:ImageView)
    }

}

class MovieViewHolder(root:View):ViewHolder(root){
    private val binding = ItemMovieBinding.bind(root)
    val image = binding.imgMovie
    val labelName = binding.labelMovieName
    val labelStars = binding.labelStars
}