package ru.abyzbaev.taskmaster.data.di

import android.app.Application
import dagger.Component
import ru.abyzbaev.taskmaster.MainActivity
import ru.abyzbaev.taskmaster.app.TaskMasterApplication
import ru.abyzbaev.taskmaster.di.AppModule
import ru.abyzbaev.taskmaster.di.ViewModelModule

@Component(modules = [AppModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(application: TaskMasterApplication)
}