package com.globant.imdb.data.remote.firebase

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.globant.imdb.R
import com.globant.imdb.data.model.movies.Movie
import com.globant.imdb.data.model.user.User
import javax.inject.Inject


class FirestoreManager @Inject constructor(
    private val db: FirebaseFirestore,
    private val authManager:FirebaseAuthManager
) {
    var handleFailure: (title:String, msg:String)->Unit =
        { _: String, _: String -> }

    private val email: String by lazy {
        authManager.getEmail()
    }

    fun createUser(user: User, handleSuccess: (user: User?) -> Unit) {
        db.collection("users").document(user.email).set(user)
            .addOnCompleteListener {
                handleSuccess(user)
            }
            .addOnFailureListener {
                it.printStackTrace()
                handleSuccess(null)
            }
    }

    fun getUser(context: Context, localEmail:String, handleSuccess:(user:User?)->Unit) {
        db.collection("users").document(localEmail).get()
            .addOnSuccessListener {
                if(it.exists()){
                    val user = it.toObject(User::class.java)
                    handleSuccess(user)
                }else{
                    handleSuccess(null)
                }
            }.addOnFailureListener {
                it.printStackTrace()
                handleFailure(
                    context.getString(R.string.error),
                    context.getString(R.string.create_user_error)
                )
            }
    }

    fun getUserMoviesList(context:Context, numberList:Int, handleSuccess: (movies:List<Movie>)->Unit) {
        val collection = when(numberList){
            1 -> "watchList"
            2 -> "recentlyViewed"
            3 -> "favoritePeople"
            else -> null
        }
        collection?.let {
            db.collection("users").document(email).collection(collection).get()
                .addOnSuccessListener {
                    val result:ArrayList<Movie> = ArrayList()
                    if(!it.isEmpty){
                        for( document in it.documents ){
                            val movie = document.toObject(Movie::class.java)!!
                            result.add(movie)
                        }
                        handleSuccess(result)
                    }else{
                        handleSuccess(emptyList())
                    }
                }.addOnFailureListener {
                    it.printStackTrace()
                    handleFailure(
                        context.getString(R.string.error),
                        context.getString(R.string.fetch_movies_error)
                    )
                }
        }
    }

    fun addMovieToList(context:Context, movie:Movie, listNumber: Int, handleSuccess:(movie:Movie)->Unit){
        val collection = when(listNumber){
            1 -> "watchList"
            2 -> "recentlyViewed"
            3 -> "favoritePeople"
            else -> null
        }

        collection?.let {
            db.collection("users")
                .document(email).collection(it).document(movie.id.toString()).set(movie)
                .addOnCompleteListener {
                    handleSuccess(movie)
                }.addOnFailureListener { e ->
                    e.printStackTrace()
                    handleFailure(
                        context.getString(R.string.error),
                        context.getString(R.string.error)
                    )
                }
        }
    }

    fun deleteMovieFromList(
        context:Context,
        movieId:Int,
        listNumber:Int,
        handleSuccess:()->Unit
    ){
        val collection = when(listNumber){
            1 -> "watchList"
            2 -> "recentlyViewed"
            3 -> "favoritePeople"
            else -> null
        }

        collection?.let {
            db.collection("users")
                .document(email).collection(it).document(movieId.toString()).delete()
                .addOnSuccessListener {
                    handleSuccess()
                }.addOnFailureListener {
                    handleFailure(
                        context.getString(R.string.error),
                        context.getString(R.string.delete_movie_error)
                    )
                }
        }

    }

}