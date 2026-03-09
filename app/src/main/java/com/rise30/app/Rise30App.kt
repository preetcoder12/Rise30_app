package com.rise30.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.rise30.app.util.NotificationHelper

class Rise30App : Application() {

    override fun onCreate() {
        super.onCreate()
        // Create notification channels on app startup
        NotificationHelper.createNotificationChannels(this)
    }
}

