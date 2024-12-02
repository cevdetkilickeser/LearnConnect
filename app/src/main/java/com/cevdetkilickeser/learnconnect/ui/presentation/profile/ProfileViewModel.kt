package com.cevdetkilickeser.learnconnect.ui.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.learnconnect.data.entity.User
import com.cevdetkilickeser.learnconnect.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _passwordChanged = MutableSharedFlow<Boolean>()
    val passwordChanged: SharedFlow<Boolean> = _passwordChanged

    fun getUserInfo(userId: Int) {
        viewModelScope.launch {
            viewModelScope.launch {
                val user = userRepository.getUserInfo(userId)
                _user.value = user
            }
        }
    }

    fun changePassword(userId: Int, currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            val result = userRepository.changePassword(userId, currentPassword, newPassword)
            _passwordChanged.emit(result > 0)
        }
    }
}