package ru.abyzbaev.taskmaster.ui.tasks

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.abyzbaev.taskmaster.data.model.TaskEntity
import ru.abyzbaev.taskmaster.data.repository.TaskRepository
import kotlin.random.Random

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {
    private val tasks : MutableList<TaskEntity> = mutableListOf()


    private val _liveData = MutableLiveData<MutableList<TaskEntity>>()
    private val liveData: LiveData<MutableList<TaskEntity>> = _liveData

    fun updateTaskInDB(task: TaskEntity) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }
    private fun updateData() {
        _liveData.value = mutableListOf()

    }

    fun subscribeToLiveData() : LiveData<MutableList<TaskEntity>> {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                getAllTasks()
            }
        }
        _liveData.postValue(tasks)
        return liveData
    }

    suspend fun getAllTasks() {
        val repositoryTask = repository.getAllTasks()
        if(repositoryTask.isNotEmpty()) {
            tasks.clear()
            for(task in repositoryTask){
                //Проверка на то что эта задача уже есть в списке
                if (tasks.find { it.id == task.id } == null){
                    tasks.add(task)
                }
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