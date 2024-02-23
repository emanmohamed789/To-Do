package com.example.todoapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.todoapp.R
import com.example.todoapp.database.TaskDatabase
import com.example.todoapp.database.models.Task
import com.example.todoapp.databinding.ItemTaskBinding
import com.zerobranch.layout.SwipeLayout
import java.text.SimpleDateFormat
import java.util.Locale

class TaskAdapter(private var tasksList: List<Task>?) : Adapter<TaskAdapter.TasksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TasksViewHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {
        return tasksList?.size ?: 0
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val item = tasksList?.get(position) ?: return
        holder.bind(item)
        holder.binding.swipeLayout.setOnActionsListener(object : SwipeLayout.SwipeActionsListener {
            override fun onOpen(direction: Int, isContinuous: Boolean) {
                if (direction == SwipeLayout.RIGHT) {
                    holder.binding.swipeLayout.close(true)
                    onDeleteItem?.onDeleteClick(item, tasksList!!.indexOf(item))
                    notifyItemRemoved(tasksList?.indexOf(item)!!)
                }
                if (direction == SwipeLayout.LEFT) {
                    holder.binding.swipeLayout.close(true)
                    onEditClick?.onEditItemClick(item, tasksList!!.indexOf(item))
                    updateData(tasksList)
                }
            }

            override fun onClose() {
            }
        })
    }

    fun updateData(tasksList: List<Task>?) {
        this.tasksList = tasksList
        notifyDataSetChanged()
    }

    var onEditClick: OnEditClickListener? = null

    interface OnEditClickListener {
        fun onEditItemClick(task: Task, position: Int)
    }

    var onDeleteItem: OnItemDeleteListener? = null

    interface OnItemDeleteListener {
        fun onDeleteClick(task: Task, position: Int)
    }

    class TasksViewHolder(val binding: ItemTaskBinding, val context: Context) :
        ViewHolder(binding.root) {

        fun bind(task: Task) {
            if (task.isDone == true) {
                doneClick()
                body(task)
            } else if (task.isDone == false) {
                unDoneClick()
                body(task)
            }
        }

        private fun body(task: Task) {
            binding.title.text = task.title
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dateAsString = simpleDateFormat.format(task.date)
            binding.time.text = dateAsString
            binding.btnTaskIsDone.setOnClickListener {
                doneClick()
                var taskUpdate = Task(
                    id = task.id,
                    title = task.title,
                    description = task.description,
                    date = task.date,
                    isDone = true
                )
                TaskDatabase.getInstance(context).getTaskDao().updateTask(taskUpdate)
            }

        }

        private fun unDoneClick() {
            binding.title.setTextColor(ContextCompat.getColor(context, R.color.blue))
            binding.btnTaskIsDone.setImageResource(R.drawable.check_mark)
            binding.draggingBar.setImageResource(R.drawable.dragging_bar)
        }

        private fun doneClick() {
            binding.title.setTextColor(ContextCompat.getColor(context, R.color.green))
            binding.btnTaskIsDone.setImageResource(R.drawable.done)
            binding.draggingBar.setImageResource(R.drawable.dragging_bar_done)
        }
    }
}
