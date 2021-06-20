package com.example.whatdidyoudo.di

import com.example.whatdidyoudo.databases.Repository
import com.example.whatdidyoudo.ui.main.TaskFragmentViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single { Repository(Dispatchers.IO) }
}

val appModule = module {
    viewModel { TaskFragmentViewModel(Dispatchers.IO, get()) }
}