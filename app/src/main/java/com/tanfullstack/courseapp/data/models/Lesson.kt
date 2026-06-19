package com.tanfullstack.courseapp.data.models

data class Lesson(
    val id: String,
    val title: String,
    val markdownFile: String,   // filename in assets/lessons/
    val estimatedMinutes: Int,
    val hasQuiz: Boolean,
    val hasChallenge: Boolean,
    val order: Int
)