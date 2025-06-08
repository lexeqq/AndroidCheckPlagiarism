package com.example.bottomnavigationdemo.mybd

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bottomnavigationdemo.apifiles.Match
import com.example.bottomnavigationdemo.apifiles.PlagiarismResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "history")
data class HistoryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String, // Проверенный текст
    val percent: Double, // % уникальности
    val matchesJson: String, // JSON-строка для списка совпадений
    val timestamp: Long = System.currentTimeMillis()
) {
    // Преобразует JSON обратно в список Match
    fun getMatches(): List<Match> {
        val type = object : TypeToken<List<Match>>() {}.type
        return Gson().fromJson(matchesJson, type) ?: emptyList()
    }

    companion object { // Создает HistoryItem из PlagiarismResponse
        fun fromResponse(text: String, response: PlagiarismResponse): HistoryItem {
            val matchesJson = Gson().toJson(response.matches)
            return HistoryItem(
                text = text,
                percent = response.percent,
                matchesJson = matchesJson
            )
        }
    }
}