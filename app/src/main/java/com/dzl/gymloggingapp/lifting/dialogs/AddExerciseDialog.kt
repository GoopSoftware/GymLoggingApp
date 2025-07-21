package com.dzl.gymloggingapp.lifting.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSpinner

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzl.gymloggingapp.databinding.DialogAddCustomExerciseBinding
import com.dzl.gymloggingapp.databinding.DialogAddExerciseBinding
import com.dzl.gymloggingapp.lifting.CustomExerciseViewModel
import com.dzl.gymloggingapp.lifting.ExerciseListAdapter
import com.dzl.gymloggingapp.lifting.ExerciseSelectionViewModel

class AddExerciseDialog : DialogFragment() {

    private val viewModel: ExerciseSelectionViewModel by activityViewModels()
    private val customExerciseViewModel: CustomExerciseViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogAddExerciseBinding.inflate(LayoutInflater.from(context))
        customExerciseViewModel.loadExercises()

        var sortedExercises = customExerciseViewModel.exerciseList.sortedBy { it.lowercase() }

        val adapter = ExerciseListAdapter(sortedExercises) { selected ->
            viewModel.selectedExercise(selected)
            dismiss()
        }

        binding.recyclerViewExercises.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewExercises.adapter = adapter

        val customExerciseBinding =
            DialogAddCustomExerciseBinding.inflate(LayoutInflater.from(context))
        binding.buttonCustomExercise.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Custom Exercise")
                .setMessage("Enter the name of the new exercise: ")
                .setView(customExerciseBinding.root)
                .setPositiveButton("Add") { _, _ ->
                    val name = customExerciseBinding.editTextCustomExercise.text.toString().trim()
                    if (name.isNotEmpty()) {
                        customExerciseViewModel.addExercise(name)
                        sortedExercises =
                            customExerciseViewModel.exerciseList.sortedBy { it.lowercase() }
                        binding.recyclerViewExercises.adapter =
                            ExerciseListAdapter(sortedExercises) { selected ->
                                viewModel.selectedExercise(selected)
                                dismiss()
                            }
                        Toast.makeText(context, "$name added!", Toast.LENGTH_LONG).show()
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