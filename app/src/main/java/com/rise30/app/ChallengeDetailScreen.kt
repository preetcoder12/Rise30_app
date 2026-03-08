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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.ui.graphics.graphicsLayer

private val iconMap = mapOf(
    "star" to R.drawable.star,
    "run" to R.drawable.run,
    "gym" to R.drawable.gym,
    "books" to R.drawable.books,
    "yoga" to R.drawable.yoga,
    "leaf" to R.drawable.leaf,
    "music" to R.drawable.music,
    "tech" to R.drawable.tech,
    "drop" to R.drawable.drop
)

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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    
    var challenge by remember { mutableStateOf<ChallengeDetail?>(null) }
    var progress by remember { mutableStateOf<ChallengeProgress?>(null) }
    var dayEntries by remember { mutableStateOf<List<DayEntry>>(emptyList()) }
    var selectedDay by remember { mutableStateOf<DayEntry?>(null) }
    var showDayDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var showAnalytics by remember { mutableStateOf(false) }
    
    val haptic = LocalHapticFeedback.current
    val scroll = rememberScrollState()
    val parallaxOffset by animateFloatAsState(
        targetValue = scroll.value * 0.1f,
        label = "parallax"
    )

    val infinite = rememberInfiniteTransition(label = "background")
    val gradientShift by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradientShift"
    )
    
    // Load challenge data
    LaunchedEffect(challengeId) {
        // Try Cache First
        val cached = CacheManager.getChallengeDetail(context, challengeId)
        if (cached != null && cached.success) {
            val progressObj = cached.challenge.progress
            challenge = ChallengeDetail(
                id = cached.challenge.id,
                name = cached.challenge.name,
                description = cached.challenge.description,
                type = cached.challenge.type,
                category = cached.challenge.category,
                duration = cached.challenge.duration,
                color = cached.challenge.color,
                icon = cached.challenge.icon,
                isActive = cached.challenge.isActive,
                targetValue = cached.challenge.targetValue,
                unit = cached.challenge.unit
            )
            progress = ChallengeProgress(
                completedDays = progressObj.completedDays,
                totalDays = progressObj.totalDays,
                percentage = progressObj.percentage,
                currentStreak = progressObj.currentStreak,
                currentDay = progressObj.currentDay
            )
            dayEntries = cached.challenge.dailyTasks.map { task ->
                DayEntry(
                    dayNumber = task.dayNumber,
                    date = task.date,
                    completed = task.completed,
                    completedAt = task.completedAt,
                    notes = task.notes,
                    value = task.value
                )
            }
            isLoading = false
        } else {
            isLoading = true
        }

        loadChallengeDetail(context, userId, challengeId) { detail, prog, entries ->
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
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF0F0F11),
                            Color(0xFF18181C),
                            challengeColor.copy(alpha = 0.05f)
                        ),
                        start = Offset(0f, gradientShift),
                        end = Offset(gradientShift, 0f)
                    )
                )
        ) {
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
                    .verticalScroll(scroll)
                    .padding(horizontal = 20.dp)
                    .padding(top = 16.dp), // Added extra top padding to fix cutting
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                    // Header
                    ChallengeDetailHeader(
                        challengeName = challenge?.name ?: "",
                        challengeIcon = challengeIcon,
                        challengeColor = challengeColor,
                        onBack = onBack,
                        onToggleAnalytics = { showAnalytics = !showAnalytics },
                        parallaxOffset = parallaxOffset
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
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                scope.launch {
                                    markTodayComplete(context, userId, challengeId) { updatedProgress, updatedEntries ->
                                        progress = updatedProgress
                                        dayEntries = updatedEntries
                                    }
                                }
                            }
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Challenge Day Grid
                        ChallengeDayGrid(
                            dayEntries = dayEntries,
                            currentDay = progress?.currentDay ?: 1,
                            totalDays = progress?.totalDays ?: challenge?.duration ?: 30,
                            challengeColor = challengeColor,
                            onDayClick = { day ->
                                selectedDay = day
                                showDayDialog = true
                            }
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Streak Card
                        ChallengeStreakCard(
                            currentStreak = progress?.currentStreak ?: 0,
                            challengeColor = challengeColor
                        )
                        
                        if (!challenge?.description.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Description Card
                            ChallengeDescriptionCard(
                                description = challenge?.description ?: "",
                                challengeColor = challengeColor
                            )
                        }
                        
                        // Bottom clearance to match Sign Out button padding (120dp)
                        Spacer(modifier = Modifier.height(120.dp))
                    }
                }
            }
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
                        context = context,
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
    onToggleAnalytics: () -> Unit,
    parallaxOffset: Float
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                translationY = -parallaxOffset
            }
            .padding(top = 16.dp, bottom = 23.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back Button + Title Row
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.offset(x = (-16).dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }
            
            // Title with optional icon
            val resId = iconMap[challengeIcon]
            if (resId != null) {
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = challengeName,
                    color = Color.White,
                    fontSize = 24.sp, 
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            } else {
                Text(
                    text = "$challengeIcon $challengeName",
                    color = Color.White,
                    fontSize = 24.sp, 
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        
        // Stats Button stays on right
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(challengeColor.copy(alpha = 0.15f))
                .clickable(onClick = onToggleAnalytics),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.BarChart,
                contentDescription = "Analytics",
                tint = challengeColor,
                modifier = Modifier.size(22.dp)
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
    
    // Premium Liquid Glass UI
    LiquidGlassCard(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            // Top Row: Icon and Quick Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Animated Icon
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    challengeColor.copy(alpha = 0.3f),
                                    challengeColor.copy(alpha = 0.1f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    val resId = iconMap[challengeIcon]
                    if (resId != null) {
                        Image(
                            painter = painterResource(id = resId),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    } else {
                        Text(
                            text = challengeIcon,
                            fontSize = 32.sp
                        )
                    }
                }
                
                // Quick Stats
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    QuickStat(
                        value = "${progress?.completedDays ?: 0}",
                        label = "Done",
                        color = challengeColor
                    )
                    QuickStat(
                        value = "${progress?.currentStreak ?: 0}",
                        label = "Streak",
                        color = challengeColor
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // 3D Progress Ring
            ThreeDProgressRing(
                progress = percentage / 100f,
                color = challengeColor,
                size = 160.dp
            )
            
            // Center text overlay (if you want text inside the ring)
            Column(
                modifier = Modifier
                    .offset(y = (-110).dp), // Adjust position relative to ThreeDProgressRing
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$percentage%",
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "COMPLETED",
                    color = Color.Gray,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 2.sp
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Day indicator pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(alpha = 0.06f))
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                val total = progress?.totalDays ?: 30
                Text(
                    text = "Day ${progress?.currentDay ?: 1} of $total",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Premium Mark Today Button with Glow
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = challengeColor,
                        spotColor = challengeColor
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(challengeColor)
                    .clickable(onClick = onMarkToday),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "✓",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Mark Today Complete",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
        }
    }
}

@Composable
private fun QuickStat(
    value: String,
    label: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = color,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 11.sp,
            letterSpacing = 1.sp
        )
    }
}

@Composable
private fun ChallengeDayGrid(
    dayEntries: List<DayEntry>,
    currentDay: Int,
    totalDays: Int,
    challengeColor: Color,
    onDayClick: (DayEntry) -> Unit
) {
    Column {
        // Premium Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$totalDays-Day Journey",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.2.sp
            )
            
            // Progress mini indicator
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(challengeColor.copy(alpha = 0.15f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "${dayEntries.count { it.completed }}/$totalDays",
                    color = challengeColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Premium Card with Liquid Glass effect
        LiquidGlassCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                // Modern Legend
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ModernLegendItem(
                        color = challengeColor,
                        label = "Done",
                        icon = "✓"
                    )
                    ModernLegendItem(
                        color = Color(0xFF3A3A40),
                        label = "Upcoming",
                        icon = "○"
                    )
                    ModernLegendItem(
                        color = challengeColor.copy(alpha = 0.4f),
                        label = "Today",
                        icon = "◉"
                    )
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                val configuration = androidx.compose.ui.platform.LocalConfiguration.current
                val screenWidth = configuration.screenWidthDp.dp
                val availableWidth = screenWidth - 40.dp - 48.dp // padding + card inner padding
                val cellSize = (availableWidth / 6f)
                val rows = Math.ceil(totalDays / 6.0).toInt()
                val gridHeight = (rows * cellSize.value + (rows - 1) * 10).dp
                
                val itemsToShow = dayEntries.take(totalDays)
                
                // Dynamic Day Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(6),
                    modifier = Modifier.height(gridHeight),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(itemsToShow) { day ->
                        PremiumDayCell(
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
private fun PremiumDayCell(
    day: DayEntry,
    isToday: Boolean,
    challengeColor: Color,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val interaction = remember { MutableInteractionSource() }
    val backgroundColor by animateColorAsState(
        targetValue = when {
            day.completed -> challengeColor
            isToday -> challengeColor.copy(alpha = 0.25f)
            else -> Color(0xFF2A2A30)
        },
        animationSpec = tween(300),
        label = "dayBackground"
    )
    
    val scale by animateFloatAsState(
        targetValue = if (isToday) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "dayScale"
    )
    
    val glowAlpha by animateFloatAsState(
        targetValue = if (isToday) 0.6f else 0f,
        animationSpec = tween(400),
        label = "glowAlpha"
    )
    
    Box(
        modifier = Modifier
            .fillMaxWidth() // Fill the grid cell width
            .aspectRatio(1f) // Keep it perfect square
            .scale(scale),
        contentAlignment = Alignment.Center
    ) {
        // Glow effect for today
        if (isToday) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(challengeColor.copy(alpha = glowAlpha * 0.3f))
            )
        }
        
        // Main cell
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(backgroundColor)
                .border(
                    width = when {
                        isToday -> 2.dp
                        day.completed -> 0.dp
                        else -> 1.dp
                    },
                    color = when {
                        isToday -> challengeColor
                        day.completed -> Color.Transparent
                        else -> Color(0xFF3A3A40)
                    },
                    shape = CircleShape
                )
                .clickable(
                    interactionSource = interaction,
                    indication = rememberRipple(
                        color = challengeColor,
                        bounded = true
                    ),
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onClick()
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (day.completed) {
                // Premium checkmark
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✓",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Text(
                    text = day.dayNumber.toString(),
                    color = when {
                        isToday -> challengeColor
                        else -> Color(0xFF888888)
                    },
                    fontSize = 13.sp,
                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun ModernLegendItem(color: Color, label: String, icon: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.03f))
            .padding(horizontal = 8.dp, vertical = 5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 11.sp
        )
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
    PremiumCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Animated fire icon container
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                challengeColor.copy(alpha = 0.4f),
                                challengeColor.copy(alpha = 0.1f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Pulsing animation for the fire
                val infiniteTransition = rememberInfiniteTransition(label = "fire")
                val scale by infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 1.1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "fireScale"
                )
                
                Icon(
                    imageVector = Icons.Filled.LocalFireDepartment,
                    contentDescription = "Streak",
                    tint = challengeColor,
                    modifier = Modifier.size(36.dp).scale(scale)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$currentStreak",
                        color = challengeColor,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = " Day Streak",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (currentStreak > 0) 
                        "Keep the momentum going!" 
                    else 
                        "Start your streak today!",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
            
            // Streak badge
            if (currentStreak >= 7) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(challengeColor.copy(alpha = 0.2f))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "ON FIRE!",
                        color = challengeColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ChallengeDescriptionCard(
    description: String,
    challengeColor: Color
) {
    PremiumCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "About This Challenge",
                color = challengeColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp
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
        PremiumCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Challenge Statistics",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp
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
        PremiumCard(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.FitnessCenter,
                    contentDescription = "Motivation",
                    tint = challengeColor,
                    modifier = Modifier.size(48.dp)
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

// Premium Reusable Components

@Composable
fun PremiumCard(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.06f),
                        Color.White.copy(alpha = 0.02f)
                    )
                )
            )
            .border(
                1.dp,
                Color.White.copy(alpha = 0.08f),
                RoundedCornerShape(28.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = horizontalAlignment,
            content = content
        )
    }
}

@Composable
fun LiquidGlassCard(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.08f),
                        Color.White.copy(alpha = 0.02f)
                    )
                )
            )
            .border(
                1.dp,
                Color.White.copy(alpha = 0.15f),
                RoundedCornerShape(28.dp)
            )
    ) {
        // inner glow
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.06f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = horizontalAlignment,
            content = content
        )
    }
}

@Composable
fun ThreeDProgressRing(
    progress: Float,
    color: Color,
    size: androidx.compose.ui.unit.Dp = 160.dp
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(1400, easing = FastOutSlowInEasing),
        label = "progress"
    )

    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = 16.dp.toPx()

            // Shadow ring (depth)
            drawArc(
                color = Color.Black.copy(alpha = 0.35f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = stroke),
                topLeft = Offset(4f, 4f)
            )

            // Base track
            drawArc(
                color = Color(0xFF2A2A30),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = stroke)
            )

            // Main progress gradient
            drawArc(
                brush = Brush.sweepGradient(
                    listOf(
                        color,
                        color.copy(alpha = 0.6f),
                        color
                    )
                ),
                startAngle = -90f,
                sweepAngle = 360 * animatedProgress,
                useCenter = false,
                style = Stroke(
                    width = stroke,
                    cap = StrokeCap.Round
                )
            )

            // Highlight ring (3D shine)
            drawArc(
                color = Color.White.copy(alpha = 0.25f),
                startAngle = -90f,
                sweepAngle = 360 * animatedProgress * 0.4f,
                useCenter = false,
                style = Stroke(width = stroke * 0.3f)
            )
        }
    }
}

