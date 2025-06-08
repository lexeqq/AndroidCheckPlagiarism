package com.example.bottomnavigationdemo.mybd


import android.app.Application
import com.example.bottomnavigationdemo.mybd.AppDatabase
import com.example.bottomnavigationdemo.mybd.HistoryRepository

class MyApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val historyRepository by lazy { HistoryRepository(database.historyDao()) }
}