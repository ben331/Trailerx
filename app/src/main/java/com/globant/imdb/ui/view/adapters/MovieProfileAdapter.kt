package com.globant.imdb.ui.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.globant.imdb.R
import com.globant.imdb.core.RetrofitHelper
import com.globant.imdb.data.model.movies.Movie
import com.globant.imdb.databinding.ItemMovieProfileBinding
import com.globant.imdb.ui.view.ProfileFragment

class MovieProfileAdapter: Adapter<MovieProfileViewHolder>() {

    lateinit var movieList: MutableLiveData<List<Movie>>
    lateinit var moviesListener: ProfileFragment
    var listNumber:Int=-1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieProfileViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_movie_profile, parent, false)
        return MovieProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieProfileViewHolder, position: Int) {
        holder.listNumber = listNumber
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

class MovieProfileViewHolder(root:View):ViewHolder(root){
    lateinit var listener:MovieListener
    var id:Int = 0
    var listNumber:Int=-1

    private val binding = ItemMovieProfileBinding.bind(root)
    val image = binding.imgMovie
    val labelName = binding.labelMovieName
    val labelStars = binding.labelStars

    init {
        binding.imgMovie.setOnClickListener {
            listener.showDetails(id)
        }
        binding.btnBookmarkDelete.setOnClickListener {
            listener.deleteFromList(id, listNumber)
        }
    }

    interface MovieListener {
        fun showDetails(id:Int)
        fun deleteFromList(id:Int, listNumber:Int)
    }
}