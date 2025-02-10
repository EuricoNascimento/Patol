package com.eurico.patol.repository

import com.eurico.patol.database.AppDatabase
import com.eurico.patol.model.ConsultResult
import com.eurico.patol.model.IdsRequest
import com.eurico.patol.model.Material
import com.eurico.patol.model.database.MaterialDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MaterialRepositoryImpl(
    private val httpClient: HttpClient,
    private val appDatabase: AppDatabase
): MaterialRepository {

    fun checkMaterial(): Flow<ConsultResult<Boolean>> = flow {
        emit(ConsultResult.Loading)
        try {
            val materialDao = appDatabase.materialDao()
            val materialId = materialDao.getAllMaterialId()

            if (materialId.isEmpty()) {
                val result = getAllMaterialsFromApi()

                if(result is ConsultResult.Success) {
                    emit(ConsultResult.Success(true))
                    return@flow
                }
                emit(ConsultResult.Success(false))
                return@flow
            }

            val result = getMaterialsWithoutIds(materialId)
            if (result is ConsultResult.Success ) {
                emit(ConsultResult.Success(true))
                return@flow
            }
            emit(ConsultResult.Success(false))
            return@flow
        } catch (e: Exception) {
            emit(ConsultResult.Error(e))
        }
    }

    override suspend fun getAllMaterialsFromApi(): ConsultResult<List<Material>> {
        return try {
            val response = httpClient.get("https://66f2-2804-d4b-7763-3000-d450-565d-24b4-e440.ngrok-free.app/materialApi/getAll")
            when (response.status.value) {
                200 -> {
                    val materialList = response.body<List<Material>>()
                    materialList.forEach{material -> saveInDadaBase(material)}
                    ConsultResult.Success(materialList)
                }

                else -> throw response.body()
            }
        } catch (e: Exception) {
            throw e
        }
    }
    override suspend fun getAllMaterialsFromDataBase(): Flow<ConsultResult<List<MaterialDTO>>> = flow {
        emit(ConsultResult.Loading)
        val materialDao = appDatabase.materialDao()

        try {
            val materialList = materialDao.getAllMaterial()
            emit(ConsultResult.Success(materialList))
        } catch (e: Exception) {
            emit(ConsultResult.Error(e))
        }
    }

    override suspend fun getMaterialsWithoutIds(ids: List<Long>): ConsultResult<List<Material>> {
        return try {
            val response = httpClient
                .post("https://66f2-2804-d4b-7763-3000-d450-565d-24b4-e440.ngrok-free.app/materialApi/getAllExclude") {
                    contentType(ContentType.Application.Json)
                    setBody(IdsRequest(ids))
                }

            when (response.status.value) {
                200 -> {
                    val materialList = response.body<List<Material>>()
                    if (materialList.isNotEmpty()) {
                        materialList.forEach { material -> saveInDadaBase(material) }
                    }
                    ConsultResult.Success(materialList)
                }

                else -> throw response.body()
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun saveInDadaBase(material: Material) {
        val materialDao = appDatabase.materialDao()

        try {
            materialDao.save(material.convertToDB())

            material.content.forEach {
                materialDao.save(it.convertToDB(material.id))
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getAllMaterialIdsFromDatabase(): Flow<ConsultResult<List<Long>>> {
        TODO("Not yet implemented")
    }
}