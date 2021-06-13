package com.example.whatdidyoudo.ui.main

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.ViewModel
import com.example.whatdidyoudo.databases.Task

class MainViewModel : ViewModel() {

    val taskList = ObservableArrayList<Task>()
}