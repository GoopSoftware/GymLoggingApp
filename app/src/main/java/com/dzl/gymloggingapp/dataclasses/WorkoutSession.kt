package com.dzl.gymloggingapp.dataclasses

import com.dzl.gymloggingapp.lifting.ExerciseLog

/**
 * Represents a complete workout session with a date and list of exercises.
 * The exercises list is nullable to handle deserialization edge cases gracefully.
 */
data class WorkoutSession(
    val date: String,
    val exercises: List<ExerciseLog>? = null
)
