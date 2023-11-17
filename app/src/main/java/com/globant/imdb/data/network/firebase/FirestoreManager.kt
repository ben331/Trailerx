package com.globant.imdb.data.network.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.globant.imdb.R
import com.globant.imdb.data.model.movies.MovieModel
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
        numberList:Int,
        handleSuccess: (movies:List<MovieModel>)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ) {
        val collection = when(numberList){
            1 -> "watchList"
            2 -> "recentlyViewed"
            3 -> "favoritePeople"
            else -> null
        }
        collection?.let {
            db.collection("users").document(email).collection(collection).get()
                .addOnSuccessListener {
                    val result:ArrayList<MovieModel> = ArrayList()
                    if(!it.isEmpty){
                        for( document in it.documents ){
                            val movie = document.toObject(MovieModel::class.java)!!
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
    }

    fun addMovieToList(
        movie:MovieItem, listNumber: Int,
        handleSuccess:(movie:MovieItem)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ){
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
                        R.string.error,
                        R.string.error
                    )
                }
        }
    }

    fun deleteMovieFromList(
        movieId:Int,
        listNumber:Int,
        handleSuccess:()->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
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
                        R.string.error,
                        R.string.delete_movie_error
                    )
                }
        }
    }
}