package tech.benhack.home.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tech.benhack.auth.repository.AuthRepositoryImpl
import tech.benhack.di.IoDispatcher
import tech.benhack.di.MainDispatcher
import tech.benhack.home.R
import tech.benhack.movies.usecase.DeleteUserDataUseCase
import tech.benhack.ui.helpers.DialogManager
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepositoryImpl,
    private val deleteUserDataUseCase: DeleteUserDataUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    val accountDeleted = MutableLiveData(false)
    val isLoading = MutableLiveData(false)

    val email by lazy {
        authRepository.getEmail()
    }

    @Inject
    lateinit var dialogManager: DialogManager

    fun deleteAccount(context:Context){
        isLoading.postValue(true)
        viewModelScope.launch(ioDispatcher) {
            val authToken = authRepository.getAuthToken()
            if(authToken!=null){
                val isDataDeleted = deleteUserDataUseCase(email, authToken)
                if(isDataDeleted){
                    val isAccountDeleted = authRepository.deleteAccount(email)
                    if(isAccountDeleted){
                        accountDeleted.postValue(true)
                        withContext(mainDispatcher){
                            dialogManager.showAlert(context, R.string.success, R.string.delete_account_success)
                            accountDeleted.postValue(true)
                        }
                    }
                    else  {
                        withContext(mainDispatcher){
                            dialogManager.showAlert(context, R.string.warning, R.string.delete_account_error)
                            accountDeleted.postValue(true)
                        }
                    }
                }else {
                    withContext(mainDispatcher){
                        dialogManager.showAlert(context, R.string.error, R.string.delete_user_data_error)
                    }
                }
            }else {
                withContext(mainDispatcher){
                    dialogManager.showAlert(context, R.string.error, R.string.delete_user_data_error)
                }
            }

            isLoading.postValue(false)
        }
    }
}