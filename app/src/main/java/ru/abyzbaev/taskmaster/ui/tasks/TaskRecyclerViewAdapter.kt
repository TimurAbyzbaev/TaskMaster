package ru.abyzbaev.taskmaster.ui.tasks

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.abyzbaev.taskmaster.R
import ru.abyzbaev.taskmaster.data.model.TaskEntity
import ru.abyzbaev.taskmaster.databinding.TaskRecyclerviewItemBinding
import ru.abyzbaev.taskmaster.ui.itemtouchhelper.ItemTouchHelperAdapter
import ru.abyzbaev.taskmaster.ui.itemtouchhelper.OnItemDismissListener
import ru.abyzbaev.taskmaster.utlis.changeVectorDrawableColor
import ru.abyzbaev.taskmaster.utlis.formatDateFromLong
import java.util.*

class TaskRecyclerViewAdapter(
    private val clickListener: (TaskEntity) -> Unit,
    private var itemDismissListener: OnItemDismissListener
) :
    RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskViewHolder>(), ItemTouchHelperAdapter {
    private lateinit var context: Context
    private var tasks: MutableList<TaskEntity> = arrayListOf()

    fun setData(tasks: MutableList<TaskEntity>) {
        this.tasks = arrayListOf()
        this.tasks = tasks
        notifyDataSetChanged()
        Log.d("####", "Adapter $this setData $tasks")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        context = parent.context
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
            binding.dueDateTextview.text = formatDateFromLong(task.dueDate)
            var currentTimeInMillis = System.currentTimeMillis()
            if (task.dueDate <= currentTimeInMillis) {
                binding.dueDateTextview.setTextColor(context.resources.getColor(R.color.color_error))
                changeVectorDrawableColor(binding.dueDateImg, R.color.color_error)
            } else if (task.dueDate > currentTimeInMillis
                && task.dueDate <= currentTimeInMillis + (24 * 60 * 60 * 1000)
            ) {
                binding.dueDateTextview.setTextColor(context.resources.getColor(R.color.color_warning))
                changeVectorDrawableColor(binding.dueDateImg, R.color.color_warning)
            } else {
                binding.dueDateTextview.setTextColor(context.resources.getColor(R.color.color_black))
                changeVectorDrawableColor(binding.dueDateImg, R.color.black)
            }
            Log.d("####", "task title = " + task.title)
        }
    }


    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(tasks, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(tasks, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        val task = tasks[position]
        itemDismissListener.onItemDismiss(task)
        tasks.remove(task)
        notifyItemRemoved(position)
    }
}