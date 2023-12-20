package com.globant.imdb.data.network.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.data.model.user.UserModel
import com.globant.imdb.domain.model.MovieItem
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

private const val SHORT_TIMEOUT = 2500L
private const val LONG_TIMEOUT = 5000L

class FirestoreManager @Inject constructor(
    private val db:FirebaseFirestore,
    private val auth:FirebaseAuthManager
) {

    private val email: String by lazy {
        auth.getEmail()
    }

    suspend fun createUser(user: UserModel):Boolean {
        return try {
            withTimeout(LONG_TIMEOUT){
                db.collection("users").document(user.email).set(user).await()
            }
            true
        } catch (e:Exception){
            false
        }
    }

    suspend fun getUser(localEmail:String):UserModel? {
        return try {
            withTimeout(SHORT_TIMEOUT){
                val document:DocumentSnapshot =
                    db.collection("users").document(localEmail).get().await()
                if(document.exists()){
                    document.toObject(UserModel::class.java)
                }else{
                    null
                }
            }
        } catch (e:Exception){
            null
        }
    }

    suspend fun getUserMoviesList(listType:CategoryType):List<MovieItem>? {
        return try {
            withTimeout(SHORT_TIMEOUT) {
                val query: QuerySnapshot =
                    db.collection("users").document(email).collection(listType.name).get()
                        .await()
                val result: ArrayList<MovieItem> = ArrayList()
                for (document in query.documents) {
                    val movie = document.toObject(MovieItem::class.java)!!
                    result.add(movie)
                }
                result
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun addMovieToList(movie:MovieItem, category:CategoryType):Boolean {
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

    suspend fun deleteMovieFromList(movieId:Int,category:CategoryType):Boolean {
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