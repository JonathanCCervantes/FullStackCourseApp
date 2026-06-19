package com.tanfullstack.courseapp.ui.lesson

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tanfullstack.courseapp.R
import com.tanfullstack.courseapp.data.repository.CourseData
import com.tanfullstack.courseapp.data.repository.ProgressRepository
import com.tanfullstack.courseapp.databinding.FragmentLessonBinding
import com.tanfullstack.courseapp.utils.MarkdownLoader
import io.noties.markwon.Markwon

class LessonFragment : Fragment() {

    private var _binding: FragmentLessonBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressRepo: ProgressRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLessonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressRepo = ProgressRepository(requireContext())

        val lessonId = arguments?.getString("lessonId") ?: return
        val moduleId = arguments?.getString("moduleId") ?: return

        val module = CourseData.getAllModules().find { it.id == moduleId } ?: return
        val lesson = module.lessons.find { it.id == lessonId } ?: return

        binding.toolbar.title = lesson.title
        binding.toolbar.setNavigationIcon(android.R.drawable.ic_media_previous)
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

        // Render Markdown lesson content
        val markwon = Markwon.builder(requireContext()).build()
        val markdown = MarkdownLoader.loadFromAssets(requireContext(), lesson.markdownFile)
        markwon.setMarkdown(binding.tvLessonContent, markdown)

        // Status
        val isComplete = progressRepo.isLessonComplete(lessonId)
        binding.tvLessonStatus.text = if (isComplete) "✅ Lesson Complete" else ""
        binding.btnComplete.text = if (isComplete) "✅ Completed!" else "Mark Complete ✅"
        binding.btnComplete.alpha = if (isComplete) 0.6f else 1f

        // Quiz button
        binding.btnQuiz.isEnabled = lesson.hasQuiz
        binding.btnQuiz.alpha = if (lesson.hasQuiz) 1f else 0.4f
        binding.btnQuiz.setOnClickListener {
            findNavController().navigate(
                R.id.action_lesson_to_quiz,
                bundleOf("lessonId" to lessonId)
            )
        }

        // Challenge button
        binding.btnChallenge.isEnabled = lesson.hasChallenge
        binding.btnChallenge.alpha = if (lesson.hasChallenge) 1f else 0.4f
        binding.btnChallenge.setOnClickListener {
            findNavController().navigate(
                R.id.action_lesson_to_challenge,
                bundleOf("lessonId" to lessonId)
            )
        }

        // Mark complete
        binding.btnComplete.setOnClickListener {
            if (!progressRepo.isLessonComplete(lessonId)) {
                progressRepo.markLessonComplete(lessonId)
                binding.tvLessonStatus.text = "✅ Lesson Complete!"
                binding.btnComplete.text = "✅ Completed!"
                binding.btnComplete.alpha = 0.6f
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}