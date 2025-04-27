package com.eurico.patol.common.util

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.CoroutineScope

object ServerUtil {
    suspend fun isSiteOnline(url: String): Boolean {
        val client = HttpClient(OkHttp)
        return try {
            val response: HttpResponse = client.get(url)
            response.status.value in 200..299
        } catch (e: Exception) {
            false
        } finally {
            client.close()
        }
    }
}