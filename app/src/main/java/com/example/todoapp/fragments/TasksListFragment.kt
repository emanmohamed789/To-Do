package com.example.todoapp.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.todoapp.EditTask
import com.example.todoapp.adapters.TaskAdapter
import com.example.todoapp.clearTime
import com.example.todoapp.database.TaskDatabase
import com.example.todoapp.database.models.Task
import com.example.todoapp.databinding.FragmentTasksBinding
import java.util.Calendar
import java.util.Date


class TasksListFragment : Fragment() {

    lateinit var binding: FragmentTasksBinding
    lateinit var adapter: TaskAdapter
    lateinit var calender: Calendar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = TaskAdapter(null)
        calender = Calendar.getInstance()
        binding.rvTasks.adapter = adapter

        val list = TaskDatabase.getInstance(requireContext()).getTaskDao().getAllTasks()
        adapter.updateData(list)

        binding.calendarView.setOnDateChangedListener { _, date, _ ->
            val year = date.year
            val month = date.month - 1
            val dayOfMonth = date.day
            calender.clearTime()

            calender.set(Calendar.YEAR, year)
            calender.set(Calendar.MONTH, month)
            calender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            getDataByDate(calender.time)
        }

        adapter.onDeleteItem = object : TaskAdapter.OnItemDeleteListener {
            override fun onDeleteClick(task: Task, position: Int) {
                val taskUpdate = Task(
                    id = task.id,
                    title = task.title,
                    description = task.description,
                    date = task.date,
                    isDone = task.isDone
                )
                TaskDatabase.getInstance(requireContext()).getTaskDao().deleteTask(taskUpdate)
                adapter.notifyDataSetChanged()
                if (binding.calendarView.selectedDate == null) getData()
                else getDataByDate(calender.time)
            }
        }

        val editTaskLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    if (binding.calendarView.selectedDate == null) getData()
                    else {
                        getDataByDate(calender.time)
                    }
                }
            }
        adapter.onEditClick = object : TaskAdapter.OnEditClickListener {
            override fun onEditItemClick(task: Task, position: Int) {
                val intent = Intent(requireContext(), EditTask::class.java)
                intent.putExtra("title", task.title)
                intent.putExtra("desc", task.description)
                intent.putExtra("date", task.date?.time)
                intent.putExtra("id", task.id)
                adapter.notifyDataSetChanged()
                editTaskLauncher.launch(intent)
            }
        }
    }

    fun getData() {
        val updatedList = TaskDatabase.getInstance(requireContext()).getTaskDao().getAllTasks()
        adapter.updateData(updatedList)
    }

    fun getDataByDate(time: Date) {
        val updatedList =
            TaskDatabase.getInstance(this.requireContext()).getTaskDao().getAllTasksByDate(time)
        adapter.updateData(updatedList)
    }
}