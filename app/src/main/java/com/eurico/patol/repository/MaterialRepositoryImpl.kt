package com.eurico.patol.repository

import com.eurico.patol.R
import com.eurico.patol.common.constanst.UrlConstants
import com.eurico.patol.common.util.ImageUtil
import com.eurico.patol.common.util.ServerUtil
import com.eurico.patol.database.AppDatabase
import com.eurico.patol.model.ConsultResult
import com.eurico.patol.model.Material
import com.eurico.patol.model.database.ContentDTO
import com.eurico.patol.model.database.MaterialDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MaterialRepositoryImpl(
    private val httpClient: HttpClient,
    private val appDatabase: AppDatabase
): MaterialRepository {

    override suspend fun checkMaterial(): Flow<ConsultResult<Boolean>> = flow {
        emit(ConsultResult.Loading)

        try {
            val materialDao = appDatabase.materialDao()
            val materialIds = materialDao.getAllMaterialIds()

            when {
                ServerUtil.isSiteOnline(UrlConstants.PATOL_URL) -> {
                    if (materialIds.isNotEmpty()) dropDatabase()
                    val result = getAllMaterialsFromApi()

                    if (result is ConsultResult.Success) {
                        emit(ConsultResult.Success(true))
                    }
                }
                !ServerUtil.isSiteOnline(UrlConstants.PATOL_URL) && materialIds.isNotEmpty() ->
                    emit(ConsultResult.Success(true))
                else -> emit(ConsultResult.Error(Exception(R.string.connection_error.toString())))
            }
        } catch (e: Exception) {
            emit(ConsultResult.Error(Exception(R.string.download_error.toString())))
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

    override suspend fun getAllContentByMaterialId(materialId: Long): Flow<ConsultResult<List<ContentDTO>>> = flow {
        emit(ConsultResult.Loading)
        val materialDao = appDatabase.materialDao()

        try {
            val contentList = materialDao.getAllContentByMaterialId(materialId)

            contentList.forEach {
                it.img = ImageUtil.converterBase64ToImg(it.image)
            }

            emit(ConsultResult.Success(contentList))
        } catch (e: Exception) {
            emit(ConsultResult.Error(e))
        }
    }

    private suspend fun getAllMaterialsFromApi(): ConsultResult<List<Material>> {
        return try {
            val response = httpClient.get("${UrlConstants.PATOL_URL}materialApi/getAll")
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

    private fun saveInDadaBase(material: Material) {
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

    private fun dropDatabase() {
        val materialDao = appDatabase.materialDao()

        try {
            materialDao.deleteAllContent()
            materialDao.deleteAllMaterial()
        } catch (e: Exception) {
            throw e
        }
    }
}