package com.tanfullstack.courseapp.utils

import android.content.Context

object MarkdownLoader {
    fun loadFromAssets(context: Context, filename: String): String {
        return try {
            context.assets.open("lessons/$filename").bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            """
# Lesson Not Found
The file `$filename` could not be loaded.
Please ensure the markdown files are placed in `assets/lessons/` folder.
            """.trimIndent()
        }
    }
}