package com.example.todoapp

import java.util.Date

interface OnTaskAddedListener {
    fun onTaskAdded(calendar: Date)
}