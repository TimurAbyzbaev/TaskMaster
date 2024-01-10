package ru.abyzbaev.taskmaster.ui.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import ru.abyzbaev.taskmaster.data.model.CategoryEntity
import ru.abyzbaev.taskmaster.databinding.CategoryRecyclerviewItemBinding
import ru.abyzbaev.taskmaster.ui.emptycategorylistener.OnEmptyCategoryListener
import ru.abyzbaev.taskmaster.ui.tasks.TaskFragment

class CategoryAdapter(
    private val fragmentManager: FragmentManager,
    private val emptyCategoryListener: OnEmptyCategoryListener
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var categories: List<CategoryEntity> = arrayListOf()

    fun setData(categories: List<CategoryEntity>) {
        this.categories = arrayListOf()
        this.categories = categories
        if(categories.isEmpty()) {
            emptyCategoryListener.onEmptyCategoryList()
        }
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(
        private val binding: CategoryRecyclerviewItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: CategoryEntity) {
            binding.categoryName.text = category.name

            val container = FrameLayout(binding.root.context)
            val layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            container.layoutParams = layoutParams

            container.id = View.generateViewId()

            binding.root.addView(container)

            val taskFragment = TaskFragment.newInstance(category.id, category ,emptyCategoryListener)
            fragmentManager.beginTransaction()
                .replace(container.id, taskFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CategoryRecyclerviewItemBinding.inflate(inflater, parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}