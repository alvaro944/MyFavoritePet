package edu.alvarocervantes.myfavoritepet

import java.io.Serializable

data class Pet(
    val id: Int, // ID único para identificar la mascota
    var name: String,
    var scientificName: String,
    var furType: String,
    var category: String,
    var imageUri: String?, // Guardar la URI de la imagen (puede ser nula)
    var loveLevel: Int, // Nivel de amorosidad (1 a 5)
    var isFavorite: Boolean = false,
    var wikiLink: String
) : Serializable
