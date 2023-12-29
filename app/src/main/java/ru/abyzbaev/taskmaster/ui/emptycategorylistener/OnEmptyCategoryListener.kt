package ru.abyzbaev.taskmaster.ui.emptycategorylistener

import ru.abyzbaev.taskmaster.data.model.CategoryEntity

interface OnEmptyCategoryListener {
    fun onEmptyCategory(category: CategoryEntity)
    fun onEmptyCategoryList()
}