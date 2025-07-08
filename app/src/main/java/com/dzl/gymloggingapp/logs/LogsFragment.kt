package com.dzl.gymloggingapp.logs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dzl.gymloggingapp.databinding.FragmentLogsBinding

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

}