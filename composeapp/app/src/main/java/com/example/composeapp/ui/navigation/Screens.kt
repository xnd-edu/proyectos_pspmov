package com.example.composeapp.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object Reindeers

@Serializable
object Profile

@Serializable
data class Detail(val name: String)
