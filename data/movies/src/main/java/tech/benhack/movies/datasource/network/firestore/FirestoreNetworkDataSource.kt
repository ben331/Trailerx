package tech.benhack.movies.datasource.network.firestore

import com.google.firebase.Firebase
import tech.benhack.common.CategoryType
import tech.benhack.movies.datasource.UserMoviesNetworkDataSource
import tech.benhack.movies.model.MovieItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.functions.functions
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

private const val SHORT_TIMEOUT = 2500L
private const val LONG_TIMEOUT = 6000L

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

    override suspend fun deleteUserData(email: String, authToken:String): Boolean {
        return try {
            val pathHistory = db.collection("users").document(email).collection(CategoryType.HISTORY_MOVIES.name).path
            val pathWatchList = db.collection("users").document(email).collection(CategoryType.WATCH_LIST_MOVIES.name).path

            val deleteFn = Firebase.functions.getHttpsCallable("recursiveDelete")

            withTimeout(LONG_TIMEOUT) {
                deleteFn.call(hashMapOf("path" to pathHistory, "token" to authToken)).await()
            }
            withTimeout(LONG_TIMEOUT) {
                deleteFn.call(hashMapOf("path" to pathWatchList, "token" to authToken)).await()
            }
            withTimeout(SHORT_TIMEOUT){
                db.collection("users").document(email).delete().await()
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}