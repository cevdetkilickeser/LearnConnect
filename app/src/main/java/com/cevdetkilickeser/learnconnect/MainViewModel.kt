package com.cevdetkilickeser.learnconnect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.learnconnect.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _name = MutableStateFlow<String?>(null)
    val name: StateFlow<String?> = _name

    fun getUserInfo(userId: Int) {
        viewModelScope.launch {
            if (userId > 0) {
                val user = userRepository.getUserInfo(userId)
                _name.value = user.name
            }
        }
    }
}