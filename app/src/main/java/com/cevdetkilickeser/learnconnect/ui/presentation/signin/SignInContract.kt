package com.cevdetkilickeser.learnconnect.ui.presentation.signin

object SignInContract {
    data class UiState(
        val email: String = "",
        val password: String = ""
    )

    sealed interface UiAction {
        data class EmailChanged(val email: String) : UiAction
        data class PasswordChanged(val password: String) : UiAction
        data object SignInClicked : UiAction
    }

    sealed class UiEffect {
        data object NavigateToSignUp : UiEffect()
        data object NavigateToHome : UiEffect()
        data class SaveUserIdToShared(val userId: Int) : UiEffect()
        data class ShowToast(val message: String) : UiEffect()
    }
}