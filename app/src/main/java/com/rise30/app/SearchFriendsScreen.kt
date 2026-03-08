package com.rise30.app

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class UserSearchResponse(
    val success: Boolean,
    val users: List<SearchUser>
)

@Serializable
data class SearchUser(
    val id: String,
    val displayName: String?,
    val email: String,
    val avatarUrl: String? = null
)

@Composable
fun SearchFriendsScreen(
    currentUserId: String,
    onBack: () -> Unit,
    onUserClick: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<SearchUser>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(top = 40.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                "Find Friends",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { 
                searchQuery = it
                if (it.length >= 2) {
                    scope.launch {
                        isLoading = true
                        try {
                            val response: UserSearchResponse = ApiConfig.httpClient.get("${ApiConfig.BASE_URL}/api/users/search?q=$it").body()
                            if (response.success) {
                                searchResults = response.users.filter { u -> u.id != currentUserId }
                            }
                        } catch (e: Exception) {
                            // handle error
                        } finally {
                            isLoading = false
                        }
                    }
                } else {
                    searchResults = emptyList()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Search by name or email...", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Accent) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Accent,
                unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                cursorColor = Accent
            ),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Accent)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(searchResults) { user ->
                    UserResultCard(user = user, onClick = { onUserClick(user.id) })
                }
                
                if (searchQuery.length >= 2 && searchResults.isEmpty()) {
                    item {
                        Text(
                            "No users found",
                            color = Color.Gray,
                            modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserResultCard(user: SearchUser, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CardDark)
            .clickable(onClick = onClick)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Accent.copy(alpha = 0.1f)),
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
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = user.displayName ?: "User",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = user.email,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Icon(
                painter = painterResource(id = R.drawable.back), // Use an arrow icon
                contentDescription = null,
                tint = Color.Gray.copy(alpha = 0.3f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
