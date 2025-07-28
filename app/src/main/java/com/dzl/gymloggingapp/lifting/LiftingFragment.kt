package com.dzl.gymloggingapp.lifting

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.ContextThemeWrapper
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
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dzl.gymloggingapp.databinding.FragmentLiftingBinding
import com.dzl.gymloggingapp.dataclasses.WorkoutSession
import com.dzl.gymloggingapp.lifting.dialogs.FinishWorkoutDialog
import com.dzl.gymloggingapp.logs.WorkoutLogViewModel
import com.dzl.gymloggingapp.R
import com.dzl.gymloggingapp.databinding.DialogAddSetBinding
import com.google.android.material.chip.Chip
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
        setUpTemplateChips()
    }

    private fun setUpOnClickListeners() {
        binding.buttonFinishWorkout.setOnClickListener {
            launchFinishWorkoutDialog()
        }
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

    private fun setUpExerciseRecyclerView() {
        /*
        Initializes the RecyclerView that displays exercises for the current workout
        Uses an adapter (ExercisesAdapter) with a click listener for each.
        When an exercise is tapped it opens AddSetDialog to let the user add a set
        once a set is added (weight + reps) its inserted into the exercises list
        and the recyclerView updates just that item
         */

        // TODO: Un clutter this function by making helper functions

        // Set up the RecyclerView
        binding.recyclerViewExercises.layoutManager = LinearLayoutManager(requireContext())

        // Attach adapter and handle clicks to open AddSetDialog
        setUpAdapter()

        // This function controls the left and right swipe functionality of the exercises
        setUpSwipeControls()

    }

    private fun setUpAdapter() {
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
                launchAddExerciseFragment()
            },
            onAddSetClicked = { position ->
                launchAddSetDialog(position)
            }
        )
        binding.recyclerViewExercises.adapter = adapter
    }

    private fun setUpSwipeControls() {
        attachSwipeHandler(
            binding.recyclerViewExercises,
            onSwipeLeft = { position ->
                confirmDeletion(position)
            },
            onSwipeRight = { position ->
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
                adapter.notifyItemChanged(position)
            }
        )
    }


    private fun setUpTemplateChips() {
        val templatesDir = File(requireContext().filesDir, "templates")
        binding.templateChipGroup.removeAllViews()

        val newChip = Chip(
            ContextThemeWrapper(requireContext(), R.style.CustomChipStyle),
            null,
            com.google.android.material.R.attr.chipStyle
        ).apply {
            text = "+ New Template"
            isCheckable = false
            // TODO: Change this to a new template create window when its done
            val defaultBg = ContextCompat.getColor(context, R.color.darkGray)
            val defaultText = ContextCompat.getColor(context, R.color.lightGray)

            chipBackgroundColor = ColorStateList.valueOf(defaultBg)
            setTextColor(defaultText)
            setOnClickListener { promptTemplateNameAndSave() }
        }
        binding.templateChipGroup.addView(newChip)

        if (templatesDir.exists()) {
            templatesDir.listFiles()?.filter {it.extension == "json" }?.forEach { file ->
                val chip = Chip(requireContext()).apply {
                    text = file.nameWithoutExtension
                    isCheckable = false

                    val defaultBg = ContextCompat.getColor(context, R.color.darkGray)
                    val defaultText = ContextCompat.getColor(context, R.color.lightGray)

                    chipBackgroundColor = ColorStateList.valueOf(defaultBg)
                    setTextColor(defaultText)

                    setOnClickListener {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Load Template \"${file.nameWithoutExtension}\"?")
                            .setMessage("This will replace the current workout.")
                            .setPositiveButton("Load") { _, _ -> loadTemplateFromFile(file) }
                            .setNegativeButton("Cancel", null)
                            .show()
                    }
                }
                binding.templateChipGroup.addView(chip)
            }
        }

    }

    private fun confirmDeletion(position: Int) {
        val exerciseName = workoutLogViewModel.workoutExercises[position].name

        AlertDialog.Builder(requireContext())
            .setTitle("Delete $exerciseName?")
            .setMessage("Are you sure you want to delete?")
            .setPositiveButton("Delete") { _, _ ->
                workoutLogViewModel.workoutExercises.removeAt(position)
                adapter.notifyItemRemoved(position)
                Toast.makeText(context, "Exercise Removed", Toast.LENGTH_SHORT).show()

            }
            .setNegativeButton("Cancel") { _, _ ->
                adapter.notifyItemChanged(position)
            }
            .show()
    }


    private fun attachSwipeHandler(
        recyclerView: RecyclerView,
        onSwipeLeft: (position: Int) -> Unit,
        onSwipeRight: (position: Int) -> Unit
    ) {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                // Skips special rows (aka the green + add exercise button)
                if (viewHolder is ExercisesAdapterLogger.AddButtonViewHolder) {
                    recyclerView.adapter?.notifyItemChanged(position)
                    return
                }

                when (direction) {
                    ItemTouchHelper.LEFT -> onSwipeLeft(position)
                    ItemTouchHelper.RIGHT -> onSwipeRight(position)
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                drawSwipeBackground(c, recyclerView.context, viewHolder.itemView, dX)
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

            override fun getSwipeDirs(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return if (viewHolder is ExercisesAdapterLogger.AddButtonViewHolder) 0 else super.getSwipeDirs(
                    recyclerView,
                    viewHolder
                )
            }
        })

        itemTouchHelper.attachToRecyclerView(recyclerView)
    }


    private fun drawSwipeBackground(c: Canvas, context: Context, itemView: View, dX: Float) {
        val iconMargin = itemView.height / 4
        val editIcon = ContextCompat.getDrawable(context, R.drawable.icons8_edit_24)
        val deleteIcon = ContextCompat.getDrawable(context, R.drawable.icons8_delete_24)

        val background = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 48f
            setColor(if (dX > 0) Color.parseColor("#2196F3") else Color.parseColor("#F44336"))
        }

        if (dX > 0) {
            background.setBounds(
                itemView.left,
                itemView.top,
                itemView.left + dX.toInt(),
                itemView.bottom
            )
            background.draw(c)

            editIcon?.let {
                val iconTop = itemView.top + (itemView.height - it.intrinsicHeight) / 2
                val iconLeft = itemView.left + iconMargin
                val iconRight = iconLeft + it.intrinsicWidth
                val iconBottom = iconTop + it.intrinsicHeight

                if (dX > it.intrinsicWidth) {
                    it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    it.draw(c)
                }
            }
        } else if (dX < 0) {
            background.setBounds(
                itemView.right + dX.toInt(),
                itemView.top,
                itemView.right,
                itemView.bottom
            )
            background.draw(c)

            deleteIcon?.let {
                val iconTop = itemView.top + (itemView.height - it.intrinsicHeight) / 2
                val iconRight = itemView.right - iconMargin
                val iconLeft = iconRight - it.intrinsicWidth
                val iconBottom = iconTop + it.intrinsicHeight

                if (dX < -it.intrinsicWidth) {
                    it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    it.draw(c)
                }
            }
        }
    }

    private fun observeExerciseSelection() {
        /*
        Pull the selected exercise via the ExerciseSelectionViewModel.kt
        When a new exercise is selected from AddExerciseDialog
        it adds the exercise to the workout list and updates the recyclerview
         */
        exerciseViewModel.selectedExercise.observe(viewLifecycleOwner) { exerciseName ->
            if (exerciseName != null) {
                val alreadyExists = workoutLogViewModel.workoutExercises.any {
                    it.name.equals(exerciseName, ignoreCase = true)
                }

                if (alreadyExists) {
                    Toast.makeText(
                        context,
                        "$exerciseName is already in your workout.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Add selected exercise to workoutExercises list
                    workoutLogViewModel.workoutExercises.add(
                        ExerciseLog(
                            exerciseName,
                            mutableListOf()
                        )
                    )
                    Toast.makeText(context, "Exercise added", Toast.LENGTH_SHORT).show()
                }

                if (workoutLogViewModel.workoutExercises.size == 1) {
                    adapter.notifyDataSetChanged()
                } else {
                    adapter.notifyItemInserted(workoutLogViewModel.workoutExercises.lastIndex)
                }
                exerciseViewModel.clearSelection()
            }
        }
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
            Toast.makeText(
                requireContext(),
                "No logs in past $daysBack days",
                Toast.LENGTH_SHORT
            )
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
            Toast.makeText(requireContext(), "Failed to load log", Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
        }

    }

    private fun promptTemplateLoad() {
        val templatesDir = File(requireContext().filesDir, "templates")
        if (!templatesDir.exists() || templatesDir.listFiles().isNullOrEmpty()) {
            Toast.makeText(requireContext(), "No templates found", Toast.LENGTH_SHORT)
                .show()
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
            Toast.makeText(
                requireContext(),
                "Failed to load template",
                Toast.LENGTH_SHORT
            ).show()
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
                    Toast.makeText(
                        requireContext(),
                        "Template name required",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun saveWorkoutAsTemplate(templateName: String) {
        val exerciseNames = workoutLogViewModel.workoutExercises.map { it.name }
        if (exerciseNames.isEmpty()) {
            Toast.makeText(requireContext(), "No exercises to save", Toast.LENGTH_SHORT)
                .show()
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

        Toast.makeText(
            requireContext(),
            "Template \"$templateName\" saved!",
            Toast.LENGTH_SHORT
        )
            .show()
    }

    override fun onPause() {
        workoutLogViewModel.saveToFile()
        super.onPause()
    }

    private fun launchAddExerciseFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_content, AddExerciseFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun launchAddSetDialog(position: Int) {
        val context = requireContext()
        val exercise = workoutLogViewModel.workoutExercises[position]

        val binding = DialogAddSetBinding.inflate(layoutInflater)

        AlertDialog.Builder(context)
            .setTitle("Add Set to ${exercise.name}")
            .setView(binding.root)
            .setPositiveButton("Add") { _, _ ->
                val weight = binding.editTextWeight.text.toString().toFloatOrNull()
                val reps = binding.editTextReps.text.toString().toFloatOrNull()

                if (weight != null && reps != null) {
                    val set = SetEntry(weight, reps)
                    exercise.sets.add(set)
                    adapter.notifyItemChanged(position)
                    Toast.makeText(context, "Set Added!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        context,
                        "Enter valid weight and reps",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()

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
    var weight: Float? = null,
    var reps: Float? = null
)

data class WorkoutTemplate(
    val name: String,
    val exercises: List<String>
)