package com.globant.auth.datasource.remote

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.globant.auth.R
import com.globant.auth.datasource.remote.response.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

enum class ProviderType {
    GUEST,
    BASIC,
    GOOGLE,
    FACEBOOK,
    APPLE
}

private const val SHORT_TIMEOUT = 2500L
private const val LONG_TIMEOUT = 5000L

class AuthRemoteDataSourceFirebase @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val callbackManager: CallbackManager,
    private val googleConf: GoogleSignInOptions
){
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
                val document: DocumentSnapshot =
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

    fun updateProfilePhotoURL(
        url: Uri?,
        handleSuccess: () -> Unit,
        handleFailure: () -> Unit
    ) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(url)
            .build()

        auth.currentUser?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if(task.isSuccessful){
                    handleSuccess()
                }else{
                    handleFailure()
                }
            }
    }

    fun getProfilePhotoURL():Uri?{
        return auth.currentUser?.photoUrl
    }
    fun getEmail():String{
        return auth.currentUser?.email ?: ""
    }
    fun getDisplayName():String{
        return auth.currentUser?.displayName ?: ""
    }
    fun logout(provider: ProviderType){
        auth.signOut()
        if(provider == ProviderType.FACEBOOK){
            LoginManager.getInstance().logOut()
        }
    }
    fun sendPasswordResetEmail(
        email: String,
        onSuccess: ()->Unit,
        onFailure:(title:Int, msg:Int)->Unit
        ){
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            onSuccess()
        }.addOnCanceledListener {
            onFailure(
                R.string.error,
                R.string.password_reset_error
            )
        }
    }
    private fun uploadName(
        displayName:String,
        onFailure: (title:Int, message:Int) -> Unit
    ) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(displayName)
            .build()

        auth.currentUser?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if(!task.isSuccessful){
                    onFailure(
                        R.string.error,
                        R.string.upload_username_error
                    )
                }
            }
    }

    fun setupName(useName: (remoteDisplayName:String?) -> Unit) {
        auth.currentUser?.displayName.let { remoteDisplayName ->
            useName(remoteDisplayName)
        }
    }

    fun signUpWithEmailAndPassword(
        email:String,
        password: String,
        displayName: String,
        onSuccess: (email: String) -> Unit,
        onFailure: (title:Int, msg: Int) -> Unit
    ){
        auth.createUserWithEmailAndPassword( email, password )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { verificationTask ->
                            if (verificationTask.isSuccessful) {
                                uploadName(displayName, onFailure)
                                onSuccess(email)
                            } else {
                                onFailure(
                                    R.string.error,
                                    R.string.auth_error,
                                )
                            }
                        }
                } else {
                    onFailure(
                        R.string.error,
                        R.string.auth_error,
                    )
                }
            }
    }

    fun loginWithEmailAndPassword(
        email:String,
        password:String,
        onSuccess: (email:String, provides: ProviderType)->Unit,
        onFailure:(title:Int, msg:Int)->Unit){

        auth.signInWithEmailAndPassword( email, password ).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = it.result.user!!
                if(user.isEmailVerified){
                    onSuccess(email, ProviderType.BASIC)
                }else{
                    onFailure(
                        R.string.error,
                        R.string.account_created_success
                    )
                }
            } else {
                onFailure(
                    R.string.error,
                    R.string.auth_error,
                )
            }
        }
    }

    fun loginWithApple(
        activity: Activity,
        onSuccess: (email:String, provides: ProviderType)->Unit,
        onFailure:(title:Int, msg:Int)->Unit
    ){
        val provider = OAuthProvider.newBuilder("apple.com")
        provider.scopes = listOf("email", "name")
        provider.addCustomParameter("locale", "es_MX")

        val pending = auth.pendingAuthResult
        if (pending != null) {
            pending.addOnSuccessListener { authResult ->
                val email = authResult.user?.email ?: ""
                val displayName = authResult.user?.displayName ?: ""

                uploadName(displayName, onFailure)
                onSuccess(email, ProviderType.APPLE)
            }.addOnFailureListener {
                onFailure(
                    R.string.error,
                    R.string.auth_error,
                )
            }
        } else {
            auth.startActivityForSignInWithProvider( activity, provider.build())
                .addOnSuccessListener { authResult ->
                    val email = authResult.user?.email ?: ""
                    val displayName = authResult.user?.displayName ?: ""

                    uploadName(displayName, onFailure)
                    onSuccess(email, ProviderType.APPLE)
                }
                .addOnFailureListener {
                    onFailure(
                        R.string.error,
                        R.string.auth_error,
                    )
                }
        }
    }

    fun loginWithFacebook(
        activity:Activity,
        onSuccess: (email:String, provides: ProviderType)->Unit,
        onFailure:(title:Int, msg:Int)->Unit
    ){
        LoginManager.getInstance().logInWithReadPermissions(activity, listOf("email"))

        LoginManager.getInstance().registerCallback( callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    result?.let {
                        val token = it.accessToken
                        val credential = FacebookAuthProvider.getCredential(token.token)
                        auth.signInWithCredential(credential).addOnCompleteListener { task ->
                            if(task.isSuccessful){

                                val email = task.result.user?.email ?: ""
                                val displayName = task.result.user?.displayName ?:""

                                uploadName(displayName, onFailure)
                                onSuccess( email, ProviderType.FACEBOOK)
                            }else{
                                onFailure(
                                    R.string.error,
                                    R.string.error,
                                )
                            }
                        }
                    }
                }

                override fun onCancel() {
                    onFailure(
                        R.string.error,
                        R.string.auth_error,
                    )
                }

                override fun onError(error: FacebookException?) {
                    onFailure(
                        R.string.error,
                        R.string.auth_error,
                    )
                }
            }
        )
    }

    fun loginWithGoogle( activity: Activity ): Intent{
        val googleClient = GoogleSignIn.getClient(activity, googleConf)
        googleClient.signOut()
        return googleClient.signInIntent
    }

    fun onGoogleResult(
        intent:Intent?,
        onSuccess: (email:String, provides: ProviderType)->Unit,
        onFailure:(title:Int, msg:Int)->Unit
    ){
        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)

        try {
            task.getResult(ApiException::class.java).let { account ->
                val credential =
                    GoogleAuthProvider.getCredential(account.idToken, null)

                auth.signInWithCredential(credential).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val email = account.email ?: ""
                        val displayName = account.displayName ?: ""

                        uploadName(displayName, onFailure)
                        onSuccess(email, ProviderType.GOOGLE)
                    }else{
                        onFailure(
                            R.string.error,
                            R.string.auth_error,
                        )
                    }
                }
            }
        }catch (e: ApiException){
            onFailure(
                R.string.error,
                R.string.auth_error
            )
        }
    }
}