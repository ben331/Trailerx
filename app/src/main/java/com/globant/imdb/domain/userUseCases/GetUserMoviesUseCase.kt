package com.globant.imdb.domain.userUseCases

import com.globant.imdb.data.repositories.IMDbRepository
import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.data.database.entities.movie.toDatabase
import com.globant.imdb.domain.model.MovieItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class GetUserMoviesUseCase @Inject constructor(
    private val repository: IMDbRepository,
    private val syncUserLocalDataUseCase: SyncUserLocalDataUseCase
) {
    operator fun invoke(
        category: CategoryType,
        handleSuccess:(movies:List<MovieItem>)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ) {
        fun handleRemoteSuccess(movies:List<MovieItem>){
            CoroutineScope(Dispatchers.IO).launch {
                syncUserLocalDataUseCase()
                try {
                    repository.clearMoviesByCategoryDatabase(category)
                    repository.addMoviesToCategoryDatabase(movies.map { it.toDatabase() }, category)
                }catch (e: Exception){
                    e.printStackTrace()
                }finally {
                    handleSuccess(movies)
                }
            }
        }

        fun handleRemoteFailure(title:Int, msg:Int){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val movies = repository.getMoviesByCategoryFromDatabase(category)
                    if(movies.isNotEmpty()){
                        handleSuccess(movies)
                    }else{
                        withContext(Dispatchers.Main){
                            handleFailure(title, msg)
                        }
                    }
                }catch (e: Exception){
                    withContext(Dispatchers.Main){
                        handleFailure(title, msg)
                    }
                }
            }
        }

        repository.getUserMoviesList(category, ::handleRemoteSuccess, ::handleRemoteFailure)
    }
}