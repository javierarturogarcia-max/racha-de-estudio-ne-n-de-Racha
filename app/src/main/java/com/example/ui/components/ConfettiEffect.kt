package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonLime
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.NeonOrangePrimary
import kotlin.random.Random

private data class Particle(
    val initialX: Float,
    val initialY: Float,
    val targetX: Float,
    val targetY: Float,
    val color: Color,
    val size: Float,
    val rotation: Float,
    val rotationSpeed: Float
)

@Composable
fun NeonBurstEffect(
    trigger: Long,
    modifier: Modifier = Modifier
) {
    if (trigger == 0L) return

    val progress = remember(trigger) { Animatable(0f) }

    val particles = remember(trigger) {
        val colors = listOf(NeonOrangePrimary, NeonCyan, NeonLime, NeonMagenta, Color.White)
        List(40) {
            val angle = Random.nextFloat() * 2 * Math.PI.toFloat()
            val speed = Random.nextFloat() * 450f + 250f
            val startX = 0.5f
            val startY = 0.45f
            val dx = kotlin.math.cos(angle.toDouble()).toFloat() * speed
            val dy = kotlin.math.sin(angle.toDouble()).toFloat() * speed + 200f
            Particle(
                initialX = startX,
                initialY = startY,
                targetX = dx,
                targetY = dy,
                color = colors.random(),
                size = Random.nextFloat() * 12f + 8f,
                rotation = Random.nextFloat() * 360f,
                rotationSpeed = (Random.nextFloat() - 0.5f) * 720f
            )
        }
    }

    LaunchedEffect(trigger) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1400, easing = LinearEasing)
        )
    }

    if (progress.value < 1f) {
        Canvas(modifier = modifier.fillMaxSize()) {
            val canvasW = size.width
            val canvasH = size.height
            val currentProgress = progress.value
            val alpha = (1f - currentProgress).coerceIn(0f, 1f)

            particles.forEach { p ->
                val originX = canvasW * p.initialX
                val originY = canvasH * p.initialY
                val currentX = originX + (p.targetX * currentProgress)
                val currentY = originY + (p.targetY * currentProgress) + (currentProgress * currentProgress * 250f)
                val currentRot = p.rotation + (p.rotationSpeed * currentProgress)

                rotate(degrees = currentRot, pivot = Offset(currentX, currentY)) {
                    drawRect(
                        color = p.color.copy(alpha = alpha),
                        topLeft = Offset(currentX - p.size / 2, currentY - p.size / 2),
                        size = Size(p.size, p.size * 0.6f)
                    )
                }
            }
        }
    }
}
