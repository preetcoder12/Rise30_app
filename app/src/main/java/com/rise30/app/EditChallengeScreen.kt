package com.rise30.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

@Serializable
data class EditChallengeRequest(
    val userId: String,
    val name: String,
    val description: String,
    val type: String,
    val category: String,
    val duration: Int,
    val color: String,
    val icon: String,
    val targetValue: Float? = null,
    val unit: String? = null
)

@Composable
fun EditChallengeScreen(
    userId: String,
    challengeId: String,
    onBack: () -> Unit,
    onChallengeUpdated: () -> Unit,
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()


    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("30") }
    var selectedCategory by remember { mutableStateOf("Health") }
    var selectedColor by remember { mutableStateOf("#4FC3F7") }
    var selectedIcon by remember { mutableStateOf("🎯") }
    var targetValue by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    LaunchedEffect(challengeId) {
        isLoading = true
        try {
            val response: ChallengeDetailApiResponse = httpClient.get("$BASE_URL/api/challenges/$challengeId").body()
            if (response.success) {
                val challenge = response.challenge
                name = challenge.name
                description = challenge.description ?: ""
                duration = challenge.duration.toString()
                selectedCategory = challenge.category
                selectedColor = challenge.color ?: "#4FC3F7"
                selectedIcon = challenge.icon ?: "🎯"
            } else {
                errorMessage = "Failed to load challenge details."
            }
        } catch (e: Exception) {
            errorMessage = "Error: ${e.message}"
        }
        isLoading = false
    }
    
    val categories = listOf("Health", "Fitness", "Productivity", "Skills", "Mindfulness", "Other")
    val colors = listOf(
        "#4FC3F7" to "Blue",
        "#66BB6A" to "Green",
        "#FFA726" to "Orange",
        "#EF5350" to "Red",
        "#AB47BC" to "Purple",
        "#FFD54F" to "Yellow",
        "#EC407A" to "Pink",
        "#26C6DA" to "Cyan"
    )
    val icons = listOf("🎯", "💧", "🏃", "📚", "🧘", "💪", "🎨", "💻", "🎵", "🌱", "⭐", "🔥")
    
    val isFormValid = name.isNotBlank() && duration.isNotBlank() && duration.toIntOrNull() != null
    
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
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, bottom = 15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Accent
                        )
                    }
                    
                    Text(
                        text = "Update Challenge",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(5.dp))
                
                // Preview Card
                ChallengePreviewCard(
                    name = name.ifBlank { "Challenge Name" },
                    description = description,
                    icon = selectedIcon,
                    color = Color(android.graphics.Color.parseColor(selectedColor)),
                    duration = duration.toIntOrNull() ?: 30
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Form Fields
                Text(
                    text = "Challenge Details",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Name Input
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Challenge Name") },
                    placeholder = { Text("e.g., 30-Day Water Challenge") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Accent,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Accent,
                        unfocusedLabelColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Description Input
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (Optional)") },
                    placeholder = { Text("Describe your challenge...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Accent,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Accent,
                        unfocusedLabelColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Duration Input
                OutlinedTextField(
                    value = duration,
                    onValueChange = { 
                        if (it.isEmpty() || it.toIntOrNull() != null) {
                            duration = it
                        }
                    },
                    label = { Text("Duration (Days)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Accent,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Accent,
                        unfocusedLabelColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Category Selection
                Text(
                    text = "Category",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { category ->
                        CategoryChip(
                            label = category,
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Color Selection
                Text(
                    text = "Theme Color",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    colors.forEach { (colorHex, colorName) ->
                        ColorOption(
                            color = Color(android.graphics.Color.parseColor(colorHex)),
                            selected = selectedColor == colorHex,
                            onClick = { selectedColor = colorHex }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Icon Selection
                Text(
                    text = "Icon",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    icons.forEach { icon ->
                        IconOption(
                            icon = icon,
                            selected = selectedIcon == icon,
                            onClick = { selectedIcon = icon }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Optional: Target Value
                Text(
                    text = "Target (Optional)",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = targetValue,
                        onValueChange = { 
                            if (it.isEmpty() || it.toFloatOrNull() != null) {
                                targetValue = it
                            }
                        },
                        label = { Text("Amount") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Accent,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Accent,
                            unfocusedLabelColor = Color.Gray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    
                    OutlinedTextField(
                        value = unit,
                        onValueChange = { unit = it },
                        label = { Text("Unit") },
                        placeholder = { Text("e.g., liters, min") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Accent,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Accent,
                            unfocusedLabelColor = Color.Gray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Error Message
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = Color(0xFFEF5350),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
                
                // Create Button
                Button(
                    onClick = {
                        scope.launch {
                            isLoading = true
                            errorMessage = null
                            
                            updateChallenge(
                                challengeId = challengeId,
                                userId = userId,
                                name = name,
                                description = description,
                                category = selectedCategory,
                                duration = duration.toInt(),
                                color = selectedColor,
                                icon = selectedIcon,
                                targetValue = targetValue.toFloatOrNull(),
                                unit = unit.ifBlank { null },
                                onSuccess = {
                                    isLoading = false
                                    onChallengeUpdated()
                                },
                                onError = { error ->
                                    isLoading = false
                                    errorMessage = error
                                }
                            )
                        }
                    },
                    enabled = isFormValid && !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Accent,
                        contentColor = Color.Black,
                        disabledContainerColor = Color.Gray,
                        disabledContentColor = Color.DarkGray
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "Update Challenge",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(106.dp))
            }
            
            HomeFloatingBottomBar(
                currentTab = currentTab,
                onTabSelected = onTabSelected
            )
        }
    }
}

@Composable
private fun ChallengePreviewCard(
    name: String,
    description: String,
    icon: String,
    color: Color,
    duration: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icon,
                    fontSize = 36.sp
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                if (description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        color = Color.Gray,
                        fontSize = 13.sp,
                        maxLines = 2
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$duration days",
                    color = color,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun CategoryChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(if (selected) Accent else CardDark)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = if (selected) Color.Black else Color.Gray,
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun ColorOption(
    color: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(color)
            .border(
                width = if (selected) 3.dp else 0.dp,
                color = if (selected) Color.White else Color.Transparent,
                shape = CircleShape
            )
            .clickable(onClick = onClick)
    )
}

@Composable
private fun IconOption(
    icon: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) Accent else CardDark)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = icon,
            fontSize = 24.sp
        )
    }
}

// API Function
private suspend fun updateChallenge(
    challengeId: String,
    userId: String,
    name: String,
    description: String,
    category: String,
    duration: Int,
    color: String,
    icon: String,
    targetValue: Float?,
    unit: String?,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    try {
        val request = EditChallengeRequest(
            userId = userId,
            name = name,
            description = description,
            type = "custom",
            category = category,
            duration = duration,
            color = color,
            icon = icon,
            targetValue = targetValue,
            unit = unit
        )
        
        val response: HttpResponse = httpClient.put("$BASE_URL/api/challenges/$challengeId") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        
        if (response.status.isSuccess()) {
            onSuccess()
        } else {
            onError("Failed to create challenge: ${response.status}")
        }
    } catch (e: Exception) {
        onError(e.message ?: "Failed to create challenge")
    }
}
