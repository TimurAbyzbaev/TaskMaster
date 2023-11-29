package ru.abyzbaev.taskmaster.di

import dagger.Module
import dagger.Provides
import ru.abyzbaev.taskmaster.data.repository.CategoryRepository
import ru.abyzbaev.taskmaster.data.repository.TaskRepository
import ru.abyzbaev.taskmaster.ui.categories.CategoryViewModelFactory
import ru.abyzbaev.taskmaster.ui.tasks.TaskViewModelFactory
import javax.inject.Singleton

@Module
class ViewModelModule {
    @Provides
    fun provideTaskViewModelFactory(repository: TaskRepository): TaskViewModelFactory {
        return TaskViewModelFactory(repository)
    }

    @Provides
    fun provideCategoryViewModelFactory(repository: CategoryRepository): CategoryViewModelFactory {
        return CategoryViewModelFactory(repository)
    }
}