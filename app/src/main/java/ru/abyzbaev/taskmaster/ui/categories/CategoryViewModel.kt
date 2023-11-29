package ru.abyzbaev.taskmaster.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.abyzbaev.taskmaster.data.model.CategoryEntity
import ru.abyzbaev.taskmaster.data.repository.CategoryRepository

class CategoryViewModel(private val repository: CategoryRepository) : ViewModel() {

    private val _categories = MutableLiveData<List<CategoryEntity>>()
    val categories: LiveData<List<CategoryEntity>> get() = _categories

}