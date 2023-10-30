package com.globant.imdb.ui.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.globant.imdb.data.model.Movie
import com.globant.imdb.domain.GetNowPlayingMoviesUseCase
import com.globant.imdb.domain.GetPopularMoviesUseCase
import com.globant.imdb.domain.GetRandomTopMovieUseCase
import com.globant.imdb.domain.GetOfficialTrailerUseCase
import com.globant.imdb.domain.GetUpcomingMoviesUseCase
import com.globant.imdb.ui.view.adapters.MovieViewHolder
import kotlinx.coroutines.launch

class MovieViewModel: ViewModel() {

    // Live data
    val mainMovie = MutableLiveData<Movie>()
    val videoIframe = MutableLiveData<String?>()
    val nowPlayingMovies = MutableLiveData<List<Movie>>()
    val upcomingMovies = MutableLiveData<List<Movie>>()
    val popularMovies = MutableLiveData<List<Movie>>()

    // Use Cases
    val getRandomTopMovieUseCase = GetRandomTopMovieUseCase()
    val getNowPlayingMoviesUseCase = GetNowPlayingMoviesUseCase()
    val getUpcomingMovies = GetUpcomingMoviesUseCase()
    val getPopularMoviesUseCase = GetPopularMoviesUseCase()
    val getTrailerUseCase = GetOfficialTrailerUseCase()

    @SuppressLint("NotifyDataSetChanged")
    fun onCreate(
        nowPlayingMoviesAdapter:Adapter<MovieViewHolder>,
        upcomingMoviesAdapter:Adapter<MovieViewHolder>,
        popularMoviesAdapter:Adapter<MovieViewHolder>
    ) {
        viewModelScope.launch {
            val result = getRandomTopMovieUseCase()
            result?.let {
                mainMovie.postValue(it)
            }
        }
        viewModelScope.launch {
            val result = getNowPlayingMoviesUseCase()
            if(result.isNotEmpty()){
                nowPlayingMovies.postValue(result)
                nowPlayingMoviesAdapter.notifyDataSetChanged()
            }
        }
        viewModelScope.launch {
            val result = getUpcomingMovies()
            if(result.isNotEmpty()){
                upcomingMovies.postValue(result)
                upcomingMoviesAdapter.notifyDataSetChanged()
            }
        }
        viewModelScope.launch {
            val result = getPopularMoviesUseCase()
            if(result.isNotEmpty()){
                popularMovies.postValue(result)
                popularMoviesAdapter.notifyDataSetChanged()
            }
        }
    }

    fun getTrailerOfMovie(movieId:Int){
        viewModelScope.launch {
            val result = getTrailerUseCase(movieId)
            result?.let {
                videoIframe.postValue(result)
            }
        }
    }
}