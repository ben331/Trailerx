package com.globant.imdb.ui.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.imdb.domain.model.MovieItem
import com.globant.imdb.domain.movies_use_cases.SearchMovieUseCase
import com.globant.imdb.ui.view.adapters.MovieResultAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMovieUseCase:SearchMovieUseCase
): ViewModel() {

    // Live data
    val resultMovies = MutableLiveData<List<MovieItem>>()

    // Use Cases

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