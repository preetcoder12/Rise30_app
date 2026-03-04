// (YOUR IMPORTS REMAIN SAME – no change needed)
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import kotlin.math.roundToInt

// 🌑 Premium Colors
val BackgroundDark = Color(0xFF0D0D0F)
val CardDark = Color(0xFF1A1A1F)
val Accent = Color(0xFFFFD54F)
val AccentSoft = Color(0x33FFD54F)

@Composable
private fun TopSection(
    userName: String,
    onSignOut: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome back,",
                color = Color.Gray,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = userName,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        TextButton(
            onClick = onSignOut,
            shape = RoundedCornerShape(50)
        ) {
            Text(
                text = "Sign out",
                color = Accent,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun MainChallengeCard(
    currentDay: Int,
    totalDays: Int,
    onMarkComplete: () -> Unit
) {
    val progress = remember(currentDay, totalDays) {
        (currentDay.coerceAtLeast(0).coerceAtMost(totalDays).toFloat() / totalDays.coerceAtLeast(1))
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
                        color = AccentSoft,
                        style = Stroke(width = strokeWidth)
                    )

                    // Progress arc
                    drawArc(
                        color = Accent,
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
                    text = "Rise30 Focus Challenge",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Lock in one deep-work block every day for 30 days.",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onMarkComplete,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Accent,
                        contentColor = Color.Black
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Mark today complete",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun WeeklyOverview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(CardDark)
            .padding(20.dp)
    ) {
        Text(
            text = "📆 Weekly Overview",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Track your last 7 days of focus blocks.",
            color = Color.Gray,
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        val fakeData = listOf(0.2f, 0.8f, 0.6f, 1f, 0.4f, 0.9f, 0.7f)
        val dayLabels = listOf("M", "T", "W", "T", "F", "S", "S")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            fakeData.forEachIndexed { index, value ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .width(16.dp)
                            .height(70.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFF2A2A30))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(value)
                                .align(Alignment.BottomCenter)
                                .clip(RoundedCornerShape(50))
                                .background(Accent)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = dayLabels[index],
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }
            }
        }
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
        Surface(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(30.dp)),
            color = CardDark,
            tonalElevation = 8.dp,
            shadowElevation = 16.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomBarItem(
                    label = "Home",
                    tab = MainTab.Home,
                    currentTab = currentTab,
                    onTabSelected = onTabSelected
                )
                BottomBarItem(
                    label = "Challenges",
                    tab = MainTab.Challenges,
                    currentTab = currentTab,
                    onTabSelected = onTabSelected
                )
                BottomBarItem(
                    label = "Notifications",
                    tab = MainTab.Notifications,
                    currentTab = currentTab,
                    onTabSelected = onTabSelected
                )
                BottomBarItem(
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
    label: String,
    tab: MainTab,
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    val selected = currentTab == tab
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onTabSelected(tab) }
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(if (selected) Accent else Color.Transparent)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            color = if (selected) Accent else Color.Gray,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
fun HomePage(
    userName: String,
    onMarkComplete: () -> Unit,
    onSignOut: () -> Unit,
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundDark
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(20.dp)
            ) {

                TopSection(userName, onSignOut)

                Spacer(modifier = Modifier.height(30.dp))

                MainChallengeCard(
                    currentDay = 12,
                    totalDays = 30,
                    onMarkComplete = onMarkComplete
                )

                Spacer(modifier = Modifier.height(30.dp))

                WeeklyOverview()

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

            HomeFloatingBottomBar(
                currentTab = currentTab,
                onTabSelected = onTabSelected
            )
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
            .clip(RoundedCornerShape(24.dp))
            .background(CardDark)
            .padding(20.dp)
    ) {

        Text(
            "🛡 Streak Shield",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Accent
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Your 12-day streak is protected.",
            color = Color.White,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        LinearProgressIndicator(
            progress = 0.8f,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50)),
            color = Accent,
            trackColor = AccentSoft
        )
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