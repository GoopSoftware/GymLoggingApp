package com.dzl.gymloggingapp.lifting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dzl.gymloggingapp.databinding.ItemExerciseTextBinding

class ExerciseListAdapter(
    private val exercises: MutableList<String>,
    private val onExerciseSelected: (String) -> Unit,
    private val onEdit: ((String) -> Unit)? = null,
    private val onDelete: ((String) -> Unit)? = null,
    private val isCustom: (String) -> Boolean = {false}
) : RecyclerView.Adapter<ExerciseListAdapter.ExerciseViewHolder>() {

    inner class ExerciseViewHolder(val binding: ItemExerciseTextBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding = ItemExerciseTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExerciseViewHolder(binding)
    }

    override fun getItemCount(): Int = exercises.size


    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val name = exercises[position]
        val binding = holder.binding

        binding.textViewExercise.text = name
        binding.root.setOnClickListener {
            onExerciseSelected(name)
        }

        val showControls = isCustom(name)

        // Enables the ability to hide or show the editing and deleting of the custom exercises. This allows
        // the use of this adapter for the add exercise dialog and the custom exercise fragment
        binding.buttonEdit.visibility = if (showControls && onEdit != null) View.VISIBLE else View.GONE
        binding.buttonDelete.visibility = if (showControls && onDelete != null) View.VISIBLE else View.GONE

        binding.buttonEdit.setOnClickListener {
            onEdit?.invoke(name)
        }

        binding.buttonDelete.setOnClickListener {
            onDelete?.invoke(name)
        }

    }

    fun updateExercises(newList: List<String>) {
        (exercises as MutableList).clear()
        (exercises as MutableList).addAll(newList)
        notifyDataSetChanged()
    }

}

