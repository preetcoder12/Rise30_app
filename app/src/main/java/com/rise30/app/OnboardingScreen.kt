package com.rise30.app

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Onboarding data model
data class OnboardingData(
    val goal: String = "",
    val challengeType: String = "",
    val motivation: String = "",
    val difficulty: String = "",
    val challengeName: String = "",
    val dailyTarget: Int = 30,
    val reminderTime: String = "8:00 AM",
    val startDate: String = "Today"
)

sealed class OnboardingStep(val index: Int, val totalSteps: Int = 6) {
    object Welcome : OnboardingStep(0)
    object GoalSelection : OnboardingStep(1)
    object ChallengeType : OnboardingStep(2)
    object Motivation : OnboardingStep(3)
    object Difficulty : OnboardingStep(4)
    object Preview : OnboardingStep(5)
}

@Composable
fun OnboardingScreen(
    onComplete: (OnboardingData) -> Unit,
    onSkip: () -> Unit
) {
    var currentStep by remember { mutableStateOf<OnboardingStep>(OnboardingStep.Welcome) }
    var onboardingData by remember { mutableStateOf(OnboardingData()) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Charcoal)
    ) {
        // Progress dots at top
        if (currentStep.index > 0) {
            ProgressIndicator(
                currentStep = currentStep.index,
                totalSteps = currentStep.totalSteps,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 60.dp)
            )
        }
        
        // Back button
        if (currentStep.index > 0) {
            IconButton(
                onClick = {
                    currentStep = when (currentStep) {
                        OnboardingStep.GoalSelection -> OnboardingStep.Welcome
                        OnboardingStep.ChallengeType -> OnboardingStep.GoalSelection
                        OnboardingStep.Motivation -> OnboardingStep.ChallengeType
                        OnboardingStep.Difficulty -> OnboardingStep.Motivation
                        OnboardingStep.Preview -> OnboardingStep.Difficulty
                        else -> OnboardingStep.Welcome
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp, top = 50.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        
        // Skip button
        if (currentStep.index > 0 && currentStep.index < 5) {
            TextButton(
                onClick = onSkip,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 16.dp, top = 50.dp)
            ) {
                Text(
                    text = "Skip",
                    color = Color(0xFF8C8C8C),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        
        // Animated content
        AnimatedContent(
            targetState = currentStep,
            transitionSpec = {
                slideInHorizontally { it } + fadeIn() togetherWith
                slideOutHorizontally { -it } + fadeOut()
            },
            modifier = Modifier.fillMaxSize()
        ) { step ->
            when (step) {
                OnboardingStep.Welcome -> WelcomeScreen(
                    onGetStarted = { currentStep = OnboardingStep.GoalSelection }
                )
                OnboardingStep.GoalSelection -> GoalSelectionScreen(
                    selectedGoal = onboardingData.goal,
                    onGoalSelected = { goal ->
                        onboardingData = onboardingData.copy(goal = goal)
                        currentStep = OnboardingStep.ChallengeType
                    }
                )
                OnboardingStep.ChallengeType -> ChallengeTypeScreen(
                    selectedType = onboardingData.challengeType,
                    onTypeSelected = { type ->
                        onboardingData = onboardingData.copy(challengeType = type)
                        currentStep = OnboardingStep.Motivation
                    }
                )
                OnboardingStep.Motivation -> MotivationScreen(
                    selectedMotivation = onboardingData.motivation,
                    onMotivationSelected = { motivation ->
                        onboardingData = onboardingData.copy(motivation = motivation)
                        currentStep = OnboardingStep.Difficulty
                    }
                )
                OnboardingStep.Difficulty -> DifficultyScreen(
                    selectedDifficulty = onboardingData.difficulty,
                    onDifficultySelected = { difficulty ->
                        onboardingData = onboardingData.copy(difficulty = difficulty)
                        currentStep = OnboardingStep.Preview
                    }
                )
                OnboardingStep.Preview -> PreviewScreen(
                    data = onboardingData,
                    onStartChallenge = { onComplete(onboardingData) }
                )
            }
        }
    }
}

@Composable
private fun ProgressIndicator(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(totalSteps) { index ->
            Box(
                modifier = Modifier
                    .width(if (index < currentStep) 24.dp else 8.dp)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        if (index < currentStep) LemonYellow else Color(0xFF3A3A3A)
                    )
                    .animateContentSize()
            )
        }
    }
}

@Composable
private fun WelcomeScreen(
    onGetStarted: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp)
            .padding(bottom = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))
        
        // Logo
        Image(
            painter = painterResource(id = R.drawable.bg_removed_applogo),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(120.dp)
        )
        
        Spacer(modifier = Modifier.height(40.dp))
        
        Text(
            text = "Build powerful habits\nin just 30 days",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 40.sp
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Track progress. Stay consistent. Achieve more.",
            fontSize = 18.sp,
            color = Color(0xFF8C8C8C),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = onGetStarted,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LemonYellow,
                contentColor = Charcoal
            )
        ) {
            Text(
                text = "Get Started",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun GoalSelectionScreen(
    selectedGoal: String,
    onGoalSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 120.dp, bottom = 40.dp)
    ) {
        Text(
            text = "What do you want\nto improve?",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            lineHeight = 36.sp
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Select one that fits your goals",
            fontSize = 16.sp,
            color = Color(0xFF8C8C8C)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        val goals = listOf(
            Triple("💪", "Fitness", "Get stronger & healthier"),
            Triple("📚", "Study", "Learn new skills daily"),
            Triple("🧠", "Mental Health", "Build mindfulness habits"),
            Triple("💰", "Productivity", "Achieve more every day"),
            Triple("🌱", "Self Growth", "Become your best self")
        )
        
        goals.forEach { (emoji, title, subtitle) ->
            GoalCard(
                emoji = emoji,
                title = title,
                subtitle = subtitle,
                isSelected = selectedGoal == title,
                onClick = { onGoalSelected(title) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun GoalCard(
    emoji: String,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 0.98f else 1f,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 300f)
    )
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) LemonYellow else Color(0xFF1A1A1A))
            .border(
                width = if (isSelected) 0.dp else 1.dp,
                color = Color(0xFF2A2A2A),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = emoji,
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSelected) Charcoal else Color.White
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = if (isSelected) Charcoal.copy(alpha = 0.7f) else Color(0xFF8C8C8C)
                )
            }
        }
    }
}

