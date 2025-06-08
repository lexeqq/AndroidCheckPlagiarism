package com.example.bottomnavigationdemo.apifiles

data class PlagiarismResponse(
    val text: String, // Проверенный текст
    val percent: Double, // % уникальности текста
    val highlight: List<List<Int>>, // Позиции овпадений (В ПРОЕКТЕ НЕ ИСПОЛЬУЕТСЯ)
    val matches: List<Match>, // Совпадения
    val error: String? = null // Ошибки сервера
)

data class Match( // Дата класс для хранения значений по совпадениям
    val url: String, // Ссылка на источник совпадений
    val percent: Double, // % совпадения
    val words: List<Int> // Слова совпадений (В ПРОЕКТЕ НЕ ИСПОЛЬЗУЕТСЯ)
)