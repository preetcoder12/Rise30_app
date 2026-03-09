package com.rise30.app.util

import android.content.Context
import android.util.Log

/**
 * NotificationManager for ViewModels
 * Provides a clean interface for managing notifications from ViewModels
 */
class NotificationStateManager(private val context: Context) {

    companion object {
        private const val TAG = "NotificationState"
    }

    /**
     * Call when user successfully completes today's task
     */
    fun onTaskCompleted(currentDay: Int, streakLength: Int) {
        Log.d(TAG, "Task completed - Day $currentDay, Streak $streakLength")
        
        // Cancel streak reminders since today is done
        NotificationScheduler.cancelStreakReminder(context)
        
        // Schedule tomorrow's morning ritual
        NotificationScheduler.scheduleMorningRitual(context, currentDay + 1)
    }

    /**
     * Call when detecting user has missed days
     */
    fun onMissedDaysDetected(currentDay: Int, streakLength: Int, missedDays: Int) {
        Log.d(TAG, "Missed days detected - Current: $currentDay, Streak: $streakLength, Missed: $missedDays")
        
        if (missedDays > 0 && missedDays <= 2) {
            // Schedule evening reminder
            NotificationScheduler.scheduleStreakReminder(context, streakLength, missedDays)
        }
        
        // Ensure morning ritual is scheduled
        NotificationScheduler.scheduleMorningRitual(context, currentDay)
    }

    /**
     * Call when streak is broken/reset
     */
    fun onStreakReset(previousStreak: Int, currentDay: Int) {
        Log.d(TAG, "Streak reset - Previous: $previousStreak, Current day: $currentDay")
        
        // Show supportive notification
        NotificationHelper.showStreakBrokenNotification(context, previousStreak)
        
        // Schedule fresh start
        NotificationScheduler.scheduleMorningRitual(context, currentDay)
        NotificationScheduler.cancelStreakReminder(context)
    }

    /**
     * Call when user recovers a missed day (streak continues)
     */
    fun onStreakRecovered(newStreakLength: Int, currentDay: Int) {
        Log.d(TAG, "Streak recovered - New length: $newStreakLength, Current day: $currentDay")
        
        NotificationScheduler.cancelStreakReminder(context)
        NotificationScheduler.scheduleMorningRitual(context, currentDay + 1)
    }

    /**
     * Call when challenge is first created
     */
    fun onChallengeCreated() {
        Log.d(TAG, "Challenge created - scheduling first morning ritual")
        NotificationScheduler.scheduleMorningRitual(context, dayNumber = 1)
    }

    /**
     * Call when user creates a new account - send warm welcome!
     */
    fun onNewUserSignup(userName: String) {
        Log.d(TAG, "New user signup - sending welcome notification for $userName")
        
        // Show immediate welcome notification
        NotificationHelper.showWelcomeNotification(context, userName)
        
        // Schedule first morning ritual for tomorrow
        NotificationScheduler.scheduleMorningRitual(context, dayNumber = 1)
    }

    /**
     * Call when app is opened/resumed
     * This refreshes all notification schedules based on current state
     */
    fun onAppOpened(currentDay: Int, streakLength: Int, isTodayCompleted: Boolean) {
        Log.d(TAG, "App opened - Day $currentDay, Streak $streakLength, Today completed: $isTodayCompleted")
        
        // Reschedule all notifications
        NotificationScheduler.rescheduleAllNotifications(
            context = context,
            currentDay = currentDay,
            streakLength = streakLength
        )
        
        // If late in the day and not completed, show immediate reminder
        if (!isTodayCompleted && currentDay > streakLength) {
            val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
            if (hour >= 18) { // After 6 PM
                NotificationHelper.showStreakRecoveryNotification(
                    context,
                    streakLength,
                    currentDay - streakLength
                )
            }
        }
    }

    /**
     * Call when user enables/disables morning notifications in settings
     */
    fun setMorningNotificationsEnabled(enabled: Boolean, currentDay: Int) {
        Log.d(TAG, "Morning notifications ${if (enabled) "enabled" else "disabled"}")
        
        if (enabled) {
            NotificationScheduler.scheduleMorningRitual(context, currentDay)
        } else {
            NotificationScheduler.cancelMorningRitual(context)
        }
    }

    /**
     * Call when user enables/disables streak reminders in settings
     */
    fun setStreakRemindersEnabled(enabled: Boolean, streakLength: Int, missedDay: Int) {
        Log.d(TAG, "Streak reminders ${if (enabled) "enabled" else "disabled"}")
        
        if (enabled && missedDay > 0) {
            NotificationScheduler.scheduleStreakReminder(context, streakLength, missedDay)
        } else {
            NotificationScheduler.cancelStreakReminder(context)
        }
    }

    /**
     * Get notification permission status
     */
    fun hasPermission(): Boolean {
        return NotificationHelper.hasNotificationPermission(context)
    }

    /**
     * Request notification permission (should be called from Activity/Fragment)
     * Returns true if permission was already granted, false if request was launched
     */
    fun requestPermission(): Boolean {
        if (hasPermission()) {
            return true
        }
        // Note: Actual permission request should be done from Activity using launcher
        return false
    }
}

/**
 * Extension function to create NotificationStateManager from Context
 */
fun Context.createNotificationManager(): NotificationStateManager {
    return NotificationStateManager(this)
}
