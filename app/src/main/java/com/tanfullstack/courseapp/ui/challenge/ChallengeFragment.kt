package com.tanfullstack.courseapp.ui.challenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tanfullstack.courseapp.data.repository.ChallengeData
import com.tanfullstack.courseapp.data.repository.ProgressRepository
import com.tanfullstack.courseapp.databinding.FragmentChallengeBinding

class ChallengeFragment : Fragment() {

    private var _binding: FragmentChallengeBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressRepo: ProgressRepository
    private var hintsRevealed = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChallengeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressRepo = ProgressRepository(requireContext())

        val lessonId = arguments?.getString("lessonId") ?: return
        val challenge = ChallengeData.getChallengeForLesson(lessonId)

        if (challenge == null) {
            Toast.makeText(requireContext(), "No challenge available for this lesson yet.", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        binding.toolbar.title = "💻 Challenge"
        binding.toolbar.setNavigationIcon(android.R.drawable.ic_media_previous)
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

        binding.tvChallengeTitle.text = challenge.title
        binding.tvChallengeDesc.text = challenge.description
        binding.etUserCode.setText(challenge.starterCode)

        binding.btnHint.setOnClickListener {
            if (hintsRevealed < challenge.hints.size) {
                val hintView = TextView(requireContext()).apply {
                    text = challenge.hints[hintsRevealed]
                    setTextColor(resources.getColor(android.R.color.holo_orange_light, null))
                    textSize = 13f
                    setPadding(0, 8, 0, 8)
                }
                binding.hintsContainer.addView(hintView)
                hintsRevealed++
            } else {
                Toast.makeText(requireContext(), "No more hints!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSolution.setOnClickListener {
            binding.tvSolution.text = challenge.solution
            binding.solutionCard.visibility = View.VISIBLE
        }

        binding.btnSubmit.setOnClickListener {
            progressRepo.markChallengeComplete(challenge.id)
            Toast.makeText(requireContext(), "Challenge Complete! +100 XP 🏆", Toast.LENGTH_LONG).show()
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}