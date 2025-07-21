package com.dzl.gymloggingapp.lifting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dzl.gymloggingapp.databinding.ItemExerciseTextBinding

class ExerciseListAdapter(
    private val exercises: List<String>,
    private val onExerciseSelected: (String) -> Unit
) : RecyclerView.Adapter<ExerciseListAdapter.ExerciseViewHolder>() {

    inner class ExerciseViewHolder(val binding: ItemExerciseTextBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding = ItemExerciseTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExerciseViewHolder(binding)
    }

    override fun getItemCount(): Int = exercises.size


    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val name = exercises[position]
        holder.binding.textViewExercise.text = name
        holder.binding.root.setOnClickListener {
            onExerciseSelected(name)
        }
    }


}