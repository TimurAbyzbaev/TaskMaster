package ru.abyzbaev.taskmaster.ui.tasks

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.abyzbaev.taskmaster.data.model.TaskEntity
import ru.abyzbaev.taskmaster.databinding.TaskRecyclerviewItemBinding
import ru.abyzbaev.taskmaster.ui.ItemTouchHelperAdapter
import java.util.*
import kotlin.collections.ArrayList

class TaskRecyclerViewAdapter(private val clickListener: (TaskEntity) -> Unit) :
    RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskViewHolder>(), ItemTouchHelperAdapter {

    private var tasks: MutableList<TaskEntity> = arrayListOf()

    fun setData(tasks: MutableList<TaskEntity>) {
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

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(tasks, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo  toPosition + 1) {
                Collections.swap(tasks, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        tasks.remove(tasks[position])
        notifyItemRemoved(position)
    }
}