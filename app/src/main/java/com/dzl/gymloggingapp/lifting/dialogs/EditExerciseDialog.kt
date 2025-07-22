package com.dzl.gymloggingapp.lifting.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzl.gymloggingapp.databinding.DialogEditExerciseBinding
import com.dzl.gymloggingapp.databinding.ItemEditSetBinding
import com.dzl.gymloggingapp.lifting.EditSetsAdapter
import com.dzl.gymloggingapp.lifting.SetEntry

class EditExerciseDialog(
    private val exerciseName: String,
    private val originalSets: MutableList<SetEntry>,
    private val onSave: (updatedSets: List<SetEntry>) -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogEditExerciseBinding
    private val currentSets = mutableListOf<SetEntry>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogEditExerciseBinding.inflate(LayoutInflater.from(context))
        currentSets.addAll(originalSets.map { it.copy() })

        binding.textViewExerciseName.text = exerciseName
        renderAllSetViews()

        binding.buttonAddSet.setOnClickListener {
            currentSets.add(SetEntry(0, 0))
            renderAllSetViews()
        }

        binding.buttonSaveChanges.setOnClickListener {
            val updatedSets = mutableListOf<SetEntry>()

            for (i in 0 until binding.linearLayoutSets.childCount) {
                val setView = binding.linearLayoutSets.getChildAt(i)
                val weightInput =
                    setView.findViewById<EditText>(com.dzl.gymloggingapp.R.id.edit_text_weight)
                val repsInput =
                    setView.findViewById<EditText>(com.dzl.gymloggingapp.R.id.edit_text_reps)

                val weight = weightInput.text.toString().toIntOrNull() ?: 0
                val reps = repsInput.text.toString().toIntOrNull() ?: 0

                updatedSets.add(SetEntry(weight, reps))
            }
            onSave(updatedSets)
            dismiss()
        }
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()

        dialog.setOnShowListener {
            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

            binding.linearLayoutSets.post {
                val firstInput = binding.linearLayoutSets
                    .getChildAt(0)
                    ?.findViewById<EditText>(com.dzl.gymloggingapp.R.id.edit_text_weight)
                firstInput?.let {
                    it.requestFocus()
                    val imm = requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                    imm.showSoftInput(it, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT)
                }

            }
        }

        return dialog

    }

    private fun renderAllSetViews() {
        binding.linearLayoutSets.removeAllViews()

        currentSets.forEachIndexed { index, setEntry ->
            val setBinding = ItemEditSetBinding.inflate(layoutInflater)

            setBinding.editTextWeight.setText(setEntry.weight.toString())
            setBinding.editTextReps.setText(setEntry.reps.toString())

            setBinding.buttonDeleteSet.setOnClickListener {
                currentSets.removeAt(index)
                renderAllSetViews()
            }

            binding.linearLayoutSets.addView(setBinding.root)
        }
    }
}



