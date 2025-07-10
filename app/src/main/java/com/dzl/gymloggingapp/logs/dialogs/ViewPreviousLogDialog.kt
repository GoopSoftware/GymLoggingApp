package com.dzl.gymloggingapp.logs.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.dzl.gymloggingapp.databinding.DialogViewPreviousLogBinding
import com.dzl.gymloggingapp.dataclasses.WorkoutSession
import com.dzl.gymloggingapp.lifting.ExercisesAdapterLogger
import com.dzl.gymloggingapp.logs.PreviousExerciseLogAdapter
import com.dzl.gymloggingapp.logs.WorkoutLogAdapter
import com.google.gson.Gson

class ViewPreviousLogDialog : DialogFragment() {


    companion object {
        private const val ARG_SESSION = "workout_session"

        fun newInstance(session: WorkoutSession) : ViewPreviousLogDialog {
            val args = Bundle()
            args.putString(ARG_SESSION, Gson().toJson(session))
            val fragment = ViewPreviousLogDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogViewPreviousLogBinding.inflate(LayoutInflater.from(context))

        val json = requireArguments().getString(ARG_SESSION)
        val session = Gson().fromJson(json, WorkoutSession::class.java)

        Log.d("Dialog", "Exercises loaded: ${session.exercises.size}")

        binding.textViewDate.text = session.date

        val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val localDate = java.time.LocalDate.parse(session.date, formatter)
        val dayOfWeek = localDate.dayOfWeek.toString().lowercase().replaceFirstChar { it.uppercase() }
        binding.textViewWeekday.text = dayOfWeek

        binding.recyclerViewLogs.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = PreviousExerciseLogAdapter(session.exercises)
        }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setPositiveButton("Close", null)
            .create()
    }


}