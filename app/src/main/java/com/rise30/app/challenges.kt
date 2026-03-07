package com.rise30.app

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
<<<<<<< HEAD
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
=======
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
>>>>>>> 5376e4c04b5b0e6f10f16eb23b1028ae8562da77
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
<<<<<<< HEAD
import com.rise30.app.calendar.CalendarHelper
import com.rise30.app.calendar.CalendarIntegrationDialog
import com.rise30.app.calendar.CalendarPermissionHandler
import com.rise30.app.calendar.CalendarSuccessNotification
import java.util.Calendar
=======
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable

@Serializable
data class ChallengeSummary(
    val id: String,
    val name: String,
    val description: String?,
    val type: String,
    val category: String,
    val duration: Int,
    val color: String?,
    val icon: String?,
    val progress: Int,
    val completedDays: Int,
    val currentStreak: Int,
    val isActive: Boolean
)
>>>>>>> 5376e4c04b5b0e6f10f16eb23b1028ae8562da77

@Composable
fun ChallengesPage(
    userName: String,
    userId: String,
    onStartChallenge: () -> Unit,
    onStartWaterChallenge: () -> Unit,
    onChallengeClick: (String) -> Unit,
    onCreateChallenge: () -> Unit,
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
<<<<<<< HEAD
    val calendarHelper = remember { CalendarHelper(context) }
    
    // Calendar integration state
    var showCalendarDialog by remember { mutableStateOf(false) }
    var showSuccessNotification by remember { mutableStateOf(false) }
    var eventsCreated by remember { mutableStateOf(0) }
    var pendingReminderTime by remember { mutableStateOf<Calendar?>(null) }
=======
    var selectedCategory by remember { mutableStateOf("All") }
    var challenges by remember { mutableStateOf<List<ChallengeSummary>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var refreshTrigger by remember { mutableStateOf(0) }
    
    // Load challenges from real database
    LaunchedEffect(userId, refreshTrigger, currentTab) {
        if (currentTab == MainTab.Challenges) {
            isLoading = true
            errorMessage = null
            try {
                loadUserChallengesFromApi(userId) { loadedChallenges ->
                    challenges = loadedChallenges
                    isLoading = false
                }
            } catch (e: Exception) {
                errorMessage = "Failed to load challenges: ${e.message}"
                isLoading = false
            }
        }
    }
    
    val filteredChallenges = if (selectedCategory == "All") {
        challenges
    } else {
        challenges.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }
    
    val categories = listOf("All", "Health", "Fitness", "Productivity", "Skills")
>>>>>>> 5376e4c04b5b0e6f10f16eb23b1028ae8562da77

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
<<<<<<< HEAD
                Text(
                    text = "Challenges",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Hero recommended challenge card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CardDark),
                    shape = RoundedCornerShape(22.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Recommended for You",
                                color = Color.LightGray,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "21-Day Cognitive Clarity Intensive",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "MASTER YOUR MIND",
                                color = Accent,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Duration: 21 Days   Target: Focus & Clarity",
                                color = Color.LightGray,
                                fontSize = 12.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { 
                                    // Show calendar dialog when starting challenge
                                    showCalendarDialog = true
                                },
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Accent,
                                    contentColor = Color.Black
                                )
                            ) {
                                Text(
                                    text = "Start this Challenge",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Image(
                            painter = painterResource(id = R.drawable.tree),
                            contentDescription = "Mind tree",
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(20.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Simple category tabs row
=======
                // Header
>>>>>>> 5376e4c04b5b0e6f10f16eb23b1028ae8562da77
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, bottom = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "My Challenges",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = "${challenges.size} Active",
                        color = Accent,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))
                
                // Featured Water Challenge Card - only show if no water challenge exists
                val hasWaterChallenge = challenges.any { it.type == "water" }
                if (!hasWaterChallenge) {
                    WaterChallengeCard(onStartWaterChallenge)
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Category tabs - horizontally scrollable
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    categories.forEach { category ->
                        CategoryPill(
                            label = category,
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(5.dp))

                // Challenges List
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Loading challenges...",
                            color = Color.Gray
                        )
                    }
                } else if (filteredChallenges.isEmpty()) {
                    EmptyChallengesView(onCreateChallenge)
                } else {
                    filteredChallenges.forEach { challenge ->
                        ChallengeListCard(
                            challenge = challenge,
                            onClick = { onChallengeClick(challenge.id) }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                Spacer(modifier = Modifier.height(106.dp))
            }

            // FAB for creating new challenge
            FloatingActionButton(
                onClick = onCreateChallenge,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 20.dp, bottom = 140.dp),
                containerColor = Accent,
                contentColor = Color.Black,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create Challenge"
                )
            }

            // Bottom Navigation
            HomeFloatingBottomBar(
                currentTab = currentTab,
                onTabSelected = onTabSelected
            )
            
            // Calendar Integration Dialog
            if (showCalendarDialog) {
                CalendarIntegrationDialog(
                    challengeName = "21-Day Cognitive Clarity",
                    durationDays = 21,
                    onDismiss = { showCalendarDialog = false },
                    onAddToCalendar = { reminderTime ->
                        pendingReminderTime = reminderTime
                        showCalendarDialog = false
                        
                        // Check permissions and create events
                        if (calendarHelper.hasCalendarPermissions()) {
                            // Create all 21 events
                            val startDate = Calendar.getInstance()
                            val eventIds = calendarHelper.createChallengeEvents(
                                challengeName = "Cognitive Clarity",
                                durationDays = 21,
                                startDate = startDate,
                                dailyReminderTime = reminderTime
                            )
                            eventsCreated = eventIds.size
                            showSuccessNotification = true
                            onStartChallenge()
                        }
                    },
                    onSkip = {
                        showCalendarDialog = false
                        onStartChallenge()
                    }
                )
            }
            
            // Calendar Permission Handler (for when permissions not granted)
            if (pendingReminderTime != null && !calendarHelper.hasCalendarPermissions()) {
                CalendarPermissionHandler(
                    onPermissionsGranted = {
                        // Create events now that we have permission
                        val startDate = Calendar.getInstance()
                        val eventIds = calendarHelper.createChallengeEvents(
                            challengeName = "Cognitive Clarity",
                            durationDays = 21,
                            startDate = startDate,
                            dailyReminderTime = pendingReminderTime!!
                        )
                        eventsCreated = eventIds.size
                        showSuccessNotification = true
                        pendingReminderTime = null
                        onStartChallenge()
                    },
                    onPermissionsDenied = {
                        // User denied permissions, continue without calendar
                        pendingReminderTime = null
                        onStartChallenge()
                    }
                ) { requestPermissions ->
                    // Show permission request
                    requestPermissions()
                }
            }
            
            // Success Notification
            CalendarSuccessNotification(
                eventsCount = eventsCreated,
                visible = showSuccessNotification,
                onDismiss = { showSuccessNotification = false }
            )
        }
    }
}

