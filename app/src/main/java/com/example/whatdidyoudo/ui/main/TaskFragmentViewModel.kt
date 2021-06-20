package com.example.whatdidyoudo.ui.main

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
import java.text.SimpleDateFormat
import java.util.*

class TaskFragmentViewModel(ioDispatcher: CoroutineDispatcher, private val repository: Repository) :
    ViewModel() {

    companion object {
        private val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    }

    val currentDateString: MutableLiveData<String> =
        MutableLiveData(simpleDateFormat.format(Date()))

    private val selectedDate: MutableStateFlow<Date> = MutableStateFlow(Date())

    @OptIn(ExperimentalCoroutinesApi::class)
    val liveData: LiveData<List<Task>> = selectedDate.flatMapLatest { value ->
        repository.getTaskFlowFromDate(value)
    }.asLiveData(ioDispatcher)

    private val coroutineScope = CoroutineScope(ioDispatcher + Job())

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

    fun getSelectedYear(): Int {
        return Calendar.getInstance().apply { time = selectedDate.value }.get(Calendar.YEAR)
    }

    fun getSelectedMonth(): Int {
        return Calendar.getInstance().apply { time = selectedDate.value }.get(Calendar.MONTH)
    }

    fun getSelectedDayOfMonth(): Int {
        return Calendar.getInstance().apply { time = selectedDate.value }.get(Calendar.DAY_OF_MONTH)
    }
}