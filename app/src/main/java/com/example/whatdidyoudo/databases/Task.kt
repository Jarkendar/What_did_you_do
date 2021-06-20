package com.example.whatdidyoudo.databases

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Task(
    @PrimaryKey(autoGenerate = false) val timestamp: Date,
    val text: String,
    var isProductive: Boolean = false
)
