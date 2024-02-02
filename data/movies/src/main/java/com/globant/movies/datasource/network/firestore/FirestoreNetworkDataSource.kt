package com.globant.movies.datasource.network.firestore

import android.util.Log
import com.globant.common.CategoryType
import com.globant.movies.datasource.UserMoviesNetworkDataSource
import com.globant.movies.model.MovieItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

private const val SHORT_TIMEOUT = 2500L

class FirestoreNetworkDataSource @Inject constructor(
    private val db: FirebaseFirestore,
): UserMoviesNetworkDataSource {
    override suspend fun getUserMoviesList(listType: CategoryType, email:String):List<MovieItem>? {
        return try {
            withTimeout(SHORT_TIMEOUT) {
                val query: QuerySnapshot =
                    db.collection("users").document(email).collection(listType.name).get()
                        .await()
                val result = mutableListOf<MovieItem>()
                for (document in query.documents) {
                    val movie =
                        document.toObject(MovieItem::class.java)!!
                    result.add(movie)
                }
                result
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun addMovieToList(movie: MovieItem, category: CategoryType, email:String):Boolean {
        return try {
            withTimeout(SHORT_TIMEOUT) {
                db.collection("users").document(email).collection(category.name)
                    .document(movie.id.toString()).set(movie).await()
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteMovieFromList(movieId:Int, category: CategoryType, email:String):Boolean {
        return try {
            withTimeout(SHORT_TIMEOUT) {
                db.collection("users").document(email).collection(category.name)
                    .document(movieId.toString()).delete().await()
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}