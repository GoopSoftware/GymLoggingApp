# 🏋️ GymLoggingApp

A modern Android gym logging application built with Kotlin — track your workouts without the database bloat.

---

## Features

### Lifting Tracking
- Add exercises to a workout
- Add sets with weight and reps
- Edit individual sets or all sets of an exercise
- Swipe to edit exercises
- Tap to quickly add a set
- Drag to reorder exercises
- Floating point support for weight and reps

### Templates
- Create workout templates
- Create template from current workout
- Load templates into active workout
- Supports custom + default exercises

### Logs & History
- Workouts saved as JSON files
- Log viewer with detailed entries
- Editable workout dates
- Template name stored with workout
- View previous workout details

### UX
- Contextual tooltips (new users only)
- Confirmation dialogs for deletions
- Fragment animations
- Clean exercise card UI

---

## Tech Stack

| Category | Technology |
|----------|------------|
| Language | Kotlin |
| UI | XML layouts + ViewBinding |
| Navigation | Manual fragment transactions |
| Persistence | JSON files (Gson) |
| State | ViewModel + temp autosave |
| Min SDK | 33 (Android 13) |
| Target SDK | 35 |

---

## Architecture

### Data Flow

```
User Action → Fragment → ViewModel → JSON File
                ↓
         RecyclerView Adapter
```

### Storage Strategy

- **Active workout:** In-memory (ViewModel) → autosaved to `temp_workout.json`
- **Completed workouts:** `/files/logs/YYYY-MM-DD.json`

This hybrid approach provides crash recovery and fragment-safe state handling without a database.

### Package Structure

```
app/src/main/java/com/dzl/gymloggingapp/
├── MainActivity.kt
├── dataclasses/
│   └── WorkoutSession.kt
├── home/
│   └── HomeFragment.kt
├── lifting/
│   ├── LiftingFragment.kt
│   ├── ExerciseListAdapter.kt
│   ├── ExercisesAdapterLogger.kt
│   ├── EditExerciseFragment.kt
│   ├── CustomExerciseViewModel.kt
│   ├── ExerciseSelectionViewModel.kt
│   └── dialogs/
│       ├── AddExerciseDialog.kt
│       └── FinishWorkoutDialog.kt
├── cardio/
│   └── CardioFragment.kt
└── logs/
    ├── LogsFragment.kt
    ├── WorkoutLogAdapter.kt
    ├── PreviousExerciseLogAdapter.kt
    ├── WorkoutLogViewModel.kt
    └── dialogs/
        └── ViewPreviousLogDialog.kt
```

---

## Screens

| Screen | Description |
|--------|-------------|
| Home | Dashboard / welcome |
| Lifting | Main workout logging |
| Cardio | Cardio tracking |
| Logs | View past workouts |

Navigation: Bottom nav with 4 tabs. `MainActivity` hosts fragments in `frame_content`.

---

## Design Goals

- ⚡ **Fast** — Minimal friction for logging sets
- 🔧 **Maintainable** — Modular dialogs and fragments
- 📖 **Learnable** — Beginner-friendly codebase
- 🧱 **Scalable** — Foundation for future features

---

## Future Enhancements

- [ ] Personal progression tracking ("Grow This Dude")
- [ ] Analytics dashboard (volume per week)
- [ ] PR (personal record) tracking
- [ ] Cloud sync
- [ ] Export to CSV
- [ ] Dark mode polish

---

## Building

```bash
./gradlew assembleDebug
```

The APK outputs to `app/build/outputs/apk/debug/`.

---

## License

MIT
