package com.example.bottomnavigationdemo.model.room


import android.app.Application

class MyApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val historyRepository by lazy { HistoryRepository(database.historyDao()) }
}