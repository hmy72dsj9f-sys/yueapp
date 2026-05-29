package com.example.myapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapp.model.AIFaceState
import com.example.myapp.model.ChatMessage

/**
 * 消息气泡组件
 * 对应 Swift 中的 MessageBubble
 */
@Composable
fun MessageBubble(
    message: ChatMessage,
    isTyping: Boolean = false,
    faceState: AIFaceState = AIFaceState.HAPPY,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        if (!message.isUser) {
            SmallAIFace(
                faceState = faceState,
                modifier = Modifier.size(44.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        Column(
            horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start,
            modifier = Modifier.weight(if (message.isUser) 1f else 1f, fill = false)
        ) {
            // 发送者名称
            Text(
                text = if (message.isUser) "你" else "AI Assistant",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 气泡内容
            Column(
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        if (message.isUser) {
                            Brush.linearGradient(
                                colors = listOf(
                                    androidx.compose.ui.graphics.Color(0xFF2196F3).copy(alpha = 0.85f),
                                    androidx.compose.ui.graphics.Color(0xFF00BCD4).copy(alpha = 0.8f)
                                )
                            )
                        } else {
                            Brush.linearGradient(
                                colors = listOf(
                                    androidx.compose.ui.graphics.Color.White.copy(alpha = 0.85f),
                                    androidx.compose.ui.graphics.Color.White.copy(alpha = 0.75f)
                                )
                            )
                        }
                    )
                    .then(
                        if (!message.isUser) {
                            Modifier.background(
                                color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.1f)
                            )
                        } else Modifier
                    )
                    .padding(horizontal = 18.dp, vertical = 14.dp)
                    .shadow(
                        elevation = if (message.isUser) 10.dp else 4.dp,
                        shape = RoundedCornerShape(28.dp)
                    )
            ) {
                Text(
                    text = message.text,
                    fontSize = 17.sp,
                    color = if (message.isUser) {
                        androidx.compose.ui.graphics.Color.White
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )

                if (isTyping) {
                    Spacer(modifier = Modifier.height(4.dp))
                    TypingDots()
                }
            }
        }

        if (!message.isUser) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

/**
 * 用户消息气泡（底部浮动样式）
 * 对应 Swift ContentView 中的用户消息展示
 */
@Composable
fun UserMessageBubble(
    message: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = message,
            fontSize = 18.sp,
            color = androidx.compose.ui.graphics.Color.White,
            modifier = Modifier
                .clip(RoundedCornerShape(percent = 50))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            androidx.compose.ui.graphics.Color(0xFF2196F3),
                            androidx.compose.ui.graphics.Color(0xFF00BCD4)
                        )
                    )
                )
                .padding(horizontal = 20.dp, vertical = 14.dp)
        )
    }
}