@Composable
private fun ChallengeTypeScreen(
    selectedType: String,
    onTypeSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 120.dp, bottom = 40.dp)
    ) {
        Text(
            text = "What type of\nchallenge?",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            lineHeight = 36.sp
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        val types = listOf(
            Triple("🎯", "Build a Habit", "Create positive daily routines"),
            Triple("🚫", "Break a Bad Habit", "Quit something harmful"),
            Triple("📊", "Track Progress", "Monitor daily improvements")
        )
        
        types.forEach { (emoji, title, subtitle) ->
            GoalCard(
                emoji = emoji,
                title = title,
                subtitle = subtitle,
                isSelected = selectedType == title,
                onClick = { onTypeSelected(title) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun MotivationScreen(
    selectedMotivation: String,
    onMotivationSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 120.dp, bottom = 40.dp)
    ) {
        Text(
            text = "Why do you want\nto start?",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            lineHeight = 36.sp
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "This helps us personalize your experience",
            fontSize = 16.sp,
            color = Color(0xFF8C8C8C)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        val motivations = listOf(
            "Become disciplined",
            "Improve health",
            "Learn something new",
            "Stay consistent",
            "Prove to myself"
        )
        
        motivations.forEach { motivation ->
            SelectionChip(
                text = motivation,
                isSelected = selectedMotivation == motivation,
                onClick = { onMotivationSelected(motivation) }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun SelectionChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) LemonYellow else Color(0xFF1A1A1A))
            .border(
                width = if (isSelected) 0.dp else 1.dp,
                color = Color(0xFF2A2A2A),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) Charcoal else Color.White
        )
    }
}

@Composable
private fun DifficultyScreen(
    selectedDifficulty: String,
    onDifficultySelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 120.dp, bottom = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Choose your\ndifficulty",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            lineHeight = 36.sp,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        val difficulties = listOf(
            Triple("🌱", "Beginner", "Start small, build gradually"),
            Triple("⚡", "Intermediate", "Step up your game"),
            Triple("🔥", "Advanced", "Push your limits")
        )
        
        difficulties.forEach { (emoji, title, subtitle) ->
            DifficultyCard(
                emoji = emoji,
                title = title,
                subtitle = subtitle,
                isSelected = selectedDifficulty == title,
                onClick = { onDifficultySelected(title) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun DifficultyCard(
    emoji: String,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 300f)
    )
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(20.dp))
            .background(
                when {
                    isSelected && title == "Beginner" -> Color(0xFF4CAF50)
                    isSelected && title == "Intermediate" -> LemonYellow
                    isSelected && title == "Advanced" -> Color(0xFFFF5722)
                    else -> Color(0xFF1A1A1A)
                }
            )
            .border(
                width = if (isSelected) 0.dp else 1.dp,
                color = Color(0xFF2A2A2A),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = emoji,
                fontSize = 40.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Charcoal else Color.White
            )
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = if (isSelected) Charcoal.copy(alpha = 0.7f) else Color(0xFF8C8C8C)
            )
        }
    }
}

@Composable
private fun PreviewScreen(
    data: OnboardingData,
    onStartChallenge: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 100.dp, bottom = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "You're all set! 🚀",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Here's what your challenge looks like",
            fontSize = 16.sp,
            color = Color(0xFF8C8C8C),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(40.dp))
        
        // Challenge Preview Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF1A1A1A))
                .border(1.dp, Color(0xFF2A2A2A), RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Day counter
                Text(
                    text = "Day 1 / 30",
                    fontSize = 14.sp,
                    color = LemonYellow,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Streak
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🔥",
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "0 Day Streak",
                        fontSize = 18.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Progress bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF2A2A2A))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.03f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(LemonYellow)
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "3% Complete",
                    fontSize = 14.sp,
                    color = Color(0xFF8C8C8C)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                HorizontalDivider(color = Color(0xFF2A2A2A))
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Challenge details
                DetailRow("🎯", "Goal", data.goal)
                Spacer(modifier = Modifier.height(12.dp))
                DetailRow("📊", "Type", data.challengeType)
                Spacer(modifier = Modifier.height(12.dp))
                DetailRow("⚡", "Level", data.difficulty)
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Motivation quote
        Text(
            text = "\"Consistency beats intensity\"",
            fontSize = 16.sp,
            color = Color(0xFF8C8C8C),
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = onStartChallenge,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LemonYellow,
                contentColor = Charcoal
            )
        ) {
            Text(
                text = "Start Day 1",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DetailRow(icon: String, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "$label: ",
            fontSize = 14.sp,
            color = Color(0xFF8C8C8C)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
    }
}
