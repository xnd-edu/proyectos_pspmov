package com.example.composeapp.ui.screens.reindeers.add

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
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.composeapp.R
import com.example.composeapp.domain.model.Reindeer
import com.example.composeapp.ui.common.Constants
import com.example.composeapp.ui.common.DeviceConfiguration
import com.example.composeapp.ui.common.UiEvent
import com.example.composeapp.ui.theme.AppTheme
import com.example.composeapp.ui.theme.Dimens

@Composable
fun ReindeerAddScreenVM(
    modifier: Modifier = Modifier,
    viewModel: ReindeerAddViewModel = hiltViewModel(),
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
                }
            }
        }
    }

    ReindeerAddScreen(
        modifier = modifier,
        state = state,
        onReindeerChange = { viewModel.handleIntent(ReindeerIntent.ChangeReindeer(it)) },
        onGuardar = { viewModel.handleIntent(ReindeerIntent.SaveReindeer) }
    )
}

@Composable
fun ReindeerAddScreen(
    modifier: Modifier = Modifier,
    state: ReindeerAddState,
    onReindeerChange: (Reindeer) -> Unit = {},
    onGuardar: () -> Unit = {}
) {
    val deviceConfig = DeviceConfiguration.fromWindowAdaptiveInfo()

    when (deviceConfig) {
        DeviceConfiguration.MOBILE_LANDSCAPE,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> {
            ReindeerAddScreenLandscape(
                modifier = modifier,
                state = state,
                onReindeerChange = onReindeerChange,
                onGuardar = onGuardar
            )
        }
        else -> {
            ReindeerAddScreenPortrait(
                modifier = modifier,
                state = state,
                onReindeerChange = onReindeerChange,
                onGuardar = onGuardar
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ReindeerAddScreenPortrait(
    modifier: Modifier = Modifier,
    state: ReindeerAddState,
    onReindeerChange: (Reindeer) -> Unit = {},
    onGuardar: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.paddingLarge)
    ) {
            // Título
            Text(
                text = stringResource(R.string.añadir_reno),
                style = MaterialTheme.typography.headlineLargeEmphasized,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(Dimens.spacingLarge))

            // Nombre
            CampoNombre(
                value = state.reindeer.nombre ?: "",
                onValueChange = { onReindeerChange(state.reindeer.copy(nombre = it)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Dimens.spacingMedium))

            // ID del Usuario
            CampoUserId(
                value = state.reindeer.userId?.toString() ?: "",
                onValueChange = {
                    val userId = it.toIntOrNull()
                    onReindeerChange(state.reindeer.copy(userId = userId))
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Dimens.spacingMedium))

            // Color
            CampoColor(
                value = state.reindeer.color ?: "",
                onValueChange = { onReindeerChange(state.reindeer.copy(color = it)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Dimens.spacingMedium))

            // Checkbox Cuernos
            CheckboxCuernos(
                checked = state.reindeer.cuernos ?: false,
                onCheckedChange = { onReindeerChange(state.reindeer.copy(cuernos = it)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(Dimens.spacingExtraLarge))

            // Botón Añadir
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                BotonAnadir(onClick = onGuardar)
            }
        }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ReindeerAddScreenLandscape(
    modifier: Modifier = Modifier,
    state: ReindeerAddState,
    onReindeerChange: (Reindeer) -> Unit = {},
    onGuardar: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.paddingLarge)
    ) {
            // Título centrado en toda la pantalla
            Text(
                text = stringResource(R.string.añadir_reno),
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
                    // Nombre
                    CampoNombre(
                        value = state.reindeer.nombre ?: "",
                        onValueChange = { onReindeerChange(state.reindeer.copy(nombre = it)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // ID del Usuario
                    CampoUserId(
                        value = state.reindeer.userId?.toString() ?: "",
                        onValueChange = {
                            val userId = it.toIntOrNull()
                            onReindeerChange(state.reindeer.copy(userId = userId))
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Color
                    CampoColor(
                        value = state.reindeer.color ?: "",
                        onValueChange = { onReindeerChange(state.reindeer.copy(color = it)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Checkbox Cuernos
                    CheckboxCuernos(
                        checked = state.reindeer.cuernos ?: false,
                        onCheckedChange = { onReindeerChange(state.reindeer.copy(cuernos = it)) },
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
                        BotonAnadir(onClick = onGuardar)
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
            text = stringResource(R.string.añadir),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun CampoNombre(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.nombre)) },
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.edit_24px),
                contentDescription = null
            )
        },
        modifier = modifier
    )
}

@Composable
fun CampoUserId(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.user_id)) },
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.person_24px),
                contentDescription = null
            )
        },
        modifier = modifier
    )
}

@Composable
fun CampoColor(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.color)) },
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.edit_24px),
                contentDescription = null
            )
        },
        modifier = modifier
    )
}

@Composable
fun CheckboxCuernos(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text(
            text = stringResource(R.string.cuernos)
        )
    }
}

// Previews

@Preview(showBackground = true, showSystemUi = true, name = "Portrait")
@Composable
fun ReindeerAddScreenPreview() {
    AppTheme {
        ReindeerAddScreen(
            state = ReindeerAddState()
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
fun ReindeerAddScreenLandscapePreview() {
    AppTheme {
        ReindeerAddScreenLandscape(
            state = ReindeerAddState(
                reindeer = Reindeer(
                    nombre = Constants.PREVIEW_REINDEER_NAME_1,
                    color = Constants.PREVIEW_REINDEER_COLOR_1,
                    cuernos = true
                )
            )
        )
    }
}

