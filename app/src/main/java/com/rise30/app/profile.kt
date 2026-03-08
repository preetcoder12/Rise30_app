package com.rise30.app

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Serializable
data class UserProfileResponse(
    val success: Boolean,
    val profile: UserProfile
)

@Serializable
data class UserProfile(
    val id: String,
    val email: String,
    val displayName: String? = null,
    val createdAt: String,
    val stats: UserStats
)

@Serializable
data class UserStats(
    val totalChallenges: Int,
    val activeChallenges: Int,
    val completedChallenges: Int,
    val totalCompletedDays: Int,
    val longestStreak: Int
)

@Serializable
data class AchievementsResponse(
    val success: Boolean,
    val achievements: List<Achievement>
)

@Serializable
data class Achievement(
    val id: String,
    val name: String,
    val description: String,
    val icon: String,
    val unlockedAt: String? = null
)

// Format ISO date string to "MMM yyyy"
private fun formatJoinDate(isoDate: String): String {
    return try {
        val inputFmt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        inputFmt.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFmt.parse(isoDate) ?: return "Unknown"
        val outputFmt = SimpleDateFormat("MMM yyyy", Locale.getDefault())
        outputFmt.format(date)
    } catch (e: Exception) {
        "Unknown"
    }
}

// Format today's date as "EEEE, MMMM d"
private fun getTodayDisplayDate(): String {
    val sdf = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
    return sdf.format(Date())
}

