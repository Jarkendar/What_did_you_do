package com.example.whatdidyoudo.di

import com.example.whatdidyoudo.databases.Repository
import org.koin.dsl.module

val repositoryModule = module {
    single { Repository() }
}