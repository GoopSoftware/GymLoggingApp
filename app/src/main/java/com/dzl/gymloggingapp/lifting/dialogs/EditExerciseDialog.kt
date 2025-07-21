package com.dzl.gymloggingapp.lifting.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzl.gymloggingapp.databinding.DialogEditExerciseBinding
import com.dzl.gymloggingapp.lifting.EditSetsAdapter
import com.dzl.gymloggingapp.lifting.SetEntry

class EditExerciseDialog(
    private val exerciseName: String,
    private val originalSets: MutableList<SetEntry>,
    private val onSave: (updatedSets: List<SetEntry>) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogEditExerciseBinding.inflate(LayoutInflater.from(context))
        val adapter = EditSetsAdapter(originalSets.toMutableList())

        binding.textViewExerciseName.text = exerciseName
        binding.recyclerViewSets.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewSets.adapter = adapter

        binding.buttonAddSet.setOnClickListener {
            adapter.addSet(SetEntry(weight = 0, reps = 0))
            binding.recyclerViewSets.post {
                binding.recyclerViewSets.smoothScrollToPosition(adapter.itemCount - 1)
            }
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()

        binding.buttonSaveChanges.setOnClickListener {
            onSave(adapter.getCurrentSets())
            dialog.dismiss()
        }

        return dialog

    }
}