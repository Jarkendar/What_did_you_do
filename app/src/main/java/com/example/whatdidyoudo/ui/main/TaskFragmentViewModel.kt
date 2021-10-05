package com.example.whatdidyoudo.ui.main

import android.icu.util.Calendar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.whatdidyoudo.databases.Repository
import com.example.whatdidyoudo.databases.Task
import com.example.whatdidyoudo.utils.getEndDateMillis
import com.example.whatdidyoudo.utils.getStartDateMillis
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TaskFragmentViewModel(ioDispatcher: CoroutineDispatcher, private val repository: Repository) :
    ViewModel() {

    data class TaskListState(val date: Date) {
        override fun equals(other: Any?): Boolean {
            return false
        }

        override fun hashCode(): Int {
            return date.hashCode()
        }
    }

    companion object {
        private const val DEFAULT_START_MILLIS = 0L
        private const val OPAQUE = 1.0f
        private const val DISABLED_TRANSPARENT = .3f
    }

    private val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    val currentDateString = MutableLiveData(simpleDateFormat.format(Date()))
    val nextDayButtonAlpha = MutableLiveData(OPAQUE)
    val nextDayButtonEnabled = MutableLiveData(true)
    val previousDayButtonAlpha = MutableLiveData(OPAQUE)
    val previousDayButtonEnabled = MutableLiveData(true)

    var minimalDate: Date = Date(DEFAULT_START_MILLIS)

    private val coroutineScope = CoroutineScope(ioDispatcher + Job())
    private val selectedDate: MutableStateFlow<TaskListState> =
        MutableStateFlow(TaskListState(Date()))

    @OptIn(ExperimentalCoroutinesApi::class)
    val liveTaskList: LiveData<List<Task>> = selectedDate.flatMapLatest { value ->
        repository.getTaskFlowFromDate(value.date)
    }.asLiveData(ioDispatcher)


    init {
        coroutineScope.launch {
            minimalDate = repository.getMinDateOfTask()
        }
        selectedDate.asLiveData().observeForever {
            if (it.date.after(getStartDateMillis(Date()))) {
                nextDayButtonAlpha.postValue(DISABLED_TRANSPARENT)
                nextDayButtonEnabled.postValue(false)
            } else {
                nextDayButtonAlpha.postValue(OPAQUE)
                nextDayButtonEnabled.postValue(true)
            }

            if (it.date.before(getEndDateMillis(minimalDate))) {
                previousDayButtonAlpha.postValue(DISABLED_TRANSPARENT)
                previousDayButtonEnabled.postValue(false)
            } else {
                previousDayButtonAlpha.postValue(OPAQUE)
                previousDayButtonEnabled.postValue(true)
            }
        }
    }

    fun addTask(task: Task) {
        coroutineScope.launch {
            repository.insertTask(task)
            requireRefreshList()
        }
    }

    fun updateTask(task: Task) {
        coroutineScope.launch {
            repository.updateTask(task)
            requireRefreshList()
        }
    }

    fun removeTask(task: Task) {
        coroutineScope.launch {
            repository.removeTask(task)
            requireRefreshList()
        }
    }

    fun changeDate(newDate: Date) {
        currentDateString.postValue(simpleDateFormat.format(newDate))
        selectedDate.value = TaskListState(newDate)
    }

    fun setNextDay() {
        if (selectedDate.value.date.before(getStartDateMillis(Date()))) {
            changeDate(Calendar.getInstance().apply {
                time = selectedDate.value.date
                add(Calendar.DAY_OF_MONTH, 1)
            }.time)
        }
    }

    fun setPreviousDay() {
        if (selectedDate.value.date.after(getEndDateMillis(minimalDate))) {
            changeDate(Calendar.getInstance().apply {
                time = selectedDate.value.date
                add(Calendar.DAY_OF_MONTH, -1)
            }.time)
        }
    }

    fun getSelectedYear(): Int {
        return Calendar.getInstance().apply { time = selectedDate.value.date }.get(Calendar.YEAR)
    }

    fun getSelectedMonth(): Int {
        return Calendar.getInstance().apply { time = selectedDate.value.date }.get(Calendar.MONTH)
    }

    fun getSelectedDayOfMonth(): Int {
        return Calendar.getInstance().apply { time = selectedDate.value.date }
            .get(Calendar.DAY_OF_MONTH)
    }

    private fun requireRefreshList() {
        selectedDate.value = TaskListState(selectedDate.value.date)
    }
}