# Rise30 Notification System

## Overview
The notification system provides friendly, encouraging notifications to help users stay on track with their morning rituals and habit challenges without being spammy or guilt-inducing.

## Features

### 1. Morning Ritual Notifications 🌟
- **Time**: Daily at 7:00 AM
- **Tone**: Friendly, encouraging, positive
- **Purpose**: Motivate users to start their daily ritual
- **Example Messages**:
  - "Good morning! ☀️ Your 5-day ritual awaits. Let's make today count!"
  - "Rise and shine! 🌅 Day 5 of your journey. You've got this!"
  - "Morning champion! 💪 Your 5-day streak is calling. Time to shine!"

### 2. Streak Recovery Reminders 💪
- **Time**: Daily at 8:00 PM (if user hasn't completed today's task)
- **Tone**: Supportive, understanding, motivating
- **Purpose**: Help users recover missed days before streak resets
- **Example Messages**:
  - "Hey there! 👋 Your 5-day streak is waiting. Complete today's task to keep it burning! 🔥"
  - "Don't give up! 💫 Your 5-day streak can still be saved. You're stronger than yesterday!"

### 3. Fresh Start Notifications 🔄
- **Trigger**: When a streak is reset after multiple missed days
- **Tone**: Compassionate, forward-looking, encouraging
- **Purpose**: Encourage users to restart without guilt
- **Example Message**: "Your 5-day streak may have reset, but your strength hasn't. 💪 Every master was once a beginner. Let's begin again! 🌟"

## Technical Implementation

### Components

1. **NotificationHelper.kt**
   - Creates notification channels
   - Builds and displays notifications
   - Handles permission checks
   - Provides friendly, randomized messages

2. **NotificationScheduler.kt**
   - Schedules daily morning ritual notifications (7:00 AM)
   - Schedules streak reminder notifications (8:00 PM)
   - Uses AlarmManager for precise timing
   - Automatically reschedules based on challenge state

3. **NotificationReceiver.kt**
   - BroadcastReceiver that handles scheduled notification triggers
   - Displays appropriate notifications based on intent action

4. **Rise30App.kt**
   - Initializes notification channels on app startup
   - Ensures channels are created before first use

### Permissions Required

```xml
<!-- For showing notifications (Android 13+) -->
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

<!-- For scheduling exact alarms -->
<uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" />
<uses-permission android:name="android.permission.USE_EXACT_ALARM" />
```

### Notification Channels

1. **Morning Rituals** (High Importance)
   - ID: `morning_ritual_channel`
   - Lights: Enabled
   - Vibration: Enabled
   - Purpose: Daily motivation and ritual reminders

2. **Streak Reminders** (High Importance)
   - ID: `streak_reminder_channel`
   - Lights: Enabled
   - Vibration: Enabled
   - Purpose: Critical streak recovery notifications

3. **General Notifications** (Default Importance)
   - ID: `general_channel`
   - Lights: Disabled
   - Vibration: Disabled
   - Purpose: Social interactions and general updates

## Usage

### Request Permission
The app automatically requests notification permission when user first logs in.

```kotlin
// In MainActivity
private fun requestNotificationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Already granted
            }
            else -> {
                // Request permission
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
```

### Schedule Notifications
```kotlin
// Schedule morning ritual
NotificationScheduler.scheduleMorningRitual(context, dayNumber = 5)

// Schedule streak reminder
NotificationScheduler.scheduleStreakReminder(context, streakLength = 5, missedDay = 1)

// Reschedule all based on current state
NotificationScheduler.rescheduleAllNotifications(
    context = context,
    currentDay = 5,
    streakLength = 3
)
```

### Show Immediate Notification
```kotlin
// Morning ritual
NotificationHelper.showMorningRitualNotification(context, dayNumber = 5)

// Streak recovery
NotificationHelper.showStreakRecoveryNotification(context, streakLength = 5, missedDay = 1)

// Streak broken (supportive)
NotificationHelper.showStreakBrokenNotification(context, previousStreak = 5)

// General notification
NotificationHelper.showGeneralNotification(
    context = context,
    title = "Friend Request",
    message = "John wants to join your challenge!"
)
```

## Design Principles

### ✅ DO
- Use emojis sparingly and appropriately (☀️💪🔥🌟✨)
- Keep messages short and uplifting
- Focus on encouragement, not guilt
- Acknowledge effort, not just results
- Use varied messages to avoid repetition
- Respect user's autonomy and choices

### ❌ DON'T
- Use guilt-tripping language ("You failed", "You're losing")
- Send too many notifications (max 2 per day)
- Nag or spam the user
- Use negative reinforcement
- Wake users up at inappropriate times
- Show notifications when user is actively using the app

## Future Enhancements

1. **Smart Timing**: Learn user's preferred wake-up time and adjust morning notifications accordingly
2. **Personalization**: Allow users to customize notification messages and frequency
3. **Achievement Celebrations**: Notify users when they hit milestones (7, 14, 30 days)
4. **Social Nudges**: Friendly competition notifications ("Your friend just completed their task!")
5. **Quiet Hours**: Respect user's Do Not Disturb preferences
6. **Adaptive Frequency**: Reduce notification frequency for highly engaged users

## Testing

To test notifications during development:

```kotlin
// Test morning ritual notification
NotificationHelper.showMorningRitualNotification(context, 5)

// Test streak recovery notification  
NotificationHelper.showStreakRecoveryNotification(context, 5, 1)

// Test streak broken notification
NotificationHelper.showStreakBrokenNotification(context, 5)
```

## Troubleshooting

### Notifications not showing?
1. Check if permission is granted (Settings → Apps → Rise30 → Notifications)
2. Ensure notification channel is enabled
3. Verify AlarmManager permissions for Android 12+
4. Check Do Not Disturb mode

### Notifications at wrong time?
1. Verify device timezone settings
2. Check if alarm scheduling is working correctly
3. Ensure app has background data/battery optimization exemptions

### Too many notifications?
1. Review notification scheduling logic
2. Cancel old alarms before scheduling new ones
3. Implement smarter deduplication logic
