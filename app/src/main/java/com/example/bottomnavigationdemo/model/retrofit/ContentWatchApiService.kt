package com.example.bottomnavigationdemo.model.retrofit

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ContentWatchApiService {
    @FormUrlEncoded
    @POST("https://content-watch.ru/public/api/") // Базовая ссылка к API
    suspend fun checkPlagiarism( // Suspend-функция для корутин
        @Field("key") apiKey: String, // API ключ
        @Field("text") text: String, // Текст для проверки
        @Field("test") testMode: Int = 0 // Тестовый режим
    ): PlagiarismResponse // Ответ сервера
}