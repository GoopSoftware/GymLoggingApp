package com.dzl.gymloggingapp.lifting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dzl.gymloggingapp.databinding.FragmentCustomExerciseBinding

class CustomExerciseFragment : Fragment() {

    private lateinit var binding: FragmentCustomExerciseBinding
    private val customExerciseViewModel: CustomExerciseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.customExerciseToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val adapter = ExerciseListAdapter(
            exercises = mutableListOf(),
            onExerciseSelected = {},
            onEdit = { showRenameDialog(it) },
            onDelete = { confirmDelete(it) },
            isCustom = { true }
        )

        binding.recyclerCustomExercises.adapter = adapter

        customExerciseViewModel.loadExercises()

        customExerciseViewModel.exerciseList.observe(viewLifecycleOwner) { list ->
            adapter.updateExercises(list)
        }

        binding.buttonCreateNewExercise.setOnClickListener {
            val input = EditText(requireContext())

            AlertDialog.Builder(requireContext())
                .setTitle("New Exercise")
                .setView(input)
                .setPositiveButton("Add") { _, _ ->
                    val name = input.text.toString().trim()
                    if (name.isNotEmpty()) {
                        customExerciseViewModel.addExercise(name)
                        Toast.makeText(context, "$name added!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun confirmDelete(name: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete $name?")
            .setMessage("This action cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                customExerciseViewModel.deleteExercise(name)
                Toast.makeText(context, "$name deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    private fun showRenameDialog(oldName: String) {
        val input = EditText(requireContext())
        input.setText(oldName)

        AlertDialog.Builder(requireContext())
            .setTitle("Rename Exercise")
            .setView(input)
            .setPositiveButton("Save") { _, _ ->
                val newName = input.text.toString().trim()
                if (customExerciseViewModel.renameExercise(oldName, newName)) {
                    Toast.makeText(context, "Renamed", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Name exists or invalid", Toast.LENGTH_SHORT).show()

                }

            }
            .setNegativeButton("Cancel", null)
            .show()


    }
}