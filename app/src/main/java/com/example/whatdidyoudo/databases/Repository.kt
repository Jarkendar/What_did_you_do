package com.example.whatdidyoudo.databases

import com.example.whatdidyoudo.utils.getEndDateMillis
import com.example.whatdidyoudo.utils.getStartDateMillis
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.util.*

class Repository(private val ioDispatcher: CoroutineDispatcher): KoinComponent {

    private val appDatabase: AppDatabase = get()

    fun getTaskFlowFromDate(date: Date): Flow<List<Task>> {
        return flow {
            val taskList = getTaskFromDate(date).sortedByDescending { it.timestamp }
            emit(taskList)
        }.flowOn(ioDispatcher)
    }

    fun insertTask(task: Task) {
        appDatabase.taskDao().insertTask(task)
    }

    fun updateTask(task: Task) {
        appDatabase.taskDao().updateTask(task)
    }

    fun removeTask(task: Task) {
        appDatabase.taskDao().deleteTask(task)
    }

    fun getMinDateOfTask(): Date {
        return Date(appDatabase.taskDao().getMinimalDateOfTasks())
    }

    private fun getTaskFromDate(date: Date): List<Task> {
        val startDate = getStartDateMillis(date).time
        val endDate = getEndDateMillis(date).time
        return appDatabase.taskDao().getAllTaskInDay(startDate, endDate)
    }
}