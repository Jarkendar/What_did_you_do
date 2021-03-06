package com.example.whatdidyoudo.databases

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDAO {

    @Query("SELECT * FROM task")
    fun getAll(): List<Task>

    @Query("SELECT * FROM task WHERE task.timestamp BETWEEN :startTime AND :endTime")
    fun getAllTaskInDay(startTime: Long, endTime: Long): List<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Query("SELECT MIN(task.timestamp) FROM task")
    fun getMinimalDateOfTasks(): Long
}