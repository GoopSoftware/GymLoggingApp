package com.dzl.gymloggingapp.lifting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzl.gymloggingapp.databinding.FragmentLiftingBinding
import com.dzl.gymloggingapp.dataclasses.WorkoutSession
import com.dzl.gymloggingapp.lifting.dialogs.AddExerciseDialog
import com.dzl.gymloggingapp.lifting.dialogs.AddSetDialog
import com.dzl.gymloggingapp.lifting.dialogs.FinishWorkoutDialog
import com.dzl.gymloggingapp.logs.WorkoutLogViewModel
import com.google.gson.Gson
import java.io.File
import java.time.LocalDate

class LiftingFragment : Fragment() {

    private lateinit var binding: FragmentLiftingBinding
    private lateinit var adapter: ExercisesAdapterLogger
    private val exerciseViewModel: ExerciseSelectionViewModel by activityViewModels()
    private val workoutLogViewModel: WorkoutLogViewModel by activityViewModels()


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

        // Loading the exercise list from file temp_workout.json
        workoutLogViewModel.loadFromFile()
        // Logic for Adding sets/reps to an exercise in the RecyclerView
        setUpExerciseRecyclerView()
        // Logic for when user adds a new exercise
        observeExerciseSelection()

        setUpOnClickListeners()
    }


    override fun onPause() {
        workoutLogViewModel.saveToFile()
        super.onPause()
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
        adapter =
            ExercisesAdapterLogger(
                workoutLogViewModel.workoutExercises,
                onAddSetClicked = { position ->
                    val dialog = AddSetDialog { weight, reps ->
                        workoutLogViewModel.workoutExercises[position].sets.add(
                            SetEntry(
                                weight,
                                reps
                            )
                        )
                        adapter.notifyItemChanged(position)
                    }
                    dialog.show(parentFragmentManager, "AddSetDialog")
                },
                onAddExerciseClicked = {
                    launchAddExerciseDialog()
                }
            )
        binding.recyclerViewExercises.adapter = adapter
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
                workoutLogViewModel.workoutExercises.add(ExerciseLog(exerciseName, mutableListOf()))

                if (workoutLogViewModel.workoutExercises.size == 1) {
                    adapter.notifyDataSetChanged()
                } else {
                    adapter.notifyItemInserted(workoutLogViewModel.workoutExercises.lastIndex)
                }
                exerciseViewModel.clearSelection()
            }
        }
    }

    private fun setUpOnClickListeners() {
        //binding.buttonAddExercise.setOnClickListener { launchExerciseDialog() }
        binding.buttonFinishWorkout.setOnClickListener {
            launchFinishWorkoutDialog()
        }
    }

    private fun launchAddExerciseDialog() {
        val dialog = AddExerciseDialog()
        dialog.show(parentFragmentManager, "AddExerciseDialog")
    }

    private fun launchFinishWorkoutDialog() {
        val dialog = FinishWorkoutDialog(onConfirm = {
            saveWorkoutToFile()
        })
        dialog.show(parentFragmentManager, "AddExerciseDialog")
    }

    private fun saveWorkoutToFile() {
        /*
        This function will take the current mutable list workoutExercises and apply the date to
        the data class WorkoutSession() then apply that to a json file using Gson dependancy
        when the user clicks the finish workout button
         */
        val currentDate = LocalDate.now().toString()
        val session = WorkoutSession(currentDate, workoutLogViewModel.workoutExercises)

        val gson = Gson()
        val json = gson.toJson(session)

        val filename = "$currentDate.json"
        val file = File(requireContext().filesDir, filename)

        file.writeText(json)

        workoutLogViewModel.clearTempFile()
        workoutLogViewModel.workoutExercises.clear()
        binding.recyclerViewExercises.adapter?.notifyDataSetChanged()

        Toast.makeText(context, "Workout Saved! Check 'Logs' to view", Toast.LENGTH_LONG).show()

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