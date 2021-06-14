package com.example.whatdidyoudo.ui.main

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.ViewModel
import com.example.whatdidyoudo.databases.Repository
import com.example.whatdidyoudo.databases.Task
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel(repository: Repository) : ViewModel() {

    val taskList = ObservableArrayList<Task>()

    init {
        GlobalScope.launch {
            taskList.addAll( repository.getAllTasks())
        }
    }
}