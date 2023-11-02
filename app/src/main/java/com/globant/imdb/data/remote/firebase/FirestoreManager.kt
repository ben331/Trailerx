package com.globant.imdb.data.remote.firebase

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.globant.imdb.R
import com.globant.imdb.data.model.movies.Movie
import com.globant.imdb.data.model.user.User


class FirestoreManager {
    var handleFailure: (title:String, msg:String)->Unit = { _: String, _: String -> }

    private val db: FirebaseFirestore by lazy {
        Firebase.firestore
    }

    private val email: String by lazy {
        FirebaseAuthManager().getEmail()
    }

    fun createUser(user: User, handleSuccess: (user: User?) -> Unit) {
        db.collection("users").add(user)
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

    fun getWatchList(context:Context, handleSuccess: (movies:ArrayList<Movie>)->Unit) {
        db.collection("users").document(email).collection("watchList").get()
            .addOnCompleteListener {
                val result:ArrayList<Movie> = ArrayList()
                if(it.isSuccessful && !it.result.isEmpty){
                    for( document in it.result.documents ){
                        val movie = document.toObject(Movie::class.java)!!
                        result.add(movie)
                    }
                    handleSuccess(result)
                }else{
                    handleFailure(
                        context.getString(R.string.error),
                        context.getString(R.string.error)
                    )
                }
            }.addOnFailureListener {
                it.printStackTrace()
                handleFailure(
                    context.getString(R.string.error),
                    context.getString(R.string.error)
                )
            }
    }

    fun addMovieToWatchList(context:Context, movie:Movie, handleSuccess:(movie:Movie)->Unit){
        db.collection("users")
            .document(email).collection("watchList").add(movie)
            .addOnCompleteListener {
                handleSuccess(movie)
            }.addOnFailureListener {
                it.printStackTrace()
                handleFailure(
                    context.getString(R.string.error),
                    context.getString(R.string.error)
                )
            }
    }

}