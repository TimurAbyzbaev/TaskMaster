package ru.abyzbaev.taskmaster.ui.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.abyzbaev.taskmaster.R
import ru.abyzbaev.taskmaster.data.model.CategoryEntity

class CategoryAdapter(private var categories: List<CategoryEntity>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

        class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(category: CategoryEntity) {
                val categoryTextView = itemView.findViewById<TextView>(R.id.category_name)
                categoryTextView.text = category.name
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.category_recyclerview_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    fun submitList(categories: List<CategoryEntity>) {
        this.categories = categories
        notifyDataSetChanged()
    }
}