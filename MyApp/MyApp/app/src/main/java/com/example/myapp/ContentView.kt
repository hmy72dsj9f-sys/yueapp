package com.example.myapp

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapp.model.AIFaceState
import com.example.myapp.service.AIService
import com.example.myapp.ui.components.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 主界面 - 对应 Swift 中的 ContentView
 */
@Composable
fun ContentView() {
    // 状态管理
    var aiText by remember { mutableStateOf("你好呀～") }
    var displayedAIText by remember { mutableStateOf("") }
    var userText by remember { mutableStateOf("") }
    var currentUserMessage by remember { mutableStateOf("") }
    var isTyping by remember { mutableStateOf(false) }
    var faceState by remember { mutableStateOf(AIFaceState.HAPPY) }

    val aiService = remember { AIService() }
    val scope = rememberCoroutineScope()

    // 打字机效果
    LaunchedEffect(aiText) {
        val chars = aiText.toList()
        displayedAIText = ""
        for (char in chars) {
            displayedAIText += char
            delay(35)
        }
    }

    // 初始打字效果
    LaunchedEffect(Unit) {
        val chars = aiText.toList()
        displayedAIText = ""
        for (char in chars) {
            displayedAIText += char
            delay(35)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        androidx.compose.ui.graphics.Color.White,
                        androidx.compose.ui.graphics.Color(0xFFE0E0E0).copy(alpha = 0.08f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // AI 动态大脸
            AppleAIFace(
                faceState = faceState,
                modifier = Modifier.size(170.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // AI 当前消息展示
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = displayedAIText,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    color = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.82f),
                    modifier = Modifier.padding(horizontal = 40.dp)
                )

                // 思考状态的打字三个点
                if (isTyping) {
                    TypingDots()
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // 用户消息气泡展示
            if (currentUserMessage.isNotEmpty()) {
                UserMessageBubble(
                    message = currentUserMessage,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            // 输入框
            ChatInputBar(
                text = userText,
                onTextChange = { userText = it },
                onSend = {
                    if (userText.trim().isNotEmpty()) {
                        currentUserMessage = userText
                        val prompt = userText
                        userText = ""
                        isTyping = true
                        faceState = AIFaceState.THINKING

                        scope.launch {
                            val response = aiService.sendMessage(prompt)
                            isTyping = false
                            faceState = AIFaceState.HAPPY
                            aiText = response
                        }
                    }
                }
            )
        }
    }
}
