package com.example.todoapp

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.database.TaskDatabase
import com.example.todoapp.database.models.Task
import com.example.todoapp.databinding.ContentTaskDetailsBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditTask : AppCompatActivity() {

    private lateinit var binding: ContentTaskDetailsBinding
    private lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ContentTaskDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("desc")
        val date = intent.getLongExtra("date", 0)
        val id = intent.getIntExtra("id", 0)

        binding.title.editableText.append(title)
        binding.description.editableText.append(description)

        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateAsString = simpleDateFormat.format(date)
        binding.selectDateTv.text = dateAsString

        calendar = Calendar.getInstance()
        binding.selectTimeTv.setOnClickListener {
            selectTime()
        }
        binding.selectDateTv.setOnClickListener {
            selectDate()
        }
        binding.btnSave.setOnClickListener {

            Log.d("save", "")
            if (validateData()) {
                calendar.clearTime()
                val task = Task(
                    id = id,
                    title = binding.title.text.toString(),
                    description = binding.description.text.toString(),
                    date = calendar.time,
                    isDone = false
                )
                Log.d("save", "$task")

                TaskDatabase
                    .getInstance(this)
                    .getTaskDao()
                    .updateTask(task)

                // After updating the task
                val editedDate = task.date?.time ?: 0
                val resultIntent = Intent()
                resultIntent.putExtra("editedDate", editedDate)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    private fun validateData(): Boolean {

        if (binding.title.text?.isEmpty() == true || binding.title.text?.isBlank() == true) {
            binding.title.error = "Required"
            return false
        } else
            binding.title.error = null

        if (binding.description.text?.isEmpty() == true || binding.description.text?.isBlank() == true) {
            binding.description.error = "Required"
            return false
        } else
            binding.description.error = null
        if (binding.selectDateTv.text == getString(R.string.select_date)) {
            return false
        }
        if (binding.selectTimeTv.text == getString(R.string.select_time)) {
            return false
        }

        return true
    }

    private fun selectDate() {
        val picker = DatePickerDialog(
            this, { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                binding.selectDateTv.text = "$dayOfMonth / ${month + 1} / $year"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        picker.datePicker.minDate = System.currentTimeMillis()
        picker.show()
    }

    private fun selectTime() {
        val picker = TimePickerDialog(
            this, { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                binding.selectTimeTv.text = "$hourOfDay : $minute"
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )
        picker.show()
    }
}