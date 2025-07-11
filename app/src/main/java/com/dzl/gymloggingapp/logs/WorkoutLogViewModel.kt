package com.dzl.gymloggingapp.logs

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.dzl.gymloggingapp.dataclasses.WorkoutSession
import com.dzl.gymloggingapp.lifting.ExerciseLog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.time.LocalDate

class WorkoutLogViewModel(application: Application) : AndroidViewModel(application) {

    val workoutExercises: MutableList<ExerciseLog> = mutableListOf()

    private val fileName = "temp_workout.json"
    private val file = File(application.filesDir, fileName)
    private val gson = Gson()

    fun saveToFile() {
        val session = WorkoutSession(LocalDate.now().toString(), workoutExercises)
        try {
            file.writeText(gson.toJson(session))
            Log.d("WorkoutLogViewModel", "Workout saved to $fileName")
        } catch (e: Exception) {
            Log.e("workoutLogViewModel", "Error saving workout", e)
        }
    }


    fun loadFromFile() {
        if (!file.exists()) {
            Log.d("WorkoutLogViewModel", "No temp file to load.")
            workoutExercises.clear()
            return
        }
        try {
            val json = file.readText()
            val session = gson.fromJson(json, WorkoutSession::class.java)
            workoutExercises.clear()
            workoutExercises.addAll(session.exercises)
            Log.d(
                "WorkoutLogViewModel",
                "Loaded ${session.exercises.size} exercises from temp file."
            )
        } catch (e: Exception) {
            Log.e("WorkoutLogViewModel", "Error loading temp file", e)
        }
    }


    fun clearTempFile() {
        if (file.exists()) {
            file.delete()
            Log.d("WorkoutLogViewModel", "Temp workout file deleted.")
        }
    }


}