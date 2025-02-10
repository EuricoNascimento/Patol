package com.eurico.patol.repository

import com.eurico.patol.model.ConsultResult
import com.eurico.patol.model.Material
import com.eurico.patol.model.database.MaterialDTO
import kotlinx.coroutines.flow.Flow

interface MaterialRepository {
    suspend fun getAllMaterialsFromApi(): ConsultResult<List<Material>>
    suspend fun getAllMaterialsFromDataBase(): Flow<ConsultResult<List<MaterialDTO>>>
    suspend fun getMaterialsWithoutIds(ids: List<Long>): ConsultResult<List<Material>>
    suspend fun saveInDadaBase(material: Material)
    suspend fun getAllMaterialIdsFromDatabase(): Flow<ConsultResult<List<Long>>>
}