@Composable
fun ProfilePage(
    userId: String,
    userName: String,
    onSignOut: () -> Unit,
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    // Profile data state
    var userProfile by remember {
        mutableStateOf<UserProfile?>(CacheManager.getProfile(context, userId)?.profile)
    }
    var userStats by remember {
        mutableStateOf<UserStats?>(CacheManager.getProfile(context, userId)?.profile?.stats)
    }
    var isLoading by remember { mutableStateOf(userStats == null) }

    // Challenge history state (real data from API)
    var challengeHistory by remember { mutableStateOf<List<ChallengeSummary>>(emptyList()) }
    var historyFilter by remember { mutableStateOf("All") }

    // Achievements (badges) state
    var achievements by remember { mutableStateOf<List<Achievement>>(emptyList()) }

    // Edit Profile dialog state
    var showEditDialog by remember { mutableStateOf(false) }
    var editDisplayName by remember { mutableStateOf(userName) }

    // Load profile, challenges, and achievements
    LaunchedEffect(userId) {
        scope.launch {
            try {
                val response: UserProfileResponse =
                    ApiConfig.httpClient.get("${ApiConfig.BASE_URL}/api/users/$userId/profile").body()
                if (response.success) {
                    userProfile = response.profile
                    userStats = response.profile.stats
                    CacheManager.saveProfile(context, userId, response)
                }
            } catch (e: Exception) {
                // Use cached on error
            } finally {
                isLoading = false
            }
        }

        // Load challenge history
        scope.launch {
            try {
                val cached = CacheManager.getChallenges(context, userId)
                if (cached != null) challengeHistory = cached
                val response: ChallengesResponse =
                    ApiConfig.httpClient.get("${ApiConfig.BASE_URL}/api/challenges/user/$userId").body()
                if (response.success) {
                    val summaries = response.challenges.map { c ->
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
                    CacheManager.saveChallenges(context, userId, summaries)
                }
            } catch (_: Exception) {}
        }

        // Load achievements
        scope.launch {
            try {
                val response: AchievementsResponse =
                    ApiConfig.httpClient.get("${ApiConfig.BASE_URL}/api/users/$userId/achievements").body()
                if (response.success) {
                    achievements = response.achievements
                }
            } catch (_: Exception) {}
        }
    }

    val filteredHistory = when (historyFilter) {
        "Completed" -> challengeHistory.filter { it.progress >= 100 }
        "Active" -> challengeHistory.filter { it.isActive && it.progress < 100 }
        else -> challengeHistory
    }

    val joinDate = userProfile?.createdAt?.let { formatJoinDate(it) } ?: "—"
    val todayDate = remember { getTodayDisplayDate() }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF151525), Color(0xFF090912)),
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
                // --- Header ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, bottom = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Rise30",
                        color = Accent,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = todayDate,
                        color = Color.LightGray,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(9.dp))

                // --- Profile Info ---
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(139.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Accent)
                            .padding(3.dp)
                            .clip(RoundedCornerShape(22.dp))
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = CardDark
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Person,
                                contentDescription = "Profile Picture",
                                tint = Accent,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = userName,
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = if (joinDate == "—") "Join date loading…" else "Joined: $joinDate",
                        color = Color.Gray,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- My Progress Dashboard ---
                Text(
                    text = "My Progress Dashboard",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Rounded.Whatshot,
                        title = "Longest Streak",
                        value = if (isLoading) "..." else "${userStats?.longestStreak ?: 0} Days",
                        subtitle = "Best Streak"
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        iconPainter = painterResource(id = R.drawable.trophie),
                        title = "Challenges Completed",
                        value = if (isLoading) "..." else "${userStats?.completedChallenges ?: 0} ${if (userStats?.completedChallenges == 1) "challenge" else "challenges"}",
                        subtitle = "Total: ${userStats?.totalChallenges ?: 0}"
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Rounded.LocalFireDepartment,
                        title = "Active Challenges",
                        value = if (isLoading) "..." else "${userStats?.activeChallenges ?: 0} ${if (userStats?.activeChallenges == 1) "challenge" else "challenges"}",
                        subtitle = "In Progress"
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Rounded.Star,
                        title = "Days Completed",
                        value = if (isLoading) "..." else "${userStats?.totalCompletedDays ?: 0} ${if (userStats?.totalCompletedDays == 1) "day" else "days"}",
                        subtitle = "Total Days"
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- Unlocked Badges (real achievements) ---
                Text(
                    text = "Unlocked Badges",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (achievements.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(CardDark)
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Rounded.EmojiEvents,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No badges yet — start your first challenge!",
                                color = Color.Gray,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
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

                Spacer(modifier = Modifier.height(28.dp))

                // --- Challenge History (real data) ---
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Challenge History",
                        color = Color.White,
                        fontSize = 17.sp,
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
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (challengeHistory.isEmpty() && !isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(CardDark)
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No challenge history yet.",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    filteredHistory.forEach { challenge ->
                        val isCompleted = challenge.progress >= 100
                        if (isCompleted) {
                            HistoryItemCompleted(
                                title = challenge.name,
                                awardText = "Completed",
                                duration = "Duration: ${challenge.duration} Days"
                            )
                        } else {
                            HistoryItem(
                                title = challenge.name,
                                progress = "Day ${challenge.completedDays} of ${challenge.duration}",
                                nextMilestone = "${challenge.progress}% complete",
                                progressValue = challenge.progress / 100f
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- Account & Settings ---
                Text(
                    text = "Account & Settings",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        editDisplayName = userName
                        showEditDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Edit Profile", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                SettingsItem(
                    label = "Total Challenges",
                    value = if (isLoading) "..." else "${userStats?.totalChallenges ?: 0}",
                    valueColor = Accent
                )

                SettingsItem(
                    label = "Active Challenges",
                    value = if (isLoading) "..." else "${userStats?.activeChallenges ?: 0}",
                    valueColor = Color.Gray
                )

                SettingsItem(
                    label = "Completed Days",
                    value = if (isLoading) "..." else "${userStats?.totalCompletedDays ?: 0}",
                    valueColor = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                TextButton(
                    onClick = onSignOut,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 120.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Sign Out", color = Color.Red, fontSize = 15.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Image(
                            painter = painterResource(id = R.drawable.signout),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        } // close Box
    }

    // --- Edit Profile Dialog ---
    if (showEditDialog) {
        Dialog(onDismissRequest = { showEditDialog = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF1E1E2E), Color(0xFF0E0E18))
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
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Text(
                        text = "Edit Profile",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = editDisplayName,
                        onValueChange = { editDisplayName = it },
                        label = { Text("Display Name", color = Color.Gray) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Accent,
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.4f),
                            cursorColor = Accent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showEditDialog = false },
                            modifier = Modifier.weight(1f),
                            border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.4f)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Cancel", color = Color.Gray)
                        }
                        Button(
                            onClick = {
                                showEditDialog = false
                                // API call to update profile name (stored in Supabase user metadata)
                                scope.launch {
                                    try {
                                        ApiConfig.httpClient.put("${ApiConfig.BASE_URL}/api/users/$userId/profile") {
                                            contentType(ContentType.Application.Json)
                                            setBody(mapOf("displayName" to editDisplayName))
                                        }
                                    } catch (_: Exception) {}
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Accent,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Save", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    iconPainter: Painter? = null,
    title: String,
    value: String,
    subtitle: String
) {
    val parts = value.split(" ")
    val n = parts.getOrNull(0) ?: ""
    val unit = if (parts.size > 1) parts.subList(1, parts.size).joinToString(" ") else ""

    Box(
        modifier = modifier
            .height(171.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF1C1C2C), Color(0xFF0E0E18))
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
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (iconPainter != null) {
                    Image(
                        painter = iconPainter,
                        contentDescription = null,
                        modifier = Modifier.size(35.dp)
                    )
                } else if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Accent,
                        modifier = Modifier.size(35.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    color = Color.LightGray,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 20.sp
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = n,
                        color = Color.White,
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (unit.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = unit,
                            color = Color.White,
                            fontSize = 15.sp
                        )
                    }
                }
                if (subtitle.isNotEmpty()) {
                    Text(
                        text = subtitle,
                        color = Color.Gray,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun BadgeItem(
    name: String,
    category: String,
    painter: Painter? = null,
    icon: ImageVector? = null,
    emojiIcon: String? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .clip(HexagonShape())
                .background(CardDark)
                .border(2.dp, Accent, HexagonShape()),
            contentAlignment = Alignment.Center
        ) {
            when {
                painter != null -> Image(
                    painter = painter,
                    contentDescription = name,
                    modifier = Modifier.size(50.dp)
                )
                icon != null -> Icon(
                    imageVector = icon,
                    contentDescription = name,
                    tint = Accent,
                    modifier = Modifier.size(42.dp)
                )
                emojiIcon != null -> Text(
                    text = emojiIcon,
                    fontSize = 36.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = category,
            color = Color.Gray,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun HistoryTabPill(label: String, selected: Boolean, onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(if (selected) Accent else CardDark)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            color = if (selected) Color.Black else Color.Gray,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun HistoryItem(
    title: String,
    progress: String,
    nextMilestone: String,
    progressValue: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(61.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = Color(0xFF2A2A30),
                        style = Stroke(width = 4.dp.toPx())
                    )
                    drawArc(
                        color = Accent,
                        startAngle = -90f,
                        sweepAngle = 360f * progressValue,
                        useCenter = false,
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                Icon(Icons.Rounded.Eco, contentDescription = null, tint = Accent, modifier = Modifier.size(22.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = progress, color = Color.LightGray, fontSize = 14.sp)
                Text(text = nextMilestone, color = Accent, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun HistoryItemCompleted(
    title: String,
    awardText: String,
    duration: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(27.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = awardText,
                        color = Color(0xFF4CAF50),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(text = duration, color = Color.Gray, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun SettingsItem(
    label: String,
    value: String,
    valueColor: Color = Color.Gray
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                color = valueColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun GoalCard(
    modifier: Modifier = Modifier,
    title: String,
    goalName: String,
    progressText: String,
    progressValue: Float,
    subtext: String
) {
    Card(
        modifier = modifier.height(110.dp),
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = AccentSoft,
                        style = Stroke(width = 4.dp.toPx())
                    )
                    drawArc(
                        color = Accent,
                        startAngle = -90f,
                        sweepAngle = 360f * progressValue,
                        useCenter = false,
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                Text(text = subtext, color = Color.Gray, fontSize = 8.sp)
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(verticalArrangement = Arrangement.Center) {
                Text(text = title, color = Color.LightGray, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                Text(text = goalName, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(text = progressText, color = Accent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

class HexagonShape : Shape {
    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: androidx.compose.ui.unit.LayoutDirection,
        density: androidx.compose.ui.unit.Density
    ): Outline {
        val path = Path().apply {
            val width = size.width
            val height = size.height

            moveTo(width / 2, 0f)
            lineTo(width, height * 0.25f)
            lineTo(width, height * 0.75f)
            lineTo(width / 2, height)
            lineTo(0f, height * 0.75f)
            lineTo(0f, height * 0.25f)
            close()
        }
        return Outline.Generic(path)
    }
}
