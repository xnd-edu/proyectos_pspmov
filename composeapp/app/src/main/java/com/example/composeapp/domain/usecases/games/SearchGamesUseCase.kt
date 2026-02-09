package com.example.composeapp.domain.usecases.games

import com.example.composeapp.data.IGDBRepository
import javax.inject.Inject

class SearchGamesUseCase @Inject constructor(
    private val igdbRepository: IGDBRepository
) {
    suspend operator fun invoke(query: String) = igdbRepository.searchGames(query)
}