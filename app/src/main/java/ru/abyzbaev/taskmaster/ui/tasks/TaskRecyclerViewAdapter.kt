package ru.abyzbaev.taskmaster.ui.tasks

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.abyzbaev.taskmaster.data.model.TaskEntity
import ru.abyzbaev.taskmaster.databinding.TaskRecyclerviewItemBinding

class TaskRecyclerViewAdapter(private val clickListener: (TaskEntity) -> Unit) :
    RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskViewHolder>() {

    private var tasks: List<TaskEntity> = arrayListOf()

    fun setData(tasks: List<TaskEntity>) {
        this.tasks = arrayListOf()
        this.tasks = tasks
        notifyDataSetChanged()
        Log.d("####", "Adapter $this setData $tasks")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TaskRecyclerviewItemBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position], clickListener)
    }

    inner class TaskViewHolder(private val binding: TaskRecyclerviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: TaskEntity, clickListener: (TaskEntity) -> Unit) {
            binding.root.setOnClickListener { clickListener(task) }
            binding.taskName.text = task.title
            Log.d("####", "task title = " + task.title)
        }
    }
}