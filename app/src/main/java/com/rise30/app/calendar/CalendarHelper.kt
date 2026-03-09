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
            put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
            put(CalendarContract.Events.STATUS, CalendarContract.Events.STATUS_CONFIRMED)
            put(CalendarContract.Events.ALL_DAY, 0)
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
        // Check if reminder already exists
        val existingReminder = checkExistingReminder(eventId)
        
        if (existingReminder) {
            // Update existing reminder
            updateReminder(eventId, minutes)
        } else {
            // Insert new reminder
            insertReminder(eventId, minutes)
        }
    }
    
    /**
     * Check if event already has a reminder
     */
    private fun checkExistingReminder(eventId: Long): Boolean {
        val projection = arrayOf(CalendarContract.Reminders.MINUTES)
        val selection = "${CalendarContract.Reminders.EVENT_ID} = ?"
        val selectionArgs = arrayOf(eventId.toString())
        
        context.contentResolver.query(
            CalendarContract.Reminders.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            return cursor.count > 0
        }
        return false
    }
    
    /**
     * Update existing reminder
     */
    private fun updateReminder(eventId: Long, minutes: Int) {
        val values = ContentValues().apply {
            put(CalendarContract.Reminders.MINUTES, minutes)
            put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
        }
        
        val selection = "${CalendarContract.Reminders.EVENT_ID} = ?"
        val selectionArgs = arrayOf(eventId.toString())
        
        context.contentResolver.update(
            CalendarContract.Reminders.CONTENT_URI,
            values,
            selection,
            selectionArgs
        )
    }
    
    /**
     * Insert new reminder
     */
    private fun insertReminder(eventId: Long, minutes: Int) {
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
        val currentTime = System.currentTimeMillis()
        
        for (day in 1..durationDays) {
            // Calculate the date for this day
            val eventDate = startDate.clone() as Calendar
            eventDate.add(Calendar.DAY_OF_YEAR, day - 1)
            
            // Set the reminder time
            eventDate.set(Calendar.HOUR_OF_DAY, dailyReminderTime.get(Calendar.HOUR_OF_DAY))
            eventDate.set(Calendar.MINUTE, dailyReminderTime.get(Calendar.MINUTE))
            eventDate.set(Calendar.SECOND, 0)
            eventDate.set(Calendar.MILLISECOND, 0)
            
            val startTime = eventDate.timeInMillis
            
            // Skip events in the past
            if (startTime < currentTime) {
                continue
            }
            
            val endTime = startTime + TimeUnit.MINUTES.toMillis(30) // 30 min duration
            
            // Calculate minutes until event for dynamic reminder
            val minutesUntilEvent = ((startTime - currentTime) / 60000).toInt()
            val reminderMinutes = maxOf(1, minOf(minutesUntilEvent, 15)) // Remind 15 min before, or 1 min if very soon
            
            val event = CalendarEvent(
                title = "Rise30: Day $day - $challengeName",
                description = "Complete your daily challenge task for Day $day. Stay consistent and build your streak! 🔥",
                startTime = startTime,
                endTime = endTime,
                reminderMinutes = reminderMinutes
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

    /**
     * Get all events for a challenge by title pattern
     */
    fun getChallengeEvents(challengeName: String): List<Long> {
        if (!hasCalendarPermissions()) return emptyList()
        
        val eventIds = mutableListOf<Long>()
        val selection = "${CalendarContract.Events.TITLE} LIKE ?"
        val selectionArgs = arrayOf("%$challengeName%")
        
        context.contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            arrayOf(CalendarContract.Events._ID),
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndex(CalendarContract.Events._ID)
            while (cursor.moveToNext()) {
                eventIds.add(cursor.getLong(idColumn))
            }
        }
        
        return eventIds
    }

    /**
     * Delete all events for a challenge
     */
    fun deleteChallengeEvents(challengeName: String): Int {
        if (!hasCalendarPermissions()) return 0
        
        val eventIds = getChallengeEvents(challengeName)
        var deletedCount = 0
        
        eventIds.forEach { eventId ->
            if (deleteEvent(eventId)) {
                deletedCount++
            }
        }
        
        return deletedCount
    }

    /**
     * Update an existing event
     */
    fun updateEvent(eventId: Long, event: CalendarEvent): Boolean {
        if (!hasCalendarPermissions()) return false

        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, event.startTime)
            put(CalendarContract.Events.DTEND, event.endTime)
            put(CalendarContract.Events.TITLE, event.title)
            put(CalendarContract.Events.DESCRIPTION, event.description)
        }

        val uri = Uri.withAppendedPath(CalendarContract.Events.CONTENT_URI, eventId.toString())
        val rowsUpdated = context.contentResolver.update(uri, values, null, null)
        
        return rowsUpdated > 0
    }

    /**
     * Get calendar accounts available on device
     */
    fun getAvailableCalendars(): List<CalendarAccount> {
        if (!hasCalendarPermissions()) return emptyList()

        val calendars = mutableListOf<CalendarAccount>()
        val projection = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CalendarContract.Calendars.ACCOUNT_NAME,
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
            val nameColumn = cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)
            val accountColumn = cursor.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME)
            val primaryColumn = cursor.getColumnIndex(CalendarContract.Calendars.IS_PRIMARY)

            while (cursor.moveToNext()) {
                calendars.add(
                    CalendarAccount(
                        id = cursor.getLong(idColumn),
                        displayName = cursor.getString(nameColumn) ?: "",
                        accountName = cursor.getString(accountColumn) ?: "",
                        isPrimary = cursor.getInt(primaryColumn) == 1
                    )
                )
            }
        }

        return calendars
    }
}

/**
 * Data class for calendar account
 */
data class CalendarAccount(
    val id: Long,
    val displayName: String,
    val accountName: String,
    val isPrimary: Boolean
)

/**
 * Extension function to check if calendar app is available
 */
fun Context.isCalendarAppAvailable(): Boolean {
    val intent = Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI
    }
    return intent.resolveActivity(packageManager) != null
}
