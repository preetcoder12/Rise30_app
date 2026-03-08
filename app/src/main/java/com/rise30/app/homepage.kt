package com.rise30.app

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt
import androidx.compose.ui.platform.LocalContext
import io.ktor.client.request.post
import io.ktor.client.request.setBody

// 🌑 Premium Colors
val BackgroundDark = Color(0xFF0D0D0F)
val CardDark = Color(0xFF1A1A1F)
val Accent = Color(0xFFFFD54F)
val AccentSoft = Color(0x33FFD54F)

// Data classes for API
@Serializable
data class AnalyticsResponse(
    val success: Boolean,
    val analytics: AnalyticsData
)

@Serializable
data class AnalyticsData(
    val weeklyProgress: List<WeeklyProgressItem>,
    val completionRate: Int,
    val categoryBreakdown: Map<String, CategoryStat>,
    val totalChallenges: Int,
    val activeChallenges: Int,
    val completedChallenges: Int,
    val longestStreak: Int
)

@Serializable
data class WeeklyProgressItem(
    val date: String,
    val completed: Int,
    val total: Int
)

@Serializable
data class CategoryStat(
    val count: Int,
    val completed: Int
)

// Using ChallengeSummary and ChallengesResponse from challenges.kt

@Composable
private fun TopSection(
    userName: String,
    onNotificationClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp, bottom = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome back,",
                color = Color.Gray,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = userName,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        IconButton(
            onClick = onNotificationClick,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(CardDark.copy(alpha = 0.5f))
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = Accent,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun MainChallengeCard(
    challenge: ChallengeApiData?,
    onMarkComplete: () -> Unit
) {
    val currentDay = challenge?.progress?.completedDays ?: 0
    val totalDays = challenge?.progress?.totalDays ?: challenge?.duration ?: 30
    val progress = remember(currentDay, totalDays) {
        (currentDay.coerceAtLeast(0).coerceAtMost(totalDays).toFloat() / totalDays.coerceAtLeast(1))
    }
    
    val challengeColor = remember(challenge?.color) {
        try {
            Color(android.graphics.Color.parseColor(challenge?.color ?: "#FFD54F"))
        } catch (e: Exception) {
            Accent
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = CardDark
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 10.dp.toPx()
                    val radius = (size.minDimension - strokeWidth) / 2

                    // Background circle
                    drawCircle(
                        color = challengeColor.copy(alpha = 0.2f),
                        style = Stroke(width = strokeWidth)
                    )

                    // Progress arc
                    drawArc(
                        color = challengeColor,
                        startAngle = -90f,
                        sweepAngle = 360f * progress,
                        useCenter = false,
                        style = Stroke(width = strokeWidth)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${(progress * 100).roundToInt()}%",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Day $currentDay/$totalDays",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(18.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = challenge?.name ?: "No Active Challenge",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = challenge?.description ?: "Create a challenge to start your journey!",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    maxLines = 2
                )
                
                // Streak info row
                val streakLength = challenge?.progress?.currentStreak ?: 0
                if (streakLength > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = Accent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$streakLength day streak",
                            color = Accent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                val isTodayCompleted = challenge?.progress?.isTodayCompleted ?: false
                
                Button(
                    onClick = onMarkComplete,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isTodayCompleted) Color(0xFF4CAF50) else challengeColor,
                        contentColor = Color.Black
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    enabled = challenge != null && !isTodayCompleted
                ) {
                    Text(
                        text = when {
                            challenge == null -> "Create Challenge"
                            isTodayCompleted -> "Daily Task Completed ✓"
                            else -> "Mark Complete Today"
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun WeeklyOverview(userId: String) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var analyticsData by remember { mutableStateOf<AnalyticsData?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    
    LaunchedEffect(userId) {
        val cached = CacheManager.getAnalytics(context, userId)
        if (cached != null) {
            analyticsData = cached.analytics
            isLoading = false
        }

        scope.launch {
            try {
                val response: AnalyticsResponse = httpClient.get("$BASE_URL/api/users/$userId/analytics").body()
                if (response.success) {
                    analyticsData = response.analytics
                    CacheManager.saveAnalytics(context, userId, response)
                }
            } catch (e: Exception) {
                // Use empty data on error
            }
            isLoading = false
        }
    }

    val weeklyData = analyticsData?.weeklyProgress ?: emptyList()
    val completedCount = weeklyData.count { it.completed > 0 }
    val totalCount = weeklyData.size.coerceAtLeast(7)
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(CardDark)
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Weekly Overview",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Your consistency this week",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$completedCount/$totalCount days",
                    color = Accent,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "This Week",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        
        // Informative stats row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoBox(label = "Longest Streak", value = "${analyticsData?.longestStreak ?: 0} days")
            InfoBox(label = "In Progress", value = "${analyticsData?.activeChallenges ?: 0} challenges")
            InfoBox(label = "Overall Journey", value = "${analyticsData?.completionRate ?: 0}%")
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Accent, strokeWidth = 3.dp)
            }
        } else {
            val dayLabels = listOf("M", "T", "W", "T", "F", "S", "S")
            val todayIndex = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK).let {
                // Calendar.MONDAY is 2, SUNDAY is 1. We want M=0, T=1, ..., S=6.
                if (it == java.util.Calendar.SUNDAY) 6 else it - 2
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                dayLabels.forEachIndexed { index, label ->
                    val item = weeklyData.getOrNull(index)
                    val progress = if (item != null && item.total > 0) {
                        item.completed.toFloat() / item.total.toFloat()
                    } else 0f
                    
                    val isToday = index == todayIndex
                    
                    // Animate the bar height
                    val animatedProgress by animateFloatAsState(
                        targetValue = progress,
                        animationSpec = tween(durationMillis = 1000, delayMillis = index * 100, easing = FastOutSlowInEasing),
                        label = "barHeight"
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(32.dp)
                                .height(100.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isToday) Color.White.copy(alpha = 0.05f) else Color.Transparent),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            // Bar Track
                            Box(
                                modifier = Modifier
                                    .width(10.dp)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(50))
                                    .background(Color.White.copy(alpha = 0.05f))
                            )
                            
                            // Progress Fill
                            Box(
                                modifier = Modifier
                                    .width(10.dp)
                                    .fillMaxHeight(animatedProgress.coerceIn(0.001f, 1f))
                                    .clip(RoundedCornerShape(50))
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = if (isToday) {
                                                listOf(Accent, Accent.copy(alpha = 0.7f))
                                            } else {
                                                listOf(Accent.copy(alpha = 0.8f), Accent.copy(alpha = 0.4f))
                                            }
                                        )
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (isToday) Accent.copy(alpha = 0.5f) else Color.Transparent,
                                        shape = RoundedCornerShape(50)
                                    )
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            text = label,
                            color = if (isToday) Accent else Color.Gray.copy(alpha = 0.7f),
                            fontSize = 12.sp,
                            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoBox(label: String, value: String) {
    Column(modifier = Modifier.padding(horizontal = 4.dp)) {
        Text(
            text = value,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun MotivationCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = CardDark
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "💡 Today’s Shift",
                color = Accent,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "You don’t need more time, you need more intentional time. One deep, distraction-free block today moves you closer to the identity you want.",
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun HomeFloatingBottomBar(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Glassmorphism background effect
        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = 8.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(72.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2A2A35).copy(alpha = 0.95f),
                            Color(0xFF1A1A25).copy(alpha = 0.98f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(28.dp)
                )
        ) {
            // Subtle top highlight line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Accent.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        )
                    )
                    .align(Alignment.TopCenter)
            )
            
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomBarItem(
                    icon = "🏠",
                    label = "Home",
                    tab = MainTab.Home,
                    currentTab = currentTab,
                    onTabSelected = onTabSelected
                )
                BottomBarItem(
                    icon = "🎯",
                    label = "Challenges",
                    tab = MainTab.Challenges,
                    currentTab = currentTab,
                    onTabSelected = onTabSelected
                )
                BottomBarItem(
                    icon = "👤",
                    label = "Profile",
                    tab = MainTab.Profile,
                    currentTab = currentTab,
                    onTabSelected = onTabSelected
                )
            }
        }
    }
}

@Composable
private fun BottomBarItem(
    icon: String,
    label: String,
    tab: MainTab,
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    val selected = currentTab == tab
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.1f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )
    
    Box(
        modifier = Modifier
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onTabSelected(tab) }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon container with background when selected
            Box(
                modifier = Modifier
                    .size(if (selected) 36.dp else 32.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .then(
                        if (selected) {
                            Modifier.background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Accent.copy(alpha = 0.25f),
                                        Accent.copy(alpha = 0.1f)
                                    )
                                )
                            )
                        } else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icon,
                    fontSize = if (selected) 20.sp else 18.sp
                )
            }
            
            Spacer(modifier = Modifier.height(2.dp))
            
            Text(
                text = label,
                color = if (selected) Accent else Color.Gray.copy(alpha = 0.7f),
                fontSize = 11.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                letterSpacing = 0.3.sp
            )
            
            // Active indicator dot
            AnimatedVisibility(
                visible = selected,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(Accent)
                )
            }
        }
    }
}

