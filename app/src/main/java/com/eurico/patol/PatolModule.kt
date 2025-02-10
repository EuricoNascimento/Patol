package com.eurico.patol

import android.app.Application
import com.eurico.patol.di.appModule
import com.eurico.patol.di.dataBaseModule
import com.eurico.patol.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PatolApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PatolApplication)
            modules(
                appModule,
                networkModule,
                dataBaseModule
            )
        }
    }
}