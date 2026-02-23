package com.example.composeapp.ui.screens.secrets.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.composeapp.R
import com.example.composeapp.domain.model.SecretDecrypted
import com.example.composeapp.ui.common.Constants
import com.example.composeapp.ui.common.DeviceConfiguration
import com.example.composeapp.ui.common.UiEvent
import com.example.composeapp.ui.common.toFormattedString
import com.example.composeapp.ui.theme.AppTheme
import com.example.composeapp.ui.theme.Dimens

@Composable
fun SecretEditScreenVM(
    modifier: Modifier = Modifier,
    viewModel: SecretViewViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navigateBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.events.collect { event ->
                when (event) {
                    is UiEvent.ShowSnackbar -> {
                        snackbarHostState.showSnackbar(
                            message = event.message,
                            duration = SnackbarDuration.Short
                        )
                    }

                    is UiEvent.NavigateBack -> {
                        navigateBack()
                    }

                    else -> {}
                }
            }
        }
    }

    state.secret?.let { _ ->
        SecretEditScreen(
            modifier = modifier,
            state = state,
            onIntent = viewModel::handleIntent
        )
    }
}

@Composable
fun SecretEditScreen(
    modifier: Modifier = Modifier,
    state: SecretViewState,
    onIntent: (SecretViewIntent) -> Unit = {}
) {
    if (state.isShareDialogVisible) {
        ShareSecretDialog(
            username = state.usernameInput,
            onUsernameChange = { onIntent(SecretViewIntent.OnUsernameChange(it)) },
            onDismiss = { onIntent(SecretViewIntent.OnDismissDialog) },
            onConfirm = { onIntent(SecretViewIntent.OnConfirmShare) }
        )
    }

    val deviceConfig = DeviceConfiguration.fromWindowAdaptiveInfo()

    when (deviceConfig) {
        DeviceConfiguration.MOBILE_LANDSCAPE,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> {
            SecretEditScreenLandscape(
                modifier = modifier,
                state = state,
                onEliminar = { onIntent(SecretViewIntent.DeleteSecret) },
                onShare = { onIntent(SecretViewIntent.ShareSecret) },
                onRevoke = { onIntent(SecretViewIntent.RevokeSecret) }
            )
        }

        else -> {
            SecretEditScreenPortrait(
                modifier = modifier,
                state = state,
                onEliminar = { onIntent(SecretViewIntent.DeleteSecret) },
                onShare = { onIntent(SecretViewIntent.ShareSecret) },
                onRevoke = { onIntent(SecretViewIntent.RevokeSecret) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SecretEditScreenPortrait(
    modifier: Modifier = Modifier,
    state: SecretViewState,
    onEliminar: () -> Unit = {},
    onShare: () -> Unit = {},
    onRevoke: () -> Unit = {}
) {
    val secret = state.secret ?: return

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.paddingLarge)
    ) {
        // Título
        Text(
            text = stringResource(R.string.secreto_title, secret.id),
            style = MaterialTheme.typography.headlineLargeEmphasized,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Dimens.spacingLarge))

        Text(
            text = state.secret.decryptedData,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Dimens.spacingMedium))

        Text(
            text = state.secret.createdAt.toFormattedString(),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(Dimens.spacingExtraLarge))

        // Botones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimens.spacingMedium)
        ) {
            BotonCompartir(
                onClick = onShare,
                modifier = Modifier.weight(1f)
            )

            BotonEliminar(
                onClick = onEliminar,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(Dimens.spacingSmall))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            BotonDejarCompartir(
                onClick = onRevoke,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SecretEditScreenLandscape(
    modifier: Modifier = Modifier,
    state: SecretViewState,
    onEliminar: () -> Unit = {},
    onShare: () -> Unit = {},
    onRevoke: () -> Unit = {}
) {
    val secret = state.secret ?: return

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.paddingLarge)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(Dimens.spacingLarge)
        ) {
            // Columna izquierda - Secreto
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(Dimens.spacingMedium)
            ) {
                // Título
                Text(
                    text = stringResource(R.string.secreto_title, secret.id),
                    style = MaterialTheme.typography.headlineLargeEmphasized,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(Dimens.spacingLarge))

                Text(
                    text = state.secret.decryptedData,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(Dimens.spacingMedium))

                Text(
                    text = state.secret.createdAt.toFormattedString(),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            // Columna derecha - Botones de acción
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Dimens.spacingMedium)
                ) {
                    BotonCompartir(
                        onClick = onShare,
                        modifier = Modifier.fillMaxWidth()
                    )
                    BotonEliminar(
                        onClick = onEliminar,
                        modifier = Modifier.fillMaxWidth()
                    )
                    BotonDejarCompartir(
                        onClick = onRevoke,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

}

// Campos de entrada reutilizables

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BotonCompartir(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(ButtonDefaults.MediumContainerHeight),
        contentPadding = ButtonDefaults.MediumContentPadding
    ) {
        Text(
            text = stringResource(R.string.compartir),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BotonEliminar(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(ButtonDefaults.MediumContainerHeight),
        contentPadding = ButtonDefaults.MediumContentPadding,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError
        )
    ) {
        Text(
            text = stringResource(R.string.eliminar),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BotonDejarCompartir(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(ButtonDefaults.MediumContainerHeight),
    ) {
        Text(
            text = stringResource(R.string.dejar_compartir),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun ShareSecretDialog(
    username: String,
    onUsernameChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.compartir_secreto)) },
        text = {
            Column {
                Text(
                    text = stringResource(R.string.introduce_usuario_compartir),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(Dimens.paddingMedium))
                OutlinedTextField(
                    value = username,
                    onValueChange = onUsernameChange,
                    label = { Text(stringResource(R.string.register_username)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.compartir))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancelar))
            }
        }
    )
}

// Previews

@Preview(showBackground = true, showSystemUi = true, name = "Portrait")
@Composable
fun SecretEditScreenPreview() {
    AppTheme {
        SecretEditScreen(
            state = SecretViewState(
                secret = SecretDecrypted(
                    id = 1,
                    decryptedData = Constants.PREVIEW_SECRET_TEXT,
                    createdAt = Constants.PREVIEW_SECRET_CREATED_AT_1
                )
            )
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Landscape",
    device = TABLET
)
@Composable
fun SecretEditScreenLandscapePreview() {
    AppTheme {
        SecretEditScreenLandscape(
            state = SecretViewState(
                secret = SecretDecrypted(
                    id = 1,
                    decryptedData = Constants.PREVIEW_SECRET_TEXT,
                    createdAt = Constants.PREVIEW_SECRET_CREATED_AT_1
                )
            )
        )
    }
}

