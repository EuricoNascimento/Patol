package com.eurico.patol.model.database

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.eurico.patol.model.ContentType

@Entity(
    tableName = "content",
    foreignKeys = [
        ForeignKey(
            entity = MaterialDTO::class,
            parentColumns = ["id"],
            childColumns = ["material_id"]
        )
    ]
)
data class ContentDTO(
    @PrimaryKey
    val id: Long = 0,
    val order: Int = 0,
    val contentType: ContentType = ContentType.TEXT,
    val image: String = "",
    val text: String = "",
    @ColumnInfo(name = "material_id", index = true)
    val materialId: Long = 0
) {
    @Ignore
    var img: Bitmap? = null
}
