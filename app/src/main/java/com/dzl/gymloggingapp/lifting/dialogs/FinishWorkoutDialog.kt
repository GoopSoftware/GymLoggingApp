package com.dzl.gymloggingapp.lifting.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.dzl.gymloggingapp.databinding.DialogFinishWorkoutBinding
import com.dzl.gymloggingapp.lifting.LiftingFragment

class FinishWorkoutDialog(private val onConfirm: () -> Unit)
    : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogFinishWorkoutBinding.inflate(LayoutInflater.from(context))

        binding.buttonConfirm.setOnClickListener {
            onConfirm()
            dismiss()
        }

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle("Finish Workout")
            .create()
    }

}