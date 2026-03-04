package com.rise30.app

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {

    // Replace with your own redirect URL configuration in Supabase dashboard:
    // e.g. rise30://auth
    private const val SUPABASE_URL = "https://htllljrunjgolzhfoqcy.supabase.co"
    private const val SUPABASE_KEY =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh0bGxsanJ1bmpnb2x6aGZvcWN5Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzI2MDEwOTEsImV4cCI6MjA4ODE3NzA5MX0.WVjN4xL4LnBSQeA5d19Lstr6TYaBgPAemJ5i0HXPddg"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Auth) {
            scheme = "rise30"
            host = "auth"
            autoLoadFromStorage = true
            alwaysAutoRefresh = true
        }
        install(Postgrest)
    }
}

