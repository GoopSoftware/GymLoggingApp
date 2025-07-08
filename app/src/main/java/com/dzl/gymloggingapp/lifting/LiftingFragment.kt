package com.dzl.gymloggingapp.lifting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dzl.gymloggingapp.R
import com.dzl.gymloggingapp.addexercise.AddExercise
import com.dzl.gymloggingapp.databinding.FragmentLiftingBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class LiftingFragment : Fragment() {

    private lateinit var binding: FragmentLiftingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLiftingBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpOnClickListeners()

        val workout = listOf(
            ExerciseLog("Bench Press", mutableListOf(SetEntry(135, 8), SetEntry(135, 8), SetEntry(145, 6))),
            ExerciseLog("Squat", mutableListOf(SetEntry(185, 5), SetEntry(195, 5)))
        )

        for (exercise in workout) {
            val exerciseTitle = TextView(requireContext()).apply {
                text = exercise.name
                textSize = 18f
                setPadding(0, 16, 0, 4)
            }

            val setsText = exercise.sets.joinToString { "${it.weight}x${it.reps}" }
            val setsView = TextView(requireContext()).apply {
                text = setsText
                textSize = 14f
            }
        }

        binding.recyclerViewExercises.adapter = ExercisesAdapter(workout)
        binding.recyclerViewExercises.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())


    }

    private fun setUpOnClickListeners() {
        binding.buttonAddExercise.setOnClickListener { launchExerciseDialog() }
    }

    private fun launchExerciseDialog() {
        val dialog = AddExercise()
        dialog.show(parentFragmentManager, "AddExerciseDialog")
    }

}

data class ExerciseLog(
    val name: String,
    val sets: MutableList<SetEntry>
)

data class SetEntry(
    val weight: Int,
    val reps: Int
)