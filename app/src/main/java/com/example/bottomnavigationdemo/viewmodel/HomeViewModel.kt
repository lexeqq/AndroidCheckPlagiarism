package com.example.bottomnavigationdemo.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bottomnavigationdemo.Constants
import com.example.bottomnavigationdemo.model.retrofit.PlagiarismResponse
import com.example.bottomnavigationdemo.model.retrofit.RetrofitClient
import com.example.bottomnavigationdemo.model.room.HistoryItem
import com.example.bottomnavigationdemo.model.room.HistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val historyRepository: HistoryRepository) : ViewModel() {
    // Состояния UI:
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _result = MutableStateFlow<PlagiarismResponse?>(null)
    val result: StateFlow<PlagiarismResponse?> = _result

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _textInput = MutableStateFlow("")
    val textInput: StateFlow<String> = _textInput

    private val _selectedIndex = mutableIntStateOf(0)
    var selectedIndex by _selectedIndex

    fun updateTextInput(newText: String) {
        _textInput.value = newText
    }

    fun deleteText() {
        _textInput.value = ""
    }

    fun checkPlagiarism(text: String) {
        viewModelScope.launch {

            // Асинхронная операция
            _isLoading.value = true
            _result.value = null // Сбрасываем предыдущий результат
            _error.value = null

            val text = _textInput.value
            if (text.isBlank()) return@launch

            try {
                val response = RetrofitClient.instance.checkPlagiarism(
                    apiKey = Constants.API_KEY,
                    text = text
                )
                _result.value = response

                // Сохранение в БД
                historyRepository.insert(HistoryItem.fromResponse(text, response))
            } catch (e: Exception) {
                _error.value = e.message ?: "Неизвестная ошибка"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}