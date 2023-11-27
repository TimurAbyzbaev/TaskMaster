package ru.abyzbaev.taskmaster.data.repository

import ru.abyzbaev.taskmaster.data.dao.TaskDao
import ru.abyzbaev.taskmaster.data.model.TaskEntity

class TaskRepository(private val taskDao: TaskDao) {

    suspend fun insertTask(task: TaskEntity) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: TaskEntity) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }

    suspend fun getAllTasks(): List<TaskEntity> {
        return taskDao.getAllTasks()
    }

    suspend fun getTasksByCategory(categoryId: Long): List<TaskEntity> {
        return taskDao.getTasksByCategory(categoryId)
    }
}