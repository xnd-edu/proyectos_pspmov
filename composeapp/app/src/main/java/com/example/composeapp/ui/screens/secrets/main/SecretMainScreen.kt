package com.example.composeapp.ui.screens.secrets.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.composeapp.R
import com.example.composeapp.domain.model.Secret
import com.example.composeapp.ui.common.Constants
import com.example.composeapp.ui.common.UiEvent
import com.example.composeapp.ui.common.toFormattedString
import com.example.composeapp.ui.theme.AppTheme
import com.example.composeapp.ui.theme.Dimens

@Composable
fun SecretMainScreenVM(
    modifier: Modifier = Modifier,
    viewModel: SecretMainViewModel = hiltViewModel(),
    navigateToDetail: (Long, String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    // Recargar la lista cada vez que volvemos a esta pantalla
    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.handleIntent(SecretMainIntent.LoadSecrets)
        }
    }

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when(event) {
                is UiEvent.NavigateToDetail -> {
                    navigateToDetail(event.id, event.password)
                }
                else -> {}
            }
        }
    }


    SecretMainScreen(
        modifier = modifier,
        state = state,
        onIntent = viewModel::handleIntent
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SecretMainScreen(
    modifier: Modifier = Modifier,
    state: SecretMainState,
    onIntent: (SecretMainIntent) -> Unit = {}
) {
    if (state.selectedSecretId != null) {
        PasswordDialog(
            password = state.passwordInput,
            onPasswordChange = { onIntent(SecretMainIntent.OnPasswordChange(it)) },
            onDismiss = { onIntent(SecretMainIntent.OnDismissDialog) },
            onConfirm = { onIntent(SecretMainIntent.OnConfirmPassword) }
        )
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = stringResource(R.string.secretos),
            style = MaterialTheme.typography.headlineLargeEmphasized,
            modifier = Modifier.padding(Dimens.paddingLarge)
        )

        Spacer(modifier = Modifier.height(Dimens.paddingLarge))

        // Content
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator()
                }
            }

            state.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.error,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(Dimens.paddingLarge)
                        )
                        Button(onClick = { onIntent(SecretMainIntent.LoadSecrets) }) {
                            Text(stringResource(R.string.reintentar))
                        }
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(Dimens.itemSpacing)
                ) {
                    items(
                        count = state.secrets.size,
                        key = { index -> state.secrets[index].id ?: index }
                    ) { index ->
                        SecretItem(
                            secret = state.secrets[index],
                            onItemClick = { id ->
                                onIntent(SecretMainIntent.OnSecretClick(id))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SecretItem(
    secret: Secret,
    onItemClick: (Long) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.paddingLarge),
        shape = RoundedCornerShape(Dimens.cardRadius),
        onClick = { secret.id?.let { onItemClick(it) } }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.cardPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Información del secreto
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Secreto ${secret.id}",
                    fontSize = Dimens.itemFontSizeName,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(Dimens.itemInfoSpacing))

                Text(
                    text = secret.createdAt.toFormattedString(),
                    fontSize = Dimens.itemFontSizeInfo,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun PasswordDialog(
    password: String,
    onPasswordChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.acceso_seguro)) },
        text = {
            Column {
                Text(stringResource(R.string.introduce_contrasena))
                Spacer(modifier = Modifier.height(Dimens.paddingMedium))
                OutlinedTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = { Text(stringResource(R.string.register_password)) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.ver_secreto))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancelar))
            }
        }
    )
}

@Preview()
@Composable
fun SecretMainScreenPreview() {
    AppTheme {
        SecretMainScreen(
            state = SecretMainState(
                isLoading = false,
                error = null,
                secrets = listOf(
                    Secret(
                        id = 1,
                        createdAt = Constants.PREVIEW_SECRET_CREATED_AT_1
                    ),
                    Secret(
                        id = 2,
                        createdAt = Constants.PREVIEW_SECRET_CREATED_AT_2
                    ),
                    Secret(
                        id = 3,
                        createdAt = Constants.PREVIEW_SECRET_CREATED_AT_3
                    )
                )
            ),
            onIntent = { }
        )
    }
}