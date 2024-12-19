package com.cevdetkilickeser.learnconnect.ui.presentation.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.cevdetkilickeser.learnconnect.R
import com.cevdetkilickeser.learnconnect.collectWithLifecycle
import com.cevdetkilickeser.learnconnect.ui.presentation.profile.ProfileContract.UiAction
import com.cevdetkilickeser.learnconnect.ui.presentation.profile.ProfileContract.UiEffect
import com.cevdetkilickeser.learnconnect.ui.presentation.profile.ProfileContract.UiState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalPermissionsApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun ProfileScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    uiAction: (UiAction) -> Unit,
    isDarkTheme: Boolean,
    changeAppTheme: () -> Unit,
    removeUserIdFromSharedPref: () -> Unit,
    navigateToSignIn: () -> Unit,
    updateTopBarName: () -> Unit
) {
    val context = LocalContext.current
    uiEffect.collectWithLifecycle { effect ->
        when (effect) {
            UiEffect.NavigateToSignIn -> navigateToSignIn()
            UiEffect.RemoveUserIdFromSharedPref -> removeUserIdFromSharedPref()
            is UiEffect.ShowToast -> {
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    val galleryPermission = rememberPermissionState(
        if (Build.VERSION.SDK_INT >= 33) Manifest.permission.READ_MEDIA_IMAGES
        else Manifest.permission.READ_EXTERNAL_STORAGE
    )

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            uri?.let {
                uiAction(UiAction.ProfileImageSelected(uri))
            }
        }
    }

    if (uiState.showChangePasswordDialog) {
        ChangePasswordDialog(
            onDismiss = { uiAction(UiAction.ChangePasswordDialogNegativeClicked) },
            onConfirm = { currentPassword, newPassword ->
                uiAction(
                    UiAction.ChangePasswordDialogPositiveClicked(
                        currentPassword,
                        newPassword
                    )
                )
            }
        )
    }

    if (uiState.showNameDialog) {
        ChangeNameDialog(
            onDismiss = { uiAction(UiAction.NameDialogNegativeClicked) },
            onConfirm = { name ->
                uiAction(UiAction.NameDialogPositiveClicked(name, updateTopBarName))
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.primary
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center)
            ) {
                Card(
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface),
                    shape = CircleShape,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .clickable {
                            if (galleryPermission.status.isGranted) {
                                val intent = Intent(Intent.ACTION_PICK).apply {
                                    type = "image/*"
                                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                                }
                                galleryLauncher.launch(intent)
                            } else {
                                galleryPermission.launchPermissionRequest()
                            }
                        }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        GlideImage(
                            model = uiState.imageUri,
                            contentDescription = null,
                            failure = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Text(
                    text = uiState.user?.email ?: "e-mail",
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = uiState.user?.name ?: "Input Name",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    modifier = Modifier.clickable { uiAction(UiAction.NameClicked) }
                )
            }
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxSize()
                    .padding(horizontal = 32.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            color = MaterialTheme.colorScheme.surface
                        )
                        .clickable { uiAction(UiAction.ChangePasswordClicked) }
                ) {
                    Text(
                        text = "Change Password",
                        modifier = Modifier
                            .padding(16.dp)
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 16.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            color = MaterialTheme.colorScheme.surface
                        )
                ) {
                    Text(
                        text = "Light - Dark",
                        modifier = Modifier
                            .padding(16.dp)
                    )
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { changeAppTheme() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = Color.Black,
                            uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                            uncheckedTrackColor = Color.White,
                            uncheckedBorderColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .padding(end = 16.dp)
                    )
                }
                Button(
                    onClick = { uiAction(UiAction.SignOutClicked) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 64.dp)
                ) {
                    Text(text = stringResource(id = R.string.sign_out))
                }
            }
        }
    }
}