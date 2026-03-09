package com.rise30.app.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.rise30.app.MainActivity
import com.rise30.app.R

object NotificationHelper {

    const val MORNING_RITUAL_CHANNEL_ID = "morning_ritual_channel"
    const val STREAK_REMINDER_CHANNEL_ID = "streak_reminder_channel"
    const val GENERAL_CHANNEL_ID = "general_channel"

    const val MORNING_RITUAL_NOTIFICATION_ID = 1001
    const val STREAK_REMINDER_NOTIFICATION_ID = 1002
    const val GENERAL_NOTIFICATION_ID = 1003
    const val WELCOME_NOTIFICATION_ID = 1004

    /**
     * Create notification channels (required for Android 8.0+)
     * Call this in Application.onCreate()
     */
    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Morning Ritual Channel - High importance for daily motivation
            val morningRitualChannel = NotificationChannel(
                MORNING_RITUAL_CHANNEL_ID,
                "Morning Rituals",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Daily morning motivation and ritual reset reminders"
                enableLights(true)
                enableVibration(true)
            }

            // Streak Reminder Channel - High importance for streak recovery
            val streakReminderChannel = NotificationChannel(
                STREAK_REMINDER_CHANNEL_ID,
                "Streak Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Important streak recovery and completion reminders"
                enableLights(true)
                enableVibration(true)
            }

            // General Channel - Default importance for general updates
            val generalChannel = NotificationChannel(
                GENERAL_CHANNEL_ID,
                "General Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "General app updates and social notifications"
                enableLights(false)
                enableVibration(false)
            }

            // Welcome Channel - High importance for new user onboarding
            val welcomeChannel = NotificationChannel(
                WELCOME_NOTIFICATION_ID.toString(),
                "Welcome & Onboarding",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Warm welcome messages for new users"
                enableLights(true)
                enableVibration(true)
                setShowBadge(true)
            }

            notificationManager.createNotificationChannels(
                listOf(morningRitualChannel, streakReminderChannel, generalChannel, welcomeChannel)
            )
        }
    }

    /**
     * Check if notification permissions are granted
     */
    fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // Permissions not required for older versions
        }
    }

    /**
     * Show morning ritual notification - Friendly and encouraging
     */
    fun showMorningRitualNotification(context: Context, dayNumber: Int) {
        if (!hasNotificationPermission(context)) return

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val friendlyMessages = listOf(
            "Good morning! ☀️ Your $dayNumber-day ritual awaits. Let's make today count!",
            "Rise and shine! 🌅 Day $dayNumber of your journey. You've got this!",
            "Morning champion! 💪 Your $dayNumber-day streak is calling. Time to shine!",
            "New day, new win! 🎯 Day $dayNumber starts now. Let's do it!",
            "Hello sunshine! ✨ Your $dayNumber-day ritual is ready. Make it happen!"
        )

        val message = friendlyMessages.random()

        val builder = NotificationCompat.Builder(context, MORNING_RITUAL_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("🌟 Time for Your Morning Ritual!")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        try {
            NotificationManagerCompat.from(context).notify(
                MORNING_RITUAL_NOTIFICATION_ID,
                builder.build()
            )
        } catch (e: SecurityException) {
            // Permission not granted
        }
    }

    /**
     * Show streak recovery notification - Encouraging, not guilt-tripping
     */
    fun showStreakRecoveryNotification(context: Context, streakLength: Int, missedDay: Int) {
        if (!hasNotificationPermission(context)) return

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val message = when {
            missedDay == 1 -> "Hey there! 👋 Your $streakLength-day streak is waiting. Complete today's task to keep it burning! 🔥"
            missedDay == 2 -> "Don't give up! 💫 Your $streakLength-day streak can still be saved. You're stronger than yesterday!"
            else -> "Fresh start time! 🌱 Every day is a new beginning. Your challenge is ready when you are!"
        }

        val builder = NotificationCompat.Builder(context, STREAK_REMINDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("💪 Your Journey Continues!")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        try {
            NotificationManagerCompat.from(context).notify(
                STREAK_REMINDER_NOTIFICATION_ID,
                builder.build()
            )
        } catch (e: SecurityException) {
            // Permission not granted
        }
    }

    /**
     * Show streak broken notification - Supportive and motivating
     */
    fun showStreakBrokenNotification(context: Context, previousStreak: Int) {
        if (!hasNotificationPermission(context)) return

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val message = "Your $previousStreak-day streak may have reset, but your strength hasn't. 💪 Every master was once a beginner. Let's begin again! 🌟"

        val builder = NotificationCompat.Builder(context, STREAK_REMINDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("🔄 Fresh Start Time!")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        try {
            NotificationManagerCompat.from(context).notify(
                STREAK_REMINDER_NOTIFICATION_ID,
                builder.build()
            )
        } catch (e: SecurityException) {
            // Permission not granted
        }
    }

    /**
     * Show welcome notification for new users - Warm and encouraging
     */
    fun showWelcomeNotification(context: Context, userName: String) {
        if (!hasNotificationPermission(context)) return

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val welcomeMessages = listOf(
            "Welcome to Rise30, $userName! 🌟 Ready to build amazing habits together? Let's start your journey today!",
            "Hey $userName! 👋 Welcome to the Rise30 family! Your transformation starts now - one day at a time 💪",
            "So excited to have you here, $userName! ✨ Get ready to discover the best version of yourself 🚀",
            "Welcome aboard, $userName! 🎯 Your 30-day journey to greatness begins right now. You've got this!",
            "Hi $userName! 🌅 Welcome to Rise30! Every expert was once a beginner - let's begin your story today 📖"
        )

        val message = welcomeMessages.random()

        val builder = NotificationCompat.Builder(context, WELCOME_NOTIFICATION_ID.toString())
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("🎉 Welcome to Rise30, $userName!")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_SOCIAL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        try {
            NotificationManagerCompat.from(context).notify(
                WELCOME_NOTIFICATION_ID,
                builder.build()
            )
        } catch (e: SecurityException) {
            // Permission not granted
        }
    }

    /**
     * Show general notification for social interactions
     */
    fun showGeneralNotification(
        context: Context,
        title: String,
        message: String,
        notificationId: Int = GENERAL_NOTIFICATION_ID
    ) {
        if (!hasNotificationPermission(context)) return

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, GENERAL_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SOCIAL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        try {
            NotificationManagerCompat.from(context).notify(notificationId, builder.build())
        } catch (e: SecurityException) {
            // Permission not granted
        }
    }
}
