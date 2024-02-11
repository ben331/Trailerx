package tech.benhack.auth.repository

import android.app.Activity
import android.content.Intent
import android.net.Uri
import tech.benhack.auth.datasource.remote.AuthRemoteDataSourceFirebase
import tech.benhack.auth.datasource.remote.ProviderType
import tech.benhack.auth.datasource.remote.response.UserModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val dataSource: AuthRemoteDataSourceFirebase
) {
    suspend fun createUser(user: UserModel): Boolean {
        return dataSource.createUser(user)
    }

    suspend fun getUser(localEmail: String): UserModel? {
        return dataSource.getUser(localEmail)
    }

    fun updateProfilePhotoURL(
        url: Uri?,
        handleSuccess: () -> Unit,
        handleFailure: () -> Unit
    ) = dataSource.updateProfilePhotoURL(url, handleSuccess, handleFailure)

    fun getProfilePhotoURL(): Uri? = dataSource.getProfilePhotoURL()
    fun getEmail(): String = dataSource.getEmail()
    fun getDisplayName(): String = dataSource.getDisplayName()
    fun logout(provider: ProviderType) = dataSource.logout(provider)
    fun sendPasswordResetEmail(
        email: String,
        onSuccess: () -> Unit,
        onFailure: (title: Int, msg: Int) -> Unit
    ) = dataSource.sendPasswordResetEmail(email, onSuccess, onFailure)

    fun setupName(useName: (remoteDisplayName: String?) -> Unit) = dataSource.setupName(useName)

    fun signUpWithEmailAndPassword(
        email: String,
        password: String,
        displayName: String,
        onSuccess: (email: String) -> Unit,
        onFailure: (title: Int, msg: Int) -> Unit
    ) = dataSource.signUpWithEmailAndPassword(
        email,
        password,
        displayName,
        onSuccess,
        onFailure
    )

    fun loginWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: (email: String, provides: ProviderType) -> Unit,
        onFailure: (title: Int, msg: Int) -> Unit
    ) = dataSource.loginWithEmailAndPassword(
        email,
        password,
        onSuccess,
        onFailure
    )

    fun loginWithApple(
        activity: Activity,
        onSuccess: (email: String, provides: ProviderType) -> Unit,
        onFailure: (title: Int, msg: Int) -> Unit
    ) = dataSource.loginWithApple(
        activity,
        onSuccess,
        onFailure
    )

    fun loginWithFacebook(
        activity: Activity,
        onSuccess: (email: String, provides: ProviderType) -> Unit,
        onFailure: (title: Int, msg: Int) -> Unit
    ) = dataSource.loginWithFacebook(
        activity,
        onSuccess,
        onFailure
    )

    fun loginWithGoogle(activity: Activity): Intent = dataSource.loginWithGoogle(activity)

    fun onGoogleResult(
        intent: Intent?,
        onSuccess: (email: String, provides: ProviderType) -> Unit,
        onFailure: (title: Int, msg: Int) -> Unit
    ) = dataSource.onGoogleResult(
        intent,
        onSuccess,
        onFailure
    )

    suspend fun deleteAccount(email:String):Boolean = dataSource.deleteAccount(email)

    suspend fun getAuthToken():String? = dataSource.getAuthToken()
}