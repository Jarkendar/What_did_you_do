package com.example.whatdidyoudo.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whatdidyoudo.databases.Repository
import com.example.whatdidyoudo.databases.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class TaskFragmentViewModel(repository: Repository) : ViewModel() {

    val taskLiveData: MutableLiveData<ArrayList<Task>> = MutableLiveData()
    val taskList = ArrayList<Task>()

    val dupaString = MutableLiveData<String>("dupa")

    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())

    init {
        coroutineScope.launch {
            taskList.addAll(repository.getAllTasks())
        }
        viewModelScope.launch {
            taskLiveData.value = taskList
        }

        viewModelScope.launch {
            repository.getTaskFlow().collect { list ->
                taskLiveData.value = ArrayList(list)
            }
        }

        viewModelScope.launch {
            taskList.add(Task(5, "dupa", Date(), false))
            taskList.add(Task(1, "dupa", Date(), true))
            taskList.add(Task(2, "dupa", Date(), false))
            taskList.add(Task(3, "dupa", Date(), true))
            taskList.add(Task(4, "dupa", Date(), false))
            taskLiveData.value = taskList
        }
    }
}