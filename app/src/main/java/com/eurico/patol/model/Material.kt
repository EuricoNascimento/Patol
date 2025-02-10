package com.eurico.patol.model

import com.eurico.patol.model.database.MaterialDTO
import kotlinx.serialization.Serializable

@Serializable
data class Material(
    var id: Long = 0,
    var title: String = "",
    var subtitle: String = "",
    var content: List<Content> = mutableListOf()
) {
    fun convertToDB(): MaterialDTO = MaterialDTO(
        id = this.id,
        title = this.title,
        subtitle = this.subtitle
    )
}
