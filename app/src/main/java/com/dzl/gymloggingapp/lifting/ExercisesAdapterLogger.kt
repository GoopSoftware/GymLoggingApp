package com.dzl.gymloggingapp.lifting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dzl.gymloggingapp.databinding.ItemExerciseLogForLiftingFragmentBinding
import com.dzl.gymloggingapp.databinding.ItemExerciseLogForLogsDialogBinding

class ExercisesAdapterLogger(
    private val exercises: List<ExerciseLog>,
    private val onAddSetClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<ExercisesAdapterLogger.ExerciseViewHolder>() {

    inner class ExerciseViewHolder(val binding: ItemExerciseLogForLiftingFragmentBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding = ItemExerciseLogForLiftingFragmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExerciseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
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
            onAddSetClicked(position)
        }


    }


    override fun getItemCount(): Int = exercises.size

}
