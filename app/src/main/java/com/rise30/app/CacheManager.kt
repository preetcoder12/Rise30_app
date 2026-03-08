package com.rise30.app

import android.content.Context
import android.content.SharedPreferences
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object CacheManager {
    private const val PREF_NAME = "rise30_cache"
    private const val KEY_CHALLENGES = "challenges_list_"
    private const val KEY_CHALLENGE_DETAIL = "challenge_detail_"
    private const val KEY_HOME_DATA = "home_data_"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // --- Challenges List ---
    fun saveChallenges(context: Context, userId: String, challenges: List<ChallengeSummary>) {
        try {
            val json = Json.encodeToString(challenges)
            getPrefs(context).edit().putString(KEY_CHALLENGES + userId, json).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getChallenges(context: Context, userId: String): List<ChallengeSummary>? {
        try {
            val json = getPrefs(context).getString(KEY_CHALLENGES + userId, null) ?: return null
            return Json.decodeFromString<List<ChallengeSummary>>(json)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    // --- Challenge Detail ---
    fun saveChallengeDetail(context: Context, challengeId: String, detail: ChallengeDetailApiResponse) {
        try {
            val json = Json.encodeToString(detail)
            getPrefs(context).edit().putString(KEY_CHALLENGE_DETAIL + challengeId, json).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getChallengeDetail(context: Context, challengeId: String): ChallengeDetailApiResponse? {
        try {
            val json = getPrefs(context).getString(KEY_CHALLENGE_DETAIL + challengeId, null) ?: return null
            return Json.decodeFromString<ChallengeDetailApiResponse>(json)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
    // --- Analytics Details ---
    fun saveAnalytics(context: Context, userId: String, analytics: AnalyticsResponse) {
        try {
            val json = Json.encodeToString(analytics)
            getPrefs(context).edit().putString("analytics_" + userId, json).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getAnalytics(context: Context, userId: String): AnalyticsResponse? {
        try {
            val json = getPrefs(context).getString("analytics_" + userId, null) ?: return null
            return Json.decodeFromString<AnalyticsResponse>(json)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    // --- User Profile ---
    fun saveProfile(context: Context, userId: String, profile: UserProfileResponse) {
        try {
            val json = Json.encodeToString(profile)
            getPrefs(context).edit().putString("profile_" + userId, json).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getProfile(context: Context, userId: String): UserProfileResponse? {
        try {
            val json = getPrefs(context).getString("profile_" + userId, null) ?: return null
            return Json.decodeFromString<UserProfileResponse>(json)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}
