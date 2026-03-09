package com.rise30.app.calendar

import android.content.Context
import android.content.SharedPreferences
import java.util.*

/**
 * Manages calendar integration for challenges
 * Stores event IDs and handles sync status
 */
class ChallengeCalendarManager(private val context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, 
        Context.MODE_PRIVATE
    )
    
    companion object {
        private const val PREFS_NAME = "challenge_calendar_prefs"
        private const val KEY_EVENT_IDS_PREFIX = "event_ids_"
        private const val KEY_SYNCED_PREFIX = "synced_"
        private const val KEY_REMINDER_TIME_PREFIX = "reminder_time_"
    }
    
    /**
     * Save calendar event IDs for a challenge
     */
    fun saveChallengeEvents(challengeId: String, eventIds: List<Long>) {
        prefs.edit().apply {
            putString(
                KEY_EVENT_IDS_PREFIX + challengeId,
                eventIds.joinToString(",")
            )
            putBoolean(KEY_SYNCED_PREFIX + challengeId, true)
            apply()
        }
    }
    
    /**
     * Get saved event IDs for a challenge
     */
    fun getChallengeEventIds(challengeId: String): List<Long> {
        val idsString = prefs.getString(KEY_EVENT_IDS_PREFIX + challengeId, "") ?: ""
        return if (idsString.isEmpty()) {
            emptyList()
        } else {
            idsString.split(",").mapNotNull { it.toLongOrNull() }
        }
    }
    
    /**
     * Check if challenge is synced to calendar
     */
    fun isChallengeSynced(challengeId: String): Boolean {
        return prefs.getBoolean(KEY_SYNCED_PREFIX + challengeId, false)
    }
    
    /**
     * Save reminder time for a challenge
     */
    fun saveReminderTime(challengeId: String, hour: Int, minute: Int) {
        prefs.edit().apply {
            putInt(KEY_REMINDER_TIME_PREFIX + challengeId + "_hour", hour)
            putInt(KEY_REMINDER_TIME_PREFIX + challengeId + "_minute", minute)
            apply()
        }
    }
    
    /**
     * Get saved reminder time for a challenge
     */
    fun getReminderTime(challengeId: String): Pair<Int, Int> {
        val hour = prefs.getInt(KEY_REMINDER_TIME_PREFIX + challengeId + "_hour", 20)
        val minute = prefs.getInt(KEY_REMINDER_TIME_PREFIX + challengeId + "_minute", 0)
        return Pair(hour, minute)
    }
    
    /**
     * Remove challenge from calendar and clear saved data
     */
    fun removeChallengeCalendarData(challengeId: String): Int {
        val eventIds = getChallengeEventIds(challengeId)
        val calendarHelper = CalendarHelper(context)
        
        var deletedCount = 0
        eventIds.forEach { eventId ->
            if (calendarHelper.deleteEvent(eventId)) {
                deletedCount++
            }
        }
        
        // Clear saved data
        prefs.edit().apply {
            remove(KEY_EVENT_IDS_PREFIX + challengeId)
            remove(KEY_SYNCED_PREFIX + challengeId)
            remove(KEY_REMINDER_TIME_PREFIX + challengeId + "_hour")
            remove(KEY_REMINDER_TIME_PREFIX + challengeId + "_minute")
            apply()
        }
        
        return deletedCount
    }
    
    /**
     * Update calendar events for a challenge (e.g., when reminder time changes)
     */
    fun updateChallengeReminderTime(
        challengeId: String,
        challengeName: String,
        durationDays: Int,
        startDate: Calendar,
        newHour: Int,
        newMinute: Int
    ): List<Long> {
        // Remove old events
        removeChallengeCalendarData(challengeId)
        
        // Create new events with updated time
        val calendarHelper = CalendarHelper(context)
        val reminderTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, newHour)
            set(Calendar.MINUTE, newMinute)
        }
        
        val newEventIds = calendarHelper.createChallengeEvents(
            challengeName,
            durationDays,
            startDate,
            reminderTime
        )
        
        // Save new event IDs and reminder time
        saveChallengeEvents(challengeId, newEventIds)
        saveReminderTime(challengeId, newHour, newMinute)
        
        return newEventIds
    }
    
    /**
     * Get sync status for all challenges
     */
    fun getAllSyncedChallenges(): List<String> {
        return prefs.all.keys
            .filter { it.startsWith(KEY_SYNCED_PREFIX) }
            .map { it.removePrefix(KEY_SYNCED_PREFIX) }
            .filter { prefs.getBoolean(KEY_SYNCED_PREFIX + it, false) }
    }
    
    /**
     * Verify and fix sync status (remove entries for deleted events)
     */
    fun verifyAndFixSyncStatus(): Int {
        val calendarHelper = CalendarHelper(context)
        var fixedCount = 0
        
        getAllSyncedChallenges().forEach { challengeId ->
            val eventIds = getChallengeEventIds(challengeId)
            val existingEvents = eventIds.filter { calendarHelper.eventExists(it) }
            
            if (existingEvents.size != eventIds.size) {
                // Some events were deleted externally
                if (existingEvents.isEmpty()) {
                    // All events deleted, clear sync status
                    prefs.edit().apply {
                        remove(KEY_EVENT_IDS_PREFIX + challengeId)
                        remove(KEY_SYNCED_PREFIX + challengeId)
                        apply()
                    }
                } else {
                    // Update with remaining events
                    saveChallengeEvents(challengeId, existingEvents)
                }
                fixedCount++
            }
        }
        
        return fixedCount
    }
}
