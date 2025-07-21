package com.dzl.gymloggingapp.logs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzl.gymloggingapp.databinding.FragmentLogsBinding
import com.dzl.gymloggingapp.dataclasses.WorkoutSession
import com.dzl.gymloggingapp.logs.dialogs.ViewPreviousLogDialog
import com.google.gson.Gson
import java.io.File

class LogsFragment : Fragment() {

    private lateinit var binding: FragmentLogsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpLogsRecyclerView()
    }

    private fun loadWorkoutLogs(): List<WorkoutSession> {
        val logsDir = File(requireContext().filesDir, "logs")
        val gson = Gson()
        val logs = mutableListOf<WorkoutSession>()

        if (!logsDir.exists()) return emptyList()

        logsDir.listFiles()?.forEach { file ->
            if (file.name.matches(Regex("\\d{4}-\\d{2}-\\d{2}\\.json"))) {
                val json = file.readText()
                try {
                    val session = gson.fromJson(json, WorkoutSession::class.java)
                    logs.add(session)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return logs.sortedByDescending { it.date }
    }


    private fun setUpLogsRecyclerView() {
        binding.recyclerViewLogs.layoutManager = LinearLayoutManager(requireContext())

        val logs = loadWorkoutLogs()

        binding.recyclerViewLogs.adapter = WorkoutLogAdapter(logs) { selectedLog ->
            launchLogsDialog(selectedLog)
        }
    }

    private fun launchLogsDialog(session: WorkoutSession) {
        val dialog = ViewPreviousLogDialog.newInstance(session)
        dialog.show(parentFragmentManager, "ViewPreviousLogDialog")
    }

}