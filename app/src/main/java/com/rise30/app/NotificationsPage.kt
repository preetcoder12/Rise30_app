package com.rise30.app

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
data class PendingRequestsResponse(
    val success: Boolean,
    val requests: List<PendingRequestUser> = emptyList()
)

@Serializable
data class PendingRequestUser(
    val id: String,
    val email: String,
    val displayName: String? = null,
    val avatarUrl: String? = null
)

@Composable
fun NotificationsPage(
    currentUserId: String,
    onBack: () -> Unit,
    onUserClick: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    var pendingRequests by remember { mutableStateOf<List<PendingRequestUser>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(currentUserId) {
        isLoading = true
        try {
            val response: PendingRequestsResponse = ApiConfig.httpClient.get("${ApiConfig.BASE_URL}/api/friends/pending/${currentUserId}").body()
            if (response.success) {
                pendingRequests = response.requests
            }
        } catch (e: Exception) {
            // handle error
        } finally {
            isLoading = false
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {
            // Header with Back Button and Title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                
                Text(
                    text = "Notifications",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Text(
                text = "Stay updated on your connections",
                color = Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            if (isLoading) {
                Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Accent)
                }
            } else if (pendingRequests.isNotEmpty()) {
                Text("Friend Requests", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(16.dp))
                
                pendingRequests.forEach { user ->
                    var isAccepting by remember { mutableStateOf(false) }
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(CardDark)
                            .clickable { onUserClick(user.id) }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Accent.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            val avatar = user.avatarUrl
                            if (!avatar.isNullOrBlank()) {
                                Text(
                                    text = avatar,
                                    fontSize = 20.sp
                                )
                            } else {
                                Text(
                                    text = (user.displayName ?: user.email).take(1).uppercase(),
                                    color = Accent,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = user.displayName ?: "User",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Wants to connect",
                                color = Color.Gray,
                                fontSize = 13.sp
                            )
                        }
                        
                        Button(
                            onClick = {
                                isAccepting = true
                                scope.launch {
                                    try {
                                        ApiConfig.httpClient.post("${ApiConfig.BASE_URL}/api/friends/accept") {
                                            contentType(ContentType.Application.Json)
                                            setBody(mapOf("userId" to currentUserId, "friendId" to user.id))
                                        }
                                        pendingRequests = pendingRequests.filter { it.id != user.id }
                                    } catch (e: Exception) {}
                                    isAccepting = false
                                }
                            },
                            enabled = !isAccepting,
                            colors = ButtonDefaults.buttonColors(containerColor = Accent, contentColor = Color.Black),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Accept", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                // Empty state with animation
                EmptyNotificationsAnimation()
            }
            
            Spacer(modifier = Modifier.height(96.dp))
        }
    }
}

@Composable
private fun EmptyNotificationsAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Animated bell icon
        Box(
            modifier = Modifier
                .size(120.dp)
                .scale(scale)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Accent.copy(alpha = alpha),
                            Color.Transparent
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "No notifications",
                modifier = Modifier.size(48.dp),
                tint = Accent.copy(alpha = 0.8f)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "No notifications yet",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "We'll notify you when you reach milestones\nor when it's time for your daily challenge!",
            color = Color.Gray,
            fontSize = 14.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
