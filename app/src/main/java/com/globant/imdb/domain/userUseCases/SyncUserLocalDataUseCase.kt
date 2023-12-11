package com.globant.imdb.domain.userUseCases

import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.data.repositories.IMDbRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SyncUserLocalDataUseCase @Inject constructor(private val repository: IMDbRepository) {
    suspend operator fun invoke() {
        val watchListToAdd = repository.getMoviesByCategoryFromDatabase(CategoryType.PENDING_TO_ADD_TO_WATCH_LIST_MOVIES)
        val watchListToRemove = repository.getMoviesByCategoryFromDatabase(CategoryType.PENDING_TO_DELETE_FROM_WATCH_LIST_MOVIES)
        val historyToAdd = repository.getMoviesByCategoryFromDatabase(CategoryType.PENDING_TO_ADD_TO_HISTORY_MOVIES)
        val historyToDelete = repository.getMoviesByCategoryFromDatabase(CategoryType.PENDING_TO_DELETE_FROM_HISTORY_MOVIES)

        for(movie in watchListToAdd){
           repository.addMovieToCategory(
               movie,
               CategoryType.WATCH_LIST_MOVIES,
               {
                   CoroutineScope(Dispatchers.IO).launch {
                       repository.deleteMovieFromCategoryDatabase(movie.id, CategoryType.PENDING_TO_ADD_TO_WATCH_LIST_MOVIES)}
               }
               ,
               {_,_->}
           )
        }
        for(movie in watchListToRemove){
            repository.deleteMovieFromCategory(
                movie.id,
                CategoryType.WATCH_LIST_MOVIES,
                {
                    CoroutineScope(Dispatchers.IO).launch {
                        repository.deleteMovieFromCategoryDatabase(movie.id, CategoryType.PENDING_TO_DELETE_FROM_WATCH_LIST_MOVIES)}
                }
                ,
                {_,_->}
            )
        }
        for(movie in historyToAdd){
            repository.addMovieToCategory(
                movie,
                CategoryType.HISTORY_MOVIES,
                {
                    CoroutineScope(Dispatchers.IO).launch {
                        repository.deleteMovieFromCategoryDatabase(movie.id, CategoryType.PENDING_TO_ADD_TO_HISTORY_MOVIES)}
                }
                ,
                {_,_->}
            )
        }
        for(movie in historyToDelete){
            repository.deleteMovieFromCategory(
                movie.id,
                CategoryType.HISTORY_MOVIES,
                {
                    CoroutineScope(Dispatchers.IO).launch {
                        repository.deleteMovieFromCategoryDatabase(movie.id, CategoryType.PENDING_TO_DELETE_FROM_HISTORY_MOVIES)}
                }
                ,
                {_,_->}
            )
        }
    }
}