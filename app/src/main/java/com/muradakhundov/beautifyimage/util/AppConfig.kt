package com.muradakhundov.beautifyimage.util

import android.app.Application
import com.muradakhundov.beautifyimage.di.repositoryModule
import com.muradakhundov.beautifyimage.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("unused")
class AppConfig : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppConfig)
            modules(listOf(repositoryModule, viewModelModule))
        }
    }
}