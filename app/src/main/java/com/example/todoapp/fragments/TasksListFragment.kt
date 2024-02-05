package com.example.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.todoapp.adapters.TaskAdapter
import com.example.todoapp.database.TaskDatabase
import com.example.todoapp.databinding.FragmentTasksBinding

class TasksListFragment : Fragment() {

    lateinit var binding: FragmentTasksBinding
    lateinit var adapter: TaskAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = TaskAdapter(null)
        binding.rvTasks.adapter = adapter
        val list = TaskDatabase.getInstance(requireContext()).getTaskDao().getAllTasks()
        adapter.updateData(list)
    }
}