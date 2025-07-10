package com.dzl.gymloggingapp.lifting.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.dzl.gymloggingapp.databinding.DialogAddSetBinding

class AddSetDialog(
    private val onSetAdded: (weight: Int, reps: Int) -> Unit
) : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogAddSetBinding.inflate(LayoutInflater.from(context))

        val dialog = android.app.AlertDialog.Builder(requireContext())
            .setTitle("Add Set")
            .setView(binding.root)
            .create()

        binding.buttonAddSet.setOnClickListener {
            val weight = binding.editTextWeight.text.toString().toIntOrNull()
            val reps = binding.editTextReps.text.toString().toIntOrNull()

            if (weight != null && reps != null && weight >= 0 && reps >= 0) {
                onSetAdded(weight, reps)
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Enter valid numbers", Toast.LENGTH_LONG)
            }
        }

        return dialog
    }

}