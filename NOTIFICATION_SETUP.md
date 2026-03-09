# 🌟 Rise30 Notification System - Quick Start

## What's Been Added

### ✅ Core Features Implemented

1. **Welcome Notifications** 🎉
   - Warm, friendly welcome for new users
   - Sent immediately after account creation
   - Example: "Welcome to Rise30, John! 🌟 Ready to build amazing habits together?"
   - Automatically schedules first morning ritual

2. **Morning Ritual Notifications** (7:00 AM daily)
   - Friendly, encouraging messages
   - Randomized to avoid repetition
   - Example: "Good morning! ☀️ Your 5-day ritual awaits. Let's make today count!"

3. **Streak Recovery Reminders** (8:00 PM if needed)
   - Supportive messages when users miss days
   - Helps prevent streak resets
   - Example: "Hey there! 👋 Your 5-day streak is waiting. Complete today's task!"

4. **Fresh Start Notifications** (When streak resets)
   - Compassionate, forward-looking messages
   - No guilt-tripping, only encouragement
   - Example: "Your streak reset, but your strength hasn't. Let's begin again! 🌟"

### 📁 Files Created

```
app/src/main/java/com/rise30/app/util/
├── NotificationHelper.kt          # Creates & shows notifications
├── NotificationScheduler.kt       # Schedules daily notifications  
├── NotificationExamples.kt        # Integration guide & examples
├── NotificationStateManager.kt    # ViewModel integration helper
└── 
app/src/main/java/com/rise30/app/
├── NotificationReceiver.kt        # Handles scheduled triggers
├── Rise30App.kt                   # Initializes notification channels
└── MainActivity.kt                # Requests permissions & integrates

res/drawable/
└── ic_notification.xml            # Bell icon for notifications

NOTIFICATIONS.md                   # Complete documentation
```

### 🔧 Permissions Added

- `POST_NOTIFICATIONS` - Show notifications (Android 13+)
- `SCHEDULE_EXACT_ALARM` - Schedule precise notification times
- `USE_EXACT_ALARM` - Use exact alarm API

### 🎯 Notification Channels

1. **Morning Rituals** - High priority, lights & vibration
2. **Streak Reminders** - High priority, lights & vibration  
3. **General** - Default priority, no lights/vibration

## How to Use

### Automatic Integration (Already Done ✅)

The app automatically:
- Requests notification permission on first login
- Creates notification channels on app startup
- Schedules morning rituals when challenges are loaded
- Reschedules notifications when app is opened

### Manual Integration Points

Add these calls to your existing code:

#### 1. After Completing a Daily Task
```kotlin
// In ChallengeDetailScreen.kt or wherever you mark tasks complete
NotificationExamples.onTaskCompleted(context, currentDay = 5, streakLength = 5)
```

#### 2. When Loading Challenges with Missed Days
```kotlin
// In HomePage.kt or ChallengesPage.kt
if (!isTodayCompleted) {
    NotificationExamples.onChallengesLoadedWithMissedDays(
        context = context,
        currentDay = currentDayNumber,
        streakLength = currentStreak,
        missedDays = currentDayNumber - currentStreak
    )
}
```

#### 3. When Streak is Broken
```kotlin
// In your streak detection logic
if (streakBroken) {
    NotificationExamples.onStreakBroken(context, previousStreak = 5, currentDay = 6)
}
```

#### 4. On App Resume
```kotlin
// In MainActivity.onResume()
NotificationExamples.onAppOpened(
    context = this,
    currentDay = 5,
    streakLength = 3,
    isTodayCompleted = false
)
```

## Testing

### Test Immediately
```kotlin
// In any composable or activity
val context = LocalContext.current

Button(
    onClick = {
        NotificationHelper.showMorningRitualNotification(context, dayNumber = 5)
    }
) {
    Text("Test Morning Notification")
}
```

### Test All Types
```kotlin
// Morning ritual
NotificationHelper.showMorningRitualNotification(context, 5)

// Streak recovery
NotificationHelper.showStreakRecoveryNotification(context, 5, 1)

// Streak broken (supportive)
NotificationHelper.showStreakBrokenNotification(context, 5)
```

## Design Principles

### ✅ DO
- ✨ Use friendly emojis (☀️💪🔥🌟)
- 🎯 Keep messages short and uplifting
- 🚀 Focus on encouragement, not guilt
- 🎭 Vary messages to avoid repetition
- ⏰ Respect user's time (max 2/day)

### ❌ DON'T
- 😞 Use guilt-tripping language
- 📱 Send too many notifications
- 🔔 Nag or spam the user
- ⚠️ Use negative reinforcement

## Next Steps

### Recommended Integrations

1. **ChallengeDetailScreen.kt**
   - Add `NotificationExamples.onTaskCompleted()` after successful completion
   
2. **HomePage.kt**
   - Add `NotificationExamples.onChallengesLoadedWithMissedDays()` when loading challenges
   
3. **StreakViewModel.kt** or **streakService.ts**
   - Add `NotificationExamples.onStreakBroken()` when detecting broken streaks

4. **Settings Screen** (Future)
   - Add toggles for notification preferences
   - Use `NotificationExamples.toggleMorningNotifications()`
   - Use `NotificationExamples.toggleStreakReminders()`

### Future Enhancements

- [ ] Smart timing based on user's actual wake-up time
- [ ] Achievement celebration notifications (7, 14, 30 days)
- [ ] Social competition notifications
- [ ] Customizable notification times
- [ ] Quiet hours / Do Not Disturb integration

## Troubleshooting

### Notifications not showing?
1. Check Settings → Apps → Rise30 → Notifications
2. Ensure permission is granted
3. Verify notification channel is enabled
4. Check Do Not Disturb mode

### Wrong timing?
1. Verify device timezone settings
2. Check if alarms are being scheduled correctly
3. Ensure battery optimization isn't blocking

### Need to debug?
```kotlin
// Check if permission is granted
val hasPermission = NotificationHelper.hasNotificationPermission(context)

// Manually schedule for testing (5 seconds from now)
val calendar = Calendar.getInstance().apply {
    add(Calendar.SECOND, 5)
}
// ... use this to test scheduling logic
```

## Documentation

- **Full Documentation**: See `NOTIFICATIONS.md` for complete details
- **Integration Examples**: See `NotificationExamples.kt` for code snippets
- **Architecture**: All notification logic is in `util/` package

---

**Made with 💛 for the Rise30 community**
