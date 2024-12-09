package com.cevdetkilickeser.learnconnect.ui.presentation.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.learnconnect.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private val _userId = MutableStateFlow(-1)
    val userId: StateFlow<Int> = _userId

    private val _isSignInSuccessful = MutableSharedFlow<Boolean?>()
    val isSignInSuccessful: SharedFlow<Boolean?> = _isSignInSuccessful

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            val userId = userRepository.signIn(email, password)
            _userId.value = userId
            _isSignInSuccessful.emit(userId > 0)
        }
    }
}