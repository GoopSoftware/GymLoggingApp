package com.dzl.gymloggingapp.logs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dzl.gymloggingapp.databinding.ItemExerciseLogForLogsDialogBinding
import com.dzl.gymloggingapp.databinding.ItemExerciseLogForLogsFragmentBinding
import com.dzl.gymloggingapp.dataclasses.WorkoutSession
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WorkoutLogAdapter(
    private val workoutSessions: List<WorkoutSession>,
    private val onLogClicked: (WorkoutSession) -> Unit
) :
    RecyclerView.Adapter<WorkoutLogAdapter.WorkoutLogViewHolder>() {

    inner class WorkoutLogViewHolder(val binding: ItemExerciseLogForLogsFragmentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutLogViewHolder {
        val binding = ItemExerciseLogForLogsFragmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WorkoutLogViewHolder(binding)
    }


    override fun onBindViewHolder(holder: WorkoutLogViewHolder, position: Int) {
        val session = workoutSessions[position]

        val dateText = "${getDayOfTheWeek(session.date)}\n\n${session.date} \n"

        // Format:
        // Monday
        // 2025-07-08
        holder.binding.textViewExerciseDate.text = dateText

        // Format: Bench Press: 135x8, 145x6
        // Squat: 185x5, 195x5
        val exercisesFormatted = session.exercises.joinToString("\n") { exercise ->
            val sets = exercise.sets.joinToString { "${it.weight}x${it.reps}" }
            "${exercise.name}: $sets"
        }
        holder.binding.textViewExerciseSummary.text = exercisesFormatted

        holder.binding.root.setOnClickListener {
            onLogClicked(session)
        }
    }

    override fun getItemCount(): Int = workoutSessions.size


    fun getDayOfTheWeek(dateString: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dateString, formatter)
        return date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
    }

}