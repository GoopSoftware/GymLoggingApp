package com.dzl.gymloggingapp.lifting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzl.gymloggingapp.databinding.FragmentLiftingBinding
import com.dzl.gymloggingapp.dataclasses.WorkoutSession
import com.dzl.gymloggingapp.lifting.dialogs.AddExerciseDialog
import com.dzl.gymloggingapp.lifting.dialogs.FinishWorkoutDialog
import com.dzl.gymloggingapp.logs.WorkoutLogViewModel
import com.dzl.gymloggingapp.R
import com.dzl.gymloggingapp.lifting.dialogs.EditExerciseDialog
import com.google.gson.Gson
import java.io.File
import java.time.LocalDate

class LiftingFragment : Fragment() {

    private lateinit var binding: FragmentLiftingBinding
    private lateinit var adapter: ExercisesAdapterLogger
    private val exerciseViewModel: ExerciseSelectionViewModel by activityViewModels()
    private val workoutLogViewModel: WorkoutLogViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLiftingBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolBar()
        // Loading the exercise list from file temp_workout.json
        workoutLogViewModel.loadFromFile()
        // Logic for Adding sets/reps to an exercise in the RecyclerView
        setUpExerciseRecyclerView()
        // Logic for when user adds a new exercise
        observeExerciseSelection()

