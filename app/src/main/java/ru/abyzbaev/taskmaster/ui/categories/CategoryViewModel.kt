package ru.abyzbaev.taskmaster.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.abyzbaev.taskmaster.data.model.CategoryEntity
import ru.abyzbaev.taskmaster.data.repository.CategoryRepository
import kotlin.random.Random

class CategoryViewModel(private val repository: CategoryRepository) : ViewModel() {
    private val categories: MutableList<CategoryEntity> = mutableListOf()

    private val _liveData = MutableLiveData<MutableList<CategoryEntity>>()
    private val liveData: LiveData<MutableList<CategoryEntity>> = _liveData

    fun subscribeToLiveData(): LiveData<MutableList<CategoryEntity>> {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                getAllCategoties()
            }
        }
        _liveData.postValue(categories)
        return liveData
    }

    suspend fun getAllCategoties() {
        val repositoryCategory = repository.getAllCategories()
        if (repositoryCategory.isNotEmpty()) {
            categories.clear()
            for (category in repositoryCategory) {
                if (categories.find { it.id == category.id } == null) {
                    categories.add(category)
                }
            }
        } else {
//            addCategory("Unknown", 0L)
//            addCategory("category 1", 1L)
//            addCategory("category 2", 2L)
        }
    }

    fun addCategory(name: String = "New Category", categoryId: Long = Random.nextLong()) {
        //val id = Random.nextLong()
        val newCategory = CategoryEntity(categoryId, name)
        insertCategoryInDB(newCategory)
    }

    private fun insertCategoryInDB(category: CategoryEntity) {
        viewModelScope.launch {
            repository.insertCategory(category)
        }
    }
}