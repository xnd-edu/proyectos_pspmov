package com.example.composeapp.ui.screens.games

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import com.example.composeapp.R
import com.example.composeapp.common.ApiConstants
import com.example.composeapp.common.FormatConstants
import com.example.composeapp.domain.model.Game
import com.example.composeapp.ui.common.Constants
import com.example.composeapp.ui.common.UiEvent
import com.example.composeapp.ui.theme.AppTheme
import com.example.composeapp.ui.theme.Dimens
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun GamesMainScreenVM(
    modifier: Modifier = Modifier,
    viewModel: GamesMainViewModel = hiltViewModel()
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
                    else -> {}
                }
            }
        }
    }

    GamesMainScreen(
        modifier = modifier,
        state = state,
        onIntent = viewModel::handleIntent
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GamesMainScreen(
    modifier: Modifier = Modifier,
    state: GamesMainState,
    onIntent: (GamesMainIntent) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = stringResource(R.string.juegos),
            style = MaterialTheme.typography.headlineLargeEmphasized,
            modifier = Modifier.padding(Dimens.paddingLarge)
        )

        // TextField de búsqueda
        TextField(
            value = state.searchQuery,
            onValueChange = { onIntent(GamesMainIntent.OnSearchQueryChange(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.paddingLarge),
            placeholder = { Text(stringResource(R.string.buscar_juego)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (state.searchQuery.isNotBlank()) {
                        onIntent(GamesMainIntent.SearchGames(state.searchQuery))
                    }
                }
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(Dimens.spacingMedium))

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

            state.games.isEmpty() && state.searchQuery.isBlank() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.escribe_nombre_juego),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(Dimens.paddingLarge)
                    )
                }
            }

            state.games.isEmpty() && state.searchQuery.isNotBlank() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_juegos_encontrados),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(Dimens.paddingLarge)
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(Dimens.gameItemSpacing)
                ) {
                    items(
                        count = state.games.size,
                        key = { index -> state.games[index].id }
                    ) { index ->
                        GameItem(
                            game = state.games[index]
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GameItem(
    game: Game,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.paddingLarge, vertical = Dimens.paddingSmall),
        shape = RoundedCornerShape(Dimens.gameCardRadius)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Imagen de portada (cover)
            AsyncImage(
                model = getCoverUrl(game),
                contentDescription = stringResource(R.string.cd_portada_juego),
                modifier = Modifier
                    .width(Dimens.gameCoverWidth)
                    .height(Dimens.gameCoverHeight),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.person_24px),
                error = painterResource(R.drawable.person_24px)
            )

            Spacer(modifier = Modifier.width(Dimens.paddingLarge))

            // Columna con la información del juego
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = Dimens.paddingMedium, bottom = Dimens.paddingMedium, end = Dimens.paddingMedium)
            ) {
                // Nombre del juego
                Text(
                    text = game.name,
                    fontSize = Dimens.fontSizeLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(Dimens.paddingSmall))

                // Rating y fecha de lanzamiento
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rating badge
                    game.rating?.let { rating ->
                        Box(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(Dimens.gameRatingBadgeRadius)
                                )
                                .padding(
                                    horizontal = Dimens.gameRatingBadgePaddingHorizontal,
                                    vertical = Dimens.gameRatingBadgePaddingVertical
                                )
                        ) {
                            Text(
                                text = String.format(
                                    Locale.US,
                                    FormatConstants.RATING_DECIMAL_FORMAT,
                                    rating
                                ),
                                fontSize = Dimens.fontSizeSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(Dimens.paddingMedium))
                    }

                    // Fecha de lanzamiento
                    game.firstReleaseDate?.let { timestamp ->
                        Text(
                            text = formatReleaseDate(timestamp),
                            fontSize = Dimens.fontSizeSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Dimens.paddingMedium))

                // Resumen
                game.summary?.let { summary ->
                    Text(
                        text = summary,
                        fontSize = Dimens.fontSizeMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 4,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

// Función auxiliar para formatear la fecha
private fun formatReleaseDate(timestamp: Long): String {
    // Convertir timestamp Unix (segundos) a fecha
    val date = Date(timestamp * FormatConstants.TIMESTAMP_TO_MILLIS_MULTIPLIER)
    val format = SimpleDateFormat(FormatConstants.DATE_FORMAT_YYYY, Locale.getDefault())
    return format.format(date)
}

// Función auxiliar para obtener la URL de la imagen de portada
private fun getCoverUrl(game: Game): String? {
    return game.cover?.imageId?.let { imageId ->
        "${ApiConstants.IGDB_IMAGE_BASE_URL}${ApiConstants.IGDB_COVER_SIZE}/$imageId.${ApiConstants.IGDB_IMAGE_FORMAT}"
    }
}

@Preview
@Composable
fun GamesMainScreenPreview() {
    AppTheme {
        GamesMainScreen(
            state = GamesMainState(
                isLoading = false,
                games = listOf(
                    Game(id = 1, name = Constants.PREVIEW_GAME_NAME_1),
                    Game(id = 2, name = Constants.PREVIEW_GAME_NAME_2),
                    Game(id = 3, name = Constants.PREVIEW_GAME_NAME_3)
                ),
                searchQuery = Constants.PREVIEW_SEARCH_QUERY_ZELDA
            )
        )
    }
}

@Preview
@Composable
fun GamesMainScreenEmptyPreview() {
    AppTheme {
        GamesMainScreen(
            state = GamesMainState(
                isLoading = false,
                games = emptyList(),
                searchQuery = Constants.PREVIEW_SEARCH_QUERY_EMPTY
            )
        )
    }
}

