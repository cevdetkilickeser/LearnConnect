package com.cevdetkilickeser.learnconnect.ui.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.learnconnect.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _userId = MutableStateFlow(-1)
    val userId: StateFlow<Int> = _userId

    private val _isSignUpSuccessful = MutableStateFlow(false)
    val isSignUpSuccessful: StateFlow<Boolean> = _isSignUpSuccessful

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            val userId = userRepository.signUp(email, password)
            _userId.value = userId
            _isSignUpSuccessful.value = userId > 0
        }
    }
}