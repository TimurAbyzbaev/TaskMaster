package ru.abyzbaev.taskmaster.data.dao

import androidx.room.*
import ru.abyzbaev.taskmaster.data.model.TaskEntity

@Dao
interface TaskDao {
    @Insert
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE id LIKE :taskId")
    suspend fun getTaskById(taskId: Long): TaskEntity?

    @Query("SELECT * FROM tasks WHERE categoryId LIKE :categoryId")
    suspend fun getTasksByCategory(categoryId: Long): List<TaskEntity>
}