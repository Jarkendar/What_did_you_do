package com.example.whatdidyoudo.di

import androidx.room.Room
import com.example.whatdidyoudo.databases.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appDatabase = module {
    single { Room.databaseBuilder(androidApplication(), AppDatabase::class.java, "appDatabase").build() }
}