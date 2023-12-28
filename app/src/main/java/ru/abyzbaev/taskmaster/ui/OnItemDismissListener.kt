package ru.abyzbaev.taskmaster.ui

import ru.abyzbaev.taskmaster.data.model.TaskEntity

interface OnItemDismissListener {
    fun onItemDismiss(task: TaskEntity)
}