package ru.abyzbaev.taskmaster.ui.itemtouchhelper

import ru.abyzbaev.taskmaster.data.model.TaskEntity

interface OnItemDismissListener {
    fun onItemDismiss(task: TaskEntity)
}