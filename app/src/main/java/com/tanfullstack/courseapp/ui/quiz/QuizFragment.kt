package com.tanfullstack.courseapp.ui.quiz

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tanfullstack.courseapp.data.repository.QuizData
import com.tanfullstack.courseapp.data.repository.ProgressRepository
import com.tanfullstack.courseapp.databinding.FragmentQuizBinding
import com.tanfullstack.courseapp.databinding.ItemQuizOptionBinding

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressRepo: ProgressRepository
    private var currentQuestion = 0
    private var score = 0
    private var answered = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressRepo = ProgressRepository(requireContext())

        val lessonId = arguments?.getString("lessonId") ?: return
        val quiz = QuizData.getQuizForLesson(lessonId)

        if (quiz == null) {
            Toast.makeText(requireContext(), "No quiz available for this lesson yet.", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        fun showQuestion(index: Int) {
            answered = false
            val question = quiz.questions[index]
            val total = quiz.questions.size
            val progress = ((index.toFloat() / total.toFloat()) * 100).toInt()

            binding.quizProgressBar.progress = progress
            binding.tvQuestionCount.text = "${index + 1} / $total"
            binding.tvQuestion.text = question.question
            binding.explanationCard.visibility = View.GONE
            binding.btnNext.visibility = View.GONE
            binding.optionsContainer.removeAllViews()

            val letters = listOf("A", "B", "C", "D")

            question.options.forEachIndexed { i, option ->
                val optionBinding = ItemQuizOptionBinding.inflate(layoutInflater, binding.optionsContainer, true)
                optionBinding.tvOptionLetter.text = letters[i]
                optionBinding.tvOptionText.text = option

                optionBinding.optionCard.setOnClickListener {
                    if (answered) return@setOnClickListener
                    answered = true

                    val correct = i == question.correctAnswerIndex
                    if (correct) {
                        score++
                        optionBinding.optionCard.setCardBackgroundColor(Color.parseColor("#1A3FB950"))
                        optionBinding.tvOptionLetter.setTextColor(Color.parseColor("#3FB950"))
                    } else {
                        optionBinding.optionCard.setCardBackgroundColor(Color.parseColor("#1AF85149"))
                        optionBinding.tvOptionLetter.setTextColor(Color.parseColor("#F85149"))
                    }

                    binding.tvExplanation.text = "💡 ${question.explanation}"
                    binding.explanationCard.visibility = View.VISIBLE
                    binding.btnNext.visibility = View.VISIBLE

                    if (index + 1 >= total) {
                        binding.btnNext.text = "See Results 🎉"
                    } else {
                        binding.btnNext.text = "Next →"
                    }
                }
            }
        }

        showQuestion(0)

        binding.btnNext.setOnClickListener {
            currentQuestion++
            if (currentQuestion < quiz.questions.size) {
                showQuestion(currentQuestion)
            } else {
                // Quiz complete
                val total = quiz.questions.size
                progressRepo.markQuizComplete(quiz.id, score)
                Toast.makeText(
                    requireContext(),
                    "Quiz Complete! Score: $score/$total 🎉",
                    Toast.LENGTH_LONG
                ).show()
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}