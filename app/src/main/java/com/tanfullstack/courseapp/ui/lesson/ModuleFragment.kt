package com.tanfullstack.courseapp.ui.lesson

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentModuleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressRepo = ProgressRepository(requireContext())

        val moduleId = arguments?.getString("moduleId") ?: return
        val module = CourseData.getAllModules().find { it.id == moduleId } ?: return
        val progress = progressRepo.getProgress()

        binding.toolbar.title = module.title
        binding.toolbar.setNavigationIcon(android.R.drawable.ic_media_previous)
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

        binding.rvLessons.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLessons.adapter = LessonAdapter(
            lessons = module.lessons,
            completedLessons = progress.completedLessons,
            onClick = { lesson ->
                findNavController().navigate(
                    R.id.action_module_to_lesson,
                    bundleOf("lessonId" to lesson.id, "moduleId" to moduleId)
                )
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}