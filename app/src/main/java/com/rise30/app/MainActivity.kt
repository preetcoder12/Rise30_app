package com.rise30.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.rise30.app.streak.StreakViewModel
import com.rise30.app.LemonYellow
import com.rise30.app.util.NotificationHelper
import com.rise30.app.util.NotificationScheduler
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import android.content.Context
import android.content.SharedPreferences

@Serializable
data class UserAccount(
    val id: String,
    val email: String,
    val displayName: String? = null
)

@Serializable
data class AuthResponse(
    val success: Boolean,
    val user: UserAccount? = null,
    val token: String? = null,
    val error: String? = null
)

object SessionManager {
    private const val PREF_NAME = "rise30_session"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_EMAIL = "email"
    private const val KEY_DISPLAY_NAME = "display_name"
    private const val KEY_TOKEN = "auth_token"

    fun saveSession(context: Context, user: UserAccount, token: String?) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString(KEY_USER_ID, user.id)
            putString(KEY_EMAIL, user.email)
            putString(KEY_DISPLAY_NAME, user.displayName)
            putString(KEY_TOKEN, token)
            apply()
        }
    }

    fun getSession(context: Context): UserAccount? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val id = prefs.getString(KEY_USER_ID, null) ?: return null
        val email = prefs.getString(KEY_EMAIL, null) ?: return null
        val displayName = prefs.getString(KEY_DISPLAY_NAME, null)
        return UserAccount(id, email, displayName)
    }

    fun getToken(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_TOKEN, null)
    }

    fun clearSession(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
}


enum class AuthPage {
    SignIn,
    SignUp,
    ForgotPassword
}

enum class MainTab {
    Home,
    Challenges,
    Profile
}

enum class ChallengeScreen {
    None,
    WaterChallenge,
    ChallengeDetail,
    CreateChallenge,
    EditChallenge,
    Notifications,
    UserSearch,
    OtherProfile,
    FriendsList
}

class MainActivity : ComponentActivity() {

    private val viewModel: AuthViewModel by viewModels()
    private val streakViewModel: StreakViewModel by viewModels()
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
    
    // Permission launcher for notification permissions
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted, notifications can be shown
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install Android 12+ splash screen using Theme.Rise30.Splash
        val splashScreen = installSplashScreen()
        
        splashScreen.setKeepOnScreenCondition {
            viewModel.state.isInitializing
        }

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        viewModel.initSession(this)


        // Supabase Auth deep links removed as we are Firebase-only

