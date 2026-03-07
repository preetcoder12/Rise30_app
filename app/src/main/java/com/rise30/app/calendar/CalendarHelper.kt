package com.rise30.app.calendar

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.CalendarContract
import androidx.core.content.ContextCompat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Data class representing a calendar event
 */
data class CalendarEvent(
    val title: String,
    val description: String,
    val startTime: Long,
    val endTime: Long,
    val reminderMinutes: Int = 10
)

/**
 * Helper class for calendar operations
 */
class CalendarHelper(private val context: Context) {

    companion object {
        const val PERMISSION_REQUEST_CODE = 100
        val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
        )
    }

    /**
     * Check if calendar permissions are granted
     */
    fun hasCalendarPermissions(): Boolean {
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * Get the primary Google calendar ID
     * Returns null if no calendar found or no permission
     */
    fun getPrimaryCalendarId(): Long? {
        if (!hasCalendarPermissions()) return null

        val projection = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CalendarContract.Calendars.IS_PRIMARY
        )

        context.contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            projection,
            null,
            null,
            null
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndex(CalendarContract.Calendars._ID)
            val isPrimaryColumn = cursor.getColumnIndex(CalendarContract.Calendars.IS_PRIMARY)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val isPrimary = cursor.getInt(isPrimaryColumn)
                
                // Return the primary calendar (usually Google)
                if (isPrimary == 1) {
                    return id
                }
            }
            
            // If no primary found, return the first calendar
            cursor.moveToFirst()
            if (cursor.count > 0) {
                return cursor.getLong(idColumn)
            }
        }
        
        return null
    }

    /**
     * Create a calendar event directly (requires WRITE_CALENDAR permission)
     */
    fun createEvent(event: CalendarEvent): Long? {
        if (!hasCalendarPermissions()) return null

        val calendarId = getPrimaryCalendarId() ?: return null

        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, event.startTime)
            put(CalendarContract.Events.DTEND, event.endTime)
            put(CalendarContract.Events.TITLE, event.title)
            put(CalendarContract.Events.DESCRIPTION, event.description)
            put(CalendarContract.Events.CALENDAR_ID, calendarId)
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
            put(CalendarContract.Events.HAS_ALARM, 1)
        }

        val uri = context.contentResolver.insert(
            CalendarContract.Events.CONTENT_URI,
            values
        )

        val eventId = uri?.lastPathSegment?.toLongOrNull()
        
        // Add reminder
        if (eventId != null && event.reminderMinutes > 0) {
            addReminder(eventId, event.reminderMinutes)
        }

        return eventId
    }

    /**
     * Add a reminder to an existing event
     */
    private fun addReminder(eventId: Long, minutes: Int) {
        val values = ContentValues().apply {
            put(CalendarContract.Reminders.EVENT_ID, eventId)
            put(CalendarContract.Reminders.MINUTES, minutes)
            put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
        }

        context.contentResolver.insert(
            CalendarContract.Reminders.CONTENT_URI,
            values
        )
    }

    /**
     * Create multiple events for a challenge (e.g., 30 days)
     */
    fun createChallengeEvents(
        challengeName: String,
        durationDays: Int,
        startDate: Calendar,
        dailyReminderTime: Calendar
    ): List<Long> {
        val eventIds = mutableListOf<Long>()
        
        for (day in 1..durationDays) {
            // Calculate the date for this day
            val eventDate = startDate.clone() as Calendar
            eventDate.add(Calendar.DAY_OF_YEAR, day - 1)
            
            // Set the reminder time
            eventDate.set(Calendar.HOUR_OF_DAY, dailyReminderTime.get(Calendar.HOUR_OF_DAY))
            eventDate.set(Calendar.MINUTE, dailyReminderTime.get(Calendar.MINUTE))
            eventDate.set(Calendar.SECOND, 0)
            
            val startTime = eventDate.timeInMillis
            val endTime = startTime + TimeUnit.MINUTES.toMillis(30) // 30 min duration
            
            val event = CalendarEvent(
                title = "Rise30: Day $day - $challengeName",
                description = "Complete your daily challenge task for Day $day. Stay consistent and build your streak! 🔥",
                startTime = startTime,
                endTime = endTime,
                reminderMinutes = 15 // Remind 15 minutes before
            )
            
            createEvent(event)?.let { eventIds.add(it) }
        }
        
        return eventIds
    }

    /**
     * Open calendar intent to add a single event (no permissions needed)
     * This opens the native calendar app with pre-filled details
     */
    fun openCalendarIntent(
        title: String,
        description: String,
        startTime: Long,
        endTime: Long
    ) {
        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, title)
            putExtra(CalendarContract.Events.DESCRIPTION, description)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
            putExtra(CalendarContract.Events.ALL_DAY, false)
            putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
        }
        
        context.startActivity(intent)
    }

    /**
     * Open calendar intent for today's challenge
     */
    fun openCalendarForToday(dayNumber: Int, challengeName: String) {
        val calendar = Calendar.getInstance()
        val startTime = calendar.timeInMillis
        val endTime = startTime + TimeUnit.MINUTES.toMillis(30)
        
        openCalendarIntent(
            title = "Rise30: Day $dayNumber - $challengeName",
            description = "Complete your daily challenge task for Day $dayNumber. Stay consistent! 🔥",
            startTime = startTime,
            endTime = endTime
        )
    }

    /**
     * Delete a calendar event by ID
     */
    fun deleteEvent(eventId: Long): Boolean {
        if (!hasCalendarPermissions()) return false
        
        val uri = Uri.withAppendedPath(CalendarContract.Events.CONTENT_URI, eventId.toString())
        val rowsDeleted = context.contentResolver.delete(uri, null, null)
        return rowsDeleted > 0
    }

    /**
     * Check if a calendar event exists
     */
    fun eventExists(eventId: Long): Boolean {
        if (!hasCalendarPermissions()) return false
        
        val uri = Uri.withAppendedPath(CalendarContract.Events.CONTENT_URI, eventId.toString())
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            return cursor.count > 0
        }
        return false
    }
}

/**
 * Extension function to check if calendar app is available
 */
fun Context.isCalendarAppAvailable(): Boolean {
    val intent = Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI
    }
    return intent.resolveActivity(packageManager) != null
}
