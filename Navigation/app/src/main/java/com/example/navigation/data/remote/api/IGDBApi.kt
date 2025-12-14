package com.example.navigation.data.remote.api

import com.example.navigation.domain.model.Game
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface IGDBApi {

    /**
     * Obtiene una lista de juegos según la consulta proporcionada.
     * La API de IGDB usa un lenguaje de consulta específico (Apicalypse).
     *
     * Ejemplo de body:
     * "fields name, rating, cover.url, summary; limit 10; where rating > 80;"
     */
    @POST("games")
    suspend fun getGames(@Body query: RequestBody): Response<List<Game>>

    /**
     * Busca juegos por nombre.
     *
     * Ejemplo de body:
     * "search \"The Witcher\"; fields name, rating, cover.url; limit 5;"
     */
    @POST("games")
    suspend fun searchGames(@Body query: RequestBody): Response<List<Game>>
}