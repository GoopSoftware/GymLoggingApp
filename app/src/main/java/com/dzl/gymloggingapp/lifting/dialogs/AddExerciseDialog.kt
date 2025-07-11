package com.dzl.gymloggingapp.lifting.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.dzl.gymloggingapp.databinding.DialogAddCustomExerciseBinding
import com.dzl.gymloggingapp.databinding.DialogAddExerciseBinding
import com.dzl.gymloggingapp.lifting.CustomExerciseViewModel
import com.dzl.gymloggingapp.lifting.ExerciseSelectionViewModel

class AddExerciseDialog : DialogFragment() {

    private val viewModel: ExerciseSelectionViewModel by activityViewModels()
    private val customExerciseViewModel: CustomExerciseViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogAddExerciseBinding.inflate(LayoutInflater.from(context))

        customExerciseViewModel.loadExercises()

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            customExerciseViewModel.exerciseList
        )

        //val exercises = listOf("Bench Press", "Dead Lift", "Squat")
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerExercise.adapter = adapter

        binding.buttonAddExercise.setOnClickListener {
            val selected = binding.spinnerExercise.selectedItem.toString()
            viewModel.selectedExercise(selected)
            dismiss()
        }

        val customBinding = DialogAddCustomExerciseBinding.inflate(LayoutInflater.from(context))
        binding.buttonCustomExercise.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Custom Exercise")
                .setMessage("Enter the name of the new exercise: ")
                .setView(customBinding.root)
                .setPositiveButton("Add") { _, _ ->
                    val name = customBinding.editTextCustomExercise.text.toString().trim()
                    if (name.isNotEmpty()) {
                        customExerciseViewModel.addExercise(name)
                        adapter.notifyDataSetChanged()
                        Toast.makeText(context, "$name added!", Toast.LENGTH_LONG ).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }


        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle("Add Exercise")
            .create()
    }
}