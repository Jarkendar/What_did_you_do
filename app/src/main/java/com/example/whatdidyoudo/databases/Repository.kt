package com.example.whatdidyoudo.databases

import android.icu.util.Calendar
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.util.*

class Repository(private val ioDispatcher: CoroutineDispatcher): KoinComponent {

    companion object {
        private const val TASK_LIST_REFRESH_TIME = 1000L        // 1s
    }

    private val appDatabase: AppDatabase = get()

    fun getTaskFlowFromDate(date: Date): Flow<List<Task>> {
        return flow {
            while (true) {
                val taskList = getTaskFromDate(date).sortedByDescending { it.timestamp }
                emit(taskList)
                kotlinx.coroutines.delay(TASK_LIST_REFRESH_TIME)
            }
        }.flowOn(ioDispatcher)
    }

    fun insertTask(task: Task) {
        appDatabase.taskDao().insertTask(task)
    }

    fun updateTask(task: Task) {
        appDatabase.taskDao().updateTask(task)
    }

    fun getMinDateOfTask(): Date {
        return Date(appDatabase.taskDao().getMinimalDateOfTasks())
    }

    private fun getTaskFromDate(date: Date): List<Task> {
        val startDate = getStartDateMillis(date)
        val endDate = getEndDateMillis(date)
        return appDatabase.taskDao().getAllTaskInDay(startDate, endDate)
    }


    private fun getStartDateMillis(date: Date) = Calendar.getInstance().apply {
        time = date
        set(Calendar.MILLISECONDS_IN_DAY, 0)
    }.timeInMillis

    private fun getEndDateMillis(date: Date) = Calendar.getInstance().apply {
        time = date
        set(Calendar.MILLISECONDS_IN_DAY, 24 * 60 * 60 * 1000 - 1)
    }.timeInMillis
}