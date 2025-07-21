package com.dzl.gymloggingapp.lifting

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.dzl.gymloggingapp.databinding.ItemAddExerciseButtonBinding
import com.dzl.gymloggingapp.databinding.ItemExerciseLogForLiftingFragmentBinding
import com.dzl.gymloggingapp.databinding.ItemExerciseLogForLogsDialogBinding

class ExercisesAdapterLogger(
    private val exercises: List<ExerciseLog>,
    private val onEditExerciseClicked: (position: Int) -> Unit,
    private val onAddExerciseClicked: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_EXERCISE = 0
    private val TYPE_ADD_BUTTON = 1

    inner class ExerciseViewHolder(val binding: ItemExerciseLogForLiftingFragmentBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class AddButtonViewHolder(val binding: ItemAddExerciseButtonBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return if (position == exercises.size) TYPE_ADD_BUTTON else TYPE_EXERCISE
    }

    override fun getItemCount(): Int = exercises.size + 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_EXERCISE) {
            val binding = ItemExerciseLogForLiftingFragmentBinding.inflate(
                LayoutInflater.from(
                parent.context),
                parent,
                false
            )
            ExerciseViewHolder(binding)
        } else {
            val binding = ItemAddExerciseButtonBinding.inflate(
                LayoutInflater.from(
                parent.context),
                parent,
                false
            )
            AddButtonViewHolder(binding)
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ExerciseViewHolder && position < exercises.size) {
            val exercise = exercises[position]
            holder.binding.textViewExerciseName.text = exercise.name
            holder.binding.textViewExerciseSets.text = if (exercise.sets.isEmpty()) {
                //--------------------------------------------------------------------------------
                // This is where we will create the logic to display the previous weeks sets/reps
                //--------------------------------------------------------------------------------
                "Press to add a set"
            } else {
                exercise.sets.joinToString { "${it.weight}x${it.reps}" }
            }

            holder.itemView.setOnClickListener {
                onEditExerciseClicked(position)
            }

        } else if (holder is AddButtonViewHolder) {
            holder.binding.textViewAddExercise.setOnClickListener {
                onAddExerciseClicked()
            }
        }
    }

}
