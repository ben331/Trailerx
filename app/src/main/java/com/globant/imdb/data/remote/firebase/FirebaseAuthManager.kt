package com.globant.imdb.data.remote.firebase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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
    GUEST,
    BASIC,
    GOOGLE,
    FACEBOOK,
    APPLE
}

class FirebaseAuthManager {

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    val callbackManager: CallbackManager = CallbackManager.Factory.create()

    fun updateProfilePhotoURL(
        context: Context,
        url: Uri?,
        handleSuccess: () -> Unit,
        handleAlert: (title:String, msg:String) -> Unit,
    ){
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(url)
            .build()

        auth.currentUser?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if(task.isSuccessful){
                    handleSuccess()
                }else{
                    handleAlert(
                        context.getString(R.string.error),
                        context.getString(R.string.fail_new_photo)
                    )
                }
            }
    }

    fun getProfilePhotoURL():Uri?{
        return auth.currentUser?.photoUrl
    }

    fun getProfileEmail():String?{
        return auth.currentUser?.email
    }

    fun logout(provider:ProviderType){
        auth.signOut()
        if(provider == ProviderType.FACEBOOK){
            LoginManager.getInstance().logOut()
        }
    }

    fun sendPasswordResetEmail(
        context: Context,
        email: String,
        onSuccess: ()->Unit,
        onFailure:(title:String, msg:String)->Unit
        ){
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            onSuccess()
        }.addOnCanceledListener {
            onFailure(
                ContextCompat.getString(context, R.string.error ),
                ContextCompat.getString(context, R.string.password_reset_error )
            )
        }
    }

    private fun uploadName(
        context: Context,
        displayName:String,
        onFailure: (title:String, message:String) -> Unit
    ) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(displayName)
            .build()

        auth.currentUser?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if(!task.isSuccessful){
                    onFailure(
                        ContextCompat.getString(context,R.string.error),
                        ContextCompat.getString(context, R.string.upload_username_error)
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
        context: Context,
        email:String,
        password: String,
        displayName: String,
        onSuccess: (email: String) -> Unit,
        onFailure: (title:String, msg: String) -> Unit
    ){
        auth.createUserWithEmailAndPassword( email, password )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { verificationTask ->
                            if (verificationTask.isSuccessful) {
                                uploadName(context, displayName, onFailure)
                                onSuccess(email)
                            } else {
                                onFailure(
                                    ContextCompat.getString(context, R.string.error),
                                    ContextCompat.getString(context, R.string.auth_error),
                                )
                            }
                        }
                } else {
                    onFailure(
                        ContextCompat.getString(context, R.string.error),
                        ContextCompat.getString(context, R.string.auth_error),
                    )
                }
            }
    }

    fun loginWithEmailAndPassword(
        context: Context,
        email:String,
        password:String,
        onSuccess: (email:String, provides:ProviderType)->Unit,
        onFailure:(title:String, msg:String)->Unit){

        auth.signInWithEmailAndPassword( email, password ).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = it.result.user!!
                if(user.isEmailVerified){
                    onSuccess(email, ProviderType.BASIC)
                }else{
                    onFailure(
                        ContextCompat.getString(context, R.string.error),
                        ContextCompat.getString(context, R.string.account_created_success)
                    )
                }
            } else {
                onFailure(
                    ContextCompat.getString(context, R.string.error),
                    ContextCompat.getString(context, R.string.auth_error),
                )
            }
        }
    }

    fun loginWithApple(
        activity: Activity,
        onSuccess: (email:String, provides:ProviderType)->Unit,
        onFailure:(title:String, msg:String)->Unit
    ){
        val provider = OAuthProvider.newBuilder("apple.com")
        provider.scopes = listOf("email", "name")
        provider.addCustomParameter("locale", "es_MX")

        val pending = auth.pendingAuthResult
        if (pending != null) {
            pending.addOnSuccessListener { authResult ->
                val email = authResult.user?.email ?: ""
                val displayName = authResult.user?.displayName ?: ""

                uploadName(activity, displayName, onFailure)
                onSuccess(email, ProviderType.APPLE)
            }.addOnFailureListener {
                onFailure(
                    ContextCompat.getString(activity, R.string.error),
                    ContextCompat.getString(activity, R.string.auth_error),
                )
            }
        } else {
            auth.startActivityForSignInWithProvider( activity, provider.build())
                .addOnSuccessListener { authResult ->
                    val email = authResult.user?.email ?: ""
                    val displayName = authResult.user?.displayName ?: ""

                    uploadName(activity, displayName, onFailure)
                    onSuccess(email, ProviderType.APPLE)
                }
                .addOnFailureListener {
                    onFailure(
                        ContextCompat.getString(activity, R.string.error),
                        ContextCompat.getString(activity, R.string.auth_error),
                    )
                }
        }
    }

    fun loginWithFacebook(
        fragment: Fragment,
        onSuccess: (email:String, provides:ProviderType)->Unit,
        onFailure:(title:String, msg:String)->Unit
    ){
        LoginManager.getInstance().logInWithReadPermissions(fragment, listOf("email"))

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

                                uploadName(fragment.requireContext(), displayName, onFailure)
                                onSuccess( email, ProviderType.FACEBOOK)
                            }else{
                                onFailure(
                                    fragment.requireContext().getString(R.string.error),
                                    task.exception?.message.toString(),
                                )
                            }
                        }
                    }
                }

                override fun onCancel() {
                    onFailure(
                        fragment.requireContext().getString(R.string.error),
                        fragment.requireContext().getString(R.string.auth_error),
                    )
                }

                override fun onError(error: FacebookException?) {
                    onFailure(
                        fragment.requireContext().getString(R.string.error),
                        fragment.requireContext().getString(R.string.auth_error),
                    )
                }
            }
        )
    }

    fun loginWithGoogle(context: Context): Intent{
        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(ContextCompat.getString(context, com.firebase.ui.auth.R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleClient = GoogleSignIn.getClient(context, googleConf)
        googleClient.signOut()
        return googleClient.signInIntent
    }

    fun onGoogleResult(
        context: Context,
        intent:Intent?,
        onSuccess: (email:String, provides:ProviderType)->Unit,
        onFailure:(title:String, msg:String)->Unit
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

                        uploadName(context, displayName, onFailure)
                        onSuccess(email, ProviderType.GOOGLE)
                    }else{
                        onFailure(
                            ContextCompat.getString(context, R.string.error),
                            ContextCompat.getString(context, R.string.auth_error),
                        )
                    }
                }
            }
        }catch (e: ApiException){
            onFailure(
                ContextCompat.getString(context, R.string.error),
                ContextCompat.getString(context, R.string.auth_error)
            )
        }
    }

}