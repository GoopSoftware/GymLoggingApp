package com.dzl.gymloggingapp.logs

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dzl.gymloggingapp.databinding.FragmentViewLogsBinding
import com.dzl.gymloggingapp.dataclasses.WorkoutSession
import com.google.gson.Gson
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

class ViewLogsFragment : Fragment() {

    private lateinit var binding: FragmentViewLogsBinding
    private lateinit var logFileName: String
    private lateinit var currentSession: WorkoutSession


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logFileName = requireArguments().getString("log_file_name") ?: ""
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewLogsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadLogData()

        binding.textViewDate.text = currentSession.date
        binding.textViewWeekday.text = getDayOfWeek(currentSession.date)

        binding.textViewDate.setOnClickListener {
            showDatePicker()
        }
    }

    private fun loadLogData() {
        val file = File(requireContext().filesDir, "logs/$logFileName")

        if (!file.exists()) {
            return
        }

        val json = file.readText()
        currentSession = Gson().fromJson(json, WorkoutSession::class.java)
    }

    private fun getDayOfWeek(dateString: String): CharSequence? {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val date = sdf.parse(dateString)
            SimpleDateFormat("EEEE", Locale.US).format(date!!)
        } catch (e: Exception) {
            "Unknown Day"
        }
    }

    private fun showDatePicker() {
        val parts = currentSession.date.split("-").map { it.toInt() }
        val calender = Calendar.getInstance().apply {
            set(parts[0], parts[1] - 1, parts[2])
        }

        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val newDate = String.format("%04d-%02d-%02d", year, month + 1, day)
                handleDateChange(newDate)
            },
            calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun handleDateChange(newDate: String) {
        val oldFile = File(requireContext().filesDir, "logs/$logFileName")
        val newFile = File(requireContext().filesDir, "logs/$newDate.json")

        if (newFile.exists()) {
            Toast.makeText(requireContext(), "Log already exists for this date", Toast.LENGTH_SHORT).show()
            return
        }
        val renamed = oldFile.renameTo(newFile)

        if (renamed) {
            logFileName = "$newDate.json"
            currentSession = currentSession.copy(date = newDate)
            binding.textViewDate.text = newDate
            binding.textViewWeekday.text = getDayOfWeek(newDate)
        } else {
            // Create error toast
        }


    }

    companion object {
        fun newInstance(fileName: String) : ViewLogsFragment {
            val fragment = ViewLogsFragment()
            val args = Bundle().apply {
                putString("log_file_name", fileName)
            }
            fragment.arguments = args
            return fragment
        }
}


}