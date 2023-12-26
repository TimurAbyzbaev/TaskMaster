package ru.abyzbaev.taskmaster.ui.tasks

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.abyzbaev.taskmaster.data.di.AppComponent
import ru.abyzbaev.taskmaster.data.model.TaskEntity
import ru.abyzbaev.taskmaster.data.repository.TaskRepository
import ru.abyzbaev.taskmaster.di.AppModule
import kotlin.random.Random

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {
    private val tasks: MutableList<TaskEntity> = mutableListOf()


    private val _liveData = MutableLiveData<MutableList<TaskEntity>>()
    private val liveData: LiveData<MutableList<TaskEntity>> = _liveData

    fun updateTaskInDB(task: TaskEntity) {
        viewModelScope.launch {
            if(tasks.contains(task)){
                repository.updateTask(task)
            } else {
                repository.insertTask(task)
            }

        }
    }

    private fun updateData() {
        _liveData.value = mutableListOf()

    }

    fun subscribeToLiveData(categoryId: Long?): LiveData<MutableList<TaskEntity>> {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                if (categoryId != null) {
                    getTaskByCategory(categoryId)
                    _liveData.postValue(tasks)
                }
            }
        }
        //_liveData.postValue(tasks)
        Log.d("####", "_liveData TaskViewModel post $tasks")
        return liveData
    }

    suspend fun getTaskByCategory(categoryId: Long) {
        val repositoryTasks = repository.getTasksByCategory(categoryId)
        if (repositoryTasks.isNotEmpty()) {
            tasks.clear()
            for (task in repositoryTasks) {
                tasks.add(task)
            }
        }
        Log.d("####", "getTaskByCategory - ${tasks[0]} ${tasks[1]}")
    }

    suspend fun getAllTasks() {
        val repositoryTask = repository.getAllTasks()
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
        return repository.getTaskById(id)
    }

    fun addTask(name: String = "New task", categoryId: Long) {
        val id = Random.nextLong()
        val dueDate = System.currentTimeMillis() + 86400000
        val newTask = TaskEntity(id, name, "Description", dueDate, categoryId)
        insertTaskInDB(newTask)
    }

    private fun insertTaskInDB(task: TaskEntity) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
        tasks.remove(task)
    }
}