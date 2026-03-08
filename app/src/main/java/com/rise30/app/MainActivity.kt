package com.rise30.app

import android.content.Intent
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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.rise30.app.streak.StreakViewModel
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.handleDeeplinks
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.github.jan.supabase.auth.providers.builtin.OTP
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
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

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install Android 12+ splash screen using Theme.Rise30.Splash
        val splashScreen = installSplashScreen()
        
        splashScreen.setKeepOnScreenCondition {
            viewModel.state.isInitializing
        }

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        viewModel.initSession(this)


        // Handle Supabase OAuth/OTP deep links
        intent?.let { SupabaseClient.client.handleDeeplinks(it) }

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
                    val account = GoogleSignIn.getSignedInAccountFromIntent(data)
                        .getResult(ApiException::class.java)
                    val idToken = account.idToken
                    if (idToken.isNullOrBlank()) {
                        viewModel.onAuthError("No Google ID token returned")
                    } else {
                        viewModel.loginWithGoogleIdToken(idToken)
                    }
                } catch (e: Exception) {
                    viewModel.onAuthError(e.message ?: "Google sign-in failed")
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
                
                
                LaunchedEffect(state.isLoggedIn, state.userId) {
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
                        }
                    } else if (!state.isLoggedIn) {
                        wasLoggedIn = false
                        realDisplayName = null
                    }
                }


                if (state.isInitializing) {
                    // Keep screen blank/background colored while native splash screen is showing
                    Box(modifier = Modifier.fillMaxSize().background(Charcoal))
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
        SupabaseClient.client.handleDeeplinks(intent)
    }

    override fun onResume() {
        super.onResume()
        // Refresh session when app comes back to foreground
        lifecycleScope.launch {
            SupabaseClient.refreshSessionIfNeeded()
        }
    }
}

data class AuthUiState(
    val isInitializing: Boolean = true,
    val isLoading: Boolean = false,
    val currentAccount: UserAccount? = null,
    val supabaseSession: UserSession? = null,
    val error: String? = null,
    val info: String? = null
) {
    val isLoggedIn: Boolean get() = currentAccount != null || supabaseSession != null
    val userId: String? get() = currentAccount?.id ?: supabaseSession?.user?.id
    val email: String? get() = currentAccount?.email ?: supabaseSession?.user?.email
}

class AuthViewModel : ViewModel() {

    private val auth: Auth = SupabaseClient.client.auth

    var state by mutableStateOf(AuthUiState())
        private set

    fun initSession(context: Context) {
        viewModelScope.launch {
            // 1. Check Custom Backend Session (Email/Pass)
            val savedAccount = SessionManager.getSession(context)
            if (savedAccount != null) {
                state = state.copy(currentAccount = savedAccount, isInitializing = false)
            }

            // 2. Listen for Supabase Status (Google/OAuth)
            auth.sessionStatus.collect { status ->
                when (status) {
                    is io.github.jan.supabase.auth.status.SessionStatus.Authenticated -> {
                        state = state.copy(supabaseSession = status.session, isInitializing = false)
                    }
                    is io.github.jan.supabase.auth.status.SessionStatus.NotAuthenticated -> {
                        state = state.copy(supabaseSession = null)
                        if (state.currentAccount == null) {
                            state = state.copy(isInitializing = false)
                        }
                    }
                    else -> {
                        // E.g., Initializing. Keep isInitializing = true if no savedAccount exists yet.
                    }
                }
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
                val response: AuthResponse = ApiConfig.httpClient.post("${ApiConfig.BASE_URL}/api/auth/login") {
                    contentType(ContentType.Application.Json)
                    setBody(mapOf("email" to email, "password" to password))
                }.body()

                if (response.success && response.user != null) {
                    SessionManager.saveSession(context, response.user, response.token)
                    state = state.copy(isLoading = false, currentAccount = response.user)
                } else {
                    state = state.copy(isLoading = false, error = response.error ?: "Login failed")
                }
            } catch (e: Exception) {
                state = state.copy(isLoading = false, error = e.message ?: "Network error")
            }
        }
    }

    fun signUpWithEmailPassword(context: Context, email: String, password: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null, info = null)
            try {
                val response: AuthResponse = ApiConfig.httpClient.post("${ApiConfig.BASE_URL}/api/auth/register") {
                    contentType(ContentType.Application.Json)
                    setBody(mapOf("email" to email, "password" to password, "displayName" to email.substringBefore("@")))
                }.body()

                if (response.success && response.user != null) {
                    SessionManager.saveSession(context, response.user, response.token)
                    state = state.copy(isLoading = false, currentAccount = response.user, info = "Account created!")
                } else {
                    state = state.copy(isLoading = false, error = response.error ?: "Signup failed")
                }
            } catch (e: Exception) {
                state = state.copy(isLoading = false, error = e.message ?: "Network error")
            }
        }
    }


    fun sendOtp(email: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null, info = null)
            try {
                auth.signInWith(OTP) { this.email = email }
                state = state.copy(
                    isLoading = false,
                    info = "OTP sent. Check your email."
                )
            } catch (e: Exception) {
                state = state.copy(isLoading = false, error = e.message ?: "OTP failed")
            }
        }
    }

    fun loginWithGoogleIdToken(idToken: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null, info = null)
            try {
                withTimeout(20_000) {
                    auth.signInWith(IDToken) {
                        this.idToken = idToken
                        provider = Google
                    }
                }
                val session = auth.currentSessionOrNull()
                if (session != null) {
                    syncPublicUserRow(session)
                }
                state = state.copy(
                    isLoading = false,
                    supabaseSession = session,
                    info = if (session != null) "Signed in with Google." else "Signed in (no session found)."
                )
            } catch (e: Exception) {
                state = state.copy(
                    isLoading = false,
                    error = e.message ?: "Google login failed"
                )
            }
        }
    }

    fun signOut(context: Context) {
        viewModelScope.launch {
            try {
                SessionManager.clearSession(context)
                auth.signOut()
            } finally {
                state = AuthUiState(isInitializing = false, info = "Signed out.")
            }
        }
    }


    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null, info = null)
            try {
                auth.resetPasswordForEmail(
                    email = email,
                    redirectUrl = "rise30://auth"
                )
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

    private suspend fun syncPublicUserRow(session: UserSession) {
        val user = session.user ?: return
        val userId = user.id
        val email = user.email ?: return
        val displayName = email.substringBefore("@")

        try {
            val response: AuthResponse = ApiConfig.httpClient.post("${ApiConfig.BASE_URL}/api/auth/sync") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("userId" to userId, "email" to email, "displayName" to displayName))
            }.body()

            if (response.success) {
                state = state.copy(info = "Google profile synced.")
            } else {
                state = state.copy(error = response.error ?: "Failed to sync Google profile.")
            }
        } catch (e: Exception) {
            state = state.copy(info = "Signed in locally (API sync failed): ${e.message}")
        }
    }
}


@Composable
fun SignedInScreen(
    session: UserSession,
    info: String?,
    onSignOut: () -> Unit
) {
    val userId = session.user?.id ?: "(no id)"
    val email = session.user?.email ?: "(no email)"

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Charcoal),
        color = Charcoal
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Signed in",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = LemonYellow,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Email: $email",
                color = Color.White,
                fontSize = 14.sp
            )
            Text(
                text = "User ID: $userId",
                color = Color.White,
                fontSize = 12.sp
            )

            if (!info.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = info,
                    color = LemonYellow,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onSignOut,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CharcoalLight,
                    contentColor = LemonYellow
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Sign out")
            }
        }
    }
}

