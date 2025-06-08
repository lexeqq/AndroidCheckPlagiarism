package com.example.bottomnavigationdemo.mybd


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bottomnavigationdemo.mybd.HistoryRepository
import com.example.bottomnavigationdemo.mybd.HistoryViewModel
import com.example.bottomnavigationdemo.pages.HomeViewModel

class ViewModelFactory(
    private val historyRepository: HistoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(historyRepository) as T
            }
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(historyRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}