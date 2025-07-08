package com.dzl.gymloggingapp.lifting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzl.gymloggingapp.addexercise.AddExerciseDialog
import com.dzl.gymloggingapp.addexercise.ExerciseSelectionViewModel
import com.dzl.gymloggingapp.databinding.FragmentLiftingBinding

class LiftingFragment : Fragment() {

    private lateinit var binding: FragmentLiftingBinding
    private val exerciseViewModel: ExerciseSelectionViewModel by activityViewModels()

    private val workoutExercises = mutableListOf<ExerciseLog>()

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

        binding.recyclerViewExercises.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewExercises.adapter = ExercisesAdapter(workoutExercises)

        // Logic for when user adds a new exercise through the AddExerciseDialog.kt
        observeExerciseSelection()

        setUpOnClickListeners()

        //
        //displayExercises()
    }

    private fun observeExerciseSelection() {
        /*
        Pull the selected exercise from AddExerciseDialog.kt via the ExerciseSelectionViewModel.kt
        When a new exercise is selected from AddExerciseDialog
        it adds the exercise to the workout list, updates the recyclerview,
        and clears the selection to prevent duplicate entries
         */
        exerciseViewModel.selectedExercise.observe(viewLifecycleOwner) { exerciseName ->
            if (exerciseName != null) {
                // Add selected exercise to workoutExercises list
                workoutExercises.add(ExerciseLog(exerciseName, mutableListOf()))
                // Tells the RecyclerView that a new item was added
                binding.recyclerViewExercises.adapter?.notifyItemInserted(workoutExercises.lastIndex)
                // Clears the ViewModel so that duplicates arent added
                exerciseViewModel.clearSelection()
            }
        }
    }

    private fun setUpOnClickListeners() {
        binding.buttonAddExercise.setOnClickListener { launchExerciseDialog() }
    }


    private fun displayExercises() {
        for (exercise in workoutExercises) {
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
    }


    private fun launchExerciseDialog() {
        val dialog = AddExerciseDialog()
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