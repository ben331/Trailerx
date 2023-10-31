package com.globant.imdb.data.remote.firebase

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.globant.imdb.R
import com.globant.imdb.data.model.movies.Movie


class FirestoreManager private constructor(
    val context:Context?,
    val handleSuccessGetMovies:(movies:ArrayList<Movie>)->Unit,
    val handleSuccessAddMovie:(movie:Movie)->Unit,
    val handleFailure:(title:String, msg:String)->Unit
){
    private val db: FirebaseFirestore by lazy {
        Firebase.firestore
    }

    private val email: String by lazy {
        FirebaseAuthManager().getEmail()
    }

    class Builder {
        private var context: Context? = null
        private var handleSuccessGetMovies: (movies:ArrayList<Movie>)->Unit= {}
        private var handleSuccessAddMovie: (movie:Movie)->Unit = {}
        private var handleFailure: (title:String, msg:String)->Unit = { _: String, _: String -> }

        fun setContext(context:Context): Builder {
            this.context = context
            return this
        }

        fun setHandleSuccessGetMovies(handleSuccessGetMovies: (movies:ArrayList<Movie>)->Unit): Builder {
            this.handleSuccessGetMovies = handleSuccessGetMovies
            return this
        }
        fun setHandleSuccessAddMovie(handleSuccessAddMovie: (movie:Movie)->Unit): Builder {
            this.handleSuccessAddMovie = handleSuccessAddMovie
            return this
        }

        fun setHandleFailure(handleFailure: (title:String, msg:String)->Unit): Builder {
            this.handleFailure = handleFailure
            return this
        }

        fun build(): FirestoreManager {
            return FirestoreManager(context, handleSuccessGetMovies, handleSuccessAddMovie, handleFailure)
        }
    }

    fun getWatchList() {
        db.collection("users").document(email).collection("watchList").get()
            .addOnCompleteListener {
                val result:ArrayList<Movie> = ArrayList()
                if(it.isSuccessful && !it.result.isEmpty){
                    for( document in it.result.documents ){
                        val movie = document.toObject(Movie::class.java)!!
                        result.add(movie)
                    }
                }
                handleSuccessGetMovies(result)
            }.addOnFailureListener {
                handleFailure(
                    context!!.getString(R.string.error),
                    context.getString(R.string.error)
                )
            }
    }

    fun addMovieToWatchList(movie:Movie){
        db.collection("users")
            .document(email).collection("watchList").add(movie)
            .addOnCompleteListener {
                handleSuccessAddMovie(movie)
            }.addOnFailureListener {
                handleFailure(
                    context!!.getString(R.string.error),
                    context.getString(R.string.error)
                )
            }
    }

}