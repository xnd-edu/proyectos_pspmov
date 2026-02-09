package com.example.composeapp.ui.screens.profile

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.composeapp.R
import com.example.composeapp.common.UserRoles
import com.example.composeapp.domain.model.UserDTO
import com.example.composeapp.ui.common.Constants
import com.example.composeapp.ui.theme.AppTheme
import com.example.composeapp.ui.theme.Dimens

@Composable
fun ProfileScreenVM(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToLogin: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val logoutSuccess by viewModel.logoutSuccess.collectAsStateWithLifecycle()

    // Observar logoutSuccess para navegar al login
    LaunchedEffect(logoutSuccess) {
        if (logoutSuccess) {
            navigateToLogin()
        }
    }

    ProfileScreen(
        modifier = modifier,
        state = state,
        onIntent = viewModel::handleIntent
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    state: ProfileState,
    onIntent: (ProfileIntent) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.paddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            // Título
            Text(
                text = stringResource(R.string.perfil),
                style = MaterialTheme.typography.headlineLargeEmphasized,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(Dimens.spacingLarge))

            // Información del usuario
            state.user?.let { user ->
                UserInfoCard(user = user)
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(Dimens.spacingLarge))

            // Botón de logout
            LogoutButton(
                onClick = { onIntent(ProfileIntent.Logout) },
                isLoading = state.isLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
}

@Composable
fun UserInfoCard(
    user: UserDTO,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.paddingSmall)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.paddingLarge),
            verticalArrangement = Arrangement.spacedBy(Dimens.spacingMedium)
        ) {
            Text(
                text = stringResource(R.string.informacion_personal),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(Dimens.spacingSmall))

            // Username
            InfoRow(
                label = stringResource(R.string.usuario),
                value = user.username
            )

            // Email
            user.email?.let { email ->
                InfoRow(
                    label = stringResource(R.string.email),
                    value = email
                )
            }

            // Nombre
            user.nombre?.let { nombre ->
                InfoRow(
                    label = stringResource(R.string.nombre),
                    value = nombre
                )
            }

            // Rol
            user.rol?.let { rol ->
                InfoRow(
                    label = stringResource(R.string.rol),
                    value = rol
                )
            }
        }
    }
}

@Composable
fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LogoutButton(
    onClick: () -> Unit,
    isLoading: Boolean,
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
        if (isLoading) {
            CircularWavyProgressIndicator()
        } else {
            Text(
                text = stringResource(R.string.cerrar_sesion),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

// Preview

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    AppTheme {
        ProfileScreen(
            state = ProfileState(
                user = UserDTO(
                    id = Constants.PREVIEW_USER_ID,
                    username = Constants.PREVIEW_USER_USERNAME,
                    email = Constants.PREVIEW_USER_EMAIL,
                    nombre = Constants.PREVIEW_USER_NOMBRE,
                    rol = UserRoles.ADMIN
                )
            )
        )
    }
}



