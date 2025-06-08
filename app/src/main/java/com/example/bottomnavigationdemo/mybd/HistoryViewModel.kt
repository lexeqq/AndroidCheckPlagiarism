package com.example.bottomnavigationdemo.mybd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bottomnavigationdemo.mybd.HistoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: HistoryRepository) : ViewModel() {
    val historyItems = repository.allItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }
}