package ru.abyzbaev.taskmaster.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.abyzbaev.taskmaster.app.TaskMasterApplication
import ru.abyzbaev.taskmaster.data.dao.CategoryDao
import ru.abyzbaev.taskmaster.data.dao.TaskDao
import ru.abyzbaev.taskmaster.data.database.AppDatabase
import ru.abyzbaev.taskmaster.data.repository.CategoryRepository
import ru.abyzbaev.taskmaster.data.repository.TaskRepository
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideContext(application: TaskMasterApplication): Context {
        return application.applicationContext
    }

    @Provides
    fun provideAppDatabase(context: Context) : AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideTaskDao(appDatabase: AppDatabase): TaskDao {
        return appDatabase.taskDao()
    }

    @Provides
    fun provideCategoryDao(appDatabase: AppDatabase): CategoryDao {
        return appDatabase.categoryDao()
    }

    @Provides
    fun provideTaskRepository(taskDao: TaskDao): TaskRepository {
        return TaskRepository(taskDao)
    }

    @Provides
    fun provideCategoryRepository(categoryDao: CategoryDao): CategoryRepository {
        return CategoryRepository(categoryDao)
    }
}