        googleSignInClient = GoogleSignIn.getClient(
            this,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_server_client_id))
                .requestEmail()
                .build()
        )

        googleSignInLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                val data = result.data ?: run {
                    viewModel.onAuthError("Google sign-in cancelled")
                    return@registerForActivityResult
                }
                try {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    val account = task.getResult(ApiException::class.java)
                    val idToken = account.idToken
                    if (idToken.isNullOrBlank()) {
                        viewModel.onAuthError("No Google ID token returned from account")
                    } else {
                        viewModel.loginWithGoogleIdToken(this@MainActivity, idToken)
                    }
                } catch (e: ApiException) {
                    val message = when (e.statusCode) {
                        10 -> "Developer Error (Code 10): Ensure SHA-1 is in Firebase Console and Web Client ID is correct."
                        12500 -> "Sign-in failed (Code 12500): Check Google Play Services and fingerprints."
                        else -> "Google error (${e.statusCode}): ${e.message}"
                    }
                    viewModel.onAuthError(message)
                } catch (e: Exception) {
                    viewModel.onAuthError("Unexpected error: ${e.message}")
                }
            }

        setContent {
            Rise30Theme {
                val state = viewModel.state
                var page by remember { mutableStateOf(AuthPage.SignIn) }
                var currentTab by remember { mutableStateOf(MainTab.Home) }
                var currentChallengeScreen by remember { mutableStateOf(ChallengeScreen.None) }
                var selectedChallengeId by remember { mutableStateOf<String?>(null) }
                // Only reset to Home tab when the user FIRST logs in
                var wasLoggedIn by remember { mutableStateOf(state.isLoggedIn) }
                var realDisplayName by remember { mutableStateOf<String?>(null) }
                var selectedOtherUserId by remember { mutableStateOf<String?>(null) }
                
                
                LaunchedEffect(state.isLoggedIn, state.userId, state.info) {
                    val userId = state.userId
                    val email = state.email
                    
                    if (userId != null && email != null) {
                        // Fetch real profile from DB
                        try {
                            val response: UserProfileResponse = ApiConfig.httpClient.get("${ApiConfig.BASE_URL}/api/users/$userId/profile").body()
                            if (response.success && response.profile.displayName != null) {
                                realDisplayName = response.profile.displayName
                            }
                        } catch(e: Exception) { /* ignore */ }

                        if (!wasLoggedIn) {
                            currentTab = MainTab.Home
                            currentChallengeScreen = ChallengeScreen.None
                            wasLoggedIn = true
                            
                            // Request notification permission for new login
                            requestNotificationPermission()
                            
                            // Show welcome notification for brand new accounts (email signup)
                            if (state.info == "Account created!") {
                                val userName = realDisplayName ?: email.substringBefore("@")
                                NotificationHelper.showWelcomeNotification(this@MainActivity, userName)
                                NotificationScheduler.scheduleMorningRitual(this@MainActivity, dayNumber = 1)
                            }
                        }
                    } else if (!state.isLoggedIn) {
                        wasLoggedIn = false
                        realDisplayName = null
                    }
                }


                if (state.isInitializing) {
                    // Show animated splash screen with logo and Rise30 text
                    AnimatedSplashScreen()
                } else if (state.isLoggedIn) {
                    val fallbackName = state.email?.substringBefore("@") ?: "User"
                    val displayName = realDisplayName ?: fallbackName
                    val userId = state.userId ?: ""

                    
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Handle system back gesture
                        BackHandler(enabled = currentChallengeScreen != ChallengeScreen.None) {
                            currentChallengeScreen = ChallengeScreen.None
                        }

                        // Animated content with slide transitions
                        AnimatedContent(
                            targetState = currentChallengeScreen,
                            modifier = Modifier.fillMaxSize(),
                            transitionSpec = {
                                val direction = if (targetState == ChallengeScreen.None) {
                                    AnimatedContentTransitionScope.SlideDirection.Right
                                } else {
                                    AnimatedContentTransitionScope.SlideDirection.Left
                                }
                                slideIntoContainer(direction) + fadeIn() togetherWith
                                slideOutOfContainer(direction) + fadeOut()
                            },
                            label = "challenge_screen"
                        ) { screen ->
                            when (screen) {
                                ChallengeScreen.Notifications -> {
                                    NotificationsPage(
                                        currentUserId = userId,
                                        onBack = { currentChallengeScreen = ChallengeScreen.None },
                                        onUserClick = { requesterId ->
                                            selectedOtherUserId = requesterId
                                            currentChallengeScreen = ChallengeScreen.OtherProfile
                                        }
                                    )
                                }
                                ChallengeScreen.FriendsList -> {
                                    FriendsListScreen(
                                        currentUserId = userId,
                                        onBack = { currentChallengeScreen = ChallengeScreen.None },
                                        onUserClick = { friendId ->
                                            selectedOtherUserId = friendId
                                            currentChallengeScreen = ChallengeScreen.OtherProfile
                                        }
                                    )
                                }
                                ChallengeScreen.WaterChallenge -> {
                                    WaterChallengeScreen(
                                        userId = userId,
                                        onBack = { currentChallengeScreen = ChallengeScreen.None },
                                        currentTab = currentTab,
                                        onTabSelected = { selected -> 
                                            currentTab = selected
                                            currentChallengeScreen = ChallengeScreen.None
                                        }
                                    )
                                }
                                ChallengeScreen.ChallengeDetail -> {
                                    ChallengeDetailScreen(
                                        userId = userId,
                                        challengeId = selectedChallengeId ?: "",
                                        onBack = { currentChallengeScreen = ChallengeScreen.None },
                                        currentTab = currentTab,
                                        onTabSelected = { selected -> 
                                            currentTab = selected
                                            currentChallengeScreen = ChallengeScreen.None
                                        }
                                    )
                                }
                                ChallengeScreen.CreateChallenge -> {
                                    CreateChallengeScreen(
                                        userId = userId,
                                        onBack = { currentChallengeScreen = ChallengeScreen.None },
                                        onChallengeCreated = { 
                                            currentChallengeScreen = ChallengeScreen.None
                                        },
                                        currentTab = currentTab,
                                        onTabSelected = { selected -> 
                                            currentTab = selected
                                            currentChallengeScreen = ChallengeScreen.None
                                        }
                                    )
                                }
                                 ChallengeScreen.EditChallenge -> {
                                    EditChallengeScreen(
                                        userId = userId,
                                        challengeId = selectedChallengeId ?: "",
                                        onBack = { currentChallengeScreen = ChallengeScreen.None },
                                        onChallengeUpdated = { 
                                            currentChallengeScreen = ChallengeScreen.None
                                        },
                                        currentTab = currentTab,
                                        onTabSelected = { selected -> 
                                            currentTab = selected
                                            currentChallengeScreen = ChallengeScreen.None
                                        }
                                    )
                                }
                                ChallengeScreen.UserSearch -> {
                                    SearchFriendsScreen(
                                        currentUserId = userId,
                                        onBack = { currentChallengeScreen = ChallengeScreen.None },
                                        onUserClick = { otherId ->
                                            selectedOtherUserId = otherId
                                            currentChallengeScreen = ChallengeScreen.OtherProfile
                                        }
                                    )
                                }
                                ChallengeScreen.OtherProfile -> {
                                    OtherUserProfileScreen(
                                        currentUserId = userId,
                                        otherUserId = selectedOtherUserId ?: "",
                                        onBack = { currentChallengeScreen = ChallengeScreen.UserSearch }
                                    )
                                }
                                ChallengeScreen.None -> {

                                    // Animated tab content with horizontal sliding
                                    AnimatedContent(
                                        targetState = currentTab,
                                        modifier = Modifier.fillMaxSize(),
                                        transitionSpec = {
                                            fadeIn(animationSpec = tween(400, easing = LinearEasing)) togetherWith 
                                            fadeOut(animationSpec = tween(400, easing = LinearEasing))
                                        },
                                        label = "main_tab"
                                    ) { tab ->
                                        when (tab) {
                                            MainTab.Home -> HomePage(
                                                userId = userId,
                                                userName = displayName,
                                                onMarkComplete = { },
                                                onCreateChallenge = { currentChallengeScreen = ChallengeScreen.CreateChallenge },
                                                onNotificationClick = {
                                                    currentChallengeScreen = ChallengeScreen.Notifications
                                                },
                                                onSearchFriends = {
                                                    currentChallengeScreen = ChallengeScreen.UserSearch
                                                },
                                                currentTab = currentTab,
                                                onTabSelected = { selected -> currentTab = selected }
                                            )
                                            MainTab.Challenges -> ChallengesPage(
                                                userName = displayName,
                                                userId = userId,
                                                onStartChallenge = { },
                                                onStartWaterChallenge = { 
                                                    currentChallengeScreen = ChallengeScreen.WaterChallenge 
                                                },
                                                onChallengeClick = { challengeId ->
                                                    selectedChallengeId = challengeId
                                                    currentChallengeScreen = ChallengeScreen.ChallengeDetail
                                                },
                                                onCreateChallenge = {
                                                    currentChallengeScreen = ChallengeScreen.CreateChallenge
                                                },
                                                onEditChallenge = { challengeId ->
                                                    selectedChallengeId = challengeId
                                                    currentChallengeScreen = ChallengeScreen.EditChallenge
                                                },
                                                currentTab = currentTab,
                                                onTabSelected = { selected -> currentTab = selected }
                                            )

                                            MainTab.Profile -> {
                                                val context = LocalContext.current
                                                ProfilePage(
                                                    userId = userId,
                                                    userName = displayName,
                                                    onSignOut = { viewModel.signOut(context) },
                                                    onNavigateToFriends = { currentChallengeScreen = ChallengeScreen.FriendsList },
                                                    currentTab = currentTab,
                                                    onTabSelected = { selected -> currentTab = selected }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Persistent Bottom Bar
                        HomeFloatingBottomBar(
                            currentTab = currentTab,
                            onTabSelected = { selected ->
                                currentTab = selected
                                currentChallengeScreen = ChallengeScreen.None
                            }
                        )
                    }
                } else {
                    val context = LocalContext.current
                    AnimatedContent(
                        targetState = page,
                        transitionSpec = {
                            val direction = when (targetState) {
                                AuthPage.SignIn -> AnimatedContentTransitionScope.SlideDirection.Right
                                AuthPage.SignUp -> AnimatedContentTransitionScope.SlideDirection.Left
                                AuthPage.ForgotPassword -> AnimatedContentTransitionScope.SlideDirection.Up
                            }
                            slideIntoContainer(direction) + fadeIn() togetherWith
                            slideOutOfContainer(direction) + fadeOut()
                        },
                        label = "auth_page"
                    ) { authPage ->
                        when (authPage) {
                            AuthPage.SignIn -> SignInScreen(
                                state = state,
                                onEmailPasswordLogin = { email, password ->
                                    viewModel.loginWithEmailPassword(context, email, password)
                                },
                                onSendOtp = { _ -> },
                                onGoogleLogin = {
                                    viewModel.onAuthLoading()
                                    googleSignInLauncher.launch(googleSignInClient.signInIntent)
                                },
                                onGoToSignUp = { page = AuthPage.SignUp },
                                onGoToForgotPassword = { page = AuthPage.ForgotPassword }
                            )
                            AuthPage.SignUp -> SignUpScreen(
                                state = state,
                                onEmailPasswordSignup = { email, password ->
                                    viewModel.signUpWithEmailPassword(context, email, password)
                                },
                                onGoToSignIn = { page = AuthPage.SignIn }
                            )
                            AuthPage.ForgotPassword -> ForgotPasswordScreen(
                                state = state,
                                onSendReset = { email ->
                                    viewModel.sendPasswordResetEmail(email)
                                },
                                onBackToSignIn = { page = AuthPage.SignIn }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
    }

    override fun onResume() {
        super.onResume()
    }
    
    /**
     * Request notification permission (Android 13+)
     */
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission already granted
                }
                else -> {
                    // Request permission
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }
}

data class AuthUiState(
    val isInitializing: Boolean = true,
    val isLoading: Boolean = false,
    val currentAccount: UserAccount? = null,
    val firebaseUser: FirebaseUser? = null,
    val error: String? = null,
    val info: String? = null
) {
    val isLoggedIn: Boolean get() = currentAccount != null || firebaseUser != null
    val userId: String? get() = currentAccount?.id ?: firebaseUser?.uid
    val email: String? get() = currentAccount?.email ?: firebaseUser?.email
}

class AuthViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    var state by mutableStateOf(AuthUiState())
        private set

    fun initSession(context: Context) {
        viewModelScope.launch {
            // 1. Check Custom Backend Session (for local profile info)
            val savedAccount = SessionManager.getSession(context)
            if (savedAccount != null) {
                state = state.copy(currentAccount = savedAccount)
            }

            // 2. Check Firebase Auth (The main authority)
            val currentFirebaseUser = firebaseAuth.currentUser
            if (currentFirebaseUser != null) {
                state = state.copy(firebaseUser = currentFirebaseUser, isInitializing = false)
            } else {
                state = state.copy(isInitializing = false)
            }
        }
    }

    fun onAuthLoading() {
        state = state.copy(isLoading = true, error = null, info = null)
    }

    fun onAuthError(message: String) {
        state = state.copy(isLoading = false, error = message, info = null)
    }

    fun loginWithEmailPassword(context: Context, email: String, password: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null, info = null)
            try {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                val user = result.user
                if (user != null) {
                    syncFirebaseUserWithBackend(context, user)
                    state = state.copy(isLoading = false, firebaseUser = user)
                } else {
                    state = state.copy(isLoading = false, error = "Login failed: No user returned")
                }
            } catch (e: Exception) {
                state = state.copy(isLoading = false, error = e.message ?: "Login error")
            }
        }
    }

    fun signUpWithEmailPassword(context: Context, email: String, password: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null, info = null)
            try {
                val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val user = result.user
                if (user != null) {
                    syncFirebaseUserWithBackend(context, user)
                    state = state.copy(isLoading = false, firebaseUser = user, info = "Account created!")
                    
                    // Send warm welcome notification!
                    val userName = email.substringBefore("@")
                    NotificationHelper.showWelcomeNotification(context, userName)
                    NotificationScheduler.scheduleMorningRitual(context, dayNumber = 1)
                } else {
                    state = state.copy(isLoading = false, error = "Signup failed")
                }
            } catch (e: Exception) {
                state = state.copy(isLoading = false, error = e.message ?: "Signup error")
            }
        }
    }


    fun sendOtp(email: String) {
        // Kept for UI signature, but OTP not yet implemented via Firebase here
        state = state.copy(error = "OTP not implemented via Firebase in this version")
    }

    fun loginWithGoogleFirebase(context: Context, idToken: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null, info = null)
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val result = firebaseAuth.signInWithCredential(credential).await()
                val user = result.user
                
                if (user != null) {
                    syncFirebaseUserWithBackend(context, user)
                    state = state.copy(
                        isLoading = false,
                        firebaseUser = user,
                        info = "Signed in with Google (Firebase)."
                    )
                } else {
                    state = state.copy(isLoading = false, error = "Firebase sign-in failed: No user")
                }
            } catch (e: Exception) {
                state = state.copy(
                    isLoading = false,
                    error = e.message ?: "Google Firebase login failed"
                )
            }
        }
    }

    private suspend fun syncFirebaseUserWithBackend(context: Context, user: FirebaseUser) {
        val userId = user.uid
        val email = user.email ?: return
        val displayName = user.displayName ?: email.substringBefore("@")

        try {
            val response: AuthResponse = ApiConfig.httpClient.post("${ApiConfig.BASE_URL}/api/auth/sync") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("userId" to userId, "email" to email, "displayName" to displayName))
            }.body()

            if (response.success && response.user != null) {
                SessionManager.saveSession(context, response.user, response.token)
                state = state.copy(currentAccount = response.user, info = "Profile synced.")
            } else {
                state = state.copy(error = response.error ?: "Failed to sync profile.")
            }
        } catch (e: Exception) {
            state = state.copy(info = "Signed in (API sync failed): ${e.message}")
        }
    }

    fun loginWithGoogleIdToken(context: Context, idToken: String) {
        loginWithGoogleFirebase(context, idToken)
    }

    fun signOut(context: Context) {
        viewModelScope.launch {
            try {
                SessionManager.clearSession(context)
                firebaseAuth.signOut()
            } finally {
                state = AuthUiState(isInitializing = false, info = "Signed out.")
            }
        }
    }


    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null, info = null)
            try {
                firebaseAuth.sendPasswordResetEmail(email).await()
                state = state.copy(
                    isLoading = false,
                    info = "Password reset email sent. Check your inbox."
                )
            } catch (e: Exception) {
                state = state.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to send reset email"
                )
            }
        }
    }

    // Supabase sync row removed as we are Firebase-only now
}




