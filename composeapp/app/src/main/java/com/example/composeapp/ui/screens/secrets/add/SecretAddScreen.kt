package com.example.composeapp.ui.screens.secrets.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.composeapp.R
import com.example.composeapp.ui.common.Constants
import com.example.composeapp.ui.common.DeviceConfiguration
import com.example.composeapp.ui.common.UiEvent
import com.example.composeapp.ui.theme.AppTheme
import com.example.composeapp.ui.theme.Dimens

@Composable
fun SecretAddScreenVM(
    modifier: Modifier = Modifier,
    viewModel: SecretAddViewModel = hiltViewModel(),
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

    SecretAddScreen(
        modifier = modifier,
        state = state,
        onIntent = viewModel::handleIntent
    )
}

@Composable
fun SecretAddScreen(
    modifier: Modifier = Modifier,
    state: SecretAddState,
    onIntent: (SecretIntent) -> Unit
) {
    val deviceConfig = DeviceConfiguration.fromWindowAdaptiveInfo()

    when (deviceConfig) {
        DeviceConfiguration.MOBILE_LANDSCAPE,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> {
            SecretAddScreenLandscape(
                modifier = modifier,
                state = state,
                onIntent = onIntent
            )
        }
        else -> {
            SecretAddScreenPortrait(
                modifier = modifier,
                state = state,
                onIntent = onIntent
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SecretAddScreenPortrait(
    modifier: Modifier = Modifier,
    state: SecretAddState,
    onIntent: (SecretIntent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.paddingLarge)
    ) {
            // Título
            Text(
                text = stringResource(R.string.guardar_secreto),
                style = MaterialTheme.typography.headlineLargeEmphasized,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(Dimens.spacingLarge))

            // Secreto
            CampoSecreto(
                value = state.secretText,
                onValueChange = { onIntent(SecretIntent.ChangeSecretText(it)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Dimens.spacingMedium))

            // Password del Usuario
            CampoPassword(
                value = state.password,
                onValueChange = { onIntent(SecretIntent.ChangePassword(it)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(Dimens.spacingExtraLarge))

            // Botón Añadir
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                BotonAnadir(onClick = { onIntent(SecretIntent.SaveSecret) })
            }
        }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SecretAddScreenLandscape(
    modifier: Modifier = Modifier,
    state: SecretAddState,
    onIntent: (SecretIntent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.paddingLarge)
    ) {
            // Título centrado en toda la pantalla
            Text(
                text = stringResource(R.string.guardar_secreto),
                style = MaterialTheme.typography.headlineLargeEmphasized,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(Dimens.spacingLarge))

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.spacingLarge)
            ) {
                // Columna izquierda - Formulario
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(Dimens.spacingMedium)
                ) {
                    // Secreto
                    CampoSecreto(
                        value = state.secretText,
                        onValueChange = { onIntent(SecretIntent.ChangeSecretText(it)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Password del Usuario
                    CampoPassword(
                        value = state.password,
                        onValueChange = { onIntent(SecretIntent.ChangePassword(it)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Columna derecha - Botón de acción
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        // Botón Añadir
                        BotonAnadir(onClick = { onIntent(SecretIntent.SaveSecret) })
                    }
                }
            }
        }
}

// Campos de entrada reutilizables

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BotonAnadir(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(ButtonDefaults.MediumContainerHeight),
        contentPadding = ButtonDefaults.MediumContentPadding
    ) {
        Text(
            text = stringResource(R.string.guardar),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun CampoSecreto(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.secreto)) },
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.edit_24px),
                contentDescription = null
            )
        },
        minLines = Constants.MIN_LINES_SECRET_FIELD,
        maxLines = Constants.MAX_LINES_SECRET_FIELD,
        singleLine = false,
        modifier = modifier
    )
}

@Composable
fun CampoPassword(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.register_password)) },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.lock_24px),
                contentDescription = null
            )
        },
        modifier = modifier
    )
}

// Previews

@Preview(showBackground = true, showSystemUi = true, name = "Portrait")
@Composable
fun SecretAddScreenPreview() {
    AppTheme {
        SecretAddScreen(
            state = SecretAddState(
                secretText = Constants.PREVIEW_SECRET_TEXT,
                password = Constants.PREVIEW_SECRET_PASSWORD
            ),
            onIntent = {}
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
fun SecretAddScreenLandscapePreview() {
    AppTheme {
        SecretAddScreenLandscape(
            state = SecretAddState(
                secretText = Constants.PREVIEW_SECRET_TEXT,
                password = Constants.PREVIEW_SECRET_PASSWORD
            ),
            onIntent = {}
        )
    }
}

