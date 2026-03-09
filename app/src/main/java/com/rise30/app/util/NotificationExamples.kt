package com.rise30.app.util

import android.content.Context
import com.rise30.app.util.NotificationHelper
import com.rise30.app.util.NotificationScheduler

/**
 * Notification Integration Examples
 * 
 * Use these examples to integrate notifications into your challenge flows
 */

object NotificationExamples {

    /**
     * Example 1: When user completes a daily task
     * Call this after successful task completion
     */
    fun onTaskCompleted(context: Context, currentDay: Int, streakLength: Int) {
        // Cancel any pending streak reminders since user completed today's task
        NotificationScheduler.cancelStreakReminder(context)
        
        // Schedule tomorrow's morning ritual
        NotificationScheduler.scheduleMorningRitual(context, currentDay + 1)
        
        // Optional: Show immediate celebration notification
        // (Only if you want to celebrate the completion immediately)
        // NotificationHelper.showMorningRitualNotification(context, currentDay)
    }

    /**
     * Example 2: When loading challenges and user hasn't completed today
     * Call this when you detect missed days
     */
    fun onChallengesLoadedWithMissedDays(
        context: Context,
        currentDay: Int,
        streakLength: Int,
        missedDays: Int
    ) {
        // Schedule evening reminder to help user catch up
        if (missedDays > 0 && missedDays <= 2) {
            NotificationScheduler.scheduleStreakReminder(context, streakLength, missedDays)
        }
        
        // Always schedule morning ritual
        NotificationScheduler.scheduleMorningRitual(context, currentDay)
    }

    /**
     * Example 3: When streak is broken
     * Call this when detecting a reset streak
     */
    fun onStreakBroken(context: Context, previousStreak: Int, currentDay: Int) {
        // Show supportive notification immediately
        NotificationHelper.showStreakBrokenNotification(context, previousStreak)
        
        // Schedule fresh start morning ritual
        NotificationScheduler.scheduleMorningRitual(context, currentDay)
        
        // Don't schedule streak reminder - give them a fresh start
        NotificationScheduler.cancelStreakReminder(context)
    }

    /**
     * Example 4: When user recovers a missed day
     * Call this after successful recovery
     */
    fun onStreakRecovered(context: Context, newStreakLength: Int, currentDay: Int) {
        // Cancel streak reminders
        NotificationScheduler.cancelStreakReminder(context)
        
        // Schedule normal morning ritual
        NotificationScheduler.scheduleMorningRitual(context, currentDay + 1)
    }

    /**
     * Example 5: When user creates a new account
     * Call this immediately after successful signup
     */
    fun onNewUserSignup(context: Context, userName: String) {
        // Show warm welcome notification immediately
        NotificationHelper.showWelcomeNotification(context, userName)
        
        // Schedule first morning ritual for tomorrow
        NotificationScheduler.scheduleMorningRitual(context, dayNumber = 1)
    }

    /**
     * Example 6: When challenge is first created
     * Call this when user creates a new challenge (for existing users)
     */
    fun onChallengeCreated(context: Context) {
        // Schedule first morning ritual for tomorrow
        NotificationScheduler.scheduleMorningRitual(context, dayNumber = 1)
    }

    /**
     * Example 7: When user opens the app
     * Call this in onResume or similar to reschedule based on current state
     */
    fun onAppOpened(
        context: Context,
        currentDay: Int,
        streakLength: Int,
        isTodayCompleted: Boolean
    ) {
        // Reschedule all notifications based on current state
        NotificationScheduler.rescheduleAllNotifications(
            context = context,
            currentDay = currentDay,
            streakLength = streakLength
        )
        
        // If today is not completed and it's late in the day, show immediate reminder
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
     * Example 7: When user manually wants to enable/disable notifications
     * Add this to settings or preferences
     */
    fun toggleMorningNotifications(context: Context, enabled: Boolean, currentDay: Int) {
        if (enabled) {
            NotificationScheduler.scheduleMorningRitual(context, currentDay)
        } else {
            NotificationScheduler.cancelMorningRitual(context)
        }
    }

    fun toggleStreakReminders(context: Context, enabled: Boolean, streakLength: Int, missedDay: Int) {
        if (enabled && missedDay > 0) {
            NotificationScheduler.scheduleStreakReminder(context, streakLength, missedDay)
        } else {
            NotificationScheduler.cancelStreakReminder(context)
        }
    }
}

/**
 * Integration Points in Your Code:
 * 
 * 1. In AuthViewModel.kt - after successful signup:
 *    ```kotlin
 *    NotificationExamples.onNewUserSignup(context, userName = state.email?.substringBefore("@") ?: "Friend")
 *    ```
 * 
 * 2. In ChallengeDetailScreen.kt - after marking day complete:
 *    ```kotlin
 *    NotificationExamples.onTaskCompleted(context, currentDay, streakLength)
 *    ```
 * 
 * 3. In HomePage.kt - when loading challenges:
 *    ```kotlin
 *    if (!isTodayCompleted) {
 *        NotificationExamples.onChallengesLoadedWithMissedDays(
 *            context, 
 *            currentDayNumber, 
 *            currentStreak, 
 *            missedDays = currentDayNumber - currentStreak
 *        )
 *    }
 *    ```
 * 
 * 4. In StreakViewModel.kt - when detecting broken streak:
 *    ```kotlin
 *    if (streakBroken) {
 *        NotificationExamples.onStreakBroken(context, previousStreak, currentDay)
 *    }
 *    ```
 * 
 * 5. In MainActivity.onResume() - refresh notifications:
 *    ```kotlin
 *    NotificationExamples.onAppOpened(context, currentDay, streakLength, isTodayCompleted)
 *    ```
 */
