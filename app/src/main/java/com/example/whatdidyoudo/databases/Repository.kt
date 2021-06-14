package com.example.whatdidyoudo.databases

import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class Repository: KoinComponent {

    private val appDatabase: AppDatabase = get()

    fun getAllTasks(): List<Task> {
            return appDatabase.taskDao().getAll()
    }
}