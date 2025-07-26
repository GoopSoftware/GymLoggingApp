data class ExercisePreset(
    val name: String,
    val primaryMuscleGroup: List<String>
)

object DefaultExercises {
    val list = listOf(
        // Chest
        ExercisePreset("Bench Press", listOf("Chest", "Triceps", "Shoulders")),
        ExercisePreset("Incline Bench Press", listOf("Chest", "Triceps", "Shoulders")),
        ExercisePreset("Decline Bench Press", listOf("Chest", "Triceps")),
        ExercisePreset("Dumbbell Bench Press", listOf("Chest", "Triceps", "Shoulders")),
        ExercisePreset("Chest Fly", listOf("Chest")),
        ExercisePreset("Cable Crossover", listOf("Chest")),

        // Shoulders
        ExercisePreset("Overhead Press", listOf("Shoulders", "Triceps")),
        ExercisePreset("Dumbbell Shoulder Press", listOf("Shoulders", "Triceps")),
        ExercisePreset("Arnold Press", listOf("Shoulders", "Triceps")),
        ExercisePreset("Lateral Raise", listOf("Shoulders")),
        ExercisePreset("Front Raise", listOf("Shoulders")),
        ExercisePreset("Face Pull", listOf("Rear Delts", "Upper Back")),

        // Back
        ExercisePreset("Barbell Row", listOf("Back", "Lats")),
        ExercisePreset("Dumbbell Row", listOf("Back", "Lats")),
        ExercisePreset("Seated Cable Row", listOf("Back", "Lats")),
        ExercisePreset("Lat Pulldown", listOf("Lats", "Back", "Biceps")),
        ExercisePreset("Pull-up", listOf("Lats", "Back", "Biceps")),
        ExercisePreset("Chin-up", listOf("Lats", "Back", "Biceps")),
        ExercisePreset("T-Bar Row", listOf("Back", "Lats")),

        // Biceps
        ExercisePreset("Bicep Curl", listOf("Biceps")),
        ExercisePreset("Hammer Curl", listOf("Biceps", "Forearms")),
        ExercisePreset("Concentration Curl", listOf("Biceps")),
        ExercisePreset("Preacher Curl", listOf("Biceps")),

        // Triceps
        ExercisePreset("Dips", listOf("Triceps", "Chest", "Shoulders")),
        ExercisePreset("Tricep Pushdown", listOf("Triceps")),
        ExercisePreset("Overhead Tricep Extension", listOf("Triceps")),
        ExercisePreset("Skullcrusher", listOf("Triceps")),

        // Legs
        ExercisePreset("Squat", listOf("Quads", "Glutes", "Hamstrings")),
        ExercisePreset("Front Squat", listOf("Quads", "Glutes")),
        ExercisePreset("Goblet Squat", listOf("Quads", "Glutes")),
        ExercisePreset("Leg Press", listOf("Quads", "Glutes", "Hamstrings")),
        ExercisePreset("Walking Lunge", listOf("Quads", "Glutes", "Hamstrings")),
        ExercisePreset("Bulgarian Split Squat", listOf("Quads", "Glutes")),
        ExercisePreset("Step-Up", listOf("Quads", "Glutes")),

        // Hamstrings / Glutes
        ExercisePreset("Romanian Deadlift", listOf("Hamstrings", "Glutes", "Back")),
        ExercisePreset("Sumo Deadlift", listOf("Hamstrings", "Glutes", "Back")),
        ExercisePreset("Conventional Deadlift", listOf("Hamstrings", "Glutes", "Back")),
        ExercisePreset("Hip Thrust", listOf("Glutes", "Hamstrings")),
        ExercisePreset("Glute Bridge", listOf("Glutes", "Hamstrings")),
        ExercisePreset("Leg Curl", listOf("Hamstrings")),

        // Quads
        ExercisePreset("Leg Extension", listOf("Quads")),

        // Calves
        ExercisePreset("Calf Raise", listOf("Calves")),
        ExercisePreset("Seated Calf Raise", listOf("Calves")),

        // Core
        ExercisePreset("Plank", listOf("Core")),
        ExercisePreset("Cable Crunch", listOf("Core")),
        ExercisePreset("Hanging Leg Raise", listOf("Core")),
        ExercisePreset("Russian Twist", listOf("Core", "Obliques")),
        ExercisePreset("Ab Wheel Rollout", listOf("Core")),

        // Full Body / Olympic
        ExercisePreset("Power Clean", listOf("Full Body")),
        ExercisePreset("Snatch", listOf("Full Body")),
        ExercisePreset("Push Press", listOf("Full Body", "Shoulders", "Triceps")),
        ExercisePreset("Thruster", listOf("Full Body", "Quads", "Shoulders")),
        ExercisePreset("Farmer's Carry", listOf("Full Body", "Forearms", "Traps")),
        ExercisePreset("Kettlebell Swing", listOf("Full Body", "Glutes", "Hamstrings"))
    )
}
