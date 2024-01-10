package ru.abyzbaev.taskmaster.ui.itemtouchhelper

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int)

    fun onItemDismiss(position: Int)
}