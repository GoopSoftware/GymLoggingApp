package com.dzl.gymloggingapp.lifting

/**
 * Represents a single set entry with weight and reps.
 * Uses nullable Int to handle missing/uninitialized values.
 */
data class SetEntry(
    val weight: Int? = null,
    val reps: Int? = null
)

/**
 * Represents an exercise with its name and list of sets.
 * The sets list is mutable to allow adding sets during workout logging.
 */
data class ExerciseLog(
    val name: String,
    val sets: MutableList<SetEntry> = mutableListOf()
)

/**
 * Represents a workout template that can be saved and loaded.
 * Contains only exercise names (not set data) since templates are
 * meant to be starting points for new workouts.
 */
data class WorkoutTemplate(
    val name: String,
    val exercises: List<String>
)
