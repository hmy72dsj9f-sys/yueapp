package com.example.myapp.model

/**
 * 聊天消息数据模型
 */
data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean
)

/**
 * AI 表情状态枚举
 */
enum class AIFaceState {
    HAPPY,
    THINKING,
    LISTENING,
    CONFUSED
}

// ==================== API 请求/响应模型 ====================

data class ChatRequest(
    val model: String,
    val messages: List<Message>
)

data class Message(
    val role: String,
    val content: String
)

data class ChatResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: ResponseMessage
)

data class ResponseMessage(
    val content: String
)
