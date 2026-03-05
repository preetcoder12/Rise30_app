package com.rise30.app

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

@Serializable
data class ChallengeDetail(
    val id: String,
    val name: String,
    val description: String?,
    val type: String,
    val category: String,
    val duration: Int,
    val color: String?,
    val icon: String?,
    val isActive: Boolean,
    val targetValue: Float?,
    val unit: String?
)

@Serializable
data class DayEntry(
    val dayNumber: Int,
    val date: String,
    val completed: Boolean,
    val completedAt: String?,
    val notes: String?,
    val value: Float?
)

@Serializable
data class ChallengeProgress(
    val completedDays: Int,
    val totalDays: Int,
    val percentage: Int,
    val currentStreak: Int,
    val currentDay: Int
)

@Serializable
data class ChallengeDetailResponse(
    val challenge: ChallengeDetail,
    val progress: ChallengeProgress
)

@Serializable
data class ChallengeDetailApiResponse(
    val success: Boolean,
    val challenge: ChallengeDetailWithTasks
)

@Serializable
data class ChallengeDetailWithTasks(
    val id: String,
    val name: String,
    val description: String?,
    val type: String,
    val category: String,
    val duration: Int,
    val color: String?,
    val icon: String?,
    val isActive: Boolean,
    val targetValue: Float?,
    val unit: String?,
    val dailyTasks: List<DailyTask>,
    val progress: ChallengeProgress
)

@Serializable
data class DailyTask(
    val id: String,
    val dayNumber: Int,
    val date: String,
    val completed: Boolean,
    val completedAt: String?,
    val notes: String?,
    val value: Float?
)

@Composable
fun ChallengeDetailScreen(
    userId: String,
    challengeId: String,
    onBack: () -> Unit,
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    
    var challenge by remember { mutableStateOf<ChallengeDetail?>(null) }
    var progress by remember { mutableStateOf<ChallengeProgress?>(null) }
    var dayEntries by remember { mutableStateOf<List<DayEntry>>(emptyList()) }
    var selectedDay by remember { mutableStateOf<DayEntry?>(null) }
    var showDayDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var showAnalytics by remember { mutableStateOf(false) }
    
    // Load challenge data
    LaunchedEffect(challengeId) {
        loadChallengeDetail(userId, challengeId) { detail, prog, entries ->
            challenge = detail
            progress = prog
            dayEntries = entries
            isLoading = false
        }
    }
    
    val challengeColor = remember(challenge?.color) {
        challenge?.color?.let { Color(android.graphics.Color.parseColor(it)) } ?: Accent
    }
    
    val challengeIcon = challenge?.icon ?: "🎯"
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundDark
    ) {
        Box {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = challengeColor)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header
                    ChallengeDetailHeader(
                        challengeName = challenge?.name ?: "",
                        challengeIcon = challengeIcon,
                        challengeColor = challengeColor,
                        onBack = onBack,
                        onToggleAnalytics = { showAnalytics = !showAnalytics }
                    )
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    if (showAnalytics) {
                        ChallengeAnalyticsSection(
                            progress = progress,
                            challengeColor = challengeColor
                        )
                    } else {
                        // Progress Overview
                        ChallengeProgressOverview(
                            progress = progress,
                            challengeColor = challengeColor,
                            challengeIcon = challengeIcon,
                            onMarkToday = {
                                scope.launch {
                                    markTodayComplete(userId, challengeId) { updatedProgress, updatedEntries ->
                                        progress = updatedProgress
                                        dayEntries = updatedEntries
                                    }
                                }
                            }
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // 30-Day Grid
                        ChallengeDayGrid(
                            dayEntries = dayEntries,
                            currentDay = progress?.currentDay ?: 1,
                            challengeColor = challengeColor,
                            onDayClick = { day ->
                                selectedDay = day
                                showDayDialog = true
                            }
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Streak Card
                        ChallengeStreakCard(
                            currentStreak = progress?.currentStreak ?: 0,
                            challengeColor = challengeColor
                        )
                        
                        if (!challenge?.description.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            // Description Card
                            ChallengeDescriptionCard(
                                description = challenge?.description ?: "",
                                challengeColor = challengeColor
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(96.dp))
                }
            }
            
            HomeFloatingBottomBar(
                currentTab = currentTab,
                onTabSelected = { selected ->
                    onTabSelected(selected)
                }
            )
        }
    }
    
    // Day Detail Dialog
    if (showDayDialog && selectedDay != null) {
        DayDetailDialog(
            dayEntry = selectedDay!!,
            challengeColor = challengeColor,
            onDismiss = { showDayDialog = false },
            onToggleComplete = { completed ->
                scope.launch {
                    toggleDayComplete(
                        userId = userId,
                        challengeId = challengeId,
                        dayNumber = selectedDay!!.dayNumber,
                        completed = completed
                    ) { updatedEntries, updatedProgress ->
                        dayEntries = updatedEntries
                        progress = updatedProgress
                        showDayDialog = false
                    }
                }
            }
        )
    }
}

