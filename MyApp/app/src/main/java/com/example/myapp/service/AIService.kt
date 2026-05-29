package com.example.myapp.service

import com.example.myapp.model.*
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

/**
 * AI 聊天服务 - 对接 DeepSeek API
 */
class AIService {

    companion object {
        private const val API_KEY = "sk-05eff166df34488e9ff3f38a96d6bc49"
        private const val MODEL = "deepseek-v4-flash"
        private const val BASE_URL = "https://api.deepseek.com/chat/completions"
        private const val JSON_MEDIA_TYPE = "application/json; charset=utf-8"
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    // 短期对话记忆
    private val messageHistory: MutableList<Message> = mutableListOf(
        Message(role = "system", content = "你是一个苹果风格的AI助手，语气自然、聪明、简洁。")
    )

    /**
     * 发送消息并获取 AI 回复
     */
    suspend fun sendMessage(messageText: String): String = withContext(Dispatchers.IO) {
        // 将用户消息加入历史
        messageHistory.add(Message(role = "user", content = messageText))

        val requestBody = ChatRequest(model = MODEL, messages = messageHistory)
        val jsonBody = gson.toJson(requestBody)

        val request = Request.Builder()
            .url(BASE_URL)
            .post(jsonBody.toRequestBody(JSON_MEDIA_TYPE.toMediaType()))
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $API_KEY")
            .build()

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (!response.isSuccessful || responseBody == null) {
                return@withContext "❌ 网络请求失败 (${response.code})"
            }

            val chatResponse = gson.fromJson(responseBody, ChatResponse::class.java)
            val replyContent = chatResponse.choices.firstOrNull()?.message?.content ?: ""

            // 将 AI 回复存入历史
            messageHistory.add(Message(role = "assistant", content = replyContent))

            replyContent
        } catch (e: Exception) {
            "❌ 错误：${e.localizedMessage}"
        }
    }
}
