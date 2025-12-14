package com.example.navigation.domain.usecases.igdb

import com.example.navigation.data.IGDBRepository
import javax.inject.Inject

class SearchGamesUsecase @Inject constructor(
    private val igdbRepository: IGDBRepository
) {
    suspend operator fun invoke(query: String) = igdbRepository.searchGames(query)
}