package com.tanfullstack.courseapp.ui.progress

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tanfullstack.courseapp.R
import com.tanfullstack.courseapp.data.repository.CourseData
import com.tanfullstack.courseapp.data.repository.ProgressRepository
import com.tanfullstack.courseapp.databinding.FragmentProgressBinding

class ProgressFragment : Fragment() {

    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressRepo: ProgressRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressRepo = ProgressRepository(requireContext())
        displayProgress()
    }

    override fun onResume() {
        super.onResume()
        displayProgress()
    }

    private fun displayProgress() {
        val progress = progressRepo.getProgress()
        binding.tvTotalXP.text = "⭐ ${progress.totalXP} XP"
        binding.tvLessonsCount.text = "${progress.completedLessons.size}"
        binding.tvQuizzesCount.text = "${progress.completedQuizzes.size}"
        binding.tvChallengesCount.text = "${progress.completedChallenges.size}"

        binding.moduleProgressContainer.removeAllViews()

        CourseData.getAllModules().forEach { module ->
            val completed = module.lessons.count { progress.completedLessons.contains(it.id) }
            val total = module.lessons.size
            val percent = if (total > 0) (completed * 100) / total else 0

            val row = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(0, 0, 0, 16)
            }

            val titleRow = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
            }

            val icon = TextView(requireContext()).apply {
                text = "${module.icon} ${module.title}"
                textSize = 14f
                setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTextPrimary))
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            val stat = TextView(requireContext()).apply {
                text = "$completed/$total"
                textSize = 13f
                setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTextSecondary))
            }

            titleRow.addView(icon)
            titleRow.addView(stat)

            val bar = ProgressBar(requireContext(), null, android.R.attr.progressBarStyleHorizontal).apply {
                max = 100
                this.progress = percent
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 12).also { p ->
                    p.topMargin = 8
                }
                progressTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.colorPrimary)
                )
            }

            row.addView(titleRow)
            row.addView(bar)
            binding.moduleProgressContainer.addView(row)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}