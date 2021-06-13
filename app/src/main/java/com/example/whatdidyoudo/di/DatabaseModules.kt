package com.example.whatdidyoudo.di

import androidx.room.Room
import com.example.whatdidyoudo.databases.AppDatabase
import org.koin.dsl.module

val appDatabase = module {
    single { Room.databaseBuilder(get(), AppDatabase::class.java, "appDatabase").build() }
}