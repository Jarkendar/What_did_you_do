package com.example.whatdidyoudo.di

import com.example.whatdidyoudo.databases.Repository
import com.example.whatdidyoudo.ui.main.TaskFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single { Repository() }
}

val appModule = module {
    viewModel { TaskFragmentViewModel(get()) }
}