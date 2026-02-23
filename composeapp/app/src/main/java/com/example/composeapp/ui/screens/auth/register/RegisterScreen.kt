package com.example.composeapp.ui.screens.auth.register

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
fun RegisterScreenVM(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel(),
    navigateToReindeers: () -> Unit,
    navigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.registerSuccess) {
        if (state.registerSuccess) navigateToReindeers()
    }

    RegisterScreen(
        modifier = modifier,
        state = state,
        onIntent = viewModel::handleIntent,
        navigateBack = navigateBack
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun RegisterScreen(
    state: RegisterState,
    onIntent: (RegisterIntent) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
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
                text = stringResource(R.string.register_title),
                style = MaterialTheme.typography.headlineLargeEmphasized
            )

            OutlinedTextField(
                value = state.nombre,
                onValueChange = { onIntent(RegisterIntent.OnNombreChange(it)) },
                label = { Text(stringResource(R.string.register_nombre)) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading,
                singleLine = true
            )

            OutlinedTextField(
                value = state.username,
                onValueChange = { onIntent(RegisterIntent.OnUsernameChange(it)) },
                label = { Text(stringResource(R.string.register_username)) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading,
                singleLine = true
            )

            OutlinedTextField(
                value = state.email,
                onValueChange = { onIntent(RegisterIntent.OnEmailChange(it)) },
                label = { Text(stringResource(R.string.register_email)) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading,
                singleLine = true
            )

            OutlinedTextField(
                value = state.password,
                onValueChange = { onIntent(RegisterIntent.OnPasswordChange(it)) },
                label = { Text(stringResource(R.string.register_password)) },
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
                onClick = { onIntent(RegisterIntent.OnRegisterClick) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularWavyProgressIndicator()
                } else {
                    Text(stringResource(R.string.register_button))
                }
            }

            OutlinedButton(
                onClick = navigateBack,
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                Text(stringResource(R.string.register_ya_tengo_cuenta))
            }
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    AppTheme {
        RegisterScreen(
            state = RegisterState(
                username = Constants.PREVIEW_USERNAME,
                email = Constants.PREVIEW_USER_EMAIL,
                password = Constants.PREVIEW_PASSWORD,
                nombre = Constants.PREVIEW_USER_NOMBRE,
                isLoading = false,
                error = null
            ),
            onIntent = {},
            navigateBack = {}
        )
    }
}
