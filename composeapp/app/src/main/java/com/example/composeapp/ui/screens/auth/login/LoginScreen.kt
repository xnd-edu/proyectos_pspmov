package com.example.composeapp.ui.screens.auth.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.composeapp.R
import com.example.composeapp.ui.common.Constants
import com.example.composeapp.ui.theme.AppTheme
import com.example.composeapp.ui.theme.Dimens

@Composable
fun LoginScreenVM(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    navigateToReindeers: () -> Unit,
    navigateToRegister: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Observar el estado de loginSuccess para navegar
    LaunchedEffect(state.loginSuccess) {
        if (state.loginSuccess) navigateToReindeers()
    }

    LoginScreen(
        modifier = modifier,
        state = state,
        onIntent = viewModel::handleIntent,
        navigateToRegister = navigateToRegister
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoginScreen(
    state: LoginState,
    onIntent: (LoginIntent) -> Unit,
    navigateToRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.loginFormPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.loginFormSpacing)
        ) {
                Text(
                    text = stringResource(R.string.login_title),
                    style = MaterialTheme.typography.headlineLargeEmphasized
                )

                OutlinedTextField(
                    value = state.username,
                    onValueChange = { onIntent(LoginIntent.OnUsernameChange(it)) },
                    label = { Text(stringResource(R.string.login_username)) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading,
                    singleLine = true
                )

                OutlinedTextField(
                    value = state.password,
                    onValueChange = { onIntent(LoginIntent.OnPasswordChange(it)) },
                    label = { Text(stringResource(R.string.login_password)) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading,
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )

                if (state.error != null) {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Button(
                    onClick = { onIntent(LoginIntent.OnLoginClick) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularWavyProgressIndicator()
                    } else {
                        Text(stringResource(R.string.login_button))
                    }
                }

                OutlinedButton(
                    onClick = { navigateToRegister() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading
                ) {
                    Text(stringResource(R.string.login_registrarse_button))
                }
            }
        }
}

@Preview
@Composable
fun LoginScreenPreview() {
    AppTheme {
        LoginScreen(
            state = LoginState(
                username = Constants.PREVIEW_USERNAME,
                password = Constants.PREVIEW_PASSWORD,
                isLoading = false,
                error = null
            ),
            navigateToRegister = {},
            onIntent = {}
        )
    }
}
