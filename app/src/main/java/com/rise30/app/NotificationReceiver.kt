package com.rise30.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rise30.app.util.NotificationHelper

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "ACTION_SHOW_MORNING_RITUAL" -> {
                val dayNumber = intent.getIntExtra("day_number", 1)
                NotificationHelper.showMorningRitualNotification(context, dayNumber)
            }
            
            "ACTION_SHOW_STREAK_REMINDER" -> {
                val streakLength = intent.getIntExtra("streak_length", 0)
                val missedDay = intent.getIntExtra("missed_day", 1)
                if (streakLength > 0) {
                    NotificationHelper.showStreakRecoveryNotification(context, streakLength, missedDay)
                }
            }
        }
    }
}
