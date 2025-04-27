package com.eurico.patol.repository

import com.eurico.patol.model.ConsultResult
import com.eurico.patol.model.Material
import com.eurico.patol.model.database.ContentDTO
import com.eurico.patol.model.database.MaterialDTO
import kotlinx.coroutines.flow.Flow

interface MaterialRepository {
    suspend fun getAllMaterialsFromDataBase(): Flow<ConsultResult<List<MaterialDTO>>>
    suspend fun getAllContentByMaterialId(materialId: Long): Flow<ConsultResult<List<ContentDTO>>>
    suspend fun checkMaterial(): Flow<ConsultResult<Boolean>>
}