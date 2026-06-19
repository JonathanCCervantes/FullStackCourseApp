package com.tanfullstack.courseapp.ui.lesson

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tanfullstack.courseapp.R
import com.tanfullstack.courseapp.data.repository.CourseData
import com.tanfullstack.courseapp.data.repository.ProgressRepository
import com.tanfullstack.courseapp.databinding.FragmentModuleBinding

class ModuleFragment : Fragment() {

    private var _binding: FragmentModuleBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressRepo: ProgressRepository
    private var moduleId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentModuleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressRepo = ProgressRepository(requireContext())

        moduleId = arguments?.getString("moduleId")
        val currentModuleId = moduleId ?: return
        val module = CourseData.getAllModules().find { it.id == currentModuleId } ?: return
        
        binding.toolbar.title = module.title
        binding.toolbar.setNavigationIcon(android.R.drawable.ic_media_previous)
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

        // Setup Reset Progress Menu
        binding.toolbar.inflateMenu(R.menu.menu_module)
        binding.toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_reset_progress) {
                showResetConfirmation(module.lessons.map { it.id })
                true
            } else {
                false
            }
        }

        setupRecyclerView(module.lessons)
    }

    private fun setupRecyclerView(lessons: List<com.tanfullstack.courseapp.data.models.Lesson>) {
        val progress = progressRepo.getProgress()
        binding.rvLessons.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLessons.adapter = LessonAdapter(
            lessons = lessons,
            completedLessons = progress.completedLessons,
            onClick = { lesson ->
                findNavController().navigate(
                    R.id.action_module_to_lesson,
                    bundleOf("lessonId" to lesson.id, "moduleId" to moduleId)
                )
            }
        )
    }

    private fun showResetConfirmation(lessonIds: List<String>) {
        AlertDialog.Builder(requireContext())
            .setTitle("Reset Progress")
            .setMessage("Are you sure you want to reset your progress for this module?")
            .setPositiveButton("Reset") { _, _ ->
                progressRepo.resetModuleProgress(lessonIds)
                Toast.makeText(requireContext(), "Progress reset", Toast.LENGTH_SHORT).show()
                // Refresh UI
                moduleId?.let { id ->
                    val module = CourseData.getAllModules().find { it.id == id }
                    module?.let { setupRecyclerView(it.lessons) }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}