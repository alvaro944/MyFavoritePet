package edu.alvarocervantes.myfavoritepet.model

import java.io.Serializable

data class Pet(
    var id: Int = 0,
    var name: String = "",
    var scientificName: String = "",
    var furType: String = "",
    var category: String = "",
    var imageUri: String? = null,
    var loveLevel: Int = 0,
    var isFavorite: Boolean = false,
    var wikiLink: String = ""
) : Serializable {
    constructor() : this(0, "", "", "", "", null, 0, false, "")
}
