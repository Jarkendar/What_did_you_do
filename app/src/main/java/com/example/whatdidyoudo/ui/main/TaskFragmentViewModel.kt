package com.example.whatdidyoudo.ui.main

import android.icu.util.Calendar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.whatdidyoudo.databases.Repository
import com.example.whatdidyoudo.databases.Task
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class TaskFragmentViewModel(ioDispatcher: CoroutineDispatcher, private val repository: Repository) :
    ViewModel() {

    companion object {
        private val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    }

    val currentDateString: MutableLiveData<String> =
        MutableLiveData(simpleDateFormat.format(Date()))
    val nextDayButtonAlpha = MutableLiveData(1.0f)
    val nextDayButtonEnabled = MutableLiveData(true)
    val previousDayButtonAlpha = MutableLiveData(1.0f)
    val previousDayButtonEnabled = MutableLiveData(true)

    var minimalDate: Date = Date(0)

    private val coroutineScope = CoroutineScope(ioDispatcher + Job())
    private val selectedDate: MutableStateFlow<Date> = MutableStateFlow(Date())

    @OptIn(ExperimentalCoroutinesApi::class)
    val liveData: LiveData<List<Task>> = selectedDate.flatMapLatest { value ->
        repository.getTaskFlowFromDate(value)
    }.asLiveData(ioDispatcher)


    init {
        coroutineScope.launch {
            minimalDate = repository.getMinDateOfTask()
            withContext(viewModelScope.coroutineContext) {

            }
        }
        selectedDate.asLiveData().observeForever {
            if (it.after(getDayStart(System.currentTimeMillis()))) {
                nextDayButtonAlpha.postValue(0.3f)
                nextDayButtonEnabled.postValue(false)
            } else {
                nextDayButtonAlpha.postValue(1.0f)
                nextDayButtonEnabled.postValue(true)
            }

            if (it.before(getDayEnd(minimalDate.time))) {
                previousDayButtonAlpha.postValue(0.3f)
                previousDayButtonEnabled.postValue(false)
            } else {
                previousDayButtonAlpha.postValue(1.0f)
                previousDayButtonEnabled.postValue(true)
            }
        }
    }

    fun addTask(task: Task) {
        coroutineScope.launch {
            repository.insertTask(task)
        }
    }

    fun updateTask(task: Task) {
        coroutineScope.launch {
            repository.updateTask(task)
        }
    }

    fun changeDate(newDate: Date) {
        viewModelScope.launch {
            currentDateString.value = simpleDateFormat.format(newDate)
            selectedDate.value = newDate
        }
    }

    fun setNextDay() {
        if (selectedDate.value.before(getDayStart(System.currentTimeMillis()))) {
            changeDate(Calendar.getInstance().apply {
                time = selectedDate.value
                add(Calendar.DAY_OF_MONTH, 1)
            }.time)
        }
    }

    fun setPreviousDay() {
        if (selectedDate.value.after(getDayEnd(minimalDate.time))) {
            changeDate(Calendar.getInstance().apply {
                time = selectedDate.value
                add(Calendar.DAY_OF_MONTH, -1)
            }.time)
        }
    }

    fun getSelectedYear(): Int {
        return Calendar.getInstance().apply { time = selectedDate.value }.get(Calendar.YEAR)
    }

    fun getSelectedMonth(): Int {
        return Calendar.getInstance().apply { time = selectedDate.value }.get(Calendar.MONTH)
    }

    fun getSelectedDayOfMonth(): Int {
        return Calendar.getInstance().apply { time = selectedDate.value }.get(Calendar.DAY_OF_MONTH)
    }

    private fun getDayStart(timeMillis: Long) = Calendar.getInstance().apply {
        time = Date(timeMillis)
        set(Calendar.MILLISECONDS_IN_DAY, 0)
    }.time

    private fun getDayEnd(timeMillis: Long) = Calendar.getInstance().apply {
        time = Date(timeMillis)
        set(Calendar.MILLISECONDS_IN_DAY, 24 * 60 * 60 * 1000 - 1)
    }.time
}