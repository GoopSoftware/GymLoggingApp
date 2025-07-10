package com.dzl.gymloggingapp.logs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dzl.gymloggingapp.databinding.ItemExerciseLogForLogsDialogBinding
import com.dzl.gymloggingapp.databinding.ItemExerciseLogForLogsFragmentBinding
import com.dzl.gymloggingapp.lifting.ExerciseLog

class PreviousExerciseLogAdapter(
    private val exercises: List<ExerciseLog>
) : RecyclerView.Adapter<PreviousExerciseLogAdapter.ExerciseViewHolder>(){

    inner class ExerciseViewHolder(val binding: ItemExerciseLogForLogsDialogBinding) :
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding = ItemExerciseLogForLogsDialogBinding.inflate(
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
            "No sets recorded"
        } else {
            exercise.sets.joinToString(separator = "\n") {
                "${it.weight} lbs x ${it.reps} reps"
            }
        }
    }

    override fun getItemCount(): Int = exercises.size
}