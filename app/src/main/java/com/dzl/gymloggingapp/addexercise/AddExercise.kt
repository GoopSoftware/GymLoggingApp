package com.dzl.gymloggingapp.addexercise

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.dzl.gymloggingapp.databinding.DialogAddExerciseBinding

class AddExercise : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogAddExerciseBinding.inflate(LayoutInflater.from(context))

        val exercises = listOf("Bench Press", "Dead Lift", "Squat")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, exercises)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerExercise.adapter = adapter


        binding.buttonCustomExercise.setOnClickListener { Toast.makeText(context, "Custom Exercise Coming Soon!", Toast.LENGTH_LONG ).show() }

        binding.buttonAddExercise.setOnClickListener { Toast.makeText(context, "Adding exercise", Toast.LENGTH_LONG).show() }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle("Add Exercise")
            .create()
    }
}