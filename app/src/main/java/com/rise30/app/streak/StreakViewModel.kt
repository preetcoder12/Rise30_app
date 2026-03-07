package com.rise30.app.streak

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import com.rise30.app.SupabaseClient

/**
 * API response data classes
 */
@Serializable
data class StreakStatusResponse(
    val streak: StreakData,
    val forgiveness: ForgivenessData,
    val message: String
)

@Serializable
data class StreakData(
    val length: Int,
    val missedDays: Int,
    val freezesUsed: Int,
    val freezesRemaining: Int,
    val lastCompletedDay: Int,
    val isActive: Boolean,
    val lastUpdated: String
)

@Serializable
data class ForgivenessData(
    val canRecover: Boolean,
    val canUseFreeze: Boolean,
    val daysMissed: Int,
    val isStreakBroken: Boolean,
    val recoveryDeadline: String? = null
)

@Serializable
data class CompleteDayRequest(val dayNumber: Int)

@Serializable
data class CompleteDayResponse(
    val success: Boolean,
    val streakLength: Int,
    val usedFreeze: Boolean,
    val recovered: Boolean,
    val message: String
)

@Serializable
data class CheckStreakResponse(
    val streakBroken: Boolean,
    val daysMissed: Int,
    val message: String,
    val canRecover: Boolean
)

/**
 * ViewModel for managing streak state and forgiveness mechanics
 */
class StreakViewModel : ViewModel() {

    private val httpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    // Backend URL - update this with your actual backend URL
    private val baseUrl = "https://your-backend-url.com/api"

    var streakState by mutableStateOf(StreakForgivenessState())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var showRecoveryCard by mutableStateOf(false)
        private set

    var showStreakBrokenCard by mutableStateOf(false)
        private set

    var previousStreakLength by mutableStateOf(0)
        private set

    /**
     * Load streak status for a user and challenge
     */
    fun loadStreakStatus(userId: String, challengeId: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                // First check if streak needs updating (missed days, etc.)
                val checkResponse: CheckStreakResponse = httpClient
                    .post("$baseUrl/streaks/$userId/$challengeId/check") {}
                    .body()

                if (checkResponse.streakBroken) {
                    previousStreakLength = streakState.streakLength
                    showStreakBrokenCard = true
                }

                // Then get current status
                val response: StreakStatusResponse = httpClient
                    .get("$baseUrl/streaks/$userId/$challengeId/status")
                    .body()

                streakState = StreakForgivenessState(
                    streakLength = response.streak.length,
                    missedDays = response.streak.missedDays,
                    freezesUsed = response.streak.freezesUsed,
                    freezesRemaining = response.streak.freezesRemaining,
                    canRecover = response.forgiveness.canRecover,
                    canUseFreeze = response.forgiveness.canUseFreeze,
                    isStreakBroken = response.forgiveness.isStreakBroken,
                    message = response.message
                )

                // Show recovery card if user can recover or use freeze
                showRecoveryCard = response.forgiveness.canRecover || response.forgiveness.canUseFreeze

            } catch (e: Exception) {
                errorMessage = e.message ?: "Failed to load streak status"
                // Use fallback state
                streakState = StreakForgivenessState(
                    message = "Offline mode - streak tracking unavailable"
                )
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Mark a day as complete
     */
    fun completeDay(userId: String, challengeId: String, dayNumber: Int) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response: CompleteDayResponse = httpClient
                    .post("$baseUrl/streaks/$userId/$challengeId/complete") {
                        contentType(ContentType.Application.Json)
                        setBody(CompleteDayRequest(dayNumber))
                    }
                    .body()

                if (response.success) {
                    streakState = streakState.copy(
                        streakLength = response.streakLength,
                        canRecover = false,
                        canUseFreeze = false,
                        message = response.message
                    )
                    showRecoveryCard = false
                } else {
                    errorMessage = response.message
                }

            } catch (e: Exception) {
                errorMessage = e.message ?: "Failed to complete day"
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Recover a missed day (user clicks "Recover Streak")
     */
    fun recoverStreak(userId: String, challengeId: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response: CompleteDayResponse = httpClient
                    .post("$baseUrl/streaks/$userId/$challengeId/recover") {}
                    .body()

                if (response.success) {
                    streakState = streakState.copy(
                        streakLength = response.streakLength,
                        canRecover = false,
                        canUseFreeze = false,
                        missedDays = 0,
                        message = response.message
                    )
                    showRecoveryCard = false
                } else {
                    errorMessage = response.message
                }

            } catch (e: Exception) {
                errorMessage = e.message ?: "Failed to recover streak"
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Dismiss the recovery card without recovering
     */
    fun dismissRecovery() {
        showRecoveryCard = false
    }

    /**
     * Dismiss the streak broken card
     */
    fun dismissStreakBroken() {
        showStreakBrokenCard = false
    }

    /**
     * Reset state (call when user logs out)
     */
    fun reset() {
        streakState = StreakForgivenessState()
        showRecoveryCard = false
        showStreakBrokenCard = false
        previousStreakLength = 0
        errorMessage = null
    }

    override fun onCleared() {
        super.onCleared()
        httpClient.close()
    }
}
