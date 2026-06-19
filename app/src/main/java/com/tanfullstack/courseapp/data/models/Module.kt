package com.tanfullstack.courseapp.data.models

data class Module(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,       // emoji icon e.g. "🌐"
    val color: String,      // hex color e.g. "#4CAF50"
    val lessons: List<Lesson>,
    val order: Int
)