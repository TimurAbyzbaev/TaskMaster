package ru.abyzbaev.taskmaster.di

import dagger.Module
import dagger.Provides
import ru.abyzbaev.taskmaster.data.dao.CategoryDao
import ru.abyzbaev.taskmaster.data.dao.TaskDao
import ru.abyzbaev.taskmaster.data.repository.CategoryRepository
import ru.abyzbaev.taskmaster.data.repository.TaskRepository
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideTaskRepository(taskDao: TaskDao): TaskRepository {
        return TaskRepository(taskDao)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(categoryDao: CategoryDao): CategoryRepository {
        return CategoryRepository(categoryDao)
    }
}