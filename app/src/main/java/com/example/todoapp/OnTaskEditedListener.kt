package com.example.todoapp

import java.util.Date

interface OnTaskEditedListener {
    fun onTaskEdited(calendar: Date) {}
}