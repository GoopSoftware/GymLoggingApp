package com.dzl.gymloggingapp.lifting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzl.gymloggingapp.addexercise.AddExerciseDialog
import com.dzl.gymloggingapp.addexercise.AddSetDialog
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
        // Logic for Adding sets/reps to an exercise in the RecyclerView
        setUpExerciseRecyclerView()
        // Logic for when user adds a new exercise
        observeExerciseSelection()

        setUpOnClickListeners()
    }

    private fun setUpExerciseRecyclerView() {
        /*
        Initializes the RecyclerView that displays exercises for the current workout
        Uses an adapter (ExercisesAdapter) with a click listener for each.
        When an exercise is tapped it opens AddSetDialog to let the user add a set
        once a set is added (weight + reps) its inserted into the exercises list
        and the recyclerView updates just that item
         */

        // Set up the RecyclerView
        binding.recyclerViewExercises.layoutManager = LinearLayoutManager(requireContext())

        // Attach adapter and handle clicks to open AddSetDialog
        binding.recyclerViewExercises.adapter = ExercisesAdapter(workoutExercises) { position ->
            val dialog = AddSetDialog { weight, reps ->
                // Adds set (weight + reps)
                workoutExercises[position].sets.add(SetEntry(weight, reps))
                //Notify the recycler that its changed passing the position through
                binding.recyclerViewExercises.adapter?.notifyItemChanged(position)
            }
            dialog.show(parentFragmentManager, "AddSetDialog")
        }
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
        binding.buttonFinishWorkout.setOnClickListener { finishExercise() }
    }

    private fun finishExercise() {
        Toast.makeText(context, "Workout Finished, Great Job! You can view and edit this workout in the workout Logs", Toast.LENGTH_LONG).show()
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