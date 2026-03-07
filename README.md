# Gym Logging App

Android workout tracking application built with **Kotlin and Android Studio**.

This app allows users to log workouts, track sets and reps, and store workout history locally using JSON-based persistence. The project focuses on building a practical Android application while exploring fragment-based navigation, RecyclerView interfaces, and lightweight file-based data storage.

---

# Features

## Workout Tracking

- Create workouts by adding exercises
- Log multiple sets per exercise
- Record **weight and reps for each set**
- Edit or delete sets during a workout

## Exercise Management

- Add exercises to a workout from a selection list
- Support for both **default and custom exercises**

## Workout History

- Completed workouts are saved as **JSON files**
- Logs are stored locally in the device's internal storage
- Users can view previously completed workouts

## Persistence System

The application maintains workout progress using a temporary save system.

- Current workout state is stored in `temp_workout.json`
- Prevents data loss if the app is closed during a workout
- Finished workouts are saved as dated log files

Example log storage:
logs/
2025-07-23.json
2025-07-25.json
---

# Architecture

The application uses a **single-activity architecture** with fragment-based navigation.

## Main Components

### MainActivity

Handles bottom navigation and fragment switching.

### LiftingFragment

The primary workout logging screen where users:

- add exercises
- log sets
- edit sets
- finish workouts

### LogsFragment

Displays previously completed workouts loaded from JSON log files.

### WorkoutLogViewModel

Maintains the current workout state in memory and handles temporary persistence during active workouts.

---
# Data Model

Example structure used for saving workouts:
WorkoutSession
├─ date
└─ exercises
└─ ExerciseLog
├─ name
└─ sets
└─ SetEntry
├─ weight
└─ reps

---

# Technologies Used

- Kotlin
- Android SDK
- Fragments
- RecyclerView
- ViewBinding
- Gson (JSON serialization)

---

# Design Decisions

## File-Based Storage Instead of a Database

This application intentionally avoids using Room or SQLite.  
Instead, workouts are stored as **JSON files**.

Benefits:

- Easy debugging
- Human-readable logs
- No database schema management
- Lightweight persistence

## Temporary Workout Recovery

To prevent losing workout progress:

1. Current workout state is stored in `temp_workout.json`
2. The file is updated during the workout
3. When the workout finishes, it is moved to the logs directory

---

# Future Improvements

Planned improvements include:

- Exercise search and filtering
- Drag-and-drop exercise reordering
- Template-based workouts
- Detailed workout log viewer
- UI and animation improvements

---

# Project Status

This project is **actively under development** as part of ongoing Android development practice and software engineering exploration.

---

# Author

**GoopSoftware**  
Computer Science Student 

GitHub:  
https://github.com/GoopSoftware