@Composable
private fun ChallengeListCard(
    challenge: ChallengeSummary,
    onClick: () -> Unit
) {
    val challengeColor = challenge.color?.let { Color(android.graphics.Color.parseColor(it)) } ?: Accent
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(challengeColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = challenge.icon ?: "🎯",
                    fontSize = 28.sp
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = challenge.name,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${challenge.completedDays}/${challenge.duration} days • ${challenge.currentStreak}🔥 streak",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                // Progress bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFF2A2A30))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(challenge.progress / 100f)
                            .fillMaxHeight()
                            .background(challengeColor)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Progress percentage
            Text(
                text = "${challenge.progress}%",
                color = challengeColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun EmptyChallengesView(
    onCreateChallenge: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.EmojiEvents,
                contentDescription = "No Challenges",
                tint = Accent,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No challenges yet",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Create your first challenge to start building habits!",
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onCreateChallenge,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Accent,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Create Challenge",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// Real API call to fetch user challenges from database
private suspend fun loadUserChallengesFromApi(
    userId: String,
    onLoaded: (List<ChallengeSummary>) -> Unit
) {
    try {
        val response: ChallengesResponse = httpClient.get("$BASE_URL/api/challenges/user/$userId").body()
        
        if (response.success) {
            val challenges = response.challenges.map { c ->
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
                    isActive = c.isActive
                )
            }
            onLoaded(challenges)
        } else {
            onLoaded(emptyList())
        }
    } catch (e: Exception) {
        onLoaded(emptyList())
    }
}

@kotlinx.serialization.Serializable
data class ChallengesResponse(
    val success: Boolean,
    val challenges: List<ChallengeApiData>
)

@kotlinx.serialization.Serializable
data class ChallengeApiData(
    val id: String,
    val name: String,
    val description: String?,
    val type: String,
    val category: String,
    val duration: Int,
    val color: String?,
    val icon: String?,
    val isActive: Boolean,
    val progress: ChallengeProgressData
)

@kotlinx.serialization.Serializable
data class ChallengeProgressData(
    val completedDays: Int,
    val totalDays: Int,
    val percentage: Int,
    val currentStreak: Int,
    val isTodayCompleted: Boolean = false,
    val currentDayNumber: Int = 1
)

@Composable
private fun WaterChallengeCard(
    onStartWaterChallenge: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(22.dp)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Health & Wellness",
                    color = Color(0xFF4FC3F7),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "30-Day Water Challenge",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "HYDRATE YOUR BODY",
                    color = Color(0xFF4FC3F7),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Duration: 30 Days   Target: 2 Liters/Day",
                    color = Color.LightGray,
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = onStartWaterChallenge,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4FC3F7),
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Start Water Challenge",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Water drop icon representation
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF1E3A5F)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.WaterDrop,
                    contentDescription = "Water",
                    tint = Color(0xFF4FC3F7),
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}

@Composable
private fun CategoryPill(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(if (selected) Accent else CardDark)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            color = if (selected) Color.Black else Color.Gray,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

