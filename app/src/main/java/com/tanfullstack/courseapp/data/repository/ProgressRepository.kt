package com.tanfullstack.courseapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.tanfullstack.courseapp.data.models.UserProgress

class ProgressRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_progress", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getProgress(): UserProgress {
        val json = prefs.getString("progress", null) ?: return UserProgress()
        return gson.fromJson(json, UserProgress::class.java)
    }

    fun saveProgress(progress: UserProgress) {
        prefs.edit().putString("progress", gson.toJson(progress)).apply()
    }

    fun markLessonComplete(lessonId: String) {
        val progress = getProgress()
        progress.completedLessons.add(lessonId)
        progress.totalXP += 50
        saveProgress(progress)
    }

    fun markQuizComplete(quizId: String, score: Int) {
        val progress = getProgress()
        progress.completedQuizzes.add(quizId)
        progress.quizScores[quizId] = score
        progress.totalXP += score * 10
        saveProgress(progress)
    }

    fun markChallengeComplete(challengeId: String) {
        val progress = getProgress()
        progress.completedChallenges.add(challengeId)
        progress.totalXP += 100
        saveProgress(progress)
    }

    fun isLessonComplete(lessonId: String): Boolean {
        return getProgress().completedLessons.contains(lessonId)
    }

    fun isQuizComplete(quizId: String): Boolean {
        return getProgress().completedQuizzes.contains(quizId)
    }

    fun isChallengeComplete(challengeId: String): Boolean {
        return getProgress().completedChallenges.contains(challengeId)
    }
}