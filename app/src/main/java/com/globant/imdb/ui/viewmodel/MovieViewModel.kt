package com.globant.imdb.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.globant.imdb.data.model.movies.Movie
import com.globant.imdb.domain.movies.GetNowPlayingMoviesUseCase
import com.globant.imdb.domain.movies.GetPopularMoviesUseCase
import com.globant.imdb.domain.movies.GetRandomTopMovieUseCase
import com.globant.imdb.domain.movies.GetOfficialTrailerUseCase
import com.globant.imdb.domain.movies.GetUpcomingMoviesUseCase
import com.globant.imdb.domain.user.AddMovieToWatchListUseCase
import com.globant.imdb.domain.user.CreateUserUseCase
import com.globant.imdb.domain.user.SetHandleFailureUseCase
import com.globant.imdb.ui.view.adapters.MovieViewHolder
import kotlinx.coroutines.launch
class MovieViewModel: ViewModel() {

    // Live data
    val mainMovie = MutableLiveData<Movie>()
    val videoIframe = MutableLiveData<String?>()
    val nowPlayingMovies = MutableLiveData<List<Movie>>()
    val upcomingMovies = MutableLiveData<List<Movie>>()
    val popularMovies = MutableLiveData<List<Movie>>()
    val isLoading = MutableLiveData(false)

    // Use Cases
    private val getRandomTopMovieUseCase = GetRandomTopMovieUseCase()
    private val getNowPlayingMoviesUseCase = GetNowPlayingMoviesUseCase()
    private val getUpcomingMovies = GetUpcomingMoviesUseCase()
    private val getPopularMoviesUseCase = GetPopularMoviesUseCase()
    private val getTrailerUseCase = GetOfficialTrailerUseCase()
    private val addMovieToWatchListUseCase = AddMovieToWatchListUseCase()
    private val setHandleFailureUseCase = SetHandleFailureUseCase()

    @SuppressLint("NotifyDataSetChanged")
    fun onCreate() {
        isLoading.postValue(true)
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
            }
        }
        viewModelScope.launch {
            val result = getUpcomingMovies()
            if(result.isNotEmpty()){
                upcomingMovies.postValue(result)
            }
        }
        viewModelScope.launch {
            val result = getPopularMoviesUseCase()
            if(result.isNotEmpty()){
                popularMovies.postValue(result)
            }
        }
    }

    fun setHandleFailure(handleAlert: (title:String, msg:String) -> Unit){
        setHandleFailureUseCase(handleAlert)
    }

    fun getTrailerOfMovie(movieId:Int){
        isLoading.postValue(true)
        viewModelScope.launch {
            val result = getTrailerUseCase(movieId, false)
            result?.let {
                videoIframe.postValue(result)
            }
        }
    }

    fun addMovieToWatchList(movieId:Int, context: Context, handleSuccess:(movie:Movie)->Unit){
        isLoading.postValue(true)
        val homeMovies =
            nowPlayingMovies.value?.plus(upcomingMovies.value)?.plus(popularMovies.value) as List<Movie>
        val movie = homeMovies.find {
            it.id == movieId
        }
        if(movie!=null){
            addMovieToWatchListUseCase(context, movie, handleSuccess)
        }
    }
}