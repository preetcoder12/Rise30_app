package com.rise30.app.streak

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Colors matching the app theme
private val BackgroundDark = Color(0xFF0D0D0F)
private val CardDark = Color(0xFF1A1A1F)
private val Accent = Color(0xFFFFD54F)
private val AccentSoft = Color(0x33FFD54F)
private val ErrorRed = Color(0xFFFF5252)
private val SuccessGreen = Color(0xFF4CAF50)

/**
 * Data class representing streak forgiveness state
 */
data class StreakForgivenessState(
    val streakLength: Int = 0,
    val missedDays: Int = 0,
    val freezesUsed: Int = 0,
    val freezesRemaining: Int = 2,
    val canRecover: Boolean = false,
    val canUseFreeze: Boolean = false,
    val isStreakBroken: Boolean = false,
    val recoveryDeadline: Long? = null, // timestamp in millis
    val message: String = ""
)

/**
 * Streak Recovery Card - Shown when user misses a day
 */
@Composable
fun StreakRecoveryCard(
    state: StreakForgivenessState,
    onRecover: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = state.canRecover || state.canUseFreeze,
        enter = slideInVertically { it } + fadeIn(),
        exit = slideOutVertically { it } + fadeOut()
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            colors = CardDefaults.cardColors(
                containerColor = CardDark
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Header with icon
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (state.canRecover) 
                            Icons.Default.Warning 
                        else 
                            Icons.Default.AcUnit,
                        contentDescription = null,
                        tint = if (state.canRecover) ErrorRed else Accent,
                        modifier = Modifier.size(28.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Text(
                        text = if (state.canRecover) 
                            "⚠️ Streak at Risk!" 
                        else 
                            "🛡 Streak Protected",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Message
                Text(
                    text = state.message,
                    color = Color.Gray,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Streak info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StreakInfoItem(
                        icon = Icons.Default.LocalFireDepartment,
                        value = "${state.streakLength}",
                        label = "Day Streak",
                        color = Accent
                    )
                    
                    StreakInfoItem(
                        icon = Icons.Default.AcUnit,
                        value = "${state.freezesRemaining}",
                        label = "Freezes Left",
                        color = if (state.freezesRemaining > 0) SuccessGreen else Color.Gray
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Action buttons
                if (state.canRecover) {
                    Button(
                        onClick = onRecover,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Accent,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = "🔥 Recover Streak",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "I'll start over",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                } else if (state.canUseFreeze) {
                    // Freeze was automatically used
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(AccentSoft)
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✅ Streak freeze used! Complete today's task to keep your streak.",
                            color = Accent,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

/**
 * Small streak info item for the recovery card
 */
@Composable
private fun StreakInfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = value,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

/**
 * Streak Broken Card - Shown when streak is reset
 */
@Composable
fun StreakBrokenCard(
    previousStreak: Int,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardDark
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "💔 Streak Reset",
                color = ErrorRed,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Your $previousStreak-day streak was reset after multiple missed days.",
                color = Color.Gray,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "But your challenge continues! Every day is a fresh start.",
                color = Color.White,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onContinue,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Accent,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Keep Going 💪",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

/**
 * Streak Shield Indicator - Small badge showing freeze status
 */
@Composable
fun StreakShieldBadge(
    freezesRemaining: Int,
    modifier: Modifier = Modifier
) {
    val hasFreezes = freezesRemaining > 0
    
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(if (hasFreezes) AccentSoft else Color(0x33FF5252))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.AcUnit,
            contentDescription = null,
            tint = if (hasFreezes) Accent else ErrorRed,
            modifier = Modifier.size(16.dp)
        )
        
        Spacer(modifier = Modifier.width(6.dp))
        
        Text(
            text = if (hasFreezes) "$freezesRemaining shield${if (freezesRemaining > 1) "s" else ""}" else "No shields",
            color = if (hasFreezes) Accent else ErrorRed,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Daily completion button with streak info
 */
@Composable
fun DailyCompletionButton(
    dayNumber: Int,
    isCompleted: Boolean,
    streakLength: Int,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Streak indicator
        if (streakLength > 0) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = null,
                    tint = Accent,
                    modifier = Modifier.size(20.dp)
                )
                
                Spacer(modifier = Modifier.width(4.dp))
                
                Text(
                    text = "$streakLength day streak",
                    color = Accent,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        // Complete button
        Button(
            onClick = onComplete,
            enabled = !isCompleted,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isCompleted) SuccessGreen else Accent,
                contentColor = Color.Black,
                disabledContainerColor = SuccessGreen,
                disabledContentColor = Color.White
            )
        ) {
            Text(
                text = if (isCompleted) "✅ Day $dayNumber Complete!" else "Mark Day $dayNumber Complete",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

/**
 * Progress bar showing streak health
 */
@Composable
fun StreakHealthBar(
    currentStreak: Int,
    maxStreak: Int,
    daysUntilFreezeNeeded: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Streak Health",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            
            if (daysUntilFreezeNeeded <= 1) {
                Text(
                    text = "⚠️ Complete today!",
                    color = ErrorRed,
                    fontSize = 12.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LinearProgressIndicator(
            progress = { (currentStreak.toFloat() / maxStreak.coerceAtLeast(1)).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50)),
            color = when {
                daysUntilFreezeNeeded <= 1 -> ErrorRed
                daysUntilFreezeNeeded <= 2 -> Accent
                else -> SuccessGreen
            },
            trackColor = AccentSoft,
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = "$currentStreak / $maxStreak days",
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}
