package com.tanfullstack.courseapp.data.models

data class Quiz(
    val id: String,
    val lessonId: String,
    val questions: List<QuizQuestion>
)

data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String
)