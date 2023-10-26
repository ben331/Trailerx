package com.globant.imdb.data.remote.firebase

import android.app.Activity
import android.content.Intent
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.globant.imdb.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest

enum class ProviderType {
    BASIC,
    GOOGLE,
    FACEBOOK,
    APPLE
}

class FirebaseAuthManager {

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val callbackManager = CallbackManager.Factory.create()

    private fun uploadName(displayName:String, onFailure: (message:String) -> Unit) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(displayName)
            .build()

        auth.currentUser?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if(!task.isSuccessful){
                    onFailure(R.string.upload_username_error.toString())
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
        onSuccess: (email: String, provides: ProviderType) -> Unit,
        onFailure: (msg: String) -> Unit
    ){
        auth.createUserWithEmailAndPassword( email, password )
            .addOnCompleteListener {
                if(it.isSuccessful){
                    uploadName(displayName, onFailure)
                    onSuccess(email, ProviderType.BASIC)
                }else{
                    onFailure(R.string.auth_error.toString())
                }
            }
    }

    fun loginWithEmailAndPassword(
        email:String,
        password:String,
        onSuccess: (email:String, provides:ProviderType)->Unit,
        onFailure:(msg:String)->Unit){

        auth.signInWithEmailAndPassword( email, password ).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess(email, ProviderType.BASIC)
            } else {
                onFailure(R.string.auth_error.toString())
            }
        }
    }

    fun loginWithApple(
        activity: Activity,
        onSuccess: (email:String, provides:ProviderType)->Unit,
        onFailure:(msg:String)->Unit
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
                onFailure(R.string.auth_error.toString())
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
                    onFailure(R.string.auth_error.toString())
                }
        }
    }

    fun loginWithFacebook(
        activity: Activity,
        onSuccess: (email:String, provides:ProviderType)->Unit,
        onFailure:(msg:String)->Unit
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
                                onFailure(R.string.auth_error.toString())
                            }
                        }
                    }
                }

                override fun onCancel() { }

                override fun onError(error: FacebookException?) {
                    onFailure(R.string.auth_error.toString())
                }
            }
        )
    }

    fun loginWithGoogle(activity: Activity): Intent{
        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(com.firebase.ui.auth.R.string.default_web_client_id.toString())
            .requestEmail()
            .build()

        val googleClient = GoogleSignIn.getClient(activity, googleConf)
        googleClient.signOut()
        return googleClient.signInIntent
    }

    fun onGoogleResult(
        intent:Intent?,
        onSuccess: (email:String, provides:ProviderType)->Unit,
        onFailure:(msg:String)->Unit
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
                        onFailure(R.string.auth_error.toString())
                    }
                }
            }
        }catch (e: ApiException){
            onFailure(R.string.auth_error.toString())
        }
    }

}