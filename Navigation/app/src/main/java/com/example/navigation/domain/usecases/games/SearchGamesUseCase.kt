package com.example.navigation.domain.usecases.games

import com.example.navigation.data.IGDBRepository
import javax.inject.Inject

class SearchGamesUseCase @Inject constructor(
    private val igdbRepository: IGDBRepository
) {
    suspend operator fun invoke(query: String) = igdbRepository.searchGames(query)
}