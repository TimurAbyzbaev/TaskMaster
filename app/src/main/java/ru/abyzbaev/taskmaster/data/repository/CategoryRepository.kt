package ru.abyzbaev.taskmaster.data.repository

import ru.abyzbaev.taskmaster.data.dao.CategoryDao
import ru.abyzbaev.taskmaster.data.model.CategoryEntity

class CategoryRepository(private val categoryDao: CategoryDao) {

    suspend fun insertCategory(category: CategoryEntity) {
        categoryDao.insertCategory(category)
    }

    suspend fun updateCategory(category: CategoryEntity) {
        categoryDao.updateCategory(category)
    }

    suspend fun deleteCategory(category: CategoryEntity) {
        categoryDao.deleteCategory(category)
    }

    suspend fun getAllCategories(): List<CategoryEntity> {
        return categoryDao.getAllCategories()
    }
}