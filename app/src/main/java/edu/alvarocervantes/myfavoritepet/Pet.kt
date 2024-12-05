package edu.alvarocervantes.myfavoritepet

import java.io.Serializable

data class Pet(
    val name: String,
    val scientificName: String,
    val furType: String,
    val classification: String,
    val loveLevel: Int,
    val isFavorite: Boolean
) : Serializable
