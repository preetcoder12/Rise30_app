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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.Canvas
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlin.random.Random

// 🌑 Premium Colors
val BackgroundDark = Color(0xFF080810)
val CardDark = Color(0xFF14141E)
val Accent = Color(0xFFFFD54F)
val AccentSoft = Color(0x33FFD54F)

// Glassmorphism card helper colors
val GlassTop = Color(0xFF1E1E2E)
val GlassStroke = Color(0x26FFFFFF) // 15% white

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

@Serializable
data class HabitResponse(
    val success: Boolean,
    val habits: List<Habit>
)

@Serializable
data class Habit(
    val id: String? = null,
    val name: String,
    val completed: Boolean
)

@Serializable
data class MotivationResponse(
    val success: Boolean,
    val quote: Quote
)

@Serializable
data class Quote(
    val text: String,
    val author: String
)
@Serializable
data class DeepDiveResponse(
    val success: Boolean,
    val deepDive: DeepDive
)

@Serializable
data class DeepDive(
    val title: String,
    val content: String,
    val category: String,
    val readTime: String
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
            .padding(top = 15.dp, bottom = 0.dp), // Removed bottom padding to control spacing strictly
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "Rise30 Logo",
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Welcome back,",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = userName,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        IconButton(
            onClick = onNotificationClick,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(CardDark.copy(alpha = 0.5f))
        ) {
            Image(
                painter = painterResource(id = R.drawable.bell),
                contentDescription = "Notifications",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun MainChallengeCard(
    challenge: ChallengeApiData?,
    onMarkComplete: () -> Unit,
    onCreateChallenge: () -> Unit = {}
) {
    val currentDay = challenge?.progress?.completedDays ?: 0
    val totalDays = challenge?.progress?.totalDays ?: challenge?.duration ?: 30
    val progress = remember(currentDay, totalDays) {
        (currentDay.coerceAtLeast(0).coerceAtMost(totalDays).toFloat() / totalDays.coerceAtLeast(1))
    }
    
    val challengeColor = Color(0xFFFFF44F)
    val isTodayCompleted = challenge?.progress?.isTodayCompleted ?: false
    var sparkleTrigger by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxWidth()) {
        // card content
        Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1E1E30),
                        Color(0xFF12121C)
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.18f),
                        Color.White.copy(alpha = 0.04f)
                    )
                ),
                shape = RoundedCornerShape(28.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Progress Circle
            Box(
                modifier = Modifier
                    .size(90.dp),
                contentAlignment = Alignment.Center
            ) {

                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {

                    val strokeWidth = 10.dp.toPx()

                    // Background circle
                    drawCircle(
                        color = challengeColor.copy(alpha = 0.12f),
                        style = Stroke(width = strokeWidth)
                    )

                    // Progress arc
                    drawArc(
                        color = challengeColor,
                        startAngle = -90f,
                        sweepAngle = 360f * progress,
                        useCenter = false,
                        style = Stroke(
                            width = strokeWidth,
                            cap = StrokeCap.Round
                        )
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "${(progress * 100).roundToInt()}%",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Done",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(20.dp))

            // Text Section
            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = challenge?.description ?: "Complete today's focus sessions to finish your challenge.",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(11.dp))
                
                // Challenge Name
                Text(
                    text = challenge?.name ?: "Daily Challenge",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                // Info Row: Streak and Day
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val streakLength = challenge?.progress?.currentStreak ?: 0
                    if (streakLength > 0) {
                        Text(
                            text = "$streakLength day streak  •  ",
                            color = Accent,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = "Day $currentDay/$totalDays",
                        color = challengeColor,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                // Button
                Button(
                    onClick = {
                        if (challenge == null) {
                            onCreateChallenge()
                        } else {
                            sparkleTrigger++
                            onMarkComplete()
                        }
                    },
                    enabled = challenge == null || !isTodayCompleted,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isTodayCompleted) Color(0xFF2E2E2E) else challengeColor,
                        contentColor = if (isTodayCompleted) Color.LightGray else Color.Black,
                        disabledContainerColor = Color.DarkGray,
                        disabledContentColor = Color.LightGray
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(
                        horizontal = 18.dp,
                        vertical = 6.dp
                    )
                ) {

                    Text(
                        text = when {
                            challenge == null -> "Create Challenge"
                            isTodayCompleted -> "Completed"
                            else -> "Continue"
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
                }
            }
        } // close inner card Box
        // Celebration sparkle overlay
        CelebrationSparkle(
            trigger = sparkleTrigger,
            modifier = Modifier
                .matchParentSize()
                .padding(bottom = 12.dp)
        )
    } // close outer Box
}

/**
 * Reusable bottom-to-top sparkle burst. Caller controls [trigger]: bump it to replay.
 */
@Composable
fun CelebrationSparkle(trigger: Int, modifier: Modifier = Modifier) {
    val sparkleProgress = remember { Animatable(0f) }
    val sparkleVisible = remember { mutableStateOf(false) }

    LaunchedEffect(trigger) {
        if (trigger > 0) {
            sparkleVisible.value = true
            sparkleProgress.snapTo(0f)
            sparkleProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1100, easing = FastOutSlowInEasing)
            )
            sparkleVisible.value = false
        }
    }

    val particles = remember {
        List(18) {
            Triple(
                Random.nextFloat(),                          // normalised X (0-1)
                Random.nextFloat() * 0.5f + 0.4f,           // rise speed
                listOf(
                    Accent, Color(0xFFFFEB3B), Color(0xFFFFF176),
                    Color(0xFFFFCA28), Color(0xFFFFFFFF), Color(0xFFFFC107)
                )[it % 6]
            )
        }
    }

    if (sparkleVisible.value) {
        val p = sparkleProgress.value
        Canvas(modifier = modifier) {
            particles.forEach { (xRatio, speedFactor, color) ->
                val x = xRatio * size.width
                val y = size.height - (p * speedFactor * size.height * 1.3f)
                val alpha = when {
                    p < 0.15f -> p / 0.15f
                    p > 0.6f  -> (1f - (p - 0.6f) / 0.4f).coerceAtLeast(0f)
                    else      -> 1f
                }
                val radius = (6f + (1f - p) * 5f) * (0.6f + xRatio * 0.8f)
                drawCircle(color = color.copy(alpha = alpha), radius = radius, center = Offset(x, y))
            }
        }
    }
}

@Composable
private fun WeeklyOverview(userId: String) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var analyticsData by remember { mutableStateOf<AnalyticsData?>(CacheManager.getAnalytics(context, userId)?.analytics) }
    var isLoading by remember { mutableStateOf(analyticsData == null) }
    
    LaunchedEffect(userId) {
        val cached = CacheManager.getAnalytics(context, userId)
        if (cached != null) {
            analyticsData = cached.analytics
            isLoading = false
        }

        scope.launch {
            try {
                val response: AnalyticsResponse = ApiConfig.httpClient.get("${ApiConfig.BASE_URL}/api/users/$userId/analytics").body()
                if (response.success) {
                    analyticsData = response.analytics
                    CacheManager.saveAnalytics(context, userId, response)
                }
            } catch (e: Exception) {
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
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1A1A2E),
                        Color(0xFF10101A)
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.15f),
                        Color.White.copy(alpha = 0.03f)
                    )
                ),
                shape = RoundedCornerShape(28.dp)
            )
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
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$completedCount/$totalCount days",
                    color = Accent,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "This Week",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoBox(label = "Longest Streak", valueText = "${analyticsData?.longestStreak ?: 0} days")
            InfoBox(label = "In Progress", valueText = "${analyticsData?.activeChallenges ?: 0} challenges")
            InfoBox(label = "Overall Journey", valueText = "${analyticsData?.completionRate ?: 0}% Done")
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Accent, strokeWidth = 3.dp)
            }
        } else {
            val dayLabels = listOf("M", "T", "W", "T", "F", "S", "S")
            val todayIndex = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK).let {
                if (it == java.util.Calendar.SUNDAY) 6 else it - 2
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                dayLabels.forEachIndexed { index, label ->
                    val item = weeklyData.getOrNull(index)
                    val progress = if (item != null && item.total > 0) {
                        item.completed.toFloat() / item.total.toFloat()
                    } else 0f
                    val isToday = index == todayIndex
                    val animatedProgress by animateFloatAsState(
                        targetValue = progress,
                        animationSpec = tween(durationMillis = 1000, delayMillis = index * 100, easing = FastOutSlowInEasing),
                        label = "progress"
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = label,
                            color = if (isToday) Accent else Color.Gray.copy(alpha = 0.7f),
                            fontSize = 14.sp,
                            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.width(30.dp)
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.White.copy(alpha = 0.05f))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(animatedProgress.coerceIn(0.001f, 1f))
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = if (isToday) listOf(Accent, Accent.copy(alpha = 0.7f)) else listOf(Accent.copy(alpha = 0.8f), Accent.copy(alpha = 0.4f))
                                        )
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoBox(label: String, valueText: String) {
    val parts = valueText.split(" ")
    val n = parts.getOrNull(0) ?: ""
    val unit = if (parts.size > 1) parts.subList(1, parts.size).joinToString(" ") else ""

    Column(
        modifier = Modifier.padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = n,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        if (unit.isNotEmpty()) {
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = unit,
                color = Color.White,
                fontSize = 11.sp // 13 - 2
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 11.sp // 13 - 2
        )
    }
}

@Composable
private fun MotivationCard() {
    var quote by remember { mutableStateOf(Quote("Loading your daily motivation...", "Rise30")) }
    
    LaunchedEffect(Unit) {
        try {
            val response: MotivationResponse = ApiConfig.httpClient.get("${ApiConfig.BASE_URL}/api/motivation").body()
            if (response.success) {
                quote = response.quote
            }
        } catch (e: Exception) { /* use default */ }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1E1E2E),
                        Color(0xFF14141E)
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.12f),
                        Color.Transparent
                    )
                ),
                shape = RoundedCornerShape(22.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Accent.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.AcUnit, // Check icon fallback
                    contentDescription = null,
                    tint = Accent,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(18.dp))
            
            Text(
                text = "“${quote.text}”",
                color = Color.White,
                fontSize = 19.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
            
            Spacer(modifier = Modifier.height(14.dp))
            
            Text(
                text = "— ${quote.author}",
                color = Accent,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
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
                    iconRes = R.drawable.home,
                    tab = MainTab.Home,
                    currentTab = currentTab,
                    onTabSelected = onTabSelected
                )
                BottomBarItem(
                    iconRes = R.drawable.challenge,
                    tab = MainTab.Challenges,
                    currentTab = currentTab,
                    onTabSelected = onTabSelected
                )
                BottomBarItem(
                    iconRes = R.drawable.person,
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
    iconRes: Int,
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
                    .size(if (selected) 40.dp else 36.dp)
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
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = if (selected) Accent else Color.Gray.copy(alpha = 0.7f),
                    modifier = Modifier.size(if (selected) 24.dp else 22.dp)
                )
            }
            
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
    onCreateChallenge: () -> Unit,
    onNotificationClick: () -> Unit,
    onSearchFriends: () -> Unit,
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    
    // State for user's most recent challenge
    var recentChallenge by remember { 
        mutableStateOf<ChallengeApiData?>(
            CacheManager.getChallenges(context, userId)?.let { cached ->
                val summary = cached.firstOrNull { it.isActive } ?: cached.firstOrNull()
                summary?.let {
                    ChallengeApiData(
                        id = it.id,
                        name = it.name,
                        description = it.description,
                        type = it.type,
                        category = it.category,
                        duration = it.duration,
                        color = it.color,
                        icon = it.icon,
                        isActive = it.isActive,
                        progress = ChallengeProgressData(
                            completedDays = it.completedDays,
                            totalDays = it.duration,
                            percentage = it.progress,
                            currentStreak = it.currentStreak,
                            isTodayCompleted = it.isTodayCompleted,
                            currentDayNumber = it.currentDayNumber
                        )
                    )
                }
            }
        ) 
    }
    var isLoadingChallenge by remember { mutableStateOf(recentChallenge == null) }
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
                val response: ChallengesResponse = ApiConfig.httpClient.get("${ApiConfig.BASE_URL}/api/challenges/user/$userId").body()
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
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF151525),
                            Color(0xFF090912)
                        ),
                        radius = 1800f
                    )
                )
        ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {

            TopSection(userName, onNotificationClick)
            
            Spacer(modifier = Modifier.height(16.dp)) // Strictly 16dp below the username section



            MainChallengeCard(
                challenge = recentChallenge,
                onMarkComplete = {
                    recentChallenge?.let { challenge ->
                        scope.launch {
                            try {
                                // Get timezone offset (e.g., "+05:30" for India)
                                val tz = java.util.TimeZone.getDefault()
                                val offsetMs = tz.rawOffset + tz.dstSavings
                                val offsetHours = kotlin.math.abs(offsetMs) / (1000 * 60 * 60)
                                val offsetMinutes = (kotlin.math.abs(offsetMs) / (1000 * 60)) % 60
                                val offsetSign = if (offsetMs >= 0) "+" else "-"
                                val tzOffset = "${offsetSign}${String.format("%02d:%02d", offsetHours, offsetMinutes)}"
                                
                                val response: HttpResponse = ApiConfig.httpClient.post("${ApiConfig.BASE_URL}/api/challenges/${challenge.id}/mark-today") {
                                    contentType(ContentType.Application.Json)
                                    setBody(mapOf("userId" to userId, "tzOffset" to tzOffset))
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
                },
                onCreateChallenge = onCreateChallenge
            )

            Spacer(modifier = Modifier.height(16.dp))

            WeeklyOverview(userId)

            Spacer(modifier = Modifier.height(16.dp))

            PowerMorningSection(userId)

            Spacer(modifier = Modifier.height(16.dp))

            FriendsSection(onSearchFriends = onSearchFriends)

            Spacer(modifier = Modifier.height(16.dp))

            StreakShieldSection(
                currentStreak = recentChallenge?.progress?.currentStreak ?: 0,
                longestStreak = 0 // longestStreak shown per-challenge; WeeklyOverview fetches the global one
            )

            Spacer(modifier = Modifier.height(16.dp))

            DailyDeepDiveSection()

            Spacer(modifier = Modifier.height(16.dp))

            MotivationCard()

            Spacer(modifier = Modifier.height(120.dp))
        }
        } // close Box
    }
}

////////////////////////////////////////////////////////////
//////////////// POWER MORNING //////////////////////////////
////////////////////////////////////////////////////////////

@Composable
private fun PowerMorningSection(userId: String) {
    val ritualHabitNames = listOf(
        "Drink 1 Glass of Water",
        "5 Min Meditation",
        "No Social Media (1h)"
    )
    
    val scope = rememberCoroutineScope()
    // SnapshotStateMap: individual key writes trigger recomposition immediately
    val doneState = remember { androidx.compose.runtime.snapshots.SnapshotStateMap<String, Boolean>() }
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current

    LaunchedEffect(userId) {
        try {
            // Get timezone offset (e.g., "+05:30" for India)
            val tz = java.util.TimeZone.getDefault()
            val offsetMs = tz.rawOffset + tz.dstSavings
            val offsetHours = kotlin.math.abs(offsetMs) / (1000 * 60 * 60)
            val offsetMinutes = (kotlin.math.abs(offsetMs) / (1000 * 60)) % 60
            val offsetSign = if (offsetMs >= 0) "+" else "-"
            val tzOffset = "${offsetSign}${String.format("%02d:%02d", offsetHours, offsetMinutes)}"
            
            val response: HabitResponse = ApiConfig.httpClient.get("${ApiConfig.BASE_URL}/api/habits/$userId?tz=${tzOffset}").body()
            if (response.success) {
                response.habits.forEach { habit -> doneState[habit.name] = habit.completed }
            }
        } catch (e: Exception) { /* use default false */ }
    }

    // Auto-reset at midnight every day
    LaunchedEffect(Unit) {
        while (true) {
            val now = java.util.Calendar.getInstance()
            val midnight = java.util.Calendar.getInstance().apply {
                add(java.util.Calendar.DAY_OF_MONTH, 1)
                set(java.util.Calendar.HOUR_OF_DAY, 0)
                set(java.util.Calendar.MINUTE, 0)
                set(java.util.Calendar.SECOND, 0)
                set(java.util.Calendar.MILLISECOND, 0)
            }
            val msUntilMidnight = midnight.timeInMillis - now.timeInMillis
            delay(msUntilMidnight.coerceAtLeast(1000L))
            doneState.clear()  // New day — reset all rituals
        }
    }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.fire),
                contentDescription = null,
                modifier = Modifier.size(31.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Power Morning Ritual",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        ritualHabitNames.forEach { habitName ->
            val isDone = doneState[habitName] ?: false
            RitualItem(
                name = habitName,
                done = isDone,
                onMarkDone = {
                    if (!isDone) {
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        doneState[habitName] = true
                        scope.launch {
                            try {
                                ApiConfig.httpClient.post("${ApiConfig.BASE_URL}/api/habits/toggle") {
                                    contentType(ContentType.Application.Json)
                                    setBody(mapOf("userId" to userId, "name" to habitName, "completed" to true))
                                }
                            } catch (e: Exception) { /* silent */ }
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun RitualItem(name: String, done: Boolean, onMarkDone: () -> Unit) {
    // Sparkle animation state
    var sparkleVisible by remember { mutableStateOf(false) }
    val sparkleProgress = remember { Animatable(0f) }

    LaunchedEffect(done) {
        if (done) {
            sparkleVisible = true
            sparkleProgress.snapTo(0f)
            sparkleProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1100, easing = FastOutSlowInEasing)
            )
            sparkleVisible = false
        }
    }

    // Particle data seeded once per item
    val particles = remember {
        List(14) {
            Triple(
                Random.nextFloat(),                           // normalised X (0-1)
                Random.nextFloat() * 0.5f + 0.4f,            // rise speed factor
                listOf(
                    Accent, Color(0xFFFFEB3B), Color(0xFFFFF176),
                    Color(0xFFFFCA28), Color(0xFFFFFFFF)
                )[it % 5]
            )
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        // Main row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.linearGradient(
                        colors = if (done)
                            listOf(Accent.copy(alpha = 0.07f), Color(0xFF0E0E18))
                        else
                            listOf(Color(0xFF1A1A28), Color(0xFF0E0E18))
                    )
                )
                .border(
                    1.dp,
                    if (done) Accent.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.06f),
                    RoundedCornerShape(20.dp)
                )
                .clickable(enabled = !done, onClick = onMarkDone)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(if (done) Accent else Color.Transparent)
                    .border(
                        width = 2.dp,
                        color = if (done) Color.Transparent else Color.White.copy(alpha = 0.25f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (done) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = name,
                color = if (done) Accent.copy(alpha = 0.4f) else Color.White,
                fontSize = 16.sp,
                fontWeight = if (done) FontWeight.Normal else FontWeight.Medium,
                textDecoration = if (done) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
            )
        }

        // Sparkle overlay — bottom-to-top particle burst
        if (sparkleVisible) {
            val p = sparkleProgress.value
            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .padding(vertical = 6.dp)
            ) {
                particles.forEach { (xRatio, speedFactor, color) ->
                    val x = xRatio * size.width
                    // start at bottom, rise upward
                    val y = size.height - (p * speedFactor * size.height * 1.2f)
                    // alpha: fade in quickly then fade out
                    val alpha = when {
                        p < 0.2f -> p / 0.2f
                        p > 0.65f -> (1f - (p - 0.65f) / 0.35f).coerceAtLeast(0f)
                        else -> 1f
                    }
                    val radius = (5f + (1f - p) * 4f) * (0.7f + xRatio * 0.6f)
                    drawCircle(
                        color = color.copy(alpha = alpha),
                        radius = radius,
                        center = Offset(x, y)
                    )
                }
            }
        }
    }
}

////////////////////////////////////////////////////////////
//////////////// FRIENDS SECTION ////////////////////////////
////////////////////////////////////////////////////////////

@Composable
private fun FriendsSection(onSearchFriends: () -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.friends),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Community",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF1E1E30), Color(0xFF14141E))
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.12f),
                            Color.White.copy(alpha = 0.04f)
                        )
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Find other people on their Rise30 journey!",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = onSearchFriends,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Accent.copy(alpha = 0.15f),
                        contentColor = Accent
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Search Members", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}


