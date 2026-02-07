package com.example.composeapp.ui.screens.coches

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.composeapp.R
import com.example.composeapp.domain.modelo.Coche
import com.example.composeapp.ui.common.Constants.TIPO_OTRO
import com.example.composeapp.ui.common.Constants.TIPO_SEDAN
import com.example.composeapp.ui.common.Constants.TIPO_SUV
import com.example.composeapp.ui.common.DeviceConfiguration
import com.example.composeapp.ui.common.UiEvent
import com.example.composeapp.ui.theme.AppTheme
import com.example.composeapp.ui.theme.Dimens

@Composable
fun CarFormScreenVM(
    viewModel: CocheViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val snackbarHostState = remember { SnackbarHostState() }

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
                }
            }
        }
    }

    CarFormScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onNavegarAnterior = { viewModel.handleIntent(CocheIntent.IrCocheAnterior) },
        onNavegarSiguiente = { viewModel.handleIntent(CocheIntent.IrCocheSiguiente) },
        onCocheChange = { viewModel.handleIntent(CocheIntent.ChangeCoche(it)) },
        onGuardar = { viewModel.handleIntent(CocheIntent.SaveCoche) },
        onActualizar = { viewModel.handleIntent(CocheIntent.UpdateCoche) },
        onEliminar = { viewModel.handleIntent(CocheIntent.DeleteCoche) },
        onLimpiar = { viewModel.handleIntent(CocheIntent.LimpiarFormulario) }
    )
}

@Composable
fun CarFormScreen(
    state: CocheState,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onNavegarAnterior: () -> Unit = {},
    onNavegarSiguiente: () -> Unit = {},
    onCocheChange: (Coche) -> Unit = {},
    onGuardar: () -> Unit = {},
    onActualizar: () -> Unit = {},
    onEliminar: () -> Unit = {},
    onLimpiar: () -> Unit = {}
) {
    val deviceConfig = DeviceConfiguration.fromWindowAdaptiveInfo()

    when (deviceConfig) {
        DeviceConfiguration.MOBILE_LANDSCAPE,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> {
            CarFormScreenLandscape(
                state = state,
                snackbarHostState = snackbarHostState,
                onNavegarAnterior = onNavegarAnterior,
                onNavegarSiguiente = onNavegarSiguiente,
                onCocheChange = onCocheChange,
                onGuardar = onGuardar,
                onActualizar = onActualizar,
                onEliminar = onEliminar,
                onLimpiar = onLimpiar
            )
        }
        else -> {
            CarFormScreenPortrait(
                state = state,
                snackbarHostState = snackbarHostState,
                onNavegarAnterior = onNavegarAnterior,
                onNavegarSiguiente = onNavegarSiguiente,
                onCocheChange = onCocheChange,
                onGuardar = onGuardar,
                onActualizar = onActualizar,
                onEliminar = onEliminar,
                onLimpiar = onLimpiar
            )
        }
    }
}

@Composable
fun CarFormScreenPortrait(
    state: CocheState,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onNavegarAnterior: () -> Unit = {},
    onNavegarSiguiente: () -> Unit = {},
    onCocheChange: (Coche) -> Unit = {},
    onGuardar: () -> Unit = {},
    onActualizar: () -> Unit = {},
    onEliminar: () -> Unit = {},
    onLimpiar: () -> Unit = {}
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(Dimens.paddingLarge)
        ) {
            // Título
            Text(
                text = stringResource(R.string.anyadirCoche),
                fontSize = Dimens.fontSizeTitle,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(Dimens.spacingLarge))

            // Matrícula y Checkbox Eléctrico
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.spacingSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CampoMatricula(
                    value = state.coche.matricula ?: "",
                    onValueChange = { onCocheChange(state.coche.copy(matricula = it)) },
                    modifier = Modifier.weight(1f)
                )

                CheckboxElectrico(
                    checked = state.coche.electrico ?: false,
                    onCheckedChange = { onCocheChange(state.coche.copy(electrico = it)) },
                    modifier = Modifier.padding(top = Dimens.paddingMedium)
                )
            }

            Spacer(modifier = Modifier.height(Dimens.spacingSmall))

            // Marca y Modelo
            CamposMarcaModelo(
                marca = state.coche.marca ?: "",
                modelo = state.coche.modelo ?: "",
                onMarcaChange = { onCocheChange(state.coche.copy(marca = it)) },
                onModeloChange = { onCocheChange(state.coche.copy(modelo = it)) }
            )

            Spacer(modifier = Modifier.height(Dimens.spacingSmall))

            // Fecha Matrícula y Color
            CamposFechaColor(
                fecha = state.coche.fechaMatriculacion ?: "",
                color = state.coche.color ?: "",
                onFechaChange = { onCocheChange(state.coche.copy(fechaMatriculacion = it)) },
                onColorChange = { onCocheChange(state.coche.copy(color = it)) }
            )

            Spacer(modifier = Modifier.height(Dimens.spacingSmall))

            // Tipo de Coche
            SelectorTipoCoche(
                tipoSeleccionado = state.coche.tipo,
                onTipoChange = { onCocheChange(state.coche.copy(tipo = it)) }
            )

            Spacer(modifier = Modifier.height(Dimens.spacingMedium))

            // Comentarios
            CampoComentarios(
                value = state.coche.comentarios ?: "",
                onValueChange = { onCocheChange(state.coche.copy(comentarios = it)) },
                maxLines = 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.textFieldMultilineHeight)
            )

            Spacer(modifier = Modifier.height(Dimens.spacingSmall))

            // Navegación entre coches
            NavigacionCoches(
                indiceCoche = state.indiceCoche,
                totalCoches = state.sizeList,
                onAnterior = onNavegarAnterior,
                onSiguiente = onNavegarSiguiente
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(Dimens.spacingExtraLarge))

            // Botones de Acción
            BotonesAccion(
                onLimpiar = onLimpiar,
                onActualizar = onActualizar,
                onEliminar = onEliminar,
                onGuardar = onGuardar
            )
        }
    }
}