// API Functions
private suspend fun loadChallengeDetail(
    context: android.content.Context,
    userId: String,
    challengeId: String,
    onLoaded: (ChallengeDetail, ChallengeProgress, List<DayEntry>) -> Unit
) {
    try {
        val response: ChallengeDetailApiResponse = ApiConfig.httpClient.get("${ApiConfig.BASE_URL}/api/challenges/$challengeId").body()
        
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
            
            CacheManager.saveChallengeDetail(context, challengeId, response)
            onLoaded(challengeDetail, challengeProgress, entries)
        }
    } catch (e: Exception) {
        // Fallback to minimal mock data
        val mockChallenge = ChallengeDetail(
            id = challengeId,
            name = "Loading Challenge...",
            description = "One moment while we load your details.",
            type = "custom",
            category = "personal",
            duration = 30,
            color = "#4FC3F7",
            icon = "🎯",
            isActive = true,
            targetValue = null,
            unit = null
        )
        
        val mockProgress = ChallengeProgress(
            completedDays = 0,
            totalDays = 30,
            percentage = 0,
            currentStreak = 0,
            currentDay = 1
        )
        
        val mockEntries = (1..30).map { day ->
            DayEntry(
                dayNumber = day,
                date = "",
                completed = false,
                completedAt = null,
                notes = null,
                value = null
            )
        }
        
        onLoaded(mockChallenge, mockProgress, mockEntries)
    }
}

