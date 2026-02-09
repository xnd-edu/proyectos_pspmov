package com.example.composeapp.ui.navigation.routes

import kotlinx.serialization.Serializable

@Serializable
object MainGraph

@Serializable
object Reindeers

@Serializable
object Games

@Serializable
object Profile

@Serializable
object AddReindeer

@Serializable
data class EditReindeer(val id: Int)

