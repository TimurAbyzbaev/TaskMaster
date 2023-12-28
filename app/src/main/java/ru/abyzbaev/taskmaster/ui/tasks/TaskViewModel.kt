package ru.abyzbaev.taskmaster.ui.tasks

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.abyzbaev.taskmaster.data.model.CategoryEntity
import ru.abyzbaev.taskmaster.data.model.TaskEntity
import ru.abyzbaev.taskmaster.data.repository.CategoryRepository
import ru.abyzbaev.taskmaster.data.repository.TaskRepository
import kotlin.random.Random

class TaskViewModel(
    private val taskRepository: TaskRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val tasks: MutableList<TaskEntity> = mutableListOf()
    private val categories: ArrayList<CategoryEntity> = arrayListOf()

    private val _liveData = MutableLiveData<MutableList<TaskEntity>>()
    private val liveData: LiveData<MutableList<TaskEntity>> = _liveData

    private val _categoriesLiveData = MutableLiveData<List<CategoryEntity>>()
    private val categoriesLiveData: LiveData<List<CategoryEntity>> = _categoriesLiveData


    private suspend fun getCategories() {
        val repositoryCategories = categoryRepository.getAllCategories()
        if (repositoryCategories.isNotEmpty()) {
            categories.clear()
            for (category in repositoryCategories) {
                categories.add(category)
            }
        }
        updateData()
    }

    fun updateTaskInDB(task: TaskEntity) {
        viewModelScope.launch {
            val tasksRepository = taskRepository.getAllTasks()
            if (tasksRepository.find { it.id == task.id } != null) {
                taskRepository.updateTask(task)
            } else {
                taskRepository.insertTask(task)
            }
        }
    }

    private fun updateData() {
        _categoriesLiveData.postValue(categories)
    }

    fun subscribeToCategoriesList(): LiveData<List<CategoryEntity>> {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                getCategories()
                _categoriesLiveData.postValue(categories)
            }
        }
        return categoriesLiveData
    }

    fun subscribeToLiveData(categoryId: Long?): LiveData<MutableList<TaskEntity>> {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                if (categoryId != null) {
                    getTaskByCategory(categoryId)
                    //getAllTasks()
                    _liveData.postValue(tasks)
                }
            }
        }
        //_liveData.postValue(tasks)
        Log.d("####", "_liveData TaskViewModel post $tasks")
        return liveData
    }

    suspend fun getTaskByCategory(categoryId: Long) {
        val repositoryTasks = taskRepository.getTasksByCategory(categoryId)
        if (repositoryTasks.isNotEmpty()) {
            tasks.clear()
            for (task in repositoryTasks) {
                tasks.add(task)
            }
        }
    }

    suspend fun getAllTasks() {
        val repositoryTask = taskRepository.getAllTasks()
        if (repositoryTask.isNotEmpty()) {
            tasks.clear()
            for (task in repositoryTask) {
                tasks.add(task)
            }
        } else {
            addTask("task1", 1L)
            addTask("task2", 1L)
            addTask("task3", 2L)
            addTask("task4", 2L)
        }
    }

    suspend fun getTask(id: Long): TaskEntity? {
        return taskRepository.getTaskById(id)
    }

    fun addTask(name: String = "New task", categoryId: Long) {
        val id = Random.nextLong()
        val dueDate = System.currentTimeMillis() + 86400000
        val newTask = TaskEntity(id, name, "Description", dueDate, categoryId)
        insertTaskInDB(newTask)
    }

    fun addCategory(categoryName: String) {
        val id = Random.nextLong()
        val newCategory = CategoryEntity(id, categoryName)
        insertCategoryInDB(newCategory)
        categories.add(newCategory)
        _categoriesLiveData.postValue(categories)
    }

    private fun insertCategoryInDB(category: CategoryEntity) {
        viewModelScope.launch {
            categoryRepository.insertCategory(category)
        }
    }

    private fun insertTaskInDB(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.insertTask(task)
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
        tasks.remove(task)
    }
}