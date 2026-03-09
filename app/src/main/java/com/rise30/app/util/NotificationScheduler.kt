package com.rise30.app.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.AlarmManagerCompat
import com.rise30.app.NotificationReceiver
import java.util.Calendar
import java.util.concurrent.TimeUnit

object NotificationScheduler {

    private const val MORNING_RITUAL_REQUEST_CODE = 5001
    private const val STREAK_REMINDER_REQUEST_CODE = 5002

    /**
     * Schedule daily morning ritual notification at 7:00 AM
     */
    fun scheduleMorningRitual(context: Context, dayNumber: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        
        // Set time for 7:00 AM local time
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 7)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            
            // If it's already past 7 AM today, schedule for tomorrow
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = "ACTION_SHOW_MORNING_RITUAL"
            putExtra("day_number", dayNumber)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            MORNING_RITUAL_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Cancel any existing alarm first
        cancelMorningRitual(context)

        // Schedule the alarm
        AlarmManagerCompat.setExactAndAllowWhileIdle(
            alarmManager,
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    /**
     * Schedule streak recovery reminder at 8:00 PM if user hasn't completed today's task
     */
    fun scheduleStreakReminder(context: Context, streakLength: Int, missedDay: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        
        // Set time for 8:00 PM local time
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 20)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            
            // If it's already past 8 PM today, schedule for tomorrow
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = "ACTION_SHOW_STREAK_REMINDER"
            putExtra("streak_length", streakLength)
            putExtra("missed_day", missedDay)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            STREAK_REMINDER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Cancel any existing streak reminder first
        cancelStreakReminder(context)

        // Schedule the alarm
        AlarmManagerCompat.setExactAndAllowWhileIdle(
            alarmManager,
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    /**
     * Cancel scheduled morning ritual notification
     */
    fun cancelMorningRitual(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = "ACTION_SHOW_MORNING_RITUAL"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            MORNING_RITUAL_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        pendingIntent?.let {
            alarmManager.cancel(it)
        }
    }

    /**
     * Cancel scheduled streak reminder notification
     */
    fun cancelStreakReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = "ACTION_SHOW_STREAK_REMINDER"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            STREAK_REMINDER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        pendingIntent?.let {
            alarmManager.cancel(it)
        }
    }

    /**
     * Reschedule all notifications based on current state
     */
    fun rescheduleAllNotifications(context: Context, currentDay: Int, streakLength: Int) {
        // Always schedule morning ritual
        scheduleMorningRitual(context, currentDay)
        
        // Only schedule streak reminder if there are missed days
        if (currentDay > streakLength) {
            val missedDays = currentDay - streakLength
            scheduleStreakReminder(context, streakLength, missedDays)
        } else {
            cancelStreakReminder(context)
        }
    }
}
