package com.rise30.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Charcoal = Color(0xFF151515)
val CharcoalLight = Color(0xFF1E1E1E)
val LemonYellow = Color(0xFFFFD54F)


// Central place to control the app font.
// When you add real Poppins font files under res/font, you can
// replace this with a FontFamily based on those resources.
val PoppinsFontFamily: FontFamily = FontFamily.SansSerif

private val RiseTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),
    titleLarge = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    labelLarge = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    )
)

private val RiseColorScheme = darkColorScheme(
    primary = LemonYellow,
    onPrimary = Charcoal,
    background = Charcoal,
    surface = Charcoal,
    onBackground = androidx.compose.ui.graphics.Color.White,
    onSurface = androidx.compose.ui.graphics.Color.White
)

@Composable
fun Rise30Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = RiseColorScheme,
        typography = RiseTypography,
        content = content
    )
}

