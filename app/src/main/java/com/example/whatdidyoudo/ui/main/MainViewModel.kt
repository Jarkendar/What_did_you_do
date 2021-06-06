package com.example.whatdidyoudo.ui.main

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    val taskList = ObservableArrayList<Task>()
}