@Composable
fun CampoMatricula(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.matricula)) },
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
fun CamposMarcaModelo(
    marca: String,
    modelo: String,
    onMarcaChange: (String) -> Unit,
    onModeloChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.spacingSmall)
    ) {
        OutlinedTextField(
            value = marca,
            onValueChange = onMarcaChange,
            label = { Text(stringResource(R.string.marca)) },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.edit_24px),
                    contentDescription = null
                )
            },
            modifier = Modifier.weight(1f)
        )

        OutlinedTextField(
            value = modelo,
            onValueChange = onModeloChange,
            label = { Text(stringResource(R.string.modelo)) },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.edit_24px),
                    contentDescription = null
                )
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun CamposFechaColor(
    fecha: String,
    color: String,
    onFechaChange: (String) -> Unit,
    onColorChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.spacingSmall)
    ) {
        OutlinedTextField(
            value = fecha,
            onValueChange = onFechaChange,
            label = { Text(stringResource(R.string.fechaMatricula)) },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.event_24px),
                    contentDescription = null
                )
            },
            modifier = Modifier
                .weight(1f)
                .clickable { }
        )

        OutlinedTextField(
            value = color,
            onValueChange = onColorChange,
            label = { Text(stringResource(R.string.color)) },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.edit_24px),
                    contentDescription = null
                )
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun SelectorTipoCoche(
    tipoSeleccionado: String?,
    onTipoChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.spacingSmall)
    ) {
        Text(
            text = stringResource(R.string.tipoCoche),
            modifier = Modifier.padding(end = Dimens.paddingMedium)
        )

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = tipoSeleccionado == TIPO_SEDAN,
                    onClick = { onTipoChange(TIPO_SEDAN) }
                )
                Text(stringResource(R.string.sedan))
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = tipoSeleccionado == TIPO_SUV,
                    onClick = { onTipoChange(TIPO_SUV) }
                )
                Text(stringResource(R.string.suv))
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = tipoSeleccionado == TIPO_OTRO,
                    onClick = { onTipoChange(TIPO_OTRO) }
                )
                Text(stringResource(R.string.otro))
            }
        }
    }
}

@Composable
fun CheckboxElectrico(
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
            text = stringResource(R.string.electrico)
        )
    }
}

@Composable
fun CampoComentarios(
    value: String,
    onValueChange: (String) -> Unit,
    maxLines: Int = 3,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.comentarios)) },
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.comment_24px),
                contentDescription = null
            )
        },
        modifier = modifier,
        maxLines = maxLines
    )
}

@Composable
fun NavigacionCoches(
    modifier: Modifier = Modifier,
    indiceCoche: Int,
    totalCoches: Int,
    onAnterior: () -> Unit = {},
    onSiguiente: () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilledIconButton(
            onClick = onAnterior,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                painter = painterResource(R.drawable.arrow_back_24px),
                contentDescription = stringResource(R.string.cd_coche_anterior)
            )
        }

        Text(
            text = "${indiceCoche + 1}/$totalCoches",
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.CenterHorizontally)
        )

        FilledIconButton(
            onClick = onSiguiente,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                painter = painterResource(R.drawable.arrow_forward_24px),
                contentDescription = stringResource(R.string.cd_siguiente_coche)
            )
        }
    }
}

@Composable
fun BotonesAccion(
    onLimpiar: () -> Unit,
    onActualizar: () -> Unit,
    onEliminar: () -> Unit,
    onGuardar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.paddingSmall)
    ) {
        // Botón Limpiar
        Button(
            onClick = onLimpiar,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.mop_24px),
                contentDescription = stringResource(R.string.cd_limpiar)
            )
        }

        // Botón Actualizar
        Button(
            onClick = onActualizar,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.update_24px),
                contentDescription = stringResource(R.string.cd_actualizar)
            )
        }

        // Botón Eliminar
        Button(
            onClick = onEliminar,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.delete_24px),
                contentDescription = stringResource(R.string.cd_eliminar)
            )
        }

        // Botón Guardar
        Button(
            onClick = onGuardar,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                painter = painterResource(R.drawable.save_24px),
                contentDescription = stringResource(R.string.cd_guardar)
            )
        }
    }
}

