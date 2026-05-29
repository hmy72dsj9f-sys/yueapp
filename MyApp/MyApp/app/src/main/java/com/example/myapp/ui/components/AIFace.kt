package com.example.myapp.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.myapp.model.AIFaceState

/**
 * AI 动态表情组件 - 使用 Canvas 绘制
 * 对应 Swift 中的 AppleAIFace
 */
@Composable
fun AppleAIFace(
    faceState: AIFaceState,
    modifier: Modifier = Modifier
) {
    // 动画状态
    val blinkTransition = rememberInfiniteTransition(label = "blink")
    val blink by blinkTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = keyframes {
            durationMillis = 2800
            0f at 0
            1f at 2700
            0f at 2800
        },
        label = "blink"
    )

    val breathingTransition = rememberInfiniteTransition(label = "breathing")
    val breathingScale by breathingTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathing"
    )

    val floatingTransition = rememberInfiniteTransition(label = "floating")
    val floatingOffset by floatingTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floating"
    )

    Box(
        modifier = modifier
            .offset(y = floatingOffset.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(170.dp)
        ) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val mainRadius = 72.dp.toPx()

            // 外圈柔光
            val glowRadius = mainRadius * breathingScale
            drawCircle(
                color = Color.White.copy(alpha = 0.18f),
                radius = glowRadius,
                center = Offset(centerX, centerY),
                alpha = 0.3f
            )

            // 主体玻璃圆 - 半透明白色背景
            drawCircle(
                color = Color.White.copy(alpha = 0.7f),
                radius = mainRadius,
                center = Offset(centerX, centerY)
            )

            // 边框
            drawCircle(
                color = Color.White.copy(alpha = 0.12f),
                radius = mainRadius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 1.dp.toPx())
            )

            // 绘制眼睛
            val eyeY = centerY - 14.dp.toPx()
            val eyeSpacing = 22.dp.toPx()

            val isBlinking = blink > 0.5f

            // 左眼
            drawEye(
                centerX = centerX - eyeSpacing,
                centerY = eyeY,
                faceState = faceState,
                isBlinking = isBlinking
            )

            // 右眼
            drawEye(
                centerX = centerX + eyeSpacing,
                centerY = eyeY,
                faceState = faceState,
                isBlinking = isBlinking
            )

            // 绘制嘴巴
            val mouthY = centerY + 14.dp.toPx()
            drawMouth(
                centerX = centerX,
                centerY = mouthY,
                faceState = faceState
            )
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawEye(
    centerX: Float,
    centerY: Float,
    faceState: AIFaceState,
    isBlinking: Boolean
) {
    val color = Color(0xFF333333)

    when (faceState) {
        AIFaceState.HAPPY -> {
            // 胶囊形眼睛
            val height = if (isBlinking) 2.dp.toPx() else 8.dp.toPx()
            val width = 12.dp.toPx()
            drawRoundRect(
                color = color,
                topLeft = Offset(centerX - width / 2, centerY - height / 2),
                size = Size(width, height),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(height / 2)
            )
        }
        AIFaceState.THINKING -> {
            // 圆形眼睛
            val size = if (isBlinking) 3.dp.toPx() else 8.dp.toPx()
            drawCircle(
                color = color,
                radius = size / 2,
                center = Offset(centerX, centerY)
            )
        }
        AIFaceState.LISTENING -> {
            // 大圆眼睛
            val size = if (isBlinking) 3.dp.toPx() else 10.dp.toPx()
            drawCircle(
                color = color,
                radius = size / 2,
                center = Offset(centerX, centerY)
            )
        }
        AIFaceState.CONFUSED -> {
            // 圆角矩形眼睛
            val height = if (isBlinking) 2.dp.toPx() else 7.dp.toPx()
            val width = 12.dp.toPx()
            drawRoundRect(
                color = color,
                topLeft = Offset(centerX - width / 2, centerY - height / 2),
                size = Size(width, height),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx())
            )
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawMouth(
    centerX: Float,
    centerY: Float,
    faceState: AIFaceState
) {
    val color = Color(0xFF333333)

    when (faceState) {
        AIFaceState.HAPPY -> {
            // 微笑弧线
            val path = Path().apply {
                addArc(
                    androidx.compose.ui.geometry.Rect(
                        left = centerX - 12.dp.toPx(),
                        top = centerY - 6.dp.toPx(),
                        right = centerX + 12.dp.toPx(),
                        bottom = centerY + 6.dp.toPx()
                    ),
                    startAngleDegrees = 20f,
                    sweepAngleDegrees = 140f
                )
            }
            drawPath(
                path = path,
                color = color,
                style = Stroke(width = 3.dp.toPx())
            )
        }
        AIFaceState.THINKING -> {
            // 横线嘴
            drawRoundRect(
                color = color,
                topLeft = Offset(centerX - 9.dp.toPx(), centerY - 1.5.dp.toPx()),
                size = Size(18.dp.toPx(), 3.dp.toPx()),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(1.5.dp.toPx())
            )
        }
        AIFaceState.LISTENING -> {
            // O 形嘴
            drawCircle(
                color = color,
                radius = 3.dp.toPx(),
                center = Offset(centerX, centerY)
            )
        }
        AIFaceState.CONFUSED -> {
            // 直线嘴
            drawRect(
                color = color,
                topLeft = Offset(centerX - 9.dp.toPx(), centerY - 1.dp.toPx()),
                size = Size(18.dp.toPx(), 2.dp.toPx())
            )
        }
    }
}

/**
 * 小型 AI 表情（用于消息气泡旁边）
 */
@Composable
fun SmallAIFace(
    faceState: AIFaceState,
    modifier: Modifier = Modifier
) {
    AppleAIFace(
        faceState = faceState,
        modifier = modifier.size(44.dp)
    )
}
