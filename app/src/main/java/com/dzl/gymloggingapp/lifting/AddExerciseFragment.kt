package com.dzl.gymloggingapp.lifting

import DefaultExercises
import ExercisePreset
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzl.gymloggingapp.R

import com.dzl.gymloggingapp.databinding.FragmentAddExerciseBinding
import com.google.android.material.chip.Chip

class AddExerciseFragment : Fragment() {

    private lateinit var binding: FragmentAddExerciseBinding
    private val selectionViewModel: ExerciseSelectionViewModel by activityViewModels()
    private val customExerciseViewModel: CustomExerciseViewModel by activityViewModels()

    private var allExercises = mutableListOf<ExercisePreset>()
    private var currentFilter = "All"
    private lateinit var adapter: ExerciseListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //setupToolBar()
        setupAdapter()
        setupSearchBar()
        setupFilterChips()

        binding.buttonCancel.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        customExerciseViewModel.loadExercises()
        customExerciseViewModel.exerciseList.observe(viewLifecycleOwner) { customList ->
            val customPresets = customList.map { ExercisePreset(it, listOf("Custom")) }
            allExercises = (DefaultExercises.list + customPresets).toMutableList()
            loadFavorites()
            applyFilters()
        }

        binding.buttonCustomExercise.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_content, CustomExerciseFragment())
                .addToBackStack(null)
                .commit()
        }

    }

    /*
    private fun setupToolBar() {
        binding.addExerciseToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
    */
    private fun setupAdapter() {
        adapter = ExerciseListAdapter(
            exercises = mutableListOf(),
            onExerciseSelected = {
                selectionViewModel.selectedExercise(it.name)
                requireActivity().onBackPressedDispatcher.onBackPressed()
            },
            onEdit = null,
            onDelete = null,
            isCustom = { it.primaryMuscleGroup.contains("Custom") }
        ).apply {
            onFavoriteToggled = { saveFavorites() }
        }
        binding.recyclerViewExercises.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewExercises.adapter = adapter
    }

    private fun setupSearchBar() {
        binding.editTextSearchBar.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) = applyFilters()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }


    private fun setupFilterChips() {
        val categories = listOf("All", "Favorites", "Custom") +
                DefaultExercises.list.flatMap { it.primaryMuscleGroup }.distinct().sorted()

        categories.forEachIndexed { index, category ->
            val chip = Chip(
                ContextThemeWrapper(requireContext(), R.style.CustomChipStyle),
                null,
                com.google.android.material.R.attr.chipStyle
            ).apply {
                text = category
                isCheckable = true
                isClickable = true
                isFocusable = true

                // Default Appearance
                val defaultBg = ContextCompat.getColor(context, if (index == 0) R.color.bottom_nav_selected else R.color.darkGray)
                val defaultText = ContextCompat.getColor(context, if (index == 0) R.color.white else R.color.lightGray)

                chipBackgroundColor = ColorStateList.valueOf(defaultBg)
                setTextColor(defaultText)
                isChecked = index == 0


                // Change background when selected
                setOnCheckedChangeListener { _, isChecked ->
                    chipBackgroundColor = ColorStateList.valueOf(
                        ContextCompat.getColor(context, if (isChecked) R.color.bottom_nav_selected else R.color.darkGray)
                    )
                    setTextColor(
                        ContextCompat.getColor(context, if (isChecked) R.color.white else R.color.lightGray)
                    )
                }

                setOnClickListener {
                    currentFilter = category
                    applyFilters()
                }
            }

            binding.filterChipGroup.addView(chip)
        }
    }

    private fun applyFilters() {
        val query = binding.editTextSearchBar.text.toString().trim().lowercase()
        val filtered = allExercises.filter { ex ->
            val matchesFilter = when (currentFilter) {
                "All" -> true
                "Favorites" -> ex.isFavorite
                "Custom" -> ex.primaryMuscleGroup.contains("Custom")
                else -> ex.primaryMuscleGroup.contains(currentFilter)
            }
            matchesFilter && ex.name.lowercase().contains(query)
        }
        adapter.updateExercises(filtered.sortedBy { it.name })
    }

    private fun saveFavorites() {
        val prefs = requireContext().getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val favNames = allExercises.filter { it.isFavorite }.map {it.name }.toSet()
        prefs.edit().putStringSet("favorites", favNames).apply()
    }

    private fun loadFavorites() {
        val prefs = requireContext().getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val favNames = prefs.getStringSet("favorites", emptySet()) ?: emptySet()
        allExercises.forEach { it.isFavorite = it.name in favNames }
    }

}