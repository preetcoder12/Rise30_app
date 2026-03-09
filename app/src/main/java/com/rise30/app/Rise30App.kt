package com.rise30.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.rise30.app.util.NotificationHelper

class Rise30App : Application() {

    companion object {
        lateinit var analytics: FirebaseAnalytics
            private set
    }

    override fun onCreate() {
        super.onCreate()
        
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        
        // Initialize Analytics
        analytics = FirebaseAnalytics.getInstance(this)
        
        // Enable Crashlytics
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        
        // Log app open event
        val bundle = Bundle()
        bundle.putString("app_version", "1.0")
        analytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle)
        
        // Create notification channels on app startup
        NotificationHelper.createNotificationChannels(this)
    }
}

