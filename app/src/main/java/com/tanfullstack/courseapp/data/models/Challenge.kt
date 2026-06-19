package com.tanfullstack.courseapp.data.models

data class Challenge(
    val id: String,
    val lessonId: String,
    val title: String,
    val description: String,
    val starterCode: String,
    val hints: List<String>,
    val expectedOutputDescription: String,
    val solution: String
)