@Composable
fun HomePage(
    userId: String,
    userName: String,
    onMarkComplete: () -> Unit,
    onNotificationClick: () -> Unit,
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    
    // State for user's most recent challenge
    var recentChallenge by remember { mutableStateOf<ChallengeApiData?>(null) }
    var isLoadingChallenge by remember { mutableStateOf(true) }
    var refreshTrigger by remember { mutableStateOf(0) }
    
    // Load user's challenges
    LaunchedEffect(userId, refreshTrigger) {
        // Try Cache First
        val cached = CacheManager.getChallenges(context, userId)
        if (cached != null && cached.isNotEmpty()) {
            // Map ChallengeSummary back to ChallengeApiData-like structure for the UI
            val summary = cached.firstOrNull { it.isActive } ?: cached.firstOrNull()
            if (summary != null) {
                recentChallenge = ChallengeApiData(
                    id = summary.id,
                    name = summary.name,
                    description = summary.description,
                    type = summary.type,
                    category = summary.category,
                    duration = summary.duration,
                    color = summary.color,
                    icon = summary.icon,
                    isActive = summary.isActive,
                    progress = ChallengeProgressData(
                        completedDays = summary.completedDays,
                        totalDays = summary.duration,
                        percentage = summary.progress,
                        currentStreak = summary.currentStreak,
                        isTodayCompleted = summary.isTodayCompleted,
                        currentDayNumber = summary.currentDayNumber
                    )
                )
                isLoadingChallenge = false
            }
        }

        scope.launch {
            try {
                val response: ChallengesResponse = httpClient.get("$BASE_URL/api/challenges/user/$userId").body()
                if (response.success && response.challenges.isNotEmpty()) {
                    // Update Cache 
                    val challengesSummary = response.challenges.map { c ->
                        ChallengeSummary(
                            id = c.id,
                            name = c.name,
                            description = c.description,
                            type = c.type,
                            category = c.category,
                            duration = c.duration,
                            color = c.color ?: "#4FC3F7",
                            icon = c.icon ?: "🎯",
                            progress = c.progress.percentage,
                            completedDays = c.progress.completedDays,
                            currentStreak = c.progress.currentStreak,
                            isActive = c.isActive,
                            isTodayCompleted = c.progress.isTodayCompleted,
                            currentDayNumber = c.progress.currentDayNumber
                        )
                    }
                    CacheManager.saveChallenges(context, userId, challengesSummary)

                    // Get the first active challenge, or the first challenge if none are active
                    recentChallenge = response.challenges
                        .firstOrNull { it.isActive } 
                        ?: response.challenges.firstOrNull()
                }
            } catch (e: Exception) {
                // Keep null on error
            }
            isLoadingChallenge = false
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundDark
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {

            TopSection(userName, onNotificationClick)

            Spacer(modifier = Modifier.height(30.dp))



            MainChallengeCard(
                challenge = recentChallenge,
                onMarkComplete = {
                    recentChallenge?.let { challenge ->
                        scope.launch {
                            try {
                                val response: HttpResponse = httpClient.post("$BASE_URL/api/challenges/${challenge.id}/mark-today") {
                                    contentType(ContentType.Application.Json)
                                    setBody(mapOf("userId" to userId))
                                }
                                if (response.status.isSuccess()) {
                                    refreshTrigger++
                                    onMarkComplete()
                                }
                            } catch (e: Exception) {
                                // Silent catch
                            }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(30.dp))

            WeeklyOverview(userId)

            Spacer(modifier = Modifier.height(30.dp))

            PowerMorningSection()

            Spacer(modifier = Modifier.height(30.dp))

            FriendsSection()

            Spacer(modifier = Modifier.height(30.dp))

            StreakShieldSection()

            Spacer(modifier = Modifier.height(30.dp))

            DailyDeepDiveSection()

            Spacer(modifier = Modifier.height(30.dp))

            ProgressProjectionSection()

            Spacer(modifier = Modifier.height(30.dp))

            MotivationCard()

            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

////////////////////////////////////////////////////////////
//////////////// POWER MORNING //////////////////////////////
////////////////////////////////////////////////////////////

@Composable
private fun PowerMorningSection() {

    var habits by remember {
        mutableStateOf(
            listOf(false, false, false)
        )
    }

    Column {
        Text(
            "🔥 Power Morning Ritual",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        listOf(
            "Drink 1 Glass of Water",
            "5 Min Meditation",
            "No Social Media (1h)"
        ).forEachIndexed { index, text ->

            val completed = habits[index]

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(CardDark)
                    .clickable {
                        habits = habits.toMutableList().also {
                            it[index] = !it[index]
                        }
                    }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(if (completed) Accent else Color.Gray)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

////////////////////////////////////////////////////////////
//////////////// FRIENDS SECTION ////////////////////////////
////////////////////////////////////////////////////////////

@Composable
private fun FriendsSection() {

    Column {

        Text(
            "👥 Friends in the Trenches",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "542 others are focusing right now",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            repeat(10) {
                Box(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(CardDark),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color.Green)
                            .align(Alignment.TopEnd)
                    )
                }
            }
        }
    }
}

////////////////////////////////////////////////////////////
//////////////// STREAK SHIELD //////////////////////////////
////////////////////////////////////////////////////////////

@Composable
private fun StreakShieldSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(CardDark)
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "🛡 Streak Shield",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Accent
            )
            
            Text(
                "Active",
                color = Color.Green.copy(alpha = 0.8f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color.Green.copy(alpha = 0.1f))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Your streak is currently protected from a 1-day lapse. Keep the momentum going!",
            color = Color.Gray,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        LinearProgressIndicator(
            progress = { 0.85f },
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(50)),
            color = Accent,
            trackColor = Color.White.copy(alpha = 0.05f)
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Shield Integrity", color = Color.Gray, fontSize = 12.sp)
            Text("85%", color = Accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

////////////////////////////////////////////////////////////
//////////////// DAILY DEEP DIVE ////////////////////////////
////////////////////////////////////////////////////////////

@Composable
private fun DailyDeepDiveSection() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF2A2A2E), Color(0xFF1C1C22))
                )
            )
            .padding(24.dp)
    ) {

        Text(
            "🧠 Daily Deep Dive",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Accent
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "Neuroplasticity strengthens what you repeat daily. Consistency rewires identity.",
            color = Color.White,
            fontSize = 14.sp
        )
    }
}

////////////////////////////////////////////////////////////
//////////////// PROGRESS PROJECTION ////////////////////////
////////////////////////////////////////////////////////////

@Composable
private fun ProgressProjectionSection() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(CardDark)
            .padding(20.dp)
    ) {

        Text(
            "📈 Progress Projection",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Accent
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "At this pace, you will unlock the Elite Performer badge in 4 days.",
            color = Color.White,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            "You saved 12 hours of distraction this week.",
            color = Color.Gray,
            fontSize = 13.sp
        )
    }
}