package com.rise30.app

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

// ------------------- Shapes -------------------
private val PanelShape = RoundedCornerShape(
    topStart = 40.dp,
    topEnd = 40.dp,
    bottomStart = 0.dp,
    bottomEnd = 0.dp
)
private val InputShape = RoundedCornerShape(14.dp)
private val TabShape = RoundedCornerShape(28.dp)

@Composable
fun AuthScreen(
    state: AuthUiState,
    isLoginSelected: Boolean,
    isForgotPassword: Boolean = false,
    onSelectLogin: () -> Unit,
    onSelectSignUp: () -> Unit,
    onEmailPasswordLogin: (String, String) -> Unit,
    onEmailPasswordSignup: (String, String) -> Unit,
    onGoogleLogin: () -> Unit,
    onGoToForgotPassword: () -> Unit,
    onSendReset: (String) -> Unit,
    onBack: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Box(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            
            // Header
            Box(
                modifier = Modifier.align(Alignment.TopStart).zIndex(2f)
            ) {
                Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)) {
                    // Logo and Title
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.bg_removed_applogo),
                            contentDescription = "Rise30 Logo",
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Rise30",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                    
                    Text(
                        text = "Go ahead and set\nup your account",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        lineHeight = 32.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Sign in/up to start your\n30-day transformation journey",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF8C8C8C),
                        lineHeight = 22.sp
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
            
            // Bottom panel
            Box(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).zIndex(2f)) {
                // Static underlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.68f)
                        .align(Alignment.BottomCenter)
                        .clip(PanelShape)
                        .background(Charcoal)
                        .zIndex(1f)
                )
                
                // Bottom panel content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.68f)
                        .clip(PanelShape)
                        .background(Charcoal)
                        .padding(horizontal = 25.dp, vertical = 16.dp)
                        .navigationBarsPadding()
                        .zIndex(2f)
                ) {
                    // Tab Switcher
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(TabShape)
                            .background(Color(0xFF1A1A1A))
                            .border(1.dp, Color(0xFF2A2A2A), TabShape)
                    ) {
                        // Animated background
                        val animatedOffset by animateFloatAsState(
                            targetValue = if (isLoginSelected) 0f else 1f,
                            animationSpec = spring(
                                dampingRatio = 0.8f,
                                stiffness = 300f
                            ),
                            label = "tab_background_animation"
                        )
                        
                        val density = LocalDensity.current
                        val screenWidth = LocalConfiguration.current.screenWidthDp.dp
                        val tabWidth = (screenWidth - 50.dp) / 2
                        
                        Box(
                            modifier = Modifier
                                .width(tabWidth)
                                .height(53.dp)
                                .offset(
                                    x = with(density) { (animatedOffset * tabWidth.toPx()).toDp() }
                                )
                                .clip(RoundedCornerShape(28.dp))
                                .background(LemonYellow)
                        )
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            SegmentedTab(
                                title = "Log In",
                                selected = isLoginSelected,
                                onClick = onSelectLogin,
                                modifier = Modifier.weight(1f),
                                enabled = !state.isLoading
                            )
                            SegmentedTab(
                                title = "Sign Up",
                                selected = !isLoginSelected,
                                onClick = onSelectSignUp,
                                modifier = Modifier.weight(1f),
                                enabled = !state.isLoading
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Content based on selected tab or forgot password
                    when {
                        isForgotPassword -> {
                            ForgotPasswordContent(
                                state = state,
                                onSendReset = onSendReset,
                                onBackToLogin = onSelectLogin
                            )
                        }
                        isLoginSelected -> {
                            LoginContent(
                                state = state,
                                onEmailPasswordLogin = onEmailPasswordLogin,
                                onGoogleLogin = onGoogleLogin,
                                onGoToForgotPassword = onGoToForgotPassword
                            )
                        }
                        else -> {
                            SignUpContent(
                                state = state,
                                onEmailPasswordSignup = onEmailPasswordSignup,
                                onGoogleLogin = onGoogleLogin
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SegmentedTab(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .height(53.dp)
            .clickable(
                enabled = enabled,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            color = if (selected) Color.Black else Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LoginContent(
    state: AuthUiState,
    onEmailPasswordLogin: (String, String) -> Unit,
    onGoogleLogin: () -> Unit,
    onGoToForgotPassword: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
    Column(modifier = Modifier.fillMaxWidth()) {
        // Email Input
        AuthTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = "Email",
            keyboardType = KeyboardType.Email,
            enabled = !state.isLoading
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Password Input
        AuthTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = "Password",
            keyboardType = KeyboardType.Password,
            isPassword = true,
            enabled = !state.isLoading
        )
        
        // Forgot Password
        TextButton(
            onClick = onGoToForgotPassword,
            modifier = Modifier.align(Alignment.End),
            enabled = !state.isLoading
        ) {
            Text(
                text = "Forgot Password?",
                color = LemonYellow,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Login Button
        AuthButton(
            text = "Log In",
            onClick = { onEmailPasswordLogin(email, password) },
            enabled = email.isNotBlank() && password.isNotBlank() && !state.isLoading,
            isLoading = state.isLoading
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Divider
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = Color(0xFF3A3A3A)
            )
            Text(
                text = "  or  ",
                color = Color(0xFF8C8C8C),
                fontSize = 14.sp
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = Color(0xFF3A3A3A)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Google Button
        GoogleButton(
            onClick = onGoogleLogin,
            enabled = !state.isLoading
        )
        
        // Error Message
        if (state.error != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = state.error,
                color = Color(0xFFFF6B6B),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SignUpContent(
    state: AuthUiState,
    onEmailPasswordSignup: (String, String) -> Unit,
    onGoogleLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    Column(modifier = Modifier.fillMaxWidth()) {
        // Email Input
        AuthTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = "Email",
            keyboardType = KeyboardType.Email,
            enabled = !state.isLoading
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Password Input
        AuthTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = "Password",
            keyboardType = KeyboardType.Password,
            isPassword = true,
            enabled = !state.isLoading
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Confirm Password Input
        AuthTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = "Confirm Password",
            keyboardType = KeyboardType.Password,
            isPassword = true,
            enabled = !state.isLoading
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Sign Up Button
        AuthButton(
            text = "Sign Up",
            onClick = { onEmailPasswordSignup(email, password) },
            enabled = email.isNotBlank() && password.isNotBlank() && 
                     password == confirmPassword && !state.isLoading,
            isLoading = state.isLoading
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Divider
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = Color(0xFF3A3A3A)
            )
            Text(
                text = "  or  ",
                color = Color(0xFF8C8C8C),
                fontSize = 14.sp
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = Color(0xFF3A3A3A)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Google Button
        GoogleButton(
            onClick = onGoogleLogin,
            enabled = !state.isLoading
        )
        
        // Error Message
        if (state.error != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = state.error,
                color = Color(0xFFFF6B6B),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType,
    isPassword: Boolean = false,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { 
            Text(
                text = placeholder,
                color = Color(0xFF8C8C8C)
            ) 
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = InputShape,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF1A1A1A),
            unfocusedContainerColor = Color(0xFF1A1A1A),
            focusedBorderColor = LemonYellow,
            unfocusedBorderColor = Color(0xFF2A2A2A),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = LemonYellow
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Next
        ),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        enabled = enabled
    )
}

@Composable
private fun AuthButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
    isLoading: Boolean
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LemonYellow,
            contentColor = Color.Black,
            disabledContainerColor = Color(0xFF3A3A3A),
            disabledContentColor = Color(0xFF8C8C8C)
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.Black,
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun GoogleButton(
    onClick: () -> Unit,
    enabled: Boolean
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDDDDDD))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Google logo
            Image(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Google",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Continue with Google",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}

@Composable
private fun ForgotPasswordContent(
    state: AuthUiState,
    onSendReset: (String) -> Unit,
    onBackToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    
    Column(modifier = Modifier.fillMaxWidth()) {
        // Title
        Text(
            text = "Reset Password",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = "Enter your email to receive a reset link",
            fontSize = 14.sp,
            color = Color(0xFF8C8C8C),
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        // Email Input
        AuthTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = "Email",
            keyboardType = KeyboardType.Email,
            enabled = !state.isLoading
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Send Reset Button
        AuthButton(
            text = "Send Reset Link",
            onClick = { onSendReset(email) },
            enabled = email.isNotBlank() && !state.isLoading,
            isLoading = state.isLoading
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Back to Login
        TextButton(
            onClick = onBackToLogin,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            Text(
                text = "Back to Log In",
                color = LemonYellow,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
        
        // Success/Error Messages
        if (!state.info.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = state.info,
                color = LemonYellow,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        if (!state.error.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = state.error,
                color = Color(0xFFFF6B6B),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