private suspend fun markTodayComplete(
    context: android.content.Context,
    userId: String,
    challengeId: String,
    onSuccess: (ChallengeProgress, List<DayEntry>) -> Unit
) {
    try {
        val response: HttpResponse = ApiConfig.httpClient.post("${ApiConfig.BASE_URL}/api/challenges/$challengeId/mark-today") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("userId" to userId))
        }
        
        if (response.status.isSuccess()) {
            // Reload challenge details to get updated progress
            loadChallengeDetail(context, userId, challengeId, onLoaded = { detail, progress, entries ->
                onSuccess(progress, entries)
            })
        }
    } catch (e: Exception) {
        // Fallback to local update on error
        val fallbackDuration = 30 // Should ideally use current context duration
        val updatedProgress = ChallengeProgress(
            completedDays = 0,
            totalDays = fallbackDuration,
            percentage = 0,
            currentStreak = 0,
            currentDay = 1
        )
        
        val updatedEntries = (1..fallbackDuration).map { day ->
            DayEntry(
                dayNumber = day,
                date = "",
                completed = false,
                completedAt = null,
                notes = null,
                value = null
            )
        }
        
        onSuccess(updatedProgress, updatedEntries)
    }
}

private suspend fun toggleDayComplete(
    context: android.content.Context,
    userId: String,
    challengeId: String,
    dayNumber: Int,
    completed: Boolean,
    onSuccess: (List<DayEntry>, ChallengeProgress) -> Unit
) {
    try {
        val response: HttpResponse = ApiConfig.httpClient.post("${ApiConfig.BASE_URL}/api/challenges/$challengeId/day/$dayNumber/toggle") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("userId" to userId, "completed" to completed))
        }
        
        if (response.status.isSuccess()) {
            // Reload challenge details to get updated progress
            loadChallengeDetail(context, userId, challengeId, onLoaded = { detail, progress, entries ->
                onSuccess(entries, progress)
            })
        }
    } catch (e: Exception) {
        // Fallback to local update on error
        val fallbackDuration = 30
        val updatedProgress = ChallengeProgress(
            completedDays = 0,
            totalDays = fallbackDuration,
            percentage = 0,
            currentStreak = 0,
            currentDay = 1
        )
        
        val updatedEntries = (1..fallbackDuration).map { day ->
            DayEntry(
                dayNumber = day,
                date = "",
                completed = day == dayNumber && completed,
                completedAt = if (day == dayNumber && completed) "2026-03-${day.toString().padStart(2, '0')}T10:00:00Z" else null,
                notes = null,
                value = null
            )
        }
        
        onSuccess(updatedEntries, updatedProgress)
    }
}
