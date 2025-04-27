package com.eurico.patol.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.eurico.patol.model.database.ContentDTO
import com.eurico.patol.model.database.MaterialDTO

@Dao
interface MaterialDao {
    @Insert
    fun save(material: MaterialDTO)

    @Insert
    fun save(content: ContentDTO)

    @Query("SELECT * FROM material")
    fun getAllMaterial(): List<MaterialDTO>

    @Query("SELECT * FROM content WHERE material_id = :id")
    fun getAllContentByMaterialId(id: Long): List<ContentDTO>

    @Query("SELECT id FROM material")
    fun getAllMaterialIds(): List<Long>

    @Query("DELETE FROM material")
    fun deleteAllMaterial()

    @Query("DELETE FROM content")
    fun deleteAllContent()
}