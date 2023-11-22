package com.globant.imdb.data.network.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.globant.imdb.R
import com.globant.imdb.data.database.entities.movie.MovieListType
import com.globant.imdb.data.model.user.UserModel
import com.globant.imdb.domain.model.MovieItem
import javax.inject.Inject


class FirestoreManager @Inject constructor(
    private val db:FirebaseFirestore,
    private val auth:FirebaseAuthManager
) {

    private val email: String by lazy {
        auth.getEmail()
    }

    fun createUser(user: UserModel, handleSuccess: (user: UserModel?) -> Unit) {
        db.collection("users").document(user.email).set(user)
            .addOnCompleteListener {
                handleSuccess(user)
            }
            .addOnFailureListener {
                it.printStackTrace()
                handleSuccess(null)
            }
    }

    fun getUser(
        localEmail:String,
        handleSuccess:(user:UserModel?)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ) {
        db.collection("users").document(localEmail).get()
            .addOnSuccessListener {
                if(it.exists()){
                    val user = it.toObject(UserModel::class.java)
                    handleSuccess(user)
                }else{
                    handleSuccess(null)
                }
            }.addOnFailureListener {
                it.printStackTrace()
                handleFailure(
                    R.string.error,
                    R.string.create_user_error
                )
            }
    }

    fun getUserMoviesList(
        listType:MovieListType,
        handleSuccess: (movies:List<MovieItem>)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ) {
        db.collection("users").document(email).collection(listType.name).get()
            .addOnSuccessListener {
                val result:ArrayList<MovieItem> = ArrayList()
                if(!it.isEmpty){
                    for( document in it.documents ){
                        val movie = document.toObject(MovieItem::class.java)!!
                        result.add(movie)
                    }
                    handleSuccess(result)
                }else{
                    handleSuccess(emptyList())
                }
            }.addOnFailureListener {
                it.printStackTrace()
                handleFailure(
                    R.string.error,
                    R.string.fetch_movies_error
                )
            }
    }

    fun addMovieToList(
        movie:MovieItem, listType:MovieListType,
        handleSuccess:(movie:MovieItem)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ){
        db.collection("users")
            .document(email).collection(listType.name).document(movie.id.toString()).set(movie)
            .addOnCompleteListener {
                handleSuccess(movie)
            }.addOnFailureListener { e ->
                e.printStackTrace()
                handleFailure(
                    R.string.error,
                    R.string.error
                )
            }
    }

    fun deleteMovieFromList(
        movieId:Int,
        listType:MovieListType,
        handleSuccess:()->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ){
        db.collection("users")
            .document(email).collection(listType.name).document(movieId.toString()).delete()
            .addOnSuccessListener {
                handleSuccess()
            }.addOnFailureListener {
                handleFailure(
                    R.string.error,
                    R.string.delete_movie_error
                )
            }
    }
}