        setUpOnClickListeners()
    }

    private fun setUpToolBar() {
        val activity = requireActivity() as AppCompatActivity

        // Create toolbar
        activity.setSupportActionBar(binding.liftingToolbar)
        // disable the app name
        activity.supportActionBar?.setDisplayShowTitleEnabled(false)
        // rebind after setting the toolbar
        activity.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.lifting_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.menu_clear_workout -> {
                            clearWorkout()
                            true
                        }

                        R.id.menu_create_template -> {
                            promptTemplateNameAndSave()
                            true
                        }

                        R.id.menu_load_template -> {
                            promptTemplateLoad()
                            true
                        }

                        R.id.menu_load_recent_log -> {
                            promptLogLoad()
                            true
                        }

                        R.id.menu_edit_custom_exercises -> {
                            promptEditCustomExercises()
                            true
                        }

                        else -> false
                    }
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
        // Not required but for app safety
        binding.liftingToolbar.invalidateMenu()
    }

    private fun promptEditCustomExercises() {
        Toast.makeText(context, "Coming soon!", Toast.LENGTH_LONG).show()
    }

    private fun showFilteredLogFilePicker(daysBack: Int) {

        val logsDir = File(requireContext().filesDir, "logs")

        if (!logsDir.exists() || logsDir.listFiles().isNullOrEmpty()) {
            Toast.makeText(requireContext(), "No logs found", Toast.LENGTH_SHORT).show()
            return
        }

        val cutOffDate = LocalDate.now().minusDays(daysBack.toLong())

        val validLogFiles = logsDir.listFiles()!!
            .filter { it.name.matches(Regex("""\d{4}-\d{2}-\d{2}\.json""")) }
            .filter {
                try {
                    val fileDate = LocalDate.parse(it.nameWithoutExtension)
                    !fileDate.isBefore(cutOffDate)
                } catch (e: Exception) {
                    false
                }
            }
            .sortedByDescending { it.name }

        if (validLogFiles.isEmpty()) {
            Toast.makeText(requireContext(), "No logs in past $daysBack days", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val displayNames = validLogFiles.map { it.nameWithoutExtension }.toTypedArray()

        AlertDialog.Builder(requireContext())
            .setTitle("Select a Workout Log")
            .setItems(displayNames) { _, which ->
                loadWorkoutLogAsTemplate(validLogFiles[which])
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun promptLogLoad() {

        val filterOptions = arrayOf("Past 30 days", "Past 90 Days")

        AlertDialog.Builder(requireContext())
            .setTitle("Filter Logs By")
            .setItems(filterOptions) { _, index ->
                val daysBack = if (index == 0) 30 else 90
                showFilteredLogFilePicker(daysBack)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun loadWorkoutLogAsTemplate(file: File) {

        try {
            val json = file.readText()
            val gson = Gson()

            val session = gson.fromJson(json, WorkoutSession::class.java)

            workoutLogViewModel.workoutExercises.clear()

            session.exercises.forEach { exerciseLog ->
                workoutLogViewModel.workoutExercises.add(
                    ExerciseLog(exerciseLog.name, mutableListOf())
                )
            }

            binding.recyclerViewExercises.adapter?.notifyDataSetChanged()

            Toast.makeText(
                requireContext(),
                "Workout from ${session.date} loaded as template",
                Toast.LENGTH_SHORT
            ).show()

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Failed to load log", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }

    }

    private fun promptTemplateLoad() {
        val templatesDir = File(requireContext().filesDir, "templates")
        if (!templatesDir.exists() || templatesDir.listFiles().isNullOrEmpty()) {
            Toast.makeText(requireContext(), "No templates found", Toast.LENGTH_SHORT).show()
            return
        }

        val templateFiles = templatesDir.listFiles()!!.filter { it.extension == "json" }

        val templateNames = templateFiles.map { it.nameWithoutExtension }.toTypedArray()

        AlertDialog.Builder(requireContext())
            .setTitle("Select Template")
            .setItems(templateNames) { _, which ->
                val selectedFile = templateFiles[which]
                loadTemplateFromFile(selectedFile)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun loadTemplateFromFile(file: File) {
        val json = file.readText()
        val gson = Gson()

        try {
            val template = gson.fromJson(json, WorkoutTemplate::class.java)

            workoutLogViewModel.workoutExercises.clear()

            template.exercises.forEach { exerciseName ->
                workoutLogViewModel.workoutExercises.add(
                    ExerciseLog(exerciseName, mutableListOf())
                )
            }

            binding.recyclerViewExercises.adapter?.notifyDataSetChanged()
            Toast.makeText(
                requireContext(),
                "Template \"${template.name}\" loaded",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Failed to load template", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun clearWorkout() {
        workoutLogViewModel.workoutExercises.clear()
        binding.recyclerViewExercises.adapter?.notifyDataSetChanged()
        Toast.makeText(requireContext(), "Workout Cleared", Toast.LENGTH_LONG)
            .show()
    }

    private fun promptTemplateNameAndSave() {
        val input = EditText(requireContext())
        input.hint = "e.g., Push Day, Upper 1, Chest"

        AlertDialog.Builder(requireContext())
            .setTitle("Save Workout as Template")
            .setView(input)
            .setPositiveButton("Save") { _, _ ->
                val name = input.text.toString().trim()
                if (name.isNotEmpty()) {
                    saveWorkoutAsTemplate(name)
                } else {
                    Toast.makeText(requireContext(), "Template name required", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun saveWorkoutAsTemplate(templateName: String) {
        val exerciseNames = workoutLogViewModel.workoutExercises.map { it.name }
        if (exerciseNames.isEmpty()) {
            Toast.makeText(requireContext(), "No exercises to save", Toast.LENGTH_SHORT).show()
            return
        }

        val template = WorkoutTemplate(name = templateName, exercises = exerciseNames)

        val gson = Gson()
        val json = gson.toJson(template)

        val templatesDir = File(requireContext().filesDir, "templates")
        if (!templatesDir.exists()) templatesDir.mkdirs()

        val safeFileName = templateName.replace("""[^\w\d_-]""".toRegex(), "_")
        val file = File(templatesDir, "$safeFileName.json")
        file.writeText(json)

        Toast.makeText(requireContext(), "Template \"$templateName\" saved!", Toast.LENGTH_SHORT)
            .show()
    }


    override fun onPause() {
        workoutLogViewModel.saveToFile()
        super.onPause()
    }


    private fun setUpExerciseRecyclerView() {
        /*
        Initializes the RecyclerView that displays exercises for the current workout
        Uses an adapter (ExercisesAdapter) with a click listener for each.
        When an exercise is tapped it opens AddSetDialog to let the user add a set
        once a set is added (weight + reps) its inserted into the exercises list
        and the recyclerView updates just that item
         */


        // Set up the RecyclerView
        binding.recyclerViewExercises.layoutManager = LinearLayoutManager(requireContext())

        // Attach adapter and handle clicks to open AddSetDialog
        adapter = ExercisesAdapterLogger(
            workoutLogViewModel.workoutExercises,
            onEditExerciseClicked = { position ->

                val exercise = workoutLogViewModel.workoutExercises[position]

                val bundle = Bundle().apply {
                    putString("exercise_name", exercise.name)
                    putString("sets_json", Gson().toJson(exercise.sets))
                    putInt("exercise_position", position)
                }

                parentFragmentManager.commit {
                    replace(R.id.frame_content, EditExerciseFragment::class.java, bundle)
                    addToBackStack("EditExercise")
                }
            },
            onAddExerciseClicked = {
                launchAddExerciseDialog()
            }
        )
        binding.recyclerViewExercises.adapter = adapter
    }

    private fun observeExerciseSelection() {
        /*
        Pull the selected exercise from AddExerciseDialog.kt via the ExerciseSelectionViewModel.kt
        When a new exercise is selected from AddExerciseDialog
        it adds the exercise to the workout list, updates the recyclerview,
        and clears the selection to prevent duplicate entries
         */
        exerciseViewModel.selectedExercise.observe(viewLifecycleOwner) { exerciseName ->
            if (exerciseName != null) {
                // Add selected exercise to workoutExercises list
                workoutLogViewModel.workoutExercises.add(
                    ExerciseLog(
                        exerciseName,
                        mutableListOf()
                    )
                )

                if (workoutLogViewModel.workoutExercises.size == 1) {
                    adapter.notifyDataSetChanged()
                } else {
                    adapter.notifyItemInserted(workoutLogViewModel.workoutExercises.lastIndex)
                }
                exerciseViewModel.clearSelection()
            }
        }
    }

    private fun setUpOnClickListeners() {
        //binding.buttonAddExercise.setOnClickListener { launchExerciseDialog() }
        binding.buttonFinishWorkout.setOnClickListener {
            launchFinishWorkoutDialog()
        }
    }

    private fun launchAddExerciseDialog() {
        val dialog = AddExerciseDialog()
        dialog.show(parentFragmentManager, "AddExerciseDialog")
    }

    private fun launchFinishWorkoutDialog() {
        val dialog = FinishWorkoutDialog(onConfirm = {
            saveWorkoutToFile()
        })
        dialog.show(parentFragmentManager, "AddExerciseDialog")
    }

    private fun saveWorkoutToFile() {
        /*
        This function will take the current mutable list workoutExercises and apply the date to
        the data class WorkoutSession() then apply that to a json file using Gson dependancy
        when the user clicks the finish workout button
         */
        val currentDate = LocalDate.now().toString()
        val session = WorkoutSession(currentDate, workoutLogViewModel.workoutExercises)

        val gson = Gson()
        val json = gson.toJson(session)

        val logsDir = File(requireContext().filesDir, "logs")
        if (!logsDir.exists()) logsDir.mkdirs()

        val file = File(logsDir, "$currentDate.json")
        file.writeText(json)

        workoutLogViewModel.clearTempFile()
        workoutLogViewModel.workoutExercises.clear()
        binding.recyclerViewExercises.adapter?.notifyDataSetChanged()

        Toast.makeText(
            context,
            "Workout Saved! Check 'Logs' to view",
            Toast.LENGTH_LONG
        ).show()

    }


}

data class ExerciseLog(
    val name: String,
    val sets: MutableList<SetEntry>
)

data class SetEntry(
    var weight: Int? = null,
    var reps: Int? = null
)

data class WorkoutTemplate(
    val name: String,
    val exercises: List<String>
)