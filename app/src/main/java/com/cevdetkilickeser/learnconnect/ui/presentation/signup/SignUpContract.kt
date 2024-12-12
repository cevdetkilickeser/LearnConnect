package com.cevdetkilickeser.learnconnect.ui.presentation.signup

object SignUpContract {
    data class UiState(
        val email: String = "",
        val password: String = ""
    )

    sealed interface UiAction {
        data class EmailChanged(val email: String) : UiAction
        data class PasswordChanged(val password: String) : UiAction
        data object SignUpClicked : UiAction
    }

    sealed class UiEffect {
        data object NavigateToSignIn : UiEffect()
        data object NavigateToProfile : UiEffect()
        data class SaveUserIdToShared(val userId: Int) : UiEffect()
        data class ShowToast(val message: String) : UiEffect()
    }
}