package com.example.todoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.fragments.AddTaskBottomSheet
import com.example.todoapp.fragments.SettingsFragment
import com.example.todoapp.fragments.TasksListFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.fabAddTask.setOnClickListener {
            val addTaskBottomSheet = AddTaskBottomSheet()
            addTaskBottomSheet.show(supportFragmentManager, null)
        }
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.list -> {
                    pushFragment(TasksListFragment())
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