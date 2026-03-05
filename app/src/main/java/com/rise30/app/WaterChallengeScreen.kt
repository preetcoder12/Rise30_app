package com.rise30.app

import android.content.Context
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.*

// Water theme colors
val WaterBlue = Color(0xFF4FC3F7)
val WaterDark = Color(0xFF0288D1)
val WaterLight = Color(0xFFB3E5FC)

@Composable
fun WaterChallengeScreen(
    userId: String,
    onBack: () -> Unit,
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    
    // State for water tracking
    var currentAmount by remember { mutableStateOf(0f) }
    var targetAmount by remember { mutableStateOf(2.0f) }
    var currentDay by remember { mutableStateOf(1) }
    var totalDays by remember { mutableStateOf(30) }
    var completedDays by remember { mutableStateOf(0) }
    var currentStreak by remember { mutableStateOf(0) }
    var challengeId by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var showAnalytics by remember { mutableStateOf(false) }
    
    // Load initial data
    LaunchedEffect(userId) {
        loadWaterChallengeData(context, userId) { data ->
            currentAmount = data.todayAmount
            targetAmount = data.todayTarget
            currentDay = data.currentDay
            totalDays = data.totalDays
            completedDays = data.completedDays
            currentStreak = data.currentStreak
            challengeId = data.challengeId
        }
    }
    
    val progress = (currentAmount / targetAmount).coerceIn(0f, 1f)
    val remaining = (targetAmount - currentAmount).coerceAtLeast(0f)
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundDark
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onBack) {
                        Text(
                            text = "← Back",
                            color = WaterBlue,
                            fontSize = 16.sp
                        )
                    }
                    
                    Text(
                        text = "Water Challenge",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    TextButton(onClick = { showAnalytics = !showAnalytics }) {
                        Text(
                            text = if (showAnalytics) "Track" else "Stats",
                            color = WaterBlue,
                            fontSize = 14.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                if (showAnalytics) {
                    WaterAnalyticsSection(
                        completedDays = completedDays,
                        currentStreak = currentStreak,
                        currentDay = currentDay,
                        totalDays = totalDays
                    )
                } else {
                    // Progress Card
                    WaterProgressCard(
                        currentAmount = currentAmount,
                        targetAmount = targetAmount,
                        progress = progress,
                        remaining = remaining,
                        currentDay = currentDay,
                        totalDays = totalDays
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Quick Add Buttons - Only allow adding if target not reached
                    QuickAddSection(
                        currentAmount = currentAmount,
                        targetAmount = targetAmount,
                        onAddWater = { amount ->
                            scope.launch {
                                // Check if adding would exceed target
                                val potentialTotal = currentAmount + amount
                                val actualAmount = if (potentialTotal > targetAmount) {
                                    targetAmount - currentAmount // Only add what's needed to reach target
                                } else {
                                    amount
                                }
                                
                                if (actualAmount > 0) {
                                    addWater(
                                        context = context,
                                        userId = userId, 
                                        challengeId = challengeId, 
                                        amount = actualAmount,
                                        currentAmount = currentAmount,
                                        targetAmount = targetAmount,
                                        completedDays = completedDays
                                    ) { updatedAmount, _ ->
                                        currentAmount = updatedAmount
                                        // If target reached, update completed days
                                        if (updatedAmount >= targetAmount && currentAmount < targetAmount) {
                                            completedDays = (completedDays + 1).coerceAtMost(totalDays)
                                        }
                                    }
                                }
                            }
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Daily Progress Grid
                    DailyProgressGrid(
                        currentDay = currentDay,
                        completedDays = completedDays
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Streak Card
                    StreakCard(currentStreak)
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Tips Card
                    WaterTipsCard()
                }
                
                Spacer(modifier = Modifier.height(96.dp))
            }
            
            HomeFloatingBottomBar(
                currentTab = currentTab,
                onTabSelected = onTabSelected
            )
        }
    }
}

@Composable
private fun WaterProgressCard(
    currentAmount: Float,
    targetAmount: Float,
    progress: Float,
    remaining: Float,
    currentDay: Int,
    totalDays: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Day $currentDay of $totalDays",
                color = Color.Gray,
                fontSize = 14.sp
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Animated Water Circle
            Box(
                modifier = Modifier.size(180.dp),
                contentAlignment = Alignment.Center
            ) {
                // Background circle
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = Color(0xFF1E3A5F),
                        style = Stroke(width = 12.dp.toPx())
                    )
                }
                
                // Progress arc
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawArc(
                        color = WaterBlue,
                        startAngle = -90f,
                        sweepAngle = 360f * progress,
                        useCenter = false,
                        style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                
                // Water wave animation effect
                val infiniteTransition = rememberInfiniteTransition(label = "wave")
                val waveOffset by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(2000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "wave"
                )
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = String.format("%.1f", currentAmount),
                        color = Color.White,
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "/ ${String.format("%.1f", targetAmount)} L",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        color = WaterBlue,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            if (remaining > 0) {
                Text(
                    text = "${String.format("%.1f", remaining)} L more to reach your goal",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            } else {
                Text(
                    text = "🎉 Goal reached! Great job!",
                    color = WaterBlue,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun QuickAddSection(
    currentAmount: Float,
    targetAmount: Float,
    onAddWater: (Float) -> Unit
) {
    val isTargetReached = currentAmount >= targetAmount
    val remaining = (targetAmount - currentAmount).coerceAtLeast(0f)
    
    Column {
        Text(
            text = "Quick Add",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        
        if (isTargetReached) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "✅ Daily goal reached! Great job!",
                color = WaterBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        } else {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${String.format("%.2f", remaining)} L remaining to reach goal",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            WaterAddButton(
                amount = 0.25f,
                label = "250ml",
                onClick = { onAddWater(0.25f) },
                enabled = !isTargetReached,
                modifier = Modifier.weight(1f)
            )
            WaterAddButton(
                amount = 0.5f,
                label = "500ml",
                onClick = { onAddWater(0.5f) },
                enabled = !isTargetReached,
                modifier = Modifier.weight(1f)
            )
            WaterAddButton(
                amount = 1.0f,
                label = "1 Liter",
                onClick = { onAddWater(1.0f) },
                enabled = !isTargetReached && remaining >= 1.0f,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun WaterAddButton(
    amount: Float,
    label: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(80.dp)
            .clickable(enabled = enabled, onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (enabled) Color(0xFF1E3A5F) else Color(0xFF2A2A30)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "💧",
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                color = if (enabled) Color.White else Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun DailyProgressGrid(
    currentDay: Int,
    completedDays: Int
) {
    Column {
        Text(
            text = "30-Day Progress",
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
                // Show 10 days per row, 3 rows
                for (row in 0..2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (col in 0..9) {
                            val day = row * 10 + col + 1
                            if (day <= 30) {
                                val isCompleted = day <= completedDays
                                val isCurrent = day == currentDay
                                
                                DayIndicator(
                                    day = day,
                                    isCompleted = isCompleted,
                                    isCurrent = isCurrent
                                )
                            }
                        }
                    }
                    if (row < 2) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun DayIndicator(
    day: Int,
    isCompleted: Boolean,
    isCurrent: Boolean
) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(
                when {
                    isCurrent -> WaterBlue
                    isCompleted -> WaterDark
                    else -> Color(0xFF2A2A30)
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            color = when {
                isCurrent || isCompleted -> Color.Black
                else -> Color.Gray
            },
            fontSize = 11.sp,
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun StreakCard(streak: Int) {
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
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(WaterBlue, WaterDark)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🔥",
                    fontSize = 28.sp
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = "$streak Day Streak",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (streak > 0) "Keep it up! You're doing great!" else "Start your streak today!",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun WaterTipsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "💡 Hydration Tips",
                color = WaterBlue,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            val tips = listOf(
                "Drink a glass of water first thing in the morning",
                "Carry a reusable water bottle with you",
                "Drink water before, during, and after exercise",
                "Eat water-rich foods like cucumber and watermelon"
            )
            
            tips.forEach { tip ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "•",
                        color = WaterBlue,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = tip,
                        color = Color.LightGray,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun WaterAnalyticsSection(
    completedDays: Int,
    currentStreak: Int,
    currentDay: Int,
    totalDays: Int
) {
    val completionRate = if (currentDay > 0) (completedDays.toFloat() / currentDay * 100) else 0f
    
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
                        value = "$completedDays",
                        label = "Days Completed",
                        color = WaterBlue
                    )
                    StatItem(
                        value = "$currentStreak",
                        label = "Current Streak",
                        color = Color(0xFFFFA726)
                    )
                    StatItem(
                        value = "${completionRate.toInt()}%",
                        label = "Success Rate",
                        color = Color(0xFF66BB6A)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Weekly Progress
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CardDark),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Last 7 Days",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Bar chart representation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    val days = listOf("M", "T", "W", "T", "F", "S", "S")
                    val values = listOf(0.8f, 1.0f, 0.6f, 1.0f, 0.4f, 1.0f, 0.5f)
                    
                    days.forEachIndexed { index, day ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .width(24.dp)
                                    .height(80.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF2A2A30))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(values[index])
                                        .align(Alignment.BottomCenter)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (values[index] >= 1.0f) WaterBlue else WaterDark.copy(alpha = 0.6f)
                                        )
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = day,
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Total Water Consumed
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = WaterBlue.copy(alpha = 0.2f)
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "💧",
                    fontSize = 40.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${completedDays * 2} Liters",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Total water consumed",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun StatItem(
    value: String,
    label: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = color,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

// API Client - shared across the app
val httpClient = HttpClient(Android) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
}

// Base URL for backend API - update with your production URL
const val BASE_URL = "https://your-backend-url.com" // Replace with your backend URL

// Data classes for API
@Serializable
data class WaterChallengeResponse(
    val active: Boolean,
    val challenge: ChallengeData? = null,
    val progress: ProgressData? = null
)

@Serializable
data class ChallengeData(
    val id: String,
    val name: String,
    val type: String,
    val duration: Int,
    val startDate: String,
    val endDate: String,
    val targetValue: Float? = null,
    val unit: String? = null
)

@Serializable
data class ProgressData(
    val currentDay: Int,
    val totalDays: Int,
    val completedDays: Int,
    val currentStreak: Int,
    val todayAmount: Float,
    val todayTarget: Float,
    val todayCompleted: Boolean
)

@Serializable
data class LogWaterRequest(
    val userId: String,
    val challengeId: String,
    val amount: Float
)

@Serializable
data class LogWaterResponse(
    val success: Boolean,
    val waterEntry: WaterEntryData? = null,
    val remaining: Float? = null,
    val progress: Float? = null
)

@Serializable
data class WaterEntryData(
    val id: String,
    val userId: String,
    val challengeId: String,
    val date: String,
    val amount: Float,
    val targetAmount: Float,
    val completed: Boolean
)

@Serializable
data class AnalyticsResponse(
    val summary: SummaryData,
    val weeklyData: List<WeeklyData>,
    val last7Days: List<DailyData>
)

@Serializable
data class SummaryData(
    val totalDays: Int,
    val completedDays: Int,
    val completionRate: Float,
    val totalLiters: Float,
    val averageDaily: Float,
    val currentStreak: Int,
    val longestStreak: Int
)

@Serializable
data class WeeklyData(
    val week: Int,
    val daysCompleted: Int,
    val totalLiters: Float
)

@Serializable
data class DailyData(
    val date: String,
    val amount: Float,
    val completed: Boolean,
    val target: Float
)

// Data classes and helper functions
private data class WaterChallengeData(
    val todayAmount: Float,
    val todayTarget: Float,
    val currentDay: Int,
    val totalDays: Int,
    val completedDays: Int,
    val currentStreak: Int,
    val challengeId: String? = null
)

// Persistent storage keys
private const val PREFS_NAME = "WaterChallengePrefs"
private const val KEY_WATER_AMOUNT = "water_amount_"
private const val KEY_COMPLETED_DAYS = "completed_days_"
private const val KEY_LAST_DATE = "last_date_"

// Helper functions for SharedPreferences
private fun getPrefs(context: Context) = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

private fun saveWaterData(context: Context, userId: String, amount: Float, completedDays: Int) {
    val prefs = getPrefs(context)
    val editor = prefs.edit()
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    
    editor.putFloat(KEY_WATER_AMOUNT + userId, amount)
    editor.putInt(KEY_COMPLETED_DAYS + userId, completedDays)
    editor.putString(KEY_LAST_DATE + userId, today)
    editor.apply()
}

private fun loadWaterData(context: Context, userId: String): Triple<Float, Int, String?> {
    val prefs = getPrefs(context)
    val amount = prefs.getFloat(KEY_WATER_AMOUNT + userId, 0f)
    val completedDays = prefs.getInt(KEY_COMPLETED_DAYS + userId, 0)
    val lastDate = prefs.getString(KEY_LAST_DATE + userId, null)
    return Triple(amount, completedDays, lastDate)
}

private fun isNewDay(context: Context, userId: String): Boolean {
    val prefs = getPrefs(context)
    val lastDate = prefs.getString(KEY_LAST_DATE + userId, null)
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    return lastDate != today
}

private fun resetDailyProgress(context: Context, userId: String) {
    val prefs = getPrefs(context)
    val editor = prefs.edit()
    editor.putFloat(KEY_WATER_AMOUNT + userId, 0f)
    editor.putString(KEY_LAST_DATE + userId, SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
    editor.apply()
}

private suspend fun loadWaterChallengeData(
    context: Context,
    userId: String,
    onDataLoaded: (WaterChallengeData) -> Unit
) {
    // Check if it's a new day - if so, reset daily progress
    if (isNewDay(context, userId)) {
        resetDailyProgress(context, userId)
    }
    
    // Load from persistent storage
    val (localAmount, localCompleted, _) = loadWaterData(context, userId)
    
    try {
        val response: WaterChallengeResponse = httpClient.get("$BASE_URL/water-challenge/active/$userId").body()
        
        if (response.active && response.progress != null) {
            // Use server data, but prefer local if local is higher (unsynced data)
            val serverTodayAmount = response.progress.todayAmount
            val finalTodayAmount = maxOf(serverTodayAmount, localAmount)
            
            onDataLoaded(
                WaterChallengeData(
                    todayAmount = finalTodayAmount,
                    todayTarget = response.progress.todayTarget,
                    currentDay = response.progress.currentDay,
                    totalDays = response.progress.totalDays,
                    completedDays = maxOf(response.progress.completedDays, localCompleted),
                    currentStreak = response.progress.currentStreak,
                    challengeId = response.challenge?.id
                )
            )
        } else {
            // No active challenge on server - use local data
            onDataLoaded(
                WaterChallengeData(
                    todayAmount = localAmount,
                    todayTarget = 2.0f,
                    currentDay = 1,
                    totalDays = 30,
                    completedDays = localCompleted,
                    currentStreak = if (localAmount >= 2.0f) 1 else 0,
                    challengeId = null
                )
            )
        }
    } catch (e: Exception) {
        // API failed - use persistent local storage
        onDataLoaded(
            WaterChallengeData(
                todayAmount = localAmount,
                todayTarget = 2.0f,
                currentDay = 1,
                totalDays = 30,
                completedDays = localCompleted,
                currentStreak = if (localAmount >= 2.0f) 1 else 0,
                challengeId = null
            )
        )
    }
}

private suspend fun createWaterChallenge(
    userId: String,
    onSuccess: (WaterChallengeData) -> Unit
) {
    try {
        val response: WaterChallengeResponse = httpClient.post("$BASE_URL/water-challenge/create") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("userId" to userId))
        }.body()
        
        if (response.active && response.progress != null) {
            onSuccess(
                WaterChallengeData(
                    todayAmount = response.progress.todayAmount,
                    todayTarget = response.progress.todayTarget,
                    currentDay = response.progress.currentDay,
                    totalDays = response.progress.totalDays,
                    completedDays = response.progress.completedDays,
                    currentStreak = response.progress.currentStreak,
                    challengeId = response.challenge?.id
                )
            )
        } else {
            // New challenge created - start at Day 1
            onSuccess(
                WaterChallengeData(
                    todayAmount = 0f,
                    todayTarget = 2.0f,
                    currentDay = 1,
                    totalDays = 30,
                    completedDays = 0,
                    currentStreak = 0,
                    challengeId = response.challenge?.id
                )
            )
        }
    } catch (e: Exception) {
        // Fallback for new user
        onSuccess(
            WaterChallengeData(
                todayAmount = 0f,
                todayTarget = 2.0f,
                currentDay = 1,
                totalDays = 30,
                completedDays = 0,
                currentStreak = 0,
                challengeId = null
            )
        )
    }
}

private suspend fun addWater(
    context: Context,
    userId: String,
    challengeId: String?,
    amount: Float,
    currentAmount: Float,
    targetAmount: Float,
    completedDays: Int,
    onSuccess: (Float, Float) -> Unit
) {
    // Calculate new total
    val newTotal = currentAmount + amount
    
    // Calculate new completed days
    val newCompletedDays = if (newTotal >= targetAmount && currentAmount < targetAmount) {
        completedDays + 1
    } else {
        completedDays
    }
    
    // Save to persistent storage immediately
    saveWaterData(context, userId, newTotal, newCompletedDays)
    
    if (challengeId == null) {
        onSuccess(newTotal, newTotal / targetAmount)
        return
    }
    
    try {
        val response: LogWaterResponse = httpClient.post("$BASE_URL/water-challenge/log") {
            contentType(ContentType.Application.Json)
            setBody(LogWaterRequest(userId, challengeId, amount))
        }.body()
        
        if (response.success && response.waterEntry != null) {
            // Update local storage with server-confirmed data
            saveWaterData(context, userId, response.waterEntry.amount, newCompletedDays)
            onSuccess(response.waterEntry.amount, (response.progress ?: 0f) / 100f)
        } else {
            onSuccess(newTotal, newTotal / targetAmount)
        }
    } catch (e: Exception) {
        // API failed but local storage is already updated
        onSuccess(newTotal, newTotal / targetAmount)
    }
}

private suspend fun loadAnalytics(
    userId: String,
    challengeId: String?,
    onDataLoaded: (AnalyticsResponse) -> Unit
) {
    if (challengeId == null) return
    
    try {
        val response: AnalyticsResponse = httpClient.get("$BASE_URL/water-challenge/analytics/$userId/$challengeId").body()
        onDataLoaded(response)
    } catch (e: Exception) {
        // Fallback with mock data
        onDataLoaded(
            AnalyticsResponse(
                summary = SummaryData(
                    totalDays = 5,
                    completedDays = 4,
                    completionRate = 80f,
                    totalLiters = 8f,
                    averageDaily = 1.6f,
                    currentStreak = 4,
                    longestStreak = 4
                ),
                weeklyData = listOf(
                    WeeklyData(1, 4, 8f),
                    WeeklyData(2, 0, 0f),
                    WeeklyData(3, 0, 0f),
                    WeeklyData(4, 0, 0f)
                ),
                last7Days = listOf(
                    DailyData("2026-03-01", 2.0f, true, 2.0f),
                    DailyData("2026-03-02", 2.0f, true, 2.0f),
                    DailyData("2026-03-03", 2.0f, true, 2.0f),
                    DailyData("2026-03-04", 2.0f, true, 2.0f),
                    DailyData("2026-03-05", 0.5f, false, 2.0f),
                    DailyData("2026-03-06", 0f, false, 2.0f),
                    DailyData("2026-03-07", 0f, false, 2.0f)
                )
            )
        )
    }
}
