package com.example.whatdidyoudo.ui.main

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Task(
    @PrimaryKey val id: Int,
    val text: String,
    val timestamp: Date,
    val isProductive: Boolean
)