@Composable
fun AnimatedSplashScreen() {
    // Animation states
    val logoAlpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }
    val logoScale = remember { Animatable(0.85f) }
    val textOffset = remember { Animatable(20f) }
    
    LaunchedEffect(Unit) {
        // Animate logo fade in with scale
        launch {
            logoAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(1000, easing = EaseOutQuart)
            )
        }
        launch {
            logoScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(1000, easing = EaseOutQuart)
            )
        }
        // Delay then animate text fade in with slide up
        delay(500)
        launch {
            textAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(900, easing = EaseOutQuart)
            )
        }
        launch {
            textOffset.animateTo(
                targetValue = 0f,
                animationSpec = tween(900, easing = EaseOutQuart)
            )
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Logo
            androidx.compose.foundation.Image(
                painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                contentDescription = "Rise30 Logo",
                modifier = Modifier
                    .size(120.dp)
                    .graphicsLayer {
                        alpha = logoAlpha.value
                        scaleX = logoScale.value
                        scaleY = logoScale.value
                    }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Rise30 Text with Poppins font style - smooth fade in
            Text(
                text = "Rise30",
                fontSize = 42.sp,
                fontWeight = FontWeight.SemiBold,
                color = LemonYellow,
                modifier = Modifier.graphicsLayer {
                    alpha = textAlpha.value
                    translationY = textOffset.value
                },
                letterSpacing = 2.sp
            )
        }
    }
}

// Easing for smooth premium animation
private val EaseOutQuart = CubicBezierEasing(0.25f, 1f, 0.5f, 1f)

