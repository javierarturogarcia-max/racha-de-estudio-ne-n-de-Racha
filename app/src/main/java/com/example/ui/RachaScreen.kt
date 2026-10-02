package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MotivationalQuote
import com.example.ui.components.NeonBurstEffect
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonCyanGlow
import com.example.ui.theme.NeonDarkBg
import com.example.ui.theme.NeonDarkSurface
import com.example.ui.theme.NeonDarkSurfaceBorder
import com.example.ui.theme.NeonDarkSurfaceCard
import com.example.ui.theme.NeonLime
import com.example.ui.theme.NeonOrangeGlow
import com.example.ui.theme.NeonOrangeLight
import com.example.ui.theme.NeonOrangePrimary
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextDarkPlaceholder
import com.example.ui.theme.TextMutedSecondary
import com.example.ui.theme.TextWhitePrimary

@Composable
fun RachaScreen(
    uiState: RachaUiState,
    onStudyTodayClicked: () -> Unit,
    onUndoTodayClicked: () -> Unit,
    onNextQuoteClicked: () -> Unit,
    onToggleDay: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NeonDarkBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(
                    top = statusBarPadding + 12.dp,
                    bottom = navBarPadding + 24.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Container with max width for tablet / large screen adaptation
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 580.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Bar: Logo & Offline Status badge
                HeaderBar()

                Spacer(modifier = Modifier.height(16.dp))

                // Hero: Big Neon Streak Counter
                StreakCounterHero(
                    streakDays = uiState.currentStreak,
                    isStudiedToday = uiState.isStudiedToday
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Primary CTA: "Hoy sí estudié"
                StudyTodayActionButton(
                    isStudiedToday = uiState.isStudiedToday,
                    onStudyTodayClicked = onStudyTodayClicked,
                    onUndoTodayClicked = onUndoTodayClicked
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Section: Últimos 7 Días
                LastSevenDaysSection(
                    days = uiState.last7Days,
                    studiedCount = uiState.studiedDatesCountInLast7Days,
                    onDayClick = onToggleDay
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Section: Motivational Quote ("Mensaje motivador distinto cada vez")
                MotivationalQuoteCard(
                    quote = uiState.currentQuote,
                    onRefreshClicked = onNextQuoteClicked
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Bottom Stats Overview
                StatsFooter(
                    bestStreak = uiState.bestStreak,
                    totalDays = uiState.totalDaysStudied
                )
            }
        }

        // Celebratory neon particle burst on logging study
        NeonBurstEffect(
            trigger = uiState.celebrationTrigger,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun HeaderBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(NeonOrangeGlow, NeonDarkSurfaceCard)
                        )
                    )
                    .border(1.5.dp, NeonOrangePrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = "Icono Racha",
                    tint = NeonOrangePrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "RACHA",
                    color = TextWhitePrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "HÁBITO DE ESTUDIO",
                    color = NeonCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
            }
        }

        // Offline / Sin Anuncios Pill
        Surface(
            color = NeonDarkSurfaceCard,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, NeonDarkSurfaceBorder)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CloudOff,
                    contentDescription = "Sin conexión",
                    tint = NeonLime,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "100% OFFLINE",
                    color = NeonLime,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

@Composable
private fun StreakCounterHero(
    streakDays: Int,
    isStudiedToday: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowPulse"
    )

    val neonBorderBrush = if (streakDays > 0) {
        Brush.sweepGradient(
            listOf(
                NeonOrangePrimary,
                NeonOrangeLight,
                NeonCyan,
                NeonOrangeGlow,
                NeonOrangePrimary
            )
        )
    } else {
        Brush.linearGradient(
            listOf(NeonDarkSurfaceBorder, NeonDarkSurfaceCard)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        // Glowing Card Background
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = if (streakDays > 0) 18.dp else 4.dp,
                    shape = RoundedCornerShape(32.dp),
                    spotColor = if (streakDays > 0) NeonOrangePrimary else Color.Transparent
                )
                .border(2.dp, neonBorderBrush, RoundedCornerShape(32.dp)),
            colors = CardDefaults.cardColors(containerColor = NeonDarkSurface),
            shape = RoundedCornerShape(32.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp, horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Flame icon with pulse scale
                Box(
                    modifier = Modifier
                        .scale(if (streakDays > 0) glowPulse else 1f)
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = if (streakDays > 0) {
                                    listOf(NeonOrangePrimary.copy(alpha = 0.35f), Color.Transparent)
                                } else {
                                    listOf(NeonDarkSurfaceCard, Color.Transparent)
                                }
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Fuego de Racha",
                        tint = if (streakDays > 0) NeonOrangePrimary else TextDarkPlaceholder,
                        modifier = Modifier.size(52.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Big Animated Number
                AnimatedContent(
                    targetState = streakDays,
                    transitionSpec = {
                        fadeIn(tween(300)) togetherWith fadeOut(tween(300))
                    },
                    label = "streakCounter"
                ) { targetStreak ->
                    Text(
                        text = targetStreak.toString(),
                        color = if (targetStreak > 0) TextWhitePrimary else TextMutedSecondary,
                        fontSize = 86.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 90.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.testTag("streak_count_text")
                    )
                }

                Text(
                    text = if (streakDays == 1) "DÍA CONSECUTIVO" else "DÍAS CONSECUTIVOS",
                    color = if (streakDays > 0) NeonOrangeLight else TextMutedSecondary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Status Chip
                Surface(
                    color = if (isStudiedToday) NeonLime.copy(alpha = 0.15f) else NeonDarkSurfaceCard,
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isStudiedToday) NeonLime else NeonDarkSurfaceBorder
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isStudiedToday) Icons.Default.CheckCircle else Icons.Default.School,
                            contentDescription = null,
                            tint = if (isStudiedToday) NeonLime else NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isStudiedToday) {
                                "¡OBJETIVO DE HOY CUMPLIDO!"
                            } else if (streakDays > 0) {
                                "PENDIENTE HOY • ¡MANTÉN EL FUEGO!"
                            } else {
                                "COMIENZA HOY TU PRIMER DÍA"
                            },
                            color = if (isStudiedToday) NeonLime else NeonCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StudyTodayActionButton(
    isStudiedToday: Boolean,
    onStudyTodayClicked: () -> Unit,
    onUndoTodayClicked: () -> Unit
) {
    val buttonBgBrush = if (isStudiedToday) {
        Brush.horizontalGradient(
            listOf(Color(0xFF0F3D24), Color(0xFF14532D))
        )
    } else {
        Brush.horizontalGradient(
            listOf(NeonOrangePrimary, NeonOrangeGlow, Color(0xFFFF3D00))
        )
    }

    val buttonBorderColor by animateColorAsState(
        targetValue = if (isStudiedToday) NeonLime else NeonOrangeLight,
        label = "btnBorder"
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Large Primary Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .shadow(
                    elevation = if (isStudiedToday) 6.dp else 16.dp,
                    shape = RoundedCornerShape(20.dp),
                    spotColor = if (isStudiedToday) NeonLime else NeonOrangePrimary
                )
                .clip(RoundedCornerShape(20.dp))
                .background(buttonBgBrush)
                .border(2.dp, buttonBorderColor, RoundedCornerShape(20.dp))
                .clickable(onClick = onStudyTodayClicked)
                .testTag("study_today_button"),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Icon(
                    imageVector = if (isStudiedToday) Icons.Default.CheckCircle else Icons.Default.LocalFireDepartment,
                    contentDescription = null,
                    tint = if (isStudiedToday) NeonLime else TextWhitePrimary,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = if (isStudiedToday) "¡HOY YA ESTUDIASTE!" else "«HOY SÍ ESTUDIÉ»",
                        color = TextWhitePrimary,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = if (isStudiedToday) {
                            "Día guardado en el teléfono • Toca para frase nueva"
                        } else {
                            "+1 a tu racha consecutiva"
                        },
                        color = if (isStudiedToday) NeonLime else Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Undo affordance if already marked today
        if (isStudiedToday) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "¿Fue un error? Deshacer registro de hoy",
                color = TextMutedSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onUndoTodayClicked)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .testTag("undo_today_button")
            )
        }
    }
}

@Composable
private fun LastSevenDaysSection(
    days: List<DayStatusItem>,
    studiedCount: Int,
    onDayClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, NeonDarkSurfaceBorder, RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(containerColor = NeonDarkSurfaceCard),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header: Title & Count
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ÚLTIMOS 7 DÍAS",
                        color = TextWhitePrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "Registro histórico local",
                        color = TextMutedSecondary,
                        fontSize = 12.sp
                    )
                }

                Surface(
                    color = NeonDarkSurface,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "$studiedCount / 7 DÍAS",
                        color = NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Progress bar
            val progress = (studiedCount / 7f).coerceIn(0f, 1f)
            val animatedProgress by animateFloatAsState(
                targetValue = progress,
                animationSpec = tween(600, easing = FastOutSlowInEasing),
                label = "weekProgress"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(NeonDarkSurface)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(NeonCyan, NeonOrangePrimary, NeonLime)
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 7 Days Grid/Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                days.forEach { item ->
                    DayItemColumn(
                        item = item,
                        onClick = { onDayClick(item.dateIso) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DayItemColumn(
    item: DayStatusItem,
    onClick: () -> Unit
) {
    val borderColor = when {
        item.isToday && item.isStudied -> NeonLime
        item.isToday -> NeonCyan
        item.isStudied -> NeonOrangePrimary
        else -> NeonDarkSurfaceBorder
    }

    val containerBg = when {
        item.isToday && item.isStudied -> NeonLime.copy(alpha = 0.15f)
        item.isToday -> NeonCyan.copy(alpha = 0.1f)
        item.isStudied -> NeonOrangePrimary.copy(alpha = 0.15f)
        else -> NeonDarkSurface
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 2.dp, vertical = 4.dp)
    ) {
        // Day of week label
        Text(
            text = if (item.isToday) "HOY" else item.dayOfWeek,
            color = if (item.isToday) NeonCyan else TextMutedSecondary,
            fontSize = 11.sp,
            fontWeight = if (item.isToday) FontWeight.Black else FontWeight.Bold,
            letterSpacing = 0.5.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Circular or rounded status capsule
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(containerBg)
                .border(
                    width = if (item.isToday) 2.dp else 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (item.isStudied) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Estudiado",
                    tint = if (item.isToday) NeonLime else NeonOrangePrimary,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = item.dayNumber,
                    color = if (item.isToday) TextWhitePrimary else TextDarkPlaceholder,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Small indicator dot
        Box(
            modifier = Modifier
                .size(5.dp)
                .clip(CircleShape)
                .background(
                    if (item.isStudied) {
                        if (item.isToday) NeonLime else NeonOrangePrimary
                    } else {
                        Color.Transparent
                    }
                )
        )
    }
}

@Composable
private fun MotivationalQuoteCard(
    quote: MotivationalQuote,
    onRefreshClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                Brush.horizontalGradient(listOf(NeonCyan.copy(alpha = 0.6f), NeonPurple.copy(alpha = 0.4f))),
                RoundedCornerShape(24.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = NeonDarkSurfaceCard),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "MENSAJE MOTIVADOR",
                        color = NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }

                // Button to get a different quote
                Surface(
                    color = NeonDarkSurface,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, NeonDarkSurfaceBorder),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = onRefreshClicked)
                        .testTag("refresh_quote_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Nuevo mensaje",
                            tint = TextMutedSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "CAMBIAR",
                            color = TextMutedSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Large, expressive typography for the motivational quote
            AnimatedContent(
                targetState = quote,
                transitionSpec = {
                    fadeIn(tween(400)) togetherWith fadeOut(tween(200))
                },
                label = "quoteAnim"
            ) { targetQuote ->
                Column {
                    Text(
                        text = "«${targetQuote.quote}»",
                        color = TextWhitePrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 26.sp,
                        letterSpacing = 0.2.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "— ${targetQuote.author}",
                        color = NeonOrangeLight,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun StatsFooter(
    bestStreak: Int,
    totalDays: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Best Streak Card
        Card(
            modifier = Modifier
                .weight(1f)
                .border(1.dp, NeonDarkSurfaceBorder, RoundedCornerShape(18.dp)),
            colors = CardDefaults.cardColors(containerColor = NeonDarkSurfaceCard),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = NeonOrangePrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "$bestStreak ${if (bestStreak == 1) "día" else "días"}",
                    color = TextWhitePrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "MEJOR RACHA",
                    color = TextMutedSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }

        // Total Days Studied Card
        Card(
            modifier = Modifier
                .weight(1f)
                .border(1.dp, NeonDarkSurfaceBorder, RoundedCornerShape(18.dp)),
            colors = CardDefaults.cardColors(containerColor = NeonDarkSurfaceCard),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "$totalDays ${if (totalDays == 1) "día" else "días"}",
                    color = TextWhitePrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "TOTAL ESTUDIADO",
                    color = TextMutedSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}
