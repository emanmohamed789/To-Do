package com.example.todoapp.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Task(

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val title: String?,
    val description: String?,
    val date: Date?,
    var isDone: Boolean? = false

)
