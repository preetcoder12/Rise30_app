package com.rise30.app

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object ApiConfig {
    // Centralized Base URL
    // Use "http://10.0.2.2:4000" for Android Emulator to hit localhost:4000
    // Use "https://rise30-app.onrender.com" for production
    const val BASE_URL = "https://rise30-app.onrender.com"

    val httpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = true
            })
        }
    }
}
