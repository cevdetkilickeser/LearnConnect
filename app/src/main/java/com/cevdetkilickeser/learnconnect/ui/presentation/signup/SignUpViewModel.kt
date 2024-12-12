package com.cevdetkilickeser.learnconnect.ui.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.learnconnect.domain.repository.UserRepositoryImpl
import com.cevdetkilickeser.learnconnect.ui.presentation.signup.SignUpContract.UiAction
import com.cevdetkilickeser.learnconnect.ui.presentation.signup.SignUpContract.UiEffect
import com.cevdetkilickeser.learnconnect.ui.presentation.signup.SignUpContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val userRepository: UserRepositoryImpl) :
    ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<UiEffect>() }
    val uiEffect: Flow<UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    fun onAction(uiAction: UiAction) {
        when (uiAction) {
            is UiAction.EmailChanged -> updateUiState { copy(email = uiAction.email) }
            is UiAction.PasswordChanged -> updateUiState { copy(password = uiAction.password) }
            is UiAction.SignUpClicked -> signUp(_uiState.value.email, _uiState.value.password)
        }
    }

    private fun signUp(email: String, password: String) {
        viewModelScope.launch {
            val userId = userRepository.signUp(email, password)
            if (userId > 0) {
                emitUiEffect(UiEffect.SaveUserIdToShared(userId))
                emitUiEffect(UiEffect.NavigateToProfile)
            } else {
                emitUiEffect(UiEffect.ShowToast("This email already taken"))
            }
        }
    }

    private fun updateUiState(block: UiState.() -> UiState) {
        _uiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: UiEffect) {
        _uiEffect.send(uiEffect)
    }
}