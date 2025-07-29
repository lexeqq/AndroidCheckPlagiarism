package com.example.bottomnavigationdemo.model.room

import kotlinx.coroutines.flow.Flow

class HistoryRepository(private val historyDao: HistoryDao) { // Получение данных с бд
    val allItems: Flow<List<HistoryItem>> = historyDao.getAll()

    suspend fun insert(item: HistoryItem) {
        historyDao.insert(item)
    }

    suspend fun clearHistory() {
        historyDao.clearAll()
    }
}