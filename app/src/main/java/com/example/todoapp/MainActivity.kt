package com.example.todoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.fragments.AddTaskBottomSheet
import com.example.todoapp.fragments.SettingsFragment
import com.example.todoapp.fragments.TasksListFragment
import java.util.Date

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var taskListFragment: TasksListFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val editTask = EditTask()
        editTask.onTaskEditedListener = object : OnTaskEditedListener {
            override fun onTaskEdited(calendar: Date) {
                if (editTask.calendar.time == taskListFragment.calender.time)
                    taskListFragment.getDataByDate(calendar)
                else {
                    taskListFragment.binding.calendarView.selectedDate = null
                    taskListFragment.getData()
                }
            }
        }
        binding.fabAddTask.setOnClickListener {
            val addTaskBottomSheet = AddTaskBottomSheet()
            addTaskBottomSheet.onTaskAddedListener = object : OnTaskAddedListener {
                override fun onTaskAdded(calendar: Date) {
                    // Reload Fragment ->  TaskListFragment
                    if (addTaskBottomSheet.calendar.time == taskListFragment.calender.time)
                        taskListFragment.getDataByDate(calendar)
                    else {
                        taskListFragment.binding.calendarView.selectedDate = null
                        taskListFragment.getData()
                    }
                }
            }
            addTaskBottomSheet.show(supportFragmentManager, null)
        }
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.list -> {
                    taskListFragment = TasksListFragment()
                    pushFragment(taskListFragment)
                }

                R.id.settings -> {
                    pushFragment(SettingsFragment())
                }
            }
            return@setOnItemSelectedListener true
        }
        binding.bottomNavigation.selectedItemId = R.id.list
    }

    private fun pushFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.content.fragmentContainer.id, fragment)
            .commit()
    }
}