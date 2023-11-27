package ru.abyzbaev.taskmaster.app

import android.app.Application
import ru.abyzbaev.taskmaster.data.di.AppComponent
import ru.abyzbaev.taskmaster.data.di.DaggerAppComponent
import ru.abyzbaev.taskmaster.di.AppModule
import ru.abyzbaev.taskmaster.di.ViewModelModule

class TaskMasterApplication: Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule())
            .viewModelModule(ViewModelModule())
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }
}