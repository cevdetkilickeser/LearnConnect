package com.cevdetkilickeser.learnconnect.ui.presentation.signin

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.cevdetkilickeser.learnconnect.R
import com.cevdetkilickeser.learnconnect.collectWithLifecycle
import com.cevdetkilickeser.learnconnect.ui.presentation.signin.SignInContract.UiAction
import com.cevdetkilickeser.learnconnect.ui.presentation.signin.SignInContract.UiEffect
import com.cevdetkilickeser.learnconnect.ui.presentation.signin.SignInContract.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SignInScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    uiAction: (UiAction) -> Unit,
    saveUserIdToShared: (Int) -> Unit,
    navigateToHome: () -> Unit,
    navigateToSignUp: () -> Unit
) {

    val context = LocalContext.current
    uiEffect.collectWithLifecycle { effect ->
        when (effect) {
            is UiEffect.SaveUserIdToShared -> saveUserIdToShared(effect.userId)
            is UiEffect.NavigateToHome -> navigateToHome()
            is UiEffect.NavigateToSignUp -> navigateToSignUp()
            is UiEffect.ShowToast -> {
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
                .align(Alignment.TopCenter)
        ) {
            Surface(
                shape = RoundedCornerShape(32.dp),
                modifier = Modifier
                    .size(128.dp)
                    .padding()
            ) {
                GlideImage(
                    model = R.drawable.ic_app_icon,
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(id = R.string.slogan),
                style = MaterialTheme.typography.labelLarge,
                color = Color.White,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Surface(
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { uiAction(UiAction.EmailChanged(it)) },
                    label = { Text(text = stringResource(id = R.string.email)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Mail,
                            contentDescription = stringResource(id = R.string.email),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )

                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { uiAction(UiAction.PasswordChanged(it)) },
                    label = { Text(text = stringResource(id = R.string.password)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = stringResource(id = R.string.password),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )

                Button(
                    onClick = { uiAction(UiAction.SignInClicked) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.email.isNotBlank() && uiState.password.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.sign_in)
                    )
                }

                Text(
                    text = stringResource(id = R.string.dont_have_account),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Surface(color = Color.Unspecified, modifier = Modifier.imePadding()) {
                    Text(
                        text = stringResource(id = R.string.sign_up),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { uiAction(UiAction.SignUpClicked) }
                    )
                }

                Text(
                    text = stringResource(id = R.string.thanks),
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Preview
@Composable
private fun SignInScreenPreview() {
    SignInScreen(
        uiState = UiState(),
        uiEffect = emptyFlow(),
        uiAction = {},
        saveUserIdToShared = {},
        navigateToHome = {},
        navigateToSignUp = {}
    )
}