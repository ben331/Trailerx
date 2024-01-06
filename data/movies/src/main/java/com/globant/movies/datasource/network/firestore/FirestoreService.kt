package com.globant.movies.datasource.network.firestore

import com.globant.imdb.data.model.user.UserModel
import com.globant.movies.datasource.local.room.entities.CategoryType
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

private const val SHORT_TIMEOUT = 2500L
private const val LONG_TIMEOUT = 5000L

class FirestoreService @javax.inject.Inject constructor(
    private val db: FirebaseFirestore,
) {

    suspend fun createUser(user: UserModel):Boolean {
        return try {
            withTimeout(LONG_TIMEOUT) {
                db.collection("users").document(user.email).set(user).await()
            }
            true
        } catch (e:Exception){
            false
        }
    }

    suspend fun getUser(localEmail:String): UserModel? {
        return try {
            withTimeout(SHORT_TIMEOUT) {
                val document: com.google.firebase.firestore.DocumentSnapshot =
                    db.collection("users").document(localEmail).get().await()
                if (document.exists()) {
                    document.toObject(com.globant.imdb.data.model.user.UserModel::class.java)
                } else {
                    null
                }
            }
        } catch (e:Exception){
            null
        }
    }

    suspend fun getUserMoviesList(listType: CategoryType):List<MovieItem>? {
        return try {
            withTimeout(SHORT_TIMEOUT) {
                val query: com.google.firebase.firestore.QuerySnapshot =
                    db.collection("users").document(email).collection(listType.name).get()
                        .await()
                val result: ArrayList<MovieItem> = ArrayList()
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

    suspend fun addMovieToList(movie: MovieItem, category: CategoryType):Boolean {
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

    suspend fun deleteMovieFromList(movieId:Int,category: CategoryType):Boolean {
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