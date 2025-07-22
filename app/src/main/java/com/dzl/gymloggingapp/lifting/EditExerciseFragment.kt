package com.dzl.gymloggingapp.lifting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dzl.gymloggingapp.databinding.FragmentEditExerciseBinding
import com.dzl.gymloggingapp.databinding.ItemEditSetBinding
import com.dzl.gymloggingapp.logs.WorkoutLogViewModel
import com.google.gson.Gson

class EditExerciseFragment : Fragment() {

    private lateinit var binding: FragmentEditExerciseBinding
    private val currentSets = mutableListOf<SetEntry>()
    private val workoutLogViewModel: WorkoutLogViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }


    private fun renderAllSetViews() {
        binding.linearLayoutSets.removeAllViews()

        currentSets.forEachIndexed { index, setEntry ->
            val setBinding = ItemEditSetBinding.inflate(layoutInflater)

            setBinding.editTextWeight.setText(setEntry.weight?.toString() ?: "")
            setBinding.editTextReps.setText(setEntry.reps?.toString() ?: "")

            setBinding.buttonDeleteSet.setOnClickListener {
                currentSets.removeAt(index)
                renderAllSetViews()
            }
            binding.linearLayoutSets.addView(setBinding.root)
        }
    }

    private fun collectSetInputs(): List<SetEntry> {
        val updatedSets = mutableListOf<SetEntry>()
        for (i in 0 until binding.linearLayoutSets.childCount) {
            val child = binding.linearLayoutSets.getChildAt(i)
            val itemBinding = ItemEditSetBinding.bind(child)

            val weight = itemBinding.editTextWeight.text.toString().trim().toIntOrNull()
            val reps = itemBinding.editTextReps.text.toString().trim().toIntOrNull()

            if (weight == null || reps == null) {
                Toast.makeText(requireContext(), "All fields must be filled before saving.", Toast.LENGTH_SHORT).show()
                return emptyList()
            }

            updatedSets.add(SetEntry(weight, reps))

        }
        return updatedSets
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val exerciseName = arguments?.getString("exercise_name") ?: return
        val setsJson = arguments?.getString("sets_json") ?: return
        val sets = Gson().fromJson(setsJson, Array<SetEntry>::class.java).toMutableList()

        currentSets.addAll(sets)
        binding.textViewExerciseName.text = exerciseName

        renderAllSetViews()

        binding.buttonAddSet.setOnClickListener {
            currentSets.add(SetEntry(null, null))
            renderAllSetViews()
        }

        binding.buttonSaveChanges.setOnClickListener {
            val updatedSets = collectSetInputs()
            if (updatedSets.isEmpty()) return@setOnClickListener

            val exerciseName = arguments?.getString("exercise_name") ?: return@setOnClickListener

            val target = workoutLogViewModel.workoutExercises.find {it.name == exerciseName}
            target?.let {
                it.sets.clear()
                it.sets.addAll(updatedSets)
                workoutLogViewModel.saveToFile()
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }


    }
}