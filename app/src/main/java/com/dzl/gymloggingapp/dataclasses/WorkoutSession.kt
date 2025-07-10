package com.dzl.gymloggingapp.dataclasses

import com.dzl.gymloggingapp.lifting.ExerciseLog

data class WorkoutSession(
    val date: String,
    val exercises: List<ExerciseLog>
)