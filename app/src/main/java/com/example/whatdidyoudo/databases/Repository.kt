package com.example.whatdidyoudo.databases

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class Repository: KoinComponent {

    private val appDatabase: AppDatabase = get()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())

//    fun getAllTasks(list: LiveData<List<Task>>): List<Task> {
//        val job = coroutineScope.async {
//            list.value = appDatabase.taskDao().getAll()
//            return@async appDatabase.taskDao().getAll()
//        }
//        return job.await()
//    }
}