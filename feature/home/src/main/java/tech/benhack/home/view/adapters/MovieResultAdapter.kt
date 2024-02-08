package tech.benhack.home.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import tech.benhack.common.Constants
import tech.benhack.home.R
import tech.benhack.home.databinding.ItemMovieResultBinding
import tech.benhack.home.view.fragments.SearchFragment
import tech.benhack.movies.model.MovieItem

class MovieResultAdapter: Adapter<MovieResultViewHolder>() {

    lateinit var movieList: MutableLiveData<List<MovieItem>>
    lateinit var moviesListener: SearchFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieResultViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_movie_result, parent, false)
        return MovieResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieResultViewHolder, position: Int) {
        holder.listener = moviesListener
        with(movieList.value?.get(position)!!){
            holder.id = id
            holder.title.text = title
            holder.labelYear.text =
                if(releaseDate!=null && releaseDate!!.length >= 4) releaseDate!!.subSequence(0,4) else "----"
            val imageUrl = (Constants.IMAGES_BASE_URL + backdropPath)
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

class MovieResultViewHolder(root:View):ViewHolder(root){
    lateinit var listener: MovieResultListener
    var id:Int = 0

    private val binding = ItemMovieResultBinding.bind(root)
    val image = binding.imgResult
    val title = binding.labelTitleResult
    val labelYear = binding.labelYear
    val labelActors = binding.labelActors

    init {
        binding.root.setOnClickListener {
            listener.showDetails(id)
        }
    }

    interface MovieResultListener {
        fun showDetails(id:Int)
    }
}