////////////////////////////////////////////////////////////
//////////////// STREAK SHIELD //////////////////////////////
////////////////////////////////////////////////////////////

@Composable
private fun StreakShieldSection(currentStreak: Int = 0, longestStreak: Int = 0) {
    // Shield integrity is based on how close current streak is to longest streak
    val shieldIntegrity = if (longestStreak > 0) {
        (currentStreak.toFloat() / longestStreak.toFloat()).coerceIn(0f, 1f)
    } else if (currentStreak > 0) 1f else 0f
    val shieldPercent = (shieldIntegrity * 100).toInt()
    val isActive = currentStreak > 0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF1E1E30), Color(0xFF0C0C18))
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Accent.copy(alpha = 0.20f),
                        Color.White.copy(alpha = 0.04f)
                    )
                ),
                shape = RoundedCornerShape(28.dp)
            )
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.shield),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Streak Shield",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Accent
                )
            }
            val statusColor = if (isActive) Color.Green else Color.Gray
            Text(
                if (isActive) "Active" else "Inactive",
                color = statusColor.copy(alpha = 0.8f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(statusColor.copy(alpha = 0.1f))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            if (isActive)
                "Current streak: $currentStreak day${if (currentStreak == 1) "" else "s"}. Keep the momentum going!"
            else
                "Start a challenge and complete today to activate your streak shield!",
            color = Color.Gray,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        LinearProgressIndicator(
            progress = { shieldIntegrity },
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
            Text("$shieldPercent%", color = Accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
                    colors = listOf(
                        Color(0xFF1A1A28),
                        Color(0xFF0C0C14)
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.13f),
                        Color.White.copy(alpha = 0.02f)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(24.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.brain),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Daily Deep Dive",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Accent
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "Neuroplasticity strengthens what you repeat daily. Consistency rewires identity.",
            color = Color.White,
            fontSize = 14.sp
        )
    }
}