@Composable
private fun ChallengeDetailHeader(
    challengeName: String,
    challengeIcon: String,
    challengeColor: Color,
    onBack: () -> Unit,
    onToggleAnalytics: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = onBack) {
            Text(
                text = "← Back",
                color = challengeColor,
                fontSize = 16.sp
            )
        }
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = challengeIcon,
                fontSize = 24.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = challengeName,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
        
        TextButton(onClick = onToggleAnalytics) {
            Text(
                text = "Stats",
                color = challengeColor,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun ChallengeProgressOverview(
    progress: ChallengeProgress?,
    challengeColor: Color,
    challengeIcon: String,
    onMarkToday: () -> Unit
) {
    val percentage = progress?.percentage ?: 0
    val animatedProgress by animateFloatAsState(
        targetValue = percentage / 100f,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "progress"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon and Title
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(challengeColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = challengeIcon,
                    fontSize = 40.sp
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Progress Circle
            Box(
                modifier = Modifier.size(140.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF2A2A30),
                    strokeWidth = 12.dp,
                    trackColor = Color.Transparent
                )
                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.fillMaxSize(),
                    color = challengeColor,
                    strokeWidth = 12.dp,
                    trackColor = Color.Transparent
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$percentage%",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${progress?.completedDays ?: 0}/${progress?.totalDays ?: 30}",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Current Day Info
            Text(
                text = "Day ${progress?.currentDay ?: 1} of ${progress?.totalDays ?: 30}",
                color = Color.Gray,
                fontSize = 16.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Mark Today Button
            Button(
                onClick = onMarkToday,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = challengeColor,
                    contentColor = Color.Black
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "✓ Mark Today Complete",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun ChallengeDayGrid(
    dayEntries: List<DayEntry>,
    currentDay: Int,
    challengeColor: Color,
    onDayClick: (DayEntry) -> Unit
) {
    Column {
        Text(
            text = "30-Day Tracker",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CardDark),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Legend
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    LegendItem(color = challengeColor, label = "Completed")
                    LegendItem(color = Color(0xFF2A2A30), label = "Pending")
                    LegendItem(color = challengeColor.copy(alpha = 0.5f), label = "Today")
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Day Grid - 6 columns x 5 rows
                LazyVerticalGrid(
                    columns = GridCells.Fixed(6),
                    modifier = Modifier.height(300.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(dayEntries) { day ->
                        DayCell(
                            day = day,
                            isToday = day.dayNumber == currentDay,
                            challengeColor = challengeColor,
                            onClick = { onDayClick(day) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    day: DayEntry,
    isToday: Boolean,
    challengeColor: Color,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = when {
            day.completed -> challengeColor
            isToday -> challengeColor.copy(alpha = 0.3f)
            else -> Color(0xFF2A2A30)
        },
        label = "dayBackground"
    )
    
    val scale by animateFloatAsState(
        targetValue = if (isToday) 1.1f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "dayScale"
    )
    
    Box(
        modifier = Modifier
            .size(44.dp)
            .scale(scale)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(
                width = if (isToday) 2.dp else 0.dp,
                color = if (isToday) challengeColor else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (day.completed) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Completed",
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Text(
                text = day.dayNumber.toString(),
                color = if (isToday) challengeColor else Color.Gray,
                fontSize = 14.sp,
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun ChallengeStreakCard(
    currentStreak: Int,
    challengeColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(challengeColor, challengeColor.copy(alpha = 0.7f))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🔥",
                    fontSize = 32.sp
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = "$currentStreak Day Streak",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (currentStreak > 0) 
                        "Keep going! You're building a great habit!" 
                    else 
                        "Start your streak by completing today's task!",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun ChallengeDescriptionCard(
    description: String,
    challengeColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "About This Challenge",
                color = challengeColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = description,
                color = Color.LightGray,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun ChallengeAnalyticsSection(
    progress: ChallengeProgress?,
    challengeColor: Color
) {
    Column {
        // Stats Overview
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CardDark),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Challenge Statistics",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(
                        value = "${progress?.completedDays ?: 0}",
                        label = "Days Done",
                        color = challengeColor
                    )
                    StatItem(
                        value = "${progress?.currentStreak ?: 0}",
                        label = "Streak",
                        color = Color(0xFFFFA726)
                    )
                    StatItem(
                        value = "${progress?.percentage ?: 0}%",
                        label = "Complete",
                        color = Color(0xFF66BB6A)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Motivation Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = challengeColor.copy(alpha = 0.15f)
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "💪",
                    fontSize = 48.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Consistency is Key",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Small daily actions lead to big transformations. Keep showing up!",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun DayDetailDialog(
    dayEntry: DayEntry,
    challengeColor: Color,
    onDismiss: () -> Unit,
    onToggleComplete: (Boolean) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CardDark,
        title = {
            Text(
                text = "Day ${dayEntry.dayNumber}",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = "Date: ${dayEntry.date}",
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (dayEntry.completed) 
                        "✅ Completed!" 
                    else 
                        "⏳ Not completed yet",
                    color = if (dayEntry.completed) Color(0xFF66BB6A) else Color.Gray
                )
                if (!dayEntry.notes.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Notes: ${dayEntry.notes}",
                        color = Color.LightGray
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onToggleComplete(!dayEntry.completed) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (dayEntry.completed) Color(0xFFCF6679) else challengeColor,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = if (dayEntry.completed) "Mark Incomplete" else "Mark Complete"
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Close", color = challengeColor)
            }
        }
    )
}

// API Functions
private suspend fun loadChallengeDetail(
    userId: String,
    challengeId: String,
    onLoaded: (ChallengeDetail, ChallengeProgress, List<DayEntry>) -> Unit
) {
    try {
        val response: ChallengeDetailApiResponse = httpClient.get("$BASE_URL/api/challenges/$challengeId").body()
        
        if (response.success) {
            val challenge = response.challenge
            val progress = response.challenge.progress
            
            // Convert dailyTasks to DayEntry list
            val entries = response.challenge.dailyTasks.map { task ->
                DayEntry(
                    dayNumber = task.dayNumber,
                    date = task.date,
                    completed = task.completed,
                    completedAt = task.completedAt,
                    notes = task.notes,
                    value = task.value
                )
            }
            
            val challengeDetail = ChallengeDetail(
                id = challenge.id,
                name = challenge.name,
                description = challenge.description,
                type = challenge.type,
                category = challenge.category,
                duration = challenge.duration,
                color = challenge.color,
                icon = challenge.icon,
                isActive = challenge.isActive,
                targetValue = challenge.targetValue,
                unit = challenge.unit
            )
            
            val challengeProgress = ChallengeProgress(
                completedDays = progress.completedDays,
                totalDays = progress.totalDays,
                percentage = progress.percentage,
                currentStreak = progress.currentStreak,
                currentDay = progress.currentDay
            )
            
            onLoaded(challengeDetail, challengeProgress, entries)
        }
    } catch (e: Exception) {
        // Fallback to mock data on error
        val mockChallenge = ChallengeDetail(
            id = challengeId,
            name = "30-Day Water Challenge",
            description = "Drink 2 liters of water every day for 30 days to improve your hydration and overall health.",
            type = "water",
            category = "health",
            duration = 30,
            color = "#4FC3F7",
            icon = "💧",
            isActive = true,
            targetValue = 2.0f,
            unit = "liters"
        )
        
        val mockProgress = ChallengeProgress(
            completedDays = 12,
            totalDays = 30,
            percentage = 40,
            currentStreak = 5,
            currentDay = 13
        )
        
        val mockEntries = (1..30).map { day ->
            DayEntry(
                dayNumber = day,
                date = "2026-03-${day.toString().padStart(2, '0')}",
                completed = day <= 12,
                completedAt = if (day <= 12) "2026-03-${day.toString().padStart(2, '0')}T10:00:00Z" else null,
                notes = null,
                value = if (day <= 12) 2.0f else null
            )
        }
        
        onLoaded(mockChallenge, mockProgress, mockEntries)
    }
}

private suspend fun markTodayComplete(
    userId: String,
    challengeId: String,
    onSuccess: (ChallengeProgress, List<DayEntry>) -> Unit
) {
    try {
        val response: HttpResponse = httpClient.post("$BASE_URL/api/challenges/$challengeId/mark-today") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("userId" to userId))
        }
        
        if (response.status.isSuccess()) {
            // Reload challenge details to get updated progress
            loadChallengeDetail(userId, challengeId, onLoaded = { detail, progress, entries ->
                onSuccess(progress, entries)
            })
        }
    } catch (e: Exception) {
        // Fallback to local update on error
        val updatedProgress = ChallengeProgress(
            completedDays = 13,
            totalDays = 30,
            percentage = 43,
            currentStreak = 6,
            currentDay = 13
        )
        
        val updatedEntries = (1..30).map { day ->
            DayEntry(
                dayNumber = day,
                date = "2026-03-${day.toString().padStart(2, '0')}",
                completed = day <= 13,
                completedAt = if (day <= 13) "2026-03-${day.toString().padStart(2, '0')}T10:00:00Z" else null,
                notes = null,
                value = if (day <= 13) 2.0f else null
            )
        }
        
        onSuccess(updatedProgress, updatedEntries)
    }
}

private suspend fun toggleDayComplete(
    userId: String,
    challengeId: String,
    dayNumber: Int,
    completed: Boolean,
    onSuccess: (List<DayEntry>, ChallengeProgress) -> Unit
) {
    try {
        val response: HttpResponse = httpClient.post("$BASE_URL/api/challenges/$challengeId/day/$dayNumber/toggle") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("userId" to userId, "completed" to completed))
        }
        
        if (response.status.isSuccess()) {
            // Reload challenge details to get updated progress
            loadChallengeDetail(userId, challengeId, onLoaded = { detail, progress, entries ->
                onSuccess(entries, progress)
            })
        }
    } catch (e: Exception) {
        // Fallback to local update on error
        val updatedProgress = ChallengeProgress(
            completedDays = if (completed) 13 else 11,
            totalDays = 30,
            percentage = if (completed) 43 else 37,
            currentStreak = if (completed) 6 else 4,
            currentDay = 13
        )
        
        val updatedEntries = (1..30).map { day ->
            DayEntry(
                dayNumber = day,
                date = "2026-03-${day.toString().padStart(2, '0')}",
                completed = if (day == dayNumber) completed else day <= 12,
                completedAt = if (day == dayNumber && completed) "2026-03-${day.toString().padStart(2, '0')}T10:00:00Z" 
                             else if (day <= 12 && day != dayNumber) "2026-03-${day.toString().padStart(2, '0')}T10:00:00Z"
                             else null,
                notes = null,
                value = if (day == dayNumber && completed) 2.0f else if (day <= 12 && day != dayNumber) 2.0f else null
            )
        }
        
        onSuccess(updatedEntries, updatedProgress)
    }
}
