package com.rise30.app

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.animation.animateContentSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
data class CreateChallengeRequest(
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
fun CreateChallengeScreen(
    userId: String,
    onBack: () -> Unit,
    onChallengeCreated: () -> Unit,
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
    var selectedIcon by remember { mutableStateOf("star") }
    var targetValue by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
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
    val icons = listOf(
        "star" to R.drawable.star,
        "run" to R.drawable.run,
        "gym" to R.drawable.gym,
        "books" to R.drawable.books,
        "yoga" to R.drawable.yoga,
        "leaf" to R.drawable.leaf,
        "music" to R.drawable.music,
        "tech" to R.drawable.tech,
        "drop" to R.drawable.drop
    )
    
    val durationInt = duration.toIntOrNull()
    val isFormValid = name.isNotBlank() && durationInt != null && durationInt >= 7 && durationInt <= 30
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundDark
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(scrollState)
                .padding(20.dp)
                .animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.offset(x = (-16).dp) // Adjusting to make image edge at 16dp (Row has 20dp padding, -16dp offset, 20-16+12=16dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "Back",
                        modifier = Modifier.size(20.dp) // Reduced size
                    )
                }
                
                Spacer(modifier = Modifier.width(6.dp))
                
                Text(
                    text = "Create Challenge",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // Preview Card
            ChallengePreviewCard(
                name = name.ifBlank { "Challenge Name" },
                description = description,
                icon = selectedIcon,
                color = try { Color(android.graphics.Color.parseColor(selectedColor)) } catch(e:Exception) { Accent },
                duration = duration.toIntOrNull() ?: 30
            )
            
            // Form Fields Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardDark),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
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
                            unfocusedTextColor = Color.White,
                            cursorColor = Accent
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )
                    
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
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
                            unfocusedTextColor = Color.White,
                            cursorColor = Accent
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )
                    
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
                            unfocusedTextColor = Color.White,
                            cursorColor = Accent
                        ),
                        shape = RoundedCornerShape(14.dp),
                        supportingText = { 
                            Text(
                                text = if (durationInt != null && (durationInt < 7 || durationInt > 30)) "Must be 7-30 days" else "7-30 days",
                                color = if (durationInt != null && (durationInt < 7 || durationInt > 30)) Color.Red else Color.Gray
                            )
                        }
                    )
                }
            }
            
            // Category Selection
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardDark),
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        "Category",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        categories.forEach { category ->
                            CategoryChip(
                                label = category,
                                selected = selectedCategory == category,
                                onClick = { selectedCategory = category }
                            )
                        }
                    }
                }
            }
            
            // Color Selection
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardDark),
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        "Theme Color",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        colors.forEach { (colorHex, colorName) ->
                            ColorOption(
                                color = Color(android.graphics.Color.parseColor(colorHex)),
                                selected = selectedColor == colorHex,
                                onClick = { selectedColor = colorHex }
                            )
                        }
                    }
                }
            }
            
            // Icon Selection
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardDark),
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        "Icon",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        icons.forEach { (name, resId) ->
                            IconOption(
                                resId = resId,
                                selected = selectedIcon == name,
                                onClick = { selectedIcon = name }
                            )
                        }
                    }
                }
            }
            
            // Target Value
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardDark),
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        "Target (Optional)",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
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
                            shape = RoundedCornerShape(14.dp)
                        )
                        
                        OutlinedTextField(
                            value = unit,
                            onValueChange = { unit = it },
                            label = { Text("Unit") },
                            placeholder = { Text("e.g. liters") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Accent,
                                unfocusedBorderColor = Color.Gray,
                                focusedLabelColor = Accent,
                                unfocusedLabelColor = Color.Gray,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            shape = RoundedCornerShape(14.dp)
                        )
                    }
                }
            }
            
            // Error Message
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color(0xFFEF5350),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            // Create Button
            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        errorMessage = null
                        
                        createChallenge(
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
                                onChallengeCreated()
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
                    .padding(bottom = 120.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(18.dp),
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
                        text = "Create Challenge",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            

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
            // Premium Logo Pattern
            Box(
                modifier = Modifier.size(72.dp)
            ) {
                // Large Circle with Selected Icon
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.15f))
                        .border(1.dp, color.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    val resId = when(icon) {
                        "star" -> R.drawable.star
                        "run" -> R.drawable.run
                        "gym" -> R.drawable.gym
                        "books" -> R.drawable.books
                        "yoga" -> R.drawable.yoga
                        "leaf" -> R.drawable.leaf
                        "music" -> R.drawable.music
                        "tech" -> R.drawable.tech
                        "drop" -> R.drawable.drop
                        else -> R.drawable.yoga
                    }
                    
                    Image(
                        painter = painterResource(id = resId),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp) // Larger icon in main circle
                    )
                }
                
                // Extra Small Badge with Name Initial (Top Right)
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.TopEnd)
                        .clip(CircleShape)
                        .background(color)
                        .border(2.dp, CardDark, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (name.isNotBlank()) name.take(1).uppercase() else "C",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
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
    resId: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(64.dp) // Increased size for better visibility
            .clip(CircleShape)
            .background(if (selected) Accent else Color(0xFF2E2E2E))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            colorFilter = if (selected) ColorFilter.tint(Color.Black) else null
        )
    }
}

// API Function
private suspend fun createChallenge(
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
        val request = CreateChallengeRequest(
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
        
        val response: HttpResponse = ApiConfig.httpClient.post("${ApiConfig.BASE_URL}/api/challenges") {
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
