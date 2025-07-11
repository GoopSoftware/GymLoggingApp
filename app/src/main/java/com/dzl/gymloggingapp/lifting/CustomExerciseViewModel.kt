package com.dzl.gymloggingapp.lifting

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class CustomExerciseViewModel(application: Application) : AndroidViewModel(application) {
    /*
    This View Models job is to load and save the exercise list for the app. The user
    can add custom exercises which will be added to the list.
     */


    private val file = File(application.filesDir, "exercises.json")
    private val gson = Gson()

    val exerciseList: MutableList<String> = mutableListOf()

    // Says its never used?? However it's being used in AddExerciseDialog??
    fun loadExercises() {
        if (file.exists()) {
            val json = file.readText()
            val type = object : TypeToken<MutableList<String>>() {}.type
            val savedList: MutableList<String> = gson.fromJson(json, type)
            exerciseList.clear()
            exerciseList.addAll(savedList)
        } else {
            exerciseList.addAll(listOf("Bench Press", "Deadlift", "Squat"))
            saveExercises()
        }
    }

    fun addExercise(name: String) {
        if (name.isNotBlank() && name !in exerciseList) {
            exerciseList.add(name)
            saveExercises()
        }
    }

    private fun saveExercises() {
        file.writeText(gson.toJson(exerciseList))
    }


}