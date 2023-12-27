package ru.abyzbaev.taskmaster.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.abyzbaev.taskmaster.data.repository.CategoryRepository
import ru.abyzbaev.taskmaster.data.repository.TaskRepository

class TaskViewModelFactory (private val taskRepository: TaskRepository, private val categoryRepository: CategoryRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            return TaskViewModel(taskRepository, categoryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}