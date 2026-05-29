package com.example.myapp.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

/**
 * 打字指示器 - 三个跳动的点
 * 对应 Swift 中的 TypingDot
 */
@Composable
fun TypingDots(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TypingDot(delay = 0)
        TypingDot(delay = 200)
        TypingDot(delay = 400)
    }
}

@Composable
private fun TypingDot(delay: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "dot")

    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -4f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = EaseInOut, delayMillis = delay),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dotOffset"
    )

    Surface(
        modifier = Modifier
            .size(8.dp)
            .offset(y = offsetY.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
    ) {}
}
