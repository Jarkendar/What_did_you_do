package com.example.whatdidyoudo.databases

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class Repository(private val ioDispatcher: CoroutineDispatcher): KoinComponent {

    companion object {
        private const val TASK_LIST_REFRESH_TIME = 1000L        // 1s
    }

    private val appDatabase: AppDatabase = get()

    private val taskFlow: Flow<List<Task>> = flow {
        while (true) {
            val taskList = getAllTasks()
            Log.d("****dupa", "$taskList")
            emit(taskList)
            kotlinx.coroutines.delay(TASK_LIST_REFRESH_TIME)
        }
    }

    fun getTaskFlow(): Flow<List<Task>> {
        return taskFlow.flowOn(ioDispatcher)
    }

    fun insertTask(task: Task) {
        appDatabase.taskDao().insertTask(task)
    }

    fun getAllTasks(): List<Task> {
        return appDatabase.taskDao().getAll()
    }
}