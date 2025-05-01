package com.eurico.patol.di

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import com.eurico.patol.database.AppDatabase
import com.eurico.patol.repository.MaterialRepositoryImpl
import com.eurico.patol.viewmodel.LoadingViewModel
import com.eurico.patol.viewmodel.MaterialListViewModel
import com.eurico.patol.viewmodel.MaterialViewModel
import com.eurico.patol.viewmodel.SharedConfigurationViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::MaterialRepositoryImpl)
    viewModelOf(::LoadingViewModel)
    viewModelOf(::MaterialListViewModel)
    viewModelOf(::MaterialViewModel)
    viewModel{ (savedStateHandle: SavedStateHandle) ->
        SharedConfigurationViewModel(savedStateHandle, Application())
    }
}

val networkModule = module {
    single {
        HttpClient(Android) {
            install(Logging) {
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }
}

val dataBaseModule = module {
    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = AppDatabase::class.java,
            name = "patol"
        ).build()
    }
}