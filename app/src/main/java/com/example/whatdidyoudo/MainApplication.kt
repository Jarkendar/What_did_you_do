package com.example.whatdidyoudo

import android.app.Application
import com.example.whatdidyoudo.di.repositoryModule
import com.example.whatdidyoudo.di.appDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            androidFileProperties()
            modules(repositoryModule, appDatabase)
        }
    }
}