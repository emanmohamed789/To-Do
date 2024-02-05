package com.example.todoapp.database.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todoapp.database.models.Task
import java.util.Date

@Dao
interface TasksDao {
    @Insert
    fun insertTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Query("SELECT * FROM Task")
    fun getAllTasks(): List<Task>

    @Query("SELECT * FROM Task WHERE date = :date")
    fun getAllTasksByDate(date: Date): List<Task>

}