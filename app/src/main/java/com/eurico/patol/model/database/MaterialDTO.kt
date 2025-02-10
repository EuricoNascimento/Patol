package com.eurico.patol.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "material")
data class MaterialDTO(
    @PrimaryKey
    val id: Long = 0,
    val title: String = "",
    val subtitle: String = ""
)
