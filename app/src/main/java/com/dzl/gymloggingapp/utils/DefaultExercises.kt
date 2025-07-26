package com.dzl.gymloggingapp.utils

data class ExercisePreset(
    val name: String,
    val primaryMuscleGroup: String
)

object DefaultExercises {
    val list = listOf(
        // Chest
        ExercisePreset("Bench Press", "Chest"),
        ExercisePreset("Incline Bench Press", "Chest"),
        ExercisePreset("Decline Bench Press", "Chest"),
        ExercisePreset("Dumbbell Bench Press", "Chest"),
        ExercisePreset("Chest Fly", "Chest"),
        ExercisePreset("Cable Crossover", "Chest"),

        // Shoulders
        ExercisePreset("Overhead Press", "Shoulders"),
        ExercisePreset("Dumbbell Shoulder Press", "Shoulders"),
        ExercisePreset("Arnold Press", "Shoulders"),
        ExercisePreset("Lateral Raise", "Shoulders"),
        ExercisePreset("Front Raise", "Shoulders"),
        ExercisePreset("Face Pull", "Shoulders"),

        // Back
        ExercisePreset("Barbell Row", "Back"),
        ExercisePreset("Dumbbell Row", "Back"),
        ExercisePreset("Seated Cable Row", "Back"),
        ExercisePreset("Lat Pulldown", "Back"),
        ExercisePreset("Pull-up", "Back"),
        ExercisePreset("Chin-up", "Back"),
        ExercisePreset("T-Bar Row", "Back"),

        // Biceps
        ExercisePreset("Bicep Curl", "Biceps"),
        ExercisePreset("Hammer Curl", "Biceps"),
        ExercisePreset("Concentration Curl", "Biceps"),
        ExercisePreset("Preacher Curl", "Biceps"),

        // Triceps
        ExercisePreset("Dips", "Triceps"),
        ExercisePreset("Tricep Pushdown", "Triceps"),
        ExercisePreset("Overhead Tricep Extension", "Triceps"),
        ExercisePreset("Skullcrusher", "Triceps"),

        // Legs
        ExercisePreset("Squat", "Legs"),
        ExercisePreset("Front Squat", "Legs"),
        ExercisePreset("Goblet Squat", "Legs"),
        ExercisePreset("Leg Press", "Legs"),
        ExercisePreset("Walking Lunge", "Legs"),
        ExercisePreset("Bulgarian Split Squat", "Legs"),
        ExercisePreset("Step-Up", "Legs"),

        // Hamstrings / Glutes
        ExercisePreset("Romanian Deadlift", "Hamstrings"),
        ExercisePreset("Sumo Deadlift", "Hamstrings"),
        ExercisePreset("Conventional Deadlift", "Hamstrings"),
        ExercisePreset("Hip Thrust", "Glutes"),
        ExercisePreset("Glute Bridge", "Glutes"),
        ExercisePreset("Leg Curl", "Hamstrings"),

        // Quads
        ExercisePreset("Leg Extension", "Quads"),

        // Calves
        ExercisePreset("Calf Raise", "Calves"),
        ExercisePreset("Seated Calf Raise", "Calves"),

        // Core
        ExercisePreset("Plank", "Core"),
        ExercisePreset("Cable Crunch", "Core"),
        ExercisePreset("Hanging Leg Raise", "Core"),
        ExercisePreset("Russian Twist", "Core"),
        ExercisePreset("Ab Wheel Rollout", "Core"),

        // Full Body / Olympic
        ExercisePreset("Power Clean", "Full Body"),
        ExercisePreset("Snatch", "Full Body"),
        ExercisePreset("Push Press", "Full Body"),
        ExercisePreset("Thruster", "Full Body"),
        ExercisePreset("Farmer's Carry", "Full Body"),
        ExercisePreset("Kettlebell Swing", "Full Body")
    )
}


