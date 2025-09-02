package com.dzl.gymloggingapp.logs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dzl.gymloggingapp.databinding.FragmentViewLogsBinding
import com.dzl.gymloggingapp.dataclasses.WorkoutSession
import com.google.gson.Gson
import java.io.File
import java.time.LocalDate

class ViewLogsFragment : Fragment() {

    private lateinit var binding: FragmentViewLogsBinding
    private lateinit var logFileName: String
    private lateinit var workoutSession: WorkoutSession




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewLogsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun loadLog() {
        val logFile = File(requireContext().filesDir, "logs/$logFileName")
        val json = logFile.readText()
        workoutSession = Gson().fromJson(json, WorkoutSession::class.java)
    }

    private fun populateUI() {
        val date = workoutSession.date
        binding.textViewDate.text = date
        binding.textViewWeekday.text = LocalDate.parse(date).dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }

        val adapter = LogExerciseAdapter(workoutSession.exercises)
    }



}