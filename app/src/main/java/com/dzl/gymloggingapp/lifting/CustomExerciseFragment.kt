package com.dzl.gymloggingapp.lifting

import ExercisePreset
import android.graphics.Canvas
import android.graphics.Color

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dzl.gymloggingapp.R
import com.dzl.gymloggingapp.databinding.FragmentCustomExerciseBinding

class CustomExerciseFragment : Fragment() {

    private lateinit var binding: FragmentCustomExerciseBinding
    private val customExerciseViewModel: CustomExerciseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.customExerciseToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val adapter = ExerciseListAdapter(
            exercises = mutableListOf(),
            onExerciseSelected = {},
            onEdit = { showRenameDialog(it) },
            onDelete = { confirmDelete(it) },
            isCustom = { true }
        )

        binding.recyclerCustomExercises.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val exercise = adapter.getExerciseAt(position)

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        confirmDelete(exercise.name)
                        adapter.notifyItemChanged(position)
                    }

                    ItemTouchHelper.RIGHT -> {
                        showRenameDialog(exercise.name)
                        adapter.notifyItemChanged(position)
                    }
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
                val itemView = viewHolder.itemView
                val iconMargin = (itemView.height / 4)

                val editIcon =
                    ContextCompat.getDrawable(recyclerView.context, R.drawable.outline_edit_24)
                val deleteIcon =
                    ContextCompat.getDrawable(recyclerView.context, R.drawable.outline_delete_24)

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

                        if (dX > it.intrinsicWidth + iconMargin) {
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

                        if (dX < -it.intrinsicWidth - iconMargin) {
                            it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                            it.draw(c)
                        }
                    }
                }


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

        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerCustomExercises)



        customExerciseViewModel.loadExercises()

        customExerciseViewModel.exerciseList.observe(viewLifecycleOwner)
        { list ->
            val presets = list.map { ExercisePreset(it, listOf("Custom")) }
            adapter.updateExercises(presets)
        }

        binding.buttonCreateNewExercise.setOnClickListener {
            val input = EditText(requireContext())

            AlertDialog.Builder(requireContext())
                .setTitle("New Exercise")
                .setView(input)
                .setPositiveButton("Add") { _, _ ->
                    val name = input.text.toString().trim()

                    if (name.isEmpty()) {
                        Toast.makeText(context, "Name cannot be empty", Toast.LENGTH_SHORT)
                            .show()
                        return@setPositiveButton
                    }

                    val alreadyExists = (customExerciseViewModel.exerciseList.value?.any {
                        it.equals(
                            name,
                            true
                        )
                    } == true) ||
                            DefaultExercises.list.any { it.name.equals(name, true) }

                    if (alreadyExists) {
                        Toast.makeText(context, "$name already exists.", Toast.LENGTH_SHORT)
                            .show()
                        return@setPositiveButton
                    }


                    val added = customExerciseViewModel.addExercise(name)
                    if (added) {
                        Toast.makeText(context, "$name added!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Exercise already exists.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun confirmDelete(name: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete $name?")
            .setMessage("Are you sure you want to delete?")
            .setPositiveButton("Delete") { _, _ ->
                customExerciseViewModel.deleteExercise(name)
                Toast.makeText(context, "$name deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    private fun showRenameDialog(oldName: String) {
        val input = EditText(requireContext())
        input.setText(oldName)

        AlertDialog.Builder(requireContext())
            .setTitle("Rename Exercise")
            .setView(input)
            .setPositiveButton("Save") { _, _ ->
                val newName = input.text.toString().trim()
                if (customExerciseViewModel.renameExercise(oldName, newName)) {
                    Toast.makeText(context, "Renamed", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Name exists or invalid", Toast.LENGTH_SHORT).show()

                }

            }
            .setNegativeButton("Cancel", null)
            .show()


    }
}