package com.example.calendarapp.ui.taskListScreen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarapp.data.rest.response.TaskData
import com.example.calendarapp.databinding.TaskItemBinding

class TaskListAdapter() : RecyclerView.Adapter<TaskListAdapter.TaskListViewHolder>() {
    private var taskList = mutableListOf<TaskData>()
    var onItemClickListener: ((TaskData) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setTaskList(data : List<TaskData>) {
        this.taskList = data.toMutableList()
        notifyDataSetChanged()
    }

    class TaskListViewHolder(private val binding: TaskItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task : TaskData) {
            binding.taskDetail = task
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TaskItemBinding.inflate(inflater, parent, false)
        return TaskListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        val taskDetail = taskList[position]
        holder.bind(taskDetail)

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(taskDetail)
        }
    }
}