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
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

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
    Notifications
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
                var wasLoggedIn by remember { mutableStateOf(state.currentUser != null) }
                var realDisplayName by remember { mutableStateOf<String?>(null) }
                
                LaunchedEffect(state.currentUser) {
                    val session = state.currentUser
                    val userId = session?.user?.id
                    val email = session?.user?.email
                    
                    if (userId != null && email != null) {
                        // Sync user with backend
                        try {
                            ApiConfig.httpClient.post("${ApiConfig.BASE_URL}/api/auth/sync") {
                                contentType(ContentType.Application.Json)
                                setBody(mapOf("userId" to userId, "email" to email))
                            }
                        } catch(e: Exception) { /* ignore */ }

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
                    } else if (session == null) {
                        wasLoggedIn = false
                        realDisplayName = null
                    }
                }

                if (state.isInitializing) {
                    // Keep screen blank/background colored while native splash screen is showing
                    Box(modifier = Modifier.fillMaxSize().background(Charcoal))
                } else if (state.currentUser != null) {
                    val fallbackName = state.currentUser?.user?.userMetadata?.get("full_name")?.toString()?.replace("\"", "") 
                        ?: state.currentUser?.user?.email?.substringBefore("@") 
                        ?: "User"
                    val displayName = realDisplayName ?: fallbackName
                    val userId = state.currentUser?.user?.id ?: ""
                    
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
                                        userName = displayName.substringBefore("@"),
                                        onBack = { currentChallengeScreen = ChallengeScreen.None }
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
                                                onNotificationClick = {
                                                    currentChallengeScreen = ChallengeScreen.Notifications
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

                                            MainTab.Profile -> ProfilePage(
                                                userId = userId,
                                                userName = displayName,
                                                onSignOut = { viewModel.signOut() },
                                                currentTab = currentTab,
                                                onTabSelected = { selected -> currentTab = selected }
                                            )
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
                    // Auth screens with slide animation
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
                                    viewModel.loginWithEmailPassword(email, password)
                                },
                                onSendOtp = { email ->
                                    viewModel.sendOtp(email)
                                },
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
                                    viewModel.signUpWithEmailPassword(email, password)
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
    val currentUser: UserSession? = null,
    val error: String? = null,
    val info: String? = null
)

class AuthViewModel : ViewModel() {

    private val auth: Auth = SupabaseClient.client.auth

    var state by mutableStateOf(AuthUiState())
        private set

    init {
        viewModelScope.launch {
            // Load session from storage on init
            state = state.copy(currentUser = auth.currentSessionOrNull(), isInitializing = false)

            // Listen for auth state changes to keep session alive
            auth.sessionStatus.collect { status ->
                when (status) {
                    is io.github.jan.supabase.auth.status.SessionStatus.Authenticated -> {
                        state = state.copy(currentUser = status.session, isInitializing = false)
                    }
                    is io.github.jan.supabase.auth.status.SessionStatus.NotAuthenticated -> {
                        state = state.copy(currentUser = null, isInitializing = false)
                    }
                    is io.github.jan.supabase.auth.status.SessionStatus.RefreshFailure -> {
                        // Token refresh failed, user needs to re-login
                        state = state.copy(currentUser = null, error = "Session expired. Please sign in again.", isInitializing = false)
                    }
                    io.github.jan.supabase.auth.status.SessionStatus.Initializing -> {
                        // Auth is initializing, keep current state
                        state = state.copy(isInitializing = true)
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

    fun loginWithEmailPassword(email: String, password: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null, info = null)
            try {
                auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }
                val session = auth.currentSessionOrNull()
                if (session != null) syncPublicUserRow(session)
                state = state.copy(isLoading = false, currentUser = session)
            } catch (e: Exception) {
                state = state.copy(isLoading = false, error = e.message ?: "Login failed")
            }
        }
    }

    fun signUpWithEmailPassword(email: String, password: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null, info = null)
            try {
                auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                }
                val session = auth.currentSessionOrNull()
                if (session != null) syncPublicUserRow(session)
                state = state.copy(
                    isLoading = false,
                    currentUser = session,
                    info = "Check your email to confirm your account."
                )
            } catch (e: Exception) {
                state = state.copy(isLoading = false, error = e.message ?: "Sign up failed")
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
                if (session != null) syncPublicUserRow(session)
                state = state.copy(
                    isLoading = false,
                    currentUser = session,
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

    fun signOut() {
        viewModelScope.launch {
            try {
                auth.signOut()
            } finally {
                state = AuthUiState(info = "Signed out.")
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

        try {
            SupabaseClient.client.from("User").upsert(
                buildJsonObject {
                    put("id", userId)
                    put("email", email)
                }
            ) {
                onConflict = "id"
            }
            state = state.copy(info = "Profile saved to DB.")
        } catch (e: Exception) {
            state = state.copy(info = "Signed in (profile not saved): ${e.message}")
        }
    }
}

val LemonYellow = Color(0xFFFFF44F)
val Charcoal = Color(0xFF2E2E2E)
val CharcoalLight = Color(0xFF3B3B3B)

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

