package com.globant.imdb.data.network.firebase

import androidx.annotation.StringRes
import com.google.firebase.firestore.FirebaseFirestore
import com.globant.imdb.R
import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.data.model.user.UserModel
import com.globant.imdb.domain.model.MovieItem
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import javax.inject.Inject


private const val TIMEOUT = 2000L

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
                handleSuccess(null)
            }
    }

    fun getUser(
        localEmail:String,
        handleSuccess:(user:UserModel?)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ) {
        val task = db.collection("users").document(localEmail).get()
            .addOnSuccessListener {
                if(it.exists()){
                    val user = it.toObject(UserModel::class.java)
                    handleSuccess(user)
                }else{
                    handleSuccess(null)
                }
            }.addOnFailureListener {
                handleFailure(
                    R.string.error,
                    R.string.create_user_error
                )
            }
        addTaskTimeOut(task, R.string.create_user_error, handleFailure)
    }

    fun getUserMoviesList(
        listType:CategoryType,
        handleSuccess: (movies:List<MovieItem>)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ) {
        val task = db.collection("users").document(email).collection(listType.name).get()
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
                handleFailure(
                    R.string.error,
                    R.string.fetch_movies_error
                )
            }
        addTaskTimeOut(task, R.string.fetch_movies_error, handleFailure)
    }

    fun addMovieToList(
        movie:MovieItem, category:CategoryType,
        handleSuccess:(movie:MovieItem, category:CategoryType)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ){
        val task = db.collection("users")
            .document(email).collection(category.name).document(movie.id.toString()).set(movie)
            .addOnCompleteListener {
                handleSuccess(movie, category)
            }.addOnFailureListener {
                handleFailure(
                    R.string.error,
                    R.string.error
                )
            }
        addTaskTimeOut(task, R.string.error, handleFailure)
    }

    fun deleteMovieFromList(
        movieId:Int,
        category:CategoryType,
        handleSuccess:(movieId:Int, category:CategoryType)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ){
        val task = db.collection("users")
            .document(email).collection(category.name).document(movieId.toString()).delete()
            .addOnSuccessListener {
                handleSuccess(movieId, category)
            }.addOnFailureListener {
                handleFailure(
                    R.string.error,
                    R.string.delete_movie_error
                )
            }
        addTaskTimeOut(task, R.string.delete_movie_error, handleFailure)
    }

    private fun addTaskTimeOut( task: Task<*>, @StringRes msg:Int, onTimeout:(title:Int, msg:Int)->Unit ){
        CoroutineScope(Dispatchers.Default).launch{
            try {
                withTimeout(TIMEOUT) {
                    while(!(task.isComplete || task.isCanceled)){
                        delay(100L)
                    }
                }
            }catch(e: TimeoutCancellationException){
                task.addOnCompleteListener {  }
                withContext(Dispatchers.Main){
                    onTimeout(
                        R.string.error,
                        msg
                    )
                }
            }
        }
    }
}