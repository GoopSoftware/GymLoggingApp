package com.dzl.gymloggingapp.logs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dzl.gymloggingapp.databinding.ItemLoggedExerciseBinding
import com.dzl.gymloggingapp.lifting.ExerciseLog

class LogExerciseAdapter(
    private val exercises: List<ExerciseLog>
) : RecyclerView.Adapter<LogExerciseAdapter.ExerciseViewHolder>() {

    inner class ExerciseViewHolder(val binding: ItemLoggedExerciseBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LogExerciseAdapter.ExerciseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLoggedExerciseBinding.inflate(inflater, parent, false)
        return ExerciseViewHolder(binding)
    }

    override fun getItemCount() = exercises.size


    override fun onBindViewHolder(holder: LogExerciseAdapter.ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.binding.textViewExerciseName.text = exercise.name

        val setsText = exercise.sets.joinToString("\n") {
            "- ${it.weight ?: "-"} lbs × ${it.reps ?: "-"} reps"
        }

        holder.binding.textViewSets.text = setsText
    }
}





