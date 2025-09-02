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
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale

class ViewLogsFragment : Fragment() {

    private lateinit var binding: FragmentViewLogsBinding
    private lateinit var logFileName: String
    private lateinit var currentSession: WorkoutSession


    companion object {
        private const val ARG_LOG_FILENAME = "log_file_name"

        fun newInstance(logFileName: String): ViewLogsFragment {
            val fragment = ViewLogsFragment()
            val args = Bundle()
            args.putString(ARG_LOG_FILENAME, logFileName)
            fragment.arguments = args
            return fragment
        }
    }

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

        binding.textViewDate.setOnClickListener{
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



    }





}