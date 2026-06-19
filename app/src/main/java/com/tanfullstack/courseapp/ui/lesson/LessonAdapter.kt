package com.tanfullstack.courseapp.ui.lesson

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tanfullstack.courseapp.data.models.Lesson
import com.tanfullstack.courseapp.databinding.ItemLessonBinding

class LessonAdapter(
    private val lessons: List<Lesson>,
    private val completedLessons: Set<String>,
    private val onClick: (Lesson) -> Unit
) : RecyclerView.Adapter<LessonAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemLessonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(lesson: Lesson, index: Int) {
            binding.tvLessonNumber.text = "${index + 1}"
            binding.tvLessonTitle.text = lesson.title
            binding.tvLessonTime.text = "⏱ ${lesson.estimatedMinutes} min"

            val badges = buildString {
                if (lesson.hasQuiz) append("📝 ")
                if (lesson.hasChallenge) append("💻")
            }
            binding.tvLessonBadges.text = badges.trim()

            val isComplete = completedLessons.contains(lesson.id)
            binding.tvLessonStatus.text = if (isComplete) "✅" else "▶"

            binding.root.setOnClickListener { onClick(lesson) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLessonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lessons[position], position)
    }

    override fun getItemCount() = lessons.size
}