# Lifting Log App

A modern Android gym logging application built with Kotlin.

This app allows users to track lifting workouts, manage exercises, create templates, and store workout history — all without using a database (JSON-based).

---

## Features

### Lifting Tracking
- Add exercises to a workout
- Add sets with weight and reps
- Edit individual sets
- Edit all sets of an exercise
- Swipe to edit exercises
- Tap to quickly add a set
- Drag to reorder exercises
- Floating point support for weight and reps

### Templates
- Create workout templates
- Create template from current workout
- Load templates into active workout
- Supports custom + default exercises

### Logs
- Workouts saved as JSON files
- Log viewer fragment
- Editable workout dates
- Detailed log view (ViewLogFragment)
- Template name stored with workout

### UX Improvements
- Contextual tooltips (shown only to new users)
- Confirmation dialogs for deletions
- Fragment animations
- Clean exercise card UI

---

## Architecture

### Core Structure

- **Language:** Kotlin
- **UI:** XML layouts with ViewBinding
- **Navigation:** Manual fragment transactions (no Navigation Component)
- **Persistence:** JSON files in internal storage
- **State Management:** ViewModel + temporary autosave file

### Main Fragments

- `HomeFragment`
- `LiftingFragment`
- `CardioFragment`
- `LogsFragment`
- `AddExerciseFragment`
- `EditSetsFragment`
- `TemplateCreationFragment`
- `ViewLogFragment`

### Navigation

`MainActivity` uses:

```
frame_content
```

as the fragment container.

Bottom navigation:
- Home
- Lifting
- Cardio
- Logs

---

## Data Persistence

This app does **not** use Room or SQLite.

Instead:

- Active workout is stored in memory (ViewModel)
- Autosaved to:  
  `temp_workout.json`
- Completed workouts saved to:  
  `/files/logs/YYYY-MM-DD.json`

This hybrid approach allows:

- Crash recovery
- Fragment-safe state handling
- Simple file-based architecture

---

## Project Structure (Simplified)

```
app/
│
├── data/
│   ├── ExercisePreset.kt
│   ├── WorkoutSession.kt
│
├── ui/
│   ├── fragments/
│   │   ├── LiftingFragment.kt
│   │   ├── AddExerciseFragment.kt
│   │   ├── EditSetsFragment.kt
│   │   ├── LogsFragment.kt
│   │   ├── ViewLogFragment.kt
│   │   └── TemplateCreationFragment.kt
│
├── adapters/
│   ├── ExercisesAdapter.kt
│   ├── WorkoutLogsAdapter.kt
│
└── MainActivity.kt
```

---

## Design Goals

- Clean UI
- Minimal friction for logging sets
- No over-engineering
- Modular dialogs/fragments
- Scalable architecture
- Beginner-friendly but extensible codebase

---

## Future Ideas

- Personal progression feature ("Grow This Dude")
- Analytics dashboard (volume per week)
- PR tracking
- Cloud sync
- Export to CSV
- Dark mode polish

---

## Tech Stack

- Kotlin
- Android SDK
- RecyclerView
- ViewBinding
- JSON Serialization
- Fragment transactions
- MVVM-lite architecture

---

## Project Vision

This app is being built as:

- A real-world Android development portfolio project
- A practical daily gym tool
- A scalable foundation for future features

It emphasizes clean architecture, state handling, and real UX iteration through field testing.
