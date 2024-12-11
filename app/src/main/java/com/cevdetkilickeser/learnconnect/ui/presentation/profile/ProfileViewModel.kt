package com.cevdetkilickeser.learnconnect.ui.presentation.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.learnconnect.data.entity.User
import com.cevdetkilickeser.learnconnect.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _uri = MutableStateFlow<Uri?>(null)
    val uri: StateFlow<Uri?> = _uri

    private val _passwordChanged = MutableSharedFlow<Boolean>()
    val passwordChanged: SharedFlow<Boolean> = _passwordChanged

    fun getUserInfo(userId: Int) {
        viewModelScope.launch {
            val user = userRepository.getUserInfo(userId)
            val uri = user.image?.let {
                Uri.parse(user.image)
            }
            _user.value = user
            _uri.value = uri
        }
    }

    fun changePassword(userId: Int, currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            val result = userRepository.changePassword(userId, currentPassword, newPassword)
            _passwordChanged.emit(result > 0)
        }
    }

    fun changeName(userId: Int, name: String, updateTopBarName: () -> Unit) {
        viewModelScope.launch {
            userRepository.changeName(userId, name)
            getUserInfo(userId)
            updateTopBarName()
        }
    }

    fun uploadImage(userId: Int, uri: Uri?) {
        viewModelScope.launch {
            userRepository.uploadImage(userId, uri)
            getUserInfo(userId)
        }
    }
}