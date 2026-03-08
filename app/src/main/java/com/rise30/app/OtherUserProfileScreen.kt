package com.rise30.app

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class FriendshipStatusResponse(
    val success: Boolean,
    val status: String
)

@Composable
fun OtherUserProfileScreen(
    currentUserId: String,
    otherUserId: String,
    onBack: () -> Unit
) {
    var profile by remember { mutableStateOf<UserProfile?>(null) }
    var friendshipStatus by remember { mutableStateOf("none") }
    var achievements by remember { mutableStateOf<List<Achievement>>(emptyList()) }
    var challengeHistory by remember { mutableStateOf<List<ChallengeSummary>>(emptyList()) }
    var historyFilter by remember { mutableStateOf("All") }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(otherUserId) {
        isLoading = true
        try {
            // Fetch profile
            val profResp: UserProfileResponse = ApiConfig.httpClient.get("${ApiConfig.BASE_URL}/api/users/$otherUserId/profile").body()
            if (profResp.success) {
                profile = profResp.profile
            }
            
            // Fetch friendship status
            val statusResp: FriendshipStatusResponse = ApiConfig.httpClient.get("${ApiConfig.BASE_URL}/api/friends/status/$currentUserId/$otherUserId").body()
            if (statusResp.success) {
                friendshipStatus = statusResp.status
            }

            // Fetch achievements
            try {
                val cachedAchievements = CacheManager.getAchievements(context, otherUserId)
                if (cachedAchievements != null) {
                    achievements = cachedAchievements
                }
                val achResp: AchievementsResponse = ApiConfig.httpClient.get("${ApiConfig.BASE_URL}/api/users/$otherUserId/achievements").body()
                if (achResp.success) {
                    achievements = achResp.achievements
                    CacheManager.saveAchievements(context, otherUserId, achResp.achievements)
                }
            } catch (e: Exception) {}

            // Fetch challenge history
            val chalResp: ChallengesResponse = ApiConfig.httpClient.get("${ApiConfig.BASE_URL}/api/challenges/user/$otherUserId").body()
            if (chalResp.success) {
                val summaries = chalResp.challenges.map { c ->
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
                challengeHistory = summaries
            }
        } catch (e: Exception) {
            // handle error
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
                    .height(56.dp)
            ) {
                IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart).padding(start = 16.dp)) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text(
                    "Profile",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Accent)
            }
        } else {
            profile?.let { user ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // User Header
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Accent.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            val currentAvatar = user.avatarUrl
                            if (!currentAvatar.isNullOrBlank()) {
                                Text(
                                    text = currentAvatar,
                                    fontSize = 42.sp
                                )
                            } else {
                                Text(
                                    text = (user.displayName ?: user.email).take(1).uppercase(),
                                    color = Accent,
                                    fontSize = 42.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = user.displayName ?: "User",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Text(
                            text = user.email,
                            color = Color.Gray,
                            fontSize = 15.sp
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Action Button
                        Button(
                            onClick = {
                                if (friendshipStatus == "none") {
                                    scope.launch {
                                        try {
                                            ApiConfig.httpClient.post("${ApiConfig.BASE_URL}/api/friends/request") {
                                                contentType(ContentType.Application.Json)
                                                setBody(mapOf("userId" to currentUserId, "friendId" to otherUserId))
                                            }
                                            friendshipStatus = "pending"
                                        } catch (e: Exception) {}
                                    }
                                } else if (friendshipStatus == "received") {
                                    scope.launch {
                                        try {
                                            ApiConfig.httpClient.post("${ApiConfig.BASE_URL}/api/friends/accept") {
                                                contentType(ContentType.Application.Json)
                                                setBody(mapOf("userId" to currentUserId, "friendId" to otherUserId))
                                            }
                                            friendshipStatus = "accepted"
                                        } catch (e: Exception) {}
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (friendshipStatus == "accepted") Color(0xFF4CAF50).copy(alpha = 0.2f) else if (friendshipStatus == "pending") Color.Gray.copy(alpha = 0.2f) else Accent,
                                contentColor = if (friendshipStatus == "accepted") Color(0xFF4CAF50) else Color.Black
                            ),
                            enabled = friendshipStatus == "none" || friendshipStatus == "received"
                        ) {
                            Icon(
                                imageVector = if (friendshipStatus == "accepted") Icons.Default.Check else Icons.Default.PersonAdd,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when(friendshipStatus) {
                                    "accepted" -> "Friends"
                                    "pending" -> "Pending Request"
                                    "received" -> "Accept Request"
                                    else -> "Add Friend"
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Stats Section
                    Text(
                        "Journey Stats",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            label = "Longest Streak",
                            value = "${user.stats.longestStreak} Days",
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = "Completed",
                            value = "${user.stats.completedChallenges}",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        "Badges",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (achievements.isEmpty()) {
                        Text(
                            text = "No badges earned yet.",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            achievements.forEach { badge ->
                                BadgeItem(
                                    name = badge.name,
                                    category = badge.description,
                                    icon = null,
                                    painter = null,
                                    emojiIcon = badge.icon
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Text(
                        text = "Challenge History",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("All", "Completed", "Active").forEach { label ->
                            HistoryTabPill(
                                label = label,
                                selected = historyFilter == label,
                                onClick = { historyFilter = label }
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    val filteredHistory = when (historyFilter) {
                        "Completed" -> challengeHistory.filter { it.progress >= 100 }
                        "Active" -> challengeHistory.filter { it.isActive && it.progress < 100 }
                        else -> challengeHistory
                    }

                    if (filteredHistory.isEmpty()) {
                        Text(
                            text = "No challenges found.",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            filteredHistory.forEach { challenge ->
                                HistoryItem(
                                    title = challenge.name,
                                    progress = "${challenge.completedDays}/${challenge.duration} Days",
                                    nextMilestone = if (challenge.progress >= 100) "Completed" else "Keep going!",
                                    progressValue = challenge.progress / 100f
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(120.dp))
                }
            } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("User not found", color = Color.Gray)
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(CardDark)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(label, color = Color.Gray, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}
