package com.tanfullstack.courseapp.data.models

data class UserProgress(
    val completedLessons: MutableSet<String> = mutableSetOf(),
    val completedQuizzes: MutableSet<String> = mutableSetOf(),
    val completedChallenges: MutableSet<String> = mutableSetOf(),
    val quizScores: MutableMap<String, Int> = mutableMapOf(),
    var streakDays: Int = 0,
    var totalXP: Int = 0
)