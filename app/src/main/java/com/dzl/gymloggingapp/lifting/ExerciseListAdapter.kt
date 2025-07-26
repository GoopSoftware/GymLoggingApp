package com.dzl.gymloggingapp.lifting

import ExercisePreset
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dzl.gymloggingapp.R
import com.dzl.gymloggingapp.databinding.ItemExerciseTextBinding

class ExerciseListAdapter(
    private val exercises: MutableList<ExercisePreset>,
    private val onExerciseSelected: (ExercisePreset) -> Unit,
    private val onEdit: ((String) -> Unit)? = null,
    private val onDelete: ((String) -> Unit)? = null,
    private val isCustom: (ExercisePreset) -> Boolean = { false },
    var onFavoriteToggled: (() -> Unit)? = null

) : RecyclerView.Adapter<ExerciseListAdapter.ExerciseViewHolder>() {

    inner class ExerciseViewHolder(val binding: ItemExerciseTextBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding =
            ItemExerciseTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExerciseViewHolder(binding)
    }

    override fun getItemCount(): Int = exercises.size


    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        val binding = holder.binding

        binding.textViewExercise.text = exercise.name

        binding.buttonFavorite.visibility = View.VISIBLE
        binding.buttonFavorite.setImageResource(
            if (exercise.isFavorite) R.drawable.baseline_favorite_24
            else R.drawable.baseline_favorite_border_24
        )
        binding.buttonFavorite.setColorFilter(
            binding.root.context.getColor(
                if (exercise.isFavorite) R.color.bottom_nav_selected else R.color.lightGray
            )
        )

        binding.buttonFavorite.setOnClickListener {
            exercise.isFavorite = !exercise.isFavorite
            notifyItemChanged(position)
            onFavoriteToggled?.invoke()
        }

        binding.root.setOnClickListener {
            onExerciseSelected(exercise)
        }

        val showControls = isCustom(exercise)

        // Enables the ability to hide or show the editing and deleting of the custom exercises. This allows
        // the use of this adapter for the add exercise dialog and the custom exercise fragment
        binding.buttonEdit.visibility =
            if (showControls && onEdit != null) View.VISIBLE else View.GONE
        binding.buttonDelete.visibility =
            if (showControls && onDelete != null) View.VISIBLE else View.GONE

        binding.buttonEdit.setOnClickListener {
            onEdit?.invoke(exercise.name)
        }

        binding.buttonDelete.setOnClickListener {
            onDelete?.invoke(exercise.name)
        }

    }

    fun updateExercises(newList: List<ExercisePreset>) {
        (exercises as MutableList).clear()
        (exercises as MutableList).addAll(newList)
        notifyDataSetChanged()
    }

}

