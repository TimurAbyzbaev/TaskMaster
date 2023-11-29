package ru.abyzbaev.taskmaster.data.di

import android.app.Application
import dagger.Component
import ru.abyzbaev.taskmaster.MainActivity
import ru.abyzbaev.taskmaster.app.TaskMasterApplication
import ru.abyzbaev.taskmaster.di.AppModule
import ru.abyzbaev.taskmaster.di.ViewModelModule
import ru.abyzbaev.taskmaster.ui.categories.CategoryFragment
import ru.abyzbaev.taskmaster.ui.tasks.TaskFragment

@Component(modules = [AppModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(application: TaskMasterApplication)
    fun inject(taskFragment: TaskFragment)
    fun inject(taskFragment: CategoryFragment)
}