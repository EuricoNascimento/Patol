package com.eurico.patol.model

import com.eurico.patol.model.database.ContentDTO
import kotlinx.serialization.Serializable

@Serializable
data class Content(
    var id: Long = 0,
    var order: Int = 0,
    var contentType: ContentType = ContentType.TEXT,
    var image: String = "",
    var text: String = "",
    var materialId: Long = 0
) {
    fun convertToDB(mId: Long): ContentDTO = ContentDTO(
        id = this.id,
        order = this.order,
        contentType = this.contentType,
        image = this.image,
        text = this.text,
        materialId = mId
    )
}
