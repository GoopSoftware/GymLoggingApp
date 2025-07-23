package com.dzl.gymloggingapp.lifting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzl.gymloggingapp.R

import com.dzl.gymloggingapp.databinding.FragmentAddExerciseBinding

class AddExerciseFragment : Fragment() {

    private lateinit var binding: FragmentAddExerciseBinding
    private val selectionViewModel: ExerciseSelectionViewModel by activityViewModels()
    private val customExerciseViewModel: CustomExerciseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.addExerciseToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val adapter = ExerciseListAdapter(
            exercises = mutableListOf(),
            onExerciseSelected = {
                selectionViewModel.selectedExercise(it)
                requireActivity().onBackPressedDispatcher.onBackPressed()
            },
            onEdit = null,
            onDelete = null,
            isCustom = { false }
        )

        binding.recyclerViewExercises.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewExercises.adapter = adapter

        customExerciseViewModel.loadExercises()

        customExerciseViewModel.exerciseList.observe(viewLifecycleOwner) { list ->
            adapter.updateExercises(list.sortedBy { it.lowercase() })
        }

        binding.buttonCustomExercise.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_content, CustomExerciseFragment())
                .addToBackStack(null)
                .commit()
        }
    }


}