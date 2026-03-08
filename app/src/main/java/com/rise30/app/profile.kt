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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileResponse(
    val success: Boolean,
    val profile: UserProfile
)

@Serializable
data class UserProfile(
    val id: String,
    val email: String,
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

@Composable
fun ProfilePage(
    userId: String,
    userName: String,
    onSignOut: () -> Unit,
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    
    // State for user profile data
    var userStats by remember { mutableStateOf<UserStats?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    
    // Fetch user profile data
    LaunchedEffect(userId) {
        scope.launch {
            try {
                val response: UserProfileResponse = httpClient.get("$BASE_URL/api/users/$userId/profile").body()
                if (response.success) {
                    userStats = response.profile.stats
                }
            } catch (e: Exception) {
                // Use null on error
            }
            isLoading = false
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
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Monday, July 22", // Should be dynamic in real app
                        color = Color.LightGray,
                        fontSize = 14.sp
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
                            .size(110.dp)
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
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Join Date: Mar 2024",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                // --- My Progress Dashboard / Profile Stats ---
                Text(
                    text = "My Progress Dashboard",
                    color = Color.White,
                    fontSize = 18.sp,
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
                        icon = Icons.Rounded.EmojiEvents,
                        title = "Challenges Completed",
                        value = if (isLoading) "..." else "${userStats?.completedChallenges ?: 0}",
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
                        value = if (isLoading) "..." else "${userStats?.activeChallenges ?: 0}",
                        subtitle = "In Progress"
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Rounded.Star,
                        title = "Days Completed",
                        value = if (isLoading) "..." else "${userStats?.totalCompletedDays ?: 0}",
                        subtitle = "Total Days"
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                // --- Unlocked Badges ---
                Text(
                    text = "Unlocked Badges",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BadgeItem("Neural Tree", "Cognitive Clarity", painterResource(id = R.drawable.tree))
                    BadgeItem("Runner", "Fitness", null, Icons.Rounded.DirectionsRun)
                    BadgeItem("Book", "Atomic Habits", null, Icons.Rounded.AutoStories)
                }

                Spacer(modifier = Modifier.height(30.dp))

                // --- Challenge History ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Challenge History",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        HistoryTabPill("All", true)
                        HistoryTabPill("Completed", false)
                        HistoryTabPill("Active", false)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                HistoryItem(
                    title = "30-Day Deep Focus Mastery",
                    progress = "Day 12 of 30",
                    nextMilestone = "Next Milestone: Day 15",
                    progressValue = 12f / 30f
                )

                Spacer(modifier = Modifier.height(12.dp))

                HistoryItemCompleted(
                    title = "21-Day Cognitive Clarity Intensive",
                    awardText = "Awarded",
                    duration = "Duration: 21 Days"
                )

                Spacer(modifier = Modifier.height(12.dp))

                HistoryItem(
                    title = "14-Day Coding Sprint",
                    progress = "Day 12 of 14",
                    nextMilestone = "Next Milestone: Day 14",
                    progressValue = 12f / 14f
                )

                Spacer(modifier = Modifier.height(30.dp))

                // --- Account & Settings (from third screen) ---
                Text(
                    text = "Account & Settings",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Edit Profile", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                SettingsItem(
                    label = "Subscription Plan",
                    value = "Rise30 Premium",
                    valueColor = Accent
                )

                SettingsItem(
                    label = "Notifications",
                    value = ""
                )

                SettingsItem(
                    label = "Privacy & Security",
                    value = ""
                )

                Spacer(modifier = Modifier.height(24.dp))

                TextButton(
                    onClick = onSignOut,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Sign Out", color = Color.Red)
                }

        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    value: String,
    subtitle: String
) {
    Card(
        modifier = modifier.height(110.dp),
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Accent,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    color = Color.LightGray,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 12.sp
                )
            }

            Column {
                Text(
                    text = value,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                if (subtitle.isNotEmpty()) {
                    Text(
                        text = subtitle,
                        color = Color.Gray,
                        fontSize = 9.sp
                    )
                }
            }
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
                modifier = Modifier.size(40.dp),
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
                Text(
                    text = subtext,
                    color = Color.Gray,
                    fontSize = 8.sp,
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = title,
                    color = Color.LightGray,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = goalName,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = progressText,
                    color = Accent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun BadgeItem(
    name: String,
    category: String,
    painter: Painter? = null,
    icon: ImageVector? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(HexagonShape())
                .background(CardDark)
                .border(2.dp, Accent, HexagonShape()),
            contentAlignment = Alignment.Center
        ) {
            if (painter != null) {
                Image(
                    painter = painter,
                    contentDescription = name,
                    modifier = Modifier.size(40.dp)
                )
            } else if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = name,
                    tint = Accent,
                    modifier = Modifier.size(35.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = category,
            color = Color.Gray,
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun HistoryTabPill(label: String, selected: Boolean) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(if (selected) Accent else CardDark)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = if (selected) Color.Black else Color.Gray,
            fontSize = 10.sp,
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
                modifier = Modifier.size(50.dp),
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
                // Small tree icon or similar
                Icon(Icons.Rounded.Eco, contentDescription = null, tint = Accent, modifier = Modifier.size(20.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = progress,
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
                Text(
                    text = nextMilestone,
                    color = Color.Gray,
                    fontSize = 11.sp
                )
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
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Check, contentDescription = null, tint = Color.White)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = awardText,
                        color = Color(0xFF4CAF50),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = duration,
                    color = Color.Gray,
                    fontSize = 11.sp
                )
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
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                color = valueColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
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
            val radius = width / 2
            val centerX = width / 2
            val centerY = height / 2

            moveTo(centerX, 0f)
            lineTo(width, height * 0.25f)
            lineTo(width, height * 0.75f)
            lineTo(centerX, height)
            lineTo(0f, height * 0.75f)
            lineTo(0f, height * 0.25f)
            close()
        }
        return Outline.Generic(path)
    }
}
