package com.cevdetkilickeser.learnconnect.ui.presentation.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.learnconnect.data.repository.UserRepository
import com.cevdetkilickeser.learnconnect.ui.presentation.profile.ProfileContract.UiAction
import com.cevdetkilickeser.learnconnect.ui.presentation.profile.ProfileContract.UiEffect
import com.cevdetkilickeser.learnconnect.ui.presentation.profile.ProfileContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) :
    ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<UiEffect>() }
    val uiEffect: Flow<UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    fun onAction(action: UiAction) {
        val userId = uiState.value.user?.userId ?: 0
        when (action) {
            is UiAction.GetProfile -> getUserInfo(action.userId)
            is UiAction.ProfileImageSelected -> uploadImage(action.uri)
            is UiAction.NameClicked -> updateUiState { copy(showNameDialog = true) }
            is UiAction.NameDialogPositiveClicked -> changeName(
                userId, action.name, action.updateTopBarName
            )

            is UiAction.NameDialogNegativeClicked -> updateUiState { copy(showNameDialog = false) }
            is UiAction.ChangePasswordClicked -> updateUiState { copy(showChangePasswordDialog = true) }
            is UiAction.ChangePasswordDialogPositiveClicked -> changePassword(
                userId, action.currentPassword, action.newPassword
            )

            is UiAction.ChangePasswordDialogNegativeClicked -> updateUiState {
                copy(
                    showChangePasswordDialog = false
                )
            }

            is UiAction.SignOutClicked -> signOut()
        }
    }

    private fun getUserInfo(userId: Int) {
        viewModelScope.launch {
            val user = userRepository.getUserInfo(userId)
            val uri = user.image?.let { Uri.parse(user.image) }
            updateUiState { copy(user = user, imageUri = uri) }
        }
    }

    private fun changePassword(userId: Int, currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            val result = userRepository.changePassword(userId, currentPassword, newPassword)
            if (result > 0) {
                emitUiEffect(UiEffect.ShowToast("Password changed"))
                updateUiState { copy(showChangePasswordDialog = false) }
            } else {
                emitUiEffect(UiEffect.ShowToast("Current password is wrong"))
            }
        }
    }

    private fun changeName(userId: Int, name: String, updateTopBarName: () -> Unit) {
        viewModelScope.launch {
            userRepository.changeName(userId, name)
            getUserInfo(userId)
            updateTopBarName()
            updateUiState { copy(showNameDialog = false) }
        }
    }

    private fun uploadImage(uri: Uri?) {
        viewModelScope.launch {
            val userId = uiState.value.user?.userId ?: 0
            userRepository.uploadImage(userId, uri)
            getUserInfo(userId)
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            emitUiEffect(UiEffect.RemoveUserIdFromSharedPref)
            emitUiEffect(UiEffect.NavigateToSignIn)
        }
    }

    private fun updateUiState(block: UiState.() -> UiState) {
        _uiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: UiEffect) {
        _uiEffect.send(uiEffect)
    }
}