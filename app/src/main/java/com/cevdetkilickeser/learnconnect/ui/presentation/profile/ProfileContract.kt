package com.cevdetkilickeser.learnconnect.ui.presentation.profile

import android.net.Uri
import com.cevdetkilickeser.learnconnect.data.entity.User

object ProfileContract {
    data class UiState(
        val user: User? = null,
        val imageUri: Uri? = null,
        val showNameDialog: Boolean = false,
        val showChangePasswordDialog: Boolean = false,
    )

    sealed interface UiAction {
        data class ProfileImageSelected(val uri: Uri) : UiAction
        data object NameClicked : UiAction
        data class NameDialogPositiveClicked(val name: String, val updateTopBarName: () -> Unit) : UiAction
        data object NameDialogNegativeClicked : UiAction
        data object ChangePasswordClicked : UiAction
        data class ChangePasswordDialogPositiveClicked(val currentPassword: String, val newPassword: String) : UiAction
        data object ChangePasswordDialogNegativeClicked : UiAction
        data object SignOutClicked : UiAction
    }

    sealed class UiEffect {
        data object RemoveUserIdFromSharedPref: UiEffect()
        data object NavigateToSignIn : UiEffect()
        data class ShowToast(val message: String) : UiEffect()
    }
}