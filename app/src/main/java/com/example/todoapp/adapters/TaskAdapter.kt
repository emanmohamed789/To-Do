package com.example.todoapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.todoapp.database.models.Task
import com.example.todoapp.databinding.ItemTaskBinding

class TaskAdapter(private var tasksList: List<Task>?) : Adapter<TaskAdapter.TasksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TasksViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return tasksList?.size ?: 0
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val item = tasksList?.get(position) ?: return
        holder.bind(item)
    }

    fun updateData(tasksList: List<Task>?) {
        this.tasksList = tasksList
        notifyDataSetChanged()
    }

    class TasksViewHolder(val binding: ItemTaskBinding) : ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.title.text = task.title
            binding.time.text = task.date.toString()
        }
    }

}