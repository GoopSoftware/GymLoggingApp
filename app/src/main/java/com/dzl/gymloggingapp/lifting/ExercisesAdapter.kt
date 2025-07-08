package com.dzl.gymloggingapp.lifting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dzl.gymloggingapp.databinding.ItemExerciseLogBinding

class ExercisesAdapter(private val exercises: List<ExerciseLog>) :
    RecyclerView.Adapter<ExercisesAdapter.ExerciseViewHolder>() {

    inner class ExerciseViewHolder(val binding: ItemExerciseLogBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding = ItemExerciseLogBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExerciseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.binding.textViewExerciseName.text = exercise.name
        holder.binding.textViewExerciseSets.text =
            exercise.sets.joinToString { "${it.weight}x${it.reps}" }

    }

    override fun getItemCount(): Int = exercises.size

}
