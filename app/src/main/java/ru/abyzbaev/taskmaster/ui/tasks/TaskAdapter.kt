package ru.abyzbaev.taskmaster.ui.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.abyzbaev.taskmaster.R
import ru.abyzbaev.taskmaster.data.model.TaskEntity

class TaskAdapter(private var tasks: List<TaskEntity>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

        class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(task: TaskEntity) {
                val taskTextView = itemView.findViewById<TextView>(R.id.task_name)
                taskTextView.text = task.title
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.task_recyclerview_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    fun submitList(tasks: List<TaskEntity>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }
}