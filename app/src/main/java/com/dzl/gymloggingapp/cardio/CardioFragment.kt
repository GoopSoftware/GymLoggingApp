package com.dzl.gymloggingapp.cardio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dzl.gymloggingapp.databinding.FragmentCardioBinding

class CardioFragment : Fragment() {

    private lateinit var binding: FragmentCardioBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCardioBinding.inflate(inflater, container, false)

        return binding.root
    }


}