@Composable
fun BotonesAccionLandscape(
    onLimpiar: () -> Unit,
    onActualizar: () -> Unit,
    onEliminar: () -> Unit,
    onGuardar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens.paddingSmall)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimens.paddingSmall)
        ) {
            // Botón Limpiar
            Button(
                onClick = onLimpiar,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.mop_24px),
                    contentDescription = stringResource(R.string.cd_limpiar)
                )
            }

            // Botón Actualizar
            Button(
                onClick = onActualizar,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.update_24px),
                    contentDescription = stringResource(R.string.cd_actualizar)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimens.paddingSmall)
        ) {
            // Botón Eliminar
            Button(
                onClick = onEliminar,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.delete_24px),
                    contentDescription = stringResource(R.string.cd_eliminar)
                )
            }

            // Botón Guardar
            Button(
                onClick = onGuardar,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    painter = painterResource(R.drawable.save_24px),
                    contentDescription = stringResource(R.string.cd_guardar)
                )
            }
        }
    }
}

@Composable
fun CarFormScreenLandscape(
    state: CocheState,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onNavegarAnterior: () -> Unit = {},
    onNavegarSiguiente: () -> Unit = {},
    onCocheChange: (Coche) -> Unit = {},
    onGuardar: () -> Unit = {},
    onActualizar: () -> Unit = {},
    onEliminar: () -> Unit = {},
    onLimpiar: () -> Unit = {}
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Dimens.paddingLarge)
        ) {
            // Título centrado en toda la pantalla
            Text(
                text = stringResource(R.string.anyadirCoche),
                fontSize = Dimens.fontSizeTitle,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(Dimens.spacingMedium))

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.spacingLarge)
            ) {
                // Columna izquierda - Formulario
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Matrícula
                    CampoMatricula(
                        value = state.coche.matricula ?: "",
                        onValueChange = { onCocheChange(state.coche.copy(matricula = it)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(Dimens.spacingSmall))

                    // Marca y Modelo
                    CamposMarcaModelo(
                        marca = state.coche.marca ?: "",
                        modelo = state.coche.modelo ?: "",
                        onMarcaChange = { onCocheChange(state.coche.copy(marca = it)) },
                        onModeloChange = { onCocheChange(state.coche.copy(modelo = it)) }
                    )

                    Spacer(modifier = Modifier.height(Dimens.spacingSmall))

                    // Fecha Matrícula y Color
                    CamposFechaColor(
                        fecha = state.coche.fechaMatriculacion ?: "",
                        color = state.coche.color ?: "",
                        onFechaChange = { onCocheChange(state.coche.copy(fechaMatriculacion = it)) },
                        onColorChange = { onCocheChange(state.coche.copy(color = it)) }
                    )

                    Spacer(modifier = Modifier.height(Dimens.spacingSmall))

                    // Tipo de Coche
                    SelectorTipoCoche(
                        tipoSeleccionado = state.coche.tipo,
                        onTipoChange = { onCocheChange(state.coche.copy(tipo = it)) }
                    )

                    Spacer(modifier = Modifier.height(Dimens.spacingSmall))

                    // Checkbox Eléctrico
                    CheckboxElectrico(
                        checked = state.coche.electrico ?: false,
                        onCheckedChange = { onCocheChange(state.coche.copy(electrico = it)) },
                        modifier = Modifier.padding(vertical = Dimens.paddingSmall)
                    )
                }

                // Columna derecha - Comentarios, navegación y acciones
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(Dimens.spacingMedium)
                ) {
                    // Comentarios
                    CampoComentarios(
                        value = state.coche.comentarios ?: "",
                        onValueChange = { onCocheChange(state.coche.copy(comentarios = it)) },
                        maxLines = 5,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dimens.textFieldMultilineHeight)
                    )

                    Spacer(modifier = Modifier.height(Dimens.spacingMedium))

                    // Navegación entre coches
                    NavigacionCoches(
                        indiceCoche = state.indiceCoche,
                        totalCoches = state.sizeList,
                        onAnterior = onNavegarAnterior,
                        onSiguiente = onNavegarSiguiente
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Botones de Acción - Distribución en dos filas
                    BotonesAccionLandscape(
                        onLimpiar = onLimpiar,
                        onActualizar = onActualizar,
                        onEliminar = onEliminar,
                        onGuardar = onGuardar
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Portrait")
@Composable
fun CarFormScreenPreview() {
    AppTheme {
        CarFormScreen(
            state = CocheState(
                indiceCoche = 1,
                sizeList = 5
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
fun CarFormScreenLandscapePreview() {
    AppTheme {
        CarFormScreenLandscape(
            state = CocheState(
                coche = Coche(
                    matricula = "1234BCD",
                    marca = "Toyota",
                    modelo = "Corolla",
                    fechaMatriculacion = "2024-01-15",
                    color = "Azul",
                    electrico = false,
                    tipo = TIPO_SEDAN,
                    comentarios = "Coche en excelente estado, revisión reciente."
                ),
                indiceCoche = 2,
                sizeList = 10
            )
        )
    }
}

