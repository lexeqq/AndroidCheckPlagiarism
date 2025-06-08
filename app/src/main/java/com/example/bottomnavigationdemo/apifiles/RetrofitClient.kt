package com.example.bottomnavigationdemo.apifiles

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://content-watch.ru/public/api/" // Базовая ссылка к API

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS) // Тайм-аут подключения
        .readTimeout(30, TimeUnit.SECONDS) // Тайм-аут чтения
        .build()

    val instance: ContentWatchApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()) // JSON конвертер
            .build()
            .create(ContentWatchApiService::class.java)
    }
}