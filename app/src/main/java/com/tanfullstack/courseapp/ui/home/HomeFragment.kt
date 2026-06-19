package com.tanfullstack.courseapp.ui.home

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
import com.tanfullstack.courseapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressRepo: ProgressRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressRepo = ProgressRepository(requireContext())
        setupUI()
    }

    override fun onResume() {
        super.onResume()
        setupUI()
    }

    private fun setupUI() {
        val progress = progressRepo.getProgress()
        binding.tvXP.text = "${progress.totalXP} XP"
        binding.progressBarXP.progress = (progress.totalXP % 1000)

        val modules = CourseData.getAllModules()
        binding.rvModules.layoutManager = LinearLayoutManager(requireContext())
        binding.rvModules.adapter = ModuleAdapter(
            modules = modules,
            completedLessons = progress.completedLessons,
            onClick = { module ->
                findNavController().navigate(
                    R.id.action_home_to_module,
                    bundleOf("moduleId" to module.id)
                )
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}