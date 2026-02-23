package com.example.composeapp.ui.screens.reindeers.main

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.composeapp.R
import com.example.composeapp.domain.model.Reindeer
import com.example.composeapp.ui.common.Constants
import com.example.composeapp.ui.theme.AppTheme
import com.example.composeapp.ui.theme.Dimens

@Composable
fun ReindeerMainScreenVM(
    modifier: Modifier = Modifier,
    viewModel: ReindeerMainViewModel = hiltViewModel(),
    navigateToDetail: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    // Recargar la lista cada vez que volvemos a esta pantalla
    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.handleIntent(ReindeerMainIntent.LoadReindeers)
        }
    }

    ReindeerMainScreen(
        modifier = modifier,
        state = state,
        onIntent = viewModel::handleIntent,
        navigateToDetail = navigateToDetail
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ReindeerMainScreen(
    modifier: Modifier = Modifier,
    state: ReindeerMainState,
    onIntent: (ReindeerMainIntent) -> Unit = {},
    navigateToDetail: (String) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = stringResource(R.string.renos),
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
                        Button(onClick = { onIntent(ReindeerMainIntent.LoadReindeers) }) {
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
                        count = state.reindeers.size,
                        key = { index -> state.reindeers[index].id ?: index }
                    ) { index ->
                        ReindeerItem(
                            reindeer = state.reindeers[index],
                            onNavigateToDetail = navigateToDetail
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReindeerItem(
    reindeer: Reindeer,
    onNavigateToDetail: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.paddingLarge),
        shape = RoundedCornerShape(Dimens.cardRadius),
        onClick = { onNavigateToDetail(reindeer.id.toString()) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.cardPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Información del reno
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = reindeer.nombre ?: stringResource(R.string.sin_nombre),
                    fontSize = Dimens.itemFontSizeName,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(Dimens.itemInfoSpacing))

                Text(
                    text = stringResource(
                        R.string.color_label,
                        reindeer.color ?: stringResource(R.string.color_desconocido)
                    ),
                    fontSize = Dimens.itemFontSizeInfo,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = stringResource(
                        R.string.cuernos_label,
                        if (reindeer.cuernos == true)
                            stringResource(R.string.si)
                        else
                            stringResource(R.string.no)
                    ),
                    fontSize = Dimens.itemFontSizeInfo,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview()
@Composable
fun ReindeerMainScreenPreview() {
    AppTheme {
        ReindeerMainScreen(
            state = ReindeerMainState(
                isLoading = false,
                error = null,
                reindeers = listOf(
                    Reindeer(id = 1, nombre = Constants.PREVIEW_REINDEER_NAME_1, color = Constants.PREVIEW_REINDEER_COLOR_1, cuernos = true),
                    Reindeer(id = 2, nombre = Constants.PREVIEW_REINDEER_NAME_2, color = Constants.PREVIEW_REINDEER_COLOR_2, cuernos = true),
                    Reindeer(id = 3, nombre = Constants.PREVIEW_REINDEER_NAME_3, color = Constants.PREVIEW_REINDEER_COLOR_3, cuernos = false)
                ),
                isAdmin = true
            ),
            onIntent = { }
        )
    }
}