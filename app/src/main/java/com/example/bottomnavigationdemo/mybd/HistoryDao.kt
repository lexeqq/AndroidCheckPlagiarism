package com.example.bottomnavigationdemo.mybd

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert
    suspend fun insert(item: HistoryItem) // Добавление записи в локальную бд

    @Query("SELECT * FROM history ORDER BY timestamp DESC")
    fun getAll(): Flow<List<HistoryItem>> // Получение данных о всех элементах бд

    @Query("DELETE FROM history")
    suspend fun clearAll() // Очистка истории
}