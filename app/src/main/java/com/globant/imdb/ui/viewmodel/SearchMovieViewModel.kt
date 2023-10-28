package com.globant.imdb.ui.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.globant.imdb.data.model.Movie
import com.globant.imdb.domain.SearchMovieUseCase
import com.globant.imdb.ui.view.adapters.MovieResultAdapter
import kotlinx.coroutines.launch

class SearchMovieViewModel: ViewModel() {

    // Live data
    val resultMovies = MutableLiveData<List<Movie>>()

    // Use Cases
    val searchMovieUseCase = SearchMovieUseCase()

    lateinit var adapter: MovieResultAdapter

    @SuppressLint("NotifyDataSetChanged")
    fun search(
        query:String
    ) {
        viewModelScope.launch {
            val result = searchMovieUseCase(query)
            if(result.isNotEmpty()){
                resultMovies.postValue(result)
                adapter.notifyDataSetChanged()
            }
